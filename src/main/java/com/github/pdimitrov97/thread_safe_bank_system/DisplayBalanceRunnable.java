package com.github.pdimitrov97.thread_safe_bank_system;

public class DisplayBalanceRunnable implements Runnable
{
	private static final int DELAY = 10;
	private Account account;

	public DisplayBalanceRunnable(Account account)
	{
		this.account = account;
	}

	public void run()
	{
		try
		{
			Thread.sleep(DELAY);
			account.displayBalance();
		}
		catch (InterruptedException e)
		{

		}
	}
}