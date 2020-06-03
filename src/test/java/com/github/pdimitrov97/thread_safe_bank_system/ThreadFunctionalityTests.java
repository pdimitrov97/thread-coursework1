package com.github.pdimitrov97.thread_safe_bank_system;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import com.github.pdimitrov97.thread_manager.ThreadManager;

public class ThreadFunctionalityTests
{
	public ThreadManager m;

	@Before
	public void setUp()
	{
		m = new ThreadManager();
	}

	@Test
	public void testFilter()
	{
		// Check for the main thread group
		ArrayList<Thread> threads = m.filterByGroup("main");
		
		for (Thread t : threads)
			assertEquals(t.getThreadGroup().getName(), "main");
		
		// Check for the system thread group
		threads = m.filterByGroup("system");
		
		for (Thread t : threads)
			assertEquals(t.getThreadGroup().getName(), "system");

		// Check for invalid/empty case
		threads = m.filterByGroup("");
		assertNull(threads);
	}

	@Test
	public void searchThread()
	{
		assertEquals(m.searchByThreadName("main").getName(), "main");
		assertNull(m.searchByThreadName(""));
	}

	@Test
	public void stopTest()
	{
		int currentNumber = m.filterByGroup("main").size();
		Thread t = m.startThread();
		
		try
		{
			// Make sure the other thread is started
			Thread.sleep(1500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		m.refresh();
		int newNumber = m.filterByGroup("main").size();
		
		assertTrue(newNumber > currentNumber);
		assertFalse(m.stopThread(""));
		assertTrue(m.stopThread(t.getName()));
		
		try
		{
			// Make sure the other thread is started
			Thread.sleep(1500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		currentNumber = m.filterByGroup("main").size();
		assertTrue(newNumber > currentNumber);
	}

	@Test
	public void startStopThread()
	{
		// Get the current number of threads and start a new one
		int currentThreads = m.filterByGroup("main").size();
		Thread started = m.startThread();
		
		try
		{
			// Make sure the other thread is started
			Thread.sleep(1500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		m.refresh();
		int threadNumber = m.filterByGroup("main").size();
		
		// Check that now there are more threads than before
		assertTrue(threadNumber > currentThreads);

		// Do the same strategy but with stop thread and check that now there are less
		// threads
		currentThreads = m.filterByGroup("main").size();
		assertTrue(m.stopThread(started.getName()));
		
		try
		{
			// Make sure the other thread is started
			Thread.sleep(1500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		m.refresh();
		threadNumber = m.filterByGroup("main").size();
		assertTrue(threadNumber < currentThreads);
	}
}