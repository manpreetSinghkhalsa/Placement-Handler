package com.minorproject.gtbit;


import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JTable;

import resources.StaticMethodClass;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

class CreatePDF
{
	JTable table;
	String location="";
	
	public CreatePDF()
	{
		table = DisplayPreciseData.table;
		location = getLocationUsingJFC();
		String columns[] = {"Enrollment Number","Name","Branch","Contact","Email ID"};
		
		if(!location.equals(""))
		{	
			try
			{
				StaticMethodClass.addErrorLabel("Generating PDF, Wait...", 5000);
				MainFrame.globalInstance.repaint();
				MainFrame.globalInstance.revalidate();

				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(location));
				document.open();
				PdfPTable pdfTable = new PdfPTable(5);
				
				for(int i=0;i<columns.length;i++)
			    {
					PdfPCell c1 = new PdfPCell(new Phrase(columns[i]));
			        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			        pdfTable.addCell(c1);
			    }

				pdfTable.setHeaderRows(1);
				System.out.println("-------------------------------------");
				System.out.println("rows: "+table.getRowCount());
				System.out.println("CCCrows: "+table.getColumnCount());
				for(int rows=0;rows<table.getRowCount();rows++)
			   {
					for(int col=0;col<table.getColumnCount();col++)
			   	    {
						
						if(col!=4&&col!=6)
						{
							String data = table.getModel().getValueAt(rows,col).toString();
							pdfTable.addCell(new Phrase(data,FontFactory.getFont(FontFactory.HELVETICA, 8)));
						}
			   	    }
			   }
			    document.add(pdfTable);
				document.close();
				StaticMethodClass.addErrorLabel("PDF Generated", 3000);
				MainFrame.globalInstance.repaint();
				MainFrame.globalInstance.revalidate();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	static String getLocationUsingJFC()
	{
		JFileChooser fileChooser = new JFileChooser();
		int us = fileChooser.showSaveDialog(null);
		String loc = "";
		if(us==JFileChooser.APPROVE_OPTION)
		{
			File file;
			file = fileChooser.getSelectedFile();
			if(!file.exists())
			{
				System.out.println("Not existed   "+file.getAbsolutePath());
				loc = file.getAbsolutePath()+".pdf";
			}
			else
			{
				loc="";
				StaticMethodClass.addErrorLabel("File name already exists", 5000);
				MainFrame.globalInstance.repaint();
				MainFrame.globalInstance.revalidate();
			}			
		}
		return loc;
	}
}
