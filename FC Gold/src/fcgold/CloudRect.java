package fcgold;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

public class CloudRect extends GamePiece{
	Rectangle renderRect;
	public double width,height;
	public Color cf = new Color(255,255,255);
	public Color cb = new Color(200,200,200);
	Path2D.Double fillPath = new Path2D.Double();
	Path2D.Double drawPath = new Path2D.Double();
	boolean isJointed;
	public CloudRect(double x, double y, double w, double h, double a, boolean j) {
		width = w;
		height = h;
		fillPath.moveTo(w*20-4, h*20-4);
		fillPath.lineTo(w*20-4, -h*20+4);
		fillPath.lineTo(-w*20+4, -h*20+4);
		fillPath.lineTo(-w*20+4, h*20-4);
		fillPath.lineTo(w*20-4, h*20-4);
		fillPath.closePath();
		drawPath.moveTo(w*20-4, h*20);
		drawPath.lineTo(-w*20+4, h*20);
		drawPath.quadTo(-w*20, h*20, -w*20, h*20-4);
		drawPath.lineTo(-w*20, -h*20+4);
		drawPath.quadTo(-w*20, -h*20, -w*20+4, -h*20);
		drawPath.lineTo(w*20-4, -h*20);
		drawPath.quadTo(w*20, -h*20, w*20, -h*20+4);
		drawPath.lineTo(w*20, h*20-4);
		drawPath.quadTo(w*20, h*20, w*20-4, h*20);
		drawPath.closePath();
		isJointed = j;
		Rectangle floorRect = new Rectangle(w, h);
		BodyFixture b1 = new BodyFixture(floorRect);
		b1.setDensity(0.5); //1
		b1.setFriction(0.7);
		b1.setRestitution(0.1);
		b1.setFilter(cf1);
		this.addFixture(b1);
		this.setMass(MassType.NORMAL);
		this.setLinearDamping(1);
		this.setAngularDamping(1);
		this.setGravityScale(-1);//-0.2
		// move the floor down a bit
		this.translate(x, y);
		this.rotateAboutCenter(a);
		String[] s = {"CR",""+(x*40),""+(y*40),""+(w*40),""+(h*40),""+(a*180/Math.PI),""+toInt(isJointed)};
		this.setUserData(s);
	}
	private int toInt(boolean joints) {
		if(joints)
		{
			return 1;
		}
		return 0;
	}
	public void render(Graphics2D g, double scale)
	{
		double x = this.getWorldCenter().x;
		double y = this.getWorldCenter().y;
		double a = this.getTransform().getRotation();
		AffineTransform ot = g.getTransform();
		AffineTransform lt = new AffineTransform();
		lt.scale(scale/40,scale/40);
		lt.translate(x * 40, y * 40);
		lt.rotate(a);
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
		lt.scale(scale/40,scale/40);
		lt.translate(x * 40, y * 40);
		lt.rotate(a);
		g.transform(lt);
		g.setColor(cf);
		g.fill(fillPath);
		g.setTransform(ot);
		if(isJointed)
		{
			g.setStroke(new BasicStroke(2));
			g.setColor(new Color(150,150,150));
			Ellipse2D.Double c = new Ellipse2D.Double(
					( - 0.075) * 40,
					( - 0.075) * 40,
					0.15 * 40,
					0.15 * 40);
			g.draw(c);
			c = new Ellipse2D.Double(
					( - 0.075-width/2-0.1) * 40,
					( - 0.075-height/2-0.1) * 40,
					0.15 * 40,
					0.15 * 40);
			g.draw(c);
			c = new Ellipse2D.Double(
					(- 0.075-width/2-0.1) * 40,
					(- 0.075+height/2+0.1) * 40,
					0.15 * 40,
					0.15 * 40);
			g.draw(c);
			c = new Ellipse2D.Double(
					(- 0.075+width/2+0.1) * 40,
					(- 0.075+height/2+0.1) * 40,
					0.15 * 40,
					0.15* 40);
			g.draw(c);
			c = new Ellipse2D.Double(
					(- 0.075+width/2+0.1) * 40,
					(- 0.075-height/2-0.1) * 40,
					0.15 * 40,
					0.15 * 40);
			g.draw(c);
		}
	}
}