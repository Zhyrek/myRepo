package fcgold;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Class used to show a simple example of using the dyn4j project using
 * Java2D for rendering.
 * <p>
 * This class can be used as a starting point for projects.
 * @author William Bittle
 * @version 3.2.0
 * @since 3.0.0
 */
public class FCGold extends JFrame {
	/** The serial version id */
	private static final long serialVersionUID = 5663760293144882635L;
	/** The scale 45 pixels per meter */
	public static double SCALE = 40.0;
	public static int curX, curY, mouseStartX, mouseStartY;
	public static double ex1,ey1,ex2,ey2;
	public static Vector2 offsetVector, click, drag, release;
	public static int editorStoredValue = -1;
	public static boolean paused = true;
	/** The conversion factor from nano to base */
	public static final double NANO_TO_BASE = 1.0e9;
	public static final double SNAP_TO_DISTANCE = 0.4;
	
	/**
	 * Custom Body class to add drawing functionality.
	 * @author William Bittle
	 * @version 3.0.2
	 * @since 3.0.0
	 */
	//test all pieces
	/*
	public static String levelData = "SR,400,550,700,50,20;SR,150,150,100,100,0;SR,190,150,100,100,30;SC,200,200,100;DR,400,400,50,50,15,1;DC,395,40,50,-30,1;"
	     		+ "CR,400,130,50,100,-20,1;BA,300,300,400,400;GA,150,450,80,80;W,100,400,40,0,0;"
	     		+ "W,250,450,40,0,100;W,300,400,40,0,-100;GR,220,350,100,30,0,1;GC,300,350,40,30,0;R,2,9,0,0,10,0,0,0,-10;R,0,9,0,0.5,10,0,0.5;"
	     		+ "R,0,9,0.5,0,10,0.5,0;R,0,9,0,0,11,0,0,0,10;R,0,10,0,0,11,0,0;R,0,5,0,0,6,0,0;J,500,100;W,220,350,40,0,100,12;BA,400,100,250,250";
	 
	*/
	//Cookie Jar
	//public static String levelData = "BA,-133.55,58.6,353.3,100.0,0;GA,349.3,53.45,100.0,100.0,0;SR,35.95,131.05,811.9,64.1,0.0,0;SR,358.15,-12.75,148.0,43.6,0.0,0;SC,288.1,-22.65,24.0,0;SC,254.25,0.75,24.0,0;SR,332.85,3.9,165.0,17.8,179.93555033144,0;SR,268.95,-13.25,49.3,17.7,-34.649628761939,0;SR,282.6,-4.3,52.7,19.6,0.0,0;SR,284.8,-10.05,33.0,28.8,-24.5582492217,0;SR,425.7,48.9,228.3,54.1,90.037907399343,0;SC,403.35,-61.7,24.0,0;SR,408.95,28.9,35.2,188.3,0.0,0;SC,440.75,-61.85,24.0,0;SC,421.7,-63.45,37.4,0;SR,-411.1,140.75,110.8,22.7,-34.961187631409,0;SC,-277.9,161.4,56.8,0;SR,-363.45,175.9,181.1,27.7,180.15679071631,0;SR,-292.35,154.5,50.0,50.0,0.0,0;SR,-347.15,146.8,133.8,35.7,0.0,0;SC,-450.15,173.75,32.0,0;SC,-366.15,114.6,31.2,0;SR,-413.85,159.7,63.4,28.5,-13.835950153652,0;SR,-374.95,132.0,28.6,49.5,56.843236277712,0;SR,-391.5,161.05,40.2,38.3,0.0,0;DR,418.55,-93.9,51.7,19.9,-18.334839528097,1,0;DC,422.7,-117.9,24.0,0.0,1,0";
	
	//basic cart level
	/*
	public static String levelData = "BA,100,400,300,300,0;GA,1250,-100,70,70,1;CR,25,400,100,250,0,1,2;W,-25,275,20,0,-100,3,2;W,-25,525,20,0,-100,4,2;GC,200,380,20,0,5;J,150,280,6;R,3,3,-0.5,0,4,-0.5,0;R,3,2,1.25,3.125,5,-0.5,0;R,3,2,1.25,-3.125,5,-0.5,0;R,0,2,1.25,-3.125,6,0,0;"
			+ "SR,100,0,800,50,0,7;SR,960,-230,800,50,-30,8;SR,557,-14,100,50,-15,9;SR,960,-30,400,50,-30,8";
	 */
	
