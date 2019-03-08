package inf112.skeleton.app.Visuals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.app.GameMechanics.Cards.Card;
import inf112.skeleton.app.GameMechanics.GameObjects.GameObject;
import inf112.skeleton.app.GameMechanics.Player;
import inf112.skeleton.app.GameMechanics.Tiles.Tile;

/**
 * This class handles all the sprites used in the game
 */
public final class SpriteSheet {

	private Texture texture;

	//CardGUI
	private final TextureRegion cardGuiClearButton;
	private final TextureRegion cardGuiSubmitButton;
	private final TextureRegion cardBar;

	//Div Sprites
	private static TextureRegion tapToStartSprite;

	//Player Sprites
	private static TextureRegion playerSpriteNorth;
	private static TextureRegion playerSpriteSouth;
	private static TextureRegion playerSpriteEast;
	private static TextureRegion playerSpriteWest;

	//Menu
	private static TextureRegion menuBackground;
	private static TextureRegion menuPlayButton;

	//Card Sprites
	private static TextureRegion backUpSprite;
	private static TextureRegion leftTurnSprite;
	private static TextureRegion move1Sprite;
	private static TextureRegion move2Sprite;
	private static TextureRegion move3Sprite;
	private static TextureRegion rightTurnSprite;
	private static TextureRegion uTurnSprite;

	//The spriteSheet
	private static TextureRegion[][] spriteSheet;



	public SpriteSheet(){

		this.texture = new Texture("RoboRallyTiles.png");
		this.spriteSheet = new TextureRegion(texture,336,624).split(336/7, 624/13);
		flip();

		this.texture = new Texture("CardImages/BackUp.png");
		this.backUpSprite = new TextureRegion(texture);
		//this.backUpSprite.flip(false, true);

		this.texture = new Texture("CardImages/LeftTurn.png");
		this.leftTurnSprite = new TextureRegion(texture);
		//this.leftTurnSprite.flip(false, true);

		this.texture = new Texture("CardImages/Move1.png");
		this.move1Sprite = new TextureRegion(texture);
		//this.move1Sprite.flip(false, true);

		this.texture = new Texture("CardImages/Move2.png");
		this.move2Sprite = new TextureRegion(texture);
		//this.move2Sprite.flip(false, true);

		this.texture = new Texture("CardImages/Move3.png");
		this.move3Sprite = new TextureRegion(texture);
		//this.move3Sprite.flip(false, true);

		this.texture = new Texture("CardImages/RightTurn.png");
		this.rightTurnSprite = new TextureRegion(texture);
		//this.rightTurnSprite.flip(false, true);

		this.texture = new Texture("CardImages/U-Turn.png");
		this.uTurnSprite = new TextureRegion(texture);
		//this.uTurnSprite.flip(false, true);

		this.texture = new Texture("StateImages/tapToStart.gif");
		this.tapToStartSprite = new TextureRegion(texture);
		//this.tapToStartSprite.flip(false, true);

		this.texture = new Texture("player.png");
		this.playerSpriteNorth = new TextureRegion(texture);
		this.playerSpriteNorth.flip(false,false);
		this.playerSpriteSouth = new TextureRegion(texture);
		this.playerSpriteSouth.flip(true,false);
		this.playerSpriteEast = new TextureRegion(texture);
		this.playerSpriteEast.flip(false,true);
		this.playerSpriteWest = new TextureRegion(texture);
		this.playerSpriteWest.flip(true,true);

		this.texture = new Texture("StateImages/tempBackground.jpg");
		this.menuBackground = new TextureRegion(texture);
		//this.menuBackground.flip(false, true);

		this.texture = new Texture("StateImages/tapToStart.gif");
		this.menuPlayButton = new TextureRegion(texture);
		//this.menuPlayButton.flip(false, true);

		this.texture = new Texture("clear.png");
		this.cardGuiClearButton = new TextureRegion(texture);
		//this.cardGuiClearButton.flip(false, true);

		this.texture = new Texture("submit.png");
		this.cardGuiSubmitButton = new TextureRegion(texture);
		//this.cardGuiSubmitButton.flip(false, true);

		this.texture = new Texture("CardImages/cardBar.png");
		this.cardBar = new TextureRegion(texture);


	}


	/**
	 * Private method that flips the textures in the spriteSheet.
	 * This is necessary because 0,0 is top left and therefore the sprites are drawn
	 * upside down. Flipping the textures is a fix fore this
	 */
	private void flip(){
		for (int i = 0; i < spriteSheet.length; i++){
			for(int j = 0; j < spriteSheet[i].length; j++){
				spriteSheet[i][j].flip(false, true);
			}
		}
	}

	/**
	 * Private method that finds the correct texture for the given SpriteType
	 * @param spriteType
	 * @return TextureRegion for a given SpriteType
	 */
	private TextureRegion findCorrectTexture(SpriteType spriteType){
		if(spriteType.isUsingCoordinates()){
			return spriteSheet[spriteType.getY()][spriteType.getX()];
		}
		switch (spriteType){
			case BACKWARD_1:
				return backUpSprite;
			case FORWARD_1:
				return move1Sprite;
			case FORWARD_2:
				return move2Sprite;
			case FORWARD_3:
				return move3Sprite;
			case ROTATE_180:
				return uTurnSprite;
			case ROTATE_90_L:
				return leftTurnSprite;
			case ROTATE_90_R:
				return rightTurnSprite;
			case MENU_BACKGROUND:
				return menuBackground;
			case MENU_PLAY_BUTTON:
				return menuPlayButton;
			case CARD_SUBMIT:
				return cardGuiSubmitButton;
			case CARD_CLEAR:
				return cardGuiClearButton;
			case CARD_BAR:
				return cardBar;
			default:
				System.err.println("No sprite found");
				return null;
		}
	}


	/**
	 * method that return correct sprite for a given player and it's direction
	 * @param player
	 * @return TextureRegion for a given Player
	 */
	public TextureRegion getTexture(Player player){
		switch (player.getDirection()){
			case NORTH:
				return playerSpriteNorth;
			case SOUTH:
				return playerSpriteSouth;
			case EAST:
				return playerSpriteEast;
			case WEST:
				return playerSpriteWest;
		}
		return null;
	}

	/**
	 * Method that return correct sprite for a given SpriteType.
	 * @param spriteType
	 * @return TextureRegion for a given SpriteType
	 */
	public TextureRegion getTexture(SpriteType spriteType){
		return findCorrectTexture(spriteType);
	}

	/**
	 * Method that return correct sprite for a given Tile
	 * @param card
	 * @return TextureRegion for the given Tile
	 */
	public TextureRegion getTexture(Card card){
		return findCorrectTexture(card.getSprite());
	}

	/**
	 * Method that return correct sprite for a given Tile
	 * @param tile
	 * @return TextureRegion for the given GameObject
	 */
	public TextureRegion getTexture(Tile tile){
		return findCorrectTexture(tile.getSpriteType());
	}

	/**
	 * Method that return correct sprite for a given GameObject.
	 * @param gameObject
	 * @return TextureRegion for the given GameObject
	 */
	public TextureRegion getTexture(GameObject gameObject){
		return findCorrectTexture(gameObject.getSpriteType());
	}

	public void dispose(){
		this.texture.dispose();
	}



}
