package com.minorproject.gtbit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import resources.MyJButton;
import resources.StaticMethodClass;

class DisplayPreciseData extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Statement st;
	Connection con;

	String columns[] = { "Enrollment_Number", "Name", "Branch_Section",
			"Contact_Number", "Date_of_Birth", "Email_Id", "Father_Name" };

	final String databaseName;
	final String query;
	static int count = 0, point = 0;
	static JTable table;
	MyJButton print,mail;
	public DisplayPreciseData(final String databaseName, final String query)
	{
		Insets insets = new Insets(10, 10, 10, 10);
		this.databaseName = databaseName;
		this.query = query;
		
		setBackground(StaticMethodClass.blueColor);

		setLayout(new GridBagLayout());

		DefaultTableModel tableModel = getTableModel();
		table = new JTable(tableModel);
		table.setFont(new Font(StaticMethodClass.defaultFont, Font.PLAIN, 18));
		StaticMethodClass.resizeColumnWidth(table);
		table.setRowHeight(table.getRowHeight() + 5);
		table.setRowHeight(25);
		
		//--Center align data or table------------//
		DefaultTableCellRenderer stringRenderer = (DefaultTableCellRenderer)
		table.getDefaultRenderer(String.class);
		stringRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		//---------------------------------------//
		JScrollPane scrollpane = new JScrollPane(table);
		add(scrollpane, new GridBagConstraints(0, 0, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets, 0, 0));

		print = new MyJButton("PRINT");
		mail = new MyJButton("MAIL");
		StaticMethodClass.setFont(new Font(StaticMethodClass.defaultFont,Font.PLAIN,StaticMethodClass.normalFontSize),print,mail);
		StaticMethodClass.setBorder(null,print,mail);
		
		StaticMethodClass.setBackground(Color.BLACK,print,mail);
		StaticMethodClass.setForeground(Color.WHITE,print,mail);
		
		
		add(print,new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		add(mail,new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		mail.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int rowCount= table.getRowCount();
				String query[] = new String[rowCount];
				for(int i=0;i<rowCount;i++)
				{
					query[i] = (String)table.getModel().getValueAt(i,5);					
				}
	
				System.out.println("-------------------------");
				for(int i=0;i<query.length;i++)
				{
					System.out.println(query[i]);
				}
				UserIDPassClass.getJTableonFrame(query);
			}
		});
		
		
		print.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new CreatePDF();
			}
		});
		
		table.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{
				count++;
				if (count == 1)
				{
					point = table.rowAtPoint(e.getPoint()); // values are 0
															// indexed; mind it

					System.out.println("count 1");
				}
				if (count == 2)
				{
					count = 0;
					int newPoint = table.rowAtPoint(e.getPoint());
					if (newPoint == point)
					{
						System.out.println("double clicked " + point);
						String enroll = (String) table.getModel().getValueAt(point, 0);
						System.out.println("Enrollemnt value: "+enroll);
						DisplayDetailedData.getDetailedJTableonFrame(enroll, databaseName,query);
					} else
						point = 0;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
			}

			public void mousePressed(MouseEvent e)
			{
			}

			public void mouseExited(MouseEvent e)
			{
			}

			public void mouseEntered(MouseEvent e)
			{
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
					String url = "jdbc:mysql://localhost:3306/", user = "root", passwrd = "";
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection con = DriverManager.getConnection(url, user, passwrd);
					Statement st = con.createStatement();
					st.executeQuery("use " + databaseName);
					ResultSet resultSet = st.executeQuery(query);
					while (resultSet.next())
					{
						String arr[] = new String[7];

						for (int i = 0; i < 7; i++)
							arr[i] = resultSet.getString(i + 1);
						tableModel.addRow(arr);
					}
					st.close();
					con.close();
				} catch (SQLException | InstantiationException | IllegalAccessException
						| ClassNotFoundException e)
				{
					e.printStackTrace();
					System.out.println("problem");
				}

				
			}
		}).start();
		return tableModel;
	}
	
	static void getJTableonFrame(String databaseName, String query)
	{
		Component[] components = MainFrame.mainContent.getComponents(); 
		if(components.length>1)
			MainFrame.mainContent.remove(1); //jtable yaha se form hoga
	MainFrame.mainContent.add(new DisplayPreciseData(databaseName, query),
				new GridBagConstraints(0, 1, 1, 1, 1, 1,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(20, 50, 20, 50), 0, 0));
		MainFrame.globalInstance.repaint();
		MainFrame.globalInstance.revalidate();
		
		System.out.println("in the static method now");
	}
}