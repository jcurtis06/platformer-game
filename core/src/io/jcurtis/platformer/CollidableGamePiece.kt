package io.jcurtis.platformer

import com.badlogic.gdx.graphics.g2d.SpriteBatch

open class CollidableGamePiece(x: Float, y: Float, width: Float, height: Float) : RectangleGamePiece(x, y, width, height) {
    var isSoft = false
    open fun onCollision(other: RectangleGamePiece) {
    }
}