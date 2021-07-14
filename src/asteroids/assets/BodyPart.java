package asteroids.assets;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import asteroids.Asteroids;
import asteroids.entities.Entity;
import utility.Utility;

public class BodyPart extends Entity {

	private boolean active;
	private double maxTimeAlive;
	private double timeAlive;
	private double rotationSpeed;

	public BodyPart(Random random, double x, double y, double speed, double angle, double initialRotation, int size) {
		
		super();
		
		entityShape.addPoint(0, 0);
		entityShape.addPoint(size, 0);
		
		active = true;
		maxTimeAlive = Asteroids.TARGET_FPS * (Utility.getRandomFromRange(random, -1, 1) + 3.5);
		
		this.speed = speed + random.nextDouble();
		this.angle = angle + Utility.getRandomFromRange(random, -25, 25);
		this.rotation = initialRotation;
		this.rotationSpeed = Utility.getRandomFromRange(random, -4, 4);
		this.x = x;
		this.y = y;
		
	}
	
	public void drag(double delta) {
		
		speed -= (speed / 48) * delta;
		
	}
	
	public void slowRotation(double delta) {
		
		rotationSpeed -= (rotationSpeed / 48) * delta;
		
	}

	public void update(double delta) {
		
		drag(delta);
		slowRotation(delta);
		
		double xVelocity = (speed * Math.cos(Math.toRadians(this.angle)) * delta);
		double yVelocity = (speed * Math.sin(Math.toRadians(this.angle)) * delta);
		
		x += xVelocity;
		y += yVelocity;
		
		
		rotate(rotationSpeed * delta);
		
		timeAlive += delta;
		
		if(timeAlive > maxTimeAlive) {
			setActive(false);
		}
		
		this.updateRotatedShape();
		
	}
	
	protected void updateRotatedShape()  {
		//http://javatongue.blogspot.com/2016/05/rotate-points-polygon-java-rotation-2d-transformation.html
		double t;
		double v;
		Polygon translatedPolygon;
		Rectangle2D rectangle;
		int numberOfPoints;
		int[] polygonXarr, polygonYarr;
		double pivotX, pivotY;
		double radian;
		
		radian = (this.rotation * Math.PI) / 180;
		
		translatedPolygon = getTranslatedPolygon();
		rectangle = translatedPolygon.getBounds2D();
		numberOfPoints = translatedPolygon.npoints;
		polygonXarr = translatedPolygon.xpoints;
		polygonYarr = translatedPolygon.ypoints;
		pivotX = rectangle.getMinX();
		pivotY = rectangle.getCenterY();
		
		for(int i = 0; i < numberOfPoints; i++) {
			t = polygonXarr[i] - pivotX;
			v = polygonYarr[i] - pivotY;
			polygonXarr[i] = (int) (pivotX + t*Math.cos(radian) - v*Math.sin(radian));
			polygonYarr[i] = (int) (pivotY + v*Math.cos(radian) + t*Math.sin(radian));
		}
		
		rotatedEntityShape.xpoints = polygonXarr;
		rotatedEntityShape.ypoints = polygonYarr;
		rotatedEntityShape.npoints = numberOfPoints;
		rotatedEntityShape.invalidate();
		
	}

	public void draw(Graphics g) {
		
		g.drawPolygon(rotatedEntityShape);
		
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}


}
