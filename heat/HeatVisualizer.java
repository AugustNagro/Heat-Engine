package heat;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import libraries.Coordinate;

/**
 * This class visualizes in two dimensions the temperature distribution described in HeatGrid 
 */
public class HeatVisualizer extends JPanel implements ActionListener {

	/**
	 * I add this because otherwise Eclipse gets mad at me.  It is not used.
	 */
	private static final long serialVersionUID = 1L;

	HeatGrid grid;
	double maxTemp;
	double timeIncrement;
	String filename;
	String filename2;
	String filename3;
	
	int counter = 0;
	
	/**
	 * @param grid The HeatGrid that is being visualized
	 * @param timeIncrement The amount of time between each update, in real physics time
	 * @param timeScale The scale that is applied to timeIncrement for simulation purposes
	 * @param maxTemp The maximum temperature that will be attained (used as the maximum on the color scale)
	 * @param filename The name of the file to write to.  Put "" if you do not want to write to file.
	 */
	public HeatVisualizer(HeatGrid grid, double timeIncrement, double timeScale, double maxTemp, String filename, String filename2, String filename3) {
		this.grid = grid;
		this.maxTemp = maxTemp;
		this.timeIncrement = timeIncrement;
		this.filename = filename;
		this.filename2 = filename2;
		this.filename3 = filename3;

		setBackground(Color.black);
		
		final int width = 800;
		final int height = 600;
		this.setSize(width, height);
		
		int incrementInMils = (int)(timeIncrement * 1000 * timeScale);
		Timer time = new Timer(incrementInMils, this);
		time.start();
	}

	public void write(String filename) throws AWTException, IOException {
		Rectangle screen = this.getBounds();
		BufferedImage capture = new Robot().createScreenCapture(screen);
		ImageIO.write(capture, "png", new File(filename));
	}
	
	@Override
	public void paint(Graphics arg0) {
		super.paint(arg0);
		
		int eachWidth = (int)Math.ceil(getSize().getWidth() / grid.getXSize());
		int eachHeight = (int)Math.ceil(getSize().getHeight() / grid.getYSize());

		for (HeatElement element : grid.getElements()) {
			Coordinate coord = transformCoords(element.getCoord());

			arg0.setColor(calcColor(element.getTemperature()));
			arg0.fillRect(coord.getX(), coord.getY() - eachHeight, eachWidth, eachHeight);
		}
	}
	
	/**
	 * @param temperature The current temperature of the HeatElement
	 * @return The color of the element based on the color scale
	 */
	private Color calcColor(double temperature) {
		double temp = temperature / maxTemp;
		final double darkBlue = 0.65;
		double hue = (1 - temp) * darkBlue;
		final double saturation = 0.9;
		final double brightness = 0.9;

		System.out.println(temperature);
		return Color.getHSBColor((float)hue, (float)saturation, (float)brightness);
	}

	/**
	 * @param coords The Coordinate of the element
	 * @return The location of the center of the element in screen space
	 */
	private Coordinate transformCoords(Coordinate coords) {
		int xCoord = (int)Math.round((double)coords.getX() / grid.getXSize() * (int)getSize().getWidth());
		int yCoord = (int)getSize().getHeight() - (int)Math.round((double)coords.getY() / grid.getYSize() * (int)getSize().getHeight());
		return new Coordinate(xCoord, yCoord, 0);
	}

	static final double screenShotTime = 1;
	static final double screenShot2Time = 5;
	static final double screenShot3Time = 10;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		grid.update(timeIncrement);
		++counter;
		if (counter == screenShotTime) {
			try {
				write(filename);
			} catch (AWTException | IOException e) {
				e.printStackTrace();
				setVisible(false);
			}
		}
		if (counter == screenShot2Time) {
			try {
				write(filename2);
			} catch (AWTException | IOException e) {
				e.printStackTrace();
				setVisible(false);
			}
		}
		if (counter == screenShot3Time) {
			try {
				write(filename3);
			} catch (AWTException | IOException e) {
				e.printStackTrace();
				setVisible(false);
			}
		}
		
		repaint();
	}

}
