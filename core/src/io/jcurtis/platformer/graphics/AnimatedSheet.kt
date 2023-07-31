package io.jcurtis.platformer.graphics

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion

class AnimatedSheet {
    private var animation: Animation<TextureRegion>? = null

    var currentAnimation: AnimationData? = null
        set(value) {
            animation = Animation(value!!.duration, *value.splitAnimation())
            field = value
        }

    var flipH = false

    private var stateTime = 0f

    fun update(delta: Float) {
        stateTime += delta
    }

    fun getCurrentFrame(): TextureRegion {
        val currentFrame = animation!!.getKeyFrame(stateTime, true)

        if (flipH && !currentFrame.isFlipX) {
            currentFrame.flip(true, false)
        } else if (!flipH && currentFrame.isFlipX) {
            currentFrame.flip(true, false)
        }

        return currentFrame
    }
}