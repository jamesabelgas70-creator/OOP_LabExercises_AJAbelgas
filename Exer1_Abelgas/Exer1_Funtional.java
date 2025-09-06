package Exer1_Abelgas;

@FunctionalInterface
interface Addition {
    int add(int a, int b);
}

public class Exer1_Funtional {
    public static void main(String[] args) {
        Addition addition = (a, b) -> a + b;

        int result = addition.add(5, 7);
        System.out.println("The sum is: " + result);
    }
}