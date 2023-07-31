package io.jcurtis.platformer.utils

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

open class BoundingBox(x: Float, y: Float, width: Float, height: Float) : Rectangle(x, y, width, height) {
    var left = 0f
        get() = x
    var right = 0f
        get() = x + width
    var top = 0f
        get() = y + height
    var bottom = 0f
        get() = y

    override fun set(rect: Rectangle?): Rectangle {
        if (rect != null) {
            left = rect.x
            right = rect.x + rect.width
            top = rect.y + rect.height
            bottom = rect.y
        }
        return super.set(rect)
    }

    override fun setPosition(position: Vector2?): Rectangle {
        if (position != null) {
            left = position.x
            right = position.x + width
            top = position.y + height
            bottom = position.y
        }
        return super.setPosition(position)
    }
}