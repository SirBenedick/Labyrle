package logic.utility;

import java.util.LinkedList;

import logic.Tilemap;
import logic.utility.TileInformation;

public class Dijkstra
{
	static public boolean isValidPath(Tilemap map, TileInformation start, TileInformation end)
	{	
		LinkedList<TileInformation> visited = new LinkedList<TileInformation>();
		LinkedList<TileInformation> border = new LinkedList<TileInformation>();
		
		visited.add(start);
		addToBorder(start, visited, border, map);
		
		//Points match
		if (start.equals(end))
			return true;
		
		//There is no border initially
		if (border.size() == 0)
			return false;
		
		boolean found = false;
		
		//As long as there are nearby possible fields or we found our target.
		do
		{
			TileInformation next = border.pop();
			visited.add(next);
			addToBorder(next, visited, border, map);
		} while( !(found = visited.contains(end)) && !border.isEmpty() );
		
		return found;
	}
	
	static private void addToBorder(TileInformation info, LinkedList<TileInformation> visited, LinkedList<TileInformation> border, Tilemap map)
	{
		int x = info.getX(), y = info.getY();
		Color color = info.getTile().getColor();
		
		//Left
		addToList(x-1, y, color, visited, border, map);
		//Right
		addToList(x+1, y, color, visited, border, map);
		//Top
		addToList(x, y-1, color, visited, border, map);
		//Bottom
		addToList(x, y+1, color, visited, border, map);
	}
	
	static private void addToList(int x, int y, Color color, LinkedList<TileInformation> visited, LinkedList<TileInformation> border, Tilemap map)
	{
		//Boundary check
		if (x < 0 || x >= map.getWidth())
			return;
		
		if (y < 0 || y >= map.getHeight())
			return;
		
		//Is it of same color and not an end point?
		if (map.getTiles()[x][y].getColor() != color && !(map.getEndPoint().getX() == x && map.getEndPoint().getY() == y))
			return;
		
		//Is already in visited or border?
		TileInformation info = new TileInformation(map.getTiles()[x][y], x, y);
		if (border.contains(info) || visited.contains(info))
			return;
	
		//We can add since it has not been visited or is not already in our border
		border.add(info);
	}
}
