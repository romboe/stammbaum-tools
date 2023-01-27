package at.romboe.stammbaumtools.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;

import at.romboe.stammbaumtools.model.Person;
import at.romboe.stammbaumtools.model.helper.ModelHelper;

public class ParserTest {

	@Test
	public void testDates() {
		Parser p = new Parser("src/test/resources/testdata01.js");
		List<Person> persons = p.getPersons();
		Person leopold = ModelHelper.findPersonByUuid(persons, "101").get();
		assertEquals(LocalDate.of(1640, Month.JUNE, 9), leopold.getBirth().get());
	}
}
