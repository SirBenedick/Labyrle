package gfx;

import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class Manager 
{
	private static BackgroundImage backgroundImageGame;
	
	public static void loadImages() throws Exception
	{
		backgroundImageGame = new BackgroundImage( new Image("gfx/background_game.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
	}
	
	public static BackgroundImage getBackgroundImageGame()
	{
		return backgroundImageGame;
	}
}
