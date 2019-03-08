package gui;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TilemapRenderer extends Application{
	
		@Override
		public void start(Stage primaryStage) {
			//Anlegen der Buttons
			final Label Level = new Label ("LEVEL");
			final Label ZahlLevel = new Label ("Eins");
			final Button Undo = new Button ("Undo");
			final Button Clear = new Button ("Clear");
			final Button Exit = new Button ();
			Exit.setText("Exit");
			//Bei dem Exit-Button nachfragen ob Spieler beenden will
			Exit.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) {
					System.out.println("Wollen Sie wirklich beenden?");
						}
				
			});
			//Anlegen der Auswahlbox
			final VBox Box =new VBox();
			
			//Abstand zur Außenseite
			Box.setPadding(new Insets(10,20,10,20));
			
			//Abstand der Buttons in der Box
			Box.setSpacing(10);
			
			//Buttons hinzufügen
			Box.getChildren().addAll(Level,ZahlLevel,Undo,Clear,Exit);

			//Ausrichtung der Buttons
			Box.setAlignment(Pos.CENTER_RIGHT);
			
			//Desgin der kompletten Boxen
			//Box.setStyle("-fx-padding:10;"+ "-fx-border-color:red;");
			
			
			//Quadrat für Labyrinth anlegen
			final BorderPane Quad = new BorderPane();
			Rectangle Quadrat = new Rectangle();
			Quadrat.setX(0);
			Quadrat.setY(0);
			Quadrat.setWidth(300);
			Quadrat.setHeight(300);
			Quad.getChildren().add(Quadrat);
			
			
			Button button = new Button();
			button.setText("Klicken, um Spiel zu beginnen.");
			
			button.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) {
					Label secondLabel =new Label("neues Label");
					StackPane secondaryLayout=new StackPane();
					secondaryLayout.getChildren().add(secondLabel);
					Scene secondScene =new Scene(secondaryLayout,230,100);
					
					Stage newWindow =new Stage();
					newWindow.setTitle("Labyrinth");
					newWindow.setScene(secondScene);
					
					newWindow.initModality(Modality.WINDOW_MODAL);
					newWindow.initOwner(primaryStage);
					newWindow.setX(primaryStage.getX()+200);
					newWindow.setY(primaryStage.getY()+100);
					
					newWindow.show();
					
				}
				
			});
			
			//Festlegung der Ordnung
			BorderPane root =new BorderPane();
			
			//Einteilung der Boxen
			Quad.setRight(Box);
			Quad.setCenter(button);
			
			//Einbindung in die GUI
			root.setTop(Quad);
			//Szene festlegen, Größe
			Scene scene = new Scene(root, 400, 300);
			
			//Name des Spiels
			primaryStage.setTitle("Finde den Weg im Labyrinth!");
			
			//Höhe und Breite der Stage festlegen
			primaryStage.setResizable(false);
			//Oder Stage selbst festlegen
		//	primaryStage.setWidth(1000);
		//	primaryStage.setHeight(900);
			
			
			//Anzeigen in der GUI
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.show();
			
			
			
			
	}

		
		
		
		
		
		
		
		public static void main(String[] args) {
			launch(args);
		}
		
	}

