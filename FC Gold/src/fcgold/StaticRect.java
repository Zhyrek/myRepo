package fcgold;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Vector2;

public class StaticRect extends GamePiece{
	public double x,y,a;
	public Polygon r;
	public Color cf = new Color(0, 200,0);
	public Color cb = cf.darker();
	public StaticRect(Convex convex, double d, double e, double f) {
		x = d;
		y = e;
		a = f;
		r = (Polygon)convex;
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
	public void render(Graphics2D g, double scale)
	{
		Vector2[] vertices = r.getVertices();
		int l = vertices.length;
		
		// create the awt polygon
		Path2D.Double p = new Path2D.Double();
		p.moveTo(vertices[0].x * scale, vertices[0].y * scale);
		for (int i = 1; i < l; i++) {
			p.lineTo(vertices[i].x * scale, vertices[i].y * scale);
		}
		p.closePath();
AffineTransform ot = g.getTransform();
		
		// transform the coordinate system from world coordinates to local coordinates
		AffineTransform lt = new AffineTransform();
		lt.translate(x * scale, y * scale);
		lt.rotate(a);
		
		// apply the transform
		g.transform(lt);
		g.setColor(cb);
		g.draw(p);
		g.setTransform(ot);
	}
	public void render2(Graphics2D g, double scale)
	{
		Vector2[] vertices = r.getVertices();
		int l = vertices.length;
		
		// create the awt polygon
		Path2D.Double p = new Path2D.Double();
		p.moveTo(vertices[0].x * scale, vertices[0].y * scale);
		for (int i = 1; i < l; i++) {
			p.lineTo(vertices[i].x * scale, vertices[i].y * scale);
		}
		p.closePath();
		AffineTransform ot = g.getTransform();
		
		// transform the coordinate system from world coordinates to local coordinates
		AffineTransform lt = new AffineTransform();
		lt.translate(x * scale, y * scale);
		lt.rotate(a);
		
		// apply the transform
		g.transform(lt);
		g.setColor(cf);
		g.fill(p);
		g.setTransform(ot);
		
	}
}
