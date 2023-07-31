package io.jcurtis.platformer.graphics.fx

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.jcurtis.platformer.MainGame
import io.jcurtis.platformer.graphics.SpriteSheet

class Background(stars: SpriteSheet) {
    private var backgroundItems = arrayListOf<Sprite>()

    private var lastCamX = 0f
    private var lastCamY = 0f

    init {
        for (i in 0..40) {
            val sprite =  Sprite(stars.getSprites().random().random())

            sprite.setPosition(
                (Math.random() * MainGame.camera!!.viewportWidth).toFloat(),
                (Math.random() * MainGame.camera!!.viewportHeight).toFloat()
            )

            backgroundItems.add(sprite)
        }
    }

    fun update() {
        val parallaxSpeedX = 0.5f
        val parallaxSpeedY = 0.3f

        val deltaX = MainGame.camera!!.position.x - lastCamX
        val deltaY = MainGame.camera!!.position.y - lastCamY

        for (item in backgroundItems) {
            item.x += deltaX * parallaxSpeedX
            item.y += deltaY * parallaxSpeedY

            // Loop in X direction
            if (item.x > MainGame.camera!!.position.x + MainGame.camera!!.viewportWidth / 2) {
                item.x -= MainGame.camera!!.viewportWidth
            } else if (item.x + item.width < MainGame.camera!!.position.x - MainGame.camera!!.viewportWidth / 2) {
                item.x += MainGame.camera!!.viewportWidth
            }

            // Loop in Y direction
            if (item.y > MainGame.camera!!.position.y + MainGame.camera!!.viewportHeight / 2) {
                item.y -= MainGame.camera!!.viewportHeight
            } else if (item.y + item.height < MainGame.camera!!.position.y - MainGame.camera!!.viewportHeight / 2) {
                item.y += MainGame.camera!!.viewportHeight
            }
        }

        lastCamX = MainGame.camera!!.position.x
        lastCamY = MainGame.camera!!.position.y
    }

    fun render(batch: SpriteBatch) {
        for (item in backgroundItems) {
            item.draw(batch)
        }
    }
}