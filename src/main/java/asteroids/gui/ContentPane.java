package asteroids.gui;

import java.awt.Graphics;

import javax.swing.JPanel;

import asteroids.Asteroids;

/**
 * Overriden JPanel class
 */
public class ContentPane extends JPanel {

	private Asteroids game;

	/**
	 * A constructor
	 * pre: game is not equal to null.
	 * post: local game has been set to game.
	 */
	public ContentPane(Asteroids game) {
		this.game = game;
	}

	/**
	 * Draws the program.
	 * pre: g is not equal to null.
	 * post: The program has been drawn.
	 */
	@Override
	protected void paintComponent(Graphics g) {

		game.draw(g);

	}

}
