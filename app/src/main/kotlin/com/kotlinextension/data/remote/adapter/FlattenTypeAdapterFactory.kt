package com.kotlinextension.data.remote.adapter

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.lang.reflect.Field
import java.lang.reflect.Type
import java.util.*

open class FlattenTypeAdapterFactory : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val delegateAdapter = gson.getDelegateAdapter(this, type)
        val defaultAdapter = gson.getAdapter(JsonElement::class.java)
        val cache = buildCache(type.rawType, gson)

        return object : TypeAdapter<T>() {
            private fun setElement(root: JsonObject, path: Array<String>, data: JsonElement) {
                var element: JsonElement? = root
                for (i in 0 until path.size - 1) {
                    // If the path element looks like a number..
                    var index: Int? = null
                    try {
                        index = Integer.valueOf(path[i])
                    } catch (ignored: NumberFormatException) {
                    }

                    // Get the next object in the chain if it exists already
                    var jsonElement: JsonElement? = null
                    if (element is JsonObject) {
                        jsonElement = element.get(path[i])
                    } else if (element is JsonArray && index != null) {
                        if (index >= 0 && index < element.size()) {
                            jsonElement = element.get(index)
                        }
                    } else {
                        // Failure. We can't walk any further - we don't know
                        // how to write this path. Maybe worth throwing exception?
                        continue
                    }

                    // Object didn't exist in the output already. Create it.
                    if (jsonElement == null) {
                        // The next element in the chain is an array
                        jsonElement = if (path[i + 1].matches("^\\d+$".toRegex())) {
                            JsonArray()
                        } else {
                            JsonObject()
                        }

                        if (element is JsonObject) {
                            element.add(path[i], jsonElement)
                        } else if (element is JsonArray && index != null) {
                            val array = element as JsonArray?
                            // Might need to pad the array out if we're writing an
                            // index that doesn't exist yet.
                            while (array!!.size() <= index) {
                                array.add(JsonNull.INSTANCE)
                            }
                            array.set(index, jsonElement)
                        }
                    }
                    element = jsonElement
                }

                if (element is JsonObject) {
                    element.add(path[path.size - 1], data)
                } else if (element is JsonArray) {
                    element.set(Integer.valueOf(path[path.size - 1]), data)
                }
            }

            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: T) {
                var res = delegateAdapter.toJsonTree(value)
                if (res.isJsonObject) {
                    val jsonElement = res.asJsonObject
                    for (cacheItem in cache) {
                        val data = jsonElement.get(cacheItem.name)
                        jsonElement.remove(cacheItem.name)
                        setElement(jsonElement, cacheItem.path, data)
                    }
                    res = jsonElement
                }
                gson.toJson(res, out)
            }

            @Throws(IOException::class)
            override fun read(jsonReader: JsonReader): T {
                if (cache.isEmpty())
                    return delegateAdapter.read(jsonReader)
                val rootElement = defaultAdapter.read(jsonReader)
                if (!rootElement.isJsonObject)
                    return delegateAdapter.fromJsonTree(rootElement)
                val root = rootElement.asJsonObject
                for (cacheElement in cache) {
                    var element: JsonElement? = root
                    for (s in cacheElement.path) {
                        if (element!!.isJsonObject) {
                            element = element!!.asJsonObject.get(s)
                        } else if (element!!.isJsonArray) {
                            element = try {
                                element!!.asJsonArray.get(Integer.valueOf(s))
                            } catch (e: NumberFormatException) {
                                null
                            } catch (e: IndexOutOfBoundsException) {
                                null
                            }

                        } else {
                            element = null
                            break
                        }
                    }
                    rootElement.asJsonObject.add(cacheElement.name, element)// FIXME: 19.05.2016 serializedName
                }
                return delegateAdapter.fromJsonTree(rootElement)
            }
        }.nullSafe()
    }

    // Find annotated fields of the class and any superclasses
    private fun getAnnotatedFields(klas: Class<*>?, annotationClass: Class<out Annotation>): List<Field> {
        var klass = klas
        val fields = ArrayList<Field>()
        while (klass != null) {
            for (field in klass.declaredFields) {
                if (field.isAnnotationPresent(annotationClass)) {
                    fields.add(field)
                }
            }
            // Walk up class hierarchy
            klass = klass.superclass
        }
        return fields
    }

    private fun buildCache(root: Class<*>, gson: Gson): ArrayList<FlattenCacheItem> {
        val cache = ArrayList<FlattenCacheItem>()
        val fields = getAnnotatedFields(root, Flatten::class.java)
        if (fields.isEmpty()) {
            return cache
        }
        var flatten: Flatten
        var type: Type
        var path: String
        var cacheItem: FlattenCacheItem
        val fieldNamingStrategy = gson.fieldNamingStrategy()

        for (field in fields) {
            flatten = field.getAnnotation(Flatten::class.java)
            path = flatten.value
            type = field.genericType
            val name = fieldNamingStrategy.translateName(field)
            cacheItem = FlattenCacheItem(path.split("::".toRegex()).toTypedArray(), gson.getAdapter(type.javaClass), name)
            //check path
            for (i in 0 until cacheItem.path.size - 1) {
                if (cacheItem.path[i].isEmpty()) {
                    throw RuntimeException("Intermediate path items cannot be empty, found $path")
                }
            }
            val i = cacheItem.path.size - 1
            if (cacheItem.path[i].isEmpty()) {
                cacheItem.path[i] = cacheItem.name
            }
            cache.add(cacheItem)
        }

        return cache
    }

    protected class FlattenCacheItem(internal val path: Array<String>, internal val adapter: TypeAdapter<*>, internal val name: String)
}
