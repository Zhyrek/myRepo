package fcgold;

import java.util.ArrayList;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.Vector3;

public class DesignEditor extends World{
	public final double SNAP_TO_DISTANCE = 0.4;
	public String levelData;
	public Vector2 p1, p2, p3;	
	public ArrayList<Vector3> jointLocations = new ArrayList<Vector3>();
	
	//gameMode: -2 = delete, -1 = move, 0 = CW, 1 = UW, 2 = CCW, 3 = water, 4 = wood, 5 = gold, 6 = ghost
	public int gameMode = -1;
	//gmsv: store design option, send it to gameMode on click (to avoid it changing halfway thru operation
	public int gmsv = -1;
	public int rodAttach1, rodAttach2;
	
	public DesignEditor(String s)
	{
		setGravity(new Vector2(0.0,9.8));//set gravity, 9.8m/s2 down
		Settings settings = new Settings(); //adjust settings here!
		System.out.println(settings.getLinearTolerance());
		settings.setBaumgarte(0.001); //eh
		settings.setLinearTolerance(0.0005); //ehh
		settings.setPositionConstraintSolverIterations(100);
		settings.setVelocityConstraintSolverIterations(100); //ehhhhh
		setSettings(settings); //its kinda like fc
		levelData = s;
		String[] str = s.split(";"); //split design string into string array, by the character ;
		String[] temp;//dummy var, to store string fragments in
		double d2r = Math.PI/180; //degrees to radians conversion
		Body go; //dummy body
	    for(int i = 0; i < str.length; i++) //add objects for each string fragment
	    {
	    	temp = str[i].split(",");
	    	if(temp[0].equals("SR"))
	    	{
	    		addBody(new StaticRect(Double.valueOf(temp[1])/40,Double.valueOf(temp[1])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40,Double.valueOf(temp[5])*d2r));
	    	}
	    	else if(temp[0].equals("SC"))
	    	{
	    		addBody(new StaticCircle(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80));
	    	}
	    	
	    	else if(temp[0].equals("DR"))
	    	{
	    		addBody(new DynRect(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40,Double.valueOf(temp[5])*d2r, isJointed(temp[6])));
		    }
	    	else if(temp[0].equals("DC"))
	    	{
	    		addBody(new DynCircle(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80,Double.valueOf(temp[4])*d2r, isJointed(temp[5])));
		    }
	    	else if(temp[0].equals("CR"))
	    	{
	    		addBody(new CloudRect(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40,Double.valueOf(temp[5])*d2r, isJointed(temp[6])));
			}
	    	else if(temp[0].equals("BA"))
	    	{
	    		addBody(new BuildArea(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40));
			}
	    	else if(temp[0].equals("GA"))
	    	{
	    		addBody(new GoalArea(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40));
			}
	    	else if(temp[0].equals("W"))
	    	{
	    		if(temp.length > 6)
	    		{
	    			addBody(new Wheel(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80,Double.valueOf(temp[4])*d2r, Double.valueOf(temp[5]), getBody(Integer.valueOf(temp[6]))));
	    		}
	    		else
	    		{
	    			addBody(new Wheel(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80,Double.valueOf(temp[4])*d2r, Double.valueOf(temp[5])));
	    		}
	    	}
	    	else if(temp[0].equals("GR"))
	    	{
	    		addBody(new GoalRect(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/40,Double.valueOf(temp[4])/40,Double.valueOf(temp[5])*d2r, isJointed(temp[6])));
			}
	    	else if(temp[0].equals("GC"))
	    	{
	    		addBody(new GoalCircle(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40,Double.valueOf(temp[3])/80,Double.valueOf(temp[4])*d2r, isJointed(temp[5])));
			}
	    	else if(temp[0].equals("R"))
	    	{
	    		Body b1 = getBody(Integer.valueOf(temp[2]));
	    		Body b2 = getBody(Integer.valueOf(temp[5]));
	    		Vector2 p1 = new Vector2(Double.valueOf(temp[3]), Double.valueOf(temp[4]));
	    		p1.add(b1.getWorldCenter());
	    		Vector2 p2 = new Vector2(Double.valueOf(temp[6]), Double.valueOf(temp[7]));
	    		p2.add(b2.getWorldCenter());
	    		go = new Rod(Integer.valueOf(temp[1]), p1, p2);
	    		String[] data = {"R",""+Integer.valueOf(temp[1]),""+Integer.valueOf(temp[2]),""+Double.valueOf(temp[3]),""+Double.valueOf(temp[4]),""+Integer.valueOf(temp[5]),""+Double.valueOf(temp[6]),""+Double.valueOf(temp[7])};
	    		go.setUserData(data);
    			addBody(go);
    			RevoluteJoint r1 = new RevoluteJoint(b1, go, p1);
    			r1.setMotorEnabled(false);
    			addJoint(r1);
    			RevoluteJoint r2 = new RevoluteJoint(b2, go, p2);
    			r2.setMotorEnabled(false);
    			addJoint(r2);
	    	}
	    	else if(temp[0].equals("J"))
	    	{
	    		go = new Joint(Double.valueOf(temp[1])/40,Double.valueOf(temp[2])/40);
	    		addBody(go);
			}
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
	public void click(Vector2 point1)
	{
		p1 = point1;
		gameMode = gmsv; //set cursor selection equal to backup cursor selection
		for(int i = 0; i < jointLocations.size(); i++) //check if the mousePress is close to a joint
		{
			Vector3 v = jointLocations.get(i).copy(); 
			if(gameMode == -2)
			{
				Body b = getBodyAtLocation(p1.x, p1.y);
				if(b != null)
				{
					deleteObject = b;
				}
			}
			else if(Math.hypot(v.x-p1.x, v.y-p1.y) < SNAP_TO_DISTANCE) //if within snap_to_distance away from joint
			{
				p1 = new Vector2(v.x, v.y);//
				rodAttach1 = (int)v.z; //set rod attachment 1 index equal to stored joint index
				if(gameMode == -1) //if "move" cursor selection
				{
					moveObject = true;//move the object, within the run thread
					checkBodyLocation = true;//runs once per mousePress in run thread, initialize movement
				}
				break; //break if you find a nearby joint
			}
		}
	}
	public void drag(Vector2 point2)
	{
		p2 = point2;
	}
	public void release(Vector2 point3)
	{
		p3 = point3;
		if(paused) 
        {
			rodAttach2 = -1; //initialize rod attachment index 2
			for(int i = 0; i < jointLocations.size(); i++) //check if release location is near a joint
			{
				Vector3 v = jointLocations.get(i).copy();
				if(Math.hypot(v.x-p3.x, v.y-p3.y) < SNAP_TO_DISTANCE && (int)v.z != rodAttach1) //if near a joint that isn't the first joint, if applicable
				{
					p3 = new Vector2(v.x,v.y);
					rodAttach2 = (int)v.z;
					break;
				}
			}
			if(gameMode > -1)
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
			gameMode = -1; //reset cursor selection (ends drawing if you unpause in the middle of creating object)
		}
		moveObject = false; //stop moving the selected object, if applicable
		rodIndices.clear();
	}
	public Body getBodyAtLocation(double x, double y)
	{
		for(Body b: getBodies())
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
}
