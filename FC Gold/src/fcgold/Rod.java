package fcgold;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

public class Rod extends GamePiece{
	public double x,y,a,width,height;
	public double density = 1;
	public Rectangle r;
	public Color cf;
	public Color cb;
	public CategoryFilter cf2 = new CategoryFilter(2,3);
	public CategoryFilter cf3 = new CategoryFilter(4,1);
	public CategoryFilter cf4 = new CategoryFilter(8,0);
	public CategoryFilter filter = cf2;
	public Vector2 p1, p2, offset1, offset2;
	public int joint1, joint2;
	public int f1 = 2;
	public int f2 = 3;
	public Rod(int type, int j1, double x1, double y1, int j2, double x2, double y2)
	{
		joint1 = j1;
		joint2 = j2;
		offset1 = new Vector2(x1, y1);
		offset2 = new Vector2(x2, y2);
		if(type == 0)
		{
			cf = new Color(0,0,255);
			cb = Color.white;
			height = 0.1;
			f1 = 4;
			f2 = 1;
		}
		else if(type == 1)
		{
			cf = new Color(200,160,120);
			cb = cf.darker();
			height = 0.2;
		}
		else if(type == 2)
		{
			cf = new Color(255,200,0);
			cb = cf.brighter();
			height = 0.2;
			density = 20;
		}
		else
		{
			cf = new Color(200,255,200);
			cb = cf.darker();
			height = 0.1;
			f1 = 8;
			f2 = 0;
		}
		setMass(MassType.NORMAL);
		String[] data = {"R",""+type,""+j1,""+x1,""+y1,""+j2,""+x2,""+y2};
		setUserData(data);
		
	}
	public Rod(int type, int j1, Vector2 os1, int j2, Vector2 os2)
	{
		joint1 = j1;
		joint2 = j2;
		offset1 = os1;
		offset2 = os2;
		if(type == 0)
		{
			cf = new Color(0,0,255);
			cb = Color.white;
			height = 0.1;
			f1 = 4;
			f2 = 1;
		}
		else if(type == 1)
		{
			cf = new Color(200,160,120);
			cb = cf.darker();
			height = 0.2;
		}
		else if(type == 2)
		{
			cf = new Color(255,200,0);
			cb = cf.brighter();
			height = 0.2;
			density = 20;
		}
		else
		{
			cf = new Color(200,255,200);
			cb = cf.darker();
			height = 0.1;
			f1 = 8;
			f2 = 0;
		}
		setMass(MassType.NORMAL);
		String[] data = {"R",""+type,""+j1,""+os1.x,""+os1.y,""+j2,""+os2.x,""+os2.y};
		setUserData(data);
		
	}
	public void adjustShape(Vector2 point1, Vector2 point2)
	{
		p1 = point1.copy();
		p2 = point2.copy();
		p1.add(offset1);
		p2.add(offset2);
		System.out.println(p1+", "+p2);
		width = Math.abs(p1.distance(p2));
		
		Vector2 midpoint = p1.copy();
		midpoint.add(p2);
		midpoint.setMagnitude(midpoint.getMagnitude()/2);
		a = (p2.copy().difference(p1)).getDirection();
		
		this.setTransform(Transform.IDENTITY);
		this.removeAllFixtures();
		Rectangle floorRect = new Rectangle(sc(width), sc(height));
		BodyFixture b = new BodyFixture(floorRect);
		b.setFriction(0.7);
		b.setRestitution(0.1);
		b.setDensity(density);
		b.setFilter(new GameFilter(f1, f2, p1, p2));
		this.addFixture(b);
		
		this.setMass(MassType.NORMAL);
		this.translate(midpoint);
		this.rotateAboutCenter(a);
	}
	public void initializeJoints(int j1, int j2)
	{
		joint1 = j1;
		joint2 = j2;
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
		r = new Rectangle(sc(width-0.2), 0.1);
		Vector2[] vertices = r.getVertices();
		int l = vertices.length;
		
		// create the awt polygon
		Path2D.Double p = new Path2D.Double();
		p.moveTo(vertices[0].x * scale, vertices[0].y * scale);
		for (int i = 1; i < l; i++) {
			p.lineTo(vertices[i].x * scale, vertices[i].y * scale);
		}
		p.closePath();
		r = new Rectangle(sc(width), 0.1);
		AffineTransform ot = g.getTransform();
		
		// transform the coordinate system from world coordinates to local coordinates
		AffineTransform lt = new AffineTransform();
		lt.translate(getWorldCenter().x * scale, getWorldCenter().y * scale);
		lt.rotate(getTransform().getRotation());
		
		// apply the transform
		g.transform(lt);
		g.setColor(cb);
		g.draw(p);
		g.setTransform(ot);
	}
	public void render2(Graphics2D g, double scale)
	{
		r = new Rectangle(sc(width-0.2), 0.1);
		Vector2[] vertices = r.getVertices();
		int l = vertices.length;
		
		// create the awt polygon
		Path2D.Double p = new Path2D.Double();
		p.moveTo(vertices[0].x * scale, vertices[0].y * scale);
		for (int i = 1; i < l; i++) {
			p.lineTo(vertices[i].x * scale, vertices[i].y * scale);
		}
		p.closePath();
		r = new Rectangle(sc(width), 0.1);
		Vector2 center = r.getCenter();
		AffineTransform ot = g.getTransform();
		// transform the coordinate system from world coordinates to local coordinates
		AffineTransform lt = new AffineTransform();
		lt.translate(getWorldCenter().x * scale, getWorldCenter().y * scale);
		lt.rotate(getTransform().getRotation());
		
		// apply the transform
		g.transform(lt);
		g.setColor(cf);
		g.fill(p);
		g.setStroke(new BasicStroke((int)(scale/20)));
		g.setColor(new Color(150,150,150));
		Ellipse2D.Double c = new Ellipse2D.Double(
				(center.x+width/2 - 0.075) * scale,
				(center.y - 0.075) * scale,
				0.15 * scale,
				0.15 * scale);
		g.draw(c);
		c = new Ellipse2D.Double(
				(center.x - 0.075-width/2) * scale,
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
	public String[] returnUpdatedData() {
		String[] s = (String[])getUserData();
		s[2] = ""+joint1;
		s[3] = ""+offset1.x;
		s[4] = ""+offset1.y;
		s[5] = ""+joint2;
		s[6] = ""+offset2.x;
		s[7] = ""+offset2.y;
		return s;
	}
}
