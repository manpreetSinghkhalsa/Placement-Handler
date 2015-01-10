package com.minorproject.gtbit;

import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import resources.StaticMethodClass;

class ConvertingExcelToSql
{

	String databaseName = "";

	/*
	 * Insert Queries for Frequentinfo, PersonalInfo, College, School Tables--
	 * useDatabaseName: Query for using database
	 */

	String useDatabaseName = "";
	StringBuilder insertFrequentInfoQuery = new StringBuilder(
			"INSERT INTO `frequentinfo`(`Class_Roll_Number`, `Enrollment_Number`, `Name`, `Branch_Section`, `Contact_Number`, `Aggregate`, `Total_Backlogs`, `Total_Cleared_Backlogs`, `10th_Aggregate`, `12th_Aggregate`, `Diploma_Aggregate`, `Date_of_Birth`, `Email_Id`, `Father_Name`)VALUES");
	StringBuilder insertPersonalInfoQuery = new StringBuilder(
			"INSERT INTO `personalinfo`(`Enrollment_Number`, `Gap_in_Study`, `value_added_Courses`, `Dream_Company`, `Gender`, `Permanent_Address`, `Correspondence_Address`, `Alternate_Contact_Number`, `Father_Occupation`, `Mother_Name`, `Mother_Occupation`)VALUES");
	StringBuilder insertCollegeQuery = new StringBuilder(
			"INSERT INTO `college`(`Enrollment_Number`, `1st_Sem_Marks`, `Backlog_1st_Sem`, `2nd_Sem_Marks`, `Backlog_2nd_Sem`, `3rd_Sem_Marks`, `Backlog_3rd_Sem`, `4th_Sem_Marks`, `Backlog_4th_Sem`, `5th_Sem_Marks`, `Backlog_5th_Sem`, `6th_Sem_Marks`, `Backlog_6th_Sem`, `Pending_Backlogs`)VALUES");
	StringBuilder insertSchoolQuery = new StringBuilder(
			"INSERT INTO `school`(`Enrollment_Number`, `10th_board`, `Year_Of_Passing_10th`, `12th_board`, `Year_Of_Passing_12th`, `Diploma_board`, `Year_Of_Passing_Diploma`)VALUES");

