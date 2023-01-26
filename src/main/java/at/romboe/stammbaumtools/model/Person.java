package at.romboe.stammbaumtools.model;

import java.util.Date;

import lombok.Data;

@Data
public class Person {
	private int id;
	private String uuid;
	private String firstname;
	private String lastname;
	private Date birth;
	private Date death;
	private Integer father;
	private Integer mother;
	private String wiki;
	private String img;
}
