package at.romboe.stammbaumtools.gedcom.model.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.romboe.stammbaumtools.gedcom.model.Fam;

public class FamFactory {

	public static Fam createFam(String id) {
		Fam fam = new Fam();
		fam.setId(id);
		fam.setHusb(Optional.empty());
		fam.setWife(Optional.empty());
		List<String> children = new ArrayList<>();
		fam.setChildren(children);
		return fam;
	}
}
