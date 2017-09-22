package Main;

import Utilities.Loop;
import Utilities.Window;

public class Main 
{
	public static void main(String[] args) 
	{	
		Loop loop = new Loop(new Window());
		
		loop.run();
	}
}
