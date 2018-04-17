package com.kotlinextension.utils

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class GenericRecyclerAdapter private constructor() :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val itemList :MutableList<Any>
    private val dataTypes :MutableList<Type>
    private val creators :MutableMap<Type, Any>

    init {
        itemList = mutableListOf()
        dataTypes = mutableListOf()
        creators = hashMapOf()
    }

    companion object {
        fun create() :GenericRecyclerAdapter {
            return GenericRecyclerAdapter()
        }

        fun <T :GenericRecyclerAdapter> create(clazz :Class<T>) :T? {
            try {
                return clazz.newInstance()
            } catch(e :InstantiationException) {
                e.printStackTrace()
            } catch(e :IllegalAccessException) {
                e.printStackTrace()
            }

            return null
        }
    }

    override fun getItemCount() :Int = itemList.size

    override fun getItemViewType(position :Int) :Int {
        val item = itemList[position]
        var index = dataTypes.indexOf(item.javaClass)
        if(index == -1)
            dataTypes.add(item.javaClass)
        index = dataTypes.indexOf(item.javaClass)
        return index
    }

    @Suppress("UNCHECKED_CAST")
    private fun <D> getItem(position :Int) :D {
        return itemList[position] as D
    }

    override fun onCreateViewHolder(parent :ViewGroup, viewType :Int) :RecyclerView.ViewHolder {
        val dataType = dataTypes[viewType]
        var creatorValue :Any? = creators[dataType]
        if(creatorValue is IBindingViewHolderCreator<*, *>) {
            val creator = creatorValue as IBindingViewHolderCreator<*, *>?
            return creator!!.create(parent)
        } else {
            if(creatorValue == null) {
                for(t in creators.keys) {
                    if(isTypeMatch(t, dataType)) {
                        creatorValue = creators[t]
                        break
                    }
                }
            }
            if(creatorValue == null)
                throw IllegalArgumentException(String.format("Neither the TYPE: %s not The DEFAULT injector found...", dataType))
            return (creatorValue as IBindingViewHolderCreator<*, *>).create(parent)
        }
    }

    override fun onBindViewHolder(holder :RecyclerView.ViewHolder, position :Int) {
        //(holder as RecyclerTypeBindingViewHolder)//.bind(position, getItem(position))
    }

    private fun isTypeMatch(type :Type, targetType :Type) :Boolean {
        if(type is Class<*> && targetType is Class<*>) {
            if(type.isAssignableFrom(targetType))
                return true
        } else if(type is ParameterizedType && targetType is ParameterizedType) {
            if(isTypeMatch(type.rawType, targetType.rawType)) {
                val types = type.actualTypeArguments
                val targetTypes = targetType.actualTypeArguments
                if(types == null || targetTypes == null || types.size != targetTypes.size) {
                    return false
                }
                val len = types.size
                for(i in 0 until len) {
                    if(!isTypeMatch(types[i], targetTypes[i])) {
                        return false
                    }
                }
                return true
            }
        }
        return false
    }

    fun <D, V : ViewDataBinding> registerBinding(layoutRes :Int, brVariable :Int,
        recyclerBindingInjector :(item :D, binding :V, position :Int) -> Unit) :GenericRecyclerAdapter {
        val interfaces = recyclerBindingInjector.javaClass.genericInterfaces
        for(type in interfaces) {
            if(type is ParameterizedType) {
                val actualType = type.actualTypeArguments[0]
                creators[actualType] = object :IBindingViewHolderCreator<D, V>{
                    override fun create(parent :ViewGroup) :RecyclerTypeBindingViewHolder<D, V> {
                        return object :RecyclerTypeBindingViewHolder<D, V>(parent, layoutRes, brVariable) {
                            override fun onBind(position :Int, item :D, binding :V) {
                                recyclerBindingInjector.invoke(item, binding, position)
                            }
                        }
                    }
                }
                break
            }
        }
        return this
    }

    fun attachTo(vararg recyclerViews :RecyclerView) :GenericRecyclerAdapter {
        for(recyclerView in recyclerViews) {
            recyclerView.adapter = this
        }
        return this
    }

    fun addAll(list :MutableList<Any>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }
}

interface IBindingViewHolderCreator<D, V :ViewDataBinding> {
    fun create(parent :ViewGroup) :RecyclerTypeBindingViewHolder<D, V>
}

abstract class RecyclerTypeBindingViewHolder<D, V :ViewDataBinding>
internal constructor(parent :ViewGroup, itemLayoutRes :Int, brVariable :Int) :RecyclerBindingViewHolder<D, V>(parent, itemLayoutRes, brVariable)

@Suppress("UNCHECKED_CAST")
abstract class RecyclerBindingViewHolder<D, V :ViewDataBinding>(private val viewDataBinding :V, variable :Int) :RecyclerView.ViewHolder(viewDataBinding.root) {
    private var brVariable = -1

    constructor(parent :ViewGroup, itemLayoutRes :Int, brVariable :Int) :
        this(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), itemLayoutRes, parent, false) as V, brVariable)

    init {
        brVariable = variable
    }

    fun bind(position :Int, item :D) {
        onBind(position, item, viewDataBinding)
        if(brVariable != -1) {
            viewDataBinding.setVariable(brVariable, item)
            viewDataBinding.executePendingBindings()
        }
    }

    protected abstract fun onBind(position :Int, item :D, binding :V)
}
