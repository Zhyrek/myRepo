package fcgold;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

public class GoalArea extends GamePiece{
	Rectangle renderRect;
	public Color cf = new Color(234,140,146);
	public Color cb = new Color(180,98,104);
	Path2D.Double fillPath = new Path2D.Double();
	Path2D.Double drawPath = new Path2D.Double();
	public GoalArea(double x, double y, double w, double h) {
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
		this.addFixture(b1);
		this.setActive(false);
		this.setMass(MassType.NORMAL);
		// move the floor down a bit
		this.translate(x, y);
		String[] s = {"GA",""+(x*40),""+(y*40),""+(w*40),""+(h*40)};
		this.setUserData(s);
	}
	public void render(Graphics2D g, double scale)
	{
		double x = this.getWorldCenter().x;
		double y = this.getWorldCenter().y;
		AffineTransform ot = g.getTransform();
		AffineTransform lt = new AffineTransform();
		lt.scale(scale/40,scale/40);
		lt.translate(x * 40, y * 40);
		g.transform(lt);
		g.setColor(cb);
		g.fill(drawPath);
		g.setTransform(ot);
	}
	public void render2(Graphics2D g, double scale)
	{
		double x = this.getWorldCenter().x;
		double y = this.getWorldCenter().y;
		AffineTransform ot = g.getTransform();
		AffineTransform lt = new AffineTransform();
		lt.scale(scale/40,scale/40);
		lt.translate(x * 40, y * 40);
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
		setUserData(s);
		return s;
	}
}