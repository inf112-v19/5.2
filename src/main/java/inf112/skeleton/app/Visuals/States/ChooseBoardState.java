package inf112.skeleton.app.Visuals.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.app.GameMechanics.Board.Board;
import inf112.skeleton.app.Visuals.RoboRally;
import inf112.skeleton.app.Visuals.SpriteSheet;
import inf112.skeleton.app.Visuals.SpriteType;

public class ChooseBoardState extends State {
    private SpriteSheet spriteSheet;
    private TextureRegion background1;

    private Stage stage;

    private int halfButtonWidth;
    private int bigButtonWidth;


    //private Image boardType;
    //private int boardTypes;
    private Image boardtype1;
    private Image boardtype2;
    private Image boardtype3;
    private String boardName;

    private boolean start;

    public ChooseBoardState(GameStateManager gsm, Board board) {
        super(gsm, board);

        this.spriteSheet = new SpriteSheet();
        this.stage = new Stage(new ScreenViewport());

        this.stage.getBatch().setProjectionMatrix(camera.combined);
        this.background1 = this.spriteSheet.getTexture(SpriteType.CHOOSE_BACKGROUND);

        this.start = false;
        this.halfButtonWidth = 193/2; //193 og ikke 191 fordi det passer bildet bedre
        this.bigButtonWidth = this.halfButtonWidth+193;
        //this.boardTypes = 3;

        setBoardTypes();

        /*
        for (int i = 1; i < boardTypes+1; i++) {
            setBoardTypes(i, boardName);
            clickable(i, boardName);
        } */

    }

    private void setBoardTypes() {
        //first board
        this.boardtype1 = new Image(new TextureRegionDrawable(new Texture("StateImages/board1.png")));
        this.boardtype1.setSize(191, 49);
        this.boardtype1.setPosition(this.halfButtonWidth, RoboRally.HEIGHT/2);
        this.stage.addActor(this.boardtype1);
        clickable(this.boardtype1, "Boards/BigBoard.txt");

        //second board
        this.boardtype2 = new Image(new TextureRegionDrawable(new Texture("StateImages/board2.png")));
        this.boardtype2.setSize(191, 49);
        this.boardtype2.setPosition((this.halfButtonWidth + this.bigButtonWidth), RoboRally.HEIGHT/2);
        this.stage.addActor(this.boardtype2);
        clickable(this.boardtype2, "2");

        //third board
        this.boardtype3 = new Image(new TextureRegionDrawable(new Texture("StateImages/board3.png")));
        this.boardtype3.setSize(191, 49);
        this.boardtype3.setPosition((this.halfButtonWidth + ((this.bigButtonWidth)*2)), RoboRally.HEIGHT/2);
        this.stage.addActor(this.boardtype3);
        clickable(this.boardtype3, "3");
    }

    /**
     * set "board types"
     */
    /*private void setBoardTypes(int nBoard, String boardName) {
        String filename = "board" + (nBoard);
        this.boardType = new Image(new TextureRegionDrawable(new Texture("StateImages/" + filename + ".png")));
        this.boardType.setSize(191, 49);
        this.boardType.setPosition(this.modifiedWidth, RoboRally.HEIGHT/2);
        this.stage.addActor(this.boardType);

        this.modifiedWidth += 191 + (191/2);
    }*/

    private void clickable(Image boardType, final String boardName) {
    //private void clickable(Image boardType, final String boardName) {
        boardType.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Board " + boardName + " was chosen!");
                saveBoardName(boardName);
                start = true;
                return true;
            }
        });
        Gdx.input.setInputProcessor(this.stage);
    }

    private String saveBoardName(String boardName) {
        this.boardName = boardName;
        return this.boardName;
    }

    public String getBoardName() {
        return this.boardName;
    }

    @Override
    public void handleInput() {
        if (this.start) {
            //System.out.println(getBoardName());
            gsm.set(new ChoosePlayerState(gsm, board));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render() {
        this.stage.act();
        this.stage.getBatch().begin();
        this.stage.getBatch().draw(this.background1, 0, 0, RoboRally.WIDTH, RoboRally.HEIGHT);
        this.stage.getBatch().end();
        this.stage.draw();
    }

    @Override
    public void dispose() {
        this.spriteSheet.dispose();
    }

    @Override
    public void resize() {
        super.resize();
        this.stage.getBatch().setProjectionMatrix(camera.combined);
    }
}