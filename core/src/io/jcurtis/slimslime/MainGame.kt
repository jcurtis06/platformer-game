package io.jcurtis.slimslime

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.jcurtis.slimslime.entities.Player
import io.jcurtis.slimslime.managers.CollisionManager
import io.jcurtis.slimslime.managers.EntityManager
import io.jcurtis.slimslime.utils.BoundingBox
import kotlin.math.roundToInt

object MainGame : ApplicationAdapter() {
    var worldWidth = 1600;
    var worldHeight = 1200;

    var viewWidth = 160f;
    var viewHeight = 120f;

    var camera: OrthographicCamera? = null
    var fbo: FrameBuffer? = null
    var viewport: Viewport? = null
    var batch: SpriteBatch? = null

    var assetManager: AssetManager? = null

    var map: TiledMap? = null
    var mapRenderer: OrthogonalTiledMapRenderer? = null

    var player: Player? = null

    override fun create() {
        camera = OrthographicCamera()
        camera!!.setToOrtho(false, worldWidth.toFloat(), worldHeight.toFloat())

        viewport = FitViewport(viewWidth, viewHeight, camera)

        fbo = FrameBuffer(Pixmap.Format.RGBA8888, worldWidth, worldHeight, false)
        fbo!!.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        batch = SpriteBatch()

        assetManager = AssetManager()
        assetManager!!.setLoader(TiledMap::class.java, TmxMapLoader())

        assetManager!!.load("slime.png", Texture::class.java)
        assetManager!!.load("maps/test.tmx", TiledMap::class.java)
        assetManager!!.finishLoading()

        player = Player()

        EntityManager.add(player!!)
        map = assetManager!!.get("maps/test.tmx", TiledMap::class.java)
        mapRenderer = OrthogonalTiledMapRenderer(map, 1f)
        mapRenderer!!.setView(camera)

        // add tilemap collisions
        val layer = map!!.layers.get(0) as TiledMapTileLayer

        // loop through each tile in the layer
        for (x in 0 until layer.width) {
            for (y in 0 until layer.height) {
                val cell = layer.getCell(x, y)

                if (cell != null) {
                    val rect = BoundingBox(x*16f, y*16f, 16f, 16f)
                    CollisionManager.add(rect)
                    println("Added collision at ${x*16f}, ${y*16f}")
                }
            }
        }
    }

    override fun render() {
        EntityManager.update(Gdx.graphics.deltaTime)
        fbo!!.begin()
        ScreenUtils.clear(1f, 1f, 1f, 1f)

        batch!!.begin()
        batch!!.projectionMatrix = camera!!.combined

        mapRenderer!!.render()

        if (assetManager!!.update()) {
            EntityManager.render(batch!!)
        }

        var lerp = 0.1f

        val targetPosition = Vector3(
            player!!.position.x.roundToInt().toFloat(),
            player!!.position.y.roundToInt().toFloat(),
            0f
        )

        val newPosition = camera!!.position.lerp(targetPosition, lerp)

        camera!!.position.set(newPosition)
        camera!!.update()

        batch!!.end()
        fbo!!.end()

        viewport!!.apply()
        batch!!.projectionMatrix = camera!!.combined

        batch!!.begin()
        batch!!.draw(
            fbo!!.colorBufferTexture,
            0f,
            0f,
            worldWidth.toFloat(),
            worldHeight.toFloat(),
            0f,
            0f,
            1f,
            1f
        )
        batch!!.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport!!.update(width, height)
        camera!!.update()
    }

    override fun dispose() {
        batch!!.dispose()
        fbo!!.dispose()
        assetManager!!.dispose()
        map!!.dispose()
        mapRenderer!!.dispose()
    }
}
