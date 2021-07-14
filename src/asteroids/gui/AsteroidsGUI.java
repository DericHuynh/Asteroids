package asteroids.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import asteroids.Asteroids;

/**
 * AsteroidsGUI class.
 */
public class AsteroidsGUI extends Thread implements WindowListener {
	
	private Asteroids game;
	private JFrame frame;
	private ContentPane contentPane;
	private ImageIcon icon;
	
	/**
	 * A constructor
	 * pre: game is not equal to null.
	 * post: Gui has been created and run.
	 */
	public AsteroidsGUI(Asteroids game) {
		
		URL imageURL = this.getClass().getResource("/asteroids/resources/icon.png");
		icon = new ImageIcon(imageURL);
		frame = new JFrame("Asteroids");
		frame.setPreferredSize(new Dimension(1280, 720));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.black);
		frame.addKeyListener(game);
		frame.addWindowListener(this);
		frame.setIconImage(icon.getImage());
		
		this.game = game;
		contentPane = new ContentPane(game);
		
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setVisible(true);
		
		synchronized (this) {
			this.notify();
		}
		
	}
	
	/**
	 * Returns frame width.
	 * pre: n/a
	 * post: Fram width returned
	 */
	public int getWidth() {
		
		return frame.getWidth();
		
	}

	/**
	 * Returns frame height.
	 * pre: n/a
	 * post: Frame height returned.
	 */
	public int getHeight() {
	
		return frame.getHeight();
	
	}
	
	/**
	 * Repaint the frame
	 * pre: n/a
	 * post: Frame has been repainted
	 */
	public void repaint() {
		
		frame.repaint();
		
	}
	
	/**
	 * Disposes the frame.
	 * pre: n/a
	 * post: Frame has been disposed.
	 */
	public void exit() {

		frame.setVisible(false);
		frame.dispose();
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	/**
	 * Request to exit game when closing gui.
	 * pre: n/a
	 * post: Game has been flagged to close.
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		
		game.exit();
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
	
}
