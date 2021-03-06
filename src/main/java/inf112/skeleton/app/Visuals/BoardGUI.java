package inf112.skeleton.app.Visuals;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.GameMechanics.Board.Board;
import inf112.skeleton.app.GameMechanics.GameObjects.GameObject;
import inf112.skeleton.app.GameMechanics.GameObjects.Laser;
import inf112.skeleton.app.GameMechanics.Player;
import inf112.skeleton.app.GameMechanics.Position;
import inf112.skeleton.app.GameMechanics.Tiles.Tile;
import inf112.skeleton.app.Visuals.States.GameStateManager;


public class BoardGUI {


    private GameStateManager gsm;

    private Board board;
    private AssetHandler assetHandler;

    private Stage stage;

    private float xOffset;
    private float yOffset;
    private float tilesize;
    private int boardWidth;
    private int boardHeight;
    private float boardPixelWidth;
    private float boardPixelHeight;

    private static final float PAD_BOTTOM = 170;
    private static final float PAD_LEFT = 200;
    private static final float MOVE_DURATION = 1;

    public BoardGUI(Board board, OrthographicCamera camera, Stage stage, GameStateManager gsm, AssetHandler assetHandler) {
        this.gsm = gsm;
        this.stage = stage;
        this.assetHandler = assetHandler;
        this.board = board;

        this.boardWidth = board.getWidth();
        this.boardHeight = board.getHeight();

        this.tilesize = Math.min((int) Math.ceil(stage.getHeight() - PAD_BOTTOM), (int) Math.ceil(stage.getWidth() - PAD_LEFT)) / (Math.min(boardWidth, boardHeight));

        this.boardPixelWidth = (boardWidth - 1) * tilesize;
        this.boardPixelHeight = (boardHeight - 1) * tilesize;

        reposition();
    }

    /**
     * Method that updates all moving parts of the BoardGUI
     */
    public void update() {
        //hiding all lasers every update
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Laser) {
                actor.setVisible(false);
            }
        }

        //moving players width actions
        for (Player player : board.getAllPlayers()) {
            Position pos = board.getPlayerPos(player);

            Action move = Actions.moveTo(pos.getX() * tilesize + xOffset, pos.getY() * tilesize + yOffset, MOVE_DURATION);
            RotateToAction rotate = Actions.rotateTo(player.getDirection().directionToDegrees(), MOVE_DURATION);
            rotate.setUseShortestDirection(true);
            player.addAction(rotate);
            player.addAction(move);
        }

        //adding all lasers every update
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                Position position = new Position(x, y);
                Tile tile = board.getTile(position);
                float tileX = tile.getX();
                float tileY = tile.getY();
                addGameObjectsOnTileToStage(tile, tileX, tileY);
            }
        }
    }

    /**
     * Method that creates the board visually
     */
    public void create() {
        int xPos = 0;
        int yPos = 0;
        Position pos;

        for (float y = yOffset; y <= yOffset + boardPixelHeight; y += tilesize) {
            for (float x = xOffset; x <= xOffset + boardPixelWidth; x += tilesize) {
                pos = new Position(xPos, yPos);
                Tile curTile = board.getTile(pos);
                addTilesToStage(curTile, x, y);
                addGameObjectsOnTileToStage(curTile, x, y);
                xPos++;
            }
            xPos = 0;
            yPos++;
        }
        addPlayersToStage();
    }


    /**
     * Method that adds all players from the board to the stage
     */
    private void addPlayersToStage() {
        Player[] allPlayers = board.getAllPlayers();
        for (Player player : allPlayers) {
            Position pos = board.getPlayerPos(player);
            addPlayerToStage(player, pos);
        }
    }


    private void addPlayerToStage(Player player, Position pos) {
        float x = pos.getX() * tilesize + xOffset;
        float y = pos.getY() * tilesize + yOffset;
        player.setDrawable(new TextureRegionDrawable(assetHandler.getTexture(player)));
        player.setSize(tilesize, tilesize);
        player.setOrigin(tilesize / 2, tilesize / 2);
        player.setRotation(player.getDirection().directionToDegrees());
        player.setPosition(x, y);

        stage.addActor(player);
    }


    private void addTilesToStage(Tile tile, float x, float y) {
        tile.setDrawable(new TextureRegionDrawable(assetHandler.getTexture(tile)));
        tile.setSize(tilesize, tilesize);
        tile.setPosition(x, y);
        stage.addActor(tile);
    }

    /**
     * Methoud for adding a GameObject to the stage
     *
     * @param gameObject
     * @param x
     * @param y
     */
    private void addGameObjectToStage(GameObject gameObject, float x, float y) {
        gameObject.setDrawable(new TextureRegionDrawable(assetHandler.getTexture(gameObject)));
        gameObject.setSize(tilesize, tilesize);
        gameObject.setPosition(x, y);
        stage.addActor(gameObject);
    }

    private void addGameObjectsOnTileToStage(final Tile tile, float x, float y) {
        if (tile.hasAnyGameObjects()) {
            for (GameObject gameObject : tile.getGameObjects()) {
                addGameObjectToStage(gameObject, x, y);
            }
        }
    }

    private void hideDeadPlayer(Player player) {
        if (!player.onBoardCheck()) {
            Action hide = Actions.fadeOut(0.1f);
            player.addAction(hide);
        }
    }

    public void hideDeadPlayers() {
        for (Player player : board.getAllPlayers()) {
            hideDeadPlayer(player);
        }
    }


    public void showRevivedPlayer(Player player) {
        if (player.onBoardCheck()) {
            Action show = Actions.fadeIn(0.1f);
            player.addAction(show);
        }
    }

    public void showRevivedPlayers() {
        for (Player player : board.getAllPlayers()) {
            showRevivedPlayer(player);
        }
    }

    private void reposition() {
        this.xOffset = PAD_LEFT;//stage.getHeight();// / 2 - boardPixelWidth / 2;
        this.yOffset = stage.getHeight() - boardPixelHeight - tilesize;
    }


    public void removeAllListeners() {
        for (Actor actor : stage.getActors()) {
            removeListener(actor);
        }
    }

    private void removeListener(Actor actor) {
        actor.clearListeners();
    }


    public InputListener createListener(final Tile tile) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(tile.spriteType);
                gsm.tileEventHandle(tile);
                return true;
            }
        };
    }

    public void addListenersToStage() {
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Tile) {
                Tile tile = (Tile) actor;
                tile.addListener(createListener(tile));
                for (GameObject gameObject : tile.getGameObjects()) {
                    gameObject.addListener(createListener(tile));
                }
            }
        }
    }

    public float getxOffset() {
        return xOffset;
    }

    public float getyOffset() {
        return yOffset;
    }
}
