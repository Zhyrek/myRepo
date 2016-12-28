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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.Vector3;

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
	public static ArrayList<StaticRect> staticRects = new ArrayList<StaticRect>();
	public static ArrayList<StaticCircle> staticCircles = new ArrayList<StaticCircle>();
	public static ArrayList<DynRect> dynRects = new ArrayList<DynRect>();
	public static ArrayList<DynCircle> dynCircles = new ArrayList<DynCircle>();
	public static ArrayList<CloudRect> cloudRects = new ArrayList<CloudRect>();
	public static ArrayList<BuildArea> buildAreas = new ArrayList<BuildArea>();
	public static ArrayList<GoalArea> goalAreas = new ArrayList<GoalArea>();
	public static ArrayList<Wheel> wheels = new ArrayList<Wheel>();
	public static ArrayList<GoalRect> goalRects = new ArrayList<GoalRect>();
	public static ArrayList<GoalCircle> goalCircles = new ArrayList<GoalCircle>();
	public static ArrayList<Rod> rods = new ArrayList<Rod>();
	public static ArrayList<Vector3> jointLocations = new ArrayList<Vector3>();
	/** The scale 45 pixels per meter */
	public static double SCALE = 40.0;
	public static int curX, curY, mouseStartX, mouseStartY,rodAttach1,rodAttach2;
	public static double ex1,ey1,ex2,ey2;
	public static int mode = 0;
	public static Vector2 offsetVector;
	public static int editorMode = -1;
	public static int editorStoredValue = -1;
	public static boolean paused = true;
	public ArrayList<double[]> movingRodEnds = new ArrayList<double[]>();
	public ArrayList<Integer> rodIndices = new ArrayList<Integer>();
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
	public static boolean resetWorld = false;
	public static boolean addObject = false;
	public static boolean moveObject = false;
	public static boolean checkBodyLocation = false;
	public static boolean assignBodyLocation = false;
	public static boolean saveCurrentDesign = false;
	public static long time;
	public static Body deleteObject;
	public static CategoryFilter cf1 = new CategoryFilter(1,7);
	public static CategoryFilter cf2 = new CategoryFilter(2,3);
	public static CategoryFilter cf3 = new CategoryFilter(4,1);
	public static CategoryFilter cf4 = new CategoryFilter(8,0);
	
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
					pause();
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
			    rodAttach1 = -1; //initialize rod attachment 1 index for an action
			    rodAttach2 = -1; //initialize rod attachment 2 index for an action
				if(paused) //self-explanatory
				{
					ex1 = s2wX(curX); //set event locations 1 and 2 equal to the world location of the mouse click
					ey1 = s2wY(curY); //
					ex2 = s2wX(curX); //
					ey2 = s2wY(curY); //
					Vector3 v; //dummy variable to store data in
					editorMode = editorStoredValue; //set cursor selection equal to backup cursor selection
					for(int i = 0; i < jointLocations.size(); i++) //check if the mousePress is close to a joint
					{
						v = jointLocations.get(i).copy(); 
						if(editorMode == -2)
						{
							Body b = getBodyAtLocation(ex1,ey1);
							if(b != null)
							{
								deleteObject = b;
							}
						}
						if(Math.hypot(v.x-ex1, v.y-ey1) < SNAP_TO_DISTANCE) //if within snap_to_distance away from joint
						{
							ex1 = v.x;//set event location 1 equal to joint location
							ey1 = v.y;//
							rodAttach1 = (int)v.z; //set rod attachment 1 index equal to stored joint index
							if(editorMode == -1) //if "move" cursor selection
							{
								moveObject = true;//move the object, within the run thread
								checkBodyLocation = true;//runs once per mousePress in run thread, initialize movement
							}
							break; //break if you find a nearby joint
						}
					}
				}
				else
				{
					
				}
				System.out.println("press"); //lol testing
				mouseStartX = evt.getX(); //set unchanged mousePress location, in case you are dragging the screen
				mouseStartY = evt.getY(); //
			}
			public void mouseReleased(MouseEvent arg0) {
				Vector3 v; //dummy var
				if(paused) 
		        {
					rodAttach2 = -1; //initialize rod attachment index 2
		        	ex2 = s2wX(curX); //event location 2 equals screen location of mouseRelease
					ey2 = s2wY(curY); //
					for(int i = 0; i < jointLocations.size(); i++) //check if release location is near a joint
					{
						v = jointLocations.get(i).copy();
						if(Math.hypot(v.x-ex2, v.y-ey2) < SNAP_TO_DISTANCE && (int)v.z != rodAttach1) //if near a joint that isn't the first joint, if applicable
						{
							ex2 = v.x;
							ey2 = v.y;
							rodAttach2 = (int)v.z;
							break;
						}
					}
					if(editorMode > -1)
					{
						addObject = true; //if you aren't moving or deleting object, create the object selected in run thread
					}
					else
					{
						assignBodyLocation = true; //finalize the movement/delete action in the run thread
					}
		        }
				else
				{
					editorMode = -1; //reset cursor selection (ends drawing if you unpause in the middle of creating object)
				}
				moveObject = false; //stop moving the selected object, if applicable
				rodIndices.clear(); //reset the array of rod indices (used to adjust indices when rods are deleted)
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
		        Vector3 v;
		        if(!paused || (editorMode == -1 && rodAttach1 == -1)) // if dragging screen (effective result of logic)
		        {
		        	xPos += ((mouseStartX-curX)); //move screen the difference between last-frames position and this one
			        yPos += ((mouseStartY-curY));
		        }
		        mouseStartX = curX; //set last position as current position, for next time
		        mouseStartY = curY;
		        if(paused)
		        {
		        	ex2 = s2wX(curX); //set event location 2 as current mouse location in-world
					ey2 = s2wY(curY);
					for(int i = 0; i < jointLocations.size(); i++)//snap-to, once again
					{
						v = jointLocations.get(i).copy(); //copy, cuz by reference. Same with previous ones
						if(Math.hypot(v.x-ex2, v.y-ey2) < SNAP_TO_DISTANCE && (int)v.z != rodAttach1)
						{
							ex2 = v.x;
							ey2 = v.y;
							break;
						}
					}
					if(editorMode == -1 && rodAttach1 != -1)
					{
						moveObject = true; //let selected object, when in move mode, be moved smoothly
					}
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
			if(resetWorld)
			{
				this.world.removeAllBodiesAndJoints();
				resetArrays();
				initializeWorld(levelData);
				resetWorld = false;
			}
			if(saveCurrentDesign)
			{
				saveCurrentDesign();
				saveCurrentDesign = false;
			}
			if(deleteObject != null)
			{
				deleteObject(deleteObject);
				deleteObject = null;
			}
			if(addObject)
			{
				addObject();
				editorMode = -1;
				addObject = false;
			}
			if(moveObject)
			{
				moveObject();
			}
			if(assignBodyLocation)
			{
				System.out.println("blah");
				Vector3 v;
				for(int i = 0; i < jointLocations.size(); i++)
				{
					v = jointLocations.get(i);
					
					if((int)v.z == rodAttach2)
					{
						v.x += s2wX(curX)-ex2;
						v.y += s2wY(curY)-ey2;
						jointLocations.set(i, v);
					}
				}
				if(rodAttach1 != -1 && editorStoredValue == -1)
				{
					String[] s = (String[]) this.world.getBody(rodAttach1).getUserData();
					s[1] = ""+s2wX(curX)*40;
					s[2] = ""+s2wY(curY)*40;
					this.world.getBody(rodAttach1).setUserData(s);
					for(double[] v2 : movingRodEnds)
					{
						Body b = addR((int)v2[2],(int)v2[3],0,0,rodAttach1,0,0);
						this.world.addBody(b);
					}
				}
				saveCurrentDesign();
				this.world.removeAllBodiesAndJoints();
				resetArrays();
				initializeWorld(levelData);
				movingRodEnds.clear();
				assignBodyLocation = false;
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
	public void pause() //pause/unpause, save on unpause, reset world to t=0 on pause
	{
		paused = !paused;
		if(paused)
		{
			resetWorld = true;
		}
		else
		{
			saveCurrentDesign = true;
		}
		time = System.nanoTime();
		this.last = time;
	}
	public void resetArrays() //clear all (most?) arraylists
	{
		staticRects = new ArrayList<StaticRect>();
		staticCircles = new ArrayList<StaticCircle>();
		dynRects = new ArrayList<DynRect>();
		dynCircles = new ArrayList<DynCircle>();
		cloudRects = new ArrayList<CloudRect>();
		buildAreas = new ArrayList<BuildArea>();
		goalAreas = new ArrayList<GoalArea>();
		wheels = new ArrayList<Wheel>();
		goalRects = new ArrayList<GoalRect>();
		goalCircles = new ArrayList<GoalCircle>();
		rods = new ArrayList<Rod>();
		jointLocations = new ArrayList<Vector3>();
		
	}
	public void renderActiveObject(Graphics2D g) //render the object(s) being moved, without creating bodies for them (yet)
	{
		if(paused)
		{
			if(editorMode == -1)
			{
				for(double[] v : movingRodEnds)
				{
					double x1 = v[0];
					double y1 = v[1];
					double height = Math.hypot(x1-ex2, y1-ey2);
					double mx = (x1+ex2)/2;
					double my = (y1+ey2)/2;
					double a = Math.atan2(ey2-y1, ex2-x1);
					Rod r = new Rod((int)v[2],new Rectangle(sc(height), 0.1),mx, my,a,this.world.getBodyCount());
					r.render(g, SCALE);
					r.render2(g, SCALE);
				}
			}
			/*
			else if(editorMode == 0)
			{
				Wheel w = new Wheel(new Circle(0.4), ex2, ey2, 0,-1, -1);
				w.render(g, SCALE);
				w.render2(g, SCALE);
			}
			else if(editorMode == 1)
			{
				Wheel w = new Wheel(new Circle(0.4), ex2, ey2, 0,0, -1);
				w.render(g, SCALE);
				w.render2(g, SCALE);
			}
			else if(editorMode == 2)
			{
				Wheel w = new Wheel(new Circle(0.4), ex2, ey2, 0,1, -1);
				w.render(g, SCALE);
				w.render2(g, SCALE);
			}
			*/
			else if(editorMode == 3)
			{
				double height = Math.hypot(ex1-ex2, ey1-ey2);
				double mx = (ex1+ex2)/2;
				double my = (ey1+ey2)/2;
				double a = Math.atan2(ey2-ey1, ex2-ex1);
				Rod r = new Rod(0,new Rectangle(sc(height), 0.1),mx, my,a,this.world.getBodyCount());
				r.render(g, SCALE);
				r.render2(g, SCALE);
			}
			else if(editorMode == 4)
			{
				double height = Math.hypot(ex1-ex2, ey1-ey2);
				double mx = (ex1+ex2)/2;
				double my = (ey1+ey2)/2;
				double a = Math.atan2(ey2-ey1, ex2-ex1);
				Rod r = new Rod(1,new Rectangle(sc(height), 0.1),mx, my,a,this.world.getBodyCount());
				r.render(g, SCALE);
				r.render2(g, SCALE);
			}
			else if(editorMode == 5)
			{
				double height = Math.hypot(ex1-ex2, ey1-ey2);
				double mx = (ex1+ex2)/2;
				double my = (ey1+ey2)/2;
				double a = Math.atan2(ey2-ey1, ex2-ex1);
				Rod r = new Rod(2,new Rectangle(sc(height), 0.1),mx, my,a,this.world.getBodyCount());
				r.render(g, SCALE);
				r.render2(g, SCALE);
			}
			else if(editorMode == 6)
			{
				double height = Math.hypot(ex1-ex2, ey1-ey2);
				double mx = (ex1+ex2)/2;
				double my = (ey1+ey2)/2;
				double a = Math.atan2(ey2-ey1, ex2-ex1);
				Rod r = new Rod(3,new Rectangle(sc(height), 0.1),mx, my,a,this.world.getBodyCount());
				r.render(g, SCALE);
				r.render2(g, SCALE);
			}
		}
	}
	public void deleteObject(Body b)
	{
		int index = this.world.getBodies().indexOf(b);
		rodIndices.add(index);
		String s = ((String[])b.getUserData())[0];
		if(s.equals("R"))
		{
			List<Body> l = b.getJoinedBodies();
			for(Body j : l)
			{
				if(((String[])j.getUserData())[0] == "J" && j.getJoinedBodies().size() == 1)
				{
					this.world.removeBody(j);
				}
			}
		}
		removeObjects(s);
		rodIndices.clear();
		
	}
	public void addObject()
	{
		Body go;
		if(editorMode == 0)
		{
			this.world.addBody(new Wheel(ex2,ey2,0.5,0,-100));
		}
		else if(editorMode == 1)
		{
			this.world.addBody(new Wheel(ex2,ey2,0.5,0,0));
		}
		else if(editorMode == 2)
		{
			this.world.addBody(new Wheel(ex2,ey2,0.5,0,100));
		}
		else
		{
			Body j1,j2;
			if(rodAttach1 != -1)
			{
				j1 = this.world.getBody(rodAttach1);
				
			}
			else
			{
				rodAttach1 = this.world.getBodyCount();
				go = addJ(ex1,ey1);
				this.world.addBody(go);
				j1 = this.world.getBody(rodAttach1);
			}
			if(rodAttach2 != -1)
			{
				j2 = this.world.getBody(rodAttach2);
			}
			else
			{
				rodAttach2 = this.world.getBodyCount();
				go = addJ(ex2,ey2);
				this.world.addBody(go);
				j2 = this.world.getBody(rodAttach2);
				
			}
			
			go = addR(editorMode-3,rodAttach1,ex1-this.world.getBody(rodAttach1).getWorldCenter().x,ey1-this.world.getBody(rodAttach1).getWorldCenter().y,rodAttach2,ex2-this.world.getBody(rodAttach2).getWorldCenter().x,ey2-this.world.getBody(rodAttach2).getWorldCenter().y);
			this.world.addBody(go);
			RevoluteJoint rj1 = new RevoluteJoint(go, j1, new Vector2(ex1,ey1));
			this.world.addJoint(rj1);
			RevoluteJoint rj2 = new RevoluteJoint(go, j2, new Vector2(ex2,ey2));
			this.world.addJoint(rj2);
		}
	}
	public void moveObject()
	{
		Body b = this.world.getBody(rodAttach1);
		List<Body> joined = b.getJoinedBodies();
		if(checkBodyLocation)
		{
			offsetVector = new Vector2(ex1-b.getWorldCenter().x,ey1-b.getWorldCenter().y);
		}
		Vector2 current = b.getWorldCenter().copy();
		current.add(offsetVector);
		Vector2 end = new Vector2(s2wX(curX),s2wY(curY));
		b.translate(end.difference(current));
		if(checkBodyLocation)
		{
			String[] s;
			List<Body> bodyList;
			Body body;
			for(int i = 0; i < this.world.getBodyCount(); i++)
			{
				body = this.world.getBody(i);
				if(joined.contains(body))
				{
					rodIndices.add(i);
					bodyList = body.getJoinedBodies();
					for(Body b2 : bodyList)
					{
						s = (String[]) b2.getUserData();
						if(b2 != b)
						{
							s = (String[]) body.getUserData();
							double[] d = {b2.getWorldCenter().x,b2.getWorldCenter().y,Integer.valueOf(s[1]),this.world.getBodies().indexOf(b2)};
							movingRodEnds.add(d);
							break;
						}
					}
				}	
			}
			Collections.sort(rodIndices,Collections.reverseOrder());
			removeObjects("R");
			checkBodyLocation = false;
		}
		moveObject = false;
	}
	public Body getBodyAtLocation(double x, double y)
	{
		for(Body b: this.world.getBodies())
		{
			if(((String[])b.getUserData())[0] != "J")
			{
				if(b.contains(new Vector2(x,y)))
				{
					return b;
				}
			}
		}
		return null;
	}
	public void removeObjects(String s)
	{
		for(int i = 0; i < buildAreas.size(); i++)
		{
			buildAreas.get(i).adjustIndex(rodIndices);
		}
		for(int i = 0; i < goalAreas.size(); i++)
		{
			goalAreas.get(i).adjustIndex(rodIndices);
		}
		for(int i = 0; i < staticRects.size(); i++)
		{
			staticRects.get(i).adjustIndex(rodIndices);
		}
		for(int i = 0; i < staticCircles.size(); i++)
		{
			staticCircles.get(i).adjustIndex(rodIndices);
		}
		for(int i = 0; i < dynRects.size(); i++)
		{
			dynRects.get(i).adjustIndex(rodIndices);
		}
		for(int i = 0; i < cloudRects.size(); i++)
		{
			cloudRects.get(i).adjustIndex(rodIndices);
		}
		for(int i = 0; i < dynCircles.size(); i++)
		{
			dynCircles.get(i).adjustIndex(rodIndices);
		}
		for(int i = 0; i < goalRects.size(); i++)
		{
			goalRects.get(i).adjustIndex(rodIndices);
		}
		if(s.equals("GC"))
		{
			for(int j = 0; j < goalCircles.size(); j++)
			{
				if(this.world.getBodies().indexOf(goalCircles.get(j)) == rodIndices.get(0))
				{
					goalCircles.remove(j);
					break;
				}
			}
		}
		for(int i = 0; i < goalCircles.size(); i++)
		{
			goalCircles.get(i).adjustIndex(rodIndices);
		}
		for(int i = 0; i < wheels.size(); i++)
		{
			wheels.get(i).adjustIndex(rodIndices);
		}
		if(s.equals("R"))
		{
			for(int i:rodIndices)
			{
				for(int j = 0; j < rods.size(); j++)
				{
					if(i(rods.get(j)) == i)
					{
						rods.remove(j);
						break;
					}
				}
				for(double[] d : movingRodEnds)
				{
					if(i < d[3])
					{
						d[3]--;
					}
				}
			}
		}
		for(int i = 0; i < rods.size(); i++)
		{
			adjustJointIndices(this.world.getBody(rods.get(i).getI()));
			rods.get(i).adjustIndex(rodIndices);
		}
		for(int i : rodIndices)
		{
			this.world.removeBody(i);
		}
	}
	private void adjustJointIndices(Body body) {
		String[] s = (String[]) body.getUserData();
		int j1 = Integer.valueOf(s[2]);
		int j2 = Integer.valueOf(s[5]);
		for(int i : rodIndices)
		{
			if(j1 > i)
			{
				j1--;
			}
			if(j2 > i)
			{
				j2--;
			}
		}
		s[2] = ""+j1;
		s[5] = ""+j2;
		body.setUserData(s);
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
		renderActiveObject(g);
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
	public Body addR(int type, int b1, double osx1, double osy1, int b2, double osx2, double osy2, double torque1, double torque2)
	{
		Body j1 = this.world.getBody(b1);
		Body j2 = this.world.getBody(b2);
		Body go;
		Vector2 point1 = j1.getWorldCenter();
		point1.add(osx1, osy1);
		Vector2 point2 = j2.getWorldCenter();
		point2.add(osx2, osy2);
		double height = Math.abs(point1.distance(point2));
		Vector2 midpoint = point1.copy();
		midpoint.add(point2);
		midpoint.setMagnitude(midpoint.getMagnitude()/2);
		double a = (point2.copy().difference(point1)).getDirection();
		if(type == 0)
		{
			Rectangle floorRect = new Rectangle(height, 0.1);
			rods.add(new Rod(0,new Rectangle(height-0.15, 0.1),midpoint.x, midpoint.y,a,this.world.getBodyCount()));
    		go = new Body();
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf3);
    		go.addFixture(b);
		}
		else if(type == 1)
		{
			Rectangle floorRect = new Rectangle(height, 0.2);
			rods.add(new Rod(1,new Rectangle(height-0.15, 0.1),midpoint.x, midpoint.y,a,this.world.getBodyCount()));
    		go = new Body();
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf2);
    		go.addFixture(b);
		}
		else if(type == 2)
		{
			Rectangle floorRect = new Rectangle(height, 0.2);
			rods.add(new Rod(2,new Rectangle(height-0.15, 0.1),midpoint.x, midpoint.y,a,this.world.getBodyCount()));
    		go = new Body();
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf2);
    		b.setDensity(20);
    		go.addFixture(b);
		}
		else
		{
			Rectangle floorRect = new Rectangle(height, 0.1);
			rods.add(new Rod(3,new Rectangle(height-0.15, 0.1),midpoint.x, midpoint.y,a,this.world.getBodyCount()));
    		go = new Body();
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf4);
    		go.addFixture(b);
		}
		go.setMass(MassType.NORMAL);
		// move the floor down a bit
		go.translate(midpoint);
		go.rotateAboutCenter((point2.copy().difference(point1)).getDirection());
		
		RevoluteJoint rj1 = new RevoluteJoint(go, j1, point1);
			if(torque1 != 0)
			{
				double d = torque1;
				rj1.setMotorSpeed(2*Math.PI*d/Math.abs(d));
	    		rj1.setMaximumMotorTorque(d);
	    		rj1.setMotorEnabled(true);
			}
		// to use a motor
		rj1.setMotorEnabled(false);
		this.world.addJoint(rj1);
		RevoluteJoint rj2 = new RevoluteJoint(go, j2, point2);
		// to use a motor
			if(torque2 != 0)
			{
				double d = torque2;
				rj2.setMotorSpeed(2*Math.PI*d/Math.abs(d));
	    		rj2.setMaximumMotorTorque(Math.abs(d));
	    		rj2.setMotorEnabled(true);
			}
		this.world.addJoint(rj2);
		String[] s = {"R",""+type,""+b1,""+osx1,""+osy1,""+b2,""+osx2,""+osy2,""+torque1,""+torque2};
		go.setUserData(s);
		return go;
	}
	public Body addR(int type, int b1, double osx1, double osy1, int b2, double osx2, double osy2)
	{
		Body j1 = this.world.getBody(b1);
		Body j2 = this.world.getBody(b2);
		Body go;
		Vector2 point1 = j1.getWorldCenter();
		point1.add(osx1, osy1);
		Vector2 point2 = j2.getWorldCenter();
		point2.add(osx2, osy2);
		double height = Math.abs(point1.distance(point2));
		Vector2 midpoint = point1.copy();
		midpoint.add(point2);
		midpoint.setMagnitude(midpoint.getMagnitude()/2);
		double a = (point2.copy().difference(point1)).getDirection();
		if(type == 0)
		{
			Rectangle floorRect = new Rectangle(height, 0.1);
			rods.add(new Rod(0,new Rectangle(height, 0.1),midpoint.x, midpoint.y,a,this.world.getBodyCount()));
    		go = new Body();
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf3);
    		go.addFixture(b);
		}
		else if(type == 1)
		{
			Rectangle floorRect = new Rectangle(height, 0.2);
			rods.add(new Rod(1,new Rectangle(height, 0.1),midpoint.x, midpoint.y,a,this.world.getBodyCount()));
    		go = new Body();
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf2);
    		go.addFixture(b);
		}
		else if(type == 2)
		{
			Rectangle floorRect = new Rectangle(height, 0.2);
			rods.add(new Rod(2,new Rectangle(height, 0.1),midpoint.x, midpoint.y,a,this.world.getBodyCount()));
    		go = new Body();
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf2);
    		b.setDensity(20);
    		go.addFixture(b);
		}
		else
		{
			Rectangle floorRect = new Rectangle(height, 0.1);
			rods.add(new Rod(3,new Rectangle(height, 0.1),midpoint.x, midpoint.y,a,this.world.getBodyCount()));
    		go = new Body();
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf4);
    		go.addFixture(b);
		}
		go.setMass(MassType.NORMAL);
		go.setAngularDamping(5);
		// move the floor down a bit
		go.translate(midpoint);
		go.rotateAboutCenter((point2.copy().difference(point1)).getDirection());
		
		RevoluteJoint rj1 = new RevoluteJoint(go, j1, point1);
		this.world.addJoint(rj1);
		RevoluteJoint rj2 = new RevoluteJoint(go, j2, point2);
		this.world.addJoint(rj2);
		String[] s = {"R",""+type,""+b1,""+osx1,""+osy1,""+b2,""+osx2,""+osy2};
		go.setUserData(s);
		return go;
	}
	public Body addJ(double x, double y)
	{
		Circle cirShape = new Circle(0.1);
		Body go = new Body();
		BodyFixture b = new BodyFixture(cirShape);
		b.setFilter(cf4);
		go.addFixture(b);
		go.setMass(MassType.NORMAL);
		go.translate(x, y);
		jointLocations.add(new Vector3(x,y,this.world.getBodyCount()));
		String[] s = {"J",""+(x*40),""+(y*40)};
		go.setUserData(s);
		return go;
	}
	public void saveCurrentDesign()
	{
		String s = "";
		String[] data;
		for(int i = 0; i < this.world.getBodyCount(); i++)
		{
			if(i != 0)
			{
				s += ";";
			}
			data = (String[])this.world.getBody(i).getUserData();
			s += data[0];
			for(int j = 1; j < data.length; j++)
			{
				s += ","+data[j];
			}
		}
		levelData = s;
		System.out.println(s);
	}
}