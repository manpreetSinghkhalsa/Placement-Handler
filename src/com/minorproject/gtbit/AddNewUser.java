package com.minorproject.gtbit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import resources.MyJButton;
import resources.MyJPasswordField;
import resources.MyJTextField;
import resources.StaticMethodClass;

class AddNewUser extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MyJTextField name,position,email,userName;
	MyJPasswordField passwordField;



	String addLoc = "images\\Add-New-24pxwhite.png";
	String backLoc = "images\\back_24pxwhite.png";
	MyJButton add, back;
	Insets insets = new Insets(10, 0, 20, 0);

	public AddNewUser()
	{
		add = new MyJButton(new ImageIcon(addLoc), "Add");
		back = new MyJButton(new ImageIcon(backLoc), "Back");

		name = new MyJTextField(20, "Name");
		position = new MyJTextField(20, "Designation");
		email = new MyJTextField(20, "Email ID");
		userName = new MyJTextField(20, "UserName");
		passwordField = new MyJPasswordField(20, "Password");
		
		setLayout(new GridBagLayout());

		setBackground(StaticMethodClass.blueColor);

		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,
				Font.PLAIN, StaticMethodClass.normalFontSize), userName,email, position,name,
				passwordField);

		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,
				Font.BOLD, StaticMethodClass.normalFontSize), add, back);

		StaticMethodClass.setBorder(null, userName, passwordField,email, position,name, add, back);

		StaticMethodClass.setBackground(Color.BLACK, add, back);
		StaticMethodClass.setForeground(Color.WHITE, add, back);

		int i=-1;
		
		add(name, new GridBagConstraints(0, ++i, 0, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(position, new GridBagConstraints(0,++i, 0, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		
		add(email, new GridBagConstraints(0,++i, 0, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		
		add(userName, new GridBagConstraints(0, ++i, 0, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(passwordField, new GridBagConstraints(0,++i, 0, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));

		add(back, new GridBagConstraints(0, ++i, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, insets, 0, 0));

		add(add, new GridBagConstraints(1, i, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						180, 7, 0), 0, 0));// new Insets(0,60,0,0)

		add.addActionListener(this);
		back.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event)
	{
		JButton button = (JButton) event.getSource();
		if (button == add)
		{
			String user = userName.getText();
			String pass = new String(passwordField.getPassword());
			if ((!user.equals("")) && (!pass.equals(""))&&!position.getText().equals("")&&!name.getText().equals("")&&!email.getText().equals(""))
			{
				if(checkUserName(user))
				{
					String values[] = {StaticMethodClass.capitalize(name.getText()),user,position.getText(),email.getText(),pass};
					addUser(values);
				}
				else
				{
					StaticMethodClass.addErrorLabel("USER Already Exists", 1000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
				}
			}
			else
			{
				StaticMethodClass.addErrorLabel("Fill both the fields", 1000);
				MainFrame.globalInstance.repaint();
				MainFrame.globalInstance.revalidate();
			}
		} else if (button == back)
		{
			AdminPanelClass.getJTableonFrame();
		}
	}
	
	boolean checkUserName(String name)
	{
		try
		{
			String url = "jdbc:mysql://localhost:3306/main", user = "root", passwrd = "";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, user,
					passwrd);

			Statement statement = con.createStatement();
			statement.executeQuery("use main");
			int counter=0;
			ResultSet resultSet = statement.executeQuery("SELECT * FROM mainTable WHERE name = '"+name+"'");
			while(resultSet.next())
			{
				counter++;
			}
			if(counter!=0)
			{
				return false;
			}
			return true;
		} catch (SQLException | InstantiationException
				| IllegalAccessException | ClassNotFoundException e)
		{
			StaticMethodClass.addErrorLabel("Problem occured", 1000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
			e.printStackTrace();
		}
		return false;
	}
	
	
	void addUser(String values[])
	{
				try
				{
					String url = "jdbc:mysql://localhost:3306/main", user = "root", passwrd = "";
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection con = DriverManager.getConnection(url, user,
							passwrd);

					Statement statement = con.createStatement();
					statement.executeQuery("use main");
					statement.executeUpdate("insert into mainTable values ('"
							+ values[0] + "','" + values[1]  + "','" + values[2]   + "','" + values[3] + "','" + values[4]+  "')");

					StaticMethodClass.addErrorLabel("USER ADDED", 1000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
				} catch (SQLException | InstantiationException
						| IllegalAccessException | ClassNotFoundException e)
				{
					StaticMethodClass.addErrorLabel("Problem occured, use another UserName", 1000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
					e.printStackTrace();
				}
	}

	static void getJTableonFrame()
	{
		Component[] components = MainFrame.mainContent.getComponents();
		if (components.length == 1)
		{
			MainFrame.mainContent.remove(0);
		}
		System.out.println("length: " + components.length);
		MainFrame.mainContent.add(new AddNewUser(), new GridBagConstraints(0,
				0, 0, 0, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(
						StaticMethodClass.screenY / 7, 0,
						StaticMethodClass.screenY / 7, 0), 0, 0));
		MainFrame.globalInstance.repaint();
		MainFrame.globalInstance.revalidate();
	}
}
