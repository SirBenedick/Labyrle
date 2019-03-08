package logic;

public class Tilemap
{
	/**
	 * 	The actual tilemap. Access order is X/Y.
	 */
	private Tile tiles[][];
	
	
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
