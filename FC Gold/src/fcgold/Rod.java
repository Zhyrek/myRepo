package fcgold;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.collision.Filter;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

public class Rod extends GamePiece{
	public double x,y,a;
	public Rectangle r;
	public Color cf;
	public Color cb;
	public CategoryFilter cf2 = new CategoryFilter(2,3);
	public CategoryFilter cf3 = new CategoryFilter(4,1);
	public CategoryFilter cf4 = new CategoryFilter(8,0);
	public Vector2 p1;
	public Vector2 p2;
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
	public Rod(int type, double x1, double y1, double x2, double y2)
	{
		p1 = new Vector2(x1,y1);
		p2 = new Vector2(x1,y1);
		double height = Math.abs(p1.distance(p2));
		Vector2 midpoint = p1.copy();
		midpoint.add(p2);
		midpoint.setMagnitude(midpoint.getMagnitude()/2);
		a = (p2.copy().difference(p1)).getDirection();
		if(type == 0)
		{
			cf = new Color(0,0,255);
			cb = Color.white;
			Rectangle floorRect = new Rectangle(height, 0.1);
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf3);
    		this.addFixture(b);
		}
		else if(type == 1)
		{
			cf = new Color(200,160,120);
			cb = cf.darker();
			Rectangle floorRect = new Rectangle(height, 0.2);
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf2);
    		this.addFixture(b);
		}
		else if(type == 2)
		{
			cf = new Color(255,200,0);
			cb = cf.brighter();
			Rectangle floorRect = new Rectangle(height, 0.2);
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf2);
    		b.setDensity(20);
    		this.addFixture(b);
		}
		else
		{
			cf = new Color(200,255,200);
			cb = cf.darker();
			Rectangle floorRect = new Rectangle(height, 0.1);
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf4);
    		this.addFixture(b);
		}
		this.setMass(MassType.NORMAL);
		// move the floor down a bit
		this.translate(midpoint);
		this.rotateAboutCenter(a);
		
	}
	public Rod(int type, Vector2 point1, Vector2 point2)
	{
		p1 = point1;
		p2 = point2;
		double height = Math.abs(p1.distance(p2));
		Vector2 midpoint = p1.copy();
		midpoint.add(p2);
		midpoint.setMagnitude(midpoint.getMagnitude()/2);
		a = (p2.copy().difference(p1)).getDirection();
		if(type == 0)
		{
			cf = new Color(0,0,255);
			cb = Color.white;
			Rectangle floorRect = new Rectangle(height, 0.1);
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf3);
    		this.addFixture(b);
		}
		else if(type == 1)
		{
			cf = new Color(200,160,120);
			cb = cf.darker();
			Rectangle floorRect = new Rectangle(height, 0.2);
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf2);
    		this.addFixture(b);
		}
		else if(type == 2)
		{
			cf = new Color(255,200,0);
			cb = cf.brighter();
			Rectangle floorRect = new Rectangle(height, 0.2);
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf2);
    		b.setDensity(20);
    		this.addFixture(b);
		}
		else
		{
			cf = new Color(200,255,200);
			cb = cf.darker();
			Rectangle floorRect = new Rectangle(height, 0.1);
    		BodyFixture b = new BodyFixture(floorRect);
    		b.setFriction(0.7);
    		b.setRestitution(0.1);
    		b.setFilter(cf4);
    		this.addFixture(b);
		}
		this.setMass(MassType.NORMAL);
		// move the floor down a bit
		this.translate(midpoint);
		this.rotateAboutCenter(a);
	}
	public void adjustShape(double x1, double y1, double x2, double y2)
	{
		p1 = new Vector2(x1, y1);
		p2 = new Vector2(x2, y2);
		double density = this.getFixture(0).getDensity();
		Filter caFi = this.getFixture(0).getFilter();
		
		double height = Math.abs(p1.distance(p2));
		Vector2 midpoint = p1.copy();
		midpoint.add(p2);
		midpoint.setMagnitude(midpoint.getMagnitude()/2);
		a = (p2.copy().difference(p1)).getDirection();
		
		this.removeAllFixtures();
		Rectangle floorRect = new Rectangle(height, 0.1);
		BodyFixture b = new BodyFixture(floorRect);
		b.setFriction(0.7);
		b.setRestitution(0.1);
		b.setDensity(density);
		b.setFilter(caFi);
		this.addFixture(b);
		
		this.translate(midpoint);
		this.rotateAboutCenter(a);
	}
	public void adjustShape(Vector2 point1, Vector2 point2)
	{
		p1 = point1;
		p2 = point2;
		double density = this.getFixture(0).getDensity();
		Filter caFi = this.getFixture(0).getFilter();
		
		double height = Math.abs(p1.distance(p2));
		Vector2 midpoint = p1.copy();
		midpoint.add(p2);
		midpoint.setMagnitude(midpoint.getMagnitude()/2);
		a = (p2.copy().difference(p1)).getDirection();
		
		this.removeAllFixtures();
		Rectangle floorRect = new Rectangle(height, 0.1);
		BodyFixture b = new BodyFixture(floorRect);
		b.setFriction(0.7);
		b.setRestitution(0.1);
		b.setDensity(density);
		b.setFilter(caFi);
		this.addFixture(b);
		
		this.translate(midpoint);
		this.rotateAboutCenter(a);
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
