package org.ballproject.extras.types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.knime.core.data.DataCell;

public class SDFCell extends DataCell implements SDFCellValue
{
	private String contents;
	private String name;
	private Map<String,String> properties = new HashMap<String,String>();
	
	public static SDFCell makeCell(String contents)
	{
		return new SDFCell(contents);
	}
	
	public SDFCell(String contents)
	{
		this.contents = contents;
		parse();
	}
	
	@Override
	public String getSDFString()
	{
		return contents;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getProperty(String name)
	{
		return properties.get(name);
	}

	@Override
	protected boolean equalsDataCell(DataCell dc)
	{
		if(!(dc instanceof SDFCell))
		{
			return false;
		}
		SDFCell cell = (SDFCell) dc;
		return contents.equals(cell.contents);
	}

	@Override
	public int hashCode()
	{
		return contents.hashCode();
	}

	@Override
	public String toString()
	{
		return contents;
	}

	public String getContents()
	{
		return contents;
	}	
	
	private void parse()
	{
		BufferedReader br = new BufferedReader(new StringReader(this.contents));
		String line="";
		try
		{
			String firstLine = br.readLine();
			name = firstLine.trim();
			boolean props = false;
			
			String key = "";
			String val = "";
			
			while((line=br.readLine())!=null)
			{
				if(props && line.startsWith(">"))
				{
					int idx1 = line.indexOf("<");
					int idx2 = line.lastIndexOf(">");
					key = line.substring(idx1+1,idx2);
					val = br.readLine().trim();
					properties.put(key, val);
				}
				
				if(line.contains("M  END"))
					props = true;
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
