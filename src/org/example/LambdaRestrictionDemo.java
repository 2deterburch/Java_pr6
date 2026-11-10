
package org.example;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;
import java.util.List;

public class LambdaRestrictionDemo {

    public static void main(String[] args) {

        System.out.println("===== LAMBDA RESTRICTIONS =====");

        List<String> transactions = Arrays.asList(
                "Transaction 1",
                "Transaction 2",
                "Transaction 3"
        );

        // Демонстрація помилки компіляції:
        //
        //int localCounter = 0;
        //transactions.forEach(transaction -> {
        //    localCounter++;
        //});
        //
        // Помилка:
        // Local variables referenced from a lambda
        // expression must be final or effectively final

        System.out.println("\nCorrect solution using AtomicInteger:");

        AtomicInteger counter = new AtomicInteger(0);

        transactions.forEach(transaction -> {
            counter.incrementAndGet();
        });

        System.out.println("Transactions count: " + counter.get());
    }
}
