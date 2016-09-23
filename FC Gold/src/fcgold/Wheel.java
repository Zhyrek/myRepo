package fcgold;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import org.dyn4j.geometry.MassType;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;

public class Wheel extends GamePiece{
	Ellipse2D.Double drawPath;
	Ellipse2D.Double fillPath;
	Ellipse2D.Double[] jointLocations = new Ellipse2D.Double[5];
	public Color cf;
	public Color cb;
	public Wheel(double x, double y, double radius, double angle, double torque)
	{
		if(torque < 0)
		{
			cb = new Color(203,46,165);
			cf = new Color(251,201,204);
		}
		else if(torque > 0)
		{
			cb = new Color(243,126,34);
			cf = new Color(249,243,22);
		}
		else
		{
			cb = new Color(68,82,252);
			cf = new Color(148,254,225);
		}
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
		this.rotateAboutCenter(angle);
		String[] s = {"GC",""+(x*40),""+(y*40),""+(radius*80),""+(angle*180/Math.PI),""+torque};
		this.setUserData(s);
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
		radius += 0.1;
		g.setStroke(new BasicStroke(2));
		g.setColor(Color.white);
		g.draw(jointLocations[0]);
		g.setColor(new Color(150,150,150));
		for(int i = 1; i < 5; i++)
		{
			g.draw(jointLocations[i]);
		}
		g.setTransform(ot);
	}
}