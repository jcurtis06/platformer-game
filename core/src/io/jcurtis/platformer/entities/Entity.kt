package io.jcurtis.platformer.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import io.jcurtis.platformer.managers.CollisionManager
import io.jcurtis.platformer.utils.BoundingBox
import io.jcurtis.platformer.utils.Direction
import kotlin.math.roundToInt
import kotlin.math.abs

abstract class Entity(x: Float, y: Float) {
    private var bounds: BoundingBox? = null

    protected var collidedDirections = hashMapOf<Direction,Boolean>(
        Direction.UP to false,
        Direction.DOWN to false,
        Direction.LEFT to false,
        Direction.RIGHT to false
    )

    fun registerBounds(width: Float, height: Float) {
        bounds = BoundingBox(position.x, position.y, width, height)
        CollisionManager.add(bounds!!)
    }

    fun getBounds(): BoundingBox? {
        bounds?.setPosition(position.x, position.y)
        return bounds
    }

    fun getOverlapping(): BoundingBox? {
        for (collider in CollisionManager.getColliders()) {
            if (collider == getBounds()) continue
            if (collider.overlaps(getBounds())) return collider
        }

        return null
    }

    open fun leftCollision() {

    }

    open fun rightCollision() {

    }

    open fun upCollision() {

    }

    open fun downCollision() {

    }

    fun checkCollisions(newX: Float, newY: Float) {
        val minStep = 16.0f
        val xDelta = newX - position.x
        val yDelta = newY - position.y
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
        var bounds = getBounds()!!

        for(i in 0 until steps) {
            position.x += xStep
            bounds = getBounds()!!

            for (box in CollisionManager.getColliders()) {
                if (box == bounds) continue
                if (box.overlaps(bounds)) {
                    if (xStep > 0) {
                        position.x = box.left - bounds.width
                        rightCollision()
                    } else {
                        position.x = box.right
                        leftCollision()
                    }
                    return
                }
            }

            position.y += yStep
            bounds = getBounds()!!

            for (box in CollisionManager.getColliders()) {
                if (box == bounds) continue
                if (box.overlaps(bounds)) {
                    if (yStep > 0) {
                        println("up")
                        position.y = box.bottom - bounds.height
                        upCollision()
                    } else {
                        println("down")
                        position.y = box.top
                        bounds = getBounds()!!
                        downCollision()
                    }
                    return
                }
            }
        }
        position.x = newX
        position.y = newY
        bounds = getBounds()!!
    }

    var position: Vector2 = Vector2(x, y)
        set(value) {
            field = Vector2(value.x.roundToInt().toFloat(), value.y.roundToInt().toFloat())
        }

    abstract fun update(delta: Float)

    abstract fun render(batch: SpriteBatch)
}