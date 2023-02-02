package at.romboe.stammbaumtools.gedcom.model;

import java.util.List;
import java.util.Optional;

import lombok.Data;

@Data
public class Indi {
	private String id;
	private String name;
	private String givn;
	private String surn;
	private Optional<String> birt;
	private Optional<String> deat;
	private List<String> fams;
	private List<String> famc;
}
