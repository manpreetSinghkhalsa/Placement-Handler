package com.minorproject.gtbit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import resources.MyJButton;
import resources.MyJLabel;
import resources.StaticMethodClass;

class AppTitle extends JPanel
/*
 * this class will consist of only the text "Placement Handler" which will be
 * using BorderLayout and this will be added at the Line_axis
 */
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String title = "PLACEMENT HANDLER";
	private final String appIconLocation = "images\\login17_16px.png";
	private final PanelIcon iconsPanel = new PanelIcon();
	private MyJLabel mainLabel, icon;

	static Color bg = new Color(0,0,0);// 129,207,224 //171,183,183
	
	public AppTitle()
	{
		// ------INITIALIZATION--------------------------------------------------//
		mainLabel = new MyJLabel(title);

		icon = new MyJLabel(new ImageIcon(appIconLocation));
		setLayout(new BorderLayout());

		// ------SET BACKGROUND,FONT,FOREGROUND AND BORDER OF THE
		// ELEMENTS--------//
		StaticMethodClass.setBackground(new Color(238,238,238), mainLabel);
		mainLabel.setFont(new Font(StaticMethodClass.defaultFont, Font.BOLD,
				StaticMethodClass.normalFontSize));
		StaticMethodClass.setForeground(Color.WHITE, icon,
				mainLabel, iconsPanel);
		StaticMethodClass.setBorder(null, icon, mainLabel);

		// ------ADDING THE PANEL, LABELS AND ICON TO THE NEXT
		// PANEL-------------//
		add(icon, BorderLayout.WEST);
		add(mainLabel, BorderLayout.CENTER);
		add(iconsPanel, BorderLayout.EAST);

		setBackground(bg);
	}
}

class PanelIcon extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * This class creates a panel which consists of CLOSE and MINIMIZE button
	 * this Panel is a right side of the main Title Bar of the application
	 * LAYOUT = FLOWLAYOUT
	 */
	private MyJButton close, minimize;
	private final String closeLocation = "images\\Cancel-24white.png";
	private final String minimizerLocation = "images\\minimize3_24pxwhite.png";

	public PanelIcon()
	{
		// ------INITIALISING THE TWO BUTTONS USING
		// IMAGE-------------------------------//
		close = new MyJButton(new ImageIcon(closeLocation));
		minimize = new MyJButton(new ImageIcon(minimizerLocation));

		// ------SETTING BACKGROUND AND BUTTON TRANSPARENT USING STATIC METHOD
		// CLASS---//
		StaticMethodClass.setBorder(null, close, minimize);
		StaticMethodClass.setTransparent(close, minimize);
		setActionListener(close, minimize);
		close.addActionListener(this);

		// ------SETTING LAYOUT AND ADDING THE BUTTONS AND THE
		// BACKGROUND--------------//
		setLayout(new FlowLayout());
		add(minimize);
		add(close);

		
		setBackground(AppTitle.bg);
	}

	// adding Action Listener to the buttons
	void setActionListener(JButton... ccompo)
	{
		for (JButton component : ccompo)
			component.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		JButton buttonName = (JButton) event.getSource();

		if (buttonName == minimize)
		{
			MainFrame.globalInstance.setState(JFrame.ICONIFIED);
		} else if (buttonName == close)
		{
			System.exit(0);
		}
	}
}
