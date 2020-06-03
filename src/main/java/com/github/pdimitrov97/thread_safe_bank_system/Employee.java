package com.github.pdimitrov97.thread_safe_bank_system;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Employee extends User
{
	private Map<Integer, Holder> holders;

	public Employee(int refNumber, String fristName, String lastName, Calendar DOB, String address, String phoneNumber)
	{
		super(refNumber, fristName, lastName, DOB, address, phoneNumber);
		holders = new HashMap<>();
	}

	public Map<Integer, Holder> getHolders()
	{
		return holders;
	}

	public Holder getHolder(int refNumber)
	{
		return holders.get(refNumber);
	}

	public boolean addHolder(int refNumber, Holder newHolder)
	{
		holders.put(refNumber, newHolder);
		return true;
	}

	public boolean createBasicAccount(int holderNumber, int accountNumber, String sortCode, Calendar validFrom, Calendar validUntil)
	{
		BasicAccount newAccount = new BasicAccount(holderNumber, accountNumber, sortCode, validFrom, validUntil);
		
		for (Holder holder : holders.values())
		{
			if (holder.getRefNumber() == holderNumber)
			{
				holder.getAccounts().put(accountNumber, newAccount);
				return true;
			}
		}
		
		return false;
	}

	public boolean createSavingsAccount(int holderNumber, int accountNumber, String sortCode, Calendar validFrom, Calendar validUntil)
	{
		Account newAccount = new SavingsAccount(holderNumber, accountNumber, sortCode, validFrom, validUntil);
		
		for (Holder holder : holders.values())
		{
			if (holder.getRefNumber() == holderNumber)
			{
				holder.getAccounts().put(accountNumber, newAccount);
				return true;
			}
		}
		
		return false;
	}

	public boolean createJointAccount(int firstHolder, int secondHolder, int accountNumber, String sortCode, Calendar validFrom, Calendar validUntil)
	{
		Account newAccount = new JointAccount(firstHolder, secondHolder, accountNumber, sortCode, validFrom, validUntil);
		
		for (Holder holder1 : holders.values())
		{
			if (holder1.getRefNumber() == firstHolder)
			{
				holder1.getAccounts().put(accountNumber, newAccount);

				for (Holder holder2 : holders.values())
				{
					if (holder2.getRefNumber() == secondHolder)
					{
						holder2.getAccounts().put(accountNumber, newAccount);
						return true;
					}
				}
				
				return true;
			}
		}
		
		return false;
	}

	public boolean deleteAccount(int holderNumber, int accountNumber)
	{
		for (Holder holder : holders.values())
		{
			if (holder.getRefNumber() == holderNumber)
			{
				holder.getAccounts().remove(accountNumber);
				return true;
			}
		}
		
		return false;
	}
}