package logic;

public class Tilemap
{
	/**
	 * 	The actual tilemap. Access order is X/Y.
	 */
	private Tile tiles[][];
	
<<<<<<< Upstream, based on origin/master
	
	public Tilemap() 
	{
		tiles = new Tile[Defaults.LabyrinthWidth][Defaults.LabyrinthHeight];
		for (int x = 0; x < Defaults.LabyrinthWidth; x++)
		{
			for (int y = 0; y < Defaults.LabyrinthHeight; y++)
				tiles[x][y] = new Tile();
		}
	}
	
	public int getWidth()
	{
		return Defaults.LabyrinthWidth;
	}
	
	public int getHeight()
	{
		return Defaults.LabyrinthHeight;
	}
}
=======
	/**
	 * Holds all 4 starting points ordered by color ascending.
	 * Is exactly the size of 4.
	 * 
	 * Whenever the value of a field is null, the starting point is not used.
	 * Are all fields null, the map is invalid.
	 */
	private TileInformation[] startingPoints;
	
	/**
	 * The point all lines have to be connected with.
	 * If null the current map is invalid.
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
	
	public Tile[][] getTiles()
	{
		return tiles;
	}

	public void saveToFile(String path) throws Exception
	{
		
	}
	
	public void loadFromFile(String path) throws Exception
	{
		
	}
}
>>>>>>> ed13201 TileInformation und Enums angelegt
