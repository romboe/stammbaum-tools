package at.romboe.stammbaumtools.sortierer;

import java.util.Comparator;

import at.romboe.stammbaumtools.model.Person;


public class DateComparator implements Comparator<Person> {

	public int compare(Person p1, Person p2) {
		if (p1.getBirth().isPresent() && p2.getBirth().isPresent()) {
			if (p1.getBirth().get().isBefore(p2.getBirth().get())) {
				return -1;
			}
			else if (p2.getBirth().get().isBefore(p1.getBirth().get())) {
				return 1;
			}
		}
		else if (p1.getBirth().isPresent()) {
			return 1;
		}
		else if (p2.getBirth().isPresent()) {
			return -1;
		}
		return 0;
	}

}
