package at.romboe.stammbaumtools.gedcom.model.helper;

import java.util.ArrayList;

import at.romboe.stammbaumtools.gedcom.model.Indi;

public class IndiFactory {

	public static Indi createIndi(String id) {
		Indi indi = new Indi();
		indi.setId(id);
		indi.setFams(new ArrayList<>());
		indi.setFamc(new ArrayList<>());
		return indi;
	}
}
