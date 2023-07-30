package io.jcurtis.platformer.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import io.jcurtis.platformer.MainGame
import io.jcurtis.platformer.graphics.AnimatedSheet
import io.jcurtis.platformer.graphics.SpriteSheet
import io.jcurtis.platformer.utils.BoundingBox
import io.jcurtis.platformer.utils.Direction
import kotlin.math.roundToInt

open class Movable(x: Float, y: Float, width: Float, height: Float): Entity(x, y, width, height) {
    private val texture = MainGame.assetManager!!.get("player.png", Texture::class.java)

    private val idleAnimation = SpriteSheet(texture, 7, 1, 0.1f, 0, 0, 4, 1)
    private val walkAnimation = SpriteSheet(texture, 7, 1, 0.1f, 4, 0, 7, 1)
    private val animatedSheet = AnimatedSheet()

    private var velocity = Vector2(0f, 0f)
    open protected var speed = 100f
    private val gravity = -300f
    private val jumpSpeed = 100f
    private val maxJumps = 10
    private var jumps = maxJumps

    private var currentTime = 0.0f
    init {
        animatedSheet.currentAnimation = idleAnimation
    }

    open fun moveLeft(): Boolean {
        return false
    }

    open fun moveRight(): Boolean {
        return false
    }

    open fun jump(): Boolean {
        return false
    }

    open fun showPosition() {
    }

    override fun upCollision(box: BoundingBox) {
        velocity.y = 0f

    }

    override fun downCollision(box: BoundingBox) {
        velocity.y = 0f
        jumps = maxJumps
    }

    override fun leftCollision(box: BoundingBox) {
        velocity.x = 0f
    }

    override fun rightCollision(box: BoundingBox) {
        velocity.x = 0f
    }

    override fun update(delta: Float) {
        currentTime += delta
        if(currentTime > 5.0f) {
            showPosition()
            currentTime = 0.0f
        }
        animatedSheet.update(delta)

        velocity.y += gravity * delta

        if (moveLeft()) {
            animatedSheet.flipH = true
            velocity.x = -speed
            animatedSheet.currentAnimation = walkAnimation
        } else if (moveRight()) {
            animatedSheet.flipH = false
            velocity.x = speed
            animatedSheet.currentAnimation = walkAnimation
        } else {
            velocity.x = 0f
            animatedSheet.currentAnimation = idleAnimation
        }

        if (jump() && jumps > 0) {
            velocity.y = jumpSpeed
            jumps--
        }

        checkCollisions(x + (velocity.x * delta).roundToInt().toFloat(), y + (velocity.y * delta).roundToInt().toFloat())
    }

    override fun render(batch: SpriteBatch) {
        batch.draw(
            animatedSheet.getCurrentFrame(),
            x,
            y,
            animatedSheet.getCurrentFrame().regionWidth.toFloat(),
            animatedSheet.getCurrentFrame().regionHeight.toFloat()
        )
    }
}