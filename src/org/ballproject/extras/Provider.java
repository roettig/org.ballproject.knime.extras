package org.ballproject.extras;

import java.util.ArrayList;
import java.util.List;

import org.ballproject.extras.demanglers.SDFFileDemangler;
import org.ballproject.extras.demanglers.SdfFileDemangler;
import org.ballproject.knime.base.mime.demangler.Demangler;
import org.ballproject.knime.base.mime.demangler.DemanglerProvider;


public class Provider implements DemanglerProvider
{

	@Override
	public List<Demangler> getDemanglers()
	{
		List<Demangler> ret = new ArrayList<Demangler>();
		ret.add(new SDFFileDemangler());
		ret.add(new SdfFileDemangler());
		return ret;
	}

}

