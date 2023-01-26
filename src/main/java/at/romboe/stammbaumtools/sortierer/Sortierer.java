package at.romboe.stammbaumtools.sortierer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import at.romboe.stammbaumtools.model.Person;
import at.romboe.stammbaumtools.model.Root;

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
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final int UNDEFINED_YEAR_SET = 1500;


	public static final void main(String[] args) {
		try {
			FileReader reader = new FileReader("data.js");
			GsonBuilder builder = new GsonBuilder();
			// handles deserialization of Date objects
			builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
						throws JsonParseException {
					String str = json.getAsString();
					try {
						if (StringUtils.isNotEmpty(str) && !str.equals(UNDEFINED_STR) && !str.equals("0000-00-00")) {
							return DATE_FORMAT.parse(str);
						}
					}
					catch(ParseException e) {
						e.printStackTrace();
					}
					Calendar c = new GregorianCalendar();
					c.set(Calendar.YEAR, UNDEFINED_YEAR_SET);
					c.set(Calendar.MONTH, Calendar.JANUARY);
					c.set(Calendar.DAY_OF_MONTH, 1);
					return c.getTime();
				}

			});
			// handles deserialization of Integer objects
			builder.registerTypeAdapter(Integer.class, new JsonDeserializer<Integer>() {
				public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
						throws JsonParseException {
					try {
						// Could be undefined = JS's 'undefined'
						return json.getAsInt();
					}
					catch(Exception e) {
					}
					return UNDEFINED;
				}
			});
			Gson gson = builder.create();

			Root data = gson.fromJson(reader, Root.class);
			List<Person> ps = data.getPeople();
//			Arrays.sort(ps, new DateComparator());
//			reassignIds(ps);
//			print(ps);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static void reassignIds(Person[] ps) {
		// Avoid reassignment of previously assigned ids
		int offset = 10000;
		for (int i=0; i<ps.length; i++) {
			Person p = ps[i];
			int id = p.getId();
			int newId = offset + i;
			// System.out.format("alt: %3d, neu: %3d, %s %s, mutter:%3d\n", id, newId, p.getLastname(), p.getFirstname(), p.getMother());
			p.setId(newId);
			for (int j=0; j<ps.length; j++) {
				Person p2 = ps[j];
				if (p2.getFather() == id) {
					p2.setFather(p.getId());
				}
				else if (p2.getMother() == id) {
					p2.setMother(p.getId());
				}
			}
		}

		for (int i=0; i<ps.length; i++) {
			Person p = ps[i];
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

	private static void print(Person[] ps) throws IOException {
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

			private void addDate(JsonObject o, String name, Date value) {
				Calendar c = new GregorianCalendar();
				c.setTime(value);
				if (c.get(Calendar.YEAR) == UNDEFINED_YEAR_SET) {
					o.add(name, new JsonPrimitive(StringUtils.EMPTY));
				}
				else {
					o.add(name, new JsonPrimitive(DATE_FORMAT.format(value)));
				}
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
