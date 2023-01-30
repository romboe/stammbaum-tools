package at.romboe.stammbaumtools.sortierer;

import java.io.IOException;
import java.util.List;

import at.romboe.stammbaumtools.Printer;
import at.romboe.stammbaumtools.model.Person;
import at.romboe.stammbaumtools.parser.Parser;

public class Sortierer {

	private static final int UNDEFINED = -1;


	public static final void main(String[] args) {
		Parser p = new Parser("data.js");
		List<Person> ps = p.getPersons();
		ps.sort(new DateComparator());
		reassignIds(ps);
		try {
			Printer.printToFile(ps);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void reassignIds(List<Person> ps) {
		// Avoid reassignment of previously assigned ids
		int offset = 10000;
		for (int i=0; i<ps.size(); i++) {
			Person p = ps.get(i);
			int id = p.getId();
			int newId = offset + i;
			// System.out.format("alt: %3d, neu: %3d, %s %s, mutter:%3d\n", id, newId, p.getLastname(), p.getFirstname(), p.getMother());
			p.setId(newId);
			for (int j=0; j<ps.size(); j++) {
				Person p2 = ps.get(j);
				if (p2.getFather() == id) {
					p2.setFather(p.getId());
				}
				else if (p2.getMother() == id) {
					p2.setMother(p.getId());
				}
			}
		}

		for (int i=0; i<ps.size(); i++) {
			Person p = ps.get(i);
			p.setId(p.getId() - offset);
			if (p.getMother() != UNDEFINED) {
				p.setMother(p.getMother() - offset);
			}
			if (p.getFather() != UNDEFINED) {
				p.setFather(p.getFather() - offset);
			}
			System.out.format("id: %3d, %s %s, mutter:%3d\n", p.getId(), p.getLastname(), p.getFirstname(), p.getMother());
		}
	}
}
