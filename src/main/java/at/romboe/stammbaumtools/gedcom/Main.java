package at.romboe.stammbaumtools.gedcom;

import at.romboe.stammbaumtools.parser.Parser;

public class Main {

	public static void main(String[] args) {
		Parser parser = new Parser("data.js");
		Converter converter = new Converter();
		Gedcom gedcom = converter.convert(parser.getPersons());
		System.out.println(Printer.print(gedcom));
	}
}
