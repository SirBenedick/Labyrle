package logic.utility;

import java.util.LinkedList;

import logic.Defaults;
import logic.Tilemap;
import logic.utility.Tile;
import logic.utility.TileInformation;

public class Dijkstra
{
	public boolean isValidPath(Tilemap map, TileInformation start, TileInformation end)
	{	
		LinkedList<TileInformation> visited = new LinkedList<TileInformation>();
		LinkedList<TileInformation> border = new LinkedList<TileInformation>();
		
		visited.addLast(start);
		
		do
		{
			border = getBorder(visited.getLast(), map, visited, border);
			if(border.size()==0)
				return false;
			visited.addLast(border.getFirst());
			border.removeFirst();			
		}	while(!isInList(visited, end));
		return true;
	}

	private LinkedList<TileInformation> getBorder(TileInformation startNode, Tilemap map, LinkedList<TileInformation> visited, LinkedList<TileInformation> border)
	{
		Tile[][] tiles = map.getTiles();
		
		//Oben
		int x = startNode.getX();
		int y = startNode.getY() - 1;
		if(isNode(map, x, y, tiles[x][y].getColor()))
			if(!isInList(visited, new TileInformation(tiles[x][y], x, y)))
				if(!isInList(border, new TileInformation(tiles[x][y], x, y)))
					border.addLast(new TileInformation(tiles[x][y], x, y));
		//Rechts
		x = startNode.getX() + 1;
		y = startNode.getY();
		if(isNode(map, x, y, tiles[x][y].getColor()))
			if(!isInList(visited, new TileInformation(tiles[x][y], x, y)))
				if(!isInList(border, new TileInformation(tiles[x][y], x, y)))
					border.addLast(new TileInformation(tiles[x][y], x, y));
		//Links
		x = startNode.getX() - 1;
		y = startNode.getY();
		if(isNode(map, x, y, tiles[x][y].getColor()))
			if(!isInList(visited, new TileInformation(tiles[x][y], x, y)))
				if(!isInList(border, new TileInformation(tiles[x][y], x, y)))
					border.addLast(new TileInformation(tiles[x][y], x, y));
		//Unten
		x = startNode.getX();
		y = startNode.getY() + 1;
		if(isNode(map, x, y, tiles[x][y].getColor()))
			if(!isInList(visited, new TileInformation(tiles[x][y], x, y)))
				if(!isInList(border, new TileInformation(tiles[x][y], x, y)))
					border.addLast(new TileInformation(tiles[x][y], x, y));
		return border;
	}
	
	private boolean isNode(Tilemap map, int x, int y, Color color)
	{
		if(x < 0 || x >= Defaults.LabyrinthWidth || y < 0 || y >= Defaults.LabyrinthHeight)
			return false;
		
		Tile[][] arrayMap = map.getTiles();
		
		if(color != arrayMap[x][y].getColor())
			return false;
		
		return true;
	}
	
	private boolean isInList(LinkedList<TileInformation> list, TileInformation tileInfo)
	{
		return list.contains(tileInfo);
	}
}
