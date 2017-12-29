package ru.swalrus.rectdungeon

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Tile (img : Texture, passable : Boolean = true) {

    private var img : Texture = img
    private var passable : Boolean  = passable

    init {

    }

    fun draw(x : Float, y : Float, batch : SpriteBatch) {

        batch.draw(img, x, y,
                Const.TILE_SIZE, Const.TILE_SIZE, 0f, 1f, 1f, 0f)
    }
}
