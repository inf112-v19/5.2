package inf112.skeleton.app.GUI;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.app.Board.Board;
import inf112.skeleton.app.Position;
import inf112.skeleton.app.Tiles.Tile;


public class BoardGUI {

    private Board board;
    private int xOffset;
    private int yOffset;
    private OrthographicCamera camera;
    private SpriteBatch batch;


    private int tilesize;
    private int boardWidth;
    private int boardHeight;
    private int boardTileWidth;
    private int boardTileHeight;

    private Texture texture = new Texture("RoboRallyTiles.png");
    private TextureRegion[][] spriteSheet = new TextureRegion(texture,336,624).split(336/7, 624/13);


    public BoardGUI(OrthographicCamera camera, SpriteBatch batch) {
        this.camera = camera;
        this.batch = batch;

        this.board = new Board("BoardBuilderTest2.txt");
        this.boardWidth = board.getWidth();
        this.boardHeight = board.getHeight();

        this.tilesize = Math.min(Gdx.graphics.getHeight(), Gdx.graphics.getWidth())/(Math.min(boardWidth, boardHeight)*5);

        this.boardTileWidth = boardWidth *tilesize;
        this.boardTileHeight = boardHeight * tilesize;

        //setting initial position
        reposition();

    }

    /**
     * function that calls drawBoard for the actual drawing of the board
     * the function is called from RoboRally.render()
     */
    public void render() {
        drawBoard();

    }

    /**
     * function that should resize the board
     * the function is called from RoboRally.resize()
     */
    public void resize(){
        this.camera.setToOrtho(false);
        this.batch.setProjectionMatrix(camera.combined);
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
        this.yOffset = Gdx.graphics.getHeight()/2 - boardTileHeight/2;
        this.xOffset = Gdx.graphics.getWidth()/2 - boardTileWidth/2;
    }

    /**
     * function that draws the board using the spriteSheet and a double for-loop
     * the function is getting Tiles on Positions, and then getting right x and y
     * coordinates for the sprite sheet.
     *
     */
    public void drawBoard(){
        batch.begin();
        int xPos = 0;
        int yPos = 0;

        for (int y = yOffset; y < yOffset + boardTileHeight; y+= tilesize){
            for (int x = xOffset; x < xOffset + boardTileWidth; x+= tilesize){
                Position pos = new Position(xPos, yPos);
                Tile curTile = board.getTile(pos);
                int spriteX = curTile.getSpriteX();
                int spriteY = curTile.getSpriteY();
                TextureRegion tileSprite = spriteSheet[spriteY][spriteX];
                batch.draw(tileSprite, x, y, tilesize, tilesize);
                xPos++;
            }
            xPos = 0;
            yPos++;
        }

        batch.end();
    }


}
