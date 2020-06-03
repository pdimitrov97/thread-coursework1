package com.github.pdimitrov97.thread_safe_bank_system;

public class WithdrawMoneyRunnable implements Runnable
{
	private static final int DELAY = 10;
	private Account account;
	private Double amount;

	public WithdrawMoneyRunnable(Account account, Double amount)
	{
		this.account = account;
		this.amount = amount;
	}

	public void run()
	{
		try
		{
			Thread.sleep(DELAY);
			account.withdrawMoney(amount);
		}
		catch (InterruptedException e)
		{
			System.out.println("Thread with id: " + Thread.currentThread().getId() + ", Cannot wait any more!");
		}
	}
}