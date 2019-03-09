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
	Pane contantPane;
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
	
	public MainMenuWindow()
	{
		contantPane = new Pane();
		Background bg = new Background(new BackgroundImage(
				new Image("gfx//Menu_ohne_Knopf.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, 
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)));
		
		contantPane.setBackground(bg);
		Label switchButton = new Label();
		switchButton.setGraphic(new ImageView(new Image("gfx//switchOn.png")));
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
            		switchButton.setGraphic(new ImageView(new Image("gfx//switchOff.png")));
            		switchOn = false;
            	}
            	else
            	{
            		switchButton.setGraphic(new ImageView(new Image("gfx//switchOn.png")));
            		switchOn = true;
            	}
            	event.consume();
            };
        });
		
		contantPane.getChildren().add(switchButton);
		
		contantPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
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
            		System.out.println("Anleitung");
            	//ToDo Anleitungsfenster
            	event.consume();
            };
        });
		
		this.setScene(new Scene(contantPane));
		this.setHeight(WINDOW_HEIGHT);
		this.setWidth(WINDOW_WIDTH);
		this.setTitle("Labyrle");
		this.setResizable(false);
		this.getIcons().add(new Image("gfx//icon.png"));
	}
}


