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
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import resources.MyJButton;
import resources.MyJTextField;
import resources.StaticMethodClass;

class DeleteRow extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MyJButton confirm;
	MyJTextField enrollmentNumber;
	Insets insets = new Insets(20, 50, 20, 10);
	static JComboBox listDatabaseNames;
	public DeleteRow()
	{
		setLayout(new GridBagLayout());
		confirm = new MyJButton("Confirm ");
		enrollmentNumber = new MyJTextField(12, "Enrollment Number");
				
		listDatabaseNames = new JComboBox(SimpleAdvancedSearchClass.getDatabaseNames());
		
		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,
				Font.PLAIN, StaticMethodClass.normalFontSize), enrollmentNumber,confirm,listDatabaseNames);

		StaticMethodClass.setBorder(null,  enrollmentNumber,confirm,listDatabaseNames);
		
		setBackground(StaticMethodClass.blueColor);
		StaticMethodClass.setBackground(Color.BLACK, confirm);
		StaticMethodClass.setForeground(Color.WHITE, confirm);
		add(enrollmentNumber, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		add(listDatabaseNames, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		add(confirm, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		confirm.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				JButton button = (JButton) event.getSource();
				if((button==confirm)&&(!enrollmentNumber.getText().equals(""))&&(listDatabaseNames.getSelectedIndex()!=0))
				{
					String enroll = enrollmentNumber.getText();
					while(enroll.charAt(0)=='0')
					{
						enroll = enroll.substring(1);
					}
					if(getDataDeleted(enroll,(String)listDatabaseNames.getSelectedItem()))
					{
						StaticMethodClass.addErrorLabel("Record Deleted", 1000);
						MainFrame.globalInstance.repaint();
						MainFrame.globalInstance.revalidate();
					}
					else
					{
						StaticMethodClass.addErrorLabel("Record has not been Deleted", 1000);
						MainFrame.globalInstance.repaint();
						MainFrame.globalInstance.revalidate();
					}
				}
				else if(enrollmentNumber.getText().equals(""))
				{
					StaticMethodClass.addErrorLabel("Enter Enrollment Number First", 1000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
				}
				else if(listDatabaseNames.getSelectedIndex()==0)
				{
					StaticMethodClass.addErrorLabel("Select database", 1000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
				}
			}
		});
	}
	
	boolean getDataDeleted(String enrol,String databaseName)
	{
		try
		{
			String url = "jdbc:mysql://localhost:3306/", user = "root", passwrd = "";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, user, passwrd);
			Statement st = con.createStatement();

			st.executeQuery("use " + databaseName);
			st.executeUpdate("delete from college where enrollment_number = '"+enrol+"'");
			st.executeUpdate("delete from school where enrollment_number = '"+enrol+"'");
			st.executeUpdate("delete from personalinfo where enrollment_number = '"+enrol+"'");
			st.executeUpdate("delete from frequentinfo where enrollment_number = '"+enrol+"'");
			st.close();
			con.close();
			return true;
		}
		catch(SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e)
		{
			StaticMethodClass.addErrorLabel("Deletion Done", 10000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
			return false;
		}
	}
	static void getJTableonFrame()
	{
		Component[] components = MainFrame.mainContent.getComponents(); 
		if(components.length>1)
			MainFrame.mainContent.remove(1); //jtable yaha se form hoga
			
		MainFrame.mainContent.add(new DeleteRow(),
				new GridBagConstraints(0, 1, 1, 1, 1, 1,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(20, 0, 20, 0), 0, 0));
		MainFrame.globalInstance.repaint();
		MainFrame.globalInstance.revalidate();
		
	}
}
