package gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class CoreTest extends Application
{
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage = new MainMenuWindow();
		primaryStage.show();
	}

	//TODO ERROR HANDLING
	public static void main(String[] args) throws Exception
	{
		launch(args);
	}
}
