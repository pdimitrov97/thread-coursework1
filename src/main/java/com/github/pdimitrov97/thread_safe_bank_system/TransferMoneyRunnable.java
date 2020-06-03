package com.github.pdimitrov97.thread_safe_bank_system;

public class TransferMoneyRunnable implements Runnable
{
	private static final int DELAY = 10;
	private Account sender;
	private Account receiver;
	private Double amount;

	public TransferMoneyRunnable(Account sender, Account receiver, Double amount)
	{
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
	}

	public void run()
	{
		try
		{
			Thread.sleep(DELAY);
			sender.transferMoney(amount, receiver);
		}
		catch (InterruptedException e)
		{
			System.out.println("Cannot wait any more!");
		}
	}
}