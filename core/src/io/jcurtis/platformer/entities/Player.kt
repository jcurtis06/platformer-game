package io.jcurtis.platformer.entities

import io.jcurtis.platformer.managers.InputAction
import io.jcurtis.platformer.managers.InputManager


class Player(x: Float, y: Float): Movable(x, y) {
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
        println("x: ${position.x}, y: ${position.y}")
    }
}