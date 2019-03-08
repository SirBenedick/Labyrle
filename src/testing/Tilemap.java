package testing;

import org.junit.Test;

public class Tilemap
{
	@Test
	public void save() throws Exception
	{
		logic.Tilemap t = logic.Tilemap.makeTestMap();
		t.saveToFile("test.bin");
		
		t = logic.Tilemap.makeTestMap();
		t.loadFromFile("test.bin");
	}
}
