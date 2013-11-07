package comets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.util.*;
import java.io.*;

// This class is primarily responsible for organizing the game of Comets
public class CometsMain implements KeyListener, ActionListener
{
	// GUI Data
	private JFrame frame; // The window itself
	private Canvas playArea;  // The area where the game takes place
	
	private final int playWidth = 500; // The width of the play area (in pixels)
	private final int playHeight = 500; // The height of the play area (in pixels)
	
	// Game Images
	private BufferedImage backgroundImg;
	private BufferedImage shipImg;
	private BufferedImage gameOverImg;
	private BufferedImage eraseShipImg;
	
	// Game Data
	private Ship ship; // The ship in play
	private Vector<Shot> shots; // The shots fired by the player
	private Vector<Comet> comets; // The comets floating around
	private volatile int numOfComets = 0;
	
	private boolean shipDead; // Whether or not the ship has been blown up
	private long shipTimeOfDeath; // The time at which the ship blew up
	
	// Keyboard data
	// Indicates whether the player is currently holding the accelerate, turn
	// left, or turn right buttons, respectively
	private boolean accelerateHeld;
	private boolean decelerateHeld;
	private boolean turnLeftHeld;
	private boolean turnRightHeld;
	
	// Indicates whether the player struck the fire key
	private boolean firing;
	
	// Indicates whether the game is over
	private volatile boolean gameOver = false;
	
	// Indicates whether the user wants to play a new game.
	private volatile boolean newGame = false;
	
	private volatile boolean firstGame = true;
	
	// Scoreboard variables
	private int largeComets = 0;
	private int mediumComets = 0;
	private int smallComets = 0;
	private int shipsRemaining = 0;
	private int shotsFired = 0;
	
	// Scoreboard Panel and labels for displaying the scoreboard
	private JPanel scoreBoard;
	private JLabel largeCometsLeft;
	private JLabel smallCometsLeft;
	private JLabel mediumCometsLeft;
	private JLabel shipsLeft;
	private JLabel totalShotsFired;
	
	//Menu bar for user options
	private JMenuBar menuBar; // Menu bar
	private JMenu optionMenu; // Options dropdown menu
	private JMenuItem newGameItem; // New game option
	private JMenuItem aboutGameItem; // About game option
	private JMenuItem exitGameItem; // Exit Option
	
	// Set up the game and play!
	public CometsMain()
	{
		// Get everything set up
		configureGUI();
		//configureGameData();
		
		// Display the window so play can begin
		frame.setVisible(true);
		
		//Set these to true to start first game.
		gameOver = true;
		newGame = true;
		
		// Loop until the user exits the program. This allows them to play more games if they want.
		while(true){
			
			// If user wants to play new game and the last game is over,
			// set gameOver to false and start a new game.
			if(gameOver == true && newGame == true){
				gameOver = false;
				playGame();
				this.drawGameOverImage(playArea.getGraphics()); // Draw Game Over Image
			}
		}
	}
	
