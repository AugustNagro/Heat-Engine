package heat;

import libraries.Coordinate;
import libraries.Vector3;

/**
 * 
 * @author August Nagro
 * This project models heat dispersion based on the heat equation. 
 */
public class HeatDriver {

	// constants (feel free to change)
	private static final double baseThermalDiffusivity = .1, material1ThermalDiffuisivity = .2, material2ThermalDiffusivity = .1, material3ThermalDiffusivity = .05, material4ThermalDiffusivity = .01;
	private static final double initialTemp = 10;
	private static final double timeIncrement = 1;
	private static final double material1InitTemp = 100, material2InitTemp = 100, material3InitTemp = 100, material4InitTemp = 100;
	private static final double maxTemp = 100;

	private static final double timeScale = 1;
	private static final String filename = "C:\\Users\\Student\\Desktop\\results.png";
	private static final String filename2 = "C:\\Users\\Student\\Desktop\\results1.png";
	private static final String filename3 = "C:\\Users\\Student\\Desktop\\results2.png";
	

	public static void main(String[] args) {
		example4();
	}
	
	/**
	 * Example 1: One dimensional heat dispersion. 
	 */
	public static void example1(){
		Vector3 min = new Vector3(0, 0, 0);
		Vector3 max = new Vector3(20, 1, 1);
		Coordinate size = new Coordinate(20, 1, 1);
		
		Coordinate spikeLocation = new Coordinate(5, 0, 0);
		double spikeTemp = 100;
		
		HeatGrid grid = new HeatGrid(initialTemp, min, max, size, baseThermalDiffusivity);
		
		grid.getCell(spikeLocation).setTemperature(spikeTemp);
		grid.run(timeIncrement, timeScale, maxTemp, filename, filename2, filename3);
	}
	
	/**
	 * Example 2: Two dimensional. 
	 */
	public static void example2(){
		Vector3 min = new Vector3(0, 0, 0);
		Vector3 max = new Vector3(20, 20, 1);
		Coordinate size = new Coordinate(20, 20, 1);
		
		Coordinate spikeLocation = new Coordinate(10, 10, 0);
		double spikeTemp = 100;
		
		HeatGrid grid = new HeatGrid(initialTemp, min, max, size, baseThermalDiffusivity);
		
		grid.getCell(spikeLocation).setTemperature(spikeTemp);
		grid.run(timeIncrement, timeScale, maxTemp, filename, filename2, filename3);
	}
	/**
	 * Example 3: Uses modified boundary conditions that assume no heat is lost to the environment
	 */
	public static void example3(){
		Vector3 min = new Vector3(0, 0, 0);
		Vector3 max = new Vector3(20, 20, 1);
		Coordinate size = new Coordinate(20, 20, 1);
		
		Coordinate spikeLocation = new Coordinate(10, 10, 0);
		double spikeTemp = 100;
		
		HeatGrid grid = new HeatGrid(initialTemp, min, max, size, baseThermalDiffusivity);
		
		grid.getCell(spikeLocation).setTemperature(spikeTemp);
		grid.setBoundryEffect(0);
		grid.run(timeIncrement, timeScale, maxTemp, filename, filename2, filename3);
	}
	
	/**
	 * Example 4: A grid of 4 different materials, with different thermal diffusivities
	 */
	public static void example4(){
		Vector3 min = new Vector3(0, 0, 0);
		Vector3 max = new Vector3(30, 30, 1);
		Coordinate size = new Coordinate(30, 30, 1);
		Coordinate material1Min = new Coordinate(12, 12, 0), material2Min = new Coordinate(12, 15, 0), material3Min = new Coordinate(15, 12, 0), material4Min = new Coordinate(15, 15, 0);
		Coordinate material1Max = new Coordinate(15, 15, 1), material2Max = new Coordinate(15, 18, 1), material3Max = new Coordinate(18, 15, 1), material4Max = new Coordinate(18, 18, 1);

		HeatGrid grid = new HeatGrid(initialTemp, min, max, size, baseThermalDiffusivity);
		
		grid.addHeatElement(material1InitTemp, material1Min, material1Max, material1ThermalDiffuisivity);
		grid.addHeatElement(material2InitTemp, material2Min, material2Max, material2ThermalDiffusivity);
		grid.addHeatElement(material3InitTemp, material3Min, material3Max, material3ThermalDiffusivity);
		grid.addHeatElement(material4InitTemp, material4Min, material4Max, material4ThermalDiffusivity);
		
		
		grid.run(timeIncrement, timeScale, maxTemp, filename, filename2, filename3);
	}
	
}
