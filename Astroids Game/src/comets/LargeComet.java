package comets;

import java.util.Vector;
import java.util.Random;

public class LargeComet extends Comet {
	
	// Large comet radius
	final static double radius = 40;
	
	// Large comet constructor
	public LargeComet(double xpos, double ypos, double xvel, double yvel) {
		super(xpos, ypos, xvel, yvel, radius);
	}

	// Large Comet explode method 
	// This method adds 2 new medium comets to a new comet vector and returns it.
	@Override
	public Vector<Comet> explode() {
		// Create a random number generator.
		Random rand = new Random();
		// Creates an empty comet vector
		Vector<Comet> comets = new Vector<Comet>();

		// Generate a random angle and speed for each medium comet created and add it to the comets vector
		for(int i = 0; i < 2; i++){
			double angle = 2 * (Math.PI) * rand.nextDouble();
			double speed = rand.nextDouble() * 10;
			double xVel = speed * (Math.sin(angle));
			double yVel = speed * (Math.cos(angle));
			comets.add(new MediumComet(this.xPosition, this.yPosition, xVel, yVel));
		}
		// return new comet vector
		return comets;
	}
}
