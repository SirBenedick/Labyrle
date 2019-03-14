package gui;

import java.io.File;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Tilemap;
import logic.utility.TileType;

public class MapEditorWindow extends Stage
{
	private void dialogMsg(String title, String text, String header, AlertType type)
	{
		Alert err = new Alert(type);
		err.setTitle(title);
		err.setHeaderText(header);
		err.setContentText(text);
		err.showAndWait();
	}
	
	private boolean yesnoDialog(String title, String text, String header) 
	{
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
 
        alert.setHeaderText(header);
        alert.setContentText(text);
 
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
	
	private GridPane rootLayout;
	private GridPane rightLayout;
	private Scene mainScene;
	private TilemapRenderer renderer;
	private Label paintModeLabel;
	private RadioButton paintModeWallRadio;
	private RadioButton paintModeFreeRadio;
	private RadioButton paintModeEntityRadio;
	private ToggleGroup paintModeGroup;
	private Label paintModeHelpLabel;
	private Label tileInfoLabel;
	private Label utilityLabel;
	private CheckBox showGridChk;
	private Button clearMapBtn;
	private Label fileLabel;
	private Button saveFile;
	private Button loadFile;
	private Label entityLabel;
	private ComboBox<String> entityBox;
	private ComboBox<String> entityColorBox;
	
	public MapEditorWindow()
	{
		this.setTitle("The ultimative labyrinth editor");
		
		final String headingStyle = "-fx-font-weight: bold; -fx-font-style: italic; -fx-font-size: 15pt;";
		//The offset in the grid pane
		final int offsetPaintMode = 0;
		final int offsetEntity = 3;
		final int offsetUtility = 5;
		final int offsetFile = 8;
		
		rootLayout = new GridPane();
		//rootLayout.setBackground(new Background(gfx.Manager.getBackgroundImageGame()));
		
		//Side bar for editor controls
		rightLayout = new GridPane();
		rightLayout.setVgap(15.0);
		rightLayout.setHgap(15.0);
		rootLayout.add(rightLayout, 1, 0);
		
		//Displays the map, is hooked with events in the end of this constructor
		renderer = new TilemapRenderer(Defaults.TILE_SIZE*logic.Defaults.LabyrinthWidth, Defaults.TILE_SIZE*logic.Defaults.LabyrinthHeight, new Tilemap());	
		renderer.getSettings().setShadowOpacity(0.0f);
		rootLayout.add(renderer, 0, 0);
		
		// PAINT MODE
		paintModeLabel = new Label("Paint mode");
		paintModeLabel.setStyle(headingStyle);
		rightLayout.add(paintModeLabel, 0, offsetPaintMode);
		
		paintModeGroup = new ToggleGroup();
		
		paintModeWallRadio = new RadioButton("Wall");
		paintModeWallRadio.setSelected(true);
		paintModeWallRadio.setToggleGroup(paintModeGroup);
		rightLayout.add(paintModeWallRadio, 1, offsetPaintMode+1);
		
		paintModeFreeRadio  = new RadioButton("Free");
		paintModeFreeRadio.setToggleGroup(paintModeGroup);
		rightLayout.add(paintModeFreeRadio, 0, offsetPaintMode+1);
		
		paintModeEntityRadio = new RadioButton("Entity");
		paintModeEntityRadio.setToggleGroup(paintModeGroup);
		rightLayout.add(paintModeEntityRadio, 0, offsetPaintMode+2);
		
		paintModeHelpLabel = new Label("Right: Copy selected tile type\n"
				+ "Left: Apply mode to selected tile\n" +
				"Button3: toggle paint mode\n"+
				"\nIn entity mode right click will delete the\nentity");
		rightLayout.add(paintModeHelpLabel, 1, offsetPaintMode+2);
		
		// ENTITY
		entityLabel = new Label("Entity");
		entityLabel.setStyle(headingStyle);
		rightLayout.add(entityLabel, 0, offsetEntity);
		
		entityBox = new ComboBox<String>
		(
				FXCollections.observableArrayList
				(
					"End point",
					"Start",
					"Pass"
				)
		);
		entityBox.getSelectionModel().select(0);
		entityBox.setOnAction((ActionEvent e) ->
		{
			if (entityBox.getSelectionModel().getSelectedIndex() == 0)
				entityColorBox.setDisable(true);
			else
				entityColorBox.setDisable(false);
		});
		rightLayout.add(entityBox, 0, offsetEntity+1);
		
		entityColorBox = new ComboBox<String>
		(
				FXCollections.observableArrayList
				(
					"COLOR0",
					"COLOR1",
					"COLOR2",
					"COLOR3"
				)
		);
		entityColorBox.getSelectionModel().select(0);
		entityColorBox.setDisable(true);
		rightLayout.add(entityColorBox, 1, offsetEntity+1);
		
		// UTILITY
		utilityLabel = new Label("Utility");
		utilityLabel.setStyle(headingStyle);
		rightLayout.add(utilityLabel, 0, offsetUtility);
		
		tileInfoLabel = new Label("Right click on a tile for more information\n ");
		rightLayout.add(tileInfoLabel, 1, offsetUtility+1);
		
		showGridChk = new CheckBox("Show grid");
		showGridChk.setSelected(true);
		renderer.setGridEnabled(true);
		showGridChk.setOnMouseClicked((MouseEvent e) ->
		{
			renderer.setGridEnabled(showGridChk.isSelected());
			renderer.drawMap();
		});
		rightLayout.add(showGridChk, 0, offsetUtility+1);
		
		clearMapBtn = new Button("Clear map");
		clearMapBtn.setOnAction((ActionEvent e) ->
		{
			if (yesnoDialog("Clear map", "You are about to clear the current map. All progress done will be lost.", null))
			{
				renderer.getMap().clear();
				renderer.resolveAllConnectionTypes();
				renderer.drawMap();
			}
		});
		rightLayout.add(clearMapBtn, 0, offsetUtility+2);
		
		// FILE
		fileLabel = new Label("File");
		fileLabel.setStyle(headingStyle);
		rightLayout.add(fileLabel, 0, offsetFile);
		
		saveFile = new Button("Save file");
		saveFile.setOnAction((ActionEvent e) ->
		{
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Save Labyrinth");
			//chooser.setInitialDirectory(new File(logic.Defaults.LABYRINTH_DIRECTORY));
			chooser.getExtensionFilters().addAll
			(
					new FileChooser.ExtensionFilter("Labyrinth file", "*"+logic.Defaults.LABYRINTH_EXTENSION),
					new FileChooser.ExtensionFilter("All files", "*.")
			);
			File file = chooser.showSaveDialog(this);
			if (file == null)
				return;
			
			try
			{
				renderer.getMap().saveToFile(file.getPath());
				dialogMsg("Success", "map saved!", null, AlertType.INFORMATION);
			}
			catch(Exception ex)
			{
				dialogMsg("Error", ex.getMessage(), "Writing the map failed", AlertType.ERROR);
			}
			
		});
		rightLayout.add(saveFile, 0, offsetFile+1);

		
		loadFile = new Button("Load file");
		loadFile.setOnAction((ActionEvent e) ->
		{
			FileChooser chooser = new FileChooser();
			chooser.setTitle("load Labyrinth");
			//chooser.setInitialDirectory(new File(logic.Defaults.LABYRINTH_DIRECTORY));
			chooser.getExtensionFilters().addAll
			(
					new FileChooser.ExtensionFilter("Labyrinth file", "*"+logic.Defaults.LABYRINTH_EXTENSION),
					new FileChooser.ExtensionFilter("All files", "*.")
			);
			File file = chooser.showOpenDialog(this);
			
			if (file == null)
				return;
			
			try
			{
				Tilemap new_map = new Tilemap();
				new_map.loadFromFile(file.getPath());
				renderer.setTilemap(new_map);
				dialogMsg("Success", "map loaded!", null, AlertType.INFORMATION);
			}
			catch(Exception ex)
			{
				dialogMsg("Error", ex.getMessage(), "Loading the map failed", AlertType.ERROR);
			}
		});
		rightLayout.add(loadFile, 1, offsetFile+1);
		
		renderer.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
		(MouseEvent e) ->
		{
			applyPaint(e);
		});
		
		renderer.addEventHandler(MouseEvent.MOUSE_RELEASED, 
		(MouseEvent e) ->
		{
			applyPaint(e);
			renderer.drawMap();
		});
		
		mainScene = new Scene(rootLayout, renderer.getWidth()+350, renderer.getHeight());
		this.setScene(mainScene);
		renderer.drawGrid();
		this.sizeToScene();
		
		this.setOnCloseRequest((WindowEvent e) ->
		{
			if (!yesnoDialog("Close", "Do you really want to exit the map editor? All unsaved progress will be lost.", null))
				e.consume();
		});
		
		mainScene.setOnKeyReleased((KeyEvent e) ->
		{
			if (e.getCode() == KeyCode.PLUS)
				renderer.getSettings().setShadowOpacity(renderer.getSettings().getShadowOpacity()+0.1f);
			else if (e.getCode() == KeyCode.MINUS)
				renderer.getSettings().setShadowOpacity(renderer.getSettings().getShadowOpacity()-0.1f);
		});
	}
	
