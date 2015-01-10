package resources;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;

public class MyJButton extends JButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Color bg = new Color(129, 207, 224);
	public MyJButton(String name)
	{
		super(name);
		setFocusPainted(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		UIManager.put("Button.select", Color.LIGHT_GRAY);
	}
	
	public MyJButton(ImageIcon name)
	{
		super(name);
		setFocusPainted(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		UIManager.put("Button.select", Color.LIGHT_GRAY);
	}
	public MyJButton(String string,boolean b)
	{
		setIcon(new ImageIcon(string));
		setFocusPainted(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		UIManager.put("Button.select", Color.LIGHT_GRAY);
	}
	public MyJButton(ImageIcon icon,String text)
	{
		super(text,icon);
		setFocusPainted(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		UIManager.put("Button.select", Color.LIGHT_GRAY);
	}
}
