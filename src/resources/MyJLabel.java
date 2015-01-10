package resources;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MyJLabel extends JLabel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MyJLabel(String text)
	{
		super(text);
		setForeground(Color.black);
		setBackground(Color.white);
	}
	public MyJLabel(ImageIcon image)
	{
		super(image);
		setForeground(StaticMethodClass.labelForeground);
	}
}
