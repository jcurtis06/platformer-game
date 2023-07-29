package io.jcurtis.slimslime.managers

import com.badlogic.gdx.math.Rectangle
import io.jcurtis.slimslime.utils.BoundingBox

object CollisionManager {
    private val colliders = arrayListOf<BoundingBox>()

    fun add(rect: BoundingBox) {
        colliders.add(rect)
    }

    fun getColliders(): ArrayList<BoundingBox> {
        return colliders
    }

    fun isColliding(rect: BoundingBox): Boolean {
        for (collider in colliders) {
            if (collider == rect) continue
            if (collider.overlaps(rect)) return true
        }
        return false
    }
}