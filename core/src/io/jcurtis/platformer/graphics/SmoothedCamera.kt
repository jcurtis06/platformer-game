package io.jcurtis.platformer.graphics

import com.badlogic.gdx.graphics.OrthographicCamera

class SmoothedCamera: OrthographicCamera() {
    private var targetX = 0
    private var targetY = 0

    fun setTarget(x: Int, y: Int) {
        targetX = x
        targetY = y
    }

    override fun update() {
        position.x += (targetX - position.x) / 10
        position.y += (targetY - position.y) / 10
        super.update()
    }
}