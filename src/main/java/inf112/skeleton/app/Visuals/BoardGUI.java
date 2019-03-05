package inf112.skeleton.app.Visuals;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.GameMechanics.Board.Board;
import inf112.skeleton.app.GameMechanics.GameObjects.GameObject;
import inf112.skeleton.app.Interfaces.IGameObject;
import inf112.skeleton.app.GameMechanics.Position;
import inf112.skeleton.app.GameMechanics.Tiles.Tile;



public class BoardGUI {

    private static Board board;

	private SpriteBatch batch;
	private TextureRegion textureRegion;
	private Stage stage;
	private ScreenViewport screenViewport;

	private SpriteSheet spriteSheet;

	private int xOffset;
	private int yOffset;
	private int tilesize;
	private int boardWidth;
	private int boardHeight;
	private int boardTileWidth;
	private int boardTileHeight;


    public BoardGUI(SpriteBatch batch, Board board, OrthographicCamera camera) {
        this.batch = batch;

        this.screenViewport = new ScreenViewport(camera);
		this.stage = new Stage(screenViewport);

        this.spriteSheet = new SpriteSheet();

        this.board = board;
        this.boardWidth = board.getWidth();
        this.boardHeight = board.getHeight();

        this.tilesize = Math.min(Gdx.graphics.getHeight(), Gdx.graphics.getWidth())/(Math.max(boardWidth, boardHeight));

        this.boardTileWidth = boardWidth *tilesize;
        this.boardTileHeight = boardHeight * tilesize;

        //setting initial position
        reposition();
        create();

    }

    private void addTilesToStage(Tile tile, int x, int y){
		Image image = new Image(spriteSheet.getTexture(tile));
		image.setSize(tilesize,tilesize);
		image.setPosition(x,y);
		stage.addActor(image);
	}

	private void addGameObjectsOnTileToStage(Tile tile, int x, int y){
		Image image;
    	if (tile.hasAnyGameObjects()){
			GameObject[] gameObjects = tile.getGameObjects();
			for(int i = 0; i < tile.getGameObjects().length; i++){
				GameObject gameObject = gameObjects[i];
				image = new Image(spriteSheet.getTexture(gameObject));
				image.setSize(tilesize,tilesize);
				image.setPosition(x,y);
				stage.addActor(image);
			}
		}
	}

    private void create(){
		int xPos = 0;
		int yPos = 0;
		Position pos;

		for (int y = yOffset; y < yOffset + boardTileHeight; y+= tilesize){
			for (int x = xOffset; x < xOffset + boardTileWidth; x+= tilesize){
				pos = new Position(xPos, yPos);
				Tile curTile = board.getTile(pos);
				addTilesToStage(curTile,x, y);
				addGameObjectsOnTileToStage(curTile, x, y);
				xPos++;
			}
			xPos = 0;
			yPos++;
		}
	}

	public void update(){
    	create();
	}

    /**
     * function that calls drawBoard for the actual drawing of the board
     * the function is called from RoboRally.render()
     */
    public void render() {
		stage.act();
		stage.draw();
    }

    /**
     * function that should resize the board
     * the function is called from RoboRally.resize()
     */
    public void resize(){
        reposition();

        //TODO - implement resize logic (maybe not needed becaus of the batch.setProjectMatrix)
        if (Gdx.graphics.getHeight() < Gdx.graphics.getWidth()){

        }else{

        }
    }


    /**
     * function that sets the position of the board.
     * is called from the constructor and resize();
     */
    public void reposition(){
        yOffset = 0;
        //yOffset = Gdx.graphics.getHeight()/2 - boardTileHeight/2;
        xOffset = Gdx.graphics.getWidth()/2 - boardTileWidth/2;
    }

    private void drawTile(Tile tile, int x, int y){
		SpriteType spriteType = tile.getSpriteType();
		textureRegion = spriteSheet.getTexture(spriteType);
		batch.draw(textureRegion, x, y, tilesize, tilesize);

	}


	private void drawGameObjects(Tile tile, int x, int y){
		if (tile.hasAnyGameObjects()){
			for(int i = 0; i < tile.getGameObjects().length; i++){
				IGameObject[] gameObjects = tile.getGameObjects();
				SpriteType gameObjectSpriteType = gameObjects[i].getSpriteType();
				textureRegion = spriteSheet.getTexture(gameObjectSpriteType);
				batch.draw(textureRegion, x, y, tilesize, tilesize);
			}
		}

	}

    /**
     * function that loops through the tiles of a Board and then calles the drawTiles() and drawGameObjects()
     *
     */
    public void drawBoard(){
        stage.act();
    	stage.draw();
    	/*batch.begin();
        int xPos = 0;
        int yPos = 0;
        Position pos;

        for (int y = yOffset; y < yOffset + boardTileHeight; y+= tilesize){
            for (int x = xOffset; x < xOffset + boardTileWidth; x+= tilesize){
                pos = new Position(xPos, yPos);
                Tile curTile = board.getTile(pos);
                drawTile(curTile, x, y);
                drawGameObjects(curTile, x, y);
                xPos++;
            }
            xPos = 0;
            yPos++;
        }

        batch.end();
        */
    }


}
