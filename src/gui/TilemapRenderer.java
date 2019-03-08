package gui;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.Optional;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TilemapRenderer extends Application{
	private void Exit() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Spielabbruch");
 
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Wollen Sie wirklich beenden?");
 
        Optional<ButtonType> result =alert.showAndWait();
        if(result.get() == ButtonType.OK){
        	Platform.exit();
        }
        else{
        	
        }
        
    }
		@Override
		public void start(Stage primaryStage) {
			//Anlegen der Buttons
			final Label Level = new Label ("LEVEL");
			final Label ZahlLevel = new Label ("Eins");
			final Button Undo = new Button ("Undo");
			final Button Clear = new Button ("Clear");
			final Button Exit = new Button ("Exit");
			Exit.setOnAction(new EventHandler<ActionEvent>(){
				
				public void handle(ActionEvent event){
					Exit();
				}
			});
			
			Clear.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					
				}
			});
			
			Undo.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					
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
			//
			
			//Quadrat für Labyrinth anlegen
			final BorderPane Quad = new BorderPane();
			Rectangle Quadrat = new Rectangle();
			Quadrat.setX(0);
			Quadrat.setY(0);
			Quadrat.setWidth(300);
			Quadrat.setHeight(300);
			Quad.getChildren().add(Quadrat);
			
			
			//Festlegung der Ordnung
			BorderPane root =new BorderPane();
			
			//Einteilung der Boxen
			Quad.setRight(Box);
			
			//Einbindung in die GUI
			root.setTop(Quad);
			//Szene festlegen, Größe
			Scene scene = new Scene(root, 400, 300, Color.BLUE);
			
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


