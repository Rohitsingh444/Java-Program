import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    private int count = 0;

    public static void main(String [] args) {
        StringCalculator stringCalculator = new StringCalculator();
        System.out.println(stringCalculator.add(""));
        System.out.println(stringCalculator.add("1"));
        System.out.println(stringCalculator.add("1,2,3"));
        System.out.println(stringCalculator.add("1\n2,3"));
        System.out.println(stringCalculator.add("//;\n1;2"));
        System.out.println(stringCalculator.add("2,1001"));
        System.out.println(stringCalculator.add("//[***]\n1***2***3"));
        System.out.println(stringCalculator.add("//[*][%]\n1*2%3"));
        System.out.println(stringCalculator.add("//[**][%%]\n1**2%%3"));
        System.out.println("Add() method is call "+stringCalculator.getCalledCount()+" times");

    }

    private int getCalledCount() {
        return count;
    }

    private int add(String numbers) {
        count++;
        List<Integer> negativeNum = new ArrayList<>();
        int sum = 0;
        if (numbers.isEmpty()) {
            return 0;
        } else {
            List<String> numList = checkDelimiter(numbers);

            for(String n : numList) {
                int number = Integer.parseInt(n);
                if(number < 0 ) {
                    negativeNum.add(number);
                } else if (number < 1001) {
                    sum += number;
                }
            }
        }

        if(!negativeNum.isEmpty()) {
            throw new IllegalArgumentException("negatives not allowed: " + negativeNum);
        }
       return sum;
    }

    private List<String> checkDelimiter(String numbers) {
        List<String> delimiters = new ArrayList<>(List.of(",", "\n"));
        String numString = numbers;

        if (numbers.startsWith("//")) {
            int delimiterEnd = numbers.indexOf("\n");

            String delimiterPart = numbers.substring(2, delimiterEnd);
            numString = numbers.substring(delimiterEnd + 1);

            if (delimiterPart.startsWith("[")) {
                Matcher m = Pattern.compile("\\[(.+?)\\]").matcher(delimiterPart);
                while (m.find()) {
                    delimiters.add(m.group(1));
                }
            } else {
                delimiters.add(delimiterPart);
            }
        }

        String delimiterRegex = delimiters.stream()
                .map(Pattern::quote)
                .reduce((a, b) -> a + "|" + b)
                .orElse(",");
        return Arrays.stream(numString.split(delimiterRegex)).toList();
    }
}
