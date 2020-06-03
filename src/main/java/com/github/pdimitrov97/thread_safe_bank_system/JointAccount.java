package com.github.pdimitrov97.thread_safe_bank_system;

import java.util.Calendar;

public class JointAccount extends Account
{
	private static final double interestRate = 0.01;
	private int secondHolderNumber;

	public JointAccount(int holderNumber, int secondHolderNumber, int accountNumber, String sortCode, Calendar validFrom, Calendar validUntil)
	{
		super(holderNumber, accountNumber, sortCode, validFrom, validUntil);
		this.secondHolderNumber = secondHolderNumber;
	}

	public int getSecondHolderNumber()
	{
		return this.secondHolderNumber;
	}

	public void setSecondHolderNumber(int secondHolderNumber)
	{
		this.secondHolderNumber = secondHolderNumber;
	}

	public void addInterest()
	{
		super.addInterest(interestRate);
	}

	public String getAccountInfo()
	{
		return super.getAccountInfo() + "\nSecond Account Number " + secondHolderNumber;
	}
}