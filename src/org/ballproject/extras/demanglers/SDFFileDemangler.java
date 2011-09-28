package org.ballproject.extras.demanglers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.ballproject.extras.types.SDFCell;
import org.ballproject.knime.base.mime.MIMEFileCell;
import org.ballproject.knime.base.mime.demangler.Demangler;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;

import org.ballproject.CADDSuite.knime.nodes.mimetypes.SDFFileCell;

public class SDFFileDemangler implements Demangler
{

	@Override
	public DataType getSourceType()
	{
		return DataType.getType(SDFFileCell.class);
	}

	@Override
	public DataType getTargetType()
	{
		return DataType.getType(SDFCell.class);
	}

	@Override
	public Iterator<DataCell> demangle(MIMEFileCell cell)
	{
		return new SDFFileDemanglerDelegate(cell.getData());
	}

	@Override
	public MIMEFileCell mangle(Iterator<DataCell> iter)
	{
		MIMEFileCell ret = new SDFFileCell();
		String data = concatenate(iter);
		ret.getDelegate().setContent(data.getBytes());
		return ret;
	}
	
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
	
	@Override
	public void close()
	{
		// TODO Auto-generated method stub
	}
	
	private static class SDFFileDemanglerDelegate implements Iterator<DataCell>
	{
		private byte[] data;
		private BufferedReader br;
		
		
		public SDFFileDemanglerDelegate(byte[] data)
		{
			this.data = data;
			InputStream    in  = new ByteArrayInputStream(this.data);
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
					if(line.startsWith("$$$$"))
						break;
					sb.append(line+System.getProperty("line.separator"));
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return SDFCell.makeCell(sb.toString());
		}

		@Override
		public void remove()
		{
			// NOP
		}
	}
}
