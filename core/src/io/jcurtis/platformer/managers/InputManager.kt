package io.jcurtis.platformer.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys

object InputManager {
    private val keyMapping = mutableMapOf<InputAction, MutableList<Int>>()

    init {
        mapKey(InputAction.LEFT, Keys.LEFT, Keys.A)
        mapKey(InputAction.RIGHT, Keys.RIGHT, Keys.D)
        mapKey(InputAction.JUMP, Keys.UP, Keys.W, Keys.SPACE)
    }

    fun isActionPressed(action: InputAction): Boolean {
        val keys = keyMapping[action]
        return keys?.any { Gdx.input.isKeyPressed(it) } ?: false
    }

    fun isActionJustPressed(action: InputAction): Boolean {
        val keys = keyMapping[action]
        return keys?.any { Gdx.input.isKeyJustPressed(it) } ?: false
    }

    private fun mapKey(action: InputAction, vararg keys: Int) {
        keyMapping[action] = keys.toMutableList()
    }
}

enum class InputAction {
    LEFT, RIGHT, JUMP
}
