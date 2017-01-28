package fcgold;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import org.dyn4j.geometry.MassType;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;

public class StaticCircle extends GamePiece{
	Ellipse2D.Double drawPath;
	Ellipse2D.Double fillPath;
	public Color cf = new Color(43,197,0);
	public Color cb = new Color(29,133,1);
	public StaticCircle(double x, double y, double radius)
	{
		Circle cirShape = new Circle(radius);
		drawPath = new Ellipse2D.Double(
				(- radius) * 40,
				(- radius) * 40,
				radius * 80,
				radius * 80);
		fillPath = new Ellipse2D.Double(
				(- radius) * 40+4,
				(- radius) * 40+4,
				radius * 80-8,
				radius * 80-8);
		BodyFixture b1 = new BodyFixture(cirShape);
		b1.setFriction(0.7);
		b1.setRestitution(0.1);
		b1.setFilter(new GameFilter(1,7));
		this.addFixture(b1);
		this.setMass(MassType.NORMAL);
		this.translate(x, y);
		String[] s = {"GC",""+(x*40),""+(y*40),""+(radius*80)};
		this.setUserData(s);
	}
	public void render(Graphics2D g, double scale)
	{
		double x = this.getWorldCenter().x;
		double y = this.getWorldCenter().y;
		double a = this.getTransform().getRotation();
		AffineTransform ot = g.getTransform();
		AffineTransform lt = new AffineTransform();
		lt.scale(scale/40, scale/40);
		lt.translate(x * 40, y * 40);
		lt.rotate(a);
		// apply the transform
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
		lt.scale(scale/40, scale/40);
		lt.translate(x * 40, y * 40);
		lt.rotate(a);
		// apply the transform
		g.transform(lt);
		g.setColor(cf);
		g.fill(fillPath);
		g.setTransform(ot);
	}
	public String[] returnUpdatedData() {
		String[] s = (String[])getUserData();
		s[1] = ""+40*this.getTransform().getTranslationX();
		s[2] = ""+40*this.getTransform().getTranslationY();
		s[3] = ""+80*((Circle)this.getFixture(0).getShape()).getRadius();
		setUserData(s);
		return s;
	}
}