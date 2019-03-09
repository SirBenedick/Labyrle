package gui;

import java.util.Arrays;
import java.util.List;

import gfx.Manager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Core extends Application
{
	private static List<String> programArguments;
	
	private void dialogMsg(String title, String text, String header, AlertType type)
	{
		Alert err = new Alert(type);
		err.setTitle(title);
		err.setHeaderText(header);
		err.setContentText(text);
		err.showAndWait();
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		boolean run_editor = false;
		
		if (programArguments.size() > 0)
		{
			//Check if we should restore our system
			if (programArguments.contains("-setup"))
			{
				logic.GameState state = new logic.GameState();
				try
				{
					state.restoreSystem();
					dialogMsg("Setup done", "yeah we did it. The system is running111!11!", null, AlertType.INFORMATION);
				}
				catch(Exception e)
				{
					dialogMsg("Error", e.getMessage(), "Ohh noes, something went wrong", AlertType.ERROR);
					Platform.exit();
					return;
				}
			}
			
			//Check if we should run in editor mode
			if (programArguments.contains("-editor"))
				run_editor = true;
		}
		
		try
		{
			Manager.loadImages();
		}
		catch(Exception e)
		{
			dialogMsg("Error", e.getMessage(), "Loading the graphics failed", AlertType.ERROR);
			Platform.exit();
			return;
		}
		
		if (run_editor)
			new MapEditorWindow().show();
		else
			new MainMenuWindow().show();
	}

	//TODO ERROR HANDLING
	public static void main(String[] argv)
	{
		programArguments = Arrays.asList(argv);
		launch(argv);
	}
}
