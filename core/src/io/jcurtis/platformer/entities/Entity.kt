package io.jcurtis.platformer.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import io.jcurtis.platformer.managers.CollisionManager
import io.jcurtis.platformer.utils.BoundingBox
import io.jcurtis.platformer.utils.Direction
import kotlin.math.abs
import kotlin.math.roundToInt

abstract class Entity(x: Float, y: Float, width: Float, height: Float): BoundingBox(x, y, width, height) {
    var zIndex = 0
    var isArea = false

    var centeredX: Float
        get() = x + width / 2
        set(value) {
            x = value - width / 2
        }

    var centeredY: Float
        get() = y + height / 2
        set(value) {
            y = value - height / 2
        }

    protected var collidedDirections = hashMapOf(
        Direction.UP to false,
        Direction.DOWN to false,
        Direction.LEFT to false,
        Direction.RIGHT to false
    )

    fun registerBounds() {
        CollisionManager.add(this)
    }

    fun checkCollisionsX(right: Boolean) {
        val newCollisions = HashSet<Entity>()

        collidedDirections[Direction.RIGHT] = false
        collidedDirections[Direction.LEFT] = false

        for (box in CollisionManager.getColliders()) {
            if (box == this) continue
            if (box.overlaps(this)) {
                if (box is Entity) {
                    newCollisions.add(box)

                    if (box.isArea) {
                        box.onCollision(this)

                        this.onCollision(box)
                        continue
                    }
                }

                if (right) {
                    collidedDirections[Direction.RIGHT] = true
                    x = box.left - this.width
                } else {
                    collidedDirections[Direction.LEFT] = true
                    x = box.right
                }
            }
        }
    }

    fun checkCollisionsY(up: Boolean) {
        val newCollisions = HashSet<Entity>()

        collidedDirections[Direction.UP] = false
        collidedDirections[Direction.DOWN] = false

        for (box in CollisionManager.getColliders()) {
            if (box == this) continue
            if (box.overlaps(this)) {
                if (box is Entity) {
                    newCollisions.add(box)

                    if (box.isArea) {
                        box.onCollision(this)
                        this.onCollision(box)
                        continue
                    }
                }

                if (up) {
                    collidedDirections[Direction.UP] = true
                    y = box.bottom - this.height
                } else {
                    collidedDirections[Direction.DOWN] = true
                    y = box.top
                }
            }
        }
    }

    abstract fun update(delta: Float)

    abstract fun render(batch: SpriteBatch)

    abstract fun onCollision(entity: Entity)
}