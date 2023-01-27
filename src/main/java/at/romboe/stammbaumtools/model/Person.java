package at.romboe.stammbaumtools.model;

import java.time.LocalDate;
import java.util.Optional;

import lombok.Data;

@Data
public class Person {
	private int id;
	private String uuid;
	private String firstname;
	private String lastname;
	private Optional<LocalDate> birth;
	private Optional<LocalDate> death;
	private Integer father;
	private Integer mother;
	private String wiki;
	private String img;
}
