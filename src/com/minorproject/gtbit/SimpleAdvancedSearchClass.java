package com.minorproject.gtbit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import resources.MyJButton;
import resources.MyJTextField;
import resources.StaticMethodClass;

class SimpleAdvancedSearchClass extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MyJTextField enrollmentNumber, contactNumber, dob, percentage10,name,
			percentage12, percentageDip, email, pendingBacklog, totalBacklogs,aggregate;
	String options[] = { "Select Option", "Greater than", "Lesser than",
			"Equal to", "Greater than equal to", "Lesser than equal to" };
	String branches[] = { "Select Branch Option", "CSE-1", "CSE-2", "CSE-3", "IT-1",
			"IT-2", "IT-3", "ECE-1", "ECE-2", "ECE-3", "EEE" };
	String placedOptions[] = {"Placed","YES","NO"};
	String placedCompany[] = {"Select Database First"};
	
	static JComboBox list10, list12, listDip, listDatabaseNames, branchSec,
			listAggregate, listPending, listTotal, listPlaced, listPlacedCompany;
	MyJButton search;
	Insets insets = new Insets(10, 10, 10, 10);

	StringBuilder query = new StringBuilder(
			"Select  frequentinfo.`Enrollment_Number`, `Name`, `Branch_Section`, `Contact_Number`,  `Date_of_Birth`, `Email_Id`, `Father_Name` from frequentinfo where ");
	StringBuilder original = query;

	boolean isFirst = true;
	String and = "";

	public Insets getInsets()
	{
		return new Insets(20, 20, 20, 20);
	}
	
	boolean simple=true;
	public SimpleAdvancedSearchClass(final boolean simple)
	{
		this.simple = simple;
		setBackground(StaticMethodClass.blueColor);
		listDatabaseNames = new JComboBox(getDatabaseNames());
		search = new MyJButton(" Search ");
		StaticMethodClass.setBackground(Color.BLACK, search);
		StaticMethodClass.setForeground(Color.WHITE, search);
		
		if(simple)
			getSimpleSearch();
		else
			getAdvancedSearch();

		search.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String databaseName = (String) listDatabaseNames
						.getSelectedItem();
				int selectedDatabase = listDatabaseNames.getSelectedIndex();
				Boolean check = false;
				
				if (selectedDatabase == 0)
				{
					StaticMethodClass.addErrorLabel("Select a DATABASE First",
							3000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
				} 
				
				else if(simple)
				{
					if(enrollmentNumber.getText().equals("")&&contactNumber.getText().equals("")&&dob.getText().equals("")&&email.getText().equals("")&&name.getText().equals(""))
					{
						
						StaticMethodClass.addErrorLabel("Please Fill at least one Field", 3000);
						MainFrame.globalInstance.repaint();
						MainFrame.globalInstance.revalidate();
					}
					else
					{
						search.setEnabled(false);
						
						try
						{
							if(!name.getText().equals(""))
							{
								if (!isFirst)
									and = " and ";
								String name1 = StaticMethodClass.capitalize(name.getText());
								query.append(and
										+ " frequentinfo.name LIKE '%"
										+ name1 + "%'");
								check = true;
								isFirst = false;

							}
							if (validateEnrollment())
							{
								if (!isFirst)
									and = " and ";
								String enroll = enrollmentNumber.getText();
								while(enroll.charAt(0)=='0')
								{
									enroll = enroll.substring(1);
								}
								query.append(and
										+ " frequentinfo.enrollment_number = '"
										+ enroll + "'");
								check = true;
								isFirst = false;
							}
							if (validateContactNumber())
							{
								if (!isFirst)
									and = " and ";
		
								query.append(and + " frequentinfo.contact_number = '"
										+ contactNumber.getText() + "'");
								check = true;
								isFirst = false;
							}
							if (validateDob())
							{
								if (!isFirst)
									and = " and ";
		
								query.append(and + " frequentinfo.date_of_birth = '"
										+ dob.getText() + "'");
								check = true;
								isFirst = false;
							}
							if (validateEmail())
							{
								if (!isFirst)
									and = " and ";
		
								query.append(and + " frequentinfo.email_id = '"
										+ email.getText() + "'");
								check = true;
								isFirst = false;
							}
							if(!check)
							{
								throw new NumberFormatException();
							}
						}
						catch(Exception ef)
						{
							StaticMethodClass.addErrorLabel("Check the input data, read Help for more.", 3000);
							MainFrame.globalInstance.repaint();
							MainFrame.globalInstance.revalidate();
						}
					}
				}
			else
			{
				if((!percentage12.getText().equals(""))&&(!percentageDip.getText().equals("")))
				{
					StaticMethodClass.addErrorLabel("Enter either 12th aggregate or diploma aggregate", 3000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
				}
				else if(branchSec.getSelectedIndex()==0&&list10.getSelectedIndex()==0&&list12.getSelectedIndex()==0&&listAggregate.getSelectedIndex()==0&&listPending.getSelectedIndex()==0&&listTotal.getSelectedIndex()==0&&listPlaced.getSelectedIndex()==0&&listPlacedCompany.getSelectedIndex()==0)
				{
					StaticMethodClass.addErrorLabel("Please Fill at least one Field and Select the Range", 3000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
				}
				else
				{
					try
					{
						if (validateBack(pendingBacklog.getText())
							&& (!getCondition(listPending).equals("")))
						{
							if (!isFirst)
								and = " and ";
							
							StringBuilder que = new StringBuilder("Select  frequentinfo.`Enrollment_Number`, `Name`, `Branch_Section`, `Contact_Number`,  `Date_of_Birth`, `Email_Id`, `Father_Name` from frequentinfo INNER JOIN college ON college.enrollment_number=frequentinfo.enrollment_number WHERE "); 
							que.append(and + " college.pending_backlogs "
									+ getCondition(listPending)
									+ Integer.parseInt(pendingBacklog.getText()));
							query=que;
							check = true;
							isFirst = false;
						}
						if (validateBack(totalBacklogs.getText())
								&& (!getCondition(listTotal).equals("")))
						{
							if (!isFirst)
							{
								and = " and ";
							}
								
					
							query.append(and + " frequentinfo.total_backlogs "
									+ getCondition(listTotal)
									+ Integer.parseInt(totalBacklogs.getText()));
	
							check = true;
							isFirst = false;
						}
						
						if (listPlaced.getSelectedIndex()!=0)
						{
							if (!isFirst)
							{
								and = " and ";
							}
								
					
							query.append(and + " frequentinfo.placed = '"
									+listPlaced.getSelectedItem().toString()+"'");
	
							check = true;
							isFirst = false;
						}

						if (listPlacedCompany.getSelectedIndex()!=0)
						{
							if (!isFirst)
							{
								and = " and ";
							}
							
							query.append(and + " frequentinfo.placed_company = '"
									+listPlacedCompany.getSelectedItem().toString()+"'");
	
							check = true;
							isFirst = false;
						}

						if (validateBranchSec())
						{
							if (!isFirst)
								and = " and ";
	
							query.append(and
									+ " frequentinfo.branch_section = '"
									+ branchSec.getSelectedItem().toString()
											.toUpperCase() + "'");
							check = true;
							isFirst = false;
						}
						if (validatePercentage(percentage10.getText())
								&& (!getCondition(list10).equals("")))
						{
							if (!isFirst)
								and = " and ";
	
							query.append(and + " frequentinfo.10th_aggregate "
									+ getCondition(list10)
									+ Float.parseFloat(percentage10.getText()));
							check = true;
							isFirst = false;
						}
						if ((validatePercentage(percentage12.getText())&& (!getCondition(list12).equals("")))||(validatePercentage(percentageDip.getText())
								&& (!getCondition(list12).equals(""))))
						{
							if (!isFirst)
								and = " and ";
							if(validatePercentage(percentage12.getText()))
							{
								query.append(and + " frequentinfo.12th_aggregate "
												+ getCondition(list12)
										+ Float.parseFloat(percentage12.getText()));
							}
							else if(validatePercentage(percentageDip.getText()))
							{
								query.append(and + " frequentinfo.Diploma_aggregate "
										+ getCondition(listDip)
										+ Float.parseFloat(percentageDip.getText()));
							}
							check = true;
							isFirst = false;
						}
						if (validatePercentage(aggregate.getText())
								&& (!getCondition(listAggregate).equals("")))
						{
							if (!isFirst)
								and = " and ";
	
							query.append(and + " frequentinfo.Aggregate "
									+ getCondition(listAggregate)
									+ Float.parseFloat(aggregate.getText()));
							check = true;
							isFirst = false;
						}
						if(!check)
						{
							throw new NumberFormatException();
						}
					}
					catch(Exception ef)
					{
						StaticMethodClass.addErrorLabel("Check the input data, read Help for more.", 3000);
						MainFrame.globalInstance.repaint();
						MainFrame.globalInstance.revalidate();
					}
				}
			}	
					
			

				if (check)
				{
					query.append(" ORDER BY frequentinfo.enrollment_number ;");
					System.out.println("query: " + query.toString());
					DisplayPreciseData.getJTableonFrame(databaseName,
							query.toString());
				}
				query = original;
				search.setEnabled(true);

			}
		});
	}

	void getAdvancedSearch()
	{
		branchSec = new JComboBox(branches);
		percentage10 = new MyJTextField(10, "10% ");
		percentage12 = new MyJTextField(10, "12% ");
		percentageDip = new MyJTextField(10, "Diploma % ");
		list10 = new JComboBox(options);
		list12 = new JComboBox(options);
		listAggregate = new JComboBox(options);
		listPending = new JComboBox(options);
		listTotal = new JComboBox(options);
		aggregate = new MyJTextField(10, "Aggregate");
		pendingBacklog = new MyJTextField(10, "Pending Backlogs");
		totalBacklogs = new MyJTextField(10, "Total Backlogs");
		listPlaced = new JComboBox(placedOptions);
//		listPlacedCompany = new JComboBox(placedCompany);

		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,
				Font.PLAIN, 18), branchSec, percentage10, percentage12,
				percentageDip, list10, list12, listAggregate,
				listPending, listTotal, pendingBacklog, totalBacklogs, search,aggregate,listDatabaseNames,listPlaced);
		
		StaticMethodClass.setBorder(null, branchSec, percentage10, percentage12,
				percentageDip, list10, list12, listAggregate,
				listPending, listTotal, pendingBacklog, totalBacklogs, search,aggregate,listDatabaseNames,listPlaced);

		this.setLayout(new GridBagLayout());
		
		add(branchSec, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		add(listDatabaseNames, new GridBagConstraints(1, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		add(list10, new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		
		add(list12, new GridBagConstraints(0,2, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		
		add(listAggregate, new GridBagConstraints(0, 3, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		add(percentage10, new GridBagConstraints(1, 1, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		add(percentage12, new GridBagConstraints(1, 2, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		add(percentageDip, new GridBagConstraints(2, 2, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));

		add(listPlaced, new GridBagConstraints(2, 3, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));


		add(aggregate, new GridBagConstraints(1, 3, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		add(listTotal, new GridBagConstraints(0, 4, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		add(totalBacklogs, new GridBagConstraints(1, 4, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		add(listPending, new GridBagConstraints(0, 5, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		add(pendingBacklog, new GridBagConstraints(1, 5, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		add(search, new GridBagConstraints(1, 6, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		listDatabaseNames.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String database = listDatabaseNames.getSelectedItem().toString();
				
				listPlacedCompany = new JComboBox(getCompanyNames(database));
				StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,
						Font.PLAIN, 18), listPlacedCompany);
				
				StaticMethodClass.setBorder(null, listPlacedCompany);

				add(listPlacedCompany, new GridBagConstraints(2, 4, 1, 1, 1, 1,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
				
				MainFrame.globalInstance.repaint();
				MainFrame.globalInstance.revalidate();
				
			}
		});
	}

	void getSimpleSearch()
	{
		enrollmentNumber = new MyJTextField(20, "Enrollment Number");
		contactNumber = new MyJTextField(20, "Contact Number");
		dob = new MyJTextField(20, "Date of Birth");
		email = new MyJTextField(20, "Email ID");
		name = new MyJTextField(20, "Name");
		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,
				Font.PLAIN, 18), enrollmentNumber, contactNumber, dob, email, listDatabaseNames,name);

		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,
				Font.BOLD, 18),search);
		
		
		StaticMethodClass.setBorder(null, enrollmentNumber, contactNumber, dob,
				email, search, listDatabaseNames,name);
		
		StaticMethodClass.setBackground(Color.BLACK, search);
		StaticMethodClass.setForeground(Color.WHITE, search);


		Insets in = new Insets(0,200,0,0);
		this.setLayout(new GridBagLayout());
		setPreferredSize(new Dimension((int)(StaticMethodClass.screenX / 1.5),
				(int)(StaticMethodClass.screenY / 1.2)));
		
		add(name, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, in, 0,
				0));

		add(listDatabaseNames, new GridBagConstraints(1, 0, 0, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,0,0,200), 0,
				0));

		add(enrollmentNumber, new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, in, 0,
				0));
		add(contactNumber, new GridBagConstraints(0, 2, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, in, 0,
				0));

		add(dob, new GridBagConstraints(0, 3, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, in, 0,
				0));

		add(email, new GridBagConstraints(0, 4, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, in, 0,
				0));
		
		add(search, new GridBagConstraints(0, 5, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, in, 0,
				0));
	}

	
	String getCondition(JComboBox list)
	{
		String ans = "";
		int index = list.getSelectedIndex();
		switch (index)
		{
		case 0:
			break; // Select
		case 1:
			ans = ">";
			break; // "Greater than",
		case 2:
			ans = "<";
			break; // "Lesser than",
		case 3:
			ans = "=";
			break; // "Equal to",
		case 4:
			ans = ">=";
			break; // "Greater than equal to",
		case 5:
			ans = "<=";
			break; // "Lesser than equal to"
		}
		return ans;
	}

	Vector getCompanyNames(String database)
	{
		Vector<String> vector = new Vector<String>();
		try
		{
			vector.add("Company");
			String url = "jdbc:mysql://localhost:3306/", user = "root", passwrd = "";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, user, passwrd);
			Statement st = con.createStatement();
			st.executeQuery("use "+database);
			ResultSet resultSet = st.executeQuery("SELECT distinct placed_company from frequentinfo");

			while (resultSet.next())
			{
				vector.add(resultSet.getString("placed_company"));
			}
			return vector;
		} catch (SQLException | InstantiationException | IllegalAccessException
				| ClassNotFoundException e)
		{
			StaticMethodClass.addErrorLabel("Problem while retrieving Company names", 2000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
		}
		return vector;
	}
	
	boolean validateEmail()
	{
		String emailString = email.getText();
		if (emailString.equals(""))
			return false;
		else
		{
			StringTokenizer st = new StringTokenizer(emailString, "@");
			st.nextToken();
			String lastCheck = st.nextToken();
			if (lastCheck.equals("gmail.com"))
				return true;
			return false;
		}
	}

	boolean validateBack(String number)
	{
		if (number.equals(""))
			return false;
		else
		{
			try
			{
				Integer.parseInt(number);
			} catch (NumberFormatException e)
			{
				return false;
			}
		}
		return true;
	}
	boolean validatePercentage(String percentage)
	{
		if (percentage.equals(""))
			return false;
		else
		{
			try
			{
				Float.parseFloat(percentage);
			} catch (NumberFormatException e)
			{
				return false;
			}
		}
		return true;
	}

	boolean validateDob()
	{
		String dobString = dob.getText();
		if (dobString.length() == 10||dobString.length()==9||dobString.length()==8)
			return true;
		return false;
	}

	boolean validateContactNumber()
	{
		String contactString = contactNumber.getText();
		if ((contactString.length() >= 10) && (contactString.length() <= 12))
			return true;
		return false;
	}

	boolean validateEnrollment()
	{

		String enrollmentString = enrollmentNumber.getText();

		if (!enrollmentString.equals(""))
		{
			if ((enrollmentString.length() >= 9)
					&& (enrollmentString.length() <= 10))
			{
				return true;
			}
		}
		return false;
	}

	boolean validateBranchSec()
	{
		int i = branchSec.getSelectedIndex();
		if(i!=0)
		{
			return true;
		}
		return false;
	}


	static Vector getDatabaseNames()
	{
		Vector<String> vector = new Vector<String>();
		try
		{

			vector.add("Select Database");
			String url = "jdbc:mysql://localhost:3306/", user = "root", passwrd = "";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, user, passwrd);
			Statement st = con.createStatement();
			st.executeQuery("use main;");
			ResultSet resultSet = st
					.executeQuery("select * from databasenames");

			while (resultSet.next())
			{
				vector.add(resultSet.getString("name"));
			}
			return vector;
		} catch (SQLException | InstantiationException | IllegalAccessException
				| ClassNotFoundException e)
		{

		}
		return vector;
	}

	static void getJTableonFrame(boolean simple)
	{
		Component[] components = MainFrame.mainContent
				.getComponents();
		if (components.length > 1)
			MainFrame.mainContent.remove(1); // jtable yaha se
															// form hoga

		MainFrame.mainContent.add(new SimpleAdvancedSearchClass(simple),
				new GridBagConstraints(0, 0, 1, 1, 1, 1,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(75, 200, 20, 200), 0, 0));
		MainFrame.globalInstance.repaint();
		MainFrame.globalInstance.revalidate();
	}
}
