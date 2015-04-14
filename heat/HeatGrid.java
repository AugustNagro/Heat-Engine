package heat;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import libraries.Coordinate;
import libraries.Vector3;

/**
 * This represents an object with a varying heat field
 */
public class HeatGrid {
	
	ArrayList<ArrayList<ArrayList<HeatElement>>> grid = new ArrayList<ArrayList<ArrayList<HeatElement>>>();
	Coordinate size;
	/**
	 * @param thermalDiffusivity The thermal diffusivity constant
	 * @param initialTemp The original temperature of each element. This can be freely modified with getCell().setTemperature().
	 * @param min The minimum x, y, and z values of the object in physical space.
	 * @param max The maximum x, y, and z values of the object in physical space.
	 * @param size The number of elements in the object in the x, y, and z direction. Make sure each component is at least 1.
	 */
	public HeatGrid(double initialTemp, Vector3 min, Vector3 max, Coordinate size, double thermalDiffusivity) {
		double xIncrement = getIncrement(min.getX(), max.getX(), size.getX());
		double yIncrement = getIncrement(min.getY(), max.getY(), size.getY());
		double zIncrement = getIncrement(min.getZ(), max.getZ(), size.getZ());
		
		for (int ix = 0; ix < size.getX(); ++ix) {
			grid.add(new ArrayList<ArrayList<HeatElement>>());
			for (int iy = 0; iy < size.getY(); ++iy) {
				grid.get(ix).add(new ArrayList<HeatElement>());
				for (int iz = 0; iz < size.getZ(); ++iz) {
					double xCoord = getCoord(min.getX(), xIncrement, ix);
					double yCoord = getCoord(min.getY(), yIncrement, iy);
					double zCoord = getCoord(min.getZ(), zIncrement, iz);
					
					HeatElement el = new HeatElement(this, new Vector3(xCoord, yCoord, zCoord), 
							new Coordinate(ix, iy, iz), initialTemp, thermalDiffusivity);
					grid.get(ix).get(iy).add(el);
				}
			}
		}
	}
	/**
	 * 
	 * @param effect Set at 0 for no environmental heat loss (level 3), or keep at 1 for level 1 & 2. 
	 */
	public void setBoundryEffect(int effect){
		for (int ix = 0; ix < grid.size()-1; ix++) {
			for (int iy = 0; iy < grid.get(iy).size()-1; iy++) {
				for (int iz = 0; iz < grid.get(iy).get(iz).size()-1; iz++) {
					grid.get(ix).get(iy).get(iz).setBoundryEffect(effect);
				}
			}
		}
	}
	public void addHeatElement(double temp, Coordinate min, Coordinate max, double thermalDiffusivity){
		
		for(int ix = min.getX(); ix < max.getX(); ix++){
			for(int iy = min.getY(); iy < max.getY(); iy++){
				for(int iz = min.getZ(); iz < max.getZ(); iz++){
					grid.get(ix).get(iy).get(iz).setDiffusivity(thermalDiffusivity);
					grid.get(ix).get(iy).get(iz).setTemperature(temp);
				}
			}
		}
	}
	
	/**
	 * @return The number of cells in the x direction
	 */
	public int getXSize() {
		return grid.size();
	}
	
	/**
	 * @return The number of cells in the y direction
	 */
	public int getYSize() {
		return grid.get(0).size();
	}
	
	/**
	 * @return The number of cells in the z direction
	 */
	public int getZSize() {
		return grid.get(0).get(0).size();
	}
	
	/**
	 * @return The physical distance between any two cells in the x direction
	 */
	public double getDx() {
		if (getXSize() <= 1) {
			return 0;
		} else {
			return getCell(1, 0, 0).getPosition().getX() - getCell(0, 0, 0).getPosition().getX();
		}
	}
	
	/**
	 * @return The physical distance between any two cells in the y direction
	 */
	public double getDy() {
		if (getYSize() <= 1) {
			return 0;
		} else {
			return getCell(0, 1, 0).getPosition().getY() - getCell(0, 0, 0).getPosition().getY();
		}	
	}

	/**
	 * @return The physical distance between any two cells in the z direction
	 */
	public double getDz() {
		if (getZSize() <= 1) {
			return 0;
		} else {
			return getCell(0, 0, 1).getPosition().getZ() - getCell(0, 0, 0).getPosition().getZ();
		}	
	}
	
	/**
	 * @return The minimum physical value of x, y, and z
	 */
	public Vector3 getMin() {
		return grid.get(0).get(0).get(0).getPosition();
	}
	
	/**
	 * @return The maximum physical value of x, y and z
	 */
	public Vector3 getMax() {
		return grid.get(getXSize() - 1).get(getYSize() - 1).get(getZSize() - 1).getPosition();		
	}
	
	
	/**
	 * @return A list of the elements inside the grid, mainly for use by HeatVisualizer
	 */
	public List<HeatElement> getElements() {
		List<HeatElement> answer = new ArrayList<HeatElement>();
		
		for (ArrayList<ArrayList<HeatElement>> xlist : grid) {
			for (ArrayList<HeatElement> ylist : xlist) {
				for (HeatElement element : ylist) {
					answer.add(element);
				}
			}
		}
		
		return answer;
	}
	
	private double getIncrement(double min, double max, int size) {
		return size == 0 ? 0 : Math.abs(max - min) / size;
	}
	
	private double getCoord(double min, double increment, int index) {
		return min + index * increment;
	}
	
	/**
	 * @param xCoord The x coordinate of the cell
	 * @param yCoord The y coordinate of the cell
	 * @param zCoord The z coordinate of the cell
	 * @return The element at the requested coordinates
	 */
	public HeatElement getCell(int xCoord, int yCoord, int zCoord) {
		return grid.get(xCoord).get(yCoord).get(zCoord);
	}
	public HeatElement getCell(Coordinate coord){
		return grid.get(coord.getX()).get(coord.getY()).get(coord.getZ());
	}
	
	/**
	 * @param deltaTime The time increment of the simulation
	 * This loops over all elements and calls update() on each
	 */
	public void update(double deltaTime) {
		for (ArrayList<ArrayList<HeatElement>> xlist : grid) {
			for (ArrayList<HeatElement> ylist : xlist) {
				for (HeatElement element : ylist) {
					element.calculate(deltaTime);
				}
			}
		}
		
		for (ArrayList<ArrayList<HeatElement>> xlist : grid) {
			for (ArrayList<HeatElement> ylist : xlist) {
				for (HeatElement element : ylist) {
					element.update();
				}
			}
		}
	}
	
	/**
	 * @param timeIncrement The time increment of the simulation
	 * @param timeScale The scaling to the visualizer, used to adjust the speed of the display
	 * @param maxTemp The maximum temperature achieved, so the visualizer can calibrate its scale properly
	 */
	public void run(double timeIncrement, double timeScale, double maxTemp, String filename, String filename2, String filename3) {
		HeatVisualizer viz = new HeatVisualizer(this, timeIncrement, timeScale, maxTemp, filename, filename2, filename3);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(viz);
		frame.setSize(viz.getWidth(), viz.getHeight());

		frame.setVisible(true);			
	}
}