	ConvertingExcelToSql(final String location, final String databaseName, final String sheetName)
	{
		try
		{
			this.databaseName = databaseName;

			// ----------------USER FEEDBACK-----------
			StaticMethodClass.addErrorLabel("Creating tables", 1000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();

			// ---------Creating use database query---------------
			useDatabaseName = "USE " + databaseName + ";";

			//----------------------------------------------------//
			StaticMethodClass.addErrorLabel("Reading Excel..", 1000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
			
			// -----Reading EXCEL FILE NOW---------------
			new Thread(new Runnable()
			{
				
				@SuppressWarnings("deprecation")
				@Override
				public void run()
				{
//					File getFileFromLocation = new File(location);
					XSSFWorkbook workbook = null;
					XSSFSheet sheet=null;
					try
					{
//						FileInputStream file = new FileInputStream(getFileFromLocation);
						workbook = new XSSFWorkbook(location);
						sheet = workbook.getSheet(sheetName);
						int lastRow = sheet.getLastRowNum() + 1;
						int columnNum = sheet.getRow(0).getLastCellNum();
						System.out.println("column number: " + columnNum);
						boolean first = true;
						String bracket_coma = "(";

						for (int i = 1; i < lastRow; i++)
						{
							XSSFRow row = sheet.getRow(i);
							int columnCount = sheet.getRow(i).getLastCellNum();

							if (columnCount == 43)
							{

								// Checking the Diploma and 12th NA scene....
								String checkNA[] = { getData(row, 25 - 1),
										getData(row, 27 - 1), getData(row, 28 - 1),
										getData(row, 30 - 1) };
								float dipAnd12thAggregate[] = new float[2];
								int yopDipAnd12th[] = new int[2];
								for (int counter = 0; counter < 4; counter++)
								{
									if (checkNA[counter].equalsIgnoreCase("NA")
											|| checkNA[counter].equalsIgnoreCase("Nil"))
									{
										switch (counter)
										{
										case 0:
											dipAnd12thAggregate[0] = 0;
											break;
										case 1:
											yopDipAnd12th[0] = 0;
											break;
										case 2:
											dipAnd12thAggregate[1] = 0;
											break;
										case 3:
											yopDipAnd12th[1] = 0;
											break;
										}
									} else
									{
										switch (counter)
										{
										case 0:
											dipAnd12thAggregate[0] = Float
													.parseFloat(checkNA[0]);
											break;
										case 1:
											yopDipAnd12th[0] = Integer.parseInt(checkNA[1]);
											break;
										case 2:
											dipAnd12thAggregate[1] = Float
													.parseFloat(checkNA[2]);
											break;
										case 3:
											yopDipAnd12th[1] = Integer.parseInt(checkNA[3]);
											break;
										}
									}
								}
								// Scene ends here :p

								if (!validateData(row))
								{
									System.out.println("Validation is FALSE");
									try
									{
										String url = "jdbc:mysql://localhost:3306/", user = "root", passwrd = "";
										Class.forName("com.mysql.jdbc.Driver")
												.newInstance();
										Connection con = DriverManager.getConnection(url,
												user, passwrd);
										Statement st = con.createStatement();

										st.executeUpdate("drop database " + databaseName);
										st.close();
										con.close();
										System.out.println("everythng done");
									} catch (Exception e)
									{
										System.out.println("exception");
									}
									displayError(i, true);
									return;
								} else
								{
									System.out.println("Validation is TRUE");
									String college = bracket_coma
											+ getStringData(row, 2 - 1) + ","
											+ getIntegerData(row, 6 - 1) + ","
											+ getStringData(row, 7 - 1).toUpperCase() + ","
											+ getIntegerData(row, 8 - 1) + ","
											+ getStringData(row, 9 - 1).toUpperCase() + ","
											+ getIntegerData(row, 10 - 1) + ","
											+ getStringData(row, 11 - 1).toUpperCase() + ","
											+ getIntegerData(row, 12 - 1) + ","
											+ getStringData(row, 13 - 1).toUpperCase() + ","
											+ getIntegerData(row, 14 - 1) + ","
											+ getStringData(row, 15 - 1).toUpperCase() + ","
											+ getIntegerData(row, 16 - 1) + ","
											+ getStringData(row, 17 - 1).toUpperCase() + ","
											+ getIntegerData(row, 21 - 1) + ") ";

									String frequent = bracket_coma
											+ getIntegerData(row, 1 - 1) + ","
											+ getStringData(row, 2 - 1) + ","
											+ StaticMethodClass.capitalize(getStringData(row, 3 - 1)) + ","
											+ getStringData(row, 4 - 1).toUpperCase() + ","
											+ getStringData(row, 5 - 1) + ","
											+ getFloatData(row, 18 - 1) + ","
											+ getIntegerData(row, 19 - 1) + ","
											+ getIntegerData(row, 20 - 1) + ","
											+ getFloatData(row, 22 - 1) + ","
											+ dipAnd12thAggregate[0]
											+ ","
											+ dipAnd12thAggregate[1]
											+ "," // getFloatData(row, 28-1)
											+ getStringData(row, 35 - 1).toUpperCase()// formatDOB(getData(row,
																		// 35-1),i)
											+ ","// getStringData(row, 35-1)
											+ getStringData(row, 39 - 1) + ","
											+ StaticMethodClass.capitalize(getStringData(row, 40 - 1)) + ") ";

									String personal = bracket_coma
											+ getStringData(row, 2 - 1) + ","
											+ getIntegerData(row, 31 - 1) + ","
											+ StaticMethodClass.capitalize(getStringData(row, 32 - 1)) + ","
											+ StaticMethodClass.capitalize(getStringData(row, 33 - 1)) + ","
											+ getStringData(row, 34 - 1).toLowerCase() + ","
											+ StaticMethodClass.capitalize(getStringData(row, 36 - 1)) + ","
											+ StaticMethodClass.capitalize(getStringData(row, 37 - 1)) + ","
											+ getStringData(row, 38 - 1) + ","
											+ StaticMethodClass.capitalize(getStringData(row, 41 - 1)) + ","
											+ StaticMethodClass.capitalize(getStringData(row, 42 - 1)) + ","
											+ StaticMethodClass.capitalize(getStringData(row, 43 - 1)) + ") ";

									String school = bracket_coma
											+ getStringData(row, 2 - 1) + ","
											+ getStringData(row, 23 - 1).toUpperCase() + ","
											+ getIntegerData(row, 24 - 1) + ","
											+ getStringData(row, 26 - 1).toUpperCase() + ","
											+ yopDipAnd12th[0]
											+ "," // getIntegerData(row, 27-1)
											+ getStringData(row, 29 - 1).toUpperCase() + ","
											+ yopDipAnd12th[1] + ") "; // getIntegerData(row,
																		// 30-1) YEAR OF
																		// PASSING
																		// Diploma

									insertCollegeQuery.append(college);
									insertFrequentInfoQuery.append(frequent);
									insertPersonalInfoQuery.append(personal);
									insertSchoolQuery.append(school);

									if (first)
									{
										first = !first;
										bracket_coma = ",(";
									}
								}
							}

						}
						insertDataToSql();

					} catch (Exception e1)
					{
						StaticMethodClass.addErrorLabel("Sheet name is not correct", 2000);
						MainFrame.globalInstance.repaint();
						MainFrame.globalInstance.revalidate();
						e1.printStackTrace();
					}
				}

			}).start();
		} catch (Exception e)
		{
			StaticMethodClass.addErrorLabel("Enter Sheet Name Correctly", 3000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
			
			e.printStackTrace();
			System.out.println("EXCEPTION ");
		}
	}

	static void displayError(int i, boolean b)
	{
		String addLabel = "One of the following rules have been voilated in row:"
				+ (i + 1);
		String listOfProblems = " \n1. Enrollment number CANNOT be lesser than 9\n2. Name CANNOT be NULL\n3. Branch and Sec: must be one of these CSE-1,CSE-2,CSE-3,IT-1,IT-2,IT-3,ECE-1,ECE-2,ECE-3,EEE\n4. Contact Number: Maximum Number of digits must be between 10 and 12 (both inclusive)\n5. Total Backlogs must be greater than or equals to Cleared and pending backlogs\n6. Year of Passing of 10th must be lesser than that of Diploma/12th\n7. Gender must be either male or female\n8. Email id: must be a GMAIL id\n\nRectify the error and try again.";
		String res = "";
		if (b)
			res = addLabel + listOfProblems;
		else
			res = listOfProblems;
		JOptionPane.showMessageDialog(null, res, "ERROR in the EXCEL File",
				JOptionPane.ERROR_MESSAGE, null);
	}

	boolean validateData(XSSFRow row)
	{
		String enroll = getData(row, 2 - 1);
		String name = getData(row, 3 - 1);
		String branchSec = getData(row, 4 - 1);
		String contact = getData(row, 5 - 1);
		int totalBacklogs = getIntegerData(row, 19 - 1);
		int clearedBacklogs = getIntegerData(row, 20 - 1);
		int pendingBacklogs = getIntegerData(row, 21 - 1);
		int yopTen = getIntegerData(row, 24 - 1);
		int yopTwe = getIntegerData(row, 27 - 1);
		int yopDip = getIntegerData(row, 30 - 1);
		String gender = getData(row, 34 - 1);
		String alternateContact = getData(row, 38 - 1);
		String mailId = getData(row, 39 - 1);
		// System.out.println("--------------------------------");
		// System.out.println("Enrollment: "+enroll+"  name: "+name+"  contact: "+contact+" lengt of contac: "+contact.length()+"\nclearedBacklogs: "+clearedBacklogs+" pending: "+pendingBacklogs+"  cleared:"+clearedBacklogs+" \nGender: "+gender+"   Alternate: "+alternateContact);

		if ((enroll.length() >= 9)
				&& (!name.equals(""))
				&& (contact.length() >= 10)
				&& (contact.length() <= 12)
				&& (clearedBacklogs <= totalBacklogs)
				&& (pendingBacklogs <= totalBacklogs)
				&& (gender.equalsIgnoreCase("male") || gender
						.equalsIgnoreCase("female"))
				&& (alternateContact.length() >= 10)
				&& (alternateContact.length() <= 12))
		{

			if (yopTwe == 0)
			{

				if (yopDip > yopTen)
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

						StringTokenizer st = new StringTokenizer(mailId, "@");
						int x = st.countTokens();
						if (x == 2)
						{
							st.nextToken();
							String gmail = st.nextToken();
							if (gmail.equals("gmail.com"))
								return true;
							else
								System.out.println("problem is in mail-id");

						}
					} else
					{
						System.out.println("problem in Branch sec");
						return false;
					}

				} else
				{
					System.out.println("probelm in Year of Passing Diploma > Year of passing tenth");
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
						StringTokenizer st = new StringTokenizer(mailId, "@");
						int x = st.countTokens();
						if (x == 2)
						{
							st.nextToken();
							String gmail = st.nextToken();
							if (gmail.equals("gmail.com"))
								return true;
							else
							{
								System.out.println("problem in mailid");
							}
						} else
						{
							System.out.println("problem in mail id");
							return false;
						}
					}
				} else
				{
					System.out.println("prblme in branch section");
					return false;
				}

			} else
			{
				System.out.println("neither yopDip=0 nor yoptwe=0");
				return false;
			}

		}
		return false;
	}

