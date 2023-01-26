package at.romboe.stammbaumtools.sortierer;

import java.util.Comparator;
import java.util.Date;

import at.romboe.stammbaumtools.model.Person;


public class DateComparator implements Comparator<Person> {

	public int compare(Person p1, Person p2) {
		Date d1 = p1.getBirth();
		Date d2 = p2.getBirth();
		if (d1.before(d2)) {
			return -1;
		}
		else if (d2.before(d1)) {
			return 1;
		}
		return 0;
	}

}
