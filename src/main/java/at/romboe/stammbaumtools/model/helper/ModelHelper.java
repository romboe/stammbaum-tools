package at.romboe.stammbaumtools.model.helper;

import java.util.List;
import java.util.Optional;

import at.romboe.stammbaumtools.model.Constants;
import at.romboe.stammbaumtools.model.Person;

public class ModelHelper {

	public static Optional<Person> findPerson(List<Person> persons, int id) {
		if (Constants.UNDEFINED == id) {
			Optional.empty();
		}
		return persons.stream().filter(p -> p.getId() == id).findFirst();
	}
}
