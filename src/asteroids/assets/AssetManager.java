package asteroids.assets;

import java.awt.Polygon;
import java.util.Random;

import utility.Utility;

/**
 * AssetManager class
 */
public final class AssetManager {
	
	/**
	 * Creates the playerShape by adding points to the polygon.
	 * pre: player is not null.
	 * post: Player points have been added.
	 */
	public final static void addPlayerPoints(Polygon player) {
		
		player.addPoint(30, 10);
		player.addPoint(0, 0);
		player.addPoint(8, 10);
		player.addPoint(0, 20);
		
	}

	/**
	 * Creates the exhaustShape by adding points to the polygon.
	 * pre: exhaust is not null.
	 * post: Exhaust points have been added.
	 */
	public final static void addPlayerExhaustPoints(Polygon exhaust) {
		
		exhaust.addPoint(8, 10);
		exhaust.addPoint(6, 7);
		exhaust.addPoint(-3, 10);
		exhaust.addPoint(6, 13);
		
	}

	/**
	 * Creates a rotated playerShape by adding points to the polygon.
	 * pre: shape is not null.
	 * post: Shape points have been added.
	 */
	public final static void addLivesLeftPoints(Polygon shape) {
		
		shape.addPoint(10, 0);
		shape.addPoint(0, 30);
		shape.addPoint(10, 22);
		shape.addPoint(20, 30);
		
	}
	
	/**
	 * Returns the angles of the playerShape
	 * pre: n/a
	 * post: Angles have been returned
	 */
	public final static double[] getPlayerAngles() {
		
		double[] angles = new double[4];
		
		angles[0] = (Math.atan2(10, 30) / Math.PI) * 180 - 180;
		angles[1] = (Math.atan2(-10, 8) / Math.PI) * 180 + 100;
		angles[2] = (Math.atan2(10, 8) / Math.PI) * 180 + 75;
		angles[3] = (Math.atan2(-10, 30) / Math.PI) * 180;
		
		return angles;
		
	}
	
	/**
	 * Returns the lengths of the side of the player.
	 * pre: n/a
	 * post: Lengths have been returned.
	 */
	public final static int[] getPlayerLengths() {
		
		int[] lengths = new int[4];
		
		lengths[0] = 32;
		lengths[1] = 13;
		lengths[2] = 13;
		lengths[3] = 32;
		
		return lengths;
		
	}
	
	/**
	 * Creates an projectile Shape by adding points to the polygon.
	 * pre: projectile is not null.
	 * post: Projectile points have been added.
	 */
	public final static void addProjectilePoints(Polygon projectile) {
		
		projectile.addPoint(0, 0);
		projectile.addPoint(15, 0);
		
	}

	/**
	 * Creates an asteroid Shape by adding points to the polygon.
	 * pre: asteroid is not null.
	 * post: Asteroid points have been added.
	 */
	public final static void addAsteroidPoints(Random random, Polygon asteroid, double size) {
		
		addRandomRangePoint(random, asteroid, 20, 0, size);
		addRandomRangePoint(random, asteroid, 0, 10, size);
		addRandomRangePoint(random, asteroid, 0, 15, size);
		addRandomRangePoint(random, asteroid, 0, 25, size);
		addRandomRangePoint(random, asteroid, 20, 30, size);
		addRandomRangePoint(random, asteroid, 35, 15, size);
		addRandomRangePoint(random, asteroid, 25, 10, size);
		
	}
	
	/**
	 * Adds a random point from a range.
	 * pre: asteroid is not null. random is not null.
	 * post: Asteroid point has been added.
	 */
	private final static void addRandomRangePoint(Random random, Polygon asteroid, int newX, int newY, double size) {
		
		int x, y;
		
		x = (int) (newX * size + Utility.getRandomFromRange(random, -2 * size, 2 * size));
		y = (int) (newY * size + Utility.getRandomFromRange(random, -2 * size, 2 * size));
		
		asteroid.addPoint(x, y);
		
	}

	/**
	 * Creates the alienShape by adding points to the polygon.
	 * pre: alien is not null.
	 * post: Alien points have been added.
	 */
	public final static void addAlienPoints(Polygon alien) {
		
		
		alien.addPoint(20, 0);
		alien.addPoint(10, 0);
		alien.addPoint(20, 0);
		alien.addPoint(20, -2);
		alien.addPoint(18, -3);
		alien.addPoint(16, -4);
		alien.addPoint(14, -4);
		alien.addPoint(12, -3);
		alien.addPoint(10, -2);
		alien.addPoint(10, 0);
		alien.addPoint(0, 5);
		alien.addPoint(10, 10);
		alien.addPoint(20, 10);
		alien.addPoint(30, 5);
		alien.addPoint(0, 5);
		alien.addPoint(30, 5);
		
	}


}
