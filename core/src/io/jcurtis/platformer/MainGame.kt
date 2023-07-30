package io.jcurtis.platformer

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.jcurtis.platformer.entities.Player
import io.jcurtis.platformer.graphics.SmoothedCamera
import io.jcurtis.platformer.managers.CollisionManager
import io.jcurtis.platformer.managers.EntityManager
import io.jcurtis.platformer.utils.BoundingBox

object MainGame : ApplicationAdapter() {
    // The virtual resolution of the game in pixels
    // Lower values will zoom in, higher values will zoom out
    const val VIRTUAL_WIDTH = 320
    const val VIRTUAL_HEIGHT = 180

    var batch: SpriteBatch? = null

    // The texture and sprite used to render the player
    var img: Texture? = null
    var sprite: Sprite? = null

    // The FrameBuffer used to scale the game to the screen
    var frameBuffer: FrameBuffer? = null

    // The camera used to render the scene. Do NOT move this camera around.
    // It will always look at (VIRTUAL_WIDTH/2, VIRTUAL_HEIGHT/2) and have the same size as the virtual screen
    var fboCamera: OrthographicCamera? = null

    // the camera used within the game
    var camera: SmoothedCamera? = null

    // The viewport used to scale the game to the screen
    var viewport: Viewport? = null

    var assetManager: AssetManager? = null

    var map: TiledMap? = null
    var mapRenderer: OrthogonalTiledMapRenderer? = null

    var player: Player? = null

    override fun create() {
        batch = SpriteBatch()
        img = Texture("slime.png")

        // Create both cameras
        fboCamera = OrthographicCamera()
        camera = SmoothedCamera()
        camera!!.setToOrtho(false, VIRTUAL_WIDTH.toFloat(), VIRTUAL_HEIGHT.toFloat())

        // Create the viewport
        viewport = FitViewport(VIRTUAL_WIDTH.toFloat(), VIRTUAL_HEIGHT.toFloat(), fboCamera)
        viewport!!.apply()

        // Create a new FrameBuffer with a specific resolution
        frameBuffer = FrameBuffer(Pixmap.Format.RGB888, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, false)
        frameBuffer!!.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)

        // Load in assets
        assetManager = AssetManager()
        assetManager!!.setLoader(TiledMap::class.java, TmxMapLoader())

        assetManager!!.load("slime.png", Texture::class.java)
        assetManager!!.load("maps/test.tmx", TiledMap::class.java)
        assetManager!!.finishLoading()

        // Create the player
        player = Player()
        EntityManager.add(player!!)

        // Load the map
        map = assetManager!!.get("maps/test.tmx", TiledMap::class.java)
        mapRenderer = OrthogonalTiledMapRenderer(map, 1f)
        mapRenderer!!.setView(camera)

        // Load the collision map
        val layer = map!!.layers.get(0) as TiledMapTileLayer

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
        // Update entities
        EntityManager.update(Gdx.graphics.deltaTime)

        // Move the camera to the player
        camera!!.setTarget(player!!.position.x.toInt(), player!!.position.y.toInt())

        // Update the camera
        camera!!.update(Gdx.graphics.deltaTime)

        mapRenderer!!.setView(camera)

        // Start rendering to the FrameBuffer
        frameBuffer!!.begin()

        // Clear the screen with a white color
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Set the projection matrix to the camera's combined matrix
        batch!!.projectionMatrix = camera!!.combined

        // Begin the batch
        batch!!.begin()
        // Everything in here will be rendered to the FrameBuffer
        // Thus making it pixel-perfect

        if (assetManager!!.update()) {
            EntityManager.render(batch!!)
            mapRenderer!!.render()
        }

        // End the batch
        batch!!.end()

        // Stop rendering to the FrameBuffer
        frameBuffer!!.end()

        // Clear the screen with a black color
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport!!.apply()
        batch!!.projectionMatrix = fboCamera!!.combined
        fboCamera!!.setToOrtho(false, VIRTUAL_WIDTH.toFloat(), VIRTUAL_HEIGHT.toFloat())

        // Render the FrameBuffer to the screen, scaling it to the screen's resolution
        batch!!.begin()
        batch!!.draw(
            frameBuffer!!.colorBufferTexture,
            0f,
            0f,
            viewport!!.worldWidth,
            viewport!!.worldHeight,
            0f,
            0f,
            1f,
            1f
        )
        batch!!.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport!!.update(width, height)
        fboCamera!!.update()
    }

    override fun dispose() {
        batch!!.dispose()
        img!!.dispose()
        frameBuffer!!.dispose()
    }
}
