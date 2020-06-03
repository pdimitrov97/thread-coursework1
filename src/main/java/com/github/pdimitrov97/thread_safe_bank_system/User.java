package com.github.pdimitrov97.thread_safe_bank_system;

import java.text.SimpleDateFormat;
import java.util.*;

public abstract class User
{
	private int refNumber;
	private String firstName;
	private String lastName;
	private Calendar DOB;
	private String address;
	private String phoneNumber;
	private Map<Integer, Account> accounts;

	public User()
	{
		refNumber = 1;
		firstName = "";
		lastName = "";
		DOB = new GregorianCalendar();
		address = "";
		phoneNumber = "";
		accounts = new HashMap<>();
	}

	public User(int refNumber, String firstName, String lastName, Calendar DOB, String address, String phonenNumber)
	{
		this.refNumber = refNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.DOB = DOB;
		this.address = address;
		this.phoneNumber = phonenNumber;
		accounts = new HashMap<>();
	}

	public int getRefNumber()
	{
		return refNumber;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public Calendar getDOB()
	{
		return DOB;
	}

	public String getAddress()
	{
		return address;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public boolean addAccount(int newAccountNumber, Account newAccount)
	{
		accounts.put(newAccountNumber, newAccount);
		return true;
	}

	public Map<Integer, Account> getAccounts()
	{
		return accounts;
	}

	public void setRefNumber(int refNumber)
	{
		this.refNumber = refNumber;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public void setDOB(Calendar DOB)
	{
		this.DOB = DOB;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public void displayAccount(int accountNumber)
	{
		System.out.println(accounts.get(accountNumber).getAccountInfo());
	}

	public Account getAccount(int accountNumber)
	{
		return accounts.get(accountNumber);
	}

	public void createStandingOrder(int account, String date, double amount, Account receiver) throws InterruptedException
	{
		Account current = accounts.get(account);
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

		if (timeStamp.equals(date))
			current.transferMoney(amount, receiver);

		System.out.println("Standing order created.");
	}
}