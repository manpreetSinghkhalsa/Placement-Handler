package resources;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

/*
 * MyJTextfield is a class which extends standard javax.swing.JTextField and
 * adds HTML Placeholder Functionality (grayed text)
 */
public class MyJTextField extends JTextField
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String placeholder = "";

	public MyJTextField(int pColumns)
	{
		super(pColumns);
		init();
	}

	public MyJTextField(int pColumns, String placeholder)
	{
		super(pColumns);
		this.placeholder = "  " + placeholder;
		init();
	}

	public MyJTextField(String text)
	{
		super(text);
		init();
	}

	public MyJTextField(String text, String placeholder)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 * paintComponent method is called whenever component state is changed. When
	 * text is 0, we write the placeholder text. This looks like HTML
	 * placeholder implementation
	 */
	@Override
	protected void paintComponent(final Graphics pG)
	{
		super.paintComponent(pG);

		if (placeholder.length() == 0 || getText().length() > 0)
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
