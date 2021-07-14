package asteroids.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

import asteroids.Asteroids;
import asteroids.assets.AssetManager;

/**
 * Player class
 */
public class Player extends Entity {

	private Polygon exhaustShape;
	private Polygon rotatedExhaustShape;
	private boolean isAccelerating;
	private boolean isTurningLeft;
	private boolean isTurningRight;
	private boolean isFiring;
	private double fireRate;
	private double fireTimeElapsed;
	private double exhaustRate;
	private double exhaustTimeElapsed;
	private boolean isTimeToFire;
	
	/**
	 * Default constructor
	 * pre: n/a
	 * post: ExhaustShape and RotatedExhaustShape are initialized.
	 * Values are initialized.
	 */
	public Player() {
	
		super();
		
		exhaustShape = new Polygon();
		rotatedExhaustShape = new Polygon();
		
		AssetManager.addPlayerPoints(entityShape);
		AssetManager.addPlayerExhaustPoints(exhaustShape);
		
		this.isTurningLeft = false;
		this.isTurningRight = false;
		this.isAccelerating = false;
		this.isAlive = false;
		this.isTimeToFire = true;
		this.fireTimeElapsed = 0;
		this.fireRate = 3;
		this.exhaustRate = 8;
		this.exhaustTimeElapsed = 0;
		
	}
	
	/**
	 * Accelerates the player by a specified amount.
	 * pre: n/a
	 * post: Player has been accelerated.
	 */
	public void accelerate(double acceleration) {
		
		double Xvector = speed * Math.cos(Math.toRadians(this.angle));
		double Yvector = speed * Math.sin(Math.toRadians(this.angle));
		
		double accelXvector = acceleration * Math.cos(Math.toRadians(rotation));
		double accelYvector = acceleration * Math.sin(Math.toRadians(rotation));
		
		Xvector += accelXvector;
		Yvector += accelYvector;
		
		angle = (float) Math.toDegrees(Math.atan2(Yvector, Xvector));
		speed = Math.min(6, Math.sqrt(Xvector * Xvector + Yvector * Yvector));
		
	}
	
	/**
	 * Speed is lessened by a relative amount.
	 * pre: n/a
	 * post: Speed has been decreased.
	 */
	public void drag(double delta) {
		
		speed -= (speed / 48) * delta;
		
	}
	
	/**
	 * Returns an arrow containing the x and y
	 * coordinates of the tip of the player polygon.
	 * pre: n/a
	 * post: Coordinates have been returned.
	 */
	public int[] getPlayerTip() {
		
		int[] tip = new int[2];
		
		tip[0] = rotatedEntityShape.xpoints[0];
		tip[1] = rotatedEntityShape.ypoints[0];
		
		return tip;
		
	}
	
	/**
	 * Returns the all of the points of the rotatedEntityShape
	 * as an integer array.
	 * pre: n/a
	 * post: All points have been returned.
	 */
	public int[] getPlayerShape() {
		
		int[] shape = new int[rotatedEntityShape.npoints * 2];
		
		for(int i = 0; i < rotatedEntityShape.npoints; i += 1) {
			shape[i * 2] = rotatedEntityShape.xpoints[i];
			shape[i * 2 + 1] = rotatedEntityShape.ypoints[i];
		}
		
		return shape;
		
	}

	/**
	 * 
	 * 
	 */
	public void update(double delta) {
		
		if(isTurningLeft) {
			this.rotate(4.0 * delta);
		}

		if(isTurningRight) {
			this.rotate(-4.0 * delta);
		}
		
		if(isAccelerating) {
			this.accelerate(0.15 * delta);
			exhaustTimeElapsed += delta;
			if(exhaustTimeElapsed >= Asteroids.TARGET_FPS / exhaustRate) {
				exhaustTimeElapsed = 0;
			}
		}
		
		if(!isTimeToFire) {
			fireTimeElapsed += delta;
			if(fireTimeElapsed >= Asteroids.TARGET_FPS / fireRate) {
				isTimeToFire = true;
				fireTimeElapsed = 0;
			}
		}
		
		drag(delta);
		
		double xVelocity = (speed * Math.cos(Math.toRadians(this.angle)) * delta);
		double yVelocity = (speed * Math.sin(Math.toRadians(this.angle)) * delta);
		
		x += xVelocity;
		y += yVelocity;
		
		this.updateRotatedShape(entityShape, rotatedEntityShape);
		this.updateRotatedShape(exhaustShape, rotatedExhaustShape);
		
	}
	
	protected void updateRotatedShape(Polygon shape, Polygon rotatedShape)  {
		//http://javatongue.blogspot.com/2016/05/rotate-points-polygon-java-rotation-2d-transformation.html
		double t;
		double v;
		Polygon translatedEntityPolygon;
		Polygon translatedOtherPolygon;
		Rectangle2D rectangle;
		int numberOfPoints;
		int[] polygonXarr, polygonYarr;
		double pivotX, pivotY;
		double radian;
		
		radian = (this.rotation * Math.PI) / 180;
		
		translatedEntityPolygon = getTranslatedPolygon(entityShape);
		translatedOtherPolygon = getTranslatedPolygon(shape);
		rectangle = translatedEntityPolygon.getBounds2D();
		numberOfPoints = translatedOtherPolygon.npoints;
		polygonXarr = translatedOtherPolygon.xpoints;
		polygonYarr = translatedOtherPolygon.ypoints;
		pivotX = rectangle.getCenterX();
		pivotY = rectangle.getCenterY();
		
		for(int i = 0; i < numberOfPoints; i++) {
			t = polygonXarr[i] - pivotX;
			v = polygonYarr[i] - pivotY;
			polygonXarr[i] = (int) (pivotX + t*Math.cos(radian) - v*Math.sin(radian));
			polygonYarr[i] = (int) (pivotY + v*Math.cos(radian) + t*Math.sin(radian));
		}
		
		rotatedShape.xpoints = polygonXarr;
		rotatedShape.ypoints = polygonYarr;
		rotatedShape.npoints = numberOfPoints;
		rotatedShape.invalidate();
		
	}
	
	protected Polygon getTranslatedPolygon(Polygon shape) {
		
		Polygon temp = new Polygon();
		
		for(int i = 0; i < shape.npoints; i++) {
			temp.addPoint(shape.xpoints[i], shape.ypoints[i]);
		}
		
		temp.translate((int)x, (int)y);
		
		return temp;
		
	}

	public void draw(Graphics g) {
		
		
		g.setColor(Color.white);
		g.drawPolygon(rotatedEntityShape);
		
		if(isAccelerating) {
			if(exhaustTimeElapsed >= Asteroids.TARGET_FPS / (exhaustRate * 2)) {
				g.fillPolygon(rotatedExhaustShape);
			}
		}
		
	}
	
	public boolean isTurningLeft() {
		return isTurningLeft;
	}

	public void setTurningLeft(boolean isTurningLeft) {
		this.isTurningLeft = isTurningLeft;
	}

	public boolean isTurningRight() {
		return isTurningRight;
	}

	public void setTurningRight(boolean isTurningRight) {
		this.isTurningRight = isTurningRight;
	}

	public boolean isAccelerating() {
		return isAccelerating;
	}

	public void setAccelerating(boolean isAccelerating) {
		this.isAccelerating = isAccelerating;
	}

	public boolean isFiring() {
		return isFiring;
	}

	public void setFiring(boolean isFiring) {
		this.isFiring = isFiring;
	}

	public boolean isTimeToFire() {
		return isTimeToFire;
	}

	public void setTimeToFire(boolean isTimeToFire) {
		this.isTimeToFire = isTimeToFire;
	}

	

}
