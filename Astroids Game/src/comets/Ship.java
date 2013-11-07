package comets;

public class Ship extends SpaceObject {

	// Ships radius and the angle the ship is pointing in
	final static double radius = 10;
	private double angle;
	
	// Ship constructor
	public Ship(double xpos, double ypos, double xvel, double yvel) {
		super(xpos, ypos, xvel, yvel, radius);
		
		// Set angle of ship based on its velocity
		angle = (Math.atan2(xVelocity, yVelocity)) + Math.PI;
	}
	
	// This is the accelerate method. It adds 0.1 frames/sec to the total velocity of the ship
	// It checks to make sure the ships speed doesn't exceed 10 frames/sec. If the ship is over 10 frames/sec
	// set the ships total velocity to 10 frames/sec
	public void accelerate() {
		
		this.xVelocity = this.xVelocity + (0.1 * Math.sin(this.getAngle()));
		this.yVelocity = this.yVelocity + (0.1 * Math.cos(this.getAngle()));
		
		double speed = Math.sqrt(Math.pow(this.xVelocity, 2) + Math.pow(this.yVelocity, 2));
		if(speed > 10){
			this.xVelocity = this.xVelocity * (10/speed);
			this.yVelocity = this.yVelocity * (10/speed);
		}
	}
	
	// This is the ships decelerate method. It allows the user to decelerate the ships velocity to 0.0 frame/sec
	// The ship cannot go backwards, just slow down to a stop. To ensure this the ships speed is checked to make sure
	// it is above 0.1 frames/sec. If it is not then the ships total velocity is reduced to 0.0
	public void decelerate() {
		
		double speed = Math.sqrt(Math.pow(this.xVelocity, 2) + Math.pow(this.yVelocity, 2));

		if(speed > 0.1){
			this.xVelocity = this.xVelocity - (0.1 * Math.sin(this.getAngle()));
			this.yVelocity = this.yVelocity - (0.1 * Math.cos(this.getAngle()));
		}
		else{
			this.xVelocity = 0.0;
			this.yVelocity = 0.0;
		}
	}
	
	// This is the ships method for firing shots. This method creates a shot
	// with a velocity of 3 frames/sec traveling in the direction the ship is pointing.
	// It then returns the new shot.
	public Shot fire() {
		double xShotVel = (3 * Math.sin(this.getAngle())) + this.xVelocity;
		double yShotVel = (3 * Math.cos(this.getAngle())) + this.yVelocity;
		return new Shot(this.xPosition, this.yPosition, xShotVel, yShotVel);
	}

	// This method rotates the ship left by 0.1 radians. This is done by adding 0.1 to the angle of the ship,
	// Then adjusting the velocities of the ship so they are traveling in the same direction.
	public void rotateLeft() {
		this.angle += 0.1;
		double speed = Math.sqrt(Math.pow(this.xVelocity, 2) + Math.pow(this.yVelocity, 2));
		this.xVelocity = speed * (Math.sin(this.angle));
		this.yVelocity = speed * (Math.cos(this.angle));
	}

	// This method rotates the ship right by 0.1 radians. This is done by subtracting 0.1 from the angle of the ship,
	// Then adjusting the velocities of the ship so they are traveling in the same direction.
	public void rotateRight() {
		this.angle -= 0.1;
		double speed = Math.sqrt(Math.pow(this.xVelocity, 2) + Math.pow(this.yVelocity, 2));
		this.xVelocity = speed * (Math.sin(this.angle));
		this.yVelocity = speed * (Math.cos(this.angle));
	}

	// This method returns the angle of the ship.
	public double getAngle() {
		return angle;
	}

}
