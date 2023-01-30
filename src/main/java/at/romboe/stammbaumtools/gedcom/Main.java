package at.romboe.stammbaumtools.gedcom;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.folg.gedcom.model.GedcomTag;
import org.folg.gedcom.parser.TreeParser;
import org.xml.sax.SAXParseException;

import at.romboe.stammbaumtools.model.Root;

public class Main {

	public static void main(String[] args) {
//		Parser parser = new Parser("data.js");
		Converter converter = new Converter();
//		Gedcom gedcom = converter.convert(parser.getPersons());
//		System.out.println(Printer.print(gedcom));

		TreeParser treeParser = new TreeParser();
		try {
			List<GedcomTag> tags = treeParser.parseGedcom(new File("sample01.ged"));
			Root root = converter.convertToStammbaumModel(tags);
			at.romboe.stammbaumtools.Printer.printToConsole(root.getPeople());
		} catch (SAXParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
