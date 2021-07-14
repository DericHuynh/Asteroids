package asteroids.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import asteroids.assets.AssetManager;

public class Asteroid extends Entity {

	final static Color fillColor = new Color(100, 100, 100, 200);
	private double size;
	private double rotationSpeed;
	
	public Asteroid(Random random, double x, double y, double speed, double angle, double size, double rotationSpeed) {
		
		super();
		
		AssetManager.addAsteroidPoints(random, entityShape, size);
		
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.angle = angle;
		this.size = size;
		this.rotationSpeed = rotationSpeed;
		
	}
	
	public void update(double delta) {
		
		double xVelocity = (speed * Math.cos(Math.toRadians(this.angle)) * delta);
		double yVelocity = (speed * Math.sin(Math.toRadians(this.angle)) * delta);
		
		this.rotate(rotationSpeed * delta);;
		
		x += xVelocity;
		y += yVelocity;
		
		this.updateRotatedShape();
		
	}
	
	public void draw(Graphics g) {
		
		g.setColor(Color.WHITE);
		g.drawPolygon(rotatedEntityShape);
		
	}

	public double getSize() {
		return size;
	}
	


}
