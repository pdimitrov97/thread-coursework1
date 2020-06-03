package com.github.pdimitrov97.thread_manager;

public class SimpleThread implements Runnable
{
	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				Thread.sleep(100000);
				
				if (Thread.interrupted())
					break;
			}
		}
		catch (InterruptedException e)
		{
			System.out.println("Interrupted!");
		}
	}
}