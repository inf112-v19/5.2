package inf112.skeleton.app.Tiles;

import inf112.skeleton.app.Direction;
import inf112.skeleton.app.GUI.SpriteType;
import inf112.skeleton.app.Interfaces.IGameObject;
import inf112.skeleton.app.Position;

public class RepairTile extends Tile {

	private final int SPRITE_X = 4;
	private final int SPRITE_Y = 0;
	private IGameObject[] gameObjects;
	private Direction direction;

	public RepairTile(IGameObject[] gameObjects, Direction direction) {
		this.gameObjects = gameObjects;
		this.direction = direction;
		super.spriteType = SpriteType.REPAIR_TILE;
	}

}