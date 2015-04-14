package libraries;

import java.util.List;

import gravitation1.Driver;
import gravitation1.GravitationEngine;

/**
 * @author Peter
 * Generic massive object that moves in three dimension and moves under the influence of arbitrary forces.
 * (Classical motion only).
 */
public class PointProjectile extends Projectile {
	 
	private Vector3 position;
	private Vector3 velocity;
	private Vector3 acceleration = Vector3.nullVector();
	private double mass;
	
	/**
	 * @param position - initial position
	 * @param velocity - initial velocity
	 * @param mass - object's mass
	 */
	
	public PointProjectile(Vector3 position, Vector3 velocity, double mass, String name) {
		super(name);
		this.position = position;
		this.velocity = velocity;
		this.mass = mass;
	}
	
	public String toString() {
		return position + "\t" + velocity + "\t" + acceleration;
	}
	
	protected void advancePosition(double timeIncrement) {
		position = Vector3.add(position, velocity.scale(timeIncrement));
	}
	
	
	protected void setAccel(GravitationEngine engine) {
		Vector3 totalF = new Vector3();
		for(PointProjectile point : engine.getProjectiles()){
			Vector3 diffPos = Vector3.subtract(point.getPosition(), this.getPosition());
			if(diffPos.magnitude() != 0){
				Vector3 forceGravity = Vector3.scale(Vector3.unitVector(diffPos) , Driver.gravConstant * this.getMass() * point.getMass() / Math.pow(diffPos.magnitude(), 2));
				totalF = Vector3.add(totalF, forceGravity);
			}
		}
		this.acceleration = Vector3.scale(totalF, 1.0/mass);
	}
	
	/**
	 * @return - the position of the object
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * @param position - the new position of the object
	 */
	public void setPosition(Vector3 position) {
		this.position = position;
	}

	/**
	 * @return the velocity of the object
	 */
	public Vector3 getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity - the new velocity of the object
	 */
	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * @return the mass of the object
	 */
	public double getMass() {
		return mass;
	}
	
	/**
	 * @param mass - the new mass of the object
	 */
	public void setMass(double mass) {
		this.mass = mass;
	}

	@Override
	protected void advanceVelocity(double timeIncrement) {
		velocity = Vector3.add(velocity, acceleration.scale(timeIncrement));
	}

	@Override
	public void addToList(List<PointProjectile> list) {
		list.add(this);
	}
	

}
