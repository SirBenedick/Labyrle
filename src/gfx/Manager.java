package gfx;

import javafx.scene.SnapshotParameters;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;

public class Manager 
{
	private static BackgroundImage backgroundImageGame;
	private static Image wallJunction;
	private static Image wallLineVertical;
	private static Image wallLineHorizontal;
	private static Image wallTJunctionRight;
	private static Image wallTJunctionLeft;
	private static Image wallTJunctionTop;
	private static Image wallTJunctionBottom;
	private static Image wallTurnTopRight;
	private static Image wallTurnTopLeft;
	private static Image wallTurnBottomRight;
	private static Image wallTurnBottomLeft;
	private static Image wallNone;
	private static Image endPoint;

	//Multi Color Images
	private static Image passPoint;
	private static Image startPoint;
	
	public static void loadImages() throws Exception
	{
		backgroundImageGame = new BackgroundImage( new Image("gfx/background_game.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		wallJunction = new Image("gfx/wall_cross.png");
		wallLineVertical = new Image("gfx/wall_line.png");
		wallTJunctionRight = new Image("gfx/wall_tjunction.png");
		wallTurnTopRight = new Image("gfx/wall_turn.png");
		wallNone = new Image("gfx/wall_none.png");
		passPoint = new Image("gfx/passpoint.png");
		endPoint = new Image("gfx/endpoint.png");
		
		startPoint = new Image("gfx/startpoint.png");
		
		//Variations
		wallLineHorizontal = rotate(wallLineVertical, 90);
		
		wallTJunctionLeft = rotate(wallTJunctionRight, 180);
		wallTJunctionBottom = rotate(wallTJunctionRight, 90);
		wallTJunctionTop = rotate(wallTJunctionRight, -90);
		
		wallTurnTopLeft = rotate(wallTurnTopRight, -90);
		wallTurnBottomRight = rotate(wallTurnTopRight, 90);
		wallTurnBottomLeft = rotate(wallTurnTopRight, 180);
	}
	
	private static Image rotate(Image img, double rotation)
	{
		ImageView worker = new ImageView(img);
		worker.setRotate(rotation);
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		return worker.snapshot(params, null);
	}
	
	public static Image getStartPoint(logic.utility.Color color)
	{
		return colorize(startPoint, gui.Defaults.COLOR_TABLE[color.ordinal()]);
	}
	
	public static Image getPassPoint(logic.utility.Color color)
	{
		return colorize(passPoint, gui.Defaults.COLOR_TABLE[color.ordinal()]);
	}
	
	private static Image colorize(Image image, Color color)
	{
		ImageView worker = new ImageView(image);
		worker.setClip(new ImageView(image));
		
		ColorAdjust monochrome = new ColorAdjust();
		monochrome.setSaturation(-1.0);
		
		Blend blend = new Blend
				(
						BlendMode.MULTIPLY, monochrome, 
						new ColorInput(0, 0, worker.getImage().getWidth(), worker.getImage().getHeight(),
								color)
				);
		
		worker.setEffect(blend);
		
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		return worker.snapshot(params, null);
	}
	
	public static BackgroundImage getBackgroundImageGame()
	{
		return backgroundImageGame;
	}
	
	public static Image getEndPoint()
	{
		return endPoint;
	}
	
	public static Image getWallTurnTopLeft()
	{
		return wallTurnTopLeft;
	}
	
	public static Image getWallTurnTopRight()
	{
		return wallTurnTopRight;
	}
	
	public static Image getWallTurnBottomLeft()
	{
		return wallTurnBottomLeft;
	}
	
	public static Image getWallTurnBottomRight()
	{
		return wallTurnBottomRight;
	}

	public static Image getWallJunction()
	{
		return wallJunction;
	}
	
	public static Image getWallLineVertical()
	{
		return wallLineVertical;
	}
	
	public static Image getWallLineHorizontal()
	{
		return wallLineHorizontal;
	}
	
	public static Image getWallTJunctionRight()
	{
		return wallTJunctionRight;
	}
	
	public static Image getWallTJunctionTop()
	{
		return wallTJunctionTop;
	}
	
	public static Image getWallTJunctionLeft()
	{
		return wallTJunctionLeft;
	}
	
	public static Image getWallTJunctionBottom()
	{
		return wallTJunctionBottom;
	}
	
	public static Image getWallTurn()
	{
		return wallTurnTopRight;
	}
	
	public static Image getWallNone()
	{
		return wallNone;
	}
}
