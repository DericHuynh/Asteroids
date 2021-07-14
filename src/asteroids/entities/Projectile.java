package asteroids.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

import asteroids.Asteroids;
import asteroids.assets.AssetManager;

public class Projectile extends Entity {

	private double timeThreshold;
	private double timeAlive;
	
	public Projectile(double x, double y, double speed, double angle, double timeThreshold) {
		
		super();
		
		AssetManager.addProjectilePoints(entityShape);

		this.timeThreshold = Asteroids.TARGET_FPS * timeThreshold;
		this.speed = speed;
		this.angle = angle;
		this.rotation = angle;
		this.x = x;
		this.y = y;
		
	}
	
	public void update(double delta) {
		
		timeAlive += delta;
		
		if(timeAlive > timeThreshold) {
			isAlive = false;
		}
		
		double xVelocity = (speed * Math.cos(Math.toRadians(this.angle)) * delta);
		double yVelocity = (speed * Math.sin(Math.toRadians(this.angle)) * delta);
		
		x += xVelocity;
		y += yVelocity;
		
		updateRotatedShape();
		
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
		
		g.setColor(Color.white);
		g.drawPolygon(rotatedEntityShape);
		
	}

}
