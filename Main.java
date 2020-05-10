import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

	static int Size = 8;
	static String[] grammar_left = new String[1000];
	static String[] grammar_right = new String[1000];
	static String[] result = new String[2000];
	static int tempSize = Size;

	public static void main(String args[]) {

		// Populate the array with unlikely to be used strings

		for (int i = 0; i < grammar_left.length; i++) {
			grammar_left[i] = "190234";
			grammar_right[i] = "112390234";
		}

		// MY HARD-CODED GRAMMAR

		grammar_left[0] = "S";
		grammar_left[1] = "S";
		grammar_left[2] = "A";
		grammar_left[3] = "A";
		grammar_left[4] = "A";
		grammar_left[5] = "B";
		grammar_left[6] = "B";
		grammar_left[7] = "B";
		grammar_right[0] = "Aa";
		grammar_right[1] = "aB";
		grammar_right[2] = "BAa";
		grammar_right[3] = "B";
		grammar_right[4] = "a";
		grammar_right[5] = "AbB";
		grammar_right[6] = "BS";
		grammar_right[7] = "EPSILON";

		// START OF LOGIC

		while (epsilonCheck() == 1) {
			epsilonRemoval();
			Size = tempSize;
		}

		unitRemoval();
		unityLeftRight();

		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}

	}

	public static void epsilonRemoval() {

		for (int i = 0; i < Size; i++) {

			if (grammar_right[i].contentEquals("EPSILON")) {
				grammar_right[i] = "112390234"; // START POINT
				String epsilon_grammar_left = grammar_left[i];
				grammar_left[i] = "190234";
				for (int j = 0; j < Size; j++) {
					if (grammar_right[j].contains(epsilon_grammar_left)) {
						tempSize++;
						grammar_left[tempSize] = grammar_left[j];
						if (grammar_right[j].length() > 1) {
							grammar_right[tempSize] = grammar_right[j].replaceFirst(Pattern.quote(epsilon_grammar_left),
									"");
						} else if (grammar_right[j].length() == 1) {
							grammar_right[tempSize] = grammar_right[j].replaceFirst(Pattern.quote(epsilon_grammar_left),
									"EPSILON");
						}
					}
				}
			}
		}

	}

	public static void unitRemoval() {
		for (int i = 0; i < Size; i++) {
			if (grammar_right[i].length() == 1) {
				if (Character.isUpperCase(grammar_right[i].charAt(0))) {

					String rightUnit = grammar_right[i];
					String leftUnit = grammar_left[i];
					grammar_left[i] = "190234";
					grammar_right[i] = "112390234";
					for (int j = 0; j < Size; j++) {

						if (grammar_left[j].contentEquals(rightUnit)) {
							tempSize++;
							grammar_left[tempSize] = leftUnit;
							grammar_right[tempSize] = grammar_right[j];
						}
					}

				}

			}
		}
	}

	public static int epsilonCheck() {
		int found_epsilon = 0;
		for (int i = 0; i < Size; i++) {
			if (grammar_right[i].contentEquals("EPSILON")) {
				found_epsilon = 1;
			}

		}
		return found_epsilon;
	}

	public static void rename() {
		for (int i = 0; i < Size; i++) {
			
		}
	}
	
	public static void unityLeftRight() {
		for (int i = 0; i < grammar_left.length; i++) {
			result[i] = grammar_left[i] + " " + grammar_right[i];
		}

		result = Arrays.stream(result).distinct().toArray(String[]::new);
	}

}
