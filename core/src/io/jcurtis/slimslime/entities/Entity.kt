package io.jcurtis.slimslime.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import io.jcurtis.slimslime.managers.CollisionManager
import io.jcurtis.slimslime.utils.BoundingBox
import io.jcurtis.slimslime.utils.Direction
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

    fun checkCollisionsX(right: Boolean) {
        collidedDirections[Direction.RIGHT] = false
        collidedDirections[Direction.LEFT] = false
        val bounds = getBounds()!!

        for (box in CollisionManager.getColliders()) {
            if (box == getBounds()) continue
            if (box.overlaps(getBounds())) {
                if (right) {
                    collidedDirections[Direction.RIGHT] = true
                    position.x = box.left - bounds.width
                } else {
                    collidedDirections[Direction.LEFT] = true
                    position.x = box.right
                }
            }
        }
    }

    fun checkCollisionsY(up: Boolean) {
        collidedDirections[Direction.UP] = false
        collidedDirections[Direction.DOWN] = false
        val bounds = getBounds()!!

        for (box in CollisionManager.getColliders()) {
            if (box == getBounds()) continue
            if (box.overlaps(getBounds())) {
                if (up) {
                    collidedDirections[Direction.UP] = true
                    position.y = box.bottom - bounds.height
                } else {
                    collidedDirections[Direction.DOWN] = true
                    position.y = box.top
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