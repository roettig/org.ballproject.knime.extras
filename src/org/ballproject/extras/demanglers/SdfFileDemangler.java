package org.ballproject.extras.demanglers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Iterator;

import org.ballproject.extras.types.SDFCell;
import org.ballproject.knime.base.mime.MIMEFileCell;
import org.ballproject.knime.base.mime.demangler.Demangler;
import org.knime.chem.types.SdfCell;
import org.knime.chem.types.SdfCellFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.url.MIMEType;


public class SdfFileDemangler implements Demangler
{
	
	@Override
	public MIMEType getMIMEType()
	{
		return MIMEType.getType("sdf");
	}
	
	@Override
	public DataType getTargetType()
	{
		return DataType.getType(SdfCell.class);
	}

	/*
	@Override
	public MIMEFileCell mangle(Iterator<DataCell> iter)
	{
		MIMEFileCell ret = new SDFFileCell();
		String data = concatenate(iter);
		// use tempfile here 
		//ret.getDelegate().setContent(data.getBytes());
		return ret;
	}
	
	*/
	
	public static String concatenate(Iterator<DataCell> iter)
	{
		StringBuffer sb = new StringBuffer();
		while(iter.hasNext())
		{
			SDFCell cell = (SDFCell) iter.next();
			sb.append(cell.getContents());
			sb.append("$$$$"+System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
	private static class SdfFileDemanglerDelegate implements Iterator<DataCell>
	{
		private BufferedReader br;
		private InputStream in;
		
		public SdfFileDemanglerDelegate(File infile)
		{
			try
			{
				in = new FileInputStream(infile);
			} 
			catch (FileNotFoundException e1)
			{
				e1.printStackTrace();
				throw new RuntimeException("could not open input file");
			}
			br  = new BufferedReader(new InputStreamReader(in));
		}
		
		@Override
		public boolean hasNext()
		{
			boolean ready = false;
			try
			{
				ready =  br.ready();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			if(!ready)
			{
				try
				{
					br.close();
				} 
				catch (IOException e)
				{

					e.printStackTrace();
				}
			}
			return ready;
		}
		
		@Override
		public DataCell next()
		{
			String line = "";
			StringBuffer sb = new StringBuffer();
			try
			{
				while((line=br.readLine())!=null)
				{
					sb.append(line+System.getProperty("line.separator"));
					// keep $$$$ in SDF string 
					if(line.startsWith("$$$$"))
						break;
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return SdfCellFactory.create(sb.toString());
		}

		@Override
		public void remove()
		{
			// NOP
		}
	}

	@Override
	public Iterator<DataCell> demangle(URI file)
	{
		return new SdfFileDemanglerDelegate(new File(file));
	}

	@Override
	public MIMEFileCell mangle(Iterator<DataCell> iter)
	{
		return null;
	}
}
