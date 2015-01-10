package com.minorproject.gtbit;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import resources.MyJButton;
import resources.MyJPasswordField;
import resources.MyJTextField;
import resources.StaticMethodClass;

public class LoginPanel extends JPanel implements ActionListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MyJPasswordField passwordField = new MyJPasswordField(20, "Password");
	MyJTextField userName = new MyJTextField(20, "userName");
	private final String nextIconLocation = "images\\login17_64.png";
	JCheckBox checkBox = new JCheckBox("Admin");
	MyJButton nextScreen = new MyJButton(new ImageIcon(nextIconLocation));
	Insets insets = new Insets(10, 0, 20, 0);

	public Insets getInsets() {
		return new Insets(20, 20, 20, 20);
	}

	public LoginPanel() {

		passwordField.setText("manpreet");
		userName.setText("manpreet");
		setLayout(new GridBagLayout());

		// change accordingly
		setBackground(StaticMethodClass.blueColor);

		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,
				Font.PLAIN, StaticMethodClass.normalFontSize), userName,
				passwordField);

		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,
				Font.BOLD, StaticMethodClass.normalFontSize),checkBox);
		
		StaticMethodClass.setBorder(null, userName, passwordField, nextScreen,
				checkBox);

		//---Setting CheckBox Transparent------------//
		checkBox.setOpaque(false);
		checkBox.setContentAreaFilled(false);
		checkBox.setBorderPainted(false);
		checkBox.setForeground(Color.WHITE);
		//-------------------------------------------//
		StaticMethodClass.setTransparent(nextScreen);

		add(userName, new GridBagConstraints(0, 0, 0, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(passwordField, new GridBagConstraints(0, 1, 0, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));

		add(checkBox, new GridBagConstraints(0, 2, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, insets, 0, 0));

		add(nextScreen, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,170,0,0), 0,
				0));

		nextScreen.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String user = userName.getText();
		String password = new String(passwordField.getPassword());
		
		boolean admin = checkBox.isSelected();
		System.out.println("admin: "+admin);
		if(!admin)
		{
			if (validateNormalLogin(user, password))
			{
				StaticMethodClass.id = user;

				StaticMethodClass.addErrorLabel("Validating Login", 1000);
				MainFrame.globalInstance.repaint();
				MainFrame.globalInstance.revalidate();
	
				MainFrame.mainContent.removeAll();
	
				MainFrame.mainContent.add(new OptionBar(),
						new GridBagConstraints(0, 0, 0, 0, 1, 1,
								GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5,
										0), 0, 0));
	
				MainFrame.globalInstance.repaint();
				MainFrame.globalInstance.revalidate();
			}	
		} 
		else if(admin)
		{

			if(user.equals("gtbit2014")&&password.equals("gtbitPlacements"))
			{
				StaticMethodClass.id = user;
				AdminPanelClass.getJTableonFrame();
			}
			else
			{
				StaticMethodClass.addErrorLabel("Incorrect Login id password combination", 1000);
				MainFrame.globalInstance.repaint();
				MainFrame.globalInstance.revalidate();
			}
		}
		else
		{
			StaticMethodClass.addErrorLabel("Incorrect Login id password combination", 1000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
		}
	}
	
	boolean validateNormalLogin(String id, String password)
	{
		String tableName = "maintable";
		try
		{
			String url = "jdbc:mysql://localhost:3306/main", user = "root", passwrd = "";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, user, passwrd);

			Statement statement = con.createStatement();
			String query = " select password from " + tableName + " where userName='"
					+ id + "';";
			ResultSet resultSet = statement.executeQuery(query);
			resultSet.next();
			String lastName = resultSet.getString("password");

			if (lastName.equals(password))
			{

				Date date  = new Date();
				String useDatabase = "use main";
				StaticMethodClass.login_time = ""+date;
				String insertVal = "INSERT INTO logtable(`userid`,`login_time`,`log_out_time`) values('"+id+"','"+date+"','in use')";
				
				statement.executeQuery(useDatabase);
				statement.executeUpdate(insertVal);
				System.out.println("data updation done................");
				statement.close();
				con.close();
				return true;
			}
			statement.close();
			con.close();

		}

		catch (Exception ex)
		{
			StaticMethodClass.addErrorLabel("Incorrect Login id password combination", 1000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
			ex.printStackTrace();
			return false;
		}
		return false;
	}
}
