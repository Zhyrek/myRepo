package fcgold;

import java.awt.Graphics2D;
import java.util.ArrayList;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector3;

public class GamePiece extends Body{
	int index;
	public GamePiece()
	{
		
	}
	public int getI()
	{
		return index;
	}
	public Vector3[] getJointVectors()
	{
		return new Vector3[0];
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
	public void render(Graphics2D g, double scale) {
		
	}
	public void render2(Graphics2D g, double scale) {
		
	}
	public String[] returnUpdatedData() {
		return null;
	}
}
