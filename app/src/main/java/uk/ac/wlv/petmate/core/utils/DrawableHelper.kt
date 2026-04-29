package uk.ac.wlv.petmate.core.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable

object DrawableHelper {

    fun resizeDrawable(
        ctx        : Context,
        drawableRes: Int,
        width      : Int,
        height     : Int
    ): Drawable {
        val bitmap   = createBitmap(width, height)
        val canvas   = Canvas(bitmap)
        val drawable = ContextCompat.getDrawable(ctx, drawableRes)!!
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
        return bitmap.toDrawable(ctx.resources)
    }
}