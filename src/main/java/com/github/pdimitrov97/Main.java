package com.github.pdimitrov97;

import com.github.pdimitrov97.thread_manager.ThreadGUI;
import com.github.pdimitrov97.thread_safe_bank_system.BankMain;

public class Main
{
	public static void main(String args[])
	{
		ThreadGUI g = new ThreadGUI();
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
}