package io.jcurtis.platformer.graphics.fx

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.jcurtis.platformer.GameBoard
import io.jcurtis.platformer.GamePiece
import io.jcurtis.platformer.graphics.SpriteSheet

class Background(stars: SpriteSheet) : GamePiece() {
    private var backgroundItems = arrayListOf<Sprite>()

    private var lastCamX = 0f
    private var lastCamY = 0f

    private val stars = stars

    override fun update(delta: Float, gameBoard: GameBoard) {
        if(backgroundItems.isEmpty()) {
            for (i in 0..40) {
                val sprite =  Sprite(stars.getSprites().random().random())

                sprite.setPosition(
                        (Math.random() * gameBoard.camera!!.viewportWidth).toFloat(),
                        (Math.random() * gameBoard.camera!!.viewportHeight).toFloat()
                )

                backgroundItems.add(sprite)
            }
        }
        val parallaxSpeedX = 0.5f
        val parallaxSpeedY = 0.3f

        val deltaX = gameBoard.camera!!.position.x - lastCamX
        val deltaY = gameBoard.camera!!.position.y - lastCamY

        for (item in backgroundItems) {
            item.x += deltaX * parallaxSpeedX
            item.y += deltaY * parallaxSpeedY

            // Loop in X direction
            if (item.x > gameBoard.camera!!.position.x + gameBoard.camera!!.viewportWidth / 2) {
                item.x -= gameBoard.camera!!.viewportWidth
            } else if (item.x + item.width < gameBoard.camera!!.position.x - gameBoard.camera!!.viewportWidth / 2) {
                item.x += gameBoard.camera!!.viewportWidth
            }

            // Loop in Y direction
            if (item.y > gameBoard.camera!!.position.y + gameBoard.camera!!.viewportHeight / 2) {
                item.y -= gameBoard.camera!!.viewportHeight
            } else if (item.y + item.height < gameBoard.camera!!.position.y - gameBoard.camera!!.viewportHeight / 2) {
                item.y += gameBoard.camera!!.viewportHeight
            }
        }

        lastCamX = gameBoard.camera!!.position.x
        lastCamY = gameBoard.camera!!.position.y
    }

    override fun render(batch: SpriteBatch) {
        for (item in backgroundItems) {
            item.draw(batch)
        }
    }
}