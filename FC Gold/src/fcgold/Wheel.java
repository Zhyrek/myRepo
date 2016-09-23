package fcgold;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.Circle;

public class Wheel extends GamePiece{
	public double x,y,a;
	public Circle r;
	public Color cf = new Color(255,50,50);
	public Color cb = cf.darker();
	public Wheel(Convex convex, double d, double e, double f, double torque, int i) {
		x = d;
		y = e;
		a = f;
		r = (Circle)convex;
		index = i;
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
		g.setTransform(ot);
	}
}