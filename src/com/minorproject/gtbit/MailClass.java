package com.minorproject.gtbit;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import resources.StaticMethodClass;
class MailClass {

	/**
	 * @param args
	 */
	String userID,password,body,text,To,subject;
	public MailClass(String to, String subject, String body) 
	{		
		Properties props = new Properties(); 
		props.put("mail.smtp.host","smtp.gmail.com"); 
		props.put("mail.smtp.socketFactory.port","465"); 
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
		props.put("mail.smtp.auth","true"); 
		props.put("mail.smtp.port","465");
		props.put("mail.smtp.from", UserIDPassClass.id);
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator()
				{
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(UserIDPassClass.id,UserIDPassClass.pass);
					}
				}
				);
		try
		{
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(UserIDPassClass.id));
			
			String mailids[] = getMailIds(to); 
			
			InternetAddress arr[] = new InternetAddress[mailids.length];
			//(to);
			
			for(int i=0;i<mailids.length;i++)
			{
				arr[i] = new InternetAddress(mailids[i]);
			}
			
			msg.setRecipients(Message.RecipientType.CC, arr);


			msg.setSubject(subject);
			msg.setText(body);
			Transport.send(msg);
			StaticMethodClass.addErrorLabel("Mail Sent", 2000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
		}
		catch(Exception e)
		{
			StaticMethodClass.addErrorLabel("Problem Occured", 2000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();

			e.printStackTrace();
		}
	}
	
	String[] getMailIds(String to)
	{
		StringTokenizer st = new StringTokenizer(to,";");
		String result[] = new String[st.countTokens()];
		int index=-1;
		while(st.hasMoreTokens())
		{
			result[++index] = st.nextToken();
		}
		if(index==-1)
		{
			result[0] = to;
		}
		return result;
	}

}
