package fcgold;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.Circle;

public class StaticCircle extends GamePiece{
	public double x,y;
	public Circle r;
	public Color cf = new Color(0, 200,0);
	public Color cb = cf.darker();
	public StaticCircle(Convex convex, double d, double e) {
		x = d;
		y = e;
		r = (Circle)convex;
	}
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
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
		
		// apply the transform
		g.transform(lt);
		g.setColor(cf);
		g.fill(c);
		g.setTransform(ot);
		
	}
}
