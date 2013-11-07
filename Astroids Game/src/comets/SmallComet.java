package comets;

import java.util.Vector;

public class SmallComet extends Comet {
	
	// Small comet radius
	final static double radius = 20;
	
	// Small Comet constructor
	public SmallComet(double xpos, double ypos, double xvel, double yvel) {
		super(xpos, ypos, xvel, yvel, radius);
	}

	// Small Comet explode method which creates an empty comet vector and returns it.
	@Override
	public Vector<Comet> explode() {
		Vector<Comet> comets = new Vector<Comet>();
		return comets;
	}

}
