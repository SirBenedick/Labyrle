package gui;

import java.util.Optional;

import gfx.Manager;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.GameState;

public class GameWindow extends Stage
{
	private void showVictoryDialog() 
	{
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Herzlichen Glückwunsch!");
 
        alert.setHeaderText("Du hast das Labyrinth gelöst");
        alert.setContentText("Möchtest du noch es einmal lösen?");
 
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
        {
        	window.renderer.getMap().clearColors();
			window.renderer.drawMap();
			//ToDo: LvlCounter ggf erhöhen
        }
        else
        {
        	//ToDo: lvlCounter ggf erhöhen
        	MainMenuWindow wd = new MainMenuWindow();
        	wd.setLevelSelectScene();
        	wd.show();
			window.close();
        }
    }
	
	private GameWindow window;
	private Pane rootLayout;
	private Scene mainScene;
	private TilemapRenderer renderer;
	private logic.utility.Color selectedColor;
	
	private static final double WINDOW_HEIGHT = 812.5;
	private static final double WINDOW_WIDTH = 1031.25;
	private static final double UNDO_BTN_OFFSET_X = 730;
	private static final double UNDO_BTN_OFFSET_Y = 410;
	private static final double UNDO_BTN_WIDTH = 100;
	private static final double UNDO_BTN_HIGHT = 75;
	private static final double EXIT_BTN_WIDTH = 200;
	private static final double EXIT_BTN_HIGHT = 106;
	private static final double EXIT_BTN_OFFSET_X = 750;
	private static final double EXIT_BTN_OFFSET_Y = 620;
	private static final double CLEAR_BTN_WIDTH = 200;
	private static final double CLEAR_BTN_HIGHT = 112;
	private static final double CLEAR_BTN_OFFSET_X = 750;
	private static final double CLEAR_BTN_OFFSET_Y = 500;
	
	private boolean spaceDown;
	
	public GameWindow()
	{
		this.window = this;
		spaceDown = false;
		selectedColor = logic.utility.Color.COLOR0;
		rootLayout = new Pane();
		rootLayout.setBackground(new Background(Manager.getBackgroundImageGame(GameState.getCurrentLevel())));
		
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
		this.getIcons().add(gfx.Manager.getIcon());
		this.setResizable(false);
		this.setWidth(WINDOW_WIDTH);
		this.setHeight(WINDOW_HEIGHT);
		
		Label undoBtn = new Label();
		undoBtn.setGraphic(new ImageView(gfx.Manager.getBackButton()));
		undoBtn.setMaxSize(UNDO_BTN_WIDTH, UNDO_BTN_HIGHT);
		undoBtn.setMinSize(UNDO_BTN_WIDTH, UNDO_BTN_HIGHT);
		undoBtn.setTranslateX(UNDO_BTN_OFFSET_X);
		undoBtn.setTranslateY(UNDO_BTN_OFFSET_Y);
		rootLayout.getChildren().add(undoBtn);
		
		undoBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(event.getButton() == MouseButton.PRIMARY)
				{
					//ToDo undoFunction call
				}
				
				event.consume();
			}
		});
		
		Label exitBtn = new Label();
		exitBtn.setGraphic(new ImageView(gfx.Manager.getExitButton()));
		exitBtn.setMaxSize(EXIT_BTN_WIDTH, EXIT_BTN_HIGHT);
		exitBtn.setMinSize(EXIT_BTN_WIDTH, EXIT_BTN_HIGHT);
		exitBtn.setTranslateX(EXIT_BTN_OFFSET_X);
		exitBtn.setTranslateY(EXIT_BTN_OFFSET_Y);
		rootLayout.getChildren().add(exitBtn);
		
		exitBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(event.getButton() == MouseButton.PRIMARY)
				{
					Alert exitAlert = new Alert(AlertType.CONFIRMATION);
			        exitAlert.setTitle("Level beenden?");
			 
			        exitAlert.setHeaderText("Du bist in einem laufenden Spiel");
			        exitAlert.setContentText("Sicher, dass du das Level beenden möchtest?\nJeglicher Fortschritt geht verloren.\n");
			 
			        Optional<ButtonType> result = exitAlert.showAndWait();
			        if(result.get() == ButtonType.OK)
			        {
						new MainMenuWindow().show();
						window.close();
			        }
				}
				
				event.consume();
			}
		});
		
		Label clearBtn = new Label();
		clearBtn.setGraphic(new ImageView(gfx.Manager.getClearButton()));
		clearBtn.setMaxSize(CLEAR_BTN_WIDTH, CLEAR_BTN_HIGHT);
		clearBtn.setMinSize(CLEAR_BTN_WIDTH, CLEAR_BTN_HIGHT);
		clearBtn.setTranslateX(CLEAR_BTN_OFFSET_X);
		clearBtn.setTranslateY(CLEAR_BTN_OFFSET_Y);
		rootLayout.getChildren().add(clearBtn);
		
		clearBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(event.getButton() == MouseButton.PRIMARY)
				{
					window.renderer.getMap().clearColors();
					window.renderer.drawMap();
				}
				
				event.consume();
			}
		});

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
				showVictoryDialog();
			}
		}
		else
			renderer.getMap().removeTileColor(renderer.getSelectedX(), renderer.getSelectedY());
		
		renderer.resolveAllConnectionTypes();
		renderer.drawMap();
	}
}


