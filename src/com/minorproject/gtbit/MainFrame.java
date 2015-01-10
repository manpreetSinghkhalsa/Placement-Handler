package com.minorproject.gtbit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import resources.StaticMethodClass;

public class MainFrame extends JFrame 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static MainFrame globalInstance;
	public static JPanel mainContent = new JPanel();
	
	//error showing South Panel
	public static JPanel errorPanel = new JPanel();
		
	int screenX, screenY;

	public MainFrame() 
	{
		setSize(StaticMethodClass.dimension);
		setUndecorated(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		
		add(new AppTitle(), BorderLayout.NORTH);
		mainContent.setLayout(new GridBagLayout());
		mainContent.add(new LoginPanel(), new GridBagConstraints(0, 0, 0, 0, 1,
				1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(StaticMethodClass.screenY / 6, 0, StaticMethodClass.screenY / 6, 0), 0, 0));
		
		add(mainContent);

		errorPanel.setPreferredSize(new Dimension(screenX,80));
		errorPanel.setBackground(new Color (0,0,0));
		add(errorPanel, BorderLayout.SOUTH);
		
		setVisible(true);
	}

	public static void main(String agrs[]) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MainFrame.globalInstance = new MainFrame();
			}
		});
	}
}
