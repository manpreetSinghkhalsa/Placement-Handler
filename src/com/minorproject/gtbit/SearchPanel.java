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

class SearchPanel extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MyJButton simple,advanced;
	Insets insets = new Insets(10, 10, 10, 10);
	String simpleLoc = "images\\VerySimpleSearch_24pxwhite.png";
	String advancedLoc = "images\\SimpleSearch_24pxwhite.png";
	public Insets getInsets()
	{
		return new Insets(20, 20, 20, 20);
	}

	public SearchPanel()
	{
		setBackground(StaticMethodClass.blueColor);
		
		simple = new MyJButton(new ImageIcon(simpleLoc), "Simple Search");
		advanced = new MyJButton(new ImageIcon(advancedLoc), "Advanced Search");
		
		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,Font.PLAIN,StaticMethodClass.normalFontSize),simple,advanced);
		StaticMethodClass.setBorder(null, simple,advanced);
		StaticMethodClass.setTransparent(simple,advanced);
	
		setLayout(new GridBagLayout());
		
		StaticMethodClass.setForeground(Color.WHITE, simple,advanced);
		add(simple, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		add(advanced, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));		

		simple.addActionListener(this);
		advanced.addActionListener(this);
	}
	public void actionPerformed(ActionEvent event)
	{
		JButton button = (JButton) event.getSource();
		if(button==simple)
		{
			SimpleAdvancedSearchClass.getJTableonFrame(true);
		}
		else if(button==advanced)
		{
			SimpleAdvancedSearchClass.getJTableonFrame(false);
		}
	}
	static void getJTableonFrame()
	{
		Component[] components = MainFrame.mainContent.getComponents();
		if (components.length > 1)
			MainFrame.mainContent.remove(1); 

		MainFrame.mainContent.add(new SearchPanel(),
				new GridBagConstraints(0, 0, 0, 0, 1,
						1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(10,0,10,0), 0, 0));
		MainFrame.globalInstance.repaint();
		MainFrame.globalInstance.revalidate();
	}
}
