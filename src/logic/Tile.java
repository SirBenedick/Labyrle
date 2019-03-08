package logic;

public class Tile
{
	TileType type;
	Color color;
	Color target;
	
	public Tile()
	{
		type = TileType.FREE;
		color = Color.NONE;
		target = Color.NONE;
	}
}
