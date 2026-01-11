package com.example;

public class ReverseWords {

    public static String reverseEachWord(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        StringBuilder word = new StringBuilder();

        for (char ch : input.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                word.append(ch);
            } else {
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
        runTest("Hello Applicantz", "olleH ztnacilppA");
        runTest("Java-8", "avaJ-8");
        runTest("abc123 def456", "321cba 654fed");

    }
}
