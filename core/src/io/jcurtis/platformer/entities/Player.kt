package io.jcurtis.platformer.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import io.jcurtis.platformer.MainGame
import io.jcurtis.platformer.graphics.AnimatedSheet
import io.jcurtis.platformer.graphics.AnimationData
import io.jcurtis.platformer.managers.InputAction
import io.jcurtis.platformer.managers.InputManager
import io.jcurtis.platformer.utils.Direction
import kotlin.math.roundToInt

class Player: Entity(144f, 160f, 14f, 12f) {
    private val texture = MainGame.assetManager!!.get("player.png", Texture::class.java)

    private val idleAnimation = AnimationData(texture, 7, 1, 0.1f, 0, 0, 4, 1)
    private val walkAnimation = AnimationData(texture, 7, 1, 0.1f, 4, 0, 7, 1)
    private val animatedSheet = AnimatedSheet()

    private var velocity = Vector2(0f, 0f)
    private val speed = 50f
    private val gravity = -300f
    private val jumpSpeed = 100f
    private var jumps = 2

    init {
        registerBounds()

        animatedSheet.currentAnimation = idleAnimation
    }

    override fun update(delta: Float) {
        animatedSheet.update(delta)

        velocity.y += gravity * delta

        if (collidedDirections[Direction.DOWN]!! || collidedDirections[Direction.UP]!!)
            velocity.y = 0f
        if (collidedDirections[Direction.DOWN]!!)
            jumps = 2
        if (collidedDirections[Direction.LEFT]!! || collidedDirections[Direction.RIGHT]!!)
            velocity.x = 0f

        if (InputManager.isActionPressed(InputAction.LEFT)) {
            animatedSheet.flipH = true
            velocity.x = -speed
            animatedSheet.currentAnimation = walkAnimation
        } else if (InputManager.isActionPressed(InputAction.RIGHT)) {
            animatedSheet.flipH = false
            velocity.x = speed
            animatedSheet.currentAnimation = walkAnimation
        } else {
            velocity.x = 0f
            animatedSheet.currentAnimation = idleAnimation
        }

        if (InputManager.isActionJustPressed(InputAction.JUMP) && jumps > 0) {
            velocity.y = jumpSpeed
        }

        x += (velocity.x * delta).roundToInt()
        checkCollisionsX(velocity.x > 0)
        y += (velocity.y * delta).roundToInt()
        checkCollisionsY(velocity.y > 0)
    }

    override fun render(batch: SpriteBatch) {
        batch.draw(
            animatedSheet.getCurrentFrame(),
            x.roundToInt().toFloat(),
            y.roundToInt().toFloat(),
            animatedSheet.getCurrentFrame().regionWidth.toFloat(),
            animatedSheet.getCurrentFrame().regionHeight.toFloat()
        )
    }

    override fun onCollision(entity: Entity) {
        return
    }
}