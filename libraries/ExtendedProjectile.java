package libraries;

import gravitation1.Driver;
import gravitation1.GravitationEngine;

import java.util.ArrayList;
import java.util.List;

public class ExtendedProjectile extends Projectile {

	List<PointProjectile> projectiles = new ArrayList<>();
	Vector3 acceleration = new Vector3();
	Vector3 velocity = new Vector3();
	

	
	public ExtendedProjectile(String name) {
		super(name);
	}
	
	public void addProjectile(PointProjectile proj) {
		projectiles.add(proj);
	}
	
	@Override
	public String toString() {
		Vector3 position = projectiles.get(0).getPosition();
		return "" + position + "\t" + position.magnitude() + "\t" + velocity + "\t" + velocity.magnitude() + "\t" + acceleration + "\t" + acceleration.magnitude() + "\t";
	}

	@Override
	protected void setAccel(GravitationEngine engine) {
		Vector3 totalF = new Vector3();
		for(PointProjectile piece : projectiles){			
			for(PointProjectile compareTo : engine.getProjectiles()){
				if(compareTo.getName() != piece.getName()){
					Vector3 diffPos = Vector3.subtract(compareTo.getPosition(), piece.getPosition());
					if(diffPos.magnitude() != 0){
						Vector3 forceGravity = Vector3.scale(Vector3.unitVector(diffPos) , Driver.gravConstant * piece.getMass() * compareTo.getMass() / Math.pow(diffPos.magnitude(), 2));
						totalF = totalF.add(forceGravity);
					}
				}
			}			
		}
		acceleration = totalF.scale(1.0 / (projectiles.get(0).getMass() * projectiles.size()));	

	}

	@Override
	protected void advanceVelocity(double timeIncrement) {
		velocity = Vector3.add(velocity, acceleration.scale(timeIncrement));

	}
	
	
	public void setVelocity(Vector3 velocity){
		this.velocity = velocity;
	}
	public void setPosition(Vector3 newPosition){
		for(PointProjectile point : projectiles){
			point.setPosition(point.getPosition().add(newPosition));
		}
	}

	@Override
	protected void advancePosition(double timeIncrement) {
		setPosition(velocity.scale(timeIncrement));
	}

	@Override
	public void addToList(List<PointProjectile> list) {
		for (PointProjectile proj : projectiles) {
			list.add(proj);
		}
	}
	public void populate(double radius, double blockHeight, double blockMass){
		for(double ZPos=radius; ZPos >= -radius; ZPos -= blockHeight){
    		for(double XPos = -radius; XPos <= radius; XPos += blockHeight){
    			for(double YPos = radius; YPos >= -radius; YPos -= blockHeight){
    				PointProjectile point = new PointProjectile(new Vector3(XPos, YPos, ZPos), new Vector3(), blockMass, "EarthBlock");
   					addProjectile(point);  
    			}
    		}
    	}
	}
	public void populateSphere(double radius, double blockHeight, double blockMass){
		for(double ZPos=radius; ZPos >= -radius; ZPos -= blockHeight){
    		for(double XPos = -radius; XPos <= radius; XPos += blockHeight){
    			for(double YPos = radius; YPos >= -radius; YPos -= blockHeight){
    				PointProjectile point = new PointProjectile(new Vector3(XPos, YPos, ZPos), new Vector3(), blockMass, "EarthBlock");
    				if(point.getPosition().magnitude() <= radius/2){
    					addProjectile(point);  
    				}
    			}
    		}
    	}
	}
	public void populateHollowSphere(double radius, double blockHeight, double blockMass){
		for(double ZPos=radius; ZPos >= -radius; ZPos -= blockHeight){
    		for(double XPos = -radius; XPos <= radius; XPos += blockHeight){
    			for(double YPos = radius; YPos >= -radius; YPos -= blockHeight){
    				PointProjectile point = new PointProjectile(new Vector3(XPos, YPos, ZPos), new Vector3(), blockMass, "EarthBlock");
    				if(point.getPosition().magnitude() > radius){
    					addProjectile(point);
    					System.out.println(point);
    				}
    			}
    		}
    	}
	}
	
	public void setBlockMass(double mass){
		for(PointProjectile x : projectiles){
			x.setMass(mass);
		}
	}
	public int numBlocks(){
		return projectiles.size();
	}

}
