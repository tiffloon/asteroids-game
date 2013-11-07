package comets;

public class Shot extends SpaceObject {

	// The radius of shots and their age
	final static double radius = 3;
	private int age;

	// This is the Shot constructor
	public Shot(double xpos, double ypos, double xvel, double yvel) {
		super(xpos, ypos, xvel, yvel, radius);
		age = 0; // age is set to 0 since the shot is new
	}
	
	// The move method increases the age of the shot by one and then calls the superclasse's move method.
	public void move() {
		age++;
		super.move();
	}

	// This method returns the age of the shot.
	public int getAge() {
		return age;
	}

}
