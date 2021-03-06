package logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import logic.utility.Color;
import logic.utility.Dijkstra;
import logic.utility.Tile;
import logic.utility.TileInformation;
import logic.utility.TileType;

/**
 * 				FILE FORMAT 
 * BYTE[2] "TQ" 
 * INT mapWidth 
 * INT mapHeight 
 * for x to width for y to height 
 *  	INT type
 *  	INT color
 *  	INT target_color 
 *
 *INT Start count 
 *For each start 
 *		INT x 
 *		INT y 
 *
 *INT x of end point
 *INT y of end point
 *
 *BYTE[2] "TQ"
 */
public class Tilemap
{
	/**
	 * The actual tilemap. Access order is X/Y.
	 */
	private Tile tiles[][];

	/**
	 * Holds all 4 starting points ordered by color ascending. Is exactly the size
	 * of 4.
	 * 
	 * Whenever the value of a field is null, the starting point is not used. Are
	 * all fields null, the map is invalid.
	 */
	private TileInformation[] startingPoints;

	/**
	 * The point all lines have to be connected with. If null the current map is
	 * invalid.
	 */
	private TileInformation endPoint;

	/*
	 * Used for fast lookup, since the pass points are embedded directly in the tile properties.
	 */
	private ArrayList<TileInformation> passPoints;
	
	/*
	 * Faster to just save the whole map instead of deltas, since maps are pretty small in ram
	 */
	private LinkedList<Tile[][]> undoMapStack;
	
	public Tilemap()
	{
		clear();
		clearUndoStack();
		passPoints = new ArrayList<TileInformation>();
	}
	
	public void setTileColor(int x, int y, Color c)
	{
		if (tiles[x][y].getColor() != Color.NONE)
			return;
		
		setTileColorForce(x, y, c);
	}
	
	public void setTileColorForce(int x, int y, Color c)
	{
		if (isEntity(x, y))
			return;
		
		if (tiles[x][y].getType() == TileType.WALL)
			return;
		
		tiles[x][y].setColor(c);
	}
	
