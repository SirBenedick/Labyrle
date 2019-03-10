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

public class MainMenuWindow extends Stage
{
	final double SWITCH_WIDTH = 150;
	final double SWITCH_HIGHT = 150;
	final double WINDOW_HEIGHT = 650;
	final double WINDOW_WIDTH = 825;
	final double SWITCH_OFFSET_X = 550;
	final double SWITCH_OFFSET_Y = 350;
	final double START_MIN_X = 140;
	final double START_MAX_X = 280;
	final double START_MIN_Y = 350;
	final double START_MAX_Y = 425;
	final double MANUAL_MIN_X = 315;
	final double MANUAL_MAX_X = 520;
	final double MANUAL_MIN_Y = 360;
	final double MANUAL_MAX_Y = 430;
	
	private boolean switchOn = true;
	private MainMenuWindow window;
	
	private Scene sceneMenu;
	
	private Scene sceneManual;
	private int manualIndex;
	private Background manualBackground[];
	private Pane manualContentPane;
	
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
			switchButton.setGraphic(new ImageView(new Image("gfx/switchOn.png")));
		else
			switchButton.setGraphic(new ImageView(new Image("gfx/switchOff.png")));
		
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
            		switchOn = false;
            	}
            	else
            	{
            		switchButton.setGraphic(new ImageView(new Image("gfx/switchOn.png")));
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
            		System.out.println("Start");
            	//ToDo LevelSelectFenster
            	else if(event.getX() >= MANUAL_MIN_X && event.getX() <= MANUAL_MAX_X && event.getY() >= MANUAL_MIN_Y && event.getY() <= MANUAL_MAX_Y)
            		window.setScene(window.sceneManual);
            	//ToDo Anleitungsfenster
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
				
				if(window.manualIndex < 0 || window.manualIndex > 4)
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
}


