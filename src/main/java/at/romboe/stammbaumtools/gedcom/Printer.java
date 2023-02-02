package at.romboe.stammbaumtools.gedcom;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import at.romboe.stammbaumtools.gedcom.model.Fam;
import at.romboe.stammbaumtools.gedcom.model.Indi;
import at.romboe.stammbaumtools.gedcom.model.helper.Constants;

public class Printer {

	private static final String LINE_BREAK = System.getProperty("line.separator");

	
	public static void printToFile(Gedcom gedcom, String fileName) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			writer.write(print(gedcom));
		}	
	}
	
	public static String print(Gedcom gedcom) {
		StringBuilder sb = new StringBuilder();
		sb.append("0 HEAD").append(LINE_BREAK);
		sb.append("1 GEDC").append(LINE_BREAK);
		sb.append("2 VERS 5.5.5").append(LINE_BREAK);
		sb.append("2 FORM LINEAGE-LINKED").append(LINE_BREAK);
		sb.append("3 VERS 5.5.5").append(LINE_BREAK);
		sb.append("1 CHAR UTF-8").append(LINE_BREAK);
		sb.append("1 SOUR gedcom.org").append(LINE_BREAK);
		sb.append("0 @U@ SUBM").append(LINE_BREAK);
		sb.append("1 NAME gedcom.org").append(LINE_BREAK);
		for (Indi indi:gedcom.getIndis()) {
			Printer.printIndi(indi, sb);
		}
		for (Fam fam:gedcom.getFams()) {
			Printer.printFam(fam, sb);
		}
		sb.append("0 TRLR");
		return sb.toString();
	}

	public static void printIndi(Indi indi, StringBuilder sb) {
		sb.append("0 ").append(printId(indi.getId())).append(" ").append(Constants.INDI).append(LINE_BREAK);
		sb.append("1 NAME ").append(indi.getName()).append(LINE_BREAK);
		sb.append("2 SURN ").append(indi.getSurn()).append(LINE_BREAK);
		sb.append("2 GIVN ").append(indi.getGivn()).append(LINE_BREAK);
		indi.getBirt().ifPresent(birth -> {
			sb.append("1 BIRT ").append(LINE_BREAK);
			sb.append("2 DATE ").append(birth).append(LINE_BREAK);
		});
		indi.getDeat().ifPresent(death -> {
			sb.append("1 DEAT ").append(LINE_BREAK);
			sb.append("2 DATE ").append(death).append(LINE_BREAK);
		});
		for (String id:indi.getFamc()) {
			sb.append("1 FAMC ").append(printId(id)).append(LINE_BREAK);
		}
		for (String id:indi.getFams()) {
			sb.append("1 FAMS ").append(printId(id)).append(LINE_BREAK);
		}
	}

	public static void printFam(Fam fam, StringBuilder sb) {
		sb.append("0 ").append(printId(fam.getId())).append(" FAM").append(LINE_BREAK);
		fam.getHusb().ifPresent(id -> sb.append("1 HUSB ").append(printId(id)).append(LINE_BREAK));
		fam.getWife().ifPresent(id -> sb.append("1 WIFE ").append(printId(id)).append(LINE_BREAK));
		for (String id:fam.getChildren()) {
			sb.append("1 CHIL ").append(printId(id)).append(LINE_BREAK);
		}
	}

	private static String printId(String id) {
		StringBuilder sb = new StringBuilder();
		sb.append('@').append(id).append('@');
		return sb.toString();
	}
}