	String getData(XSSFRow row, int j)
	{
		XSSFCell cell = row.getCell(j);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		Object res = cell.getStringCellValue();
		return "" + res;
	}

	int getIntegerData(XSSFRow row, int j)
	{

		XSSFCell cell = row.getCell(j);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		Object res = cell.getStringCellValue();
		int data = 0;
		try
		{
			data = Integer.parseInt("" + res);
		} catch (NumberFormatException e)
		{
			return 0;
		}
		return data;
	}

	float getFloatData(XSSFRow row, int j)
	{
		XSSFCell cell = row.getCell(j);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		Object res = cell.getStringCellValue();
		return Float.parseFloat("" + res);
	}

	String getStringData(XSSFRow row, int j)
	{
		XSSFCell cell = row.getCell(j);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		Object res = cell.getStringCellValue();
		String s = "" + res ;
		if (j == 34)
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
			System.out.println("value: " + s);
			if (s.length() < 8)
				return "'1/1/2001'"; // default Date
		}
		return "'"+s+"'";
	}

	void insertDataToSql()
	{
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				try
				{
					String url = "jdbc:mysql://localhost:3306/", user = "root", passwrd = "";
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection con = DriverManager.getConnection(url, user, passwrd);
					Statement st = con.createStatement();

					st.executeQuery(useDatabaseName);

					insertFrequentInfoQuery.append(";");
					insertCollegeQuery.append(";");
					insertPersonalInfoQuery.append(";");
					insertSchoolQuery.append(";");


					st.executeUpdate(insertFrequentInfoQuery.toString());
					st.executeUpdate(insertCollegeQuery.toString());
					st.executeUpdate(insertPersonalInfoQuery.toString());
					st.executeUpdate(insertSchoolQuery.toString());

					//Adding the new database name to the main database
					addDatabaseNameToDatabase();
					
					//user feedback

					StaticMethodClass.addErrorLabel(" CONVERSION SUCCESSFUL" , 2000);

					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
					
					new Thread(new Runnable()
					{
						
						@Override
						public void run()
						{
							try
							{
								Thread.sleep(1000);
							} catch (InterruptedException e)
							{
								e.printStackTrace();
							}
							SwingUtilities.invokeLater(new Runnable()
							{
								
								@Override
								public void run()
								{
									Component[] components = MainFrame.mainContent
											.getComponents();
									if (components.length > 1)
										MainFrame.mainContent.remove(1); 
									
								}
							});
						}
					}).start();
					st.close();
					con.close();

				} catch (SQLException | InstantiationException | IllegalAccessException
						| ClassNotFoundException e)
				{
					StaticMethodClass.addErrorLabel("Malformed EXCEL FILE", 10000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
					e.printStackTrace();
				}

				
			}
		}).start();
	}

	void addDatabaseNameToDatabase()
	{
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				try
				{
					String url = "jdbc:mysql://localhost:3306/", user = "root", passwrd = "";
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection con = DriverManager.getConnection(url, user, passwrd);
					Statement st = con.createStatement();

					st.executeQuery("use main;");

					st.executeUpdate("insert into databasenames values ( '"
							+ databaseName + "');");
					st.close();
					con.close();
				} catch (SQLException | InstantiationException | IllegalAccessException
						| ClassNotFoundException e)
				{
					StaticMethodClass.addErrorLabel("Problem occured while adding Database Name to database..", 1000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
					System.out.println("problem in the addDatabaseNameToDatabase");
				}
			}
		}).start();
	}
}
