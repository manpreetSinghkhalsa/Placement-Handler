package com.minorproject.gtbit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import resources.MyJButton;
import resources.MyJTextField;
import resources.StaticMethodClass;

class MailPanelClass extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MyJTextField to,subject;
	JTextArea body;
	MyJButton send,cancel;
	Insets insets = new Insets(30, 10, 10, 10);
	String[] mailid;
	public MailPanelClass(String[] mailid)
	{
		to = new MyJTextField(50, "TO");
		this.mailid = mailid;
		
		StringBuilder sb = new StringBuilder("");
		if(mailid!=null)
		{
			for(int i=0;i<mailid.length;i++)
			{
				sb.append(mailid[i]);
				sb.append(";");
			}
			to.setText(sb.toString());
		}
		
		subject = new MyJTextField(50, "Subject");
		body = new JTextArea(7, 5);
		body.setLineWrap(true);
		send = new MyJButton("Send");
		cancel = new MyJButton("Discard");
		
		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,Font.PLAIN,StaticMethodClass.normalFontSize), to,subject,body,send,cancel);
		
		StaticMethodClass.setBorder(null,to,subject,body,send,cancel);
		setBackground(StaticMethodClass.blueColor);
		setLayout(new GridBagLayout());
		
		StaticMethodClass.setBackground(Color.BLACK, send,cancel);
		StaticMethodClass.setForeground(Color.WHITE, send,cancel);

		
		add(to, new GridBagConstraints(0, 0, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets, 0, 0));
		add(subject, new GridBagConstraints(0, 1, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets, 0, 0));
		add(body, new GridBagConstraints(0, 2, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));
		add(send, new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		add(cancel, new GridBagConstraints(1, 3, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		send.addActionListener(this);
		cancel.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		JButton button = (JButton)event.getSource();
		if(button==send)
		{
			new MailClass(to.getText(), subject.getText(), body.getText());
		}
		else if(button==cancel)
		{
			MainFrame.mainContent.remove(1);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();			
		}
	}
	
	static void getJTableonFrame(String[] mailid)
	{
		Component[] components = MainFrame.mainContent.getComponents(); 
		if(components.length>1)
			MainFrame.mainContent.remove(1);
			
		MainFrame.mainContent.add(new JScrollPane(new MailPanelClass(mailid)),
				new GridBagConstraints(0, 1, 1, 1, 1, 1,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(65+25, 0, 65-25, 0), 0, 0));
		MainFrame.globalInstance.repaint();
		MainFrame.globalInstance.revalidate();
		
	}
}
