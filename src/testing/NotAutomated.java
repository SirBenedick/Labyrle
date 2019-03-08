package testing;

public class NotAutomated
{
	public static void main(String[] argv)
	{
		logic.GameState state = new logic.GameState();
		try
		{
			state.restoreSystem();
		} catch (Exception e)
		{
			System.out.println("ohh noes");
		}
	}
}
