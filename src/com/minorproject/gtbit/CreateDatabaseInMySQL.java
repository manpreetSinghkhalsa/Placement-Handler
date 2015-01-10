package com.minorproject.gtbit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import resources.StaticMethodClass;

class CreateDatabaseInMySQL
{
	String createDatabaseQuery = "", selectDatabaseQuery = "",
			createTableFrequentInfoQuery = "", createTableCollegeQuery = "",
			createTableSchoolQuery = "", createTablePersonalInfoQuery = "";
	String databaseName = "", location = "";
	Statement st;
	Connection con;
	public CreateDatabaseInMySQL(final String location, final String databaseName, final String sheetName)
	{
		this.databaseName = databaseName;
		this.location = location;
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				try
				{
					String url = "jdbc:mysql://localhost:3306/", user = "root", passwrd = "";
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					con = DriverManager.getConnection(url, user, passwrd);
					st = con.createStatement();
					
					StaticMethodClass.addErrorLabel("Creating Database..", 1000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
					
					generateQuery();
					// Dont change the order
					st.executeUpdate(createDatabaseQuery);
					
					st.addBatch(selectDatabaseQuery);
					
					st.addBatch(createTableFrequentInfoQuery);
					st.addBatch(createTableCollegeQuery);
					st.addBatch(createTablePersonalInfoQuery);
					st.addBatch(createTableSchoolQuery);

					st.executeBatch();

					con.close();
					st.close();
					
					StaticMethodClass.addErrorLabel("Database Created..", 1000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
					
					System.out.println("Database created no gng for tables");
					//reading the excel now and converting it to queries

					new ConvertingExcelToSql(location, databaseName, sheetName);
					
				} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e)
				{
					StaticMethodClass.addErrorLabel("Select Different Batch name",10000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
					System.out.println("exception occured");
					e.printStackTrace();
				}
			}
		}).start();
	}

	void generateQuery()
	{
		//Do waaari soch li kuj v kran to pehla :\ :p
		createDatabaseQuery = "create database " + databaseName
				+ ";";
		selectDatabaseQuery = "use " + databaseName + ";";
		createTableFrequentInfoQuery = "create table FrequentInfo " + "( "
				+ "Class_Roll_Number tinyint not null, "
				+ "Enrollment_Number varchar(12)  not null primary key, "
				+ "Name varchar(50)  not null, "
				+ "Branch_Section varchar(5)  not null, "
				+ "Contact_Number varchar(12)  not null, "
				+ "Aggregate float  not null, "
				+ "Total_Backlogs tinyint  not null, "
				+ "Total_Cleared_Backlogs Tinyint  not null, "
				+ "10th_Aggregate float  not null, "
				+ "12th_Aggregate float  , " + "Diploma_Aggregate float, "
				+ "Date_of_Birth varchar(12)  not null, "
				+ "Email_Id varchar(50)  not null, "
				+ "Father_Name varchar(50)  not null, " 
				+ "Placed varchar(3) DEFAULT 'NO'," 
				+ "Placed_Company varchar(50) DEFAULT 'NIL'"
				+") ";
		createTableCollegeQuery = "create table college "
				+ "( "
				+ "Enrollment_Number varchar(12) Not Null primary key, "
				+ "1st_Sem_Marks smallint not null, "
				+ "Backlog_1st_Sem varchar(80) not null, "
				+ "2nd_Sem_Marks smallint not null, "
				+ "Backlog_2nd_Sem varchar(80) not null, "
				+ "3rd_Sem_Marks smallint not null, "
				+ "Backlog_3rd_Sem varchar(80) not null, "
				+ "4th_Sem_Marks smallint not null, "
				+ "Backlog_4th_Sem varchar(80) not null, "
				+ "5th_Sem_Marks smallint not null, "
				+ "Backlog_5th_Sem varchar(80) not null, "
				+ "6th_Sem_Marks smallint not null, "
				+ "Backlog_6th_Sem varchar(80) not null, "
				+ "Pending_Backlogs tinyint not null, "
				+ "foreign key(Enrollment_Number) REFERENCES FrequentInfo(Enrollment_Number) "
				+ ") ";
		createTableSchoolQuery = "create table school "
				+ "( "
				+ "Enrollment_Number varchar(12) not null, "
				+ "10th_board varchar(10) not null, "
				+ "Year_Of_Passing_10th smallint not null, "
				+ "12th_board varchar(10) not null, "
				+ "Year_Of_Passing_12th smallint not null, "
				+ "Diploma_board varchar(10) not null, "
				+ "Year_Of_Passing_Diploma smallint not null, "
				+ "foreign key(Enrollment_Number) REFERENCES FrequentInfo(Enrollment_Number) "
				+ ") ";
		createTablePersonalInfoQuery = "create table personalInfo "
				+ "( "
				+ "Enrollment_Number varchar(12) not null, "
				+ "Gap_in_Study tinyint not null, "
				+ "value_added_Courses varchar(100) not null, "
				+ "Dream_Company varchar(50) not null, "
				+ "Gender varchar(6) not null, "
				+ "Permanent_Address varchar(500) not null, "
				+ "Correspondence_Address varchar(500) not null, "
				+ "Alternate_Contact_Number varchar(15) not null, "
				+ "Father_Occupation varchar(50) not null, "
				+ "Mother_Name varchar(50) not null, "
				+ "Mother_Occupation varchar(50) not null, "
				+ "Foreign key(Enrollment_Number) REFERENCES FrequentInfo(Enrollment_Number) "
				+ ")";
	}
}