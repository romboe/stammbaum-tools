package at.romboe.stammbaumtools.sortierer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import at.romboe.stammbaumtools.model.Person;
import at.romboe.stammbaumtools.parser.Parser;

public class Sortierer {

	public static final String ATTR_UUID = "uuid";
	public static final String ATTR_FIRSTNAME = "firstname";
	public static final String ATTR_LASTNAME = "lastname";
	public static final String ATTR_BIRTH = "birth";
	public static final String ATTR_DEATH = "death";
	public static final String ATTR_FATHER = "father";
	public static final String ATTR_MOTHER = "mother";
	public static final String ATTR_WIKI = "wiki";
	public static final String ATTR_IMG = "img";

	private static final int UNDEFINED = -1;
	private static final String UNDEFINED_STR = "undefined";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


	public static final void main(String[] args) {
		Parser p = new Parser("data.js");
		List<Person> ps = p.getPersons();
		ps.sort(new DateComparator());
		reassignIds(ps);
		try {
			print(ps);
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

	private static void print(List<Person> ps) throws IOException {
		GsonBuilder gsonoutbuilder = new GsonBuilder();
		Gson gsonout = gsonoutbuilder.registerTypeAdapter(Person.class, new JsonSerializer<Person>() {

			public JsonElement serialize(Person person, Type typeOfT, JsonSerializationContext context)
				throws JsonParseException {
				JsonObject o = new JsonObject();
				o.add("id", new JsonPrimitive(person.getId()));
				o.add("uuid", new JsonPrimitive(person.getUuid()));
				o.add(ATTR_FIRSTNAME, new JsonPrimitive(person.getFirstname()));
				o.add(ATTR_LASTNAME, new JsonPrimitive(person.getLastname()));
				addDate(o, ATTR_BIRTH, person.getBirth());
				addDate(o, ATTR_DEATH, person.getDeath());
				addParent(o, ATTR_MOTHER, person.getMother());
				addParent(o, ATTR_FATHER, person.getFather());
				if (StringUtils.isNotEmpty(person.getWiki())) {
					o.add(ATTR_WIKI, new JsonPrimitive(person.getWiki()));
				}
				return o;
			}

			private void addParent(JsonObject o, String name, Integer value) {
				o.add(name, value == UNDEFINED ? new JsonPrimitive(UNDEFINED_STR) : new JsonPrimitive(value.intValue()));
			}

			private void addDate(JsonObject o, String name, Optional<LocalDate> optionalDate) {
				String value = optionalDate.map(d -> d.format(DATE_FORMATTER)).orElse(StringUtils.EMPTY);
				o.add(name, new JsonPrimitive(value));
			}

		}).create();
		String out = gsonout.toJson(ps);
		out = replaceQuotesAndSetLineBreaks(out);
		PrintWriter pw = new PrintWriter(new File("output.js"));
		pw.print(out);
		pw.close();
	}

	private static String replaceQuotesAndSetLineBreaks(String str) {
		String[] fields = {"id", ATTR_FIRSTNAME, ATTR_LASTNAME, ATTR_BIRTH, ATTR_DEATH, ATTR_MOTHER, ATTR_UUID, ATTR_FATHER, ATTR_IMG, ATTR_WIKI, UNDEFINED_STR};
		for (String f:fields) {
			str = str.replace("\"" + f + "\"", f);
		}
		str = str.replace("\"", "'");
		str = str.replace("},", "},\n");
		return str;
	}
}
