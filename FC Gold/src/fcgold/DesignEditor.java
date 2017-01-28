package fcgold;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.Vector3;

public class DesignEditor extends World{
	public final double SNAP_TO_DISTANCE = 0.3;
	public String levelData;
	public Vector2 p1, p2, p3, offsetVector;	
	public int rodAttach1, rodAttach2, moveBody;
	public ArrayList<Vector3> jointLocations = new ArrayList<Vector3>();
	public ArrayList<Integer> rodIndices = new ArrayList<Integer>();
	public ArrayList<Integer> nonRods = new ArrayList<Integer>();
	public ArrayList<Integer> rods = new ArrayList<Integer>();
	public ArrayList<Body> buildSegments = new ArrayList<Body>();
	public ArrayList<Body> goalSegments = new ArrayList<Body>();
	
	//gameMode: -2 = delete, -1 = move, 0 = CW, 1 = UW, 2 = CCW, 3 = water, 4 = wood, 5 = gold, 6 = ghost
	public int gameMode = -1;
	//gmsv: store design option, send it to gameMode on click (to avoid it changing halfway thru operation
	public int gmsv = -1;
	
	
	public DesignEditor(String s)
	{
		levelData = s;
		setGravity(new Vector2(0.0,9.8));//set gravity, 9.8m/s2 down
		Settings settings = new Settings(); //adjust settings here!
		System.out.println(settings.getLinearTolerance());
		settings.setBaumgarte(0.001); //eh
		settings.setLinearTolerance(0.0005); //ehh
		settings.setPositionConstraintSolverIterations(100);
		settings.setVelocityConstraintSolverIterations(100); //ehhhhh
		setSettings(settings); //its kinda like fc
		preinitialize();
		initializeWorld();
	}
	public void preinitialize()
	{
		String[] str = levelData.split(";"); //split design string into string array, by the character ;
		String[] temp;//dummy var, to store string fragments in
		double d2r = Math.PI/180; //degrees to radians conversion
		ArrayList<BuildArea> buildAreas = new ArrayList<BuildArea>();
		ArrayList<BuildArea> goalAreas = new ArrayList<BuildArea>();
	    for(int i = 0; i < str.length; i++) //add objects for each string fragment
	    {
	    	temp = str[i].split(",");
	    	if(temp[0].equals("BA")){addBody(new BuildArea(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40));}
	    	else if(temp[0].equals("GA")){addBody(new GoalArea(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40));}
	    }
	}
	public void initializeWorld()
	{
		this.removeAllBodiesAndJoints();
		rods.clear();
		nonRods.clear();
		jointLocations.clear();
		String[] str = levelData.split(";"); //split design string into string array, by the character ;
		String[] temp;//dummy var, to store string fragments in
		double d2r = Math.PI/180; //degrees to radians conversion
	    for(int i = 0; i < str.length; i++) //add objects for each string fragment
	    {
	    	temp = str[i].split(",");
	    	if(temp[0].equals("SR")){addBody(new StaticRect(Double.valueOf(temp[1])/40,Double.valueOf(temp[1])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40,Double.valueOf(temp[5])*d2r));}
	    	else if(temp[0].equals("SC")){addBody(new StaticCircle(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80));}
	    	else if(temp[0].equals("DR")){addBody(new DynRect(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40,Double.valueOf(temp[5])*d2r, isJointed(temp[6])));}
	    	else if(temp[0].equals("DC")){addBody(new DynCircle(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80,Double.valueOf(temp[4])*d2r, isJointed(temp[5])));}
	    	else if(temp[0].equals("CR")){addBody(new CloudRect(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40,Double.valueOf(temp[5])*d2r, isJointed(temp[6])));}
	    	else if(temp[0].equals("BA")){addBody(new BuildArea(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40));}
	    	else if(temp[0].equals("GA")){addBody(new GoalArea(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40));}
	    	else if(temp[0].equals("W"))
	    	{
	    		Wheel w;
	    		if(temp.length > 6)
	    		{w = new Wheel(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80,Double.valueOf(temp[4])*d2r, Double.valueOf(temp[5]), getBody(Integer.valueOf(temp[6])));
	    		}
	    		else
	    		{w = new Wheel(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80,Double.valueOf(temp[4])*d2r, Double.valueOf(temp[5]));
	    		}
	    		addBody(w);
	    		nonRods.add(index(w));
	    	}
	    	else if(temp[0].equals("GR")){addBody(new GoalRect(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40,Double.valueOf(temp[5])*d2r, isJointed(temp[6])));}
	    	else if(temp[0].equals("GC")){addBody(new GoalCircle(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80,Double.valueOf(temp[4])*d2r, isJointed(temp[5])));}
	    	else if(temp[0].equals("J")){addBody(new Joint(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40));}
	    	else if(temp[0].equals("R"))
	    	{
	    		Rod rod;
	    		rod = new Rod(Integer.valueOf(temp[1]), Integer.valueOf(temp[2]), Double.valueOf(temp[3]), Double.valueOf(temp[4]), Integer.valueOf(temp[5]), Double.valueOf(temp[6]), Double.valueOf(temp[7]));
	    		addBody(rod);
	    		rods.add(index(rod));
	    	}
	    }
	    for(int k:rods)
    	{
    		Rod rod = (Rod)getBody(k);
    		Body b1 = getBody(rod.joint1);
    		Body b2 = getBody(rod.joint2);
    		Vector2 p1 = rod.offset1.copy();
    		p1.add(b1.getWorldCenter());
    		Vector2 p2 = rod.offset2.copy();
    		p2.add(b2.getWorldCenter());
    		rod.adjustShape(b1.getWorldCenter(), b2.getWorldCenter());
			RevoluteJoint r1 = new RevoluteJoint(b1, rod, p1);
			r1.setMotorEnabled(false);
			addJoint(r1);
			RevoluteJoint r2 = new RevoluteJoint(b2, rod, p2);
			r2.setMotorEnabled(false);
			addJoint(r2);
    	}
	    for(int k:nonRods)
	    {
	    	Wheel w = (Wheel)getBody(k);
	    	int pieces = 0;
	    	for(org.dyn4j.dynamics.joint.Joint j:w.getJoints())
	    	{
	    		if(j.getAnchor1().equals(w.getWorldCenter()))
	    		{
	    			pieces++;
	    		}
	    	}
	    	System.out.println(pieces);
	    	for(org.dyn4j.dynamics.joint.Joint j:w.getJoints())
	    	{
	    		if(j.getAnchor1().equals(w.getWorldCenter()))
	    		{
	    			((RevoluteJoint)j).setMotorEnabled(true);
	    			((RevoluteJoint)j).setMaximumMotorTorque(Math.abs(w.torque)/pieces);
	    			((RevoluteJoint)j).setMotorSpeed(5*Math.signum(w.torque));
	    		}
	    	}
	    }
    	rods.clear();
	    initializeJointLocations();
	}
	public void initializeJointLocations()
	{
		for(Body b: getBodies())
		{
			int index = getBodies().indexOf(b);
			Vector3[] v = ((GamePiece)b).getJointVectors();
			
			for(int i = 0; i < v.length; i++)
			{
				System.out.println(v[i].x+", "+v[i].y);
				
				v[i].z = index;
				jointLocations.add(v[i]);
			}
		}
	}
	public boolean isJointed(String s)
	{
		if( s.equals("1"))
		{
			return true;
		}
		return false;
	}
	public void saveCurrentDesign()
	{
		String s = "";
		String[] data;
		for(int i = 0; i < getBodyCount(); i++)
		{
			if(i != 0)
			{
				s += ";";
			}
			data = ((GamePiece)getBody(i)).returnUpdatedData(); 
			s += data[0];
			for(int j = 1; j < data.length; j++)
			{
				s += ","+data[j];
			}
		}
		levelData = s;
		System.out.println(s);
	}
	public void deleteObject(Body b)
	{
		int index = getBodies().indexOf(b);
		rodIndices.add(index);
		String s = ((String[])b.getUserData())[0];
		if(s.equals("R"))
		{
			List<Body> l = b.getJoinedBodies();
			for(Body j : l)
			{
				if(((String[])j.getUserData())[0] == "J" && j.getJoinedBodies().size() == 1)
				{
					
					rodIndices.add(index(j));
					removeBody(j);
				}
			}
		}
		else if(s.equals("W"))
		{
			for(Vector3 v:((GamePiece) b).getJointVectors())
			{
				int ji = getBodyCount();
				System.out.println(v.x+", "+v.y);
				Joint joint = new Joint(v.x, v.y);
				addBody(joint);
				for(Body j : b.getJoinedBodies())
				{
					if(((String[])j.getUserData())[0] == "R")
					{
						System.out.println(((Rod)j).joint1+", "+((Rod)j).joint1+", "+(int)v.z);
						if(((Rod)j).joint1 == index(b))
						{
							System.out.println(((Rod)j).p1+", "+joint.getWorldCenter());
							if(((Rod)j).p1.equals(joint.getWorldCenter()))
							{
								((Rod)j).joint1 = ji;
								((Rod)j).offset1 = new Vector2(0, 0);
								RevoluteJoint rj = new RevoluteJoint(joint, j, joint.getWorldCenter());
								addJoint(rj);
							}
						}
						else
						{
							System.out.println(((Rod)j).p2+", "+joint.getWorldCenter());
							if(((Rod)j).p2.equals(joint.getWorldCenter()))
							{
								((Rod)j).joint2 = ji;
								((Rod)j).offset2 = new Vector2(0, 0);
								RevoluteJoint rj = new RevoluteJoint(joint, j, joint.getWorldCenter());
								addJoint(rj);
							}
							
						}
					}
				}
				if(joint.getJoinedBodies().size() == 0)
				{
					removeBody(joint);
				}
			}
			
		}
		removeBody(b);
		for(Body b1: getBodies())
		{
			((GamePiece)b).adjustIndex(rodIndices);
			if(b1 instanceof Rod)
			{
				String[] str = (String[]) b1.getUserData();
				
				for(int i : rodIndices)
				{
					if(((Rod)b1).joint1 > i)
					{
						((Rod)b1).joint1--;
					}
					if(((Rod)b1).joint2 > i)
					{
						((Rod)b1).joint2--;
					}
				}
				str[2] = ""+((Rod)b1).joint1;
				str[5] = ""+((Rod)b1).joint2;
				b1.setUserData(str);
			}
		}
		rodIndices.clear();
		jointLocations.clear();
		initializeJointLocations();
	}
	public void initializeBodyMovement(int index)
	{
		System.out.println("hello!");
		nonRods.clear();
		rods.clear();
		Set<Integer> set1 = new HashSet<Integer>();
		Set<Integer> set2 = new HashSet<Integer>();
		set2.add(index);
		nonRods.add(index);
		rodAttach1 = index;
		while(set2.size() > 0)
		{
			System.out.println(set2);
			set1.clear();
			set1.addAll(set2);
			set2.clear();
			System.out.println(set1);
			for(int i: set1)
			{
				for(Body b: getBody(i).getJoinedBodies())
				{
					int ib = index(b);
					System.out.println(ib);
					if(!nonRods.contains(ib))
					{
						nonRods.add(ib);
						set2.add(ib);
					}
				}
			}
		}
		for(int i = 0; i < nonRods.size(); i++)
		{
			if(getBody(nonRods.get(i)) instanceof Rod)
			{
				rods.add(nonRods.get(i));
				nonRods.remove(i);
				i--;
			}
		}
	}
	public void initializeJointMovement(int index) 
	{
		nonRods.clear();
		rods.clear();
		nonRods.add(index);
		offsetVector = new Vector2(p1.x-getBody(index).getWorldCenter().x,p1.y-getBody(index).getWorldCenter().y);
		for(Body b: getBody(index).getJoinedBodies())
		{
			if(b instanceof Rod)
			{
				rods.add(index(b));
			}
			else
			{
				nonRods.add(index(b));
			}
		}
	}
	public void click(Vector2 point1)
	{
		rodAttach1 = -1; //initialize rod attachment 1 index for an action
	    rodAttach2 = -1; //initialize rod attachment 2 index for an action
	    offsetVector = new Vector2(0, 0);
		p1 = point1; //p1 is the click point;
		gameMode = gmsv; //set cursor selection equal to backup cursor selection
		if(gameMode == -2)
		{
			Body b = getBodyAtLocation(p1.x, p1.y);
			if(b != null)
			{
				deleteObject(b);
				//deleteObject = b;
			}
		}
		else if(gameMode == -1)
		{
			Vector3 v = getNearbyJoint(p1);
			if(v == null)
			{
				Body b = getBodyAtLocation(p1.x, p1.y);
				if(b != null)
				{
					initializeBodyMovement(index(b));
					moveBody = index(b);
				}
			}
			else //if within snap_to_distance away from joint
			{
				p1 = new Vector2(v.x, v.y);//
				rodAttach1 = (int)v.z; //set rod attachment 1 index equal to stored joint index
				initializeJointMovement(rodAttach1);
				moveBody = rodAttach1;
			}
		}
		else if(gameMode < 3)
		{
			moveBody = getBodyCount();
			rodAttach1 = moveBody;
			Vector3 v = getNearbyJoint(p1);
			if(v != null)
			{
				p1.x = v.x;
				p1.y = v.y;
			}
			addBody(new Wheel(p1.x,p1.y,0.5,0,(gameMode*100-100)));
			initializeJointMovement(moveBody);
			offsetVector = new Vector2(0, 0);
		}
		else
		{
			Vector3 v = getNearbyJoint(p1);
			if(v != null)
			{
				p1.x = v.x;
				p1.y = v.y;
				rodAttach1 = (int)v.z;
				rodAttach2 = getBodyCount();
			}
			else
			{
				rodAttach1 = getBodyCount();
				rodAttach2 = getBodyCount()+1;
				
				addBody(new Joint(p1));
				
			}
			moveBody = rodAttach2;
			addBody(new Joint(0.5+p1.x, p1.y));
			Rod rod = new Rod(gameMode-3, rodAttach1, p1.subtract(getBody(rodAttach1).getWorldCenter()), rodAttach2, new Vector2(0, 0));
			rod.adjustShape(getBody(rodAttach1).getWorldCenter(), getBody(rodAttach2).getWorldCenter());
			addBody(rod);
			RevoluteJoint r1 = new RevoluteJoint(getBody(rodAttach1), rod, rod.p1);
			r1.setMotorEnabled(false);
			addJoint(r1);
			RevoluteJoint r2 = new RevoluteJoint(getBody(rodAttach2), rod, rod.p2);
			r2.setMotorEnabled(false);
			addJoint(r2);
			initializeJointMovement(moveBody);
			offsetVector = new Vector2(0, 0);
		}
	}
	public void moveObjects()
	{
		Vector2 delta = p2.copy();
		delta.subtract(getBody(moveBody).getWorldCenter());
		delta.subtract(offsetVector);
		for(int i: nonRods)
		{
			getBody(i).translate(delta);
		}
		for(int i: rods)
		{
			Rod rod = (Rod) getBody(i);
			int j1 = rod.joint1;
			int j2 = rod.joint2;
			rod.adjustShape(getBody(j1).getWorldCenter(), getBody(j2).getWorldCenter());
		}
		
	}
	public void drag(Vector2 point2)
	{
		p2 = point2;
		Vector3 v = getNearbyJoint(p2, rodAttach1);
		if(v != null)
		{
			p2 = new Vector2(v.x, v.y);
		}
		if(rodAttach1 != -1)
		{
			moveObjects();
			//moveObject = true; //let selected object, when in move mode, be moved smoothly
		}
	}
	public void release(Vector2 point3)
	{
		p3 = point3;
		endMovement();
		//moveObject = false; //stop moving the selected object, if applicable
		rodIndices.clear();
	}
	public void endMovement()
	{
		if(gameMode > -2)
		{
			for(int i: nonRods)
			{
				if(gameMode > -1 && gameMode < 3)
				{
					Vector3 v = getNearbyJoint(p3);
					if(v != null)
					{
						if(getBody((int)v.z) instanceof Joint)
						{
							Joint j = (Joint)getBody((int)v.z);
							p3 = new Vector2(v.x,v.y);
							for(Body b: j.getJoinedBodies())
							{
								if(((Rod)b).joint1 == index(j))
								{
									((Rod)b).joint1 = moveBody;
								}
								else
								{
									((Rod)b).joint2 = moveBody;
								}
								RevoluteJoint rj = new RevoluteJoint(b, getBody(moveBody), p3);
								addJoint(rj);
							}
							deleteObject(j);
						}
					}
				}
				if(getBody(moveBody) instanceof Wheel)
				{
					getBody(moveBody).getFixture(0).setFilter(new GameFilter(2,3,getBody(moveBody).getWorldCenter()));
				}
			}
			for(int i: rods)
			{
				Rod rod = (Rod) getBody(i);
				int j1 = rod.joint1;
				int j2 = rod.joint2;
				rod.adjustShape(getBody(j1).getWorldCenter(), getBody(j2).getWorldCenter());
				if(gameMode > 2)
				{
					Vector3 v = getNearbyJoint(p3, j1);
					if(v != null)
					{
						p3 = new Vector2(v.x, v.y);
						removeBody(j2);
						rod.joint2 = (int)v.z;
						rod.offset2 = p3.copy().subtract(getBody(rod.joint2).getWorldCenter());
						RevoluteJoint rj2 = new RevoluteJoint(getBody(rod.joint2), rod, p3);
						addJoint(rj2);
					}
				}
			}
			saveCurrentDesign();
			initializeWorld();
		}
		
	}
	public Body getBodyAtLocation(double x, double y){
		for(Body b: getBodies()){
			if(((String[])b.getUserData())[0] != "J"){
				if(b.contains(new Vector2(x,y))){
					return b;}}}return null;
	}
	public int index(Body b)
	{
		return getBodies().indexOf(b);
	}
	public Vector3 getNearbyJoint(Vector2 point)
	{
		for(int i = 0; i < jointLocations.size(); i++) //check if the mousePress is close to a joint
		{
			Vector3 v = jointLocations.get(i).copy();
			if(Math.hypot(v.x-point.x, v.y-point.y) < SNAP_TO_DISTANCE) //if within snap_to_distance away from joint
			{
				return v;
			}
		}
		return null;
	}
	public Vector3 getNearbyJoint(Vector2 point, int connectedBody)
	{
		for(int i = 0; i < jointLocations.size(); i++) //check if the mousePress is close to a joint
		{
			Vector3 v = jointLocations.get(i).copy();
			if(Math.hypot(v.x-point.x, v.y-point.y) < SNAP_TO_DISTANCE) //if within snap_to_distance away from joint
			{
				if((int)v.z != connectedBody)
				{
					return v;
				}
			}
		}
		return null;
	}
	public Vector3 getNearbyJoint(Vector2 point, ArrayList<Integer> connectedBody)
	{
		for(int i = 0; i < jointLocations.size(); i++) //check if the mousePress is close to a joint
		{
			Vector3 v = jointLocations.get(i).copy();
			if(Math.hypot(v.x-point.x, v.y-point.y) < SNAP_TO_DISTANCE) //if within snap_to_distance away from joint
			{
				if(!connectedBody.contains((int)v.z))
				{
					return v;
				}
				
			}
		}
		return null;
	}
}
