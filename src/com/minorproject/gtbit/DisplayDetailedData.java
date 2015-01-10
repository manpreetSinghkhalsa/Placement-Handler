package com.minorproject.gtbit;

import java.awt.BorderLayout;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;

import resources.MyJButton;
import resources.StaticMethodClass;

class DisplayDetailedData extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MyJButton back, edit, update;
	DefaultTableModel tableModel;
	JTable table;
	String databaseName = "";
	String enrollment = "";
	String values[];
	static String fields[] = { "Class_Roll_Number", "Enrollment_Number", "Name",
			"Branch_Section", "Contact_Number", "1st_Sem_Marks",
			"Backlog_1st_Sem", "2nd_Sem_Marks", "Backlog_2nd_Sem",
			"3rd_Sem_Marks", "Backlog_3rd_Sem", "4th_Sem_Marks",
			"Backlog_4th_Sem", "5th_Sem_Marks", "Backlog_5th_Sem",
			"6th_Sem_Marks", "Backlog_6th_Sem", "Aggregate", "Total_Backlogs",
			"Total_Cleared_Backlogs", "Pending_Backlogs", "10th_Aggregate",
			"10th_board", "Year_Of_Passing_10th", "12th_Aggregate",
			"12th_board", "Year_Of_Passing_12th", "Diploma_Aggregate",
			"Diploma_board", "Year_Of_Passing_Diploma", "Gap_in_Study",
			"value_added_Courses", "Dream_Company", "Gender", "Date_of_Birth",
			"Permanent_Address", "Correspondence_Address",
			"Alternate_Contact_Number", "Email_Id", "Father_Name",
			"Father_Occupation", "Mother_Name", "Mother_Occupation","Placed","Placed_Company" };
	String queryPreviousClass;

	public Insets getInsets()
	{
		return new Insets(20,20,20,20);
	}
	public DisplayDetailedData(String enrollment, String databaseName,
			String queryPreviousClass)
	{
		this.queryPreviousClass = queryPreviousClass;
		this.enrollment = enrollment;
		this.databaseName = databaseName;
		back = new MyJButton("BACK");
		edit = new MyJButton("EDIT");
		update = new MyJButton("UPDATE");

		tableModel = new DefaultTableModel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
			{
				if (column == 0)
					return false;
				else if(column==1&&row==1)
					return false;
				else
					return true;
			}
		};
		tableModel.addColumn("Fields");
		tableModel.addColumn("Values");
		table = new JTable(tableModel);

		values = getData();


		for (int i = 0; i < 45; i++)
		{
			Vector<String> rowData = new Vector<String>();
			rowData.add(fields[i]);
			rowData.add(values[i]);
			tableModel.addRow(rowData);
		}
		table.setFont(new Font(StaticMethodClass.defaultFont, Font.PLAIN, 18));
		StaticMethodClass.resizeColumnWidth(table);
		table.setRowHeight(table.getRowHeight() + 5);

		//--Center align data or table------------//
		DefaultTableCellRenderer stringRenderer = (DefaultTableCellRenderer)
		table.getDefaultRenderer(String.class);
		stringRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		//---------------------------------------//

		
		
		JScrollPane scrollpane = new JScrollPane(table);
		
		setBackground(StaticMethodClass.blueColor);
		setLayout(new BorderLayout());
		
		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,Font.PLAIN,StaticMethodClass.normalFontSize),back, edit, update);
		StaticMethodClass.setBorder(null,back, edit, update);
		
		StaticMethodClass.setBackground(Color.BLACK,back, edit, update);
		StaticMethodClass.setForeground(Color.WHITE,back, edit, update);

		
		add(scrollpane);
		JPanel buttons = new JPanel();

		buttons.setLayout(new GridBagLayout());
		Insets insets = new Insets(25, 5, 15, 5);
		
	
		
		buttons.add(back, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
		buttons.add(edit, new GridBagConstraints(1, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		buttons.add(update, new GridBagConstraints(2, 0, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, insets, 0, 0));
		buttons.setBackground(StaticMethodClass.blueColor);
		add(buttons, BorderLayout.SOUTH);

		addAll(back, edit, update);
		table.setEnabled(false);
	}

	void display(String[] data)
	{
		System.out.println("----------------")
		;
		for (int i = 0; i < data.length; i++)
			System.out.println("" + data[i]);
	}

	void addAll(JButton... b)
	{
		for (JButton button : b)
			button.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event)
	{
		JButton button = (JButton) event.getSource();
		if (button == back)
		{
			DisplayPreciseData.getJTableonFrame(databaseName,
					queryPreviousClass);
		} else if (button == edit)
		{
			table.setEnabled(true);
		} else if (button == update)
		{
			if(validateData(table))
				insertDataToSql();
		}
	}

	void insertDataToSql()
	{
		try
		{
			StringBuilder insertFrequentInfoQuery = new StringBuilder(
					"INSERT INTO `frequentinfo`(`Class_Roll_Number`, `Enrollment_Number`, "
							+ "`Name`, `Branch_Section`, `Contact_Number`, `Aggregate`, `Total_Backlogs`, "
							+ "`Total_Cleared_Backlogs`, `10th_Aggregate`, `12th_Aggregate`, "
							+ "`Diploma_Aggregate`, `Date_of_Birth`, `Email_Id`, `Father_Name`,`Placed`,`Placed_Company`)"
							+ "VALUES ("
							+ getIntegerData(table,0)
							+ " , "
							+ "'"
							+ getStringData(table,1)
							+ "' ,"
							+ " '"
							+ StaticMethodClass.capitalize(getStringData(table,2))
							+ "',"
							+ " '"
							+ getStringData(table,3).toUpperCase()
							+ "',"
							+ " '"
							+ getStringData(table,4)
							+ "',"
							+ getFloatData(table,18 - 1)
							+ ", "
							+ getIntegerData(table,19 - 1)
							+ ", "
							+ getIntegerData(table,20 - 1)
							+ ", "
							+ getFloatData(table,22 - 1)
							+ " , "
							+ getFloatData(table,25 - 1)
							+ " , "
							+ getFloatData(table,28 - 1)
							+ " ,"
							+ " '"
							+ getStringData(table,35 - 1) //dob
							+ "' , '"
							+ getStringData(table,39 - 1)
							+ "' , '"
							+ StaticMethodClass.capitalize(getStringData(table,40 - 1))  
							+ "' , '"
							+ getStringData(table,44 - 1).toUpperCase() 
							+ "' , '"
							+ StaticMethodClass.capitalize(getStringData(table,45 - 1)) 
							+ "')");

			StringBuilder insertPersonalInfoQuery = new StringBuilder(
					"INSERT INTO `personalinfo`(`Enrollment_Number`, `Gap_in_Study`, `value_added_Courses`, `Dream_Company`, `Gender`, `Permanent_Address`, `Correspondence_Address`, `Alternate_Contact_Number`, `Father_Occupation`, `Mother_Name`, `Mother_Occupation`)"
							+ "VALUES( '"
							+ getStringData(table,2 - 1)
							+ "', "
							+ getIntegerData(table,31 - 1)
							+ ", '"
							+ getStringData(table,32 - 1).toUpperCase()
							+ "', '"
							+ StaticMethodClass.capitalize(getStringData(table,33 - 1))
							+ "', '"
							+ getStringData(table,34 - 1).toUpperCase()
							+ "', '"
							+ getStringData(table,36 - 1)
							+ "', '"
							+ getStringData(table,37 - 1)
							+ "', '"
							+ getStringData(table,38 - 1)
							+ "', '"
							+ StaticMethodClass.capitalize(getStringData(table,41 - 1))
							+ "', '"
							+ StaticMethodClass.capitalize(getStringData(table,42 - 1))
							+ "', '"
							+ StaticMethodClass.capitalize(getStringData(table,43 - 1)) + "')");

			StringBuilder insertCollegeQuery = new StringBuilder(
					"INSERT INTO `college`(`Enrollment_Number`, `1st_Sem_Marks`, `Backlog_1st_Sem`, `2nd_Sem_Marks`, `Backlog_2nd_Sem`, `3rd_Sem_Marks`, `Backlog_3rd_Sem`, `4th_Sem_Marks`, `Backlog_4th_Sem`, `5th_Sem_Marks`, `Backlog_5th_Sem`, `6th_Sem_Marks`, `Backlog_6th_Sem`, `Pending_Backlogs`)"
							+ "VALUES( '"
							+ getStringData(table,2 - 1)
							+ "', "
							+ getIntegerData(table,6 - 1)
							+ ", '"
							+ getStringData(table,7 - 1).toUpperCase()
							+ "', "
							+ getIntegerData(table,8 - 1)
							+ ", '"
							+ getStringData(table,9 - 1).toUpperCase()
							+ "', "
							+ getIntegerData(table,10 - 1)
							+ ", '"
							+ getStringData(table,11 - 1).toUpperCase()
							+ "', "
							+ getIntegerData(table,12 - 1)
							+ ", '"
							+ getStringData(table,13 - 1).toUpperCase()
							+ "', "
							+ getIntegerData(table,14 - 1)
							+ ", '"
							+ getStringData(table,15 - 1).toUpperCase()
							+ "', "
							+ getIntegerData(table,16 - 1)
							+ ", '"
							+ getStringData(table,17 - 1).toUpperCase()
							+ "', "
							+ getIntegerData(table,21 - 1) + ")");

			StringBuilder insertSchoolQuery = new StringBuilder(
					"INSERT INTO `school`(`Enrollment_Number`, `10th_board`, `Year_Of_Passing_10th`, `12th_board`, `Year_Of_Passing_12th`, `Diploma_board`, `Year_Of_Passing_Diploma`)"
							+ "VALUES( '"
							+getStringData(table,2-1)
							+"', '"
							+ getStringData(table,23-1).toUpperCase()
							+ "', "
							+ getIntegerData(table,24-1)
							+ ", '"
							+ getStringData(table,26-1).toUpperCase()
							+ "', "
							+ getIntegerData(table,27-1)
							+ ", '"
							+ getStringData(table,29-1).toUpperCase()
							+ "', "
							+ getIntegerData(table,30-1)
							+ ")");

			String url = "jdbc:mysql://localhost:3306/", user = "root", passwrd = "";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, user, passwrd);
			Statement st = con.createStatement();

			st.executeQuery("use " + databaseName);
		
			String enrol = getStringData(table,2-1);
			st.executeUpdate("delete from college where enrollment_number = '"+enrol+"'");
			st.executeUpdate("delete from school where enrollment_number = '"+enrol+"'");
			st.executeUpdate("delete from personalinfo where enrollment_number = '"+enrol+"'");
			st.executeUpdate("delete from frequentinfo where enrollment_number = '"+enrol+"'");
			
			// st.addBatch(useDatabaseName);
			insertFrequentInfoQuery.append(";");
			insertCollegeQuery.append(";");
			insertPersonalInfoQuery.append(";");
			insertSchoolQuery.append(";");

			// st.executeUpdate()
			st.executeUpdate(insertFrequentInfoQuery.toString());
			st.executeUpdate(insertCollegeQuery.toString());
			st.executeUpdate(insertPersonalInfoQuery.toString());
			st.executeUpdate(insertSchoolQuery.toString());

			// st.executeBatch();

			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();

			System.out.println("everything doneee...check database nowwwwwwww");


			StaticMethodClass.addErrorLabel("UPDATION SUCCESSFUL", 5000);

			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
			st.close();
			con.close();

		} catch (SQLException | InstantiationException | IllegalAccessException
				| ClassNotFoundException e)
		{
			StaticMethodClass.addErrorLabel("Problem while UPDATING, Check values", 10000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
			e.printStackTrace();
		}
	}

	static String getStringData(JTable table,int val)
	{
		String s = (String) table.getModel().getValueAt(val, 1);
		if (val == 34)
		{

			if(s.charAt(2)=='-')
			{
				StringTokenizer st = new StringTokenizer(s,"-");
				StringBuffer sb = new StringBuffer();
				while(st.hasMoreTokens())
				{
					sb.append(st.nextToken());
					sb.append("/");
				}
				sb.deleteCharAt(sb.length()-1);
				s = sb.toString();
			}

		}

		return s;
	}

	static int getIntegerData(JTable table,int val)
	{
		int ans;
		String value = (String) table.getModel().getValueAt(val, 1);
		System.out.println("value at "+val+" is: "+value);
		try
		{
			if(value.equalsIgnoreCase("na")||value.equalsIgnoreCase("nil"))
				return 0;
			ans = Integer.parseInt(value);
			
		} catch (NumberFormatException e)
		{
			System.out.println("problem is here: "+val);
			return -1;
		}
		return ans;
	}

	static float getFloatData(JTable table,int val)
	{
		float ans;
		String value = (String) table.getModel().getValueAt(val, 1);
		try
		{
			if(value.equalsIgnoreCase("na")||value.equalsIgnoreCase("nill"))
				return 0;

			ans = Float.parseFloat(value);
		} catch (NumberFormatException e)
		{
			System.out.println("problem is here: in float "+val);
			return -1;
		}
		return ans;
	}

	static boolean validateData(JTable table)
	{
		try
		{
			String enroll = getStringData(table,1);
			String name = getStringData(table,2);
			String branchSec = getStringData(table,3);
			String contact = getStringData(table,4);
			String gender = getStringData(table,33);
			String alternateContact = getStringData(table,37);
			String mailId = getStringData(table,38);
			
			String placed = getStringData(table,44-1);
			int totalBacklogs, clearedBacklogs, pendingBacklogs, yopTen, yopTwe, yopDip;
			totalBacklogs = getIntegerData(table,18);
			clearedBacklogs = getIntegerData(table,19);
			pendingBacklogs = getIntegerData(table,20);
			yopTen = getIntegerData(table,23);
			yopTwe = getIntegerData(table,26);
			yopDip = getIntegerData(table,29);
			
			if ((getFloatData(table,28-1) == -1) || (getFloatData(table,25-1) == -1)
			|| (getFloatData(table,22-1) == -1) || (getIntegerData(table,20-1) == -1)
			|| (getIntegerData(table,19-1) == -1) || (getFloatData(table,18-1) == -1)
			|| (getIntegerData(table,1-1) == -1) || (getIntegerData(table,31-1) == -1)
			|| (getIntegerData(table,30-1) == -1) || (getIntegerData(table,27-1) == -1)
			|| (getIntegerData(table,24-1) == -1) || (getIntegerData(table,21-1) == -1)
			|| (getIntegerData(table,16-1) == -1) || (getIntegerData(table,14-1) == -1)
			|| (getIntegerData(table,12-1) == -1) || (getIntegerData(table,10-1) == -1)
			|| (getIntegerData(table,8-1) == -1) || (getIntegerData(table,6-1) == -1)
			|| (totalBacklogs == -1) || (clearedBacklogs == -1)
			|| (pendingBacklogs == -1) || (yopDip == -1) || (yopTen == -1)
			|| (yopTwe == -1))
	{
				System.out.println("=======ERROR -1=========");
				ConvertingExcelToSql.displayError(0, false);
				return false;
			} else
			{
				if ((enroll.length() >= 8)&&(enroll.length() <= 12)
						&& (!name.equals(""))
						&& (contact.length() >= 10)
						&& (contact.length() <= 12)
						&& (clearedBacklogs <= totalBacklogs)
						&& (pendingBacklogs <= totalBacklogs)
						&& (gender.equalsIgnoreCase("male") || gender
								.equalsIgnoreCase("female"))
						&& (alternateContact.length() >= 10)
						&& (alternateContact.length() <= 12)
						&&(placed.equalsIgnoreCase("no")||placed.equalsIgnoreCase("yes"))
						)
				{
					System.out.println("inside");
					if (yopTwe == 0)
					{
						System.out.println("inside");
						if (yopDip > yopTen)
						{
							System.out.println("inside");
							if ((branchSec.equalsIgnoreCase("cse-1"))
									|| branchSec.equalsIgnoreCase("cse-2")
									|| branchSec.equalsIgnoreCase("cse-3")
									|| branchSec.equalsIgnoreCase("it-1")
									|| branchSec.equalsIgnoreCase("it-2")
									|| branchSec.equalsIgnoreCase("it-3")
									|| branchSec.equalsIgnoreCase("ece-1")
									|| branchSec.equalsIgnoreCase("ece-2")
									|| branchSec.equalsIgnoreCase("ece-3")
									|| branchSec.equalsIgnoreCase("eee"))
							{
								
								System.out.println("inside");
								StringTokenizer st = new StringTokenizer(mailId,
										"@");
								int x = st.countTokens();
								if (x == 2)
								{
									st.nextToken();
									String gmail = st.nextToken();
									if (gmail.equals("gmail.com"))
										return true;
									else
										System.out.println("prblme in mailid");
	
								}
							} else
							{
								System.out.println("prblme in Bracnh sec");
								return false;
							}
	
						} else
						{
							System.out.println("prblme in yopdip>yopten");
							return false;
						}
					} else if (yopDip == 0)
					{
						if (yopTwe > yopTen)
						{
							if ((branchSec.equalsIgnoreCase("cse-1"))
									|| branchSec.equalsIgnoreCase("cse-2")
									|| branchSec.equalsIgnoreCase("cse-3")
									|| branchSec.equalsIgnoreCase("it-1")
									|| branchSec.equalsIgnoreCase("it-2")
									|| branchSec.equalsIgnoreCase("it-3")
									|| branchSec.equalsIgnoreCase("ece-1")
									|| branchSec.equalsIgnoreCase("ece-2")
									|| branchSec.equalsIgnoreCase("ece-3")
									|| branchSec.equalsIgnoreCase("eee"))
							{
								System.out.println("Checking mail now");
								StringTokenizer st = new StringTokenizer(mailId,
										"@");
								int x = st.countTokens();
								if (x == 2)
								{
									st.nextToken();
									String gmail = st.nextToken();
									if (gmail.equals("gmail.com"))
										return true;
									else
									{
										System.out.println("prblme in mailid");
									}
								} else
								{
									System.out.println("prblme in mailid");
									return false;
								}
							}
						} else
						{
							System.out.println("prblme in bracnh section");
							return false;
						}
	
					} else
					{
						System.out.println("neith yopDip=0 nor yoptwe=0");
						return false;
					}
	
				}
			}
	
			System.out.println("some other problem ");
		}
		catch(NullPointerException e)
		{
			StaticMethodClass.addErrorLabel("Enter all the fields", 2000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
		}
		catch(Exception e)
		{
			StaticMethodClass.addErrorLabel("Check Help tab and then enter fields correctly", 2000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
		}
		return false;
	}

	String validateEnrollment(String value)
	{
		if (value.length() >= 9)
			return value;
		else
			return "";
	}

	int validateInteger(String value)
	{
		int x;
		try
		{
			x = Integer.parseInt(value);
		} catch (NumberFormatException e)
		{
			return -1;
		}
		if (x != 0)
			return x;
		else
			return -1;
	}

	String[] getData()
	{
		String result[] = new String[45];
		try
		{
			String query = "Select `Class_Roll_Number`, frequentinfo.`Enrollment_Number`, `Name`, `Branch_Section`, `Contact_Number`, `1st_Sem_Marks`, `Backlog_1st_Sem`, `2nd_Sem_Marks`, `Backlog_2nd_Sem`, `3rd_Sem_Marks`, `Backlog_3rd_Sem`, `4th_Sem_Marks`, `Backlog_4th_Sem`, `5th_Sem_Marks`, `Backlog_5th_Sem`, `6th_Sem_Marks`, `Backlog_6th_Sem`,`Aggregate`, `Total_Backlogs`, `Total_Cleared_Backlogs`, `Pending_Backlogs`,`10th_Aggregate`, `10th_board`, `Year_Of_Passing_10th`,`12th_Aggregate`, `12th_board`, `Year_Of_Passing_12th`,`Diploma_Aggregate`, `Diploma_board`, `Year_Of_Passing_Diploma`, `Gap_in_Study`, `value_added_Courses`, `Dream_Company`, `Gender`, `Date_of_Birth`,`Permanent_Address`, `Correspondence_Address`, `Alternate_Contact_Number`, `Email_Id`, `Father_Name`,`Father_Occupation`, `Mother_Name`, `Mother_Occupation`, frequentinfo.`Placed`,frequentinfo.`Placed_company` from frequentinfo, college,personalinfo,school where frequentinfo.enrollment_number=college.enrollment_number and college.enrollment_number=personalinfo.enrollment_number and college.enrollment_number=school.enrollment_number and frequentinfo.enrollment_number='"
					+ enrollment + "'";

			String url = "jdbc:mysql://localhost:3306/", user = "root", passwrd = "";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, user, passwrd);
			Statement st = con.createStatement();
			st.executeQuery("use " + databaseName);
			System.out.println("detailed: " + query);
			ResultSet resultSet = st.executeQuery(query);
			while (resultSet.next())
			{
				for (int i = 0; i < 45; i++)
					result[i] = resultSet.getString(i + 1);
			}
			st.close();
			con.close();
		} catch (SQLException | InstantiationException | IllegalAccessException
				| ClassNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("problem at 598 DisplayDetailed");
		}
		return result;
	}

	static void getDetailedJTableonFrame(String enrollment,
			String databaseName, String query)
	{
		MainFrame.mainContent.remove(1); 
		MainFrame.mainContent.add(new DisplayDetailedData(enrollment,
				databaseName, query), new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(50, 100, 20, 100), 0, 0));
		MainFrame.globalInstance.repaint();
		MainFrame.globalInstance.revalidate();
	}

}
