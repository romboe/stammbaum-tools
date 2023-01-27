package at.romboe.stammbaumtools.sortierer;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import at.romboe.stammbaumtools.model.Person;
import at.romboe.stammbaumtools.parser.Parser;

public class SortiererTest {

	@Test
	public void test() {
		Parser p = new Parser("src/test/resources/unsorted.js");
		List<Person> ps = p.getPersons();
		ps.sort(new DateComparator());
		assertEquals(ps.get(0).getUuid(), "1");
		assertEquals(ps.get(1).getUuid(), "2");
		assertEquals(ps.get(2).getUuid(), "3");
		assertEquals(ps.get(3).getUuid(), "4");
		assertEquals(ps.get(4).getUuid(), "5");
		assertEquals(ps.get(5).getUuid(), "6");
	}
}
