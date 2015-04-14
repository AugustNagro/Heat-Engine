package libraries;

import java.util.List;
import gravitation1.GravitationEngine;

abstract public class Projectile {

	private String name;
	
	public Projectile(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	abstract protected void setAccel(GravitationEngine engine);
	abstract protected void advanceVelocity(double timeIncrement);
	abstract protected void advancePosition(double timeIncrement);
	
	abstract public void addToList(List<PointProjectile> list);
	
	/**
	 * Updates the velocity and position of the object given a force, assuming a small time step
	 * @param netForce - the force acting on the object
	 * @param timeIncrement - the amount of time over which the force acts
	 */
	public void update(GravitationEngine engine, double timeIncrement) {
		setAccel(engine);
		advanceVelocity(timeIncrement);
		advancePosition(timeIncrement);
	}

}