	private void applyPaint(MouseEvent e)
	{
		if (e.getButton() == MouseButton.PRIMARY)
		{
			//normal painting
			if (paintModeWallRadio.isSelected() || paintModeFreeRadio.isSelected())
			{
				renderer.getMap().removeEntity(renderer.getSelectedX(), renderer.getSelectedY());
				renderer.getSelectedTile().setType
				( (paintModeWallRadio.isSelected() ? TileType.WALL : TileType.FREE) );
			}
			//entity
			else
			{
				switch(entityBox.getSelectionModel().getSelectedItem())
				{
				case "End point":
					renderer.getMap().setEndPoint(renderer.getSelectedX(), renderer.getSelectedY());
					break;
				case "Start":
					renderer.getMap().setStartingPoint
					(
							logic.utility.Color.values()[entityColorBox.getSelectionModel().getSelectedIndex()], 
							renderer.getSelectedX(), 
							renderer.getSelectedY()
					);
					break;
				case "Pass":
					renderer.getMap().setPassPoint
					(
							renderer.getSelectedX(), 
							renderer.getSelectedY(), 
							logic.utility.Color.values()[entityColorBox.getSelectionModel().getSelectedIndex()]
					);
					break;
				}
			}
			renderer.resolveAllConnectionTypes();
		}
		else if (e.getButton() == MouseButton.SECONDARY)
		{
			//Wall / Free paint
			if (paintModeWallRadio.isSelected() || paintModeFreeRadio.isSelected())
			{
				paintModeWallRadio.setSelected( renderer.getSelectedTile().getType() == TileType.WALL ? true : false );
				paintModeFreeRadio.setSelected( renderer.getSelectedTile().getType() == TileType.FREE ? true : false );
			
				tileInfoLabel.setText
				(
						"X: " + renderer.getSelectedX() + " Y: " + renderer.getSelectedY() + "\n" +
						"Connection type: " + renderer.getConnectionType(renderer.getSelectedX(), renderer.getSelectedY()).toString()
				);
			}
			//entity mode
			else
				renderer.getMap().removeEntity(renderer.getSelectedX(), renderer.getSelectedY());
		}
		else if (e.getButton() == MouseButton.MIDDLE)
		{
			if (paintModeFreeRadio.isSelected())
				paintModeWallRadio.setSelected(true);
			else if (paintModeWallRadio.isSelected())
				paintModeEntityRadio.setSelected(true);
			else
				paintModeFreeRadio.setSelected(true);
		}
	}
}
