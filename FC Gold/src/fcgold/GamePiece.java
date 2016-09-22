package fcgold;

import java.util.ArrayList;

public class GamePiece {
	int index;
	public GamePiece()
	{
		
	}
	public int getI()
	{
		return index;
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
}
