package at.romboe.stammbaumtools.gedcom.model.helper;

import java.util.List;
import java.util.Optional;

import at.romboe.stammbaumtools.gedcom.model.Indi;

public class GedcomModelHelper {

	public static Optional<Indi> findIndi(List<Indi> indis, String id) {
		return indis.stream().filter(i -> id.equals(i.getId())).findFirst();
	}
}
