//MADE BY CATALIN
//
//NO ABSTRACT STRUCTURES WERE HURT, AND NO COMPLEX ALREADY EXISTING METHODS WERE USED, OTHER THAN SIMPLE STRING MANIPULATIONS
//
//EACH RUN THE GRAMMAR WILL BE DIFFERENT!!! ALL GENERATED GRAMMAR IS GUARANTEED TO BE IDENTICAL IN TERMS OF FUNCTIONALITY
//THIS IS CAUSED BY A RANDOM FACTOR USED WHILE COMPUTING CNF.
//
//THIS PROGRAM SHOULD WORK WITH ANY CONTEXT-FREE GRAMMAR
//
//THE GENERATED CFG IS NOT MINIMAL. THIS WAS NOT REQUIRED!!!






import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

public class Main {

	//Only the Size variable should be modified according to the entered grammar
	static int Size = 10;
	static String[] grammar_left = new String[1000];
	static String[] grammar_right = new String[1000];
	static String[] result = new String[2000];
	static int tempSize = Size;
	static int countRenames = 0;

	public static void main(String args[]) {

		// Populate the array with unlikely to be used strings. De ce? ca sa tin memorie plina acolo cu ceva cunoscut ca sa nu fac
		// shift la tot array cind vreau sa scot ceva. Prosta o sa le ignor si ma duc la urmatoru index.

		for (int i = 0; i < grammar_left.length; i++) {
			grammar_left[i] = "190234";
			grammar_right[i] = "112390234";
		}

		// MY HARD-CODED GRAMMAR
//CHANGE Size to 8 if you want to check this grammar. THIS GRAMMAR IS AN EXAMPLE FROM CLASS, AND IS MUCH SIMPLER.
//		grammar_left[0] = "S"; grammar_left[1] = "S"; grammar_left[2] = "A";
//		grammar_left[3] = "A"; grammar_left[4] = "A"; grammar_left[5] = "B";
//		grammar_left[6] = "B"; grammar_left[7] = "B"; grammar_right[0] = "Aa";
//		grammar_right[1] = "aB"; grammar_right[2] = "BAa"; grammar_right[3] = "B";
//		grammar_right[4] = "a"; grammar_right[5] = "AbB"; grammar_right[6] = "BS";
//		grammar_right[7] = "EPSILON";

      
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

		// Remove Epsilon is a method that does only one pass. Call it as long as there are more epsilons found.

		while (epsilonCheck() == 1) {
			epsilonRemoval();
			Size = tempSize;
		}
		
		//remove the units, duh

		unitRemoval();

		
		//as long as a rule is longer than 2, it is not chomsky. Rename until it is done.

		while (getRuleLength() > 2) {
			//Repeat until length 2 on the right side
			rename();
		}
		
		//Now it is 100% length 2, but 1 more pass needed in case there is a lower case.
		rename();
				
		
		//Method for unifying the left and right sides for easier printing

		unityLeftRight();
		for (int i = 0; i < result.length; i++) {
			if (!(result[i] == null)) {
				if (!(grammar_left[i] == "190234")) {
					System.out.println(result[i]);
				}
			}
		}

	}
	
	//Epsilon Removal function. If it is epsilon, remove the the left side from all other right sides one by one.

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
	
	//If right side is UPPERCASE, it is not chomsky. remove it until there is no more.

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
			Size = tempSize;
		}
	}
	
	//checks if there are any epsilons around. Present just in case an epsilon appears from removing another epsilon. It is possible, but not for my case.....

	public static int epsilonCheck() {
		int found_epsilon = 0;
		for (int i = 0; i < Size; i++) {
			if (grammar_right[i].contentEquals("EPSILON")) {
				found_epsilon = 1;
			}

		}
		return found_epsilon;
	}

	//Renames. Works Simple. If any right side is longer than 2 then rename, replace, truncate until it is length 2.
	//If it is length 2 then replace just the lower case so that it will be the form XY. EASY
	
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

		}
	}
	
	//Renames must have new symbols, not already present in the language. Generate a random one and make sure it is not used. This causes each run the language to be different.

	public static char getUniqueSign() {
		Random r = new Random();
		char c = '0';
		int count = 0;
		while (c < 66) {
			c = (char) (r.nextInt(89));
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
	
	//Checks the length of a right side rule. It knows us if one more call of the rename function must be done.

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

	//Unifies the two sides into one array for ease of printing.
	
	public static void unityLeftRight() {

		for (int i = 0; i < grammar_left.length; i++) {

			result[i] = grammar_left[i] + " " + grammar_right[i];
		}

		result = Arrays.stream(result).distinct().toArray(String[]::new);
	}

}
