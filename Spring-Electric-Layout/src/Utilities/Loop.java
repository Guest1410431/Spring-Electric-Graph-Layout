package Utilities;

public class Loop extends Thread 
{
    private Window comp;
    
    public Loop(Window c)
    {
    	comp = c;
    }

    public void run() 
    {
        long tickTime = 100 / 60;
        long startTime;
        long sleepTime;
        
        while (true) 
        {
            startTime = System.currentTimeMillis();
            
            try 
            {
            	comp.update();
            } 
            finally 
            {
            	
            }
            sleepTime = tickTime - (System.currentTimeMillis() - startTime);
            
            try 
            {
                if (sleepTime > 0)
                {
                	sleep(sleepTime);
                }                    
                else
                {
                	sleep(10);
                }                    
            } 
            catch (Exception e) 
            {

            }
        }
    }
}