package comets;

import java.util.Random;
import java.util.Vector;

public class Comet extends SpaceObject {

	// Comet constructor
	public Comet(double xpos, double ypos, double xvel, double yvel,
			double radius) {
		super(xpos, ypos, xvel, yvel, radius);
	}

	// Abstract explode method
	public Vector<Comet> explode(){

        int n;
        double childCometRadius;
        Vector<Comet> childComets = new Vector<Comet>();
        
        if(this.radius >= 40.0){
            n = 2;
            childCometRadius = (this.radius)/(4.0/3.0);
        }else if(this.radius < 40.0 && this.radius >= 30.0){
            n = 3;
            childCometRadius = (this.radius)/(3.0/2.0);
		}else
            return childComets;
		        
        Random rand = new Random();
        
        for(int i = 0; i < n; i++){
            double angle = 2 * (Math.PI) * rand.nextDouble();
            double speed = rand.nextDouble() * 10;
            double xVel = speed * (Math.sin(angle));
            double yVel = speed * (Math.cos(angle));
            childComets.add(new Comet(this.xPosition, this.yPosition, xVel, yVel, childCometRadius));
        }
        
        return childComets;
    
    }


}
