package com.extensions.dialogs

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.support.annotation.StyleRes
import com.extensions.interfaces.F1
import com.extensions.interfaces.F3

@Suppress("unused")
@JvmOverloads
fun Context.alert(@StyleRes theme :Int, message: String, title: String = "", positiveButton:String? = null,
    cancelable: Boolean = true, callback: (DialogInterface) -> Unit = {}) =
        AlertDialog.Builder(this).apply {
            setTheme(theme)
            if (title.isEmpty().not())
                setTitle(title)
            setMessage(message)
            setNegativeButton(getString(android.R.string.cancel), { _, _ -> })
            setPositiveButton(positiveButton ?: getString(android.R.string.ok), { dialog, _ -> callback(dialog) })
            setCancelable(cancelable)
            show()
        }

@Suppress("unused")
@JvmOverloads
fun Context.alert(message: String, title: String = "", positiveButton: String? = null, cancelable: Boolean = true, callback: F1<DialogInterface>?) =
        AlertDialog.Builder(this).apply {
            if (title.isEmpty().not())
                setTitle(title)
            setMessage(message)
            setNegativeButton(getString(android.R.string.cancel), { _, _ -> })
            setPositiveButton(positiveButton ?: getString(android.R.string.ok), { dialog, _ -> callback?.invoke(dialog) })
            setCancelable(cancelable)
            show()
        }

@JvmOverloads
fun <T> Context.selector(items: List<T>, callback: (DialogInterface, item: T, Int) -> Unit, title: String = "", cancelable: Boolean = true) =
        AlertDialog.Builder(this).apply {
            if (title.isEmpty().not())
                setTitle(title)
            setItems(Array(items.size) { i -> items[i].toString() }) { dialog, which ->
                callback(dialog, items[which], which)
            }
            setCancelable(cancelable)
            show()
        }

@JvmOverloads
fun <T> Context.selector(items: List<T>, callback: F3<DialogInterface, T, Int>?, title: String = "", cancelable: Boolean = true) =
        AlertDialog.Builder(this).apply {
            if (title.isEmpty().not())
                setTitle(title)
            setItems(Array(items.size) { i -> items[i].toString() }) { dialog, which ->
                callback?.invoke(dialog, items[which], which)
            }
            setCancelable(cancelable)
            show()
        }

@Suppress("unused")
@JvmOverloads
fun Context.confirm(message: String, callback: DialogInterface.() -> Unit, title: String = "", positiveButton: String? = null, negativeButton: String? = null, cancelable: Boolean = true) =
        AlertDialog.Builder(this).apply {
            if (title.isEmpty().not())
                setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButton ?: getString(android.R.string.ok), { dialog, _ -> dialog.callback() })
            setNegativeButton(negativeButton ?: getString(android.R.string.no), { _, _ -> })
            setCancelable(cancelable)
            show()
        }

@Suppress("unused")
@JvmOverloads
fun Context.confirm(message: String, callback: F1<DialogInterface>?, title: String = "", positiveButton: String? = null, negativeButton: String? = null, cancelable: Boolean = true) =
        AlertDialog.Builder(this).apply {
            if (title.isEmpty().not())
                setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButton ?: getString(android.R.string.ok), { dialog, _ -> callback?.invoke(dialog) })
            setNegativeButton(negativeButton ?: getString(android.R.string.no), { _, _ -> })
            setCancelable(cancelable)
            show()
        }

@JvmOverloads
fun Context.progress(message: String, cancelable: Boolean = true, title: String = ""): DialogInterface {
    return ProgressDialog(this).apply {
        setProgressStyle(ProgressDialog.STYLE_SPINNER)
        setMessage(message)
        if (title.isEmpty().not())
            setTitle(title)
        setCancelable(cancelable)
        show()
    }
}
