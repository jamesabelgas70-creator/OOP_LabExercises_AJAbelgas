package Exer1_Abelgas;

import java.util.Arrays;
import java.util.List;

public class Exer1_Imperative {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(2, 4, 6, 8, 10);

        int sum = 0;
        for (int i = 0; i < numbers.size(); i++) {
            sum += numbers.get(i);
        }

        System.out.println("The sum is: " + sum);
    }
}