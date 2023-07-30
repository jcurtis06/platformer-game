package io.jcurtis.platformer.entities

import io.jcurtis.platformer.managers.InputAction
import io.jcurtis.platformer.managers.InputManager


class Player(x: Float, y: Float, width: Float, height: Float): Movable(x, y, width, height) {
    override fun moveLeft(): Boolean {
        return InputManager.isActionPressed(InputAction.LEFT)
    }

    override fun moveRight(): Boolean {
        return InputManager.isActionPressed(InputAction.RIGHT)
    }
    override fun jump(): Boolean {
        return InputManager.isActionJustPressed(InputAction.JUMP)
    }

    override fun showPosition() {
        println("x: ${x}, y: ${y}")
    }
}