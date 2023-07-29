package io.jcurtis.platformer.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import io.jcurtis.platformer.MainGame
import io.jcurtis.platformer.utils.Direction
import kotlin.math.roundToInt

class Player: Entity(144f, 160f) {
    private val sprite = Sprite(MainGame.assetManager!!.get("slime.png", Texture::class.java))

    private var velocity = Vector2(0f, 0f)
    private val speed = 100f
    private val gravity = -300f
    private val jumpSpeed = 100f
    private val friction = 0f
    private var jumps = 2

    init {
        registerBounds(14f, 12f)
    }

    override fun update(delta: Float) {
        velocity.y += gravity * delta

        if (collidedDirections[Direction.DOWN]!! || collidedDirections[Direction.UP]!!)
            velocity.y = 0f
        if (collidedDirections[Direction.DOWN]!!)
            jumps = 2
        if (collidedDirections[Direction.LEFT]!! || collidedDirections[Direction.RIGHT]!!)
            velocity.x = 0f

        if (Gdx.input.isKeyPressed(Keys.A)) {
            velocity.x = -speed
        } else if (Gdx.input.isKeyPressed(Keys.D)) {
            velocity.x = speed
        } else {
            velocity.x *= friction
        }

        if (Gdx.input.isKeyJustPressed(Keys.W) && jumps > 0) {
            velocity.y = jumpSpeed
            jumps--
        }

        position.x += (velocity.x * delta).roundToInt()
        checkCollisionsX(velocity.x > 0)
        position.y += (velocity.y * delta).roundToInt()
        checkCollisionsY(velocity.y > 0)
    }

    override fun render(batch: SpriteBatch) {
        sprite.setPosition(position.x, position.y)
        sprite.draw(batch)
    }
}