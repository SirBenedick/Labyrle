package gui;

import gfx.Manager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Core extends Application
{
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		new GameWindow().show();
	}

	//TODO ERROR HANDLING
	public static void main(String[] args) throws Exception
	{
		Manager.loadImages();
		launch(args);
	}
}
