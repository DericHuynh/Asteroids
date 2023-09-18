package asteroids.entities;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * Abstract Entity Class.
 */
public abstract class Entity {

	protected Polygon entityShape;
	protected Polygon rotatedEntityShape;
	protected double x;
	protected double y;
	protected double speed;
	protected double angle;
	protected double rotation;
	protected boolean isAlive;

	/**
	 * A constructor
	 * pre: n/a
	 * post: entityShape and rotatedEntityShape are initialized.
	 * Values are initialized.
	 */
	public Entity() {
		entityShape = new Polygon();
		rotatedEntityShape = new Polygon();
		speed = 0;
		angle = 0;
		rotation = 0;
		isAlive = true;
	}

	/**
	 * Abstract update method used to remind me
	 * to implement the update method in other entities
	 */
	public abstract void update(double delta);

	/**
	 * Abstract draw method used to remind me
	 * to implement the draw method in other entities
	 */
	public abstract void draw(Graphics g);

	/**
	 * Returns a translated copy of the entityShape.
	 * pre: n/a
	 * post: Translated copy of the entityShape has been created
	 * and returned.
	 */
	protected Polygon getTranslatedPolygon() {

		Polygon temp = new Polygon();

		for(int i = 0; i < entityShape.npoints; i++) {
			temp.addPoint(entityShape.xpoints[i], entityShape.ypoints[i]);
		}

		temp.translate((int)x, (int)y);

		return temp;

	}

	/**
	 * Checks if current entity contains other entity.
	 * pre: e is not equal to null.
	 * post: Returns true if current entity contains other
	 * entity, false otherwise.
	 */
	public boolean contains(Entity e) {

		Polygon otherRotatedEntityShape;
		int numberOfPoints;
		int tempX, tempY;
		boolean isInside;

		otherRotatedEntityShape = e.getRotatedShape();

		numberOfPoints = otherRotatedEntityShape.npoints;

		for(int i = 0; i < numberOfPoints; i++) {
			tempX = otherRotatedEntityShape.xpoints[i];
			tempY = otherRotatedEntityShape.ypoints[i];
			isInside = rotatedEntityShape.contains(tempX, tempY);
			if(isInside) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Returns rotatedEntityShape.
	 * pre: n/a
	 * post: rotatedEntityShape returned.
	 */
	protected Polygon getRotatedShape()  {
		return rotatedEntityShape;
	}

	/**
	 * Rotates entity.
	 * pre: n/a
	 * post: Entity has been rotated.
	 */
	public void rotate(double delta) {
		double newRotation;

		newRotation = rotation - delta;

		if(newRotation >= 360.0) {
			newRotation -= 360.0;
		} else if(newRotation < 0) {
			newRotation += 360.0;
		}

		rotation = newRotation;

	}

	/**
	 * Method derived by
	 * http://javatongue.blogspot.com/2016/05/rotate-points-polygon-java-rotation-2d-transformation.html
	 * --------------------------
	 * rotatedEntityShape is updated for new translation and rotation.
	 * pre: n/a
	 * post: rotatedEntityShape has been updated.
	 */
	protected void updateRotatedShape()  {

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
		pivotX = rectangle.getCenterX();
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

	/**
	 * Returns the width of the original entityShape
	 * pre: n/a
	 * post: Width has been returned.
	 */
	public double getWidth() {

		Rectangle temp;

		temp = entityShape.getBounds();

		return temp.width;

	}

	/**
	 * Returns the height of the original entityShape
	 * pre: n/a
	 * post: Height has been returned.
	 */
	public double getHeight() {

		Rectangle temp;

		temp = entityShape.getBounds();

		return temp.height;

	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}


}
