package com.minorproject.gtbit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import resources.StaticMethodClass;

class HelpClass extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	JEditorPane textArea = new JEditorPane("text/html", "");


	String add = "ADD SECTION";
	String help = " <b>ADD SECTION</b> <br> Locate excel file of student database. Only excel file should be loaded in the system.<br>"
			+ " <b>INSERT SECTION</b> have following constraints keep them in mind <br> 1. Enrolment number can have maximum 12 digits"
			+ " <br> 2. Contact number and alternate contact number can have 10-12 digits  " +
			"<br> 3. Date of birth must be of the format dd-mm-yyyy"
			+ "<br> 4. Email-id must be a gmail account only"
			+ "<br> 5. backlogs with subject code fields are allowed to have multiple values separated by comma"
			+ "<br> 6. 10th, 12th and diploma board can have maximum of 5 characters" 
			+ "<br> 7. value added courses can have maximum of 100 characters"
			+ "<br> 8. there should be only one dream company." +
			"<br> 9. gender can be either male or female, no short forms to be used"
			+"<br> 10. all the fields are mandatory"
			+ "<br><br> <b>SEARCH SECTION</b>- student's data can be searched by either of the following fields " +
			" name, branch_section, 10th  aggregate, 12th aggregate, " +
			" diploma aggregate and b.tech aggregate, contact number, email-id, total backlogs and  pending backlogs"
			+ "<br> Any of the field can be used to search data. There is no need to fill all the entries."
			+ "<br><br> <b>DELETE SECTION</b>- To delete record of any student click on Delete tab in main menu " +
			"and enter the enrollment number of student whose record is to be deleted"
			+ "<br><br> <b>MAIL SECTION</b>\n 1. To send mail first enter your gmail id and password, " +
			"fill all the fields" +
			"<br> 2. Enter the email id of the receipient manually"
			+ "<br> 3. Right Now there is now provision of attachments you can send only text messages"
			+ "<br><br> <b>PRINT SECTION</b>- " +
			"<br> 1. Currently only limited fields can be printed." +
			"<br> 2. Fields are Enrollment Number, Name, Branch & Section,Contact Number and Email Id"
			+ "<br> 3. Once you click print button system will generate a 'pdf' file which can be further printed by giving print  commmand";



	public HelpClass()
	{
		setLayout(new BorderLayout());
		textArea.setText(help);
		
		
		
		textArea.setEditable(false);
		setBackground(StaticMethodClass.blueColor);
		textArea.setFont(new Font(StaticMethodClass.defaultFont,Font.PLAIN,18));
		JScrollPane scrollPane = new JScrollPane(textArea);		
		scrollPane.setPreferredSize(new Dimension(200, 480));
		add(scrollPane);
	}

	static void getJTableonFrame()
	{
		Component[] components = MainFrame.mainContent.getComponents();
		if (components.length > 1)
			MainFrame.mainContent.remove(1); 
		MainFrame.mainContent.add(new HelpClass(), new GridBagConstraints(0, 1,
				1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(70, 150, 20, 150), 0,
				0));
		MainFrame.globalInstance.repaint();
		MainFrame.globalInstance.revalidate();
	}
}
