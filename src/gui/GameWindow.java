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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
import logic.GameState;
import logic.Tilemap;

public class GameWindow extends Stage
{
	private void Exit() 
	{
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Spielabbruch");
 
        alert.setHeaderText("Es läuft momentan ein Spiel");
        alert.setContentText("Wollen Sie wirklich beenden?");
 
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
        	Platform.exit();
    }
	
	private GridPane rootLayout;
	private Scene mainScene;
	private TilemapRenderer renderer;
	private logic.utility.Color selectedColor;
	
	public GameWindow()
	{
		selectedColor = logic.utility.Color.COLOR0;
		rootLayout = new GridPane();
		Tilemap tmap = new Tilemap();
		try
		{
			tmap.loadFromFile("labyrinths/test.la");
		}catch(Exception e) {}
		renderer = new TilemapRenderer
				(
						Defaults.TILE_SIZE*logic.Defaults.LabyrinthWidth, 
						Defaults.TILE_SIZE*logic.Defaults.LabyrinthHeight, 
						tmap
				);	
		rootLayout.add(renderer, 0, 0);
		
		renderer.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
		(MouseEvent e) ->
		{
			applyPaint(e);
		});
		
		renderer.addEventHandler(MouseEvent.MOUSE_RELEASED, 
		(MouseEvent e) ->
		{
			if (e.getButton() == MouseButton.PRIMARY)
			{
				if (renderer.getMap().isStartPoint(renderer.getSelectedX(), renderer.getSelectedY()))
				{
					selectedColor = renderer.getSelectedTile().getColor();
					return;
				}
			}
			
			applyPaint(e);
			renderer.drawMap();
		});
		
		mainScene = new Scene(rootLayout, renderer.getWidth()+300, renderer.getHeight());
		this.setScene(mainScene);
		renderer.drawGrid();
		this.sizeToScene();
	}
	
	private void applyPaint(MouseEvent e)
	{
		if (e.getButton() == MouseButton.PRIMARY)
			renderer.getMap().setTileColor(renderer.getSelectedX(), renderer.getSelectedY(), selectedColor);
		else if(e.getButton() == MouseButton.SECONDARY)
			renderer.getMap().removeTileColor(renderer.getSelectedX(), renderer.getSelectedY());
		
		renderer.resolveAllConnectionTypes();
		renderer.drawMap();
	}
}


