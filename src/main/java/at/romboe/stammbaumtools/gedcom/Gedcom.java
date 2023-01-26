package at.romboe.stammbaumtools.gedcom;

import java.util.Collection;
import java.util.List;

import at.romboe.stammbaumtools.gedcom.model.Fam;
import at.romboe.stammbaumtools.gedcom.model.Indi;
import lombok.Data;

@Data
public class Gedcom {

	private List<Indi> indis;
	private Collection<Fam> fams;
}
