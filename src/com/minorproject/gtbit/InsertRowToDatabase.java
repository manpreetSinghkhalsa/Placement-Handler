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
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

class InsertRowToDatabase extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JComboBox listDatabases;
	DefaultTableModel tableModel;
	JTable table;
	MyJButton addRow;
	Insets insets = new Insets(10, 10, 10, 10);  
	public Insets getInsets()
	{
		return new Insets(20,20,20,20);
	}
	 public InsertRowToDatabase()
	{
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
					else
						return true;
				}
			};
			tableModel.addColumn("Fields");
			tableModel.addColumn("Values");
			table = new JTable(tableModel);

			String[] values = new String[45];

			for (int i = 0; i < 45; i++)
			{
				Vector<String> rowData = new Vector<String>();
				rowData.add(DisplayDetailedData.fields[i]);
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
			addRow = new MyJButton("Add Row");
			
			listDatabases = new JComboBox(SimpleAdvancedSearchClass.getDatabaseNames());
			
			setBackground(StaticMethodClass.blueColor);
			
			StaticMethodClass.setBackground(Color.BLACK, listDatabases,addRow);

			StaticMethodClass.setForeground(Color.WHITE, listDatabases,addRow);
			setLayout(new GridBagLayout());
			
			add(listDatabases,new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
			
			add(scrollpane, new GridBagConstraints(0, 0, 0, 1, 1, 1,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

			add(addRow, new GridBagConstraints(1, 1, 1, 1, 1, 1,GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
			addRow.addActionListener(new ActionListener()
			{
				
				public void actionPerformed(ActionEvent e)
				{
					JButton button  =  (JButton) e.getSource();
					if((button==addRow)&&(listDatabases.getSelectedIndex()!=0))
					{
						
						if(DisplayDetailedData.validateData(table))
						{
							try
							{
								insertData();
								StaticMethodClass.addErrorLabel("Row Added", 2000);
								MainFrame.globalInstance.repaint();
								MainFrame.globalInstance.revalidate();								
								
							}
							catch(Exception eff)
							{
								StaticMethodClass.addErrorLabel("Problem occured, Data insertion stopped", 2000);
								MainFrame.globalInstance.repaint();
								MainFrame.globalInstance.revalidate();								
							}
						}
						else
						{
							StaticMethodClass.addErrorLabel("Data Validation Failed", 2000);
							MainFrame.globalInstance.repaint();
							MainFrame.globalInstance.revalidate();
						}
					}
					else if(listDatabases.getSelectedIndex()==0)
					{
						StaticMethodClass.addErrorLabel("SELECT DATABASE FIRST", 5000);
						MainFrame.globalInstance.repaint();
						MainFrame.globalInstance.revalidate();
					}
				}
			});
	}
	 
	 void insertData() throws Exception
	 {
			StringBuilder insertFrequentInfoQuery = new StringBuilder(
					"INSERT INTO `frequentinfo`(`Class_Roll_Number`, `Enrollment_Number`, `Name`, `Branch_Section`, `Contact_Number`, `Aggregate`, `Total_Backlogs`, `Total_Cleared_Backlogs`, `10th_Aggregate`, `12th_Aggregate`, `Diploma_Aggregate`, `Date_of_Birth`, `Email_Id`, `Father_Name`, `Placed`,`Placed_Company`)VALUES(");
			StringBuilder insertPersonalInfoQuery = new StringBuilder(
					"INSERT INTO `personalinfo`(`Enrollment_Number`, `Gap_in_Study`, `value_added_Courses`, `Dream_Company`, `Gender`, `Permanent_Address`, `Correspondence_Address`, `Alternate_Contact_Number`, `Father_Occupation`, `Mother_Name`, `Mother_Occupation`)VALUES(");
			StringBuilder insertCollegeQuery = new StringBuilder(
					"INSERT INTO `college`(`Enrollment_Number`, `1st_Sem_Marks`, `Backlog_1st_Sem`, `2nd_Sem_Marks`, `Backlog_2nd_Sem`, `3rd_Sem_Marks`, `Backlog_3rd_Sem`, `4th_Sem_Marks`, `Backlog_4th_Sem`, `5th_Sem_Marks`, `Backlog_5th_Sem`, `6th_Sem_Marks`, `Backlog_6th_Sem`, `Pending_Backlogs`)VALUES(");
			StringBuilder insertSchoolQuery = new StringBuilder(
					"INSERT INTO `school`(`Enrollment_Number`, `10th_board`, `Year_Of_Passing_10th`, `12th_board`, `Year_Of_Passing_12th`, `Diploma_board`, `Year_Of_Passing_Diploma`)VALUES(");
			
			
			String data[] = new String[45];
			
			for(int i=0;i<45;i++)
			{
				data[i] = (String) table.getModel().getValueAt(i, 1);
			}
			
			String enroll = data[1];
			while(enroll.charAt(0)=='0')
			{
				enroll = enroll.substring(1);
			}
			if(checkEnrollmentNumber(enroll))
			{
	
			insertFrequentInfoQuery.append(getIntegerData(data[0])+",'"+enroll+"','"+StaticMethodClass.capitalize(data[2])+"','"+data[3].toUpperCase()+"','"+data[4]+"',"+getFloatData(data[17])+","+getIntegerData(data[18])+","+getIntegerData(data[19])+","+getFloatData(data[21])+","+getFloatData(data[24])+","+getFloatData(data[27])+",'"+getDate(data[34])+"','"+data[38]+"','"+StaticMethodClass.capitalize(data[39])+"','"+data[43].toUpperCase()+"','"+StaticMethodClass.capitalize(data[44])+"')");
			insertPersonalInfoQuery.append("'"+enroll+"',"+getIntegerData(data[30])+",'"+StaticMethodClass.capitalize(data[31])+"','"+StaticMethodClass.capitalize(data[32])+"','"+data[33].toUpperCase()+"','"+StaticMethodClass.capitalize(data[35])+"','"+StaticMethodClass.capitalize(data[36])+"','"+data[37]+"','"+StaticMethodClass.capitalize(data[40])+"','"+StaticMethodClass.capitalize(data[41])+"','"+StaticMethodClass.capitalize(data[42])+"');");
			insertCollegeQuery.append("'"+enroll+"',"+getIntegerData(data[5])+",'"+data[6].toUpperCase()+"',"+getIntegerData(data[7])+",'"+data[8].toUpperCase()+"',"+getIntegerData(data[9])+",'"+data[10].toUpperCase()+"',"+getIntegerData(data[11])+",'"+data[12].toUpperCase()+"',"+getIntegerData(data[13])+",'"+data[14].toUpperCase()+"',"+getIntegerData(data[15])+",'"+data[16].toUpperCase()+"',"+getIntegerData(data[20])+");");
			insertSchoolQuery.append("'"+enroll+"','"+data[22].toUpperCase()+"',"+getIntegerData(data[23])+",'"+data[25].toUpperCase()+"',"+getIntegerData(data[26])+",'"+data[28].toUpperCase()+"',"+getIntegerData(data[29])+");");
			String queries[]= {insertFrequentInfoQuery.toString(),insertPersonalInfoQuery.toString(),insertCollegeQuery.toString(),insertSchoolQuery.toString()};
			System.out.println("control is here");
			for(int i=0;i<queries.length;i++)
				System.out.println(queries[i]);
			executeQueries(queries);
			}
		
	 }
	 int getIntegerData(String yop){
		 try{
			 if(yop.equals("na")||yop.equals("nil"))
				 return 0;
			 else {
				 int x= Integer.parseInt(yop);
			 	return x;
			 }
		 }catch(NumberFormatException e){
			 return 0;
		 }
	 }
	 String getDate(String s)
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
		 return s;
	 }
	 
	 float getFloatData(String yop){
		 try{
			 if(yop.equals("na")||yop.equals("nil"))
				 return 0;
			 else {
				 float x= Float.parseFloat(yop);
			 	return x;
			 }
		 }catch(NumberFormatException e){
			 return 0;
		 }
	 }
	 void executeQueries(final String queries[])
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
					 st.executeQuery("use "+(String)listDatabases.getSelectedItem());
					 for(int k=0;k<queries.length;k++){
						 st.executeUpdate(queries[k]);
					 }
					 st.close();
					 con.close();
					 
				 }
				 catch(SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e)
				 {
					 e.printStackTrace();
					 System.out.println("problem");
					 StaticMethodClass.addErrorLabel("Insertion Unsuccesful", 3000);
					 MainFrame.globalInstance.repaint();
					 MainFrame.globalInstance.revalidate();
				 }				
			}
		}).start();
		 
	 }
	 boolean checkEnrollmentNumber(String enrollment)
	 {
		 try
		 {
			 int counter=0;
			 String url = "jdbc:mysql://localhost:3306/", user = "root", passwrd = "";
			 Class.forName("com.mysql.jdbc.Driver").newInstance();
			 Connection con = DriverManager.getConnection(url, user, passwrd);
			 Statement st = con.createStatement();
			 st.executeQuery("use "+(String)listDatabases.getSelectedItem());
			 ResultSet resultSet = st.executeQuery("Select * from frequentinfo where enrollment_number='"+enrollment+"'");
			 
			 
			 while(resultSet.next())
			 {

				 counter++;
			 }
			 st.close();
			 con.close();
			 if(counter==0)
				 return true;
			 else
				 return false;
		 }
		 catch(SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e)
		 {
			 e.printStackTrace();
			 System.out.println("problem");
			 StaticMethodClass.addErrorLabel("Duplicate Enrollment Number", 3000);
			 MainFrame.globalInstance.repaint();
			 MainFrame.globalInstance.revalidate();
		 }
		return false;
	 }
	 static void getJTableonFrame()
		{
			Component[] components = MainFrame.mainContent.getComponents(); 
			if(components.length>1)
				MainFrame.mainContent.remove(1); //jtable yaha se form hoga
		MainFrame.mainContent.add(new InsertRowToDatabase(),
					new GridBagConstraints(0, 1, 1, 1, 1, 1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(20, 50, 20, 50), 0, 0));
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
		}
}
