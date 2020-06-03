package com.github.pdimitrov97.thread_safe_bank_system;

public class DepositMoneyRunnable implements Runnable
{
	private static final int DELAY = 10;
	private Account account;
	private Double amount;

	public DepositMoneyRunnable(Account account, Double amount)
	{
		this.account = account;
		this.amount = amount;
	}

	public void run()
	{
		try
		{
			Thread.sleep(DELAY);
			account.depositMoney(amount);
		}
		catch (InterruptedException e)
		{

		}
	}
}