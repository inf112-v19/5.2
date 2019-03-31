package inf112.skeleton.app.Visuals.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Queue;
import inf112.skeleton.app.GameMechanics.Board.Board;
import inf112.skeleton.app.GameMechanics.Cards.CardManager;
import inf112.skeleton.app.GameMechanics.Player;
import inf112.skeleton.app.GameMechanics.Position;
import inf112.skeleton.app.GameMechanics.Tiles.Tile;
import inf112.skeleton.app.Visuals.AssetHandler;
import inf112.skeleton.app.Visuals.BoardGUI;
import inf112.skeleton.app.Visuals.Text;


public class SpawnPointState extends State {
	private Board board;
	private Queue<Player> players;
	private AssetHandler assetHandler;
	private BoardGUI boardGUI;
	private Text text;


	public SpawnPointState(GameStateManager gsm, Board board, Queue<Player> players){
		super(gsm);
		this.players = players;
		this.board = board;
		this.assetHandler = new AssetHandler();
		this.boardGUI = new BoardGUI(board, super.camera, super.stage, gsm);
		this.boardGUI.addListenersToStage();
		this.text = new Text("'s turn to to choose spawn", stage);
		this.text.prependDynamicsText(players.first().getPlayerID());
	}

	@Override
	protected void handleInput() {

	}

	@Override
	public void update(float dt) {
		Gdx.input.setInputProcessor(stage);
		if (players.isEmpty()){
			System.out.println("setting PlaceFlagState");
			CardManager cardManager = new CardManager(board);
			boardGUI.removeAllListeners();
			gsm.set(new PlaceFlagState(this.gsm, this.board, cardManager));
			text.dispose();
		}
	}

	@Override
	public void tileEventHandle(Tile tile) {
		if (!players.isEmpty()) {
			float x = (tile.getX() - boardGUI.getxOffset())/(tile.getWidth());
			float y = (tile.getY() - boardGUI.getyOffset())/(tile.getHeight());
			Position playerPos = new Position((int)x, (int)y);

			Player player = players.first();
			if (board.spawnPlayer(playerPos, player)){
				players.removeFirst();
				player.setDrawable(new TextureRegionDrawable(assetHandler.getTexture(player)));
				player.setSize(tile.getWidth(), tile.getHeight());
				player.setPosition(tile.getX(), tile.getY());
				stage.addActor(player);
				System.out.println("placing " + player.getPlayerID());

				if(!players.isEmpty()){
					this.text.prependDynamicsText(players.first().getPlayerID());
				}

			}
		}
	}
}
