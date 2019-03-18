package gui;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Defaults;
import logic.GameState;
import logic.Tilemap;

public class MainMenuWindow extends Stage
{
	private static final double LVL_OFFSET_X = 60;
	private static final double LVL_OFFSET_Y = 120;
	private static final double BACK_BTN_OFFSET_X = 690;
	private static final double BACK_BTN_OFFSET_Y = 510;
	private static final double BACK_BTN_WIDTH = 100;
	private static final double BACK_BTN_HIGHT = 75;
	private static final double SWITCH_OFFSET_X = 550;
	private static final double SWITCH_OFFSET_Y = 350;
	private static final double SWITCH_WIDTH = 150;
	private static final double SWITCH_HIGHT = 150;
	private static final double WINDOW_HEIGHT = 650;
	private static final double WINDOW_WIDTH = 825;
	private static final double START_MIN_X = 140;
	private static final double START_MAX_X = 280;
	private static final double START_MIN_Y = 350;
	private static final double START_MAX_Y = 425;
	private static final double MANUAL_MIN_X = 315;
	private static final double MANUAL_MAX_X = 520;
	private static final double MANUAL_MIN_Y = 360;
	private static final double MANUAL_MAX_Y = 430;
	private static final int TILE_SIZE = 4;
	private static final int LVL_MARGIN = 100;
	private static final int LVL_COLUMN_COUNT = 5;
	private static final int LVL_ROW_COUNT = 4;
	
	private boolean switchOn = true;
	private MainMenuWindow window;
	
	private Scene sceneMenu;
	
	private Scene sceneManual;
	private int manualIndex;
	
	private Pane manualContentPane;
	
	private Scene sceneLevelSelect;
	
	public MainMenuWindow()
	{
		window = this;
		this.setHeight(WINDOW_HEIGHT);
		this.setWidth(WINDOW_WIDTH);
		this.setTitle("Labyrle");
		this.setResizable(false);
		this.getIcons().add(gfx.Manager.getIcon());
		initializeMenuScene();
		initializeManualScene();
		initializeLevelSelectScene();
		this.setScene(sceneMenu);
	}
	
	public void setLevelSelectScene()
	{
		this.setScene(sceneLevelSelect);
	}
	
	private void initializeMenuScene()
	{
		Pane menuContentPane = new Pane();
		this.sceneMenu = new Scene(menuContentPane);
		Background bg = new Background(new BackgroundImage(
				gfx.Manager.getMainMenuBackground(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, 
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)));
		
		menuContentPane.setBackground(bg);
		Label switchButton = new Label();
		
		
		switchButton.setGraphic(new ImageView(gfx.Manager.getSwitchOn()));
		GameState.getSettings().setShadowOpacity(logic.Defaults.SHADOW_OPACITY);
		
		
		switchButton.setMinSize(SWITCH_WIDTH, SWITCH_HIGHT);
		switchButton.setMaxSize(SWITCH_WIDTH, SWITCH_HIGHT);
		switchButton.setTranslateX(SWITCH_OFFSET_X);
		switchButton.setTranslateY(SWITCH_OFFSET_Y);
		
		switchButton.addEventHandler(MouseEvent.MOUSE_CLICKED,  new EventHandler<MouseEvent>()
		{
            public void handle(MouseEvent event)
            {
            	if (event.getButton() != MouseButton.PRIMARY)
            	{
            		event.consume();
            		return;
            	}
            	
            	if(switchOn)
            	{
            		switchButton.setGraphic(new ImageView(gfx.Manager.getSwitchOff()));
            		GameState.getSettings().setShadowOpacity(0.0f);
            		switchOn = false;
            	}
            	else
            	{
            		switchButton.setGraphic(new ImageView(gfx.Manager.getSwitchOn()));
            		GameState.getSettings().setShadowOpacity(Defaults.SHADOW_OPACITY);
            		switchOn = true;
            	}
            	event.consume();
            };
        });
		
		menuContentPane.getChildren().add(switchButton);
		
		menuContentPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
            public void handle(MouseEvent event)
            {
            	if (event.getButton() != MouseButton.PRIMARY)
            	{
            		event.consume();
            		return;
            	}
            	
            	if(event.getX() >= START_MIN_X && event.getX() <= START_MAX_X && event.getY() >= START_MIN_Y && event.getY() <= START_MAX_Y)
            		window.setScene(window.sceneLevelSelect);
            	else if(event.getX() >= MANUAL_MIN_X && event.getX() <= MANUAL_MAX_X && event.getY() >= MANUAL_MIN_Y && event.getY() <= MANUAL_MAX_Y)
            		window.setScene(window.sceneManual);
            	
            	event.consume();
            };
        });
	}
	
	private void initializeManualScene()
	{
		this.manualIndex = 0;
		manualContentPane = new Pane();
		this.sceneManual = new Scene(manualContentPane);
		manualContentPane.setBackground(gfx.Manager.getManualPage(0));
		
		manualContentPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{	
				if(event.getButton() == MouseButton.PRIMARY)
					window.manualIndex++;
				else if(event.getButton() == MouseButton.SECONDARY)
					window.manualIndex--;
				
				if(window.manualIndex < 0 || window.manualIndex >= gfx.Manager.getManualPageCount())
				{
					window.manualIndex = 0;
					window.manualContentPane.setBackground(gfx.Manager.getManualPage(window.manualIndex));
					window.setScene(window.sceneMenu);
				}
				else 
					window.manualContentPane.setBackground(gfx.Manager.getManualPage(window.manualIndex));
				
				event.consume();
			}
		});
	}
	
	private void initializeLevelSelectScene()
	{
		Pane levelSelectPane = new Pane();
		this.sceneLevelSelect = new Scene(levelSelectPane);
		levelSelectPane.setBackground(new Background(new BackgroundImage(
				gfx.Manager.getLevelSelectBackground(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, 
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true))));
		
		Label backBtn = new Label();
		backBtn.setGraphic(new ImageView(gfx.Manager.getBackButton()));
		backBtn.setMaxSize(BACK_BTN_WIDTH, BACK_BTN_HIGHT);
		backBtn.setMinSize(BACK_BTN_WIDTH, BACK_BTN_HIGHT);
		backBtn.setTranslateX(BACK_BTN_OFFSET_X);
		backBtn.setTranslateY(BACK_BTN_OFFSET_Y);
		levelSelectPane.getChildren().add(backBtn);
		
		backBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(event.getButton() == MouseButton.PRIMARY)
					window.setScene(window.sceneMenu);
				
				event.consume();
			}
		});
		
		int lvlCounter = 0;
		for(int i = 0; i < LVL_ROW_COUNT; i++)
		{
			for(int j = 0; j < LVL_COLUMN_COUNT; j++)
			{
				Tilemap map = GameState.getMap(lvlCounter);
				TilemapRenderer lvl = new TilemapRenderer(map.getWidth() * TILE_SIZE, map.getHeight() * TILE_SIZE, map);
				lvl.getSettings().setShadowOpacity(0.5f);
				lvl.setTileSize(TILE_SIZE);
				lvl.setTranslateX(LVL_OFFSET_X + LVL_MARGIN * j);
				lvl.setTranslateY(LVL_OFFSET_Y + LVL_MARGIN * i);
				
				levelSelectPane.getChildren().add(lvl);
				
				final int level = lvlCounter;
				lvlCounter++;
				
				lvl.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent event)
					{
						if(event.getButton() == MouseButton.PRIMARY)
						{
							GameState.setLevel(level);
							new GameWindow().show();
							window.close();
						}
						event.consume();
					}
				});
			}
		}
	}
}