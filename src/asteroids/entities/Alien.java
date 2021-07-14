package asteroids.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import asteroids.Asteroids;
import asteroids.assets.AssetManager;

public class Alien extends Entity {

	private Random random;
	private double distanceGoal;
	private double angleGoal;
	private double goalTimeElapsed;
	private double accelerationAngle;
	private double fireRate;
	private double fireTimeElapsed;
	private boolean isTimeToFire;

	public Alien(Random random, double x, double y, double speed, double angle) {
		
		super();
		
		AssetManager.addAlienPoints(entityShape);
		
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.angle = angle;
		this.accelerationAngle = angle;
		this.random = random;
		this.distanceGoal = random.nextDouble() * 140 + 40;
		this.angleGoal = random.nextDouble() * 360;
		this.rotation = 0;
		this.isTimeToFire = false;
		this.goalTimeElapsed = 0;
		this.fireTimeElapsed = 0;
		this.fireRate = 1;
		
	}
	
	public void rotateAcceleration(double delta) {
		double newRotation;
		
		newRotation = accelerationAngle - delta;
		
		if(newRotation >= 360.0) {
			newRotation -= 360.0;
		} else if(newRotation < 0) {
			newRotation += 360.0; 
		}
		
		accelerationAngle = newRotation;
		
	}
	
	public void accelerate(double acceleration) {
		
		double Xvector = speed * Math.cos(Math.toRadians(this.angle));
		double Yvector = speed * Math.sin(Math.toRadians(this.angle));
		
		double accelXvector = acceleration * Math.cos(Math.toRadians(accelerationAngle));
		double accelYvector = acceleration * Math.sin(Math.toRadians(accelerationAngle));
		
		Xvector += accelXvector;
		Yvector += accelYvector;
		
		angle = (float) Math.toDegrees(Math.atan2(Yvector, Xvector));
		speed = Math.min(4.5, Math.sqrt(Xvector * Xvector + Yvector * Yvector));
		
	}
	
	public void drag(double delta) {
		
		speed -= (speed / 48) * delta;
		
	}
	

	public void update(double delta) {
		
		this.accelerate(0.13 * delta);
		
		if(!isTimeToFire) {
			fireTimeElapsed += delta;
			if(fireTimeElapsed >= Asteroids.TARGET_FPS / fireRate) {
				isTimeToFire = true;
				fireTimeElapsed = 0;
			}
		}
		
		goalTimeElapsed += delta;
		
		if(goalTimeElapsed >= Asteroids.TARGET_FPS * 3) {
			this.distanceGoal = random.nextDouble() * 160 + 80;
			this.angleGoal = random.nextDouble() * 360;
			goalTimeElapsed = 0;
		}
		
		drag(delta);
		
		double xVelocity = (speed * Math.cos(Math.toRadians(this.angle)) * delta);
		double yVelocity = (speed * Math.sin(Math.toRadians(this.angle)) * delta);
		
		x += xVelocity;
		y += yVelocity;
		
		this.updateRotatedShape();
		
	}

	public void draw(Graphics g) {

		g.setColor(Color.white);
		g.drawPolygon(rotatedEntityShape);
		
	}
	
	public boolean isTimeToFire() {
		return isTimeToFire;
	}

	public void setTimeToFire(boolean isTimeToFire) {
		this.isTimeToFire = isTimeToFire;
	}

	public double getDistanceGoal() {
		return distanceGoal;
	}

	public void setDistanceGoal(double distanceGoal) {
		this.distanceGoal = distanceGoal;
	}

	public double getAngleGoal() {
		return angleGoal;
	}

	public void setAngleGoal(double angleGoal) {
		this.angleGoal = angleGoal;
	}

	public double getAccelerationAngle() {
		return accelerationAngle;
	}

	public void setAccelerationAngle(double accelerationAngle) {
		this.accelerationAngle = accelerationAngle;
	}
	
}
