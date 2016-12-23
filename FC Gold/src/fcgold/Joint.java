package fcgold;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector3;

public class Joint extends GamePiece{
	public Joint(double x, double y)
	{
		Circle cirShape = new Circle(0.1);
		BodyFixture b = new BodyFixture(cirShape);
		b.setFilter(new CategoryFilter(8,0));
		addFixture(b);
		setMass(MassType.NORMAL);
		translate(x, y);
		String[] s = {"J",""+(x*40),""+(y*40)};
		setUserData(s);
		
	}
	public Vector3[] getJointVectors()
	{
		Vector3[] v = new Vector3[1];
		v[0] = new Vector3(getWorldCenter().x, getWorldCenter().y, 0);
		return v;
	}
}
