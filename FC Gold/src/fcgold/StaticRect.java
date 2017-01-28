package fcgold;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

public class StaticRect extends GamePiece{
	Rectangle renderRect;
	public Color cf = new Color(43,197,0);
	public Color cb = new Color(29,133,1);
	Path2D.Double fillPath = new Path2D.Double();
	Path2D.Double drawPath = new Path2D.Double();
	public StaticRect(double x, double y, double w, double h, double a) {
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
		Rectangle floorRect = new Rectangle(w, h);
		BodyFixture b1 = new BodyFixture(floorRect);
		b1.setFriction(0.7);
		b1.setRestitution(0.1);
		b1.setFilter(new GameFilter(1,7));
		this.addFixture(b1);
		this.setMass(MassType.INFINITE);
		// move the floor down a bit
		this.translate(x, y);
		this.rotateAboutCenter(a);
		String[] s = {"SR",""+(x*40),""+(y*40),""+(w*40),""+(h*40),""+(a*180/Math.PI)};
		this.setUserData(s);
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
		
	}
	public String[] returnUpdatedData() {
		String[] s = (String[])getUserData();
		s[1] = ""+40*this.getTransform().getTranslationX();
		s[2] = ""+40*this.getTransform().getTranslationY();
		s[3] = ""+40*((Rectangle)this.getFixture(0).getShape()).getWidth();
		s[4] = ""+40*((Rectangle)this.getFixture(0).getShape()).getHeight();
		s[5] = ""+this.getTransform().getRotation();
		setUserData(s);
		return s;
	}
}