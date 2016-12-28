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
	public final double SNAP_TO_DISTANCE = 0.4;
	public String levelData;
	public Vector2 p1, p2, p3, offsetVector;	
	public int rodAttach1, rodAttach2, moveBody;
	public ArrayList<Vector3> jointLocations = new ArrayList<Vector3>();
	public ArrayList<Integer> rodIndices = new ArrayList<Integer>();
	public ArrayList<Integer> nonRods = new ArrayList<Integer>();
	public ArrayList<Integer> rods = new ArrayList<Integer>();
	
	
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
		initializeWorld();
	}
	public void initializeWorld()
	{
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
	    	else if(temp[0].equals("W")){
	    		if(temp.length > 6){addBody(new Wheel(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80,Double.valueOf(temp[4])*d2r, Double.valueOf(temp[5]), getBody(Integer.valueOf(temp[6]))));}
	    		else{addBody(new Wheel(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80,Double.valueOf(temp[4])*d2r, Double.valueOf(temp[5])));}}
	    	else if(temp[0].equals("GR")){addBody(new GoalRect(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40,Double.valueOf(temp[5])*d2r, isJointed(temp[6])));}
	    	else if(temp[0].equals("GC")){addBody(new GoalCircle(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80,Double.valueOf(temp[4])*d2r, isJointed(temp[5])));}
	    	else if(temp[0].equals("R"))
	    	{
	    		Rod rod;
	    		Body b1 = getBody(Integer.valueOf(temp[2]));
	    		Body b2 = getBody(Integer.valueOf(temp[5]));
	    		Vector2 p1 = new Vector2(Double.valueOf(temp[3]), Double.valueOf(temp[4]));
	    		p1.add(b1.getWorldCenter());
	    		Vector2 p2 = new Vector2(Double.valueOf(temp[6]), Double.valueOf(temp[7]));
	    		p2.add(b2.getWorldCenter());
	    		rod = new Rod(Integer.valueOf(temp[1]), Integer.valueOf(temp[2]), Double.valueOf(temp[3]), Double.valueOf(temp[4]), Integer.valueOf(temp[5]), Double.valueOf(temp[6]), Double.valueOf(temp[7]));
	    		rod.adjustShape(getBody(rod.joint1).getWorldCenter(), getBody(rod.joint2).getWorldCenter());
	    		addBody(rod);
    			RevoluteJoint r1 = new RevoluteJoint(b1, rod, p1);
    			r1.setMotorEnabled(false);
    			addJoint(r1);
    			RevoluteJoint r2 = new RevoluteJoint(b2, rod, p2);
    			r2.setMotorEnabled(false);
    			addJoint(r2);
	    	}
	    	else if(temp[0].equals("J")){addBody(new Joint(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40));}
	    }
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
			data = (String[])getBody(i).getUserData();
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
	}
	public void initializeBodyMovement(int index)
	{
		nonRods.clear();
		rods.clear();
		Set<Integer> set1 = new HashSet<Integer>();
		Set<Integer> set2 = new HashSet<Integer>();
		set2.add(index);
		nonRods.add(index);
		while(set2.size() > 0)
		{
			set1 = set2;
			set2.clear();
			for(int i: set1)
			{
				List<Body> l = getBody(i).getJoinedBodies();
				for(Body b: l)
				{
					int ib = index(b);
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
			Vector3 v = getNearbyJoint(p1);
			if(v != null)
			{
				p1.x = v.x;
				p1.y = v.y;
			}
			addBody(new Wheel(p1.x,p1.y,0.5,0,(gameMode*100-100)));
		}
		else
		{
			Vector3 v = getNearbyJoint(p1);
			moveBody = getBodyCount();
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
				moveBody++;
				addBody(new Joint(p1));
				
			}
			addBody(new Joint(20+p1.x, p1.y));
			Rod rod = new Rod(gameMode-3, rodAttach1, p1.subtract(getBody(rodAttach1).getWorldCenter()), rodAttach2, new Vector2(0, 0));
			addBody(rod);
			RevoluteJoint r1 = new RevoluteJoint(getBody(rodAttach1), rod, rod.p2);
			r1.setMotorEnabled(false);
			addJoint(r1);
			RevoluteJoint r2 = new RevoluteJoint(getBody(rodAttach2), rod, rod.p2);
			r2.setMotorEnabled(false);
			addJoint(r2);
			initializeJointMovement(moveBody);
		}
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
		for(int i = 0; i < jointLocations.size(); i++)//snap-to, once again
		{
			Vector3 v = jointLocations.get(i).copy(); //copy, cuz by reference. Same with previous ones
			if(Math.hypot(v.x-p2.x, v.y-p2.y) < SNAP_TO_DISTANCE && (int)v.z != rodAttach1)
			{
				p2 = new Vector2(v.x, v.y);
				break;
			}
		}
		if(gameMode == -1 && rodAttach1 != -1)
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
		Vector2 delta = p3.copy();
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
			if(gameMode > 2)
			{
				
				Vector3 v = getNearbyJoint(p3, j1);
				if(v != null)
				{
					removeBody(j2);
					rod.joint2 = (int)v.z;
				}
			}
		}
		saveCurrentDesign();
		initializeWorld();
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
}
