package at.romboe.stammbaumtools.gedcom;

import static at.romboe.stammbaumtools.gedcom.Printer.printToFile;

import java.io.IOException;

import at.romboe.stammbaumtools.parser.Parser;

public class Main {

	public static void main(String[] args) throws IOException {
		Parser parser = new Parser("data.js");
		Converter converter = new Converter();
		Gedcom gedcom = converter.convertToGedcom(parser.getPersons());
		printToFile(gedcom, "output.ged");

//		TreeParser treeParser = new TreeParser();
//		try {
//			List<GedcomTag> tags = treeParser.parseGedcom(new File("sample01.ged"));
//			Root root = converter.convertToStammbaumModel(tags);
//			at.romboe.stammbaumtools.Printer.printToConsole(root.getPeople());
//		} catch (SAXParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
}
