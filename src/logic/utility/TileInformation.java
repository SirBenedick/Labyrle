package logic.utility;

public class TileInformation
{
	private Tile tile;
	private int x, y;
	
	public TileInformation(Tile tile, int x, int y)
	{
		this.x = x; this.y = y; this.tile = tile;
	}
	
	public Tile getTile()
	{
		return tile;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}
