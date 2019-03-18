package logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/*
 * 			FILE FORMAT
 *  BYTE[2]		TM
 * 	INT 		unlockedLevel
 *  BYTE[2]		TM
 */
public class GameState
{
	/*
	 * All levels up to this level are getting unlocked.
	 */
	private static int unlockedLevel;
	
	private static int currentLevel;
	
	/*
	 * Holds current settings. Keep in mind that they might be invalid!
	 */
	private static Settings currentSettings;
	
	/*
	 * Stores all maps (preloaded)
	 */
	private static Tilemap[] maps;
	
	public static void reset()
	{
		unlockedLevel = 1;
		currentLevel = 0;
		currentSettings = new Settings();
		maps = new Tilemap[0];
	}
	
	public static void setLevel(int level)
	{
		currentLevel = level;
	}
	
	public static void loadAllMaps() throws Exception
	{
		maps = new Tilemap[Defaults.LABYRINTH_COUNT];
		for (int i = 0; i < maps.length; i++)
		{
			maps[i] = new Tilemap();
			maps[i].loadFromFile(Defaults.LABYRINTH_DIRECTORY+"/"+Defaults.LABYRINTH_BASE_NAME+""+i+""+Defaults.LABYRINTH_EXTENSION);
		}
	}
	
	public static Tilemap getMap(int level)
	{
		maps[level].clearColors();
		return maps[level];
	}
	
	public static int getCurrentLevel()
	{
		return currentLevel;
	}
	
	public static Settings getSettings()
	{
		return currentSettings;
	}
	
	private static void loadFromFile(String path) throws Exception
	{
		reset();
		
		InputStream in = new FileInputStream(path);
		byte[] ident = new byte[2];
		
		//Check if it is a valid file
		in.read(ident);
		if (ident[0] != 'T' || ident[1] != 'M')
		{
			in.close();
			throw new Exception("File is not a GameState");
		}
		
		//Read the unlocked level
		unlockedLevel = in.read();
		
		//Validate the file
		in.read(ident);
		if (ident[0] != 'T' || ident[1] != 'M')
		{
			in.close();
			throw new Exception("File is corrupted");
		}
		
		in.close();
	}
	
	private static void saveToFile(String path) throws Exception
	{
		OutputStream out = new FileOutputStream(path);
		
		out.write(new byte[] {'T', 'M'});
		out.write(unlockedLevel);
		out.write(new byte[] {'T', 'M'});
		
		out.close();
	}
	
	public static int getUnlockedLevel()
	{
		return unlockedLevel;
	}
	
	@Deprecated
	public static void restoreSystem() throws Exception
	{
		/*File dir = new File(Defaults.SYSTEM_PATH);
        if (!dir.exists())
        	(new File(Defaults.SYSTEM_PATH)).mkdir();
        
        dir = new File(Defaults.LABYRINTH_DIRECTORY);
        if (!dir.exists())
        	(new File(Defaults.LABYRINTH_DIRECTORY)).mkdir();*/
        
        /*BufferedWriter out = new BufferedWriter(new FileWriter(Defaults.SYSTEM_PATH+"/"+"params.txt"));
        out.write("The application supports following startup parameters: \n\n");
        out.write("-setup (restores basics directories and creates a new save game)\n");
        out.write("-editor (starts the map editor)");
        out.close();*/
       
        save();
	}
	
	public static void save() throws Exception
	{
        saveToFile(Defaults.SAVE_GAME_FILE);
	}
	
	public static void load() throws Exception
	{
		loadFromFile(Defaults.SAVE_GAME_FILE);
	}

	public static void setUnlockedLevel(int i)
	{
		unlockedLevel = i;
	}
}
