package io.jcurtis.platformer.graphics

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion


class SpriteSheet(texture: Texture, spriteWidth: Int, spriteHeight: Int) {
    private val sprites: Array<Array<TextureRegion>> = TextureRegion.split(texture, spriteWidth, spriteHeight)

    fun getSprite(x: Int, y: Int): TextureRegion {
        return sprites[y][x]
    }

    fun getSprites(): Array<Array<TextureRegion>> {
        return sprites
    }
}