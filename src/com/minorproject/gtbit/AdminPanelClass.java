package com.minorproject.gtbit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import resources.MyJButton;
import resources.StaticMethodClass;

class AdminPanelClass extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String addLoc = "images\\Add-New-24pxwhite.png";
	String viewLoc = "images\\viewlog_24pxwhite.png";
	String logoutLoc = "images\\print_24pxwhite.png";
	String viewAllLoc = "images\\User-Group_24pxwhite.png";
	
	MyJButton add,viewLog,logout,viewAll;
	Insets insets= new Insets(10,10,10,10);
	public AdminPanelClass()
	{
		add = new MyJButton(new ImageIcon(addLoc), "Add new Account");
		viewAll = new MyJButton(new ImageIcon(viewAllLoc),"View All Users");
		viewLog = new MyJButton(new ImageIcon(viewLoc), "View Logs");
		logout = new MyJButton(new ImageIcon(logoutLoc), "Log Out");
		
		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont, Font.BOLD, StaticMethodClass.normalFontSize), add,viewLog,viewAll);
		setBackground(StaticMethodClass.blueColor);
		StaticMethodClass.setBackground(Color.BLACK, add,viewLog,logout,viewAll);
		StaticMethodClass.setForeground(Color.WHITE, add,viewLog,logout,viewAll);

		setLayout(new GridBagLayout());
		int i=-1;
		add(add,new GridBagConstraints(0, ++i, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		add(viewAll,new GridBagConstraints(0, ++i, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		add(viewLog,new GridBagConstraints(0, ++i, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		add(logout,new GridBagConstraints(0, ++i, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		add.addActionListener(this);
		viewLog.addActionListener(this);
		logout.addActionListener(this);
		viewAll.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		JButton button = (JButton) event.getSource();
		if(button==add)
		{
			AddNewUser.getJTableonFrame();
		}
		else if(button==viewLog)
		{
			AdminLogView.getJTableonFrame();
		}
		else if (button==logout)
		{
			StaticMethodClass.doLogOut();
		}
		else if(button==viewAll)
		{
			AdminViewAll.getJTableonFrame();
		}
	}

	static void getJTableonFrame()
	{
		Component[] components = MainFrame.mainContent.getComponents(); 
		if(components.length==1)
		{
			MainFrame.mainContent.remove(0); 
		}
		System.out.println("length: "+components.length);	
		MainFrame.mainContent.add(new AdminPanelClass(),
				new GridBagConstraints(0, 0, 0, 0, 1,
						1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(StaticMethodClass.screenY / 6, 0, StaticMethodClass.screenY / 6, 0), 0, 0));
		MainFrame.globalInstance.repaint();
		MainFrame.globalInstance.revalidate();		
	}

}
