package gui;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.Optional;

import gfx.Manager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Tilemap;

public class GameWindow extends Stage
{
	private void Exit() 
	{
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Spielabbruch");
 
        alert.setHeaderText(null);
        alert.setContentText("Wollen Sie wirklich beenden?");
 
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
        	Platform.exit();
        
    }
	
	private GridPane rootLayout;
	private Scene mainScene;
	private TilemapRenderer renderer;
	
	public GameWindow()
	{
		rootLayout = new GridPane();
		
		renderer = new TilemapRenderer(400, 400, new Tilemap());	
		rootLayout.add(renderer, 0, 0);
		
		mainScene = new Scene(rootLayout, 500, 500);
		this.setScene(mainScene);
		renderer.drawTest();
	}
}


