package gui;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.GameState;
import logic.Tilemap;

public class MainMenuWindow extends Stage
{
	private final double LVL_OFFSET_X = 60;
	private final double LVL_OFFSET_Y = 120;
	private final double BACK_BTN_OFFSET_X = 690;
	private final double BACK_BTN_OFFSET_Y = 510;
	private final double BACK_BTN_WIDTH = 100;
	private final double BACK_BTN_HIGHT = 75;
	private final double SWITCH_OFFSET_X = 550;
	private final double SWITCH_OFFSET_Y = 350;
	private final double SWITCH_WIDTH = 150;
	private final double SWITCH_HIGHT = 150;
	private final double WINDOW_HEIGHT = 650;
	private final double WINDOW_WIDTH = 825;
	private final double START_MIN_X = 140;
	private final double START_MAX_X = 280;
	private final double START_MIN_Y = 350;
	private final double START_MAX_Y = 425;
	private final double MANUAL_MIN_X = 315;
	private final double MANUAL_MAX_X = 520;
	private final double MANUAL_MIN_Y = 360;
	private final double MANUAL_MAX_Y = 430;
	private final int TILE_SIZE = 4;
	private final int LVL_MARGIN = 100;
	private final int LVL_COLUMN_COUNT = 5;
	private final int LVL_ROW_COUNT = 4;
	
	private boolean switchOn = true;
	private MainMenuWindow window;
	
	private Scene sceneMenu;
	
	private Scene sceneManual;
	private int manualIndex;
	private Background manualBackground[];
	private Pane manualContentPane;
	
	private Scene sceneLevelSelect;
	
	public MainMenuWindow()
	{
		window = this;
		this.setHeight(WINDOW_HEIGHT);
		this.setWidth(WINDOW_WIDTH);
		this.setTitle("Labyrle");
		this.setResizable(false);
		this.getIcons().add(new Image("gfx/icon.png"));
		initialiseMenuScene();
		initilazieManualScene();
		initilazieLevelSelectScene();
		this.setScene(sceneMenu);
	}
	
	private void initialiseMenuScene()
	{
		Pane menuContentPane = new Pane();
		this.sceneMenu = new Scene(menuContentPane);
		Background bg = new Background(new BackgroundImage(
				new Image("gfx/Menu_ohne_Knopf.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, 
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)));
		
		menuContentPane.setBackground(bg);
		Label switchButton = new Label();
		
		if(switchOn)
		{
			switchButton.setGraphic(new ImageView(new Image("gfx/switchOn.png")));
			GameState.getSettings().setShadowOpacity(1.0f);
		}
		else
		{
			switchButton.setGraphic(new ImageView(new Image("gfx/switchOff.png")));
			GameState.getSettings().setShadowOpacity(0.0f);
		}
		
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
            		switchButton.setGraphic(new ImageView(new Image("gfx/switchOff.png")));
            		GameState.getSettings().setShadowOpacity(0.0f);
            		switchOn = false;
            	}
            	else
            	{
            		switchButton.setGraphic(new ImageView(new Image("gfx/switchOn.png")));
            		GameState.getSettings().setShadowOpacity(1.0f);
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
	
	private void initilazieManualScene()
	{
		this.manualIndex = 0;
		manualContentPane = new Pane();
		this.sceneManual = new Scene(manualContentPane);
		this.manualBackground = new Background[5];
		for(int i = 0; i < manualBackground.length; i++)
		{
			manualBackground[i] = new Background(new BackgroundImage(
					new Image("gfx/Manual_" + i + ".png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, 
					new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true))); 
		}
		
		manualContentPane.setBackground(manualBackground[this.manualIndex]);
		
		manualContentPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{	
				if(event.getButton() == MouseButton.PRIMARY)
					window.manualIndex++;
				else if(event.getButton() == MouseButton.SECONDARY)
					window.manualIndex--;
				
				if(window.manualIndex < 0 || window.manualIndex > manualBackground.length - 1)
				{
					window.manualIndex = 0;
					window.manualContentPane.setBackground(window.manualBackground[window.manualIndex]);
					window.setScene(window.sceneMenu);
				}
				else 
					window.manualContentPane.setBackground(window.manualBackground[window.manualIndex]);
				
				event.consume();
			}
		});
	}
	
	private void initilazieLevelSelectScene()
	{
		Pane levelSelectPane = new Pane();
		this.sceneLevelSelect = new Scene(levelSelectPane);
		levelSelectPane.setBackground(new Background(new BackgroundImage(
				new Image("gfx/background_game.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, 
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true))));
		
		Label backBtn = new Label();
		backBtn.setGraphic(new ImageView(new Image("gfx/back_to_main.png")));
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
		//ToDo: Unterschiedliche Maps laden. Eventhandler für jede Map die das GameWindow startet einfügen
		Tilemap map = new Tilemap();
		
		try
		{
			map.loadFromFile("labyrinths/test.la");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		for(int i = 0; i < LVL_ROW_COUNT; i++)
		{
			for(int j = 0; j < LVL_COLUMN_COUNT; j++)
			{
				TilemapRenderer lvl = new TilemapRenderer(map.getWidth() * TILE_SIZE, map.getHeight() * TILE_SIZE, map);
				lvl.setTileSize(TILE_SIZE);
				lvl.setTranslateX(LVL_OFFSET_X + LVL_MARGIN * j);
				lvl.setTranslateY(LVL_OFFSET_Y + LVL_MARGIN * i);
				
				levelSelectPane.getChildren().add(lvl);

			}
		}
	}
}