package com.github.pdimitrov97.thread_safe_bank_system;

import java.util.Calendar;

public class BasicAccount extends Account
{
	private static final double interestRate = 0.01;

	public BasicAccount(int holderNumber, int accountNumber, String sortCode, Calendar validFrom, Calendar validUntil)
	{
		super(holderNumber, accountNumber, sortCode, validFrom, validUntil);
	}

	public void addInterest()
	{
		super.addInterest(interestRate);
	}

	public String getAccountInfo()
	{
		return super.getAccountInfo();
	}
}