	// Set up the initial positions of all space objects
	private void configureGameData()
	{
		// Configure the play area size
		SpaceObject.playfieldWidth = playWidth;
		SpaceObject.playfieldHeight = playHeight;
		
		// Reset all scoreboard counts 
		largeComets = 0;
		mediumComets = 0;
		smallComets = 0;
		shipsRemaining = 5;
		shotsFired = 0;
		
		//Set boolean attributes for key events to false
		accelerateHeld = false;
		decelerateHeld = false;
		turnLeftHeld = false;
		turnRightHeld = false;
		firing = false;
		
		// Create the ship
		ship = null;
		ship = new Ship(playWidth/2, playHeight/2, 0, 0);
		
		// Create the shot vector (initially, there shouldn't be any shots on the screen)
		shots = null;
		shots = new Vector<Shot>();
		
		// Read the comets from comets.cfg
		comets = null;
		comets = new Vector<Comet>();
		
		Random rand = new Random();
		
		for(int i= 0; i < numOfComets; i++){
            double angle = 2 * (Math.PI) * rand.nextDouble();
            double speed = rand.nextDouble() * 5;
            double xVel = speed * (Math.sin(angle));
            double yVel = speed * (Math.cos(angle));
            double xpos = rand.nextDouble() * 480;
            double ypos = rand.nextDouble() * 480;
            double radius = (rand.nextDouble() * 20) + 30;
            double collide = Math.sqrt((Math.pow((ship.getXPosition() - xpos), 2) + Math.pow((ship.getYPosition() - ypos), 2)));
            
            while(collide < (ship.getRadius() + radius)){
            	xpos = rand.nextDouble() * 480;
                ypos = rand.nextDouble() * 480;
                collide = Math.sqrt((Math.pow((ship.getXPosition() - xpos), 2) + Math.pow((ship.getYPosition() - ypos), 2)));
    		}
            
            comets.add(new Comet(xpos, ypos, xVel, yVel, radius));
            if(radius >= 40.0)
				largeComets  += 1;
			else if(radius < 40.0 && radius >=30.0)
				mediumComets += 1;
			else 
				smallComets += 1;
        }
		
		
//		try
//		{
//			Scanner fin = new Scanner(new File("comets.cfg"));
//			
//			// Loop through each line of the file to read a comet
//			while(fin.hasNext())
//			{				
//			
//				double radius = fin.nextDouble();
//				double xpos = fin.nextDouble();
//				double ypos = fin.nextDouble();
//				double xvel = fin.nextDouble();
//				double yvel = fin.nextDouble();
//				
//				comets.add(new Comet(xpos, ypos, xvel, yvel, radius));
//				
//				if(radius >= 40.0)
//					largeComets  += 1;
//				else if(radius < 40.0 && radius >=30.0)
//					mediumComets += 1;
//				else 
//					smallComets += 1;
//				
//			}
//			// close scanner
//			fin.close();
//		}
//		// If the file could not be read correctly for whatever reason, abort
//		// the program
//		catch(FileNotFoundException e)
//		{
//			System.err.println("Unable to locate comets.cfg");
//			System.exit(0);
//		}
//		catch(Exception e)
//		{
//			System.err.println("comets.cfg is not in a proper format");
//			System.exit(0);
//		}
	}
	
	
	
	// Set up the game window
	private void configureGUI()
	{
		// Create the window object
		frame = new JFrame("Comets");
		frame.setSize(playWidth+20, playHeight+130);
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.black);
		
		// The program should end when the window is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set the window's layout manager
		frame.setLayout(new FlowLayout());

		// call getImage method to set each of the image attributes
		shipImg = this.getImage(new File("spaceship.png"));
		eraseShipImg = this.getImage(new File("eraseSpaceship.png"));
		backgroundImg = this.getImage(new File("space.png"));
		gameOverImg = this.getImage(new File("gameOver.png"));
		
