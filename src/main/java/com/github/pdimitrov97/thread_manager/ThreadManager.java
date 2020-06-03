package com.github.pdimitrov97.thread_manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreadManager
{
	private static List<Thread> allThreads;
	private static List<ThreadGroup> allThreadGroups;
	private final ThreadGroup root;

	public ThreadManager()
	{
		allThreads = new ArrayList<Thread>();
		allThreadGroups = new ArrayList<ThreadGroup>();

		root = findRootThreadGroup();
		generateThreadsAndGroups(root);
	}

	private static ThreadGroup findRootThreadGroup()
	{
		Thread currentThread = Thread.currentThread();
		ThreadGroup rootCandidate = currentThread.getThreadGroup();

		while (true)
		{
			if (rootCandidate.getParent() == null) // we have found the root
				return rootCandidate;
			else // nope, keep going up the tree
				rootCandidate = rootCandidate.getParent();
		}
	}

	private static void generateThreadsAndGroups(ThreadGroup root)
	{
		int activeGroups = root.activeGroupCount();
		ThreadGroup[] groups = new ThreadGroup[activeGroups];
		
		// Get all groups in an array
		while (true)
		{
			activeGroups = root.enumerate(groups, true);
			
			if (activeGroups != groups.length)
				break;
			else
				groups = new ThreadGroup[activeGroups + 1];
		}
		
		groups[groups.length - 1] = root;
		allThreadGroups = Arrays.asList(groups);
		
		// From every group/sub-group list all the threads
		for (ThreadGroup g : groups)
		{
			int active = root.activeCount();
			Thread[] threads = new Thread[active];
			
			while (true)
			{
				active = g.enumerate(threads, false);
				
				if (active != threads.length)
					break;
				else
					threads = new Thread[active + 1];
			}
			
			for (Thread t : threads)
			{
				if (t != null)
					allThreads.add(t);
			}
		}
	}

	public Thread searchByThreadName(String name)
	{
		for (int i = 0; i < allThreads.size(); i++)
		{
			if (allThreads.get(i).getName().equals(name))
				return allThreads.get(i);
		}
		
		return null;
	}

	public void printThreads()
	{
		for (Thread t : allThreads)
		{
			System.out.print("	Name: " + t.getName() + ", Priority: " + t.getPriority() + ", State: " + t.getState() + ", Id: " + t.getId());
			
			if (t.isDaemon())
				System.out.print(", Is a deamon.\n");
			else
				System.out.print(", Not a deamon.\n");
		}
	}

	public static List<Thread> getAllThreads()
	{
		return allThreads;
	}

	public static List<ThreadGroup> getAllThreadGroups()
	{
		return allThreadGroups;
	}

	public ArrayList<Thread> filterByGroup(String group)
	{
		ArrayList<Thread> resultThreads = new ArrayList<Thread>();
		
		for (ThreadGroup g : allThreadGroups)
		{
			if (g.getName().equals(group))
			{
				int active = g.activeCount();
				Thread[] threads = new Thread[active];
				
				while (true)
				{
					active = g.enumerate(threads, false);
					
					if (active != threads.length)
						break;
					else
						threads = new Thread[active + 1];
				}
				
				for (Thread t : threads)
				{
					if (t != null)
						resultThreads.add(t);
				}
				
				return resultThreads;
			}
		}
		
		return null;
	}

	public ArrayList<String> groups()
	{
		ArrayList<String> groupNames = new ArrayList<String>();
		
		for (ThreadGroup g : allThreadGroups)
			groupNames.add(g.getName());

		return groupNames;
	}

	public Thread startThread()
	{
		Thread t = new Thread(new SimpleThread());
		t.start();
		
		return t;
	}

	public boolean stopThread(String name)
	{
		Thread toStop = searchByThreadName(name);
		
		if (toStop != null)
		{
			toStop.interrupt();
			return true;
		}
		else
			return false;
	}

	public void refresh()
	{
		// Reset
		allThreads = new ArrayList<Thread>();
		allThreadGroups = new ArrayList<ThreadGroup>();
		generateThreadsAndGroups(root);
	}
}