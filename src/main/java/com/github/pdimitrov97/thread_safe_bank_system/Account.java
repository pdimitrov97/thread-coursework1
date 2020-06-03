package com.github.pdimitrov97.thread_safe_bank_system;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Account
{
	// Attributes
	private int holderNumber;
	private int accountNumber;
	private String sortCode;
	private Calendar validFrom;
	private Calendar validUntil;
	private double balance;
	private Lock balanceLock;
	private Lock detailsLock;
	private Condition enoughBalanceCondition;

	// Default Constructor
	public Account()
	{
		this.holderNumber = 1;
		this.accountNumber = 1;
		this.sortCode = "10-10-10";
		this.validFrom = new GregorianCalendar();
		this.validUntil = new GregorianCalendar();
		this.balance = 0.0;
		this.balanceLock = new ReentrantLock();
		this.detailsLock = new ReentrantLock();
		this.enoughBalanceCondition = balanceLock.newCondition();
	}

	// Overloaded Constructor
	public Account(int holderNumber, int accountNumber, String sortCode, Calendar validFrom, Calendar validUntil)
	{
		this.holderNumber = holderNumber;
		this.accountNumber = accountNumber;
		this.sortCode = sortCode;
		this.validFrom = validFrom;
		this.validUntil = validUntil;
		this.balance = 0.0;
		this.balanceLock = new ReentrantLock();
		this.detailsLock = new ReentrantLock();
		this.enoughBalanceCondition = balanceLock.newCondition();
	}

	// Getters
	public int getHolderNumber()
	{
		return holderNumber;
	}

	public int getAccountNumber()
	{
		return accountNumber;
	}

	public String getSortCode()
	{
		return sortCode;
	}

	public Calendar getValidFrom()
	{
		return validFrom;
	}

	public Calendar getValidUntil()
	{
		return validUntil;
	}

	// Setters
	public void setHolderNumber(int holderNumber)
	{
		this.holderNumber = holderNumber;
	}

	public void setAccountNumber(int accountNumber)
	{
		this.accountNumber = accountNumber;
	}

	public void setSortCode(String sortCode)
	{
		System.out.println("Thread with id: " + Thread.currentThread().getId() + ", account Number: " + accountNumber);

		detailsLock.lock();

		try
		{
			System.out.println("Thread with id " + Thread.currentThread().getId() + ", " + "changing sort code of: " + accountNumber);

			this.sortCode = sortCode;

			System.out.println("Thread with id " + Thread.currentThread().getId() + ", " + "sort code changed to " + sortCode);
		}
		finally
		{
			detailsLock.unlock();
		}
		System.out.println("Thread with id: " + Thread.currentThread().getId() + " changing sort code of " + accountNumber + ", DONE.");
	}

	public void setValidFrom(Calendar validFrom)
	{
		this.validFrom = validFrom;
	}

	public void setValidUntil(Calendar validUntil)
	{
		this.validUntil = validUntil;
	}

	// Deposit Money - Deposits money (double amount) into the accounts balance.
	public void depositMoney(double amount)
	{
		System.out.println("Thread with id: " + Thread.currentThread().getId() + ", account number: " + accountNumber);
		
		balanceLock.lock();

		try
		{
			System.out.println("Thread with id: " + Thread.currentThread().getId() + ", " + "depositing " + amount + " to account " + accountNumber);

			balance += amount;

			System.out.println("Thread with id: " + Thread.currentThread().getId() + ", " + "total balance of account " + accountNumber + ": " + balance);

			enoughBalanceCondition.signalAll();
		}
		finally
		{
			balanceLock.unlock();
		}
		
		System.out.println("Thread with id: " + Thread.currentThread().getId() + ", depositing to account " + accountNumber + ", DONE.");
	}

	// Withdraw Money - Withdraws money (double amount) from the accounts balance.
	// Makes sure there is enough money in
	// the account before withdrawing any money
	public void withdrawMoney(double amount) throws InterruptedException
	{
		System.out.println("Thread with id: " + Thread.currentThread().getId() + ", account number: " + accountNumber);

		boolean stillWaiting = true;
		balanceLock.lock();

		try
		{
			System.out.println("Thread with id: " + Thread.currentThread().getId() + ", " + "withdrawing " + amount + " from account " + accountNumber + "...");
			
			while (balance < amount)
			{
				if (!stillWaiting)
					Thread.currentThread().interrupt();
				
				System.out.println("Thread with id: " + Thread.currentThread().getId() + ", Not enough money in account " + accountNumber + " to withdraw " + amount + " from it. Waiting 10 seconds...");
				stillWaiting = enoughBalanceCondition.await(10, TimeUnit.SECONDS);
			}

			balance -= amount;

			System.out.println("Thread with id: " + Thread.currentThread().getId() + ", " + "total balance of account " + accountNumber + ": " + balance);
		}
		finally
		{
			balanceLock.unlock();
		}
		
		System.out.println("Thread with id: " + Thread.currentThread().getId() + ", withdrawing from account " + accountNumber + ", DONE.");
	}

	// Transfer Money - Withdraws money (double amount) from the accounts balance
	// and deposits that money into another
	// account (account receiver). Makes sure there is enough money in the first
	// account before withdrawing any money
	public void transferMoney(double amount, Account receiver) throws InterruptedException
	{
		System.out.println("Thread with id: " + Thread.currentThread().getId() + ", sender number: " + accountNumber + " , receiver number: " + receiver.getAccountNumber());

		boolean stillWaiting = true;
		balanceLock.lock();

		try
		{
			System.out.println("Thread with id: " + Thread.currentThread().getId() + ", " + "transferring " + amount + " from account " + accountNumber + "...");
			
			while (balance < amount)
			{
				if (!stillWaiting)
					Thread.currentThread().interrupt();

				System.out.println("Thread with id: " + Thread.currentThread().getId() + ", Not enough money in account " + accountNumber + " to transfer " + amount + " from it. Waiting 10 seconds...");
				stillWaiting = enoughBalanceCondition.await(10, TimeUnit.SECONDS);
			}
			
			balance -= amount;
			receiver.depositMoney(amount);

			System.out.println("Thread with id: " + Thread.currentThread().getId() + ", " + "total balance of sender account " + accountNumber + ": " + balance);
		}
		finally
		{
			balanceLock.unlock();
		}
		
		System.out.println("Thread with id: " + Thread.currentThread().getId() + ", transferring from account " + accountNumber + ", DONE.");
	}

	public void addInterest(double interestRate)
	{
		balance += balance * interestRate;
	}

	public void displayBalance()
	{
		System.out.println("Thread with id: " + Thread.currentThread().getId() + ", account Number: " + accountNumber);
		System.out.println("Thread with id " + Thread.currentThread().getId() + ", " + "displaying balance of account: " + accountNumber);
		System.out.println("Your balance is: " + balance);
		System.out.println("Thread with id " + Thread.currentThread().getId() + ", " + "balance displayed");
		System.out.println("Thread with id: " + Thread.currentThread().getId() + " displaying balance of account " + accountNumber + ", DONE.");
	}

	public String getAccountInfo()
	{
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		return "Account Information: "
			 + "\nAccount Number: " + accountNumber
			 + "\nHolder Number: " + holderNumber
			 + "\nSort Code: " + sortCode
			 + "\nValid From: " + dateFormatter.format(validFrom.getTime())
			 + "\nValid Until: " + dateFormatter.format(validUntil.getTime());
	}
}