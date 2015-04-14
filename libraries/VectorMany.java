package libraries;

public class VectorMany {
	private double[] dimensions;
	
	public VectorMany(int numDimensions){
		dimensions = new double[numDimensions];
	}
	public VectorMany(){
		dimensions = new double[3];
	}
	public double[] getDimensions(){
		return dimensions;
	}
	
	public String toString(){
		String result = "";
		for(double x : dimensions){
			result += x + "\t";
		}
		return result;
	}
	public double getDimension(int location){
		return dimensions[location];
	}
	public void setDimension(int location, double newValue){
		dimensions[location] = newValue;
	}
	public int getNumDimensions(){
		return dimensions.length;
	}
	public void add(int location, double number){
		dimensions[location] = number;
	}
	public void add(VectorMany newVector){
		if(newVector.getNumDimensions() == dimensions.length){
			for(int i=0; i<dimensions.length; i++){
				dimensions[i] += newVector.getDimension(i);
			}
		}
	}
	public static VectorMany add(VectorMany vector1, VectorMany vector2){
		if(vector1.getNumDimensions() == vector2.getNumDimensions()){
			VectorMany newVector = new VectorMany(vector1.getNumDimensions());
			for(int i=0; i<vector1.getNumDimensions(); i++){
				newVector.setDimension(i, vector1.getDimension(i)+vector2.getDimension(i));
			}
			return newVector;
		}
		return new VectorMany();
	}
	public void subtract(VectorMany newVector){
		if(newVector.getNumDimensions() == dimensions.length){
			for(int i=0; i<dimensions.length; i++){
				dimensions[i] -= newVector.getDimension(i);
			}
		}
	}
	public static VectorMany subtract(VectorMany vector1, VectorMany vector2){
		if(vector1.getNumDimensions() == vector2.getNumDimensions()){
			VectorMany newVector = new VectorMany(vector1.getNumDimensions());
			for(int i=0; i<vector1.getNumDimensions(); i++){
				newVector.setDimension(i, vector1.getDimension(i)-vector2.getDimension(i));
			}
			return newVector;
		}
		return new VectorMany();
	}
	public double magnitude(){
		double total = 0;
		for(double x : dimensions){
			total += x*x;
		}
		return Math.sqrt(total);
	}
	public VectorMany scale(double scalar){
		VectorMany newVector = new VectorMany(dimensions.length);
		for(int i=0; i<dimensions.length; i++){
			newVector.setDimension(i, dimensions[i] * scalar);
		}
		return newVector;
	}
}
