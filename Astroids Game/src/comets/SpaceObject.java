package comets;

public abstract class SpaceObject {

	// Playing field constants.
	public static double playfieldWidth;
	public static double playfieldHeight;
	
	// All space object attributes
	protected double xPosition;
	protected double yPosition;
	protected double xVelocity;
	protected double yVelocity;
	protected double radius;
	
	// Space object constructor
	public SpaceObject(double xpos, double ypos, double xvel, double yvel, double radius) {
		
		xPosition = xpos;
		yPosition = ypos;
		xVelocity = xvel;
		yVelocity = yvel;
		this.radius = radius;
	}
	
	// This method returns the radius of the space object
	public double getRadius() {
		return radius;
	}
	
	// This method returns the xPosition of the space object
	public double getXPosition() {
		return xPosition;
	}
	
	// This method returns the yPosition of the space object
	public double getYPosition() {
		return yPosition;
	}
	
	// This method moves the object by adding the velocity to the position of the object.
	// If the object is going out of bounds modify the position of the object so it re-enters
	// the playing field on the opposite end it left.
	public void move() {
		this.xPosition = this.xPosition + this.xVelocity;
		this.yPosition = this.yPosition + this.yVelocity;
		
		if(this.xPosition > playfieldWidth)
			this.xPosition = this.xPosition - playfieldWidth;
		else if(this.xPosition < 0)
			this.xPosition = playfieldWidth + this.xPosition;
		if(this.yPosition > playfieldHeight)
			this.yPosition = this.yPosition - playfieldHeight;
		else if(this.yPosition < 0)
			this.yPosition = this.yPosition + playfieldHeight;
	}
	
	// This method checks to see if either of the objects overlap
	// If the objects overlap return true, else return false.
	public boolean overlapping(SpaceObject rhs){
		double i = Math.sqrt((Math.pow((this.xPosition - rhs.xPosition), 2) + Math.pow((this.yPosition - rhs.yPosition), 2)));
		if(i < (this.radius + rhs.radius)){
			return true;
		}
		return false;
	}

}
