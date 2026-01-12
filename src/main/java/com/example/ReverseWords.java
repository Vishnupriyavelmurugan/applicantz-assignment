package com.example;

public class ReverseWords {

    /**
     * Reverses each word in the given input string while keeping the order of
     * words and non-alphanumeric characters unchanged.
     *
     * @param input the input string to be processed
     * @return a new string where each word is reversed; returns the same input
     * if it is null or empty
     */

    public static String reverseEachWord(String input) {
        // Handle edge cases where input is null or empty
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        StringBuilder word = new StringBuilder();

        for (char ch : input.toCharArray()) {
            // Check if the character is part of a word (letter or digit)
            if (Character.isLetterOrDigit(ch)) {
                word.append(ch);
            } else {
                /*
                 * Non-alphanumeric character encountered.
                 * Reverse the collected word and append it to the result,
                 * then reset the word buffer and append the special character as-is.
                 */
                result.append(word.reverse());
                word.setLength(0);
                result.append(ch);
            }
        }

        result.append(word.reverse());
        return result.toString();
    }

    private static void runTest(String input, String expected) {
        String actual = reverseEachWord(input);
        // Compare actual output with expected output and print result
        if (expected.equals(actual)) {
            System.out.println("PASS | Input: \"" + input + "\"");
        } else {
            System.err.println(
                    "FAIL | Input: \"" + input + "\" | Expected: \"" +
                            expected + "\" but got: \"" + actual + "\""
            );
        }
    }

    public static void main(String[] args) {
        runTest("String", "gnirtS");
        runTest("12345", "54321");
        runTest("Hello Applicantz!", "olleH ztnacilppA!");
        runTest("Java-8", "avaJ-8");
        runTest("abc123 def456", "321cba 654fed");

    }
}
