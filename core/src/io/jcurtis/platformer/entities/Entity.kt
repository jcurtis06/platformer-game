package io.jcurtis.platformer.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import io.jcurtis.platformer.managers.CollisionManager
import io.jcurtis.platformer.utils.BoundingBox
import io.jcurtis.platformer.utils.Direction
import kotlin.math.roundToInt

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

    fun checkCollisionsX(right: Boolean, newX: Float) {
        val oldX = position.x
        position.x = newX

        val bounds = getBounds()!!

        for (box in CollisionManager.getColliders()) {
            if (box == getBounds()) continue
            if (box.overlaps(getBounds())) {
                position.x = oldX
                if (right) {
                    rightCollision()
                } else {
                    leftCollision()
                }
                break
            }
        }
    }

    fun checkCollisionsY(up: Boolean, newY: Float) {
        val oldY = position.y
        position.y = newY
        collidedDirections[Direction.UP] = false
        collidedDirections[Direction.DOWN] = false
        val bounds = getBounds()!!

        for (box in CollisionManager.getColliders()) {
            if (box == getBounds()) continue
            if (box.overlaps(getBounds())) {
                position.y = oldY
                if (up) {
                    upCollision()
                } else {
                    downCollision()
                }
            }
        }
    }

    var position: Vector2 = Vector2(x, y)
        set(value) {
            field = Vector2(value.x.roundToInt().toFloat(), value.y.roundToInt().toFloat())
        }

    abstract fun update(delta: Float)

    abstract fun render(batch: SpriteBatch)
}