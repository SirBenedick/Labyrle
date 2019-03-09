package gui;

import gui.utility.ConnectionMatrix;
import gui.utility.ConnectionType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.Tilemap;
import logic.utility.Tile;
import logic.utility.TileInformation;
import logic.utility.TileType;

public class TilemapRenderer extends Canvas 
{
	private GraphicsContext context;
	private Tilemap map;
	private ConnectionType[][] typeMatrix;
	private boolean showGrid;
	
	private int selectedTileX, oldX;
	private int selectedTileY, oldY;

	public TilemapRenderer(int width, int height, Tilemap map)
	{
		super(width, height);
		
		context = getGraphicsContext2D();
		showGrid = false;
		
		selectedTileX = 0;
		oldX = 0;
		selectedTileY = 0;
		oldY = 0;
		
		setTilemap(map);
		
		this.addEventHandler(MouseEvent.MOUSE_MOVED, (MouseEvent e) ->
		{
			handleMove(e, false);
		});
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e) ->
		{
			handleMove(e, false);
		});
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent e) ->
		{
			handleMove(e, true);
		});
	}
	
	public void setTilemap(Tilemap map)
	{
		this.map = map;
		resolveAllConnectionTypes();
		drawMap();
	}
	
	public ConnectionType getConnectionType(int x, int y)
	{
		return typeMatrix[x][y];
	}
	
	private void handleMove(MouseEvent e, boolean forceRedrawOnValidCoordinates)
	{
		selectedTileX = (int)e.getX() / Defaults.TileSize;
		selectedTileY = (int)e.getY() / Defaults.TileSize;
		
		//There is no change
		if (oldX == selectedTileX && oldY == selectedTileY)
		{
			if (!forceRedrawOnValidCoordinates)
				return;
		}
		//We are out of boundaries
		if (
				(selectedTileX < 0 || selectedTileX >= map.getWidth())
				||
				(selectedTileY < 0 || selectedTileY >= map.getHeight())
			)
		{
			selectedTileX = oldX;
			selectedTileY = oldY;
			return;
		}

		//Reaching this points means we have valid coords
		oldX = selectedTileX;
		oldY = selectedTileY;
	
		drawMap();
	}
	
	public Tilemap getMap()
	{
		return map;
	}
	
	public Tile getSelectedTile()
	{
		return map.getTiles()[selectedTileX][selectedTileY];
	}
	
	public int getSelectedX()
	{
		return selectedTileX;
	}
	
	public int getSelectedY()
	{
		return selectedTileY;
	}
	
	public void setGridEnabled(boolean value)
	{
		showGrid = value;
	}
	
	public void resolveConnectionType(int x, int y)
	{
		typeMatrix[x][y] = ConnectionMatrix.getConnectionType(x, y, map);
	}
	
	public void resolveAllConnectionTypes()
	{
		typeMatrix = new ConnectionType[map.getWidth()][map.getHeight()];
		for (int x = 0; x < map.getWidth(); x++)
		{
			for (int y = 0; y < map.getHeight(); y++)
				resolveConnectionType(x, y);
		}
	}
	
	private void drawArea(int startX, int startY, int width, int height)
	{
		int endX = startX+width;
		int endY = startY+height;
		
		for (int x = startX; x < endX; x++)
		{
			if (x < 0 || x >= map.getWidth())
				continue;
			
			for (int y = startY; y < endY; y++)
			{
				if (y < 0 || y >= map.getHeight())
					continue;
				
				Tile t = map.getTiles()[x][y];
				int absx = x*Defaults.TileSize;
				int absy = y*Defaults.TileSize;
				if (t.getType() == TileType.WALL)
				{
					switch(typeMatrix[x][y])
					{
					case HORIZONTAL:
						context.drawImage(gfx.Manager.getWallLineHorizontal(), absx, absy, Defaults.TileSize, Defaults.TileSize);
						break;
					case VERTICAL:
						context.drawImage(gfx.Manager.getWallLineVertical(), absx, absy, Defaults.TileSize, Defaults.TileSize);
						break;
					case JUNCTION:
						context.drawImage(gfx.Manager.getWallJunction(), absx, absy, Defaults.TileSize, Defaults.TileSize);
						break;
					case LEFT_TO_BOTTOM:
						context.drawImage(gfx.Manager.getWallTurnBottomLeft(), absx, absy, Defaults.TileSize, Defaults.TileSize);
						break;
					case LEFT_TO_TOP:
						context.drawImage(gfx.Manager.getWallTurnTopLeft(), absx, absy, Defaults.TileSize, Defaults.TileSize);
						break;
					case NONE:
						context.drawImage(gfx.Manager.getWallNone(), absx, absy, Defaults.TileSize, Defaults.TileSize);
						break;
					case RIGHT_TO_BOTTOM:
						context.drawImage(gfx.Manager.getWallTurnBottomRight(), absx, absy, Defaults.TileSize, Defaults.TileSize);
						break;
					case RIGHT_TO_TOP:
						context.drawImage(gfx.Manager.getWallTurnTopRight(), absx, absy, Defaults.TileSize, Defaults.TileSize);
						break;
					case T_JUNCTION_BOTTOM:
						context.drawImage(gfx.Manager.getWallTJunctionBottom(), absx, absy, Defaults.TileSize, Defaults.TileSize);
						break;
					case T_JUNCTION_LEFT:
						context.drawImage(gfx.Manager.getWallTJunctionLeft(), absx, absy, Defaults.TileSize, Defaults.TileSize);
						break;
					case T_JUNCTION_RIGHT:
						context.drawImage(gfx.Manager.getWallTJunctionRight(), absx, absy, Defaults.TileSize, Defaults.TileSize);
						break;
					case T_JUNCTION_TOP:
						context.drawImage(gfx.Manager.getWallTJunctionTop(), absx, absy, Defaults.TileSize, Defaults.TileSize);
						break;
					default:
						break;
					}
				}
				
				if (t.getTarget() != logic.utility.Color.NONE)
					context.drawImage(gfx.Manager.getPassPoint(t.getTarget()), absx, absy, Defaults.TileSize, Defaults.TileSize);
			}
		}
	}
	
	public void drawMap()
	{
		context.save();
		context.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		if (showGrid)
			drawGrid();
		
		drawArea(0, 0, map.getWidth(), map.getHeight());
		
		//Draw Endpoint
		TileInformation endPoint = map.getEndPoint();
		if (endPoint != null)
			context.drawImage(gfx.Manager.getEndPoint(), endPoint.getX()*Defaults.TileSize, endPoint.getY()*Defaults.TileSize, Defaults.TileSize, Defaults.TileSize);
		
		//Draw all starting points
		TileInformation[] startPoints = map.getStartPoints();
		for (int i = 0; i < startPoints.length; i++)
		{
			if (startPoints[i] == null)
				continue;
			
			context.drawImage(gfx.Manager.getStartPoint(startPoints[i].getTile().getColor()), startPoints[i].getX()*Defaults.TileSize, startPoints[i].getY()*Defaults.TileSize, Defaults.TileSize, Defaults.TileSize);
		}
		
		context.restore();
	}
	
	public void drawGrid()
	{
		context.save();
		for (int x = 0; x < map.getWidth(); x++)
		{
			for (int y = 0; y < map.getHeight(); y++)
			{
				Color c = Color.WHEAT;
				if (y%2 == 0)
					c = (x%2 == 0) ? Color.AZURE : Color.WHEAT;
				else
					c = (x%2 == 0) ? Color.WHEAT : Color.AZURE;
				
				
				context.setFill(Color.color(c.getRed(), c.getGreen(), c.getBlue(), 0.35));
				int xabs = x * Defaults.TileSize;
				int yabs = y * Defaults.TileSize;
				context.fillRect(xabs, yabs, Defaults.TileSize, Defaults.TileSize);
			}
		}
		
		context.setFill(Color.RED);
		context.setStroke(Color.RED);
		context.setLineWidth(1);
		context.strokeRect(selectedTileX*Defaults.TileSize, selectedTileY*Defaults.TileSize, Defaults.TileSize, Defaults.TileSize);
		context.restore();
	}
}


