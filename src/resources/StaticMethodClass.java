package resources;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.minorproject.gtbit.MainFrame;

public class StaticMethodClass 
{
	public static String login_time="";
	public static String id="";
	public static final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int screenX = dimension.width;
	public static final int screenY = dimension.height;
	public static final Color labelForeground = new Color(236,236,236); //WHITE SMOKE
	public static final String defaultFont = "Segoe UI Light";
	public static final int normalFontSize = 22;

	public static final Insets defaultInsets = new Insets(10,10,10,10);
	public static final Color screenBackground = new Color(129, 207, 224);

	public static final Color blueColor = new Color (31,58,147); 
	public static final Color adminColor = new Color(103,65,114);//or )30,130,76
	static int time;
	
	public static final void setForeground(Color color, JComponent...componentsArray )
	{
		for(JComponent component:componentsArray)
			component.setForeground(color);
	}

	public static final void setBackground(Color color, JComponent...componentsArray )
	{
		for(JComponent component:componentsArray)
			component.setBackground(color);
	}
	
	public static final void setBorder(Border border, JComponent...componentsArray)
	{
		for(JComponent component:componentsArray)
		{	
			int leftRight = 5;
			if (component instanceof MyJButton)
				leftRight = 10;
			component.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, leftRight, 5, leftRight)));
		}
	}

	public static final void setFont(Font font, JComponent...componentsArray)
	{
		for(JComponent component: componentsArray)
		{
			component.setFont(font);
		}
	}
	
	public static final void setTransparent(JButton...ButtonArray)
	{
		for(JButton button: ButtonArray)
		{
			button.setOpaque(false);
			button.setContentAreaFilled(false);
			button.setBorderPainted(false);
		} 
	}
	public static final void setActionListener(ActionListener ac,JButton...ccompo)
	{
		for(JButton component: ccompo)
			component.addActionListener(ac); 
	}
	
	public static final void resizeColumnWidth(JTable table)
	{
		for (int column = 0; column < table.getColumnCount(); column++)
		{
			TableColumn tableColumn = table.getColumnModel().getColumn(column);
			int preferredWidth = tableColumn.getMinWidth();
			int maxWidth = tableColumn.getMaxWidth();

			for (int row = 0; row < table.getRowCount(); row++)
			{
				TableCellRenderer cellRenderer = table.getCellRenderer(row,
						column);
				Component c = table.prepareRenderer(cellRenderer, row, column);
				int width = c.getPreferredSize().width
						+ table.getIntercellSpacing().width;
				preferredWidth = Math.max(preferredWidth, width);
				if (preferredWidth >= maxWidth)
				{
					preferredWidth = maxWidth;
					break;
				}
			}

			tableColumn.setPreferredWidth(preferredWidth);
		}
	}

	
	public static void addErrorLabel(String labelText, int time)
	{
		StaticMethodClass.time = time;

		MainFrame.errorPanel.removeAll();
		MyJLabel label = new MyJLabel(labelText);
		StaticMethodClass.setFont(new Font(
				StaticMethodClass.defaultFont, Font.BOLD,
				StaticMethodClass.normalFontSize), label);
		label.setForeground(Color.WHITE);
		MainFrame.errorPanel.add(label);
		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Thread.sleep(StaticMethodClass.time);
					MainFrame.errorPanel.removeAll();
					MainFrame.globalInstance.repaint();
					MainFrame.globalInstance.revalidate();
					/*SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							MainFrame.errorPanel.removeAll();
							MainFrame.globalInstance.repaint();
							MainFrame.globalInstance.revalidate();							
						}
					});*/
			

				}
				catch(InterruptedException e)
				{
					
				}
			}
		}).start();
	}
	
	public static void doLogOut()
	{
		if(StaticMethodClass.id.equals("gtbit2014"))
			System.exit(0);
		try
		{
			String url = "jdbc:mysql://localhost:3306/main", user = "root", passwrd = "";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, user, passwrd);

			Statement statement = con.createStatement();
				Date date  = new Date();
				String useDatabase = "use main";
				String insertVal = "UPDATE logtable SET `log_out_time` = '"+date+"' WHERE login_time = '"+StaticMethodClass.login_time+"'";
				
				System.out.println("query: "+insertVal);
				statement.executeQuery(useDatabase);
				statement.executeUpdate(insertVal);
				System.out.println("data updation done................");
				statement.close();
				con.close();
				
		}
		catch (Exception ex)
		{
			StaticMethodClass.addErrorLabel("Incorrect Login id password combination", 1000);
			MainFrame.globalInstance.repaint();
			MainFrame.globalInstance.revalidate();
			ex.printStackTrace();

		}

		
		
		System.exit(0);
	}
	
	public static String capitalize(String string)
	{
		StringTokenizer st = new StringTokenizer(string);
		StringBuffer sb = new StringBuffer("");
		char dummy;
		while(st.hasMoreTokens())
		{
			String gh = st.nextToken();
			dummy = gh.toUpperCase().charAt(0);
			sb.append(dummy);
			sb.append(gh.substring(1));
			sb.append(" ");
		}
		return sb.toString();
	}
}
