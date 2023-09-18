package asteroids.assets;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import asteroids.entities.Entity;
import utility.Utility;

/**
 * Particle class.
 */
public class Particle extends Entity {

	/**
	 * A constructor.
	 * pre: n/a
	 * post:
	 */
	public Particle(double x, double y) {

		super();

		entityShape.addPoint(0, 0);
		entityShape.addPoint(1, 0);

		this.speed = 5;
		this.angle = -180;
		this.x = x;
		this.y = y;

	}

	public Particle(Random random, double x, double y) {

		super();

		entityShape.addPoint(0, 0);
		entityShape.addPoint(1, 0);

		this.speed = random.nextDouble() * 2;
		this.angle = Utility.getRandomFromRange(random, -180, 180);
		this.x = x;
		this.y = y;

	}

	public void drag(double delta) {

		speed -= (speed / 32) * delta;

	}

	@Override
	public void update(double delta) {

		drag(delta);

		double xVelocity = (speed * Math.cos(Math.toRadians(this.angle)) * delta);
		double yVelocity = (speed * Math.sin(Math.toRadians(this.angle)) * delta);

		x += xVelocity;
		y += yVelocity;

		if(speed < 0.2) {
			this.setAlive(false);
		}

		this.updateRotatedShape();

	}

	@Override
	public void draw(Graphics g) {

		g.setColor(Color.white);
		g.drawPolygon(rotatedEntityShape);

	}

}