		// Create menu bar and menu for game window
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 500, 21);
		optionMenu = new JMenu("Options");
		menuBar.add(optionMenu);
		
		// Adding menuListener so that the menu doesn't get painted over the canvas when the game has ended.
		optionMenu.addMenuListener(
			new MenuListener(){
		
				// When the optionMenu is canceled, reset the game over image in the canvas when the game is over.
				@Override
				public void menuCanceled(MenuEvent e) {
					if(gameOver == true)
						playArea.getGraphics().drawImage(gameOverImg, 0, 0, null);
				}

				// When the optionMenu is deselected, reset the game over image in the canvas when the game is over.
				@Override
				public void menuDeselected(MenuEvent e) {
					if(gameOver == true)
						playArea.getGraphics().drawImage(gameOverImg, 0, 0, null);
				}
			
				// No action needed when the menu is selected, but this method is required for the menuListener.
				@Override
				public void menuSelected(MenuEvent e) {
				}
			}
		);
		
		// Create and add new game option to the menu
		newGameItem = new JMenuItem("New Game");
		optionMenu.add(newGameItem);
		
		// Create and add about game option to the menu
		aboutGameItem = new JMenuItem("About");
		optionMenu.add(aboutGameItem);
		
		// Create and add exit game option to the menu
		exitGameItem = new JMenuItem("Exit Game");
		optionMenu.add(exitGameItem);
		
		// Add actionListeners for each menuItem so that they can perform their unique action when selected.
		newGameItem.addActionListener(this);
		aboutGameItem.addActionListener(this);
		exitGameItem.addActionListener(this);
		
		// Add the menuBar to the Window
		frame.setJMenuBar(menuBar);
		
		// Create the play area
		playArea = new Canvas();
		playArea.setIgnoreRepaint(true); // Set to true so the menu doesn't get painted on the canvas after it is canceled
		playArea.setSize(playWidth, playHeight);
		playArea.setBackground(Color.BLACK);
		playArea.setFocusable(false);
		frame.add(playArea);	
		
		// Create and add scoreBoard to window.
		createScoreboard();
		frame.add(scoreBoard);
		
		// Make the frame listen to keystrokes
		frame.addKeyListener(this);
		
	}
	
	// This method takes in a file and if the file is able to be read, it is returned. Otherwise return null.
	private BufferedImage getImage(File image) {
		try {
			return ImageIO.read(image);
		} catch (IOException e1){
			System.err.println("Unable to read " + image.getName());
			return null;
		}
	}

	// Create the scoreboard for the game.
	private void createScoreboard() {
		
		// Creates the panel for the scoreboard information to go in
		scoreBoard = new JPanel();
		scoreBoard.setPreferredSize(new Dimension(510, 85));
		scoreBoard.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		scoreBoard.setBackground(Color.BLACK);
		scoreBoard.setLayout(null);

		// Create the label for number of small comets in play and add it to the scoreBoard
		smallCometsLeft = new JLabel(String.format("Number of Small Comets: %d", smallComets));
		smallCometsLeft.setFont(new Font("Consolas", Font.BOLD, 13));
		smallCometsLeft.setPreferredSize(new Dimension(182, 14));
		smallCometsLeft.setForeground(Color.WHITE);
		scoreBoard.add(smallCometsLeft).setBounds(30, 5, 210, 14);
		
		// Create the label for number of medium comets in play and add it to the scoreBoard
		mediumCometsLeft = new JLabel(String.format("Number of Medium Comets: %d", mediumComets));
		mediumCometsLeft.setFont(new Font("Consolas", Font.BOLD, 13));
		mediumCometsLeft.setPreferredSize(new Dimension(182, 14));
		mediumCometsLeft.setForeground(Color.WHITE);
		scoreBoard.add(mediumCometsLeft).setBounds(30, 25, 210, 14);
		
		// Create the label for number of large comets in play and add it to the scoreBoard
		largeCometsLeft = new JLabel(String.format("Number of Large Comets: %d", largeComets));
		largeCometsLeft.setFont(new Font("Consolas", Font.BOLD, 13));
		largeCometsLeft.setPreferredSize(new Dimension(182, 14));
		largeCometsLeft.setForeground(Color.WHITE);
		scoreBoard.add(largeCometsLeft).setBounds(30, 45, 210, 14);
		
		// Create the label for number of shots fired and add it to the scoreBoard
		totalShotsFired = new JLabel(String.format("Number of Shots Fired: %d", shotsFired));
		totalShotsFired.setFont(new Font("Consolas", Font.BOLD, 13));
		totalShotsFired.setPreferredSize(new Dimension(182, 14));
		totalShotsFired.setForeground(Color.WHITE);
		scoreBoard.add(totalShotsFired).setBounds(260, 5, 210, 14);
		
		// Create the label for number of ships left to use and add it to the scoreBoard
		shipsLeft = new JLabel(String.format("Number of Ships Remaining: %d", shipsRemaining));
		shipsLeft.setFont(new Font("Consolas", Font.BOLD, 13));
		shipsLeft.setPreferredSize(new Dimension(182, 14));
		shipsLeft.setForeground(Color.WHITE);
		scoreBoard.add(shipsLeft).setBounds(260, 25, 210, 14);
		
	}

	// The main game loop.
	// This method coordinates everything that happens in the game
	private void playGame()
	{
		// while game is not over, loop.
		while(!gameOver)
		{
			// If a newGame is starting, reset the game data
			if(newGame == true){
				do{
					String numComets = JOptionPane.showInputDialog(frame, "How many comets would you like to start with?", "Select the Number of Comets", JOptionPane.QUESTION_MESSAGE);
					if(numComets != null){
						try{
							numOfComets = Integer.parseInt(numComets);
							this.configureGameData();
							playArea.repaint();
							shipDead = false;
							firstGame = false;
						}catch(NumberFormatException e){
							JOptionPane.showMessageDialog(frame, "Sorry you didn't enter a valid number of comets.");
						}
					}
				}while(firstGame);
				newGame = false;
				
			}
			
			// Measure the current time in an effort to keep up a consistent
			// frame rate
			long time = System.currentTimeMillis();
			
			// If the ship has been dead for more than 3 seconds, revive it
			if(shipDead && shipTimeOfDeath + 3000 < time)
			{
				shipDead = false;
				ship = new Ship(playWidth/2, playHeight/2, 0, 0);
			}
			
			// Process game events, move all the objects floating around,
			// update the display and update the scoreboard
			if(!shipDead)
				handleKeyEntries();
			handleCollisions();
			moveSpaceObjects();
			updateScoreboard();
			
			// Sleep until it's time to draw the next frame 
			// (i.e. 32 ms after this frame started processing)
			try
			{
				long delay = Math.max(0, 32-(System.currentTimeMillis()-time));
				
				Thread.sleep(delay);
				//playArea.getGraphics().drawImage(backgroundImg, 0 , 0 , playArea);

			}
			catch(InterruptedException e)
			{
				
			}
			
			// If the player has destroyed all comets or has run out of ships, set gameOver to true and repaint the canvas.
			if(comets.isEmpty() || shipsRemaining == 0){
				gameOver = true;
				playArea.repaint();
			}
			
		}
		// Draw Game Over Image
		this.drawGameOverImage(playArea.getGraphics());
	}
	
	// This method draws the game over image on the playArea.
	private void drawGameOverImage(Graphics g){
		g.setColor(Color.white);
		// If there is a gameOverImg do this.
		if(gameOverImg != null){
			g.drawImage(gameOverImg, 0, 0, null);
		}
		// Otherwise print Game Over! string.
		else{
			g.drawString("Game Over!", 240, 240);
		}
	}

	// This method updates all the scoreBoard labels and repaints them on the scoreBoard.
	private void updateScoreboard() {
		largeCometsLeft.setText(String.format("Number of Large Comets: %d", largeComets));
		mediumCometsLeft.setText(String.format("Number of Medium Comets: %d", mediumComets));
		smallCometsLeft.setText(String.format("Number of Small Comets: %d", smallComets));
		totalShotsFired.setText(String.format("Number of Shots Fired: %d", shotsFired));
		shipsLeft.setText(String.format("Number of Ships Remaining: %d", shipsRemaining));
		scoreBoard.repaint();
	}

	// Deal with objects hitting each other
	private void handleCollisions()
	{
		// Anything that is destroyed should be erased, so get ready
		// to erase stuff
		Graphics g = playArea.getGraphics();
		g.setColor(Color.BLACK);
		
		// Deal with shots blowing up comets
		for(int i = 0; i < shots.size(); i++)
		{
			Shot s = shots.elementAt(i);
			for(int j = 0; j < comets.size(); j++)
			{
				Comet c = comets.elementAt(j);
				
				// If a shot has hit a comet, destroy both the shot and comet
				if(s.overlapping(c))
				{
					// If shot hit comet, play explosion sound
					explosionAudio();
					
					// Remove the bullet from the vector
					shots.remove(i);
					i--;
					this.drawSpaceObject(g, s);
					
					// If the comet was actually destroyed, replace the comet
					// with the new comets it spawned (if any)
					Vector<Comet> newComets = c.explode();
					
					// Update the number of each comet now in play based on the size of the newComets vector.
					if(newComets.size()==0){
						smallComets -= 1;
					}
					else if(newComets.size()==2){
						largeComets -= 1;
						mediumComets += 2;
					}
					else if(newComets.size()==3){
						smallComets += 3;
						mediumComets -= 1;
					}
					
					// Remove the destroyed comet from the comets vector and add the new ones.
					if(newComets != null)
					{
						this.drawSpaceObject(g, c);
						comets.remove(j);
						j--;
						comets.addAll(newComets);		
					}
					break;
				}
			}
		}
		
		// Deal with comets blowing up the ship
		if(!shipDead)
		{
			for(Comet c : comets)
			{
				// If the ship hit a comet, kill the ship and mark down the time 
				if(c.overlapping(ship))
				{
					// Play explosion sound clip since ship was destroyed
					explosionAudio();
					shipTimeOfDeath = System.currentTimeMillis();
					shipDead = true;
					drawShip(g, ship, true);
					
					// Player lost a ship, so remove 1 from the shipsRemaining attribute
					shipsRemaining -= 1;
		
				}
			}
		}
	}
	
	// Check which keys have been pressed and respond accordingly
	private void handleKeyEntries()
	{
		// Ship movement keys
		if(accelerateHeld)
			ship.accelerate();
		if(decelerateHeld)
			ship.decelerate();
	
		// Shooting the cannon and add 1 to shotsFired
		if(firing)
		{
			firing = false;
			shots.add(ship.fire());
			shotsFired += 1;
		}
	}
	
	// Deal with moving all the objects that are floating around
	private void moveSpaceObjects()
	{
		Graphics g = playArea.getGraphics();
		// Only update the background if the backgroundImg is not null
		if(backgroundImg != null)
			// Redraw the background image so the old spaceObjects are painted over.
			g.drawImage(backgroundImg, 0, 0, playArea);
		
		// Handle the movements of all objects in the field
		updateShots(g);
		updateComets(g);		
		if(!shipDead)
			updateShip(g);
	}
	
	// Move all comets and draw them to the screen
	private void updateComets(Graphics g)
	{
		for(Comet c : comets)
		{		
			// Erase previous comet if backgroundImg is null
			if(backgroundImg == null){
				// Erase the comet at its old position
				g.setColor(Color.BLACK);
				drawSpaceObject(g, c);
			}
			
			// Move the comet to its new position
			c.move();
			
			// Draw it at its new position
			g.setColor(Color.CYAN);
			drawSpaceObject(g, c);
			
		}
	}
	
	// Move all shots and draw them to the screen
	private void updateShots(Graphics g)
	{
		for(int i = 0; i < shots.size(); i++)
		{
			Shot s = shots.elementAt(i);
			
			// Erase previous shot if backgroundImg is null
			if(backgroundImg == null){
				// Erase the shot at its old position
				g.setColor(Color.BLACK);
				drawSpaceObject(g, s);
			}
			
			// Move the shot to its new position
			s.move();
			
			// Remove the shot if it's too old
			if(s.getAge() > 180)
			{
				shots.remove(i);
				i--;
			}
			// Otherwise, draw it at its new position
			else
			{
				g.setColor(Color.RED);
				drawSpaceObject(g, s);
			}		
		}
	}
	
	// Draws the space object s to the the specified graphics context
	private void drawSpaceObject(Graphics g, SpaceObject s)
	{
		// Figure out where the object should be drawn
		int radius = (int)s.getRadius();
		int xCenter = (int)s.getXPosition();
		int yCenter = (int)s.getYPosition();
		
		// Draw the object
		g.drawOval(xCenter - radius, yCenter - radius, radius*2, radius*2);

	}
	
	// Moves the ship and draws it at its new position
	private void updateShip(Graphics g)
	{
		// Erase previous ship if backgroundImg is null
		if(backgroundImg == null){
			// Erase the ship at its old position
			g.setColor(Color.BLACK);
			drawShip(g, ship, true);
		}
		
		// Ship rotation must be handled between erasing the ship at its old position
		// and drawing it at its new position so that artifacts aren't left on the screen
		if(turnLeftHeld)
			ship.rotateLeft();
		if(turnRightHeld)
			ship.rotateRight();
		ship.move();
		
		// Draw the ship at its new position
		g.setColor(Color.WHITE);
		drawShip(g, ship, false);
	}
	
	// Draws or erase this ship s to the specified graphics context 
	private void drawShip(Graphics g, Ship s, boolean erase)
	{
		// Figure out where the ship should be drawn
		int radius = (int)s.getRadius();
		int xCenter = (int)s.getXPosition();
		int yCenter = (int)s.getYPosition();
		
		// If the shipImg is not null draw the ship using shipImg
		if(shipImg != null && eraseShipImg != null){

			// Calculate the image angle so it rotates correctly.
			double imgAngle = Math.atan2(-Math.sin(s.getAngle()), Math.cos(s.getAngle()));
			
			// Rotate image to match the imgAngle above
			AffineTransform tx = AffineTransform.getRotateInstance(imgAngle, shipImg.getWidth()/2, shipImg.getWidth()/2);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			
			// If ship is not supposed to be erased draw the shipImg
			if(!erase){
				// Draw the rotated image.
				g.drawImage(op.filter(shipImg, null), xCenter - radius, yCenter - radius, null);
			}
			// Otherwise, erase ship by drawing eraseShipImg
			else{
				g.drawImage(op.filter(eraseShipImg, null), xCenter - radius, yCenter - radius, null);
			}
		}
		// If the shipImg or eraseShipImg was null, draw the ship using basic oval and line.
		else{
			// Draw the ship body
			g.drawOval(xCenter - radius, yCenter - radius, radius*2, radius*2);
			
			// Draw the gun turret
			int guntipXoffset = (int)(radius * 1.5 * Math.sin(s.getAngle()));
			int guntipYoffset = (int)(radius * 1.5 * Math.cos(s.getAngle()));
			g.drawLine(xCenter, yCenter, xCenter + guntipXoffset, yCenter + guntipYoffset);
		}
		
	}
		
	// Deals with keyboard keys being pressed
	public void keyPressed(KeyEvent key)
	{
		// Mark down which important keys have been pressed
		// Don't change if ship is dead.
		if(!shipDead){
			if(key.getKeyCode() == KeyEvent.VK_UP)
				this.accelerateHeld = true;
			if(key.getKeyCode() == KeyEvent.VK_DOWN)
				this.decelerateHeld = true;
			if(key.getKeyCode() == KeyEvent.VK_LEFT)
				this.turnLeftHeld = true;
			if(key.getKeyCode() == KeyEvent.VK_RIGHT)
				this.turnRightHeld = true;
			if(key.getKeyCode() == KeyEvent.VK_SPACE)
				this.firing = true;
		}
	}

	// Deals with keyboard keys being released
	public void keyReleased(KeyEvent key)
	{
		// Mark down which important keys are no longer being pressed
		if(key.getKeyCode() == KeyEvent.VK_UP)
			this.accelerateHeld = false;
		if(key.getKeyCode() == KeyEvent.VK_DOWN)
			this.decelerateHeld = false;
		if(key.getKeyCode() == KeyEvent.VK_LEFT)
			this.turnLeftHeld = false;
		if(key.getKeyCode() == KeyEvent.VK_RIGHT)
			this.turnRightHeld = false;
	}

	// This method is not actually used, but is required by the KeyListener interface
	public void keyTyped(KeyEvent arg0)
	{
	}
	
	// This method is called when the actionListener to one of the menuItems is triggered.
	public void actionPerformed( ActionEvent event )
	{
		// If newGameItem was selected set newGame to true.
		if(event.getSource().equals(newGameItem)){
			newGame = true;
			
		}
		// If exitGameItem was selected close window and exit game.
		else if(event.getSource().equals(exitGameItem)){
			System.exit(0);
		}
		// If aboutGameItem was selected show popup dialog window with the information about the game.
		else if(event.getSource().equals(aboutGameItem)){
			// If the game is over then repaint the canvas with the gameover image so the menu isn't painted over it when selected.
			if(gameOver == true)
				playArea.getGraphics().drawImage(gameOverImg, 0, 0, null);
			JOptionPane.showMessageDialog(frame, "Comets was created by: Tiffany Loon\nThis game was programmed in Java.\nComets was created on: 04/16/2013", "About the Game", JOptionPane.INFORMATION_MESSAGE, null);
			// If the game is over then repaint the canvas with the gameover image so the menu isn't painted over it when selected.
			if(gameOver == true)
				playArea.getGraphics().drawImage(gameOverImg, 0, 0, null);
		}
	} 
	
	// This method plays the explosion.wav sound clip.
	public static void explosionAudio(){

		try {
		    File yourFile = new File("explosion.wav");
		    AudioInputStream stream = AudioSystem.getAudioInputStream(yourFile);;
		    AudioFormat format = stream.getFormat();
		    DataLine.Info info = new DataLine.Info(Clip.class, format);
		    Clip clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		    clip.start();
		} catch (UnsupportedAudioFileException e) {
			System.err.println( "Error reading from the source" );
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			System.err.println( "Error reading from the source" );
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println( "Error reading from the source" );
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		// A GUI program begins by creating an instance of the GUI
		// object. The program is event driven from that point on.
		new CometsMain();

	}
	
}
