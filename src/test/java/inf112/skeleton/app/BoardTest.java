package inf112.skeleton.app;

import inf112.skeleton.app.GameMechanics.Board.Board;
import inf112.skeleton.app.GameMechanics.Direction;
import inf112.skeleton.app.GameMechanics.Player;
import inf112.skeleton.app.GameMechanics.Position;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class BoardTest {
	private Board testBoard;
	private Player[] players;

	/**
	 * Setup method for creating a board to test with containing two players
	 *
	 */
	@Before
	public void setUp() {
		testBoard = new Board("Boards/ExampleBoard.txt");

		Player player0 = new Player(0,"Player 0", Direction.EAST);
		Player player1 = new Player(1,"Player 1", Direction.EAST);
		player0.setBackup(new Position(1, 4));
		player1.setBackup(new Position(1, 11));
		testBoard.placePlayerOnPos(player0, new Position(0, 4));
		testBoard.placePlayerOnPos(player1, new Position(0, 3));

		players = testBoard.getAllPlayers();
	}

	/**
	 * TearDown method that resets the position of the players
	 *
	 */
	@After
	public void tearDown() {
		players[0].setBackup(new Position(1, 4));
		players[1].setBackup(new Position(1, 11));
		testBoard.placePlayerOnPos(players[0], new Position(0, 4));
		testBoard.placePlayerOnPos(players[1], new Position(0, 3));
		players[0].setPlayerDirection(Direction.EAST);
		players[1].setPlayerDirection(Direction.EAST);
	}


	/**
	 * Testing that the posToPlayer returns the correct player - should player[0] since we know that the position
	 * of this player is (0, 0)
	 */
	@Test
	public void posToPlayerPlayer0Test() {
		Player shouldBePlayer0 = testBoard.posToPlayer(new Position(0,4));
		assertEquals(shouldBePlayer0, players[0]);
	}

	/**
	 * Testing that the posToPlayer returns null when given a position with no player present
	 */
	@Test
	public void posToPlayerNullTest() {
		Player shouldBeNull = testBoard.posToPlayer(new Position(5,5));
		assertEquals(shouldBeNull, null);
	}


	/**
	 * Testing that the isValidPos works returns false when given a position outside the board
	 */
	@Test
	public void isValidPosFalseTest() {
		Position outsideBoard = new Position(-1, -1);
		assertFalse(testBoard.isValidPos(outsideBoard));
	}

	/**
	 * Testing that the isValidPos works returns true when given a valid position on the board
	 */
	@Test
	public void isValidPosTrueTest() {
		Position outsideBoard = new Position(0, 0);
		assertTrue(testBoard.isValidPos(outsideBoard));
	}


	/**
	 * Testing that it is possible to move player[0] east two times and then south - should move without
	 * any collision or exception
	 *
	 */
	@Test
	public void movePlayer0SouthTest() {
		testBoard.movePlayer(players[0], Direction.EAST);
		testBoard.movePlayer(players[0], Direction.EAST);
		testBoard.movePlayer(players[0], Direction.SOUTH);
		Position newPos = testBoard.getPlayerPos(players[0]);

		assertEquals(new Position(2,3), newPos);
	}

	/**
	 * Testing that it is possible to move player[0] south - should move and collide with player[1] pushing
	 * it to tile (0,2)
	 *
	 */
	@Test
	public void movePlayerCollisionTest() {
		testBoard.movePlayer(players[0], Direction.SOUTH);
		Position newPos = testBoard.getPlayerPos(players[1]);

		assertEquals(new Position(0,2), newPos);
	}

	/**
	 * Testing that it is not possible to move a player where there is a wall - player[0] should still be
	 * in its starting position (0,0)
	 *
	 */
	@Test
	public void movePlayerWallTest() {
		testBoard.movePlayer(players[0], Direction.WEST);
		Position newPos = testBoard.getPlayerPos(players[0]);

		assertEquals(new Position(0,4), newPos);
	}

	/**
	 * Testing that the getAllPlayers return an array of correct size
	 */
	@Test
	public void getAllPlayers() {
		Player[] players = testBoard.getAllPlayers();
		assertEquals(players.length, 2);
	}

	/**
	 * Testing player1 walking twice south - checking that the final position is correct
	 */
	@Test
	public void playerWalkingTwice() {
		testBoard.movePlayer(players[1], Direction.SOUTH, 2);

		while (testBoard.doNextAction()){}

		Position newPos = testBoard.getPlayerPos(players[1]);

		assertEquals(new Position(0,1), newPos);

	}

	/**
	 * Testing player walking off the board - players onBoardCheck should return false
	 */
	@Test
	public void playerWalkedOffTheBoard() {
		testBoard.movePlayer(players[0], Direction.NORTH);

		assertFalse(players[0].onBoardCheck());
	}

	/**
	 * Testing player falling into a hole - players onBoardCheck should return false
	 */
	@Test
	public void playerFellInHole() {
		testBoard.movePlayer(players[0], Direction.EAST);
		testBoard.movePlayer(players[0], Direction.SOUTH);

		assertFalse(players[0].onBoardCheck());
	}

	/**
	 * Testing player getting moved by conveyor belt tile - starts recursive call of the doNextAction to move player0
	 * two times east, making him stand on a conveyor belt and then confirming that the final position is correct after
	 * being moved by the conveyor belt east once.
	 */
	@Test
	public void playerStandingOnConveyorTile() {
		testBoard.movePlayer(players[0], Direction.EAST, 2);
		players[0].setReady();

		while (testBoard.doNextAction()){}

		Position newPos = testBoard.getPlayerPos(players[0]);

		assertEquals(new Position(3,4), newPos);
	}

	/**
	 * Testing player taking damage from laser - walks player 1 to a tile with a laser and checks that the health decreases
	 */
	@Test
	public void playerTakingLaserDamage() {
		int originHealth = players[1].getDamage();

		testBoard.movePlayer(players[1], Direction.SOUTH);
		testBoard.movePlayer(players[1], Direction.EAST);
		players[1].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(originHealth+1, players[1].getDamage());
	}

	/**
	 * Testing player getting moved by double conveyor belt tile - walks player to double conveyor belt tile and checks
	 * that the final position is correct
	 */
	@Test
	public void playerStandingOnDoubleConveyorTile() {
		testBoard.placePlayerOnPos(players[0], new Position(1,2));
		testBoard.movePlayer(players[0], Direction.EAST);
		players[0].setReady();

		while (testBoard.doNextAction()){}

		Position newPos = testBoard.getPlayerPos(players[0]);

		assertEquals(new Position(4,2), newPos);
	}

	/**
	 * Testing player collecting flag - player0 should collect the flag and the numberOfFlagsCollected should return 1
	 */
	@Test
	public void collectFlagTest() {
		testBoard.movePlayer(players[0], Direction.EAST);
		players[0].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(1, players[0].numberOfFlagsCollected());
	}

	/**
	 * Testing that the player does not collect the same flag twice - numberOfFlagsCollected should still return 1
	 */
	@Test
	public void collectFlagTwiceTest() {
		testBoard.movePlayer(players[0], Direction.EAST);
		players[0].setReady();

		while (testBoard.doNextAction()){}

		testBoard.movePlayer(players[0], Direction.EAST);
		testBoard.movePlayer(players[0], Direction.WEST);

		players[0].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(1, players[0].numberOfFlagsCollected());
	}

	/**
	 * Testing player getting rotated by left rotation tile - original player direction is EAST and therefore the new
	 * player direction should be WEST (since there are 2 players on the board it will trigger the doTileAction twice
	 */
	@Test
	public void playerStandingOnLeftRotationTile() {
		testBoard.placePlayerOnPos(players[0], new Position(3,4));
		players[0].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(Direction.NORTH, players[0].getDirection());
	}


	/**
	 * Testing player getting rotated by left rotation tile - original player direction is EAST and therefore the new
	 * player direction should be SOUTH
	 */
	@Test
	public void playerStandingOnRightRotationTile() {
		testBoard.placePlayerOnPos(players[0], new Position(4,4));
		players[0].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(Direction.SOUTH, players[0].getDirection());
	}


	/**
	 * Testing player getting repaired by RepairTile - first decreases health and then places player on RepairTile,
	 * player health should be 10
	 */
	@Test
	public void playerStandingOnRepairTileWhenDamaged() {
		players[0].increaseDamage();
		players[0].increaseDamage();
		players[0].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(0, players[0].getDamage());
	}

	/**
	 * Testing player getting repaired by RepairTile when health is full - health should still be 10 (not 11)
	 */
	@Test
	public void playerStandingOnRepairTileWhenNotDamaged() {
		players[0].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(0, players[0].getDamage());
	}

	/**
	 * Testing that player backup change when standing on a tile with a flag - backup should be position (1,0)
	 */
	@Test
	public void playerBackupChangedByFlag() {

		testBoard.placePlayerOnPos(players[0], new Position(1, 4));
		players[0].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(new Position(1,4), players[0].getBackup());
	}

	/**
	 * Testing that player backup change when standing on a OptionTile - backup should be position (4,2)
	 */
	@Test
	public void playerBackupChangedByOptionTile() {

		testBoard.placePlayerOnPos(players[0], new Position(4, 2));
		players[0].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(new Position(4,2), players[0].getBackup());
	}

	/**
	 * Testing that player health gets increased by option tile - first decreases health and then places player
	 * on optionTile, player health should be 10
	 */
	@Test
	public void playerHealthIncreasedByOptionTile() {
		players[0].increaseDamage();
		testBoard.placePlayerOnPos(players[0], new Position(4, 2));
		players[0].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(0, players[0].getDamage());
	}


	/**
	 * Testing that players shooting each other both take damage - both players should have equal health and their combined
	 * health should be 2 (1+1). Places player both players on NormalTiles facing each other so they both will get hit.
	 */
	@Test
	public void playersShootingEachOther() {
		players[0].setPlayerDirection(Direction.SOUTH);
		players[1].setPlayerDirection(Direction.NORTH);
		testBoard.placePlayerOnPos(players[0], new Position(0,3));
		testBoard.placePlayerOnPos(players[1], new Position(0,2));
		players[0].setReady();
		players[1].setReady();

		while (testBoard.doNextAction()){}

		int player0Health = players[0].getDamage();
		int player1Health = players[0].getDamage();

		if (player0Health == player1Health) {
			assertEquals(2, player0Health+player1Health);
		}else{
			assert false;
		}
	}


	/**
	 * Testing that the powerdown is changed from 2(requesting powerdown next round) to 1 (powerdown this round) after
	 * a round is over
	 */
	@Test
	public void powerDownChangedTo1() {
		players[0].setPowerDown(2);
		players[0].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(players[0].getPowerDown(), 1);
	}


	/**
	 * Testing that the powerdown is changed from 2(requesting powerdown next round) to 3 (option to cancel powerdown)
	 * when a player gets destroyed (walks off the board) during a round
	 */
	@Test
	public void powerDownChangedTo3() {
		players[0].setPowerDown(2);
		testBoard.movePlayer(players[0], Direction.NORTH);
		players[0].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(players[0].getPowerDown(), 3);
	}


	/**
	 * Testing that the player damage was reset when in powerdown and that the powerdown was changed back to 0 after
	 * the round
	 */
	@Test
	public void powerDownRemovedDamage() {
		players[0].setPowerDown(1);
		players[0].increaseDamage();
		players[0].setReady();

		while (testBoard.doNextAction()){}

		assertEquals(players[0].getPowerDown(), 0);
		assertEquals(players[0].getDamage(), 0);
	}

}
