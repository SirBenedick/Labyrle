package gui;

import javafx.scene.paint.Color;

public class Defaults 
{
	public static final int TILE_SIZE = 32;
	
	public static Color[] COLOR_TABLE = new Color[]
	{
		Color.web("#227065"),
		Color.web("#66615d"),
		Color.web("#702237"),
		Color.web("#647022")
	};
	
	public static final Color WALL_COLOR = Color.BLACK;
	public static final Color SHADOW_COLOR = Color.rgb(219, 219, 219);
	
	public static final int REVELATION_TIME_MS = 1500;
	public static final int REVELATION_FADE_OUT_MS = 1000;
	public static final int REVELATION_FADE_OUT_STEPS = 50;
}
