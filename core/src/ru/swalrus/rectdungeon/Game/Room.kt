package ru.swalrus.rectdungeon.Game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ru.swalrus.rectdungeon.Const

class Room (val chunk: Chunk) {

    // Tiles
    private var map : Array<Array<Tile>> = Array<Array<Tile>>(Const.ROOM_SIZE + 2,
            { _ -> Array(Const.ROOM_SIZE + 2, { _ -> Floor() })})
    // Creatures
    private var creatureList : MutableList<Creature> = mutableListOf()
    private var currentCreature : Int = 0
    private var removeQueue: MutableList<Pair<Int, Int>> = MutableList(0, { _ -> Pair(0, 0) })


    init {
        generate()
    }


    fun addCreature(creature : Creature) {
        creatureList.add(creature)
    }

    fun getCreatureAt(x: Int, y: Int) : Creature? {
        for (item in creatureList) {
            if ((item.x == x) and (item.y == y)) {
                return item
            }
        }
        return null
    }

    fun removeCreatureAt(x: Int, y: Int) {
        removeQueue.add(Pair(x, y))
    }

    fun render(batch : SpriteBatch) {

        // If the current creature is ready to end turn,
        if (creatureList[currentCreature].ready) {
            // Move focus to the next creature
            if (currentCreature >= creatureList.size - 1) {
                currentCreature = 0
            } else {
                currentCreature++
            }
            // If it isn't sleeping AND is not in remove queue
            if (creatureList[currentCreature].isActive() and
                    (Pair(creatureList[currentCreature].x, creatureList[currentCreature].y) !in removeQueue)) {
                // Say him to make turn
                creatureList[currentCreature].act()
            }
        }

        // Remove creatures from remove queue
        for (pos in removeQueue) {
            for (creature in creatureList) {
                if ((creature.x == pos.first) and (creature.y == pos.second)) {
                    creatureList.remove(creature)
                    break
                }
            }
        }
        removeQueue.clear()

        var xPos : Float = 0f
        var yPos : Float = 0f

        for (x in 0 until map.size)
            for (y in map.size-1 downTo 0) {
                xPos = x * Const.TILE_SIZE + Const.MAP_MARGIN_LEFT
                yPos = y * Const.TILE_SIZE + Const.MAP_MARGIN_BOTTOM
                map[x][y].draw(xPos, yPos, batch)
            }

        for (creature in creatureList) {
            creature.render(batch)
        }
    }


    private fun generate() {

        for (y in 1 until map.size-1) {
            map[0][y] = Wall(Const.LEFT)
        }
        for (y in 1 until map.size-1) {
            map[map.size-1][y] = Wall(Const.RIGHT)
        }
        for (x in 1 until map.size-1) {
            map[x][0] = Wall(Const.BOTTOM)
        }
        for (x in 1 until map.size-1) {
            map[x][map.size-1] = Wall(Const.TOP)
        }
        map[map.size / 2][map.size - 1] = Door(Const.TOP, this)
        map[map.size / 2][0] = Door(Const.BOTTOM, this)
        map[0][map.size / 2] = Door(Const.LEFT, this)
        map[map.size - 1][map.size / 2] = Door(Const.RIGHT, this)
    }

    fun getTile(x: Int, y: Int) : Tile {
        try {
            return map[x][y]
        } catch (e: IndexOutOfBoundsException) {
            return EmptyTile()
        }
    }
}
