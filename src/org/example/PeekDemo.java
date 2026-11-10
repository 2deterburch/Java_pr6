
package org.example;

import java.util.Arrays;
import java.util.List;

public class PeekDemo {

    public static void main(String[] args) {

        System.out.println("===== STREAM API: PEEK =====");

        List<String> transactions = Arrays.asList(
                "Transaction 1",
                "Transaction 2",
                "Transaction 3"
        );

        // 1. peek() без термінальної операції
        System.out.println("\nWithout terminal operation:");

        transactions.stream()
                .peek(System.out::println);

        System.out.println("Stream was not executed!");

        // 2. peek() з термінальною операцією
        System.out.println("\nWith terminal operation:");

        transactions.stream()
                .peek(t -> System.out.println("Processing: " + t))
                .forEach(t -> {});

        System.out.println("Stream execution completed!");
    }
}