	//mech test level
	public static String levelData = "BA,-133.55,58.6,353.3,100.0,0;SR,250,440,500,60,0;GC,150,100,40,0,1";
			
	public static double xPos = 400;
	public static double yPos = 300;
	public static double scroll = 1;
	public static boolean isScrolling = false;
	public static boolean addObject = false;
	public static boolean moveObject = false;
	public static boolean pauseToggle = false;
	public static boolean checkBodyLocation = false;
	public static boolean assignBodyLocation = false;
	public static boolean saveCurrentDesign = false;
	public static long time;
	
	/** The canvas to draw to */
	protected Canvas canvas;
	
	/** The dynamics engine */
	protected DesignEditor world;
	
	/** Whether the example is stopped or not */
	protected boolean stopped;
	
	/** The time stamp for the last iteration */
	protected long last;
	
	/**
	 * Default constructor for the window
	 */
	public FCGold() {
		super("FC Gold");
		// setup the JFrame
		setFocusable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				// before we stop the JVM stop the example
				stop();
				super.windowClosing(e);
			}
		});
		// create the size of the window
		Dimension size = new Dimension(800, 600);
		
		// create a canvas to paint to 
		this.canvas = new Canvas();
		this.canvas.setFocusable(true);
		this.canvas.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 32)
				{
					pauseToggle = true;
				}
				if(e.getKeyCode() == 44)
				{
					editorStoredValue = -1; //set backup cursor selection to "Move"
				}
				if(e.getKeyCode() == 46)
				{
					editorStoredValue = -2; //set backup cursor selection to "Delete"
				}
				if(e.getKeyCode() == 65)
				{
					editorStoredValue = 0; //set backup cursor selection to "CCW Wheel"
				}
				if(e.getKeyCode() == 83)
				{
					editorStoredValue = 1; //set backup cursor selection to "UP Wheel"
				}
				if(e.getKeyCode() == 68)
				{
					editorStoredValue = 2; //set backup cursor selection to "CW Wheel"
				}
				if(e.getKeyCode() == 81)
				{
					editorStoredValue = 3; //set backup cursor selection to "Water Rod"
				}
				if(e.getKeyCode() == 87)
				{
					editorStoredValue = 4; //set backup cursor selection to "Wood Rod"
				}
				if(e.getKeyCode() == 69)
				{
					editorStoredValue = 5; //set backup cursor selection to "Gold Rod"
				}
				if(e.getKeyCode() == 82)
				{
					editorStoredValue = 6; //set backup cursor selection to "Ghost Rod"
				}
				world.gmsv = editorStoredValue;
			}

			public void keyReleased(KeyEvent e) {
				
			}

			public void keyTyped(KeyEvent e) {
				
			}
		});
		this.canvas.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(true)
				{
					scroll *= Math.pow(0.95, e.getWheelRotation()); //scroll exponentially based on amount scrolled
					isScrolling = true; //do scrolling in run thread, to stay in sync
				}
				
				
			}
		});
		this.canvas.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent evt) {
				
			}
			public void mouseEntered(MouseEvent arg0) {
				
			}
			public void mouseExited(MouseEvent arg0) {
				
			}
			public void mousePressed(MouseEvent evt) {
				curX = evt.getX(); //set CurX to mouse x location
			    curY = evt.getY(); //set CurY to mouse y location
			    world.rodAttach1 = -1; //initialize rod attachment 1 index for an action
			    world.rodAttach2 = -1; //initialize rod attachment 2 index for an action
				if(paused) //self-explanatory
				{
					click = new Vector2(s2wX(curX), s2wY(curY));
					//world.click(new Vector2(s2wX(curX), s2wY(curY)));
				}
				System.out.println("press"); //lol testing
				mouseStartX = evt.getX(); //set unchanged mousePress location, in case you are dragging the screen
				mouseStartY = evt.getY(); //
			}
			public void mouseReleased(MouseEvent arg0) {
				if(paused) 
		        {
					release = new Vector2(s2wX(curX), s2wY(curY));
					//world.release(new Vector2(s2wX(curX), s2wY(curY)));
		        } 
			}
		});
		this.canvas.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent evt)  
		    { 
		   	 
		         
		    } 


		    // The necessary methods. 
		    public void mouseDragged(MouseEvent evt)  
		    { 
		    	curX = evt.getX(); //get current mouse location during drag
		        curY = evt.getY(); //
		        if(!paused || (world.gameMode == -1 && world.rodAttach1 == -1)) // if dragging screen (effective result of logic)
		        {
		        	xPos += ((mouseStartX-curX)); //move screen the difference between last-frames position and this one
			        yPos += ((mouseStartY-curY));
		        }
		        mouseStartX = curX; //set last position as current position, for next time
		        mouseStartY = curY;
		        if(paused)
		        {
		        	drag = new Vector2(s2wX(curX), s2wY(curY));
					//world.drag(new Vector2(s2wX(curX), s2wY(curY)));
		        }
		    }
		});
		this.canvas.setPreferredSize(size);
		this.canvas.setMinimumSize(size);
		this.canvas.setMaximumSize(size);
		
		// add the canvas to the JFrame
		this.add(this.canvas);
		
		// make the JFrame not resizable
		// (this way I dont have to worry about resize events)
		this.setResizable(false);
		
		// size everything
		this.pack();
		
		// make sure we are not stopped
		this.stopped = false;
		
		// setup the world
		this.initializeWorld(levelData);
	}
	protected void initializeWorld(String s) {
		// create the world
		this.world = new DesignEditor(s);
	}
	/**
	 * Start active rendering the example.
	 * <p>
	 * This should be called after the JFrame has been shown.
	 */
	public void start() {
		// initialize the last update time
		this.last = System.nanoTime();
		// don't allow AWT to paint the canvas since we are
		this.canvas.setIgnoreRepaint(true);
		// enable double buffering (the JFrame has to be
		// visible before this can be done)
		this.canvas.createBufferStrategy(2);
		
		Graphics2D g = (Graphics2D)this.canvas.getBufferStrategy().getDrawGraphics();
		g.setStroke(new BasicStroke((float) (SCALE/5)));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform move = AffineTransform.getTranslateInstance(400-xPos, 300-yPos);
		g.transform(move);
		this.render(g);
		g.dispose();
		BufferStrategy strategy = this.canvas.getBufferStrategy();
		if (!strategy.contentsLost()) {
			strategy.show();
		}
        Toolkit.getDefaultToolkit().sync();
        
		Thread thread = new Thread() {
			public void run() {
				// perform an infinite loop stopped
				// render as fast as possible
				while (!isStopped()) {
					try {
						Thread.sleep(gameLoop());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// you could add a Thread.yield(); or
					// Thread.sleep(long) here to give the
					// CPU some breathing room
				}
			}
		};
		// set the game loop thread to a daemon thread so that
		// it cannot stop the JVM from exiting
		thread.setDaemon(true);
		// start the game loop
		thread.start();
	}
	
	/**
	 * The method calling the necessary methods to update
	 * the game, graphics, and poll for input.
	 */
	protected long gameLoop() {
			
			if(click != null)
			{
				world.click(click);
				click = null;
			}
			if(drag != null)
			{
				world.drag(drag);
				drag = null;
			}
			if(release != null)
			{
				world.release(release);
				release = null;
			}
			if(pauseToggle)
			{
				paused = !paused;
				if(paused)
				{
					world.initializeWorld();
				}
				else
				{
					world.saveCurrentDesign();
				}
				time = System.nanoTime();
				this.last = time;
				pauseToggle = false;
			}
			
			applyInputs();
			// get the graphics object to render to
			Graphics2D g = (Graphics2D)this.canvas.getBufferStrategy().getDrawGraphics();
			g.setStroke(new BasicStroke((float) (SCALE/5),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
			// before we render everything im going to flip the y axis and move the
			// origin to the center (instead of it being in the top left corner)
			//AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
			AffineTransform move = AffineTransform.getTranslateInstance(400-xPos, 300-yPos);
			//g.transform(yFlip);
			g.transform(move);
			// now (0, 0) is in the center of the screen with the positive x axis
			// pointing right and the positive y axis pointing up
			
			// render anything about the Example (will render the World objects)
			this.render(g);
			
			// dispose of the graphics object
			g.dispose();
			
			// blit/flip the buffer
			BufferStrategy strategy = this.canvas.getBufferStrategy();
			if (!strategy.contentsLost()) {
				strategy.show();
			}
			
			// Sync the display on some systems.
	        // (on Linux, this fixes event queue problems)
	        Toolkit.getDefaultToolkit().sync();
	        
	        // update the World
	    if(!paused)
	    {
	        // get the current time
	        time = System.nanoTime();
	        // get the elapsed time from the last iteration
	        long diff = time - this.last;
	        // set the last time
	        this.last = time;
	    	// convert from nanoseconds to seconds
	    	double elapsedTime = diff / NANO_TO_BASE;
	        // update the world with the elapsed time
	        this.world.update(0.02);
	        if(elapsedTime < 0.02)
	        {
	        	return (long) (20-elapsedTime*1000);
	        }
	        
	        //SCALE += 0.01;
		}
	    return 0;
	}

	private void applyInputs() 
	{
		if(isScrolling)
		{
			SCALE *= scroll; //adjust scale, and world viewing offset, by 'scroll'
			xPos *= scroll;
			yPos *= scroll;
			scroll = 1; //reset scroll
			isScrolling = false; //end scrolling
		}
		
	}
	public int i(Body b)
	{
		return this.world.getBodies().indexOf(b);
	}
	public double sc(double d)
	{
		if(d < 0.01)
		{
			return 0.01;
		}
		return d;
	}
	public double s2wX(double x)
	{
		return (x+xPos-400)/SCALE;
	}
	public double s2wY(double y)
	{
		return (y+yPos-300)/SCALE+1;
	}
	protected void render(Graphics2D g) {
		// lets draw over everything with a white background
		g.setColor(new Color(160, 180, 255));
		g.fillRect(-400+(int)xPos, -300+(int)yPos, 800, 600);
		
		// lets move the view up some
		g.translate(0.0, -1.0 * SCALE);
		
		// draw all the objects in the world
		renderPieces(g);
	}
	private void renderPieces(Graphics2D g) 
	{
		for(Body b: this.world.getBodies())
		{
			if(b instanceof BuildArea)
			{
				((GamePiece) b).render(g,  SCALE);
			}
		}
		for(Body b: this.world.getBodies())
		{
			if(b instanceof BuildArea)
			{
				((GamePiece) b).render2(g,  SCALE);
			}
		}
		for(Body b: this.world.getBodies())
		{
			if(b instanceof GoalArea)
			{
				((GamePiece) b).render(g,  SCALE);
			}
		}
		for(Body b: this.world.getBodies())
		{
			if(b instanceof GoalArea)
			{
				((GamePiece) b).render2(g,  SCALE);
			}
		}
		for(Body b: this.world.getBodies())
		{
			if(b instanceof GamePiece && (!(b instanceof BuildArea)) && (!(b instanceof GoalArea)))
			{
				((GamePiece) b).render(g,  SCALE);
			}
		}
		for(Body b: this.world.getBodies())
		{
			if(b instanceof GamePiece && (!(b instanceof BuildArea)) && (!(b instanceof GoalArea)))
			{
				((GamePiece) b).render2(g,  SCALE);
			}
		}
	}
	/**
	 * Stops the example.
	 */
	public synchronized void stop() {
		this.stopped = true;
	}
	public boolean isJointed(String s)
	{
		if( s.equals("1"))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the example is stopped.
	 * @return boolean true if stopped
	 */
	public synchronized boolean isStopped() {
		return this.stopped;
	}
	
	/**
	 * Entry point for the example application.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		// set the look and feel to the system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		// create the example JFrame
		FCGold window = new FCGold();
		// show it
		window.setVisible(true);
		
		
		// start it
		window.start();
	}
}