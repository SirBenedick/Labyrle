package gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import gfx.Manager;
import gui.utility.ConnectionMatrix;
import gui.utility.ConnectionType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import logic.Settings;
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
	
	private int tileSize;
	
	private int selectedTileX, oldX;
	private int selectedTileY, oldY;
	
	private int absoluteCursorX, absoluteCursorY;
	
	private boolean isBlocked;
	
	private Settings settings;
	
	public TilemapRenderer(int width, int height, Tilemap map)
	{
		super(width, height);
		this.settings = new Settings();
		
		absoluteCursorY = 0;
		absoluteCursorX = 0;
		
		isBlocked = false;
		tileSize = Defaults.TILE_SIZE;
		
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
		
		drawMap();
	}
	
	public void setSettings(Settings value)
	{
		this.settings = value;
	}
	
	public Settings getSettings()
	{
		return this.settings;
	}
	
	public int getTileSize()
	{
		return tileSize;
	}
	
	public void setTileSize(int value)
	{
		tileSize = value;
		drawMap();
	}
	
	public boolean isBlocked()
	{
		return isBlocked;
	}
	
	public void setBlocked(boolean value)
	{
		isBlocked = value;
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
		if (isBlocked)
			return;
		
		absoluteCursorX = (int)e.getX();
		absoluteCursorY = (int)e.getY();
		
		selectedTileX = (int)e.getX() / tileSize;
		selectedTileY = (int)e.getY() / tileSize;
		
		//Since we renderer a mask depending on absolute cursor position.
		//There is no change
		/*if (oldX == selectedTileX && oldY == selectedTileY)
		{
			if (!forceRedrawOnValidCoordinates)
				return;
		}*/
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
	
		resolveAllConnectionTypes();
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
	
	private void drawArea(int startX, int startY, int width, int height, boolean drawColors)
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
				int absx = x*tileSize;
				int absy = y*tileSize;
				if (t.getType() == TileType.WALL && !drawColors)
				{
					switch(typeMatrix[x][y])
					{
					case HORIZONTAL:
						context.drawImage(gfx.Manager.getWallLineHorizontal(), absx, absy, tileSize, tileSize);
						break;
					case VERTICAL:
						context.drawImage(gfx.Manager.getWallLineVertical(), absx, absy, tileSize, tileSize);
						break;
					case JUNCTION:
						context.drawImage(gfx.Manager.getWallJunction(), absx, absy, tileSize, tileSize);
						break;
					case LEFT_TO_BOTTOM:
						context.drawImage(gfx.Manager.getWallTurnBottomLeft(), absx, absy, tileSize, tileSize);
						break;
					case LEFT_TO_TOP:
						context.drawImage(gfx.Manager.getWallTurnTopLeft(), absx, absy, tileSize, tileSize);
						break;
					case NONE:
						context.drawImage(gfx.Manager.getWallNone(), absx, absy, tileSize, tileSize);
						break;
					case RIGHT_TO_BOTTOM:
						context.drawImage(gfx.Manager.getWallTurnBottomRight(), absx, absy, tileSize, tileSize);
						break;
					case RIGHT_TO_TOP:
						context.drawImage(gfx.Manager.getWallTurnTopRight(), absx, absy, tileSize, tileSize);
						break;
					case T_JUNCTION_BOTTOM:
						context.drawImage(gfx.Manager.getWallTJunctionBottom(), absx, absy, tileSize, tileSize);
						break;
					case T_JUNCTION_LEFT:
						context.drawImage(gfx.Manager.getWallTJunctionLeft(), absx, absy, tileSize, tileSize);
						break;
					case T_JUNCTION_RIGHT:
						context.drawImage(gfx.Manager.getWallTJunctionRight(), absx, absy, tileSize, tileSize);
						break;
					case T_JUNCTION_TOP:
						context.drawImage(gfx.Manager.getWallTJunctionTop(), absx, absy, tileSize, tileSize);
						break;
					default:
						break;
					}
				}
				//Is of color and not an entity?
				else if (!map.isEntity(x, y) && t.getColor() != logic.utility.Color.NONE && drawColors)
				{
					logic.utility.Color color = t.getColor();
					switch(typeMatrix[x][y])
					{
					case HORIZONTAL:
						context.drawImage(gfx.Manager.getLineLineHorizontal(color), absx, absy, tileSize, tileSize);
						break;
					case VERTICAL:
						context.drawImage(gfx.Manager.getLineLineVertical(color), absx, absy, tileSize, tileSize);
						break;
					case JUNCTION:
						context.drawImage(gfx.Manager.getLineJunction(color), absx, absy, tileSize, tileSize);
						break;
					case LEFT_TO_BOTTOM:
						context.drawImage(gfx.Manager.getLineTurnBottomLeft(color), absx, absy, tileSize, tileSize);
						break;
					case LEFT_TO_TOP:
						context.drawImage(gfx.Manager.getLineTurnTopLeft(color), absx, absy, tileSize, tileSize);
						break;
					case NONE:
						context.drawImage(gfx.Manager.getLineNone(color), absx, absy, tileSize, tileSize);
						break;
					case RIGHT_TO_BOTTOM:
						context.drawImage(gfx.Manager.getLineTurnBottomRight(color), absx, absy, tileSize, tileSize);
						break;
					case RIGHT_TO_TOP:
						context.drawImage(gfx.Manager.getLineTurnTopRight(color), absx, absy, tileSize, tileSize);
						break;
					case T_JUNCTION_BOTTOM:
						context.drawImage(gfx.Manager.getLineTJunctionBottom(color), absx, absy, tileSize, tileSize);
						break;
					case T_JUNCTION_LEFT:
						context.drawImage(gfx.Manager.getLineTJunctionLeft(color), absx, absy, tileSize, tileSize);
						break;
					case T_JUNCTION_RIGHT:
						context.drawImage(gfx.Manager.getLineTJunctionRight(color), absx, absy, tileSize, tileSize);
						break;
					case T_JUNCTION_TOP:
						context.drawImage(gfx.Manager.getLineTJunctionTop(color), absx, absy, tileSize, tileSize);
						break;
					default:
						break;
					}
				}
				
				if (t.getTarget() != logic.utility.Color.NONE)
					context.drawImage(gfx.Manager.getPassPoint(t.getTarget()), absx, absy, tileSize, tileSize);
			}
		}
	}
	
	public void drawMap()
	{
		context.save();
		context.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		if (showGrid)
			drawGrid();
		
		drawArea(0, 0, map.getWidth(), map.getHeight(), false);
		
		//Draw Dark shadow

		//context.clearRect(0, 0, getWidth(), getHeight());
		int imgOffsetY = (int) (Manager.getFlare().getHeight()/2);
		int imgOffsetX = (int) (Manager.getFlare().getWidth()/2);
		
		if (settings.getShadowOpacity() >= 0.01f)
		{
			context.setGlobalAlpha(settings.getShadowOpacity());
			context.setFill(Defaults.SHADOW_COLOR);
			//top shade
			context.fillRect(0, 0, getWidth(), absoluteCursorY - imgOffsetY);
			
			//bottom shade
			context.fillRect(0, absoluteCursorY + imgOffsetY, getWidth(), getHeight() - absoluteCursorY + imgOffsetY);
			
			//left shade
			context.fillRect(0, absoluteCursorY - imgOffsetY, absoluteCursorX - imgOffsetX, Manager.getFlare().getHeight());
			
			//right shade
			context.fillRect(absoluteCursorX + imgOffsetX, absoluteCursorY - imgOffsetY, getWidth() - absoluteCursorX - imgOffsetY, Manager.getFlare().getHeight());
			
			context.drawImage(gfx.Manager.getFlare(), absoluteCursorX - imgOffsetX, absoluteCursorY - imgOffsetX);
		}
		context.restore();
		
		//Draw Endpoint
		TileInformation endPoint = map.getEndPoint();
		if (endPoint != null)
			context.drawImage(gfx.Manager.getEndPoint(), endPoint.getX()*tileSize, endPoint.getY()*tileSize, tileSize, tileSize);
		
		//Draw all starting points
		TileInformation[] startPoints = map.getStartPoints();
		for (int i = 0; i < startPoints.length; i++)
		{
			if (startPoints[i] == null)
				continue;
			
			context.drawImage(gfx.Manager.getStartPoint(startPoints[i].getTile().getColor()), startPoints[i].getX()*tileSize, startPoints[i].getY()*tileSize, tileSize, tileSize);
		}
		//Draw colors
		drawArea(0, 0, map.getWidth(), map.getHeight(), true);
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
				int xabs = x * tileSize;
				int yabs = y * tileSize;
				context.fillRect(xabs, yabs, tileSize, tileSize);
			}
		}
		
		context.setFill(Color.RED);
		context.setStroke(Color.RED);
		context.setLineWidth(1);
		context.strokeRect(selectedTileX*tileSize, selectedTileY*tileSize, tileSize, tileSize);
		context.restore();
	}
}


