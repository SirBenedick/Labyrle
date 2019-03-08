package logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import logic.utility.Color;
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

	public Tilemap()
	{
		startingPoints = new TileInformation[4];
		endPoint = null;

		tiles = new Tile[Defaults.LabyrinthWidth][Defaults.LabyrinthHeight];
		for (int x = 0; x < Defaults.LabyrinthWidth; x++)
		{
			for (int y = 0; y < Defaults.LabyrinthHeight; y++)
				tiles[x][y] = new Tile();
		}
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

		removeStartingPoint(color);

		tiles[x][y].setColor(color);
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

	public Tile[][] getTiles()
	{
		return tiles;
	}

	public void setEndPoint(int x, int y)
	{
		removeEndPoint();
		endPoint = new TileInformation(tiles[x][y], x, y);
	}

	public void removeEndPoint()
	{
		endPoint = null;
	}

	public void saveToFile(String path) throws Exception
	{
		if (!isValid())
			throw new Exception("Tilemap is not valid!");
		
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

		// Write map validation string
		out.write(new byte[] { 'T', 'Q' });

		out.close();
	}

	public void loadFromFile(String path) throws Exception
	{
		InputStream in = new FileInputStream(path);
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
	}

	public static Tilemap makeTestMap()
	{
		Tilemap map = new Tilemap();
		map.setStartingPoint(Color.COLOR0, 0, 0);
		map.setStartingPoint(Color.COLOR1, 0, map.getHeight() - 1);
		map.setEndPoint(map.getWidth() - 1, map.getHeight() - 1);

		return map;
	}
}