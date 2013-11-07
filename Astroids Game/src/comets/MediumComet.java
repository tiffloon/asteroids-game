package comets;

import java.util.Random;
import java.util.Vector;

public class MediumComet extends Comet {
	
	// Medium comet radius
	final static double radius = 30;
	
	// Medium Comet constructor
	public MediumComet(double xpos, double ypos, double xvel, double yvel) {
		super(xpos, ypos, xvel, yvel, radius);
	}

	// Medium Comet explode method
	// This method adds 3 new small comets to a new comet vector and returns it.
	@Override
	public Vector<Comet> explode() {
		// Create a random number generator.
		Random rand = new Random();
		
		// Creates an empty comet vector
		Vector<Comet> comets = new Vector<Comet>();
		
		// Generate a random angle and speed for each small comet created and add it to the comets vector
		for(int i = 0; i < 3; i++){
			double angle = 2 * (Math.PI) * rand.nextDouble();
			double speed = rand.nextDouble() * 10;
			double xVel = speed * (Math.sin(angle));
			double yVel = speed * (Math.cos(angle));
			comets.add(new SmallComet(this.xPosition, this.yPosition, xVel, yVel));
		}
		// return the new comets vector
		return comets;
	}

}
