package testing;

public class NotAutomated
{
	public static void main(String[] argv)
	{
		logic.GameState state = new logic.GameState();
		try
		{
			//GameState.restoreSystem();
		} catch (Exception e)
		{
			System.out.println("ohh noes");
		}
	}
}
