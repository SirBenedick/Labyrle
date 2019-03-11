package logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
	
	/*
	 * Holds current settings. Keep in mind that they might be invalid!
	 */
	private static Settings currentSettings;
	
	public static void reset()
	{
		unlockedLevel = 1;
		currentSettings = new Settings();
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
	
	public static void restoreSystem() throws Exception
	{
		File dir = new File(Defaults.SYSTEM_PATH);
        if (!dir.exists())
        	(new File(Defaults.SYSTEM_PATH)).mkdir();
        
        dir = new File(Defaults.LABYRINTH_DIRECTORY);
        if (!dir.exists())
        	(new File(Defaults.LABYRINTH_DIRECTORY)).mkdir();
        
        BufferedWriter out = new BufferedWriter(new FileWriter(Defaults.SYSTEM_PATH+"/"+"params.txt"));
        out.write("The application supports following startup parameters: \n\n");
        out.write("-setup (restores basics directories and creates a new save game)\n");
        out.write("-editor (starts the map editor)");
        out.close();
       
        save();
	}
	
	public static void save() throws Exception
	{
        saveToFile(Defaults.SYSTEM_PATH+"/"+Defaults.SAVE_GAME_FILE);
	}
	
	public static void load() throws Exception
	{
		loadFromFile(Defaults.SYSTEM_PATH+"/"+Defaults.SAVE_GAME_FILE);
	}
}
