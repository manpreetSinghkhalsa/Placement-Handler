package resources;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;



/*
 * MyJPasswordField is a class which extends standard javax.swing.JPasswordField
 * and adds HTML Placeholder Functionality (grayed text)
 */
public class MyJPasswordField extends JPasswordField
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String placeholder = "";

	public MyJPasswordField(int pColumns)
	{
		super(pColumns);
		init();
	}

	public MyJPasswordField(int pColumns, String placeholder)
	{
		super(pColumns);
		this.placeholder = "  " + placeholder;
		init();
	}

	public MyJPasswordField(String text)
	{
		super(text);
		init();
	}

	public MyJPasswordField(String text, String placeholder)
	{
		super(text);
		this.placeholder = "  " + placeholder;
		init();
	}

	private void init()
	{
		setBorder(BorderFactory
				.createLineBorder(new Color(86,164,246)));
	}

	public String getPlaceholder()
	{
		return placeholder;
	}

	@Override
	protected void paintComponent(final Graphics pG)
	{
		super.paintComponent(pG);

		if (placeholder.length() == 0 || new String(getPassword()).length() > 0)
		{
			return;
		}

		final Graphics2D g = (Graphics2D) pG;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.GRAY);
		g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
				.getMaxAscent() + getInsets().top);
	}

	public void setPlaceholder(final String s)
	{
		placeholder = s;
	}
}
