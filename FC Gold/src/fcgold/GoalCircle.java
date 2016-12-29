package fcgold;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector3;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;

public class GoalCircle extends GamePiece{
	public boolean joints;
	public Circle r;
	Ellipse2D.Double drawPath;
	Ellipse2D.Double fillPath;
	Ellipse2D.Double[] jointLocations = new Ellipse2D.Double[5];
	public Color cf = new Color(246,93,106);
	public Color cb = new Color(180,98,104);
	public GoalCircle(double x, double y, double radius, double a, boolean b)
	{
		joints = b;
		Circle cirShape = new Circle(radius);
		drawPath = new Ellipse2D.Double(
				(- radius) * 40,
				(- radius) * 40,
				radius * 80,
				radius * 80);
		fillPath = new Ellipse2D.Double(
				(- radius) * 40+4,
				(- radius) * 40+4,
				radius * 80-8,
				radius * 80-8);
		jointLocations[0] = new Ellipse2D.Double(
				(- 0.075) * 40,
				(- 0.075) * 40,
				0.15 * 40,
				0.15 * 40);
		jointLocations[1] = new Ellipse2D.Double(
				(- 0.075-radius) * 40,
				(- 0.075) * 40,
				0.15 * 40,
				0.15 * 40);
		jointLocations[2] = new Ellipse2D.Double(
				(- 0.075) * 40,
				(- 0.075+radius) * 40,
				0.15 * 40,
				0.15 * 40);
		jointLocations[3] = new Ellipse2D.Double(
				(- 0.075+radius) * 40,
				(- 0.075) * 40,
				0.15 * 40,
				0.15* 40);
		jointLocations[4] = new Ellipse2D.Double(
				(- 0.075) * 40,
				(- 0.075-radius) * 40,
				0.15 * 40,
				0.15 * 40);
		BodyFixture b1 = new BodyFixture(cirShape);
		b1.setFriction(0.7);
		b1.setRestitution(0.1);
		b1.setFilter(new CategoryFilter(2,3));
		this.addFixture(b1);
		this.setMass(MassType.NORMAL);
		this.translate(x, y);
		this.rotateAboutCenter(a);
		String[] s = {"GC",""+(x*40),""+(y*40),""+(radius*80),""+(a*180/Math.PI),""+toInt(joints)};
		this.setUserData(s);
	}
	private int toInt(boolean joints) {
		if(joints)
		{
			return 1;
		}
		return 0;
	}
	public void render(Graphics2D g, double scale)
	{
		double x = this.getWorldCenter().x;
		double y = this.getWorldCenter().y;
		double a = this.getTransform().getRotation();
		AffineTransform ot = g.getTransform();
		AffineTransform lt = new AffineTransform();
		lt.scale(scale/40, scale/40);
		lt.translate(x * 40, y * 40);
		lt.rotate(a);
		// apply the transform
		g.transform(lt);
		g.setColor(cb);
		g.fill(drawPath);
		g.setTransform(ot);
	}
	public void render2(Graphics2D g, double scale)
	{
		double x = this.getWorldCenter().x;
		double y = this.getWorldCenter().y;
		double a = this.getTransform().getRotation();
		AffineTransform ot = g.getTransform();
		AffineTransform lt = new AffineTransform();
		lt.scale(scale/40, scale/40);
		lt.translate(x * 40, y * 40);
		lt.rotate(a);
		// apply the transform
		g.transform(lt);
		g.setColor(cf);
		g.fill(fillPath);
		if(joints)
		{
			radius += 0.1;
			g.setStroke(new BasicStroke(2));
			g.setColor(Color.white);
			g.draw(jointLocations[0]);
			g.setColor(new Color(150,150,150));
			for(int i = 1; i < 5; i++)
			{
				g.draw(jointLocations[i]);
			}
		}
		g.setTransform(ot);
	}
	public Vector3[] getJointVectors()
	{
		if(joints)
		{
			double a = this.getTransform().getRotation();
			Vector3[] v = new Vector3[5];
			v[4] = new Vector3(getWorldCenter().x, getWorldCenter().y, 0);
			for(int i = 0; i < 4; i++)
			{
				v[i] = new Vector3(getWorldCenter().x+(radius*Math.cos(a+Math.PI/2*i)), getWorldCenter().y+(radius*Math.sin(a+Math.PI/2*i)), 0);
			}
			return v;
		}
		return null;
	}
	public String[] returnUpdatedData() {
		String[] s = (String[])getUserData();
		s[1] = ""+40*this.getTransform().getTranslationX();
		s[2] = ""+40*this.getTransform().getTranslationY();
		s[3] = ""+80*((Circle)this.getFixture(0).getShape()).getRadius();
		s[4] = ""+this.getTransform().getRotation();
		s[5] = ""+toInt(joints);
		setUserData(s);
		return s;
	}
}