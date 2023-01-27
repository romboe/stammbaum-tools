package at.romboe.stammbaumtools.gedcom.model;

import java.util.List;

import lombok.Data;

@Data
public class Indi {
	private String id;
	private String name;
	private String givn;
	private String surn;
	private String birt;
	private List<String> fams;
	private List<String> famc;
}
