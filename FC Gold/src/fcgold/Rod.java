package fcgold;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

public class Rod extends GamePiece{
	public double x,y,a;
	public Rectangle r;
	public Color cf;
	public Color cb;
	public Rod(int type, Convex convex, double d, double e, double f, int i) {
		if(type == 0)
		{
			cf = new Color(0,0,255);
			cb = Color.white;
		}
		else if(type == 1)
		{
			cf = new Color(200,160,120);
			cb = cf.darker();
		}
		else if(type == 2)
		{
			cf = new Color(255,200,0);
			cb = cf.brighter();
		}
		else
		{
			cf = new Color(200,255,200);
			cb = cf.darker();
		}
		x = d;
		y = e;
		a = f;
		r = (Rectangle)convex;
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
	public void adjustIndex(ArrayList<Integer> a)
	{
		for(int i = 0; i < a.size(); i++)
		{
			if(a.get(i) < index)
			{
				index--;
			}
		}
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
		double ow = r.getWidth();
		r = new Rectangle(sc(ow-0.2), 0.1);
		Vector2[] vertices = r.getVertices();
		int l = vertices.length;
		
		// create the awt polygon
		Path2D.Double p = new Path2D.Double();
		p.moveTo(vertices[0].x * scale, vertices[0].y * scale);
		for (int i = 1; i < l; i++) {
			p.lineTo(vertices[i].x * scale, vertices[i].y * scale);
		}
		p.closePath();
		r = new Rectangle(ow, 0.1);
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
		double ow = r.getWidth();
		r = new Rectangle(sc(ow-0.2), 0.1);
		Vector2[] vertices = r.getVertices();
		int l = vertices.length;
		
		// create the awt polygon
		Path2D.Double p = new Path2D.Double();
		p.moveTo(vertices[0].x * scale, vertices[0].y * scale);
		for (int i = 1; i < l; i++) {
			p.lineTo(vertices[i].x * scale, vertices[i].y * scale);
		}
		p.closePath();
		r = new Rectangle(ow, 0.1);
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
		g.setStroke(new BasicStroke((int)(scale/20)));
		g.setColor(new Color(150,150,150));
		Ellipse2D.Double c = new Ellipse2D.Double(
				(center.x+ow/2 - 0.075) * scale,
				(center.y - 0.075) * scale,
				0.15 * scale,
				0.15 * scale);
		g.draw(c);
		c = new Ellipse2D.Double(
				(center.x - 0.075-ow/2) * scale,
				(center.y - 0.075) * scale,
				0.15 * scale,
				0.15 * scale);
		g.draw(c);
		g.setStroke(new BasicStroke((int)(scale/5),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g.setTransform(ot);
	}
	public double sc(double d)
	{
		if(d < 0.1)
		{
			return 0.1;
		}
		return d;
		
	}
}
