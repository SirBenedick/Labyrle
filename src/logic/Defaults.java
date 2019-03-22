package logic;

public class Defaults
{
	//TODO name refactor.
	public static final int LabyrinthWidth 			= 20;
	public static final int LabyrinthHeight 		= 16;
	
	//1.5 Seconds before parts of the map render invisible
	public static final float ILLUMINATION_SCALE 	= 1.0f;
	public static final float SHADOW_OPACITY 		= 1.0f;
	
	public static final String LABYRINTH_DIRECTORY 	= "labyrinths";
	public static final String LABYRINTH_EXTENSION 	= ".la";
	public static final String LABYRINTH_BASE_NAME	= "level_";
	public static final int	   LABYRINTH_COUNT 		= 20;
	
	//public static final String SYSTEM_PATH		= "system";
	public static final String SAVE_GAME_FILE	  	= "game.sav";
	
	public static final int MAX_UNDO_STEPS = 10;
}
