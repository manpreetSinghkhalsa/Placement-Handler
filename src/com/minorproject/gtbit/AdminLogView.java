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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import resources.MyJButton;
import resources.StaticMethodClass;

class AdminLogView extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable table;
	DefaultTableModel tableModel ;
	String backLoc = "images\\back_24pxwhite.png";
	MyJButton back;
	Insets insets = new Insets(10, 10, 10, 10);
	public AdminLogView()
	{
		back = new MyJButton(new ImageIcon(backLoc), "Back");
		
		DefaultTableModel tableModel = getTableModel();
		table = new JTable(tableModel);
		table.setFont(new Font(StaticMethodClass.defaultFont, Font.PLAIN, 18));
		StaticMethodClass.resizeColumnWidth(table);
		
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		for(int i=0;i<3;i++)
		{
			table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
		}

		table.setRowHeight(25);
		
		
		JScrollPane scrollpane = new JScrollPane(table);
		
		setBackground(StaticMethodClass.blueColor);
		StaticMethodClass.setBackground(Color.BLACK, back);
		StaticMethodClass.setForeground(Color.WHITE, back);
		setLayout(new GridBagLayout());
		add(scrollpane, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets, 0, 0));
		add(back, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		back.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JButton button = (JButton) e.getSource();
				if(button==back)
					AdminPanelClass.getJTableonFrame();
			}
		});
	}
	
	DefaultTableModel getTableModel()
	{
		final DefaultTableModel tableModel = new DefaultTableModel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		String columns[] = {"SNO.","User-ID","LOG-IN", "LOG-OUT"};
		for (int i = 0; i < columns.length; i++)
		{
			tableModel.addColumn(columns[i]);
		}
		
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				try
				{
					int counter=1;
					
					String url = "jdbc:mysql://localhost:3306/main", user = "root", passwrd = "";
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection con = DriverManager.getConnection(url, user, passwrd);
		
					Statement statement = con.createStatement();
					statement.executeQuery("use main");
					ResultSet resultSet  = statement.executeQuery("Select * from logtable");
					
					while(resultSet.next())
					{
						String data[] = new String[4];
						data[0] = ""+counter;
						data[1] = resultSet.getString(1);
						data[2] = resultSet.getString(2);
						data[3] = resultSet.getString(3);
						tableModel.addRow(data);
						counter++;
					}
				}
		
				catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e)
				{
					StaticMethodClass.addErrorLabel("Problem occured", 1000);
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
					e.printStackTrace();
				}

				
			}
		}).start();
	return tableModel;
}
	
	static void getJTableonFrame()
	{
		Component[] components = MainFrame.mainContent.getComponents(); 
		if(components.length==1)
		{
			MainFrame.mainContent.remove(0);
		}	
		MainFrame.mainContent.add(new AdminLogView(),
				new GridBagConstraints(0, 0, 0, 0, 1,
						1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(20, 50, 20, 50), 0, 0));
		MainFrame.globalInstance.repaint();
		MainFrame.globalInstance.revalidate();
	}
}
