package com.singularitycoder.materiallooksxml

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.TimingLogger
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.forEach
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

val dateFormatList = listOf(
    "dd-MMMM hh:mm",
    "dd-MM-yyyy",
    "dd/MM/yyyy",
    "dd-MMM-yyyy",
    "dd/MMM/yyyy",
    "dd-MMM-yyyy",
    "dd MMM yyyy",
    "dd-MMM-yyyy h:mm a",
    "dd MMM yyyy, hh:mm a",
    "dd MMM yyyy, hh:mm:ss a",
    "dd MMM yyyy, h:mm:ss aaa",
    "yyyy/MM/dd",
    "yyyy-MM-dd",
    "yyyy.MM.dd HH:mm",
    "yyyy/MM/dd hh:mm aa",
    "yyyy-MM-dd'T'HH:mm:ss.SS'Z'",
    "hh:mm a"
)

fun convertLongToTime(time: Long, type: UByte): String {
    val date = Date(time)
    val dateFormat = SimpleDateFormat(dateFormatList.getOrElse(index = type.toInt(), defaultValue = { dateFormatList[3] }), Locale.getDefault())
    return dateFormat.format(date)
}

fun convertDateToLong(date: String, type: UByte): Long {
    if (date.trim().isBlank() || date.toLowCase().trim() == "null") return convertDateToLong(date = Date().toString(), type = 3u)
    val dateFormat = SimpleDateFormat(dateFormatList.getOrElse(index = type.toInt(), defaultValue = { dateFormatList[3] }), Locale.getDefault())
    return try {
        if (dateFormat.parse(date) is Date) dateFormat.parse(date).time else convertDateToLong(date = Date().toString(), type = 3u)
    } catch (e: Exception) {
        convertDateToLong(date = Date().toString(), type = 3u)
    }
}

// https://www.programmersought.com/article/39074216761/
fun Menu.invokeSetMenuIconMethod() {
    if (this.javaClass.simpleName.equals("MenuBuilder", ignoreCase = true)) {
        try {
            val method: Method = this.javaClass.getDeclaredMethod("setOptionalIconsVisible", java.lang.Boolean.TYPE)
            method.isAccessible = true
            method.invoke(this, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun setMarginBtwMenuIconAndText(context: Context, menu: Menu, iconMarginDp: Int) {
    menu.forEach { item: MenuItem ->
        val iconMarginPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconMarginDp.toFloat(), context.resources.displayMetrics).toInt()
        if (null != item.icon) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
            } else {
                item.icon = object : InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                    override fun getIntrinsicWidth(): Int = intrinsicHeight + iconMarginPx + iconMarginPx
                }
            }
        }
    }
}

fun logExecutionTime(tag: String, label: String, vararg methods: () -> Any) {
    val timings = TimingLogger(tag, label).apply {
        if (methods.isNotEmpty()) methods[0].invoke()
        addSplit("stage 1 of $label")
        if (methods.size > 1) methods[1].invoke()
        addSplit("stage 2 of $label")
        dumpToLog()
    }
}

fun EditText.setOnTextChanged(
    beforeTextChanged: ((str: String, start: Int, count: Int, after: Int) -> Unit)? = null,
    onTextChanged: ((str: String, start: Int, before: Int, count: Int) -> Unit)? = null,
    afterTextChanged: ((str: String) -> Unit)? = null
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            if (null != beforeTextChanged) beforeTextChanged.invoke(s.toString(), start, count, after) else Unit
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (null != onTextChanged) onTextChanged.invoke(s.toString(), start, before, count) else Unit
        }

        override fun afterTextChanged(editable: Editable?) {
            if (null != afterTextChanged) afterTextChanged.invoke(editable.toString()) else Unit
        }
    })
}

infix fun (() -> Any).giveMethodExecTime(timeType: String): String {
    val ONE_SEC_IN_MILLI_SEC = 1E3
    val ONE_SEC_IN_MICRO_SEC = 1E6
    val ONE_SEC_IN_NANO_SEC = 1E9

    val startedAt = System.nanoTime()

    this.invoke()

    val endedAt = System.nanoTime()

    val elapsedTime = endedAt - startedAt
    return when (timeType) {
        "ms" -> (elapsedTime / ONE_SEC_IN_MICRO_SEC).toString().plus(" milli seconds")
        "Ms" -> (elapsedTime / ONE_SEC_IN_MILLI_SEC).toString().plus(" micro seconds")
        "s" -> (elapsedTime / ONE_SEC_IN_NANO_SEC).toString().plus(" seconds")
        else -> elapsedTime.toString().plus(" nano seconds")
    }
}

fun String.toLowCase(): String = this.lowercase(Locale.ROOT)

fun String.toUpCase(): String = this.uppercase(Locale.ROOT)

fun String.capFirstChar(): String = this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

infix fun String.prefixWith(str: String): String = "$str$this"

infix fun String.suffixWith(str: String): String = "$this$str"

fun TextView.showHideIcon(
    context: Context,
    showTick: Boolean,
    @DrawableRes icon1: Int = android.R.drawable.ic_delete,
    @DrawableRes icon2: Int = android.R.drawable.ic_delete,
    @DrawableRes icon3: Int = android.R.drawable.ic_delete,
    @DrawableRes icon4: Int = android.R.drawable.ic_delete,
    @ColorRes iconColor1: Int = android.R.color.white,
    @ColorRes iconColor2: Int = android.R.color.white,
    @ColorRes iconColor3: Int = android.R.color.white,
    @ColorRes iconColor4: Int = android.R.color.white,
    direction: Int
) {
    val left = 1
    val top = 2
    val right = 3
    val bottom = 4
    val leftRight = 5
    val topBottom = 6

    val drawable1 = ContextCompat.getDrawable(context, icon1)?.changeColor(context = context, color = iconColor1)
    val drawable2 = ContextCompat.getDrawable(context, icon2)?.changeColor(context = context, color = iconColor2)
    val drawable3 = ContextCompat.getDrawable(context, icon3)?.changeColor(context = context, color = iconColor3)
    val drawable4 = ContextCompat.getDrawable(context, icon4)?.changeColor(context = context, color = iconColor4)

    if (showTick) {
        when (direction) {
            left -> this.setCompoundDrawablesWithIntrinsicBounds(drawable1, null, null, null)
            top -> this.setCompoundDrawablesWithIntrinsicBounds(null, drawable2, null, null)
            right -> this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable3, null)
            bottom -> this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable4)
            leftRight -> this.setCompoundDrawablesWithIntrinsicBounds(drawable1, null, drawable3, null)
            topBottom -> this.setCompoundDrawablesWithIntrinsicBounds(null, drawable2, null, drawable4)
        }
    } else this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
}

fun Drawable.changeColor(
    context: Context,
    @ColorRes color: Int
): Drawable {
    val unwrappedDrawable = this
    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
    DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, color))
    return this
}

fun showSnackBar(
    view: View,
    @StringRes message: Int,
    anchorView: View? = null,
    duration: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(view, message, duration).apply {
        this.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        if (null != anchorView) this.anchorView = anchorView
        this.show()
    }
}

