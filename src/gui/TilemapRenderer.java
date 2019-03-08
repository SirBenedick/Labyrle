package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import logic.Tilemap;

public class TilemapRenderer extends Canvas 
{
	private GraphicsContext context;
	private Tilemap map;
	public TilemapRenderer(int width, int height, Tilemap map)
	{
		super(width, height);
		this.map = map;
		context = getGraphicsContext2D();
	}
	
	public void drawTest()
	{
		context.setFill(Color.GREEN);
		context.fillRect(0, 0, this.getWidth(), this.getHeight());
		System.out.println(this.getWidth());
	}
}


