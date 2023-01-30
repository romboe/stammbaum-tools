package at.romboe.stammbaumtools;

import static at.romboe.stammbaumtools.model.helper.Constants.UNDEFINED;

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

public class Printer {

	public static final String UNDEFINED_STR = "undefined";
	private static final String ATTR_UUID = "uuid";
	private static final String ATTR_FIRSTNAME = "firstname";
	private static final String ATTR_LASTNAME = "lastname";
	private static final String ATTR_BIRTH = "birth";
	private static final String ATTR_DEATH = "death";
	private static final String ATTR_FATHER = "father";
	private static final String ATTR_MOTHER = "mother";
	private static final String ATTR_WIKI = "wiki";
	private static final String ATTR_IMG = "img";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


	public static void printToFile(List<Person> ps) throws IOException {
		PrintWriter pw = new PrintWriter(new File("output.js"));
		pw.print(print(ps));
		pw.close();
	}

	public static void printToConsole(List<Person> ps) throws IOException {
		System.out.println(ps);
	}

	private static String print(List<Person> ps) throws IOException {
		GsonBuilder gsonoutbuilder = new GsonBuilder();
		Gson gsonout = gsonoutbuilder.registerTypeAdapter(Person.class, new JsonSerializer<Person>() {

			public JsonElement serialize(Person person, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
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
		return out;
	}

	private static String replaceQuotesAndSetLineBreaks(String str) {
		String[] fields = { "id", ATTR_FIRSTNAME, ATTR_LASTNAME, ATTR_BIRTH, ATTR_DEATH, ATTR_MOTHER, ATTR_UUID, ATTR_FATHER, ATTR_IMG, ATTR_WIKI, UNDEFINED_STR };
		for (String f : fields) {
			str = str.replace("\"" + f + "\"", f);
		}
		str = str.replace("\"", "'");
		str = str.replace("},", "},\n");
		return str;
	}
}
