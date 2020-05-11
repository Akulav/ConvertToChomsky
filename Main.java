import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

	static int Size = 8;
	static String[] grammar_left = new String[1000];
	static String[] grammar_right = new String[1000];
	static String[] result = new String[2000];
	static int tempSize = Size;
	static int countRenames = 0;

	public static void main(String args[]) {

		// Populate the array with unlikely to be used strings

		for (int i = 0; i < grammar_left.length; i++) {
			grammar_left[i] = "190234";
			grammar_right[i] = "112390234";
		}

		// MY HARD-CODED GRAMMAR

//		  grammar_left[0] = "S"; grammar_left[1] = "S"; grammar_left[2] = "A";
//		  grammar_left[3] = "A"; grammar_left[4] = "A"; grammar_left[5] = "B";
//		  grammar_left[6] = "B"; grammar_left[7] = "B"; grammar_right[0] = "Aa";
//		  grammar_right[1] = "aB"; grammar_right[2] = "BAa"; grammar_right[3] = "B";
//		  grammar_right[4] = "a"; grammar_right[5] = "AbB"; grammar_right[6] = "BS";
//		  grammar_right[7] = "EPSILON";

		grammar_left[0] = "S";
		grammar_left[1] = "S";
		grammar_left[2] = "A";
		grammar_left[3] = "A";
		grammar_left[4] = "A";
		grammar_left[5] = "B";
		grammar_left[6] = "B";
		grammar_left[7] = "B";
		grammar_left[8] = "B";
		grammar_left[9] = "C";

		grammar_right[0] = "aB";
		grammar_right[1] = "A";
		grammar_right[2] = "d";
		grammar_right[3] = "dS";
		grammar_right[4] = "aBdAB";
		grammar_right[5] = "a";
		grammar_right[6] = "dA";
		grammar_right[7] = "A";
		grammar_right[8] = "EPSILON";
		grammar_right[9] = "Aa";

		// START OF LOGIC

		while (epsilonCheck() == 1) {
			epsilonRemoval();
			Size = tempSize;
		}

		unitRemoval();

		while (getRuleLength() > 2) {
			rename();
		}
		rename();

		unityLeftRight();

		for (int i = 0; i < result.length; i++) {
			if (!(result[i] == null)) {
				if (!(grammar_left[i] == "190234")) {
					System.out.println(result[i]);
				}
			}
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
		Size = tempSize;
		for (int i = 0; i < Size; i++) {
			if (grammar_right[i].length() > 1) {

				// FORM Xy / xY
				if (grammar_right[i].length() == 2) {
					if (Character.isUpperCase(grammar_right[i].charAt(0))
							&& Character.isLowerCase(grammar_right[i].charAt(1))) {
						tempSize++;
						String newSymbol = Character.toString(getUniqueSign());
						grammar_left[tempSize] = newSymbol;
						grammar_right[tempSize] = Character.toString(grammar_right[i].charAt(1));
						grammar_right[i] = grammar_right[i].charAt(0) + newSymbol;

					}

					if (Character.isUpperCase(grammar_right[i].charAt(1))
							&& Character.isLowerCase(grammar_right[i].charAt(0))) {
						tempSize++;
						String newSymbol = Character.toString(getUniqueSign());
						grammar_left[tempSize] = newSymbol;
						grammar_right[tempSize] = Character.toString(grammar_right[i].charAt(0));
						grammar_right[i] = newSymbol + grammar_right[i].charAt(1);

					}
				}

				// Anything longer than 2 will be cut until it is a 2 and will be dealt by the
				// if from above
				if (grammar_right[i].length() > 2 && grammar_right[i] != "112390234") {
					String substring = grammar_right[i].substring(1, grammar_right[i].length());
					String newSymbol = Character.toString(getUniqueSign());
					tempSize++;
					grammar_left[tempSize] = newSymbol;
					grammar_right[tempSize] = substring;
					grammar_right[i] = grammar_right[i].charAt(0) + newSymbol;
				}
			}
			Size = tempSize;
		}
	}

	public static char getUniqueSign() {
		Random r = new Random();
		char c = '0';
		int count = 0;
		while (c < 65) {
			c = (char) (r.nextInt(90));
			for (int i = 0; i < Size; i++) {
				for (int j = 0; j < grammar_right[i].length(); j++) {
					if (grammar_right[i].charAt(j) != c) {
						count++;
					}

				}
			}
			if (count == Size) {
				break;
			}
			count = 0;
		}
		return c;
	}

	public static int getRuleLength() {
		int count = 0;
		for (int i = 0; i < tempSize; i++) {
			if (grammar_right[i] != "112390234") {
				if (grammar_right[i].length() > count) {
					count = grammar_right[i].length();
				}
			}
		}

		return count;
	}

	public static void unityLeftRight() {

		for (int i = 0; i < grammar_left.length; i++) {

			result[i] = grammar_left[i] + " " + grammar_right[i];
		}

		result = Arrays.stream(result).distinct().toArray(String[]::new);
	}

}
