package io.jcurtis.platformer.entities

import io.jcurtis.platformer.managers.InputAction
import io.jcurtis.platformer.managers.InputManager


class Enemy(steps: Int, x: Float, y: Float, width: Float, height: Float): Movable(x, y, width, height) {
    private val maxSteps = steps
    private var steps = 0
    private var direction = -1

    public override var speed = 30f

    override fun moveLeft(): Boolean {
        if(direction == -1) {
            steps++
            if(steps <= maxSteps) {
                return true
            }
            else {
                steps = 0
                direction = -direction
            }
        }
        return false
    }
    override fun moveRight(): Boolean {
        if(direction == 1) {
            steps++
            if(steps <= maxSteps) {
                return true
            }
            else {
                steps = 0
                direction = -direction
            }
        }
        return false
    }
    override fun jump(): Boolean {
        return false
    }
}