package fcgold;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.Circle;

public class GoalCircle extends GamePiece{
	public double x,y,a;
	public boolean joints;
	public Circle r;
	public Color cf = new Color(255,50,50);
	public Color cb = cf.darker();
	public GoalCircle(Convex convex, double d, double e, double f, boolean b, int i) {
		x = d;
		y = e;
		a = f;
		joints = b;
		r = (Circle)convex;
		index = i;
	}
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	public double getA()
	{
		return a;
	}
	public void setX(double d)
	{
		x = d;
	}
	public void setY(double d)
	{
		y = d;
	}
	public void setA(double d)
	{
		a = d;
	}
	public void render(Graphics2D g, double scale)
	{
		double radius = r.getRadius();
		Vector2 center = r.getCenter();
		
		double radius2 = 2.0 * radius;
		Ellipse2D.Double c = new Ellipse2D.Double(
			(center.x - radius) * 40,
			(center.y - radius) * 40,
			radius2 * 40,
			radius2 * 40);
		
		// fill the shape
		
		AffineTransform ot = g.getTransform();
		
		// transform the coordinate system from world coordinates to local coordinates
		AffineTransform lt = new AffineTransform();
		lt.scale(scale/40, scale/40);
		lt.translate(x * 40, y * 40);
		lt.rotate(a);
		// apply the transform
		g.transform(lt);
		g.setStroke(new BasicStroke(8));
		g.setColor(cb);
		g.draw(c);
		g.setTransform(ot);
	}
	public void render2(Graphics2D g, double scale)
	{
		double radius = r.getRadius();
		Vector2 center = r.getCenter();
		
		double radius2 = 2.0 * radius;
		Ellipse2D.Double c = new Ellipse2D.Double(
			(center.x - radius) * scale,
			(center.y - radius) * scale,
			radius2 * scale,
			radius2 * scale);
		
		// fill the shape
		
		AffineTransform ot = g.getTransform();
		
		// transform the coordinate system from world coordinates to local coordinates
		AffineTransform lt = new AffineTransform();
		lt.translate(x * scale, y * scale);
		lt.rotate(a);
		// apply the transform
		g.transform(lt);
		g.setColor(cf);
		g.fill(c);
		if(joints)
		{
			radius += 0.1;
			g.setStroke(new BasicStroke((int)(scale/20)));
			g.setColor(Color.white);
			c = new Ellipse2D.Double(
					(center.x - 0.075) * scale,
					(center.y - 0.075) * scale,
					0.15 * scale,
					0.15 * scale);
			g.draw(c);
			g.setColor(new Color(150,150,150));
			c = new Ellipse2D.Double(
					(center.x - 0.075-radius) * scale,
					(center.y - 0.075) * scale,
					0.15 * scale,
					0.15 * scale);
			g.draw(c);
			c = new Ellipse2D.Double(
					(center.x - 0.075) * scale,
					(center.y - 0.075+radius) * scale,
					0.15 * scale,
					0.15 * scale);
			g.draw(c);
			c = new Ellipse2D.Double(
					(center.x - 0.075+radius) * scale,
					(center.y - 0.075) * scale,
					0.15 * scale,
					0.15* scale);
			g.draw(c);
			c = new Ellipse2D.Double(
					(center.x - 0.075) * scale,
					(center.y - 0.075-radius) * scale,
					0.15 * scale,
					0.15 * scale);
			g.draw(c);
			
			g.setStroke(new BasicStroke((int)(scale/5),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		}
		g.setTransform(ot);
	}
}