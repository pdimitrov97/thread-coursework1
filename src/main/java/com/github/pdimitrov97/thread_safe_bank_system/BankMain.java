package com.github.pdimitrov97.thread_safe_bank_system;

import java.util.GregorianCalendar;

public class BankMain
{
	public static void main(String[] args) throws InterruptedException
	{
		BankMain m = new BankMain();
		
		try
		{
			m.startBank();
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public void startBank() throws InterruptedException
	{
		// Set up some users and accounts
		Account b1 = new BasicAccount(509493, 364697, "87-65-46", new GregorianCalendar(2018, 9, 1), new GregorianCalendar(2022, 9, 1));
		Account s1 = new SavingsAccount(389459, 855845, "75-93-83", new GregorianCalendar(2016, 8, 1), new GregorianCalendar(2020, 8, 1));
		Account j1 = new JointAccount(509493, 389459, 729823, "43-43-94", new GregorianCalendar(2017, 11, 1), new GregorianCalendar(2021, 11, 1));
		Account j2 = new JointAccount(509494, 389460, 729824, "44-44-95", new GregorianCalendar(2017, 11, 1), new GregorianCalendar(2021, 11, 1));
		Holder user1 = new Holder(389459, "John", "Smith", new GregorianCalendar(1986, 1, 31), "G5 1HB", "07712349967");
		Holder user2 = new Holder(509493, "Peter", "Colin", new GregorianCalendar(1986, 2, 25), "G10 3H:", "07799671234");
		Employee employee = new Employee(111111, "Clara", "Marks", new GregorianCalendar(1970, 9, 1), "B12 12B", "07000000001");

		user1.addAccount(j1.getAccountNumber(), j1);
		user2.addAccount(j1.getAccountNumber(), j1);
		user1.addAccount(s1.getAccountNumber(), s1);
		user2.addAccount(b1.getAccountNumber(), b1);
		employee.addHolder(user1.getRefNumber(), user1);
		employee.addHolder(user2.getRefNumber(), user2);

		// This covers point 1 - Two account holders are trying to check the balance simultaneously.
		System.out.println("Test 1");
		DisplayBalanceRunnable db1 = new DisplayBalanceRunnable(user1.getAccount(j1.getAccountNumber()));
		DisplayBalanceRunnable db2 = new DisplayBalanceRunnable(user2.getAccount(j1.getAccountNumber()));

		Thread t1 = new Thread(db1);
		Thread t2 = new Thread(db2);
		t1.start();
		t2.start();
		Thread.sleep(1000);

		// This covers point 2 - One account holder tries to check the balance while the other is depositing/withdrawing money.
		System.out.println("\n\nTest 2");
		DepositMoneyRunnable dm1 = new DepositMoneyRunnable(user1.getAccount(j1.getAccountNumber()), 5.0);
		DisplayBalanceRunnable db3 = new DisplayBalanceRunnable(user2.getAccount(j1.getAccountNumber()));

		Thread t3 = new Thread(dm1);
		Thread t4 = new Thread(db3);
		t3.start();
		t4.start();
		Thread.sleep(1000);

		// This covers point 3 - The two account holders are trying simultaneously to deposit/withdraw money & then check the balance.
		System.out.println("\n\nTest 3");
		DepositMoneyRunnable dm2 = new DepositMoneyRunnable(user1.getAccount(j1.getAccountNumber()), 5.0);
		DisplayBalanceRunnable db4 = new DisplayBalanceRunnable(user1.getAccount(j1.getAccountNumber()));
		WithdrawMoneyRunnable wm1 = new WithdrawMoneyRunnable(user2.getAccount(j1.getAccountNumber()), 4.0);
		DisplayBalanceRunnable db5 = new DisplayBalanceRunnable(user2.getAccount(j1.getAccountNumber()));

		Thread t5 = new Thread(dm2);
		Thread t6 = new Thread(db4);
		Thread t7 = new Thread(wm1);
		Thread t8 = new Thread(db5);
		t5.start();
		t6.start();
		t7.start();
		t8.start();
		Thread.sleep(1000);

		// This covers point 4 - Same as 3, but at the same time a bank employee is in the process of completing a standing order in/out the account.
		System.out.println("\n\nTest 4");
		DepositMoneyRunnable deposit = new DepositMoneyRunnable(user1.getAccount(j1.getAccountNumber()), 5.0);
		DisplayBalanceRunnable display = new DisplayBalanceRunnable(user1.getAccount(j1.getAccountNumber()));
		WithdrawMoneyRunnable withdraw = new WithdrawMoneyRunnable(user2.getAccount(j1.getAccountNumber()), 4.0);
		DisplayBalanceRunnable display2 = new DisplayBalanceRunnable(user2.getAccount(j1.getAccountNumber()));
		TransferMoneyRunnable transfer_emp = new TransferMoneyRunnable(employee.getHolder(user1.getRefNumber()).getAccount(j1.getAccountNumber()), employee.getHolder(user2.getRefNumber()).getAccount(b1.getAccountNumber()), 5.0);
		DisplayBalanceRunnable display_emp = new DisplayBalanceRunnable(employee.getHolder(user2.getRefNumber()).getAccount(j1.getAccountNumber()));

		Thread t15 = new Thread(deposit);
		Thread t16 = new Thread(display);
		Thread t17 = new Thread(withdraw);
		Thread t18 = new Thread(display2);
		Thread t19 = new Thread(transfer_emp);
		Thread t20 = new Thread(display_emp);
		t15.start();
		t16.start();
		t17.start();
		t18.start();
		t19.start();
		t20.start();
		Thread.sleep(1000);

		// This covers pont 5 - There are insufficient funds to complete a withdraw.
		System.out.println("\n\nTest 5");
		DepositMoneyRunnable dm3 = new DepositMoneyRunnable(user1.getAccount(j1.getAccountNumber()), 5.0);
		DepositMoneyRunnable dm4 = new DepositMoneyRunnable(user2.getAccount(j1.getAccountNumber()), 5.0);
		DepositMoneyRunnable dm5 = new DepositMoneyRunnable(user1.getAccount(j1.getAccountNumber()), 5.0);
		WithdrawMoneyRunnable wm2 = new WithdrawMoneyRunnable(user1.getAccount(j1.getAccountNumber()), 10.0);
		TransferMoneyRunnable tm1 = new TransferMoneyRunnable(user2.getAccount(j1.getAccountNumber()), user1.getAccount(s1.getAccountNumber()), 5.0);
		WithdrawMoneyRunnable wm3 = new WithdrawMoneyRunnable(user1.getAccount(s1.getAccountNumber()), 6.0);

		Thread t9 = new Thread(dm3);
		Thread t10 = new Thread(dm4);
		Thread t11 = new Thread(dm5);
		Thread t12 = new Thread(wm2);
		Thread t13 = new Thread(tm1);
		Thread t14 = new Thread(wm3);
		t9.start();
		t10.start();
		t11.start();
		t12.start();
		t13.start();
		t14.start();
		Thread.sleep(1000);

		// This covers point 6 - Two bank employees are trying simultaneously to modify the details of a bank account.
		System.out.println("\n\nTest 6");
		Employee e1 = new Employee(677589, "Jamie", "Arnold", new GregorianCalendar(1983, 11, 8), "G4 8YT", "04456876379");
		Employee e2 = new Employee(576987, "Paul", "Taylor", new GregorianCalendar(1989, 11, 3), "G7 9JL", "03676298895");
		Holder h1 = new Holder(918370, "Lauren", "Mckenzie", new GregorianCalendar(1987, 9, 1), "G5 3LM", "07890567253");

		e1.addHolder(h1.getRefNumber(), h1);
		e2.addHolder(h1.getRefNumber(), h1);
		h1.addAccount(b1.getAccountNumber(), b1);

		ModifySortCodeRunnable msc1 = new ModifySortCodeRunnable(e1.getHolder(918370).getAccount(b1.getAccountNumber()), "37-08-90");
		ModifySortCodeRunnable msc2 = new ModifySortCodeRunnable(e2.getHolder(918370).getAccount(b1.getAccountNumber()), "59-12-34");
		Thread t22 = new Thread(msc1);
		Thread t23 = new Thread(msc2);
		t22.start();
		t23.start();
		Thread.sleep(1000);
	}
}