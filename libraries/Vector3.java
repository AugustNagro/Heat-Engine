package libraries;

/**
 * @author Peter Dong
 * Generic three-dimensional vector for physics work
 * I would use generics to make it more flexible a la C++, but I can't make generic static functions. *
 */
/**
 * @author pdong
 *
 */
public class Vector3 {
	
	/**
	 * Stores internal variables in Cartesian coordinates
	 */
	private double x;
	private double y;
	private double z;

	/**
	 * Default constructor - initializes to null vector
	 */
	public Vector3() {
		x = 0;
		y = 0;
		z = 0;
	}

	/**
	 *  Basic Cartesian constructor (right-handed coordinate system)
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param z - z coordinate
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Vector copy constructor
	 * @param rhs - Vector3 object to copy
	 */
	public Vector3(Vector3 rhs) {
		x = rhs.x;
		y = rhs.y;
		z = rhs.z;
	}
	
	public String toString() {
		return new String(round(x) + "\t" + round(y) + '\t' + round(z) + '\t' + round(magnitude()));
	}
	
	/**
	 * @return x coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x - New x coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return y coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y - New y coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return z coordinate
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z - New z coordinate
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
	/**
	 * Adds a vector to this one and returns the result
	 * @param v2 - the other vector
	 * @return the sum of this vector and v2
	 */
	public Vector3 add(Vector3 v2) {
		return add(this, v2);
	}
	
	/**
	 * Subtracts a vector from this one and returns the result
	 * @param v2 - the other vector
	 * @return the difference of this vector and v2
	 */
	public Vector3 subtract(Vector3 v2) {
		return subtract(this, v2);
	}
	
	/**
	 * Scales this vector by a constant
	 * @param scaler - the constant to scale the vector by
	 * @return the vector scaled by scaler
	 */
	public Vector3 scale(double scaler) {
		return scale(this, scaler);
	}
	
	/**
	 * @return the magnitude of the vector
	 */
	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * @return the azimuthal angle of the vector - the angle from the x axis in the x-y plane (phi in physics, theta in math)
	 */
	public double azimuthal() {
		return Math.atan2(y, x);
	}
	
	/**
	 * @return the polar angle of the vector - the angle of declination from the z axis (theta in physics, phi in math)
	 */
	public double theta() {
		return Math.atan(Math.sqrt(x * x + y * y) / z);
	}

	/**
	 * Adds two vectors
	 * @param v1 - the first addend
	 * @param v2 - the second addend
	 * @return the sum of the two vectors
	 */
	public static Vector3 add(Vector3 v1, Vector3 v2) {
		return new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	
	/**
	 * Gives the multiplicative inverse of a vector
	 * @param v1 - the input vector
	 * @return the multiplicative inverse
	 */
	public static Vector3 inverse(Vector3 v1) {
		return new Vector3(-v1.x, -v1.y, -v1.z);
	}
	
	/**
	 * Subtracts one vector from another
	 * @param v1 - the initial vector
	 * @param v2 - the vector to be subtracted
	 * @return v1 - v2
	 */
	public static Vector3 subtract(Vector3 v1, Vector3 v2) {
		return new Vector3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}
	
	/**
	 * Multiplies a vector by a scalar
	 * @param v1 - the initial vector
	 * @param scaler - the amount to scale the vector by
	 * @return the scaled vector
	 */
	public static Vector3 scale(Vector3 v1, double scaler) {
		return new Vector3(v1.x * scaler, v1.y * scaler, v1.z * scaler);
	}
	
	/**
	 * Gives the dot product (Euclidean scalar product) of two vectors
	 * @param v1 - one vector
	 * @param v2 - another vector
	 * @return the dot product of the vectors
	 */
	public static double dot(Vector3 v1, Vector3 v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}
	
	/**
	 * Gives the cross product of two vectors.  (Technically an axial vector, not a vector,
	 * or even better a rank-2 asymmetrical tensor, but we don't need to worry about that in this class.)
	 * @param v1 - the first vector
	 * @param v2 - the second vector
	 * @return - v1 x v2
	 */
	public static Vector3 cross(Vector3 v1, Vector3 v2) {
		return new Vector3(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z, v1.x * v2.y - v1.y * v2.x);
	}
	
	/**
	 * @return a null vector
	 */
	public static Vector3 nullVector() {
		return new Vector3(0, 0, 0);
	}
	
	/**
	 * @param v1 - the input vector
	 * @return a unit vector that points in the same direction as the input vector
	 */
	public static Vector3 unitVector(Vector3 v1) {
		if (v1.magnitude() == 0) {
			return nullVector();
		} else {
			return scale(v1, 1 / v1.magnitude());
		}
	}
	public static double round(double input){
		return (double)Math.round(input * Math.pow(10, 5)) / Math.pow(10, 5);
	}
	public static double round(double input, int precision){
		return (double)Math.round(input * Math.pow(10, precision)) / Math.pow(10, precision);
	}
	
}
