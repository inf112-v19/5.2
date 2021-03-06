package inf112.skeleton.app.GameMechanics.GameObjects;

import inf112.skeleton.app.GameMechanics.Direction;
import inf112.skeleton.app.Visuals.SpriteType;

public class Laser extends GameObject {

    public Laser(Direction dir) {
        super(dir);
        switch (dir) {
            case NORTH:
            case SOUTH:
                spriteType = SpriteType.LASER_VERTICAL;
                break;
            case WEST:
            case EAST:
                spriteType = SpriteType.LASER_HORIZONTAL;
                break;
            default:
                System.err.println("Direction not valid in Laser");
                break;
        }
    }

    public Laser(Direction dir, boolean doubleLaser) {
        super(dir);
        switch (dir) {
            case NORTH:
            case SOUTH:
                if (doubleLaser) {
                    spriteType = SpriteType.DOUBLE_LASER_VERTIVAL;
                } else {
                    spriteType = SpriteType.LASER_VERTICAL;
                }
                break;
            case WEST:
            case EAST:
                if (doubleLaser) {
                    spriteType = SpriteType.DOUBLE_LASER_HORIZONTAL;
                } else {
                    spriteType = SpriteType.LASER_HORIZONTAL;
                }
                break;
            default:
                System.err.println("Direction not valid in Laser");
                break;
        }
    }

}

