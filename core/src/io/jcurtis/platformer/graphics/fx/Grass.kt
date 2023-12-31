package io.jcurtis.platformer.graphics.fx

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import io.jcurtis.platformer.MainGame
import io.jcurtis.platformer.entities.Entity
import io.jcurtis.platformer.graphics.SpriteSheet
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class Grass(texture: Texture, x: Float, y: Float): Entity(x, y, 5f, 5f) {
    private val spriteSheet = SpriteSheet(texture, 5, 15)
    private val sprite = Sprite(spriteSheet.getSprite(MathUtils.random(0, 6), 0))

    private val rotationSpeed = 0.15f

    private val windStrength = 15.0f
    private val windSpeed = 5.0f

    private var targetAngle = 0.0f
    private var stomped = false
    private var time = 0.0f

    init {
        registerBounds()
        sprite.setPosition(x, y)
        sprite.setOrigin(2f, 0f)
        isArea = true
        zIndex = -1
    }

    override fun update(delta: Float) {
        time += delta

        if (!stomped)
            targetAngle = windStrength * sin(time * windSpeed)

        sprite.rotation = MathUtils.lerp(sprite.rotation, targetAngle, rotationSpeed)

        val player = MainGame.player

        val dist = abs(player!!.centeredX - x)
        val distY = abs(player.centeredY - y)

        if (dist > 10 || distY > 10) {
            stomped = false
            return
        }
        if (stomped) return

        stomped = true

        var tempTarget = 0.0f

        if (player.x > x) {
            tempTarget = 70f
        } else if (player.x < x) {
            tempTarget = -70f
        }

        targetAngle = min(targetAngle + tempTarget, 90f)
        targetAngle = max(targetAngle, -90f)
    }

    override fun render(batch: SpriteBatch) {
        sprite.draw(batch)
    }

    override fun onCollision(entity: Entity) {
    }
}