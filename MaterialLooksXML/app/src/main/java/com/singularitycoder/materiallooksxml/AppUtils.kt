package com.singularitycoder.materiallooksxml

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.TimingLogger
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.util.*

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

