package heat;

import libraries.Coordinate;
import libraries.Vector3;

/**
 * This class is one element of a HeatGrid
 */
public class HeatElement {

	private Vector3 location;
	private Coordinate coord;
	private double temperature;
	private HeatGrid map;
	private double thermalDiffusivity;
	private double boundryEffect = 1;
	
	private double newTemp = 0;
	
	/**
	 * @param heatGrid The grid that this element belongs to
	 * @param location The physical location of this element
	 * @param coord The logical location of this element, in terms of the grid
	 * @param temperature The temperature of the element
	 */
	public HeatElement(HeatGrid heatGrid, Vector3 location, Coordinate coord, double temperature, double thermalDiffusivity) {
		this.location = location;
		this.coord = coord;
		this.temperature = temperature;
		this.thermalDiffusivity = thermalDiffusivity;
		this.map = heatGrid;
	}
	public void setDiffusivity(double thermalDiffusivity){
		this.thermalDiffusivity = thermalDiffusivity;
	}
	
	public double getTemperature() {
		return temperature;
	}
	public double getThermalDiffusivity(){
		return thermalDiffusivity;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
	/**
	 * This assigns a temperature to an element but doesn't actually make the change until update() is called
	 * @param temperature The new temperature to set
	 */
	public void setNewTemp(double temperature) {
		newTemp = temperature;
	}
	
	public Coordinate getCoord() {
		return coord;
	}
	
	public Vector3 getPosition() {
		return location;
	}
	/**
	 * 
	 * @param effect Set at 0 for no environmental heat loss (level 3), or keep at 1 for level 1 & 2. 
	 */
	public void setBoundryEffect(int effect){
		this.boundryEffect = effect;
	}
	
	public void calculate(double deltaTime) {
		double rightDeltaT = 0;
		double leftDeltaT = 0;
		double upDeltaT = 0;
		double downDeltaT = 0;
		double upperRightDeltaT = 0;
		double lowerRightDeltaT = 0;
		double upperLeftDeltaT = 0;
		double lowerLeftDeltaT = 0;
		double newTemperature = getTemperature();
		double hypotnuse = 0;
		
		//sorry, didn't have time to shorten up this code. 
		if(map.getDx() != 0){
			try {
				rightDeltaT = (map.getCell(coord.getX()+1, coord.getY(), coord.getZ()).getTemperature() - getTemperature()) / map.getDx();
			} catch (IndexOutOfBoundsException e) {
				//make all = 0 for level 3
				rightDeltaT = boundryEffect * (getTemperature()*-1) / map.getDx();
			}
			try {
				leftDeltaT = (getTemperature() - map.getCell(coord.getX()-1, coord.getY(), coord.getZ()).getTemperature()) / map.getDx();
			} catch (IndexOutOfBoundsException e) {
				// make all = 0 for level 3
				leftDeltaT = boundryEffect * (getTemperature()) / map.getDx();
			}	
		}
		if(map.getDy() != 0){
			try {
				upDeltaT = (map.getCell(coord.getX(), coord.getY()+1, coord.getZ()).getTemperature() - getTemperature()) / map.getDy();
			} catch (IndexOutOfBoundsException e) {
				upDeltaT = boundryEffect * (getTemperature()*-1) / map.getDy();				
			}
			try {
				downDeltaT = (getTemperature() - map.getCell(coord.getX(), coord.getY()-1, coord.getZ()).getTemperature()) / map.getDy();
			} catch (IndexOutOfBoundsException e) {
				// make all = 0 for level 3
				downDeltaT = boundryEffect * (getTemperature()) / map.getDy();
			}	
		}
		if(map.getDx() != 0 && map.getDy() != 0){
			hypotnuse = Math.sqrt(Math.pow(map.getDx(), 2) + Math.pow(map.getDy(), 2)); 
			try {
				upperRightDeltaT = (map.getCell(coord.getX()+1, coord.getY()+1, coord.getZ()).getTemperature() - getTemperature()) / hypotnuse;				
			} catch (IndexOutOfBoundsException e) {
				//make all = 0 for level 3
				upperRightDeltaT = boundryEffect * (getTemperature() * -1) / hypotnuse;				
			}
			try {
				lowerRightDeltaT = (map.getCell(coord.getX()+1, coord.getY()-1, coord.getZ()).getTemperature() - getTemperature()) / hypotnuse;
			} catch (IndexOutOfBoundsException e) {
				lowerRightDeltaT = boundryEffect * (getTemperature() * -1) / hypotnuse;
			}			
			try {
				upperLeftDeltaT = (getTemperature() - map.getCell(coord.getX()-1, coord.getY()+1, coord.getZ()).getTemperature()) / hypotnuse;				
			} catch (IndexOutOfBoundsException e) {
				// make all = 0 for level 3
				upperLeftDeltaT = boundryEffect * (getTemperature()) / hypotnuse;				
			}
			try {
				lowerLeftDeltaT = (getTemperature() - map.getCell(coord.getX()-1, coord.getY()-1, coord.getZ()).getTemperature()) / hypotnuse;
			} catch (IndexOutOfBoundsException e) {
				lowerLeftDeltaT = boundryEffect * (getTemperature()) / hypotnuse;
			}
		}		

		double xSub = rightDeltaT - leftDeltaT;
		double ySub = upDeltaT - downDeltaT;
		double topRightLowLeftSub = upperRightDeltaT - lowerLeftDeltaT;
		double topLeftLowRightSub = lowerRightDeltaT - upperLeftDeltaT;
		if(map.getDx() != 0 && map.getDy()!= 0){
			newTemperature += getThermalDiffusivity() * (xSub/map.getDx() + ySub/map.getDy() + topLeftLowRightSub/hypotnuse + topRightLowLeftSub/hypotnuse);
		}else if(map.getDx()!=0){
			newTemperature += getThermalDiffusivity() * (xSub/map.getDx());
		}else if(map.getDy()!=0){
			newTemperature += getThermalDiffusivity() * (ySub/map.getDx()); 
		}

		// Make sure to use this function.  Do not call setTemperature() or change the temperature variable directly.  Trust me.
		setNewTemp(newTemperature);
	}
	
	/**
	 * This updates each element based on the previous calculation.
	 */
	public void update() {
		if (newTemp < 0) {
			setTemperature(0);
		} else {
			setTemperature(newTemp);
		}
	}
}