	private void createPassPointList()
	{
		passPoints = new ArrayList<TileInformation>();
		
		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getHeight(); y++)
			{
				if (tiles[x][y].getTarget() != Color.NONE)
					passPoints.add(new TileInformation(tiles[x][y], x, y));
			}
		}
	}
	
	public void clearUndoStack()
	{
		undoMapStack = new LinkedList<Tile[][]>();
	}
	
	public void pushUndo()
	{
		Tile[][] tileCopy = new Tile[getWidth()][getHeight()];
		for (int x = 0; x < this.getWidth(); x++)
		{
			for (int y = 0; y < this.getHeight(); y++)
				tileCopy[x][y] = (Tile) tiles[x][y].clone();
		}
		
		if (undoMapStack.size() > 0)
		{
			boolean difference_found = false;
			
			for (int x = 0; x < this.getWidth(); x++)
			{
				for (int y = 0; y < this.getHeight(); y++)
				{
					if (!undoMapStack.getFirst()[x][y].equals(tileCopy[x][y]))
					{
						difference_found = true;
						break;
					}
				}
				
				if (difference_found)
					break;
			}
			
			//Do not add to list
			if (!difference_found)
				return;
		}
		
		if (undoMapStack.size() >= Defaults.MAX_UNDO_STEPS)
			undoMapStack.removeLast();
		
		undoMapStack.addFirst(tileCopy);
	}

	public void undo()
	{
		//We are in the first map state (no colors set!)
		if (undoMapStack.size() == 0)
			return;
		
		//Apply old state if present
		Tile[][] order = undoMapStack.pop();
		for (int x = 0; x < this.getWidth(); x++)
		{
			for (int y = 0; y < this.getHeight(); y++)
			{
				tiles[x][y].setColor(order[x][y].getColor());
				tiles[x][y].setTarget(order[x][y].getTarget());
				tiles[x][y].setType(order[x][y].getType());
			}
		}
	}
	
	public void removeTileColor(int x, int y)
	{
		if (isEntity(x, y))
			return;
		
		tiles[x][y].setColor(Color.NONE);
	}
	
	public void clear()
	{
		startingPoints = new TileInformation[4];
		endPoint = null;

		tiles = new Tile[Defaults.LabyrinthWidth][Defaults.LabyrinthHeight];
		for (int x = 0; x < Defaults.LabyrinthWidth; x++)
		{
			for (int y = 0; y < Defaults.LabyrinthHeight; y++)
				tiles[x][y] = new Tile();
		}
		
		//TOP
		for (int x = 0; x < getWidth(); x++)
			tiles[x][0].setType(TileType.WALL); 
		
		//Bottom
		for (int x = 0; x < getWidth(); x++)
			tiles[x][getHeight()-1].setType(TileType.WALL); 
		
		//LEFT
		for (int y = 0; y < getHeight(); y++)
			tiles[0][y].setType(TileType.WALL); 
		
		//RIGHT
		for (int y = 0; y < getHeight(); y++)
			tiles[getWidth()-1][y].setType(TileType.WALL); 
	}
	
	public boolean isStartPoint(int x, int y)
	{
		for (int i = 0; i < startingPoints.length; i++)
		{
			if (startingPoints[i] == null)
				continue;
			
			if (startingPoints[i].getX() == x && startingPoints[i].getY() == y)
				return true;
		}
		
		return false;
	}

	public int getStartingPointCount()
	{
		int count = 0;
		for (int i = 0; i < startingPoints.length; i++)
		{
			if (startingPoints[i] != null)
				count++;
		}

		return count;
	}
	
	public boolean isEntity(int x, int y)
	{	
		if (tiles[x][y].getTarget() != Color.NONE)
			return true;
		
		for (int i = 0; i < startingPoints.length; i++)
		{
			if (startingPoints[i] == null)
				continue;
			
			if (startingPoints[i].getX() == x && startingPoints[i].getY() == y)
				return true;
		}
		
		//Remove endpoint
		if (endPoint == null)
			return false;
		
		if (endPoint.getX() == x && endPoint.getY() == y)
			return true;
				
		return false;
	}

	public boolean hasStartingPoint()
	{
		return getStartingPointCount() != 0;
	}

	public boolean hasEndPoint()
	{
		return endPoint != null;
	}

	public boolean isValid()
	{
		return hasStartingPoint() && hasEndPoint();
	}

	public int getWidth()
	{
		return Defaults.LabyrinthWidth;
	}

	public int getHeight()
	{
		return Defaults.LabyrinthHeight;
	}

	public void setStartingPoint(Color color, int x, int y)
	{
		if (color == Color.NONE)
			return;
		
		if (startingPoints[color.ordinal()] != null)
			removeEntity(startingPoints[color.ordinal()] .getX(), startingPoints[color.ordinal()].getY());

		removeEntity(x, y);

		tiles[x][y].setColor(color);
		tiles[x][y].setType(TileType.FREE);
		
		startingPoints[color.ordinal()] = new TileInformation(tiles[x][y], x, y);
	}

	public void removeStartingPoint(Color color)
	{
		if (color == Color.NONE)
			return;

		if (startingPoints[color.ordinal()] == null)
			return;

		startingPoints[color.ordinal()].getTile().setColor(Color.NONE);
		startingPoints[color.ordinal()] = null;
	}
	
	/**
	 * @param x position of tile
	 * @param y position of tile
	 * 
	 * Removes any starting point, end point or pass point located on this tile.
	 */
	public void removeEntity(int xpos, int ypos)
	{
		//remove pass point
		tiles[xpos][ypos].setTarget(Color.NONE);
		
		//Remove startpoint
		for (int i = 0; i < startingPoints.length; i++)
		{
			if (startingPoints[i] == null)
				continue;
			
			if (startingPoints[i].getX() == xpos && startingPoints[i].getY() == ypos)
				removeStartingPoint(startingPoints[i].getTile().getColor());
		}
		
		//remove color
		tiles[xpos][ypos].setColor(Color.NONE);
		
		//Remove endpoint
		if (endPoint == null)
			return;
		
		if (endPoint.getX() == xpos && endPoint.getY() == ypos)
			removeEndPoint();
	}
	
	public TileInformation[] getStartPoints()
	{
		return startingPoints;
	}
	
	public TileInformation getEndPoint()
	{
		return endPoint;
	}

	public Tile[][] getTiles()
	{
		return tiles;
	}

	public void setEndPoint(int x, int y)
	{
		removeEntity(x, y);
		tiles[x][y].setType(TileType.FREE);
		endPoint = new TileInformation(tiles[x][y], x, y);
	}

	public void removeEndPoint()
	{
		endPoint = null;
	}
	
	public void setPassPoint(int x, int y, Color c)
	{
		removeEntity(x, y);
		tiles[x][y].setType(TileType.FREE);
		tiles[x][y].setColor(c);
		tiles[x][y].setTarget(c);
	}
	
	public boolean isSolved()
	{
		//Check if colors are connected with the finish.
		for (int i = 0; i < startingPoints.length; i++)
		{
			if (startingPoints[i] == null)
				continue;
			
			if (!Dijkstra.isValidPath(this, startingPoints[i], endPoint))
				return false;
		}
		
		//Check if all required passpoints are connected as well, with finish OR start.
		for (Iterator<TileInformation> iter = passPoints.iterator(); iter.hasNext(); )
		{
			TileInformation passPoint = iter.next();
			boolean isConnectedToStart = Dijkstra.isValidPath
			(
					this, 
					this.getStartPoints()[passPoint.getTile().getColor().ordinal()], 
					passPoint
			);
			
			boolean isConnectedToEndPoint = Dijkstra.isValidPath
			(
					this,
					passPoint,
					getEndPoint()
			);
			
			if (!isConnectedToStart && !isConnectedToEndPoint)
				return false;
		}
		
		
		return true;
	}

	public void saveToFile(String path) throws Exception
	{
		if (!isValid())
			throw new Exception("Tilemap is not valid! You are probably missing a start or an end point");
		
		OutputStream out = new FileOutputStream(path);

		// Write identification string
		out.write(new byte[] { 'T', 'Q' });

		// Write map Boundaries
		out.write(getWidth());
		out.write(getHeight());

		// Write tile data
		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getHeight(); y++)
			{
				out.write(tiles[x][y].getType().ordinal());
				out.write(tiles[x][y].getColor().ordinal());
				out.write(tiles[x][y].getTarget().ordinal());
			}
		}

		// Write starting points
		out.write(getStartingPointCount());
		for (int i = 0; i < startingPoints.length; i++)
		{
			if (startingPoints[i] == null)
				continue;

			out.write(startingPoints[i].getX());
			out.write(startingPoints[i].getY());
		}
		
		// Write endpoint
		out.write(endPoint.getX());
		out.write(endPoint.getY());
	
		// Write map validation string
		out.write(new byte[] { 'T', 'Q' });

		out.close();
	}

	public void loadFromArchive(String path) throws Exception
	{
		InputStream in = getClass().getClassLoader().getResourceAsStream(path);
		loadFromStream(in);
	}
	
	public void loadFromFile(String path) throws Exception
	{
		InputStream in = new FileInputStream(path);
		loadFromStream(in);
	}
	
	public void loadFromStream(InputStream in) throws Exception
	{
		byte[] validation_header = new byte[2];

		// Read file id
		in.read(validation_header);
		if (validation_header[0] != 'T' || validation_header[1] != 'Q')
		{
			in.close();
			throw new Exception("File is not a labyrinth, header failure");
		}
		
		// Read tile data
		int width = in.read();
		int height = in.read();
		
		if (width != Defaults.LabyrinthWidth || height != Defaults.LabyrinthHeight)
		{
			in.close();
			throw new Exception("The labyrinth boundaries do not match the defaults.");
		}
		
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				//type color target
				tiles[x][y] = new Tile(TileType.values()[in.read()], Color.values()[in.read()], Color.values()[in.read()]);
			}
		}
		
		// Read starting points
		int startingPointCount = in.read();
		if (startingPointCount < 0 || startingPointCount > 4)
		{
			in.close();
			throw new Exception("Labyrinth is corrupted. Starting point count is not between 1 and 4");
		}

		for (int i = 0; i < startingPointCount; i++)
			setStartingPoint(Color.values()[i], in.read(), in.read());
		
		//Read endpoint
		setEndPoint(in.read(), in.read());

		// Validate to be sure the correct amount of bytes have been read
		in.read(validation_header);
		if (validation_header[0] != 'T' || validation_header[1] != 'Q')
		{
			in.close();
			throw new Exception("File is not a labyrinth, validation failure");
		}

		in.close();
		
		if (!isValid())
			throw new Exception("Tilemap is not valid!");
		
		createPassPointList();
	}

	public static Tilemap makeTestMap()
	{
		Tilemap map = new Tilemap();
		map.setStartingPoint(Color.COLOR0, 0, 0);
		map.setStartingPoint(Color.COLOR1, 0, map.getHeight() - 1);
		map.setEndPoint(map.getWidth() - 1, map.getHeight() - 1);

		return map;
	}

	public void clearColors()
	{
		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getHeight(); y++)
				this.setTileColorForce(x, y, Color.NONE);
		}
	}
}