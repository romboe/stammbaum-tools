package at.romboe.stammbaumtools.parser;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import at.romboe.stammbaumtools.model.Person;
import at.romboe.stammbaumtools.model.Root;
import at.romboe.stammbaumtools.model.helper.Constants;

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
			builder.registerTypeAdapter(Optional.class, new JsonDeserializer<Optional<LocalDate>>() {
				public Optional<LocalDate> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
						throws JsonParseException {
					String str = json.getAsString();
					try {
						if (StringUtils.isNotEmpty(str) && !str.equals(UNDEFINED_STR) && !str.equals("0000-00-00")) {
							return Optional.of(LocalDate.parse(str));
						}
					}
					catch(DateTimeParseException e) {
						e.printStackTrace();
					}
					return Optional.empty();
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
