package fcgold;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Vector2;

public class DesignEditor extends World{
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
	}
	public boolean isJointed(String s)
	{
		if( s.equals("1"))
		{
			return true;
		}
		return false;
	}
}
