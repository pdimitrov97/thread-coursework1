package com.github.pdimitrov97.thread_manager;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class ThreadGUI implements ActionListener
{
	private JFrame frame;
	private JTable table;
	private JButton btnQuit;
	private JPanel panel_1;
	private JScrollPane scrollPane;
	private DefaultTableModel tableModel;
	private JTextField textField;
	private JButton btnSearch;
	private JButton btnStart;
	private JButton btnStop;
	private JComboBox<String> filterMenu;
	private Timer refreshTimer;
	private boolean updating;

	private ThreadManager m;
	private String mode;
	private String currentView;
	private String searchString;
	private JButton btnHelp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		ThreadGUI window = new ThreadGUI();
	}

	/**
	 * Create the application.
	 */
	public ThreadGUI()
	{
		m = new ThreadManager();
		mode = "All";
		currentView = "All";
		searchString = null;
		updating = false;
		initialize();
		refreshTimer.start();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 775, 479);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 757, 62);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		textField = new JTextField();
		textField.setBounds(12, 13, 260, 36);
		panel.add(textField);
		textField.setColumns(10);

		btnSearch = new JButton("Search");
		btnSearch.setBounds(275, 13, 112, 36);
		btnSearch.addActionListener(this);
		panel.add(btnSearch);

		filterMenu = new JComboBox<String>();
		filterMenu.setBounds(573, 17, 172, 29);
		filterMenu.addActionListener(this);
		filterMenu.addItem("All");

		for (String s : m.groups())
			filterMenu.addItem(s);

		panel.add(filterMenu);

		JLabel lblNewLabel = new JLabel("ThreadGroups:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(457, 12, 112, 36);
		panel.add(lblNewLabel);

		btnStart = new JButton("Start");
		btnStart.setBounds(20, 384, 97, 35);
		btnStart.addActionListener(this);
		frame.getContentPane().add(btnStart);

		btnStop = new JButton("Stop");
		btnStop.setBounds(120, 384, 97, 35);
		btnStop.addActionListener(this);
		frame.getContentPane().add(btnStop);

		panel_1 = new JPanel();
		panel_1.setBounds(0, 62, 757, 309);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		String[] columnNames = {"Thread Name", "Priority", "State", "Id", "Daemon", "ThreadGroup"};
		Object[][] empty = { };
		tableModel = new DefaultTableModel(empty, columnNames)
		{
			@Override
			public Class getColumnClass(int column)
			{
				switch (column)
				{
				   case 0:
				       return String.class;
				   case 1:
				       return Integer.class;
				   case 2:
				       return String.class;
				   case 3:
				       return Integer.class;
				   case 4:
				       return String.class;
					case 5:
						return String.class;
				   default:
					   return String.class;
				}
			}
		
			@Override
			public boolean isCellEditable(int row, int col)
			{
				return false;
			}
		};

		fillTable(ThreadManager.getAllThreads());

		table = new JTable(tableModel);
		table.setFont(new Font("Arial", Font.ITALIC, 17));
		table.setRowHeight(23);
		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(1).setMaxWidth(80);
		table.setEnabled(false);
		table.setAutoCreateRowSorter(true);
		scrollPane = new JScrollPane(table);
		panel_1.add(scrollPane);

		btnQuit = new JButton("Quit");
		btnQuit.addActionListener(this);
		btnQuit.setBounds(648, 384, 97, 35);
		frame.getContentPane().add(btnQuit);

		btnHelp = new JButton("Help");
		btnHelp.setBounds(387, 386, 96, 33);
		btnHelp.addActionListener(this);
		frame.getContentPane().add(btnHelp);

		refreshTimer = new Timer(1000, this);
	}

	private void fillTable(List<Thread> threads)
	{
		for (int i = 0; i < threads.size(); i++)
		{
			Object[] row = new Object[6];
			row[0] = threads.get(i).getName();
			row[1] = threads.get(i).getPriority();
			row[2] = threads.get(i).getState().toString();
			row[3] = threads.get(i).getId();
			row[4] = threads.get(i).isDaemon() + "";

			if (threads.get(i).getThreadGroup() != null)
				row[5] = threads.get(i).getThreadGroup().getName();

			tableModel.addRow(row);
		}
	}

	public void resetTable()
	{
		if (tableModel == null)
			return;

		updating = true;
		// Updating Table
		m.refresh();
		tableModel.setRowCount(0);
		List<Thread> threads = null;

		if (mode.equals("All"))
			threads = ThreadManager.getAllThreads();
		else if (mode.equalsIgnoreCase("filter"))
			threads = m.filterByGroup(currentView);

		if (searchString != null)
			threads = filterThreadsByKeyword(threads, searchString);

		fillTable(threads);
		tableModel.fireTableDataChanged();
		table.setModel(tableModel);

		// Updating DropDown Menu (only on change of groups)
		if ((filterMenu.getItemCount() - 1) != m.groups().size() && differentItems())
		{
			filterMenu.removeAllItems();
			filterMenu.addItem("All");

			for (String s : m.groups())
				filterMenu.addItem(s);

			filterMenu.setSelectedItem(currentView);
		}

		updating = false;
	}

	private boolean differentItems()
	{
		for (int i = 0; i < filterMenu.getItemCount() - 1; i++)
		{
			if (!filterMenu.getItemAt(i).equals(m.groups().get(i)))
				return false;
		}
		
		return true;
	}

	private List<Thread> filterThreadsByKeyword(List<Thread> threads, String keyword)
	{
		List<Thread> results = new ArrayList<>();
		String lowercaseKeyword = keyword.toLowerCase();

		for (Thread t : threads)
		{
			if (t.getName().toLowerCase().contains(lowercaseKeyword))
				results.add(t);
		}

		return results;
	}

	public void actionPerformed(ActionEvent event)
	{
		if (updating)
			return;

		if (event.getSource() == btnQuit)
			System.exit(0);
		else if (event.getSource() == filterMenu)
		{
			if (filterMenu.getSelectedItem() != null)
			{
				currentView = (String) filterMenu.getSelectedItem();
				
				if (currentView.equals("All"))
					mode = "All";
				else
					mode = "filter";
				
				resetTable();
			}
		}
		else if (event.getSource() == btnStart)
		{
			m.startThread();
			resetTable();
		}
		else if (event.getSource() == btnStop)
		{
			String name = JOptionPane.showInputDialog("Please Enter the name of the Thread you wish to stop:");
			m.stopThread(name);
			resetTable();
		}
		else if (event.getSource() == btnSearch)
		{
			if (textField.getText().length() > 0)
				searchString = textField.getText();
			else
				searchString = null;

			resetTable();
		}
		else if (event.getSource() == refreshTimer)
			resetTable();
		else if (event.getSource() == btnHelp)
		{
			JOptionPane.showMessageDialog(frame, "In order to search enter the thread's name you wish to find in the textfield then press the search button to check if it is there.\n"
											   + "When you wish to get back to viewing all the threads just empty the text field and press the search button.\n"
											   + "Pressing the start button will start a new thread immediately while pressing the stop button will open a new dialog asking for \n"
											   + "the name of the thread you wish to stop (from the ones you've started). After that just press ok and the thread will stop.)");
		}
	}
}