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

public class CloudRect extends GamePiece{
	public double x,y,a;
	public Polygon r;
	public Color cf = Color.white;
	public Color cb = cf.darker();
	public boolean joints;
	public CloudRect(Convex convex, double d, double e, double f, boolean b, int i) {
		x = d;
		y = e;
		a = f;
		r = (Polygon)convex;
		joints = b;
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
		double width = Math.abs(vertices[3].x-vertices[1].x);
		double height = Math.abs(vertices[3].y-vertices[1].y);
		Vector2 center = r.getCenter();
		AffineTransform ot = g.getTransform();
		
		// transform the coordinate system from world coordinates to local coordinates
		AffineTransform lt = new AffineTransform();
		lt.translate(x * scale, y * scale);
		lt.rotate(a);
		
		// apply the transform
		g.transform(lt);
		g.setColor(cf);
		g.fill(p);
		if(joints)
		{
			g.setStroke(new BasicStroke((int)(scale/20)));
			g.setColor(new Color(150,150,150));
			Ellipse2D.Double c = new Ellipse2D.Double(
					(center.x - 0.075) * scale,
					(center.y - 0.075) * scale,
					0.15 * scale,
					0.15 * scale);
			g.draw(c);
			c = new Ellipse2D.Double(
					(center.x - 0.075-width/2-0.1) * scale,
					(center.y - 0.075-height/2-0.1) * scale,
					0.15 * scale,
					0.15 * scale);
			g.draw(c);
			c = new Ellipse2D.Double(
					(center.x - 0.075-width/2-0.1) * scale,
					(center.y - 0.075+height/2+0.1) * scale,
					0.15 * scale,
					0.15 * scale);
			g.draw(c);
			c = new Ellipse2D.Double(
					(center.x - 0.075+width/2+0.1) * scale,
					(center.y - 0.075+height/2+0.1) * scale,
					0.15 * scale,
					0.15* scale);
			g.draw(c);
			c = new Ellipse2D.Double(
					(center.x - 0.075+width/2+0.1) * scale,
					(center.y - 0.075-height/2-0.1) * scale,
					0.15 * scale,
					0.15 * scale);
			g.draw(c);
			
			g.setStroke(new BasicStroke((int)(scale/5),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		}
		g.setTransform(ot);
	}
}