package gui;

import java.util.Optional;

import gfx.Manager;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logic.Tilemap;

public class GameWindow extends Stage
{
	private void ExitVictoryDebug() 
	{
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Sieg!");
 
        alert.setHeaderText("Du hast gewonnen");
        alert.setContentText("M�chtest du noch einmal spielen, yo?");
 
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
        	System.out.println("oh noes");
        else
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
		rootLayout.setBackground(new Background(Manager.getBackgroundImageGame()));
		
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
			if (e.getButton() == MouseButton.PRIMARY)
			{
				if (renderer.getMap().isStartPoint(renderer.getSelectedX(), renderer.getSelectedY()))
				{
					selectedColor = renderer.getSelectedTile().getColor();
					return;
				}
			}
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
		
		mainScene.setOnKeyPressed((KeyEvent e) ->
		{
			if (e.getCode() == KeyCode.ENTER)
				System.out.println(renderer.getMap().isSolved());
		});
		
		renderer.drawGrid();
		this.sizeToScene();
		//this.setWidth(825.0);
		//this.setHeight(650.0);
	}
	
	private void applyPaint(MouseEvent e)
	{
		if (e.getButton() == MouseButton.PRIMARY)
		{
			renderer.getMap().setTileColor(renderer.getSelectedX(), renderer.getSelectedY(), selectedColor);
			if (renderer.getMap().isSolved())
			{
				ExitVictoryDebug();
			}
		}
		else if(e.getButton() == MouseButton.SECONDARY)
			renderer.getMap().removeTileColor(renderer.getSelectedX(), renderer.getSelectedY());
		
		renderer.resolveAllConnectionTypes();
		renderer.drawMap();
	}
}


