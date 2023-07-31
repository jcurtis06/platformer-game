package io.jcurtis.platformer.graphics

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class AnimationData(
    private var res: Texture,
    private var cols: Int,
    private var rows: Int,
    var duration: Float,
    private var startX: Int = 0,
    private var startY: Int = 0,
    private var endX: Int = 0,
    private var endY: Int = 0
) {
    fun splitAnimation(): Array<TextureRegion> {
        val tmp = TextureRegion.split(
            res,
            res.width / cols,
            res.height / rows
        )

        val frames = Array((endX - startX) * (endY - startY)) { TextureRegion() }
        var index = 0

        for (i in startY until endY) {
            for (j in startX until endX) {
                frames[index++] = tmp[i][j]
            }
        }

        return frames
    }
}