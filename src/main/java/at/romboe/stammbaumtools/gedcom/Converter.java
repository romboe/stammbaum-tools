package at.romboe.stammbaumtools.gedcom;

import static at.romboe.stammbaumtools.model.helper.ModelHelper.findPersonById;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import at.romboe.stammbaumtools.gedcom.model.Fam;
import at.romboe.stammbaumtools.gedcom.model.Indi;
import at.romboe.stammbaumtools.gedcom.model.helper.FamFactory;
import at.romboe.stammbaumtools.gedcom.model.helper.GedcomModelHelper;
import at.romboe.stammbaumtools.gedcom.model.helper.IndiFactory;
import at.romboe.stammbaumtools.model.Person;

public class Converter {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
	private Map<String,Fam> famMap = new HashMap<>();


	public Gedcom convert(List<Person> persons) {
		List<Indi> indis = toIndis(persons);
		createFams(persons, indis);

		Gedcom gedcom = new Gedcom();
		gedcom.setIndis(indis);
		gedcom.setFams(famMap.values());
		return gedcom;
	}

	public void createFams(List<Person> persons, List<Indi> indis) {
		for (Person p:persons) {
			Optional<Person> father = findPersonById(persons, p.getFather());
			Optional<Person> mother = findPersonById(persons, p.getMother());

			String famId = buildFamId(father, mother);
			if (famId.length() > 1) {
				Fam family = famMap.get(famId);
				if (null == family) {
					Fam fam = FamFactory.createFam(famId);
					father.ifPresent(f -> {
						fam.setHusb(Optional.of(f.getUuid()));
						addFamsRef(indis, f.getUuid(), famId);
					});
					mother.ifPresent(m -> {
						fam.setWife(Optional.of(m.getUuid()));
						addFamsRef(indis, m.getUuid(), famId);
					});
					fam.getChildren().add(p.getUuid());
					famMap.put(famId, fam);
				}
				else {
					family.getChildren().add(p.getUuid());
				}

				Optional<Indi> indi = GedcomModelHelper.findIndi(indis, p.getUuid());
				indi.ifPresent(i -> i.getFamc().add(famId));
			}
		}
	}

	private static void addFamsRef(List<Indi> indis, String id, String famId) {
		Optional<Indi> indi = GedcomModelHelper.findIndi(indis, id);
		indi.ifPresent(i -> i.getFams().add(famId));
	}

	public static String buildFamId(Optional<Person> father, Optional<Person> mother) {
		StringBuilder sb = new StringBuilder();
		father.ifPresent(f -> sb.append(f.getUuid()));
		sb.append('-');
		mother.ifPresent(m -> sb.append(m.getUuid()));
		return sb.toString();
	}

	public static List<Indi> toIndis(List<Person> persons) {
		List<Indi> ret = new ArrayList<>();
		for (Person p:persons) {
			ret.add(toIndi(p));
		}
		return ret;
	}

	public static Indi toIndi(Person person) {
		Indi indi = IndiFactory.createIndi(person.getUuid());
		indi.setName(person.getFirstname() + " " + person.getLastname());
		indi.setGivn(person.getFirstname());
		indi.setSurn(person.getLastname());
		person.getBirth().ifPresent(d -> indi.setBirt(d.format(DATE_FORMATTER)));
		return indi;
	}
}
