package fcgold;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.Vector3;

public class Joint extends GamePiece{
	public Joint(double x, double y)
	{
		Circle cirShape = new Circle(0.1);
		BodyFixture b = new BodyFixture(cirShape);
		b.setFilter(new GameFilter(8,0));
		addFixture(b);
		setMass(MassType.NORMAL);
		this.translate(x, y);
		String[] s = {"J",""+(x*40),""+(y*40)};
		setUserData(s);
		
	}
	public Joint(Vector2 p2) {
		Circle cirShape = new Circle(0.1);
		BodyFixture b = new BodyFixture(cirShape);
		b.setFilter(new GameFilter(8,0));
		addFixture(b);
		setMass(MassType.NORMAL);
		this.translate(p2);
		String[] s = {"J",""+(p2.x*40),""+(p2.y*40)};
		setUserData(s);
	}
	public Vector3[] getJointVectors()
	{
		Vector3[] v = new Vector3[1];
		v[0] = new Vector3(getWorldCenter().x, getWorldCenter().y, 0);
		return v;
	}
	public void render(Graphics2D g, double scale) {
		double x = this.getWorldCenter().x;
		double y = this.getWorldCenter().y;
		AffineTransform ot = g.getTransform();
		AffineTransform lt = new AffineTransform();
		lt.scale(scale/40, scale/40);
		lt.translate(x * 40, y * 40);
		// apply the transform
		g.transform(lt);
		g.setColor(Color.BLACK);
		g.drawOval(-5, -5, 10, 10);
		g.setTransform(ot);
		
	}
	public void render2(Graphics2D g, double scale) {
		
	}
	public String[] returnData() {
		return (String[])getUserData();
	}
	public String[] returnUpdatedData() {
		String[] s = (String[])getUserData();
		s[1] = ""+40*this.getTransform().getTranslationX();
		s[2] = ""+40*this.getTransform().getTranslationY();
		setUserData(s);
		return s;
	}
}
