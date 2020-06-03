package com.github.pdimitrov97.thread_safe_bank_system;

public class ModifySortCodeRunnable implements Runnable
{
	private static final int DELAY = 10;
	private Account account;
	private String sortCode;

	public ModifySortCodeRunnable(Account account, String sortCode)
	{
		this.account = account;
		this.sortCode = sortCode;
	}

	public void run()
	{
		try
		{
			Thread.sleep(DELAY);
			account.setSortCode(sortCode);
		}
		catch (InterruptedException e)
		{
			System.out.println("Cannot wait any more!");
		}
	}
}
