package io.jcurtis.platformer.managers

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.jcurtis.platformer.entities.Entity

object EntityManager {
    private val entities = arrayListOf<Entity>()

    fun add(entity: Entity) {
        entities.add(entity)
    }

    fun remove(entity: Entity) {
        entities.remove(entity)
    }

    fun update(delta: Float) {
        for (entity in entities) {
            entity.update(delta)
        }
    }

    fun render(batch: SpriteBatch) {
        for (entity in entities) {
            entity.render(batch)
        }
    }
}