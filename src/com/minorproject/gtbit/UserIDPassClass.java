package com.minorproject.gtbit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.JPanel;

import resources.MyJButton;
import resources.MyJPasswordField;
import resources.MyJTextField;
import resources.StaticMethodClass;

//User ID PAssword class

public class UserIDPassClass extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MyJPasswordField passwordField = new MyJPasswordField(20, "Password");
	MyJTextField userName = new MyJTextField(20, "Userid ( Gmail id only )");

	MyJButton nextScreen = new MyJButton("Login");
	Insets insets = new Insets(10, 5, 20, 5);

	static String id="";
	static String pass="";
	public Insets getInsets() {
		return new Insets(10, 10, 10, 10);
	}
	String mailId[];
	public UserIDPassClass(final String[] mailId) {
		this.mailId = mailId; 
		setLayout(new GridBagLayout());

		// change accordingly
		setBackground(StaticMethodClass.blueColor);

		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,
				Font.PLAIN, StaticMethodClass.normalFontSize), userName,
				passwordField,nextScreen);
		
		StaticMethodClass.setBorder(null, userName,passwordField,nextScreen);
		
		StaticMethodClass.setBackground(Color.BLACK, nextScreen);
		StaticMethodClass.setForeground(Color.WHITE, nextScreen);
		
		
		add(userName, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(passwordField, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));
		add(nextScreen, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));

		nextScreen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String user = userName.getText();
				String password = new String(passwordField.getPassword());
				if(user.equals("")||password.equals(""))
				{
					StaticMethodClass.addErrorLabel("Fill both the Fields",2000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
				}
					
				else if (validateLogin(user, password)) 
				{
					id=user;
					pass = password;
					MailPanelClass.getJTableonFrame(mailId);
				} else{
					StaticMethodClass.addErrorLabel("Userid must be GMAIL id only",2000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();

				}
					
			}
		});
		}
	
		boolean validateLogin(String user, String password)
		{
			if(user.equals("")||password.equals(""))
				return false;
			else
			{
				StringTokenizer st = new StringTokenizer(user,"@");
				int x = st.countTokens();
				if(x==2)
				{
					st.nextToken();
					String gmail = st.nextToken();
					if(gmail.equals("gmail.com"))
						return true;			
				}
				else
					return false;
			}
			return false;
		}
		static void getJTableonFrame(String[] mailid)
		{
			Component[] components = MainFrame.mainContent.getComponents(); 
			if(components.length>1)
				MainFrame.mainContent.remove(1); //jtable yaha se form hoga
				
			MainFrame.mainContent.add(new UserIDPassClass(mailid),new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(200,0,100,0), 0, 0));
		
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
			
		}

	}
