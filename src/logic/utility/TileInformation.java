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
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			 return false;
		
		if (obj == this)
			return true;
		
		if (!(obj instanceof TileInformation))
			return false;
		
		TileInformation tileInfo = (TileInformation) obj;
		
		if(!this.tile.equals(tileInfo.tile))
			return false;
		
		if(this.x != tileInfo.x)
			return false;
		
		if(this.y != tileInfo.y)
			return false;
		
		return true;
	}
	
	public String toString()
	{
		return this.getX() + " " + this.getY() + " " + this.getTile().getColor() + " " + this.getTile().getType() + " " + this.getTile().getTarget();
	}
}
