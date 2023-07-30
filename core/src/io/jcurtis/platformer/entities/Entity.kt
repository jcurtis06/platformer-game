package io.jcurtis.platformer.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import io.jcurtis.platformer.managers.CollisionManager
import io.jcurtis.platformer.utils.BoundingBox
import io.jcurtis.platformer.utils.Direction
import kotlin.math.roundToInt
import kotlin.math.abs

abstract class Entity(x: Float, y: Float, width: Float, height: Float) : BoundingBox(x, y, width, height) {
    protected var collidedDirections = hashMapOf<Direction,Boolean>(
        Direction.UP to false,
        Direction.DOWN to false,
        Direction.LEFT to false,
        Direction.RIGHT to false
    )

    open fun leftCollision(box: BoundingBox) {

    }

    open fun rightCollision(box: BoundingBox) {

    }

    open fun upCollision(box: BoundingBox) {

    }

    open fun downCollision(box: BoundingBox) {

    }

    fun checkCollisions(newX: Float, newY: Float) {
        val minStep = 16.0f
        val xDelta = newX - x
        val yDelta = newY - y
        var steps = 1
        if(abs(xDelta) > abs(yDelta)) {
            if(abs(xDelta) > minStep)
                steps = (abs(xDelta) / minStep).roundToInt()
        }
        else {
            if(abs(yDelta) > minStep)
                steps = (abs(xDelta) / minStep).roundToInt()
        }
        var xStep = xDelta / steps.toFloat()
        var yStep = yDelta / steps.toFloat()

        for(i in 0 until steps) {
            setX(x + xStep)
            set(this)

            for (box in CollisionManager.getColliders()) {
                if (box == this) continue
                if (box.overlaps(this)) {
                    if (xStep > 0) {
                        setX(box.left - this.width)
                        set(this)
                        rightCollision(box)
                    } else {
                        setX(box.right)
                        set(this)
                        leftCollision(box)
                    }
                    return
                }
            }

            setY(y+yStep)
            set(this)

            for (box in CollisionManager.getColliders()) {
                if (box == this) continue
                if (box.overlaps(this)) {
                    if (yStep > 0) {
                        println("up")
                        setY(box.bottom + this.height)
                        set(this)
                        upCollision(box)
                    } else {
                        println("down")
                        setY(box.top)
                        set(this)
                        downCollision(box)
                    }
                    return
                }
            }
        }
        setX(newX)
        setY(newY)
        set(this)
    }

    abstract fun update(delta: Float)

    abstract fun render(batch: SpriteBatch)
}