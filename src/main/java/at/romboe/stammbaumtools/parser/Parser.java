package at.romboe.stammbaumtools.parser;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.google.gson.JsonParseException;

import at.romboe.stammbaumtools.model.Constants;
import at.romboe.stammbaumtools.model.Person;
import at.romboe.stammbaumtools.model.Root;

public class Parser {

	public static final String ATTR_UUID = "uuid";
	public static final String ATTR_FIRSTNAME = "firstname";
	public static final String ATTR_LASTNAME = "lastname";
	public static final String ATTR_BIRTH = "birth";
	public static final String ATTR_DEATH = "death";
	public static final String ATTR_FATHER = "father";
	public static final String ATTR_MOTHER = "mother";
	public static final String ATTR_WIKI = "wiki";
	public static final String ATTR_IMG = "img";
	private static final String UNDEFINED_STR = "undefined";
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final int UNDEFINED_YEAR_SET = 1500;
	private String fileName;

	public Parser(String fileName) {
		this.fileName = fileName;
	}

	public List<Person> getPersons() {
		List<Person> persons = new ArrayList<>();
		try {
			FileReader reader = new FileReader(this.fileName);
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
					return Constants.UNDEFINED;
				}
			});
			Gson gson = builder.create();

			Root data = gson.fromJson(reader, Root.class);
			persons = data.getPeople();
//			Arrays.sort(ps, new DateComparator());
//			reassignIds(ps);
//			print(ps);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return persons;
	}
}
