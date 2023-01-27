package at.romboe.stammbaumtools.gedcom;

import at.romboe.stammbaumtools.gedcom.model.Fam;
import at.romboe.stammbaumtools.gedcom.model.Indi;

public class Printer {

	private static final String LINE_BREAK = System.getProperty("line.separator");

	public static String print(Gedcom gedcom) {
		StringBuilder sb = new StringBuilder();
		for (Indi indi:gedcom.getIndis()) {
			Printer.printIndi(indi, sb);
		}
		for (Fam fam:gedcom.getFams()) {
			Printer.printFam(fam, sb);
		}
		return sb.toString();
	}


	public static void printIndi(Indi indi, StringBuilder sb) {
		sb.append("0 ").append(printId(indi.getId())).append(" INDI").append(LINE_BREAK);
		sb.append("1 NAME ").append(indi.getName()).append(LINE_BREAK);
		sb.append("1 SURN ").append(indi.getSurn()).append(LINE_BREAK);
		sb.append("1 GIVN ").append(indi.getGivn()).append(LINE_BREAK);
		sb.append("1 BIRT ").append(LINE_BREAK);
		sb.append("2 DATE ").append(indi.getBirt()).append(LINE_BREAK);
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
