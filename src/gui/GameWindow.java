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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.GameState;
import logic.Tilemap;

public class GameWindow extends Stage
{
	private void ExitVictoryDebug() 
	{
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Sieg!");
 
        alert.setHeaderText("Du hast gewonnen");
        alert.setContentText("Möchtest du noch einmal spielen, yo?");
 
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
        	System.out.println("oh noes");
        else
        	Platform.exit();
    }
	
	private Pane rootLayout;
	private Scene mainScene;
	private TilemapRenderer renderer;
	private logic.utility.Color selectedColor;
	
	private boolean spaceDown;
	public GameWindow()
	{
		spaceDown = false;
		selectedColor = logic.utility.Color.COLOR0;
		rootLayout = new Pane();
		rootLayout.setBackground(new Background(Manager.getBackgroundImageGame()));
		
		renderer = new TilemapRenderer
		(
			Defaults.TILE_SIZE*logic.Defaults.LabyrinthWidth, 
			Defaults.TILE_SIZE*logic.Defaults.LabyrinthHeight, 
			GameState.getMap(GameState.getCurrentLevel())
		);
		
		renderer.getSettings().setShadowOpacity(GameState.getSettings().getShadowOpacity());
		renderer.setTranslateX(50);
		renderer.setTranslateY(125);
		rootLayout.getChildren().add(renderer);
		
		renderer.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
		(MouseEvent e) ->
		{
			renderer.setRenderSelectionHL(false);
			applyPaint(e.getButton() == MouseButton.PRIMARY);
			renderer.drawMap();
		});
		
		renderer.addEventHandler(MouseEvent.MOUSE_RELEASED, 
		(MouseEvent e) ->
		{
			renderer.setRenderSelectionHL(false);
			applyPaint(e.getButton() == MouseButton.PRIMARY);
			renderer.drawMap();
		});
		
		renderer.addEventHandler(MouseEvent.MOUSE_MOVED, 
		(MouseEvent e) ->
		{
			renderer.setRenderSelectionHL(false);
		});
		
		mainScene = new Scene(rootLayout, renderer.getWidth()+300, renderer.getHeight());
		this.setScene(mainScene);
		
		mainScene.setOnKeyReleased((KeyEvent e) ->
		{			
			if (e.getCode() == KeyCode.UP)
				applyOffsetToSelection(0, -1);
			else if(e.getCode() == KeyCode.DOWN)
				applyOffsetToSelection(0, 1);
			else if(e.getCode() == KeyCode.LEFT)
				applyOffsetToSelection(-1, 0);
			else if(e.getCode() == KeyCode.RIGHT)
				applyOffsetToSelection(1, 0);
			
			if (spaceDown)
				applyPaint(true);
			else if(e.isShiftDown())
				applyPaint(false);
				
				
			if(e.getCode() == KeyCode.SPACE)
				spaceDown = false;
			
			e.consume();
		});
		
		mainScene.setOnKeyPressed((KeyEvent e) ->
		{
			if (e.getCode() == KeyCode.SPACE)
			{
				applyPaint(true);
				spaceDown = true;
			}
			else if(e.getCode() == KeyCode.SHIFT)
				applyPaint(false);
		});
		
		renderer.drawGrid();
		this.setResizable(false);
		this.setWidth(1031.25);
		this.setHeight(812.5);
	}
	
	private void applyOffsetToSelection(int offsetX, int offsetY)
	{
		renderer.setRenderSelectionHL(true);
		renderer.setSelectedTile(renderer.getSelectedX()+offsetX, renderer.getSelectedY()+offsetY);
	}
	
	private void applyPaint(boolean isPrimaryKey)
	{
		if (isPrimaryKey)
		{
			if (renderer.getMap().isStartPoint(renderer.getSelectedX(), renderer.getSelectedY()))
			{
				selectedColor = renderer.getSelectedTile().getColor();
				return;
			}
			
			renderer.getMap().setTileColor(renderer.getSelectedX(), renderer.getSelectedY(), selectedColor);
			if (renderer.getMap().isSolved())
			{
				ExitVictoryDebug();
			}
		}
		else
			renderer.getMap().removeTileColor(renderer.getSelectedX(), renderer.getSelectedY());
		
		renderer.resolveAllConnectionTypes();
		renderer.drawMap();
	}
}


