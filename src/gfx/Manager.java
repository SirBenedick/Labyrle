package gfx;

import javafx.scene.SnapshotParameters;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;

public class Manager 
{
	private static final int MAX_LEVEL_COUNT = 20;
	private static BackgroundImage[] backgroundImageGame;
	
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
	
	private static Image[] lineJunction;
	private static Image[] lineLineVertical;
	private static Image[] lineLineHorizontal;
	private static Image[] lineTJunctionRight;
	private static Image[] lineTJunctionLeft;
	private static Image[] lineTJunctionTop;
	private static Image[] lineTJunctionBottom;
	private static Image[] lineTurnTopRight;
	private static Image[] lineTurnTopLeft;
	private static Image[] lineTurnBottomRight;
	private static Image[] lineTurnBottomLeft;
	private static Image[] lineNone;
	
	private static Background manualBackground[];
	
	private static Image flare;
	private static Image icon;
	private static Image backButton;
	private static Image mainMenuBackground;
	private static Image levelSelectBackground;
	private static Image endPoint;
	private static Image switchOn, switchOff;

	//Multi Color Images
	private static Image[] passPoint;
	private static Image[] startPoint;
	
	public static void loadImages() throws Exception
	{
		backgroundImageGame = new BackgroundImage[MAX_LEVEL_COUNT];
		
		for(int i = 0; i < MAX_LEVEL_COUNT; i++)
		{
			backgroundImageGame[i] = new BackgroundImage
			( 
				new Image("gfx/background_game_" + (i + 1) + ".png"), 
				BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, 
				BackgroundPosition.CENTER, 
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
			);
		}
		
		mainMenuBackground = new Image("gfx/Menu_ohne_Knopf.png");
		backButton = new Image("gfx/back_to_main.png");
		
		manualBackground = new Background[5];
		for(int i = 0; i < manualBackground.length; i++)
		{
			manualBackground[i] = new Background(new BackgroundImage(
					new Image("gfx/Manual_" + i + ".png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, 
					new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true))); 
		}
		
		wallJunction = colorize(new Image("gfx/wall_cross.png"), gui.Defaults.WALL_COLOR);
		wallLineVertical = colorize(new Image("gfx/wall_line.png"), gui.Defaults.WALL_COLOR);
		wallTJunctionRight = colorize(new Image("gfx/wall_tjunction.png"), gui.Defaults.WALL_COLOR);
		wallTurnTopRight = colorize(new Image("gfx/wall_turn.png"), gui.Defaults.WALL_COLOR);
		wallNone = colorize(new Image("gfx/wall_none.png"), gui.Defaults.WALL_COLOR);
		
		levelSelectBackground = new Image("gfx/LevelSelect.png");
		
		flare = colorize(new Image("gfx/flare.png"), gui.Defaults.SHADOW_COLOR);
		icon = new Image("gfx/icon.png");
		switchOn = new Image("gfx/switchOn.png");
		switchOff = new Image("gfx/switchOff.png");
		
		endPoint = new Image("gfx/endpoint.png");
		
		//Create colorized variations
		Image passpoint_temp = new Image("gfx/passpoint.png");
		Image startpoint_temp = new Image("gfx/startpoint.png");
		passPoint = new Image[4];
		startPoint = new Image[4];
		
		lineJunction = new Image[4];
		lineLineVertical = new Image[4];
		lineLineHorizontal = new Image[4];
		lineTJunctionRight = new Image[4];
		lineTJunctionLeft = new Image[4];
		lineTJunctionTop = new Image[4];
		lineTJunctionBottom = new Image[4];
		lineTurnTopRight = new Image[4];
		lineTurnTopLeft = new Image[4];
		lineTurnBottomRight = new Image[4];
		lineTurnBottomLeft = new Image[4];
		lineNone = new Image[4];
		
		for (int i = 0; i < 4; i++)
		{
			passPoint[i] = colorize(passpoint_temp, gui.Defaults.COLOR_TABLE[i]);
			startPoint[i] = colorize(startpoint_temp, gui.Defaults.COLOR_TABLE[i]);
			lineJunction[i] = colorize(new Image("gfx/line_cross.png") ,gui.Defaults.COLOR_TABLE[i]);
			lineLineVertical[i] = colorize(new Image("gfx/line_line.png") ,gui.Defaults.COLOR_TABLE[i]);
			lineTJunctionRight[i] = colorize(new Image("gfx/line_tjunction.png") ,gui.Defaults.COLOR_TABLE[i]);
			lineTurnTopRight[i] = colorize(new Image("gfx/line_turn.png") ,gui.Defaults.COLOR_TABLE[i]);
			lineNone[i] = colorize(new Image("gfx/line_none.png") ,gui.Defaults.COLOR_TABLE[i]);
			
			lineLineHorizontal[i] = rotate(lineLineVertical[i], 90);
			
			lineTJunctionLeft[i] = rotate(lineTJunctionRight[i], 180);
			lineTJunctionTop[i] = rotate(lineTJunctionRight[i], -90);
			lineTJunctionBottom[i] = rotate(lineTJunctionRight[i], 90);
			
			lineTurnTopLeft[i] = rotate(lineTurnTopRight[i], -90);
			lineTurnBottomRight[i] = rotate(lineTurnTopRight[i], 90);
			lineTurnBottomLeft[i] = rotate(lineTurnTopRight[i], 180);
		}
		
		//Wall Variations
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
	
	public static Image getBackButton()
	{
		return backButton;
	}
	
	public static Background getManualPage(int index)
	{
		return manualBackground[index];
	}
	
	public static Image getLevelSelectBackground()
	{
		return levelSelectBackground;
	}
	
	public static int getManualPageCount()
	{
		return manualBackground.length;
	}
	
	public static Image getSwitchOn()
	{
		return switchOn;
	}
	
	public static Image getSwitchOff()
	{
		return switchOff;
	}
	
	public static Image getStartPoint(logic.utility.Color color)
	{
		return startPoint[color.ordinal()];
	}
	
	public static Image getIcon()
	{
		return icon;
	}
	
	public static Image getMainMenuBackground()
	{
		return mainMenuBackground;
	}
	
	public static Image getFlare()
	{
		return flare;
	}
	
	public static Image getPassPoint(logic.utility.Color color)
	{
		return passPoint[color.ordinal()];
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
	
	public static BackgroundImage getBackgroundImageGame(int index)
	{
		return backgroundImageGame[index];
	}
	
	public static Image getEndPoint()
	{
		return endPoint;
	}
	
	/*
	 * WALLS
	 */
	
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
	
	/*
	 * LINES
	 */
	public static Image getLineTurnTopLeft(logic.utility.Color color)
	{
		return lineTurnTopLeft[color.ordinal()];
	}
	
	public static Image getLineTurnTopRight(logic.utility.Color color)
	{
		return lineTurnTopRight[color.ordinal()];
	}
	
	public static Image getLineTurnBottomLeft(logic.utility.Color color)
	{
		return lineTurnBottomLeft[color.ordinal()];
	}
	
	public static Image getLineTurnBottomRight(logic.utility.Color color)
	{
		return lineTurnBottomRight[color.ordinal()];
	}

	public static Image getLineJunction(logic.utility.Color color)
	{
		return lineJunction[color.ordinal()];
	}
	
	public static Image getLineLineVertical(logic.utility.Color color)
	{
		return lineLineVertical[color.ordinal()];
	}
	
	public static Image getLineLineHorizontal(logic.utility.Color color)
	{
		return lineLineHorizontal[color.ordinal()];
	}
	
	public static Image getLineTJunctionRight(logic.utility.Color color)
	{
		return lineTJunctionRight[color.ordinal()];
	}
	
	public static Image getLineTJunctionTop(logic.utility.Color color)
	{
		return lineTJunctionTop[color.ordinal()];
	}
	
	public static Image getLineTJunctionLeft(logic.utility.Color color)
	{
		return lineTJunctionLeft[color.ordinal()];
	}
	
	public static Image getLineTJunctionBottom(logic.utility.Color color)
	{
		return lineTJunctionBottom[color.ordinal()];
	}
	
	public static Image getLineTurn(logic.utility.Color color)
	{
		return lineTurnTopRight[color.ordinal()];
	}
	
	public static Image getLineNone(logic.utility.Color color)
	{
		return lineNone[color.ordinal()];
	}
}
