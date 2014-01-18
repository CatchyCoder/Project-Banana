package projectbanana.main.entity;

import java.awt.Graphics;

import projectbanana.main.CollisionEvent;
import projectbanana.main.Game;
import projectbanana.main.World;
import projectbanana.main.values.GeometryId;
import projectbanana.main.values.RotationId;

public abstract class Entity {
	
	protected final int GEOMETRY_ID;
	
	protected double x, y;
	protected int width, height;
	protected double boundingRad;
	protected int boundingWidth, boundingHeight;
	
	protected int startX, startY;
	protected double lastValidX, lastValidY;
	
	protected double vel = 0, velX = 0, velY = 0, accX = 0, accY = 0;
	protected double rotVel = 0, rotAcc = 0, rotation = (3 * Math.PI) / 2; // Default rotation is facing up (North)
	protected final double MIN_VEL = 0.01, MIN_ROT_VEL = Math.toRadians(0.01); // Slowest speed that is allowed before velocity goes to 0
	protected double velDamping = 0.985, rotVelDamping = 0.92;
	
	public Entity(int x, int y, int GEOMETRY_ID) {
		this.GEOMETRY_ID = GEOMETRY_ID;
		this.x = lastValidX = x;
		this.y = lastValidY = y;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	protected void applyForces() {
		// Storing last valid location
		lastValidX = x;
		lastValidY = y;
		
		// X and Y positioning
		velX += accX;
		velY += accY;
		vel = Math.hypot(velX, velY);
		x += velX;
		y += velY;
		
		// Rotation positioning
		rotVel += rotAcc;
		rotation += rotVel;
		
		// Resetting force values
		accX = accY = rotAcc = 0;
	}
	
	public boolean isOnScreen() {
		int screenWidth = (int) Game.SIZE.width;
		int screenHeight = (int) Game.SIZE.height;
		
		if(Math.abs(getCenterX() - World.player.getCenterX()) <= ((width + screenWidth) / 2) &&
				Math.abs(getCenterY() - World.player.getCenterY()) <= ((height + screenHeight) / 2)) return true;
		return false;
	}
	
	public CollisionEvent isCollidingWith(Entity entity) {
		return isCollidingWith(entity, 0);
	}
	
	/**
	 * Checks if this Entity is colliding with another specified Entity, and also if
	 * it is colliding within a certain distance from that entity.
	 * 
	 * @param entity
	 * @param range
	 * @returns a CollisionEvent, giving the x and y distance from the entities,
	 * and if they were colliding (true of colliding, false if not).
	 */
	public CollisionEvent isCollidingWith(Entity entity, double range) {
		double xDis = getXDisFrom(entity);
		double yDis = getYDisFrom(entity);
		
		// If the Entities' shapes are different
		if(getGeometryId() != entity.getGeometryId()) {
			if(getGeometryId() == GeometryId.CIRCLE.getId()) return isCollidingWithCirRect(this, entity, range, xDis, yDis);
			else return isCollidingWithCirRect(entity, this, range, xDis, yDis);
		}
		// If both Entities are rectangles
		else if(getGeometryId() == GeometryId.RECTANGLE.getId()) {
			if((Math.abs(xDis) > (boundingWidth / 2 + entity.boundingWidth / 2 + range)) || (Math.abs(yDis) > (boundingHeight / 2 + entity.boundingHeight / 2 + range)))
				return new CollisionEvent(false, xDis, yDis);
			return new CollisionEvent(true, xDis, yDis);
		}
		// If both Entities are circles
		else {
			double radiusSqr = Math.pow(boundingRad + entity.boundingRad + range, 2);
			if(getSqrDisFrom(entity) <= radiusSqr) return new CollisionEvent(true, xDis, yDis);
			return new CollisionEvent(false, xDis, yDis);
		}
	}
	
	/**
	 * Checks whether the specified circle and rectangle Entities are colliding
	 * 
	 * @param circle the circle Entity
	 * @param rect the rectangle Entity
	 * @param xDis the x distance from the Entities
	 * @param yDis the y distance from the Entities
	 * @returns a CollisionEvent, giving the x and y distance from the entities,
	 * and if they were colliding (true of colliding, false if not).
	 */
	public CollisionEvent isCollidingWithCirRect(Entity circle, Entity rect, double range, double xDis, double yDis) {
		double xDisAbs = Math.abs(xDis);
		double yDisAbs = Math.abs(yDis);
		
		// Simple false cases
		if((xDisAbs > (rect.boundingWidth / 2 + circle.boundingRad + range)) || ((yDisAbs > (rect.boundingHeight / 2 + circle.boundingRad + range))))
			return new CollisionEvent(false, xDis, yDis);
		
		// Simple true cases
		if((xDisAbs <= rect.boundingWidth / 2 + range) || (yDisAbs <= rect.boundingHeight / 2 + range)) return new CollisionEvent(true, xDis, yDis);
		
		// Corner case
		double cornerDisSqr = (Math.pow(xDisAbs - ((rect.boundingWidth / 2) + range), 2) + Math.pow(yDisAbs - ((rect.boundingHeight / 2) + range), 2));
		if(cornerDisSqr <= Math.pow(circle.boundingRad + range, 2)) return new CollisionEvent(true, xDis, yDis);
		return new CollisionEvent(false, xDis, yDis);
	}
	
	protected void respawn() {
		x = startX;
		y = startY;
		vel = velX = velY = accX = accY = rotVel = rotAcc = 0;
		rotation = (3 * Math.PI) / 2; // Facing up (North)
	}
	
	protected void applyVelDamping() {
		applyVelDamping(velDamping);
	}
	
	protected void applyVelDamping(double dampAmnt) {
		if(vel == 0) return;
		
		// Velocity X & Y damping
		velX *= dampAmnt;
		velY *= dampAmnt;
		checkVel();
	}
	
	protected void applyRotVelDamping() {
		applyRotVelDamping(rotVelDamping);
	}
	
	protected void applyRotVelDamping(double dampAmnt) {
		if(rotVel == 0) return;
		
		rotVel *= dampAmnt;
		checkRotVel();
	}
	
	/**
	 * Accelerates the Entity forward at the required force until it is moving at the
	 * required speed, and then maintains that speed if continually
	 * called.
	 * 
	 * Note: force cannot be greater than speed, or an Exception will be thrown.
	 * 
	 * @param force
	 * @param maxVel
	 * @throws Exception
	 */
	protected void moveForward(double force, double maxVel) throws Exception {
		if(force > maxVel) throw new Exception("\"force\" cannot be greater than \"max velocity\"");
		
		accForward(force);
		if(vel > maxVel) {
			// Capping off the velocity
			velX = (velX / vel) * maxVel;
			velY = (velY / vel) * maxVel;
		}
	}
	
	/**
	 * Continually accelerates the Entity forward with the required force.
	 * 
	 * @param force
	 */
	protected void accForward(double force) {
		accX = Math.cos(rotation) * force; // X distance
		accY = Math.sin(rotation) * force; // Y distance
	}
	
	/**
	 * Checks whether the velocity is lower than MIN_VEL, if so, x
	 * and y velocity are set to 0.
	 */
	protected void checkVel() {
		if(vel < MIN_VEL) velX = velY = 0;
	}
	
	/**
	 * Checks whether the rotation velocity is lower than MIN_ROT_VEL, if so, rotation
	 * velocity is set to 0.
	 */
	protected void checkRotVel() {
		if(Math.abs(rotVel) < MIN_ROT_VEL) rotVel = 0;
	}
	
	/**
	 * Rotates the Entity with the required force until it is moving at the
	 * required speed in the specified direction, and then maintains that speed if continually
	 * called.
	 * 
	 * Note: force cannot be greater than speed, and dir can only be -1 (clockwise-clockwise) or 1 (counter), or and Exception 
	 * will be thrown.
	 * 
	 * @param dir
	 * @param maxVel
	 * @param force
	 * @throws Exception
	 */
	protected void turn(int dir, double maxVel, double force) throws Exception {
		if(force > maxVel) throw new Exception("\"force\" cannot be greater than \"max velocity\"");
		if(dir != RotationId.CLOCKWISE.getId() && dir != RotationId.COUNTER_CLOCKWISE.getId()) throw new Exception("\"dir\" can only be 1 (clockwise) or -1 (counter-clockwise)");
		
		rotAcc = force * dir;
		
		if(Math.abs(rotVel) > maxVel) {
			rotVel = maxVel * dir;
			rotAcc = 0;
		}
	}
	
	protected void lookAt(Entity entity) {
		rotation = Math.atan(getYDisFrom(entity) / getXDisFrom(entity));
		if(getXDisFrom(entity) > 0) rotation += Math.toRadians(180);
	}
	
	public double getXDisFrom(Entity entity) {
		return (getCenterX() - entity.getCenterX());
	}
	
	public double getYDisFrom(Entity entity) {
		return (getCenterY() - entity.getCenterY());
	}
	
	public double getSqrDisFrom(Entity entity) {
		return (Math.pow(getXDisFrom(entity), 2) + Math.pow(getYDisFrom(entity), 2));
	}
	
	protected boolean inRange(Entity entity, double range) {
		return isCollidingWith(entity, range).isColliding();
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getCenterX() {
		return (x + (width / 2));
	}
	
	public double getCenterY() {
		return (y + (height / 2));
	}
	
	public int getGeometryId() {
		return GEOMETRY_ID;
	}
}
