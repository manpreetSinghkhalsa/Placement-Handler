package com.minorproject.gtbit;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import resources.MyJButton;
import resources.MyJTextField;
import resources.StaticMethodClass;

class GetDatabaseName extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String clickImageLoc = "images\\arrow63_24pxwhite2.png";
	MyJTextField textfield;
	MyJTextField sheet;
	MyJButton clickNext;

	public Insets getInsets()
	{
		return new Insets(20, 20, 20, 20);
	}

	public GetDatabaseName()
	{
		Insets insets = new Insets(10, 10, 10, 10);
		textfield = new MyJTextField(30, "Enter Batch Name");
		sheet = new MyJTextField(30, "Enter Sheet Name ");
		clickNext = new MyJButton(new ImageIcon(clickImageLoc));
		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,
				Font.PLAIN, StaticMethodClass.normalFontSize), clickNext,
				textfield, sheet);
		StaticMethodClass.setBorder(null,sheet,textfield );
		
		StaticMethodClass.setBackground(Color.BLACK, clickNext);//sheet,textfield,
		
		setBackground(StaticMethodClass.blueColor);
		StaticMethodClass.setTransparent(clickNext);
		setLayout(new GridBagLayout());
		add(textfield, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(sheet, new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(clickNext, new GridBagConstraints(0, 2, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		
		
		clickNext.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event)
	{
		final String databaseName = textfield.getText();

		if (!databaseName.equals("")&&(!sheet.getText().equals("")))
		{
			clickNext.setEnabled(false);
			new Thread(new Runnable()
			{
				
				@Override
				public void run()
				{
					
					new CreateDatabaseInMySQL(OptionBar.location, databaseName,sheet.getText());
				}
			}).start();
			clickNext.setEnabled(true);

		} else
		{
			StaticMethodClass.addErrorLabel("Enter the Batch Name...!!", 5000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();

		}

	}

	static void batchPlacementJPanel()
	{
		JPanel panel = new GetDatabaseName();
		MainFrame.mainContent.add(panel, new GridBagConstraints(
				1, 0, 0, 0, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		MainFrame.globalInstance.repaint();
		MainFrame.globalInstance.revalidate();
	}
}
