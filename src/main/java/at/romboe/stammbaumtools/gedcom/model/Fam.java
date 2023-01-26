package at.romboe.stammbaumtools.gedcom.model;

import java.util.List;
import java.util.Optional;

import lombok.Data;

@Data
public class Fam {
	private String id;
	private Optional<String> husb;
	private Optional<String> wife;
	private List<String> children;
}
