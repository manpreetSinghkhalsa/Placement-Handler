package com.minorproject.gtbit;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import resources.MyJButton;
import resources.StaticMethodClass;

public class OptionBar extends JPanel implements ActionListener
{ 
	private final String searchLoc = "images\\search_24pxwhite.png";
	private final String deleteLoc = "images\\delete70_24pxwhite.png";
	private final String mailLoc = "images\\mail_24pxwhite.png";
	private final String logoutLoc = "images\\print_24pxwhite.png";
	private final String addLoc = "images\\add_24pxwhite.png";
	private final String helpLoc = "images\\questions1_24pxwhite.png";
	private final String insertLoc = "images\\document170_24pxwhite.png";
	MyJButton search, delete, mail, logout, add, help,insert;
	static String location = "";

	OptionBar()
	{
		search = new MyJButton(new ImageIcon(searchLoc), "SEARCH");
		delete = new MyJButton(new ImageIcon(deleteLoc), "DELETE");
		mail = new MyJButton(new ImageIcon(mailLoc), "MAIL");
		logout = new MyJButton(new ImageIcon(logoutLoc), "LOG OUT");
		add = new MyJButton(new ImageIcon(addLoc), "ADD");
		help = new MyJButton(new ImageIcon(helpLoc), "HELP");
		insert = new MyJButton(new ImageIcon(insertLoc), "INSERT");
		setLayout(new GridBagLayout());

		Insets insets = new Insets(10, 10, 10, 10);
		int i = -1;

		add(add, new GridBagConstraints(++i, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(search, new GridBagConstraints(++i, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(insert, new GridBagConstraints(++i, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(delete, new GridBagConstraints(++i, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(mail, new GridBagConstraints(++i, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(help, new GridBagConstraints(++i, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(logout, new GridBagConstraints(++i, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		
		setBackground(StaticMethodClass.blueColor);

		
		StaticMethodClass.setForeground(Color.WHITE, help, mail, logout, delete, search, add,insert);
		StaticMethodClass.setBackground(Color.BLACK, help, mail, logout, delete, search, add,insert);
		
		addAll(help, mail, logout, delete, search, add,insert);
		

	}

	void addAll(JButton... comp)
	{
		for (JButton c : comp)
			c.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		JButton buttonName = (JButton) event.getSource();
		if (buttonName == add)
		{

			Component[] components = MainFrame.globalInstance.mainContent
					.getComponents();
			if (components.length > 1)
				MainFrame.globalInstance.mainContent.remove(1); // jtable yaha se
			StaticMethodClass.addErrorLabel("Validating Excel file", 2000);
			if (findExcelFile())
				GetDatabaseName.batchPlacementJPanel();

		} else if (buttonName == help)
		{
			HelpClass.getJTableonFrame();
		} else if (buttonName == mail)
		{
			UserIDPassClass.getJTableonFrame(null);
		}  else if (buttonName == delete)
		{
			DeleteRow.getJTableonFrame();
		} else if (buttonName == search)
		{
			SearchPanel.getJTableonFrame();
//			SimpleAdvancedSearchClass.getJTableonFrame();			
		}
		
		else if (buttonName == insert)
		{
			InsertRowToDatabase.getJTableonFrame();
		}
		else if(buttonName==logout)
		{
			StaticMethodClass.doLogOut();
		}
	}

	boolean findExcelFile()
	{

		final JFileChooser fileChooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Only Excel Files",
				"xls", "xlsx");
		fileChooser.setFileFilter(filter);
		int response = fileChooser.showOpenDialog(null);
		if (response == JFileChooser.APPROVE_OPTION)
		{
			StaticMethodClass.addErrorLabel("Validating File Extension", 3000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
			location = fileChooser.getSelectedFile().toString();
			if (validate(location))
			{
				StaticMethodClass.addErrorLabel("Validation Done...", 3000);
				MainFrame.globalInstance.repaint();
				MainFrame.globalInstance.revalidate();
				return true;
			} else
			{
				StaticMethodClass.addErrorLabel("Wrong File Detected.", 5000);
				MainFrame.globalInstance.repaint();
				MainFrame.globalInstance.revalidate();
			}

		}
		return false;
	}

	// This method will validate that the selected file is Excel file ONLY and
	// not any other file
	boolean validate(String location)
	{

		StringTokenizer st = new StringTokenizer(location, ".");
		int num = st.countTokens();
		while (num > 1)
		{
			st.nextToken();
			num--;
		}
		String extension = st.nextToken();
		if (extension.equals("xls") || extension.equals("xlsx"))
			return true;
		else
			return false;
	}
}
