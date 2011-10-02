package org.ballproject.extras.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ballproject.CADDSuite.knime.nodes.mimetypes.SDFFileCell;
import org.ballproject.extras.demanglers.SDFFileDemangler;
import org.ballproject.extras.test.data.TestDataProvider;
import org.ballproject.extras.types.SDFCell;
import org.junit.Test;
import org.knime.core.data.DataCell;

public class SDFFileDemanglerTest
{

	@Test
	public void test() throws IOException
	{
			BufferedReader br = new BufferedReader(new InputStreamReader(TestDataProvider.class.getResourceAsStream("mols.sdf")));
			String line = "";
			StringBuffer sb = new StringBuffer();
			while((line=br.readLine())!=null)
			{
				sb.append(line+"\n");
			}

			String cont = sb.toString();

			SDFFileCell sdfc = new SDFFileCell();
			// FIXME
			//sdfc.setData(cont.getBytes());
			SDFFileDemangler demangler = new SDFFileDemangler(); 

			List<DataCell> dcs = new ArrayList<DataCell>();
			
			
			// test demangler
			Iterator<DataCell> iter = demangler.demangle(sdfc);

			String[] names = new String[]{"COO_23A","COO_23E","COO_23J"};
			String[] acts  = new String[]{"7.92","5.6","6.41"};
			String[] sets  = new String[]{"1","2","0"};

			int i = 0;
			
			while(iter.hasNext())
			{
				SDFCell sc = (SDFCell) iter.next();
				dcs.add(sc);	
				assertEquals(names[i],sc.getName());
				assertEquals(sets[i],sc.getProperty("SET"));
				assertEquals(acts[i],sc.getProperty("ACTIVITY"));
				i++;
			}

			
			
			// test mangler
			String s = SDFFileDemangler.concatenate(dcs.iterator());
			assertEquals(cont, s);
	}

}
