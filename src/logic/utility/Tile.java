package logic.utility;

public class Tile
{
	private TileType type;
	private Color color;
	private Color target;
	
	public Tile()
	{
		type = TileType.FREE;
		color = Color.NONE;
		target = Color.NONE;
	}
	
	public Tile(TileType type, Color color, Color target)
	{
		this.type = type;
		this.color = color;
		this.target = target;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public void setTarget(Color color)
	{
		this.target = color;
	}
	
	public void setType(TileType type)
	{
		this.type = type;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public Color getTarget()
	{
		return target;
	}
	
	public TileType getType()
	{
		return type;
	}
}
