package fcgold;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Vector2;

public class GoalArea extends GamePiece{
	public double x,y;
	public Polygon r;
	public Color cf = new Color(234,140,146);
	public Color cb = new Color(180,98,104);
	public GoalArea(Convex convex, double d, double e, int i) {
		x = d;
		y = e;
		r = (Polygon)convex;
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
	public void setX(double d)
	{
		x = d;
	}
	public void setY(double d)
	{
		y = d;
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
		double width = Math.abs(vertices[3].x-vertices[1].x);
		double height = Math.abs(vertices[3].y-vertices[1].y);
		Vector2 center = r.getCenter();
		AffineTransform ot = g.getTransform();
		
		// transform the coordinate system from world coordinates to local coordinates
		AffineTransform lt = new AffineTransform();
		lt.translate(x * scale, y * scale);
		
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
		double width = Math.abs(vertices[3].x-vertices[1].x);
		double height = Math.abs(vertices[3].y-vertices[1].y);
		Vector2 center = r.getCenter();
		AffineTransform ot = g.getTransform();
		
		// transform the coordinate system from world coordinates to local coordinates
		AffineTransform lt = new AffineTransform();
		lt.translate(x * scale, y * scale);
		
		// apply the transform
		g.transform(lt);
		g.setColor(cf);
		g.fill(p);
		g.setTransform(ot);
	}
}