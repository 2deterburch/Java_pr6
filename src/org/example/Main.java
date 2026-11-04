
package org.example;

import java.time.LocalDate;
import java.util.*;

public class Main {

    private static final int SIZE = 100_000;
    private static final int OPERATIONS = 1_000;

    // Узагальнений метод для списку будь-якого типу
    public static <T> void printList(List<T> list) {
        for (T item : list) {
            System.out.println(item);
        }
    }

    // Метод приймає List<Integer> і List<Number>
    public static void printNumbers(List<? extends Number> list) {
        for (Number number : list) {
            System.out.println(number);
        }
    }

    // Метод для демонстрації інваріантності типів
    public static void strictNumbers(List<Number> list) {
        System.out.println(list);
    }

    // Створення банківської транзакції
    public static BankTransaction createTransaction(int id) {
        return new BankTransaction(
                id,
                id * 10.5,
                LocalDate.now()
        );
    }

    // Завдання 1. Generics
    public static void testGenerics() {

        System.out.println("\n===== TASK 1: GENERICS =====");

        GenericContainer<BankTransaction> box =
                new GenericContainer<>();

        box.put(createTransaction(1));

        System.out.println("Container: " + box.get());

        // Помилка компіляції:
        // box.put("abc");

        // Перевірка приведення типу Object
        GenericContainer<Object> objectBox =
                new GenericContainer<>();

        objectBox.put("abc");

        try {
            BankTransaction transaction =
                    (BankTransaction) objectBox.get();

        } catch (ClassCastException e) {
            System.out.println(
                    "ClassCastException: " + e.getMessage()
            );

            e.printStackTrace();
        }

        // Перевірка узагальненого методу
        List<String> strings =
                Arrays.asList("Java", "Generics");

        printList(strings);

        List<Integer> integers =
                Arrays.asList(1, 2, 3);

        List<Number> numbers =
                Arrays.asList(1.5, 2.5, 3);

        // Помилка компіляції:
        // strictNumbers(integers);

        // Правильний варіант:
        System.out.println("Integer list:");
        printNumbers(integers);

        System.out.println("Number list:");
        printNumbers(numbers);
    }

    // Завдання 2. Продуктивність колекцій
    public static void testCollections() {

        System.out.println("\n===== TASK 2: COLLECTIONS =====");

        List<BankTransaction> arrayList =
                new ArrayList<>();

        List<BankTransaction> linkedList =
                new LinkedList<>();

        Set<BankTransaction> hashSet =
                new HashSet<>();

        // Заповнення колекцій 100 000 об'єктами
        for (int i = 0; i < SIZE; i++) {
            BankTransaction transaction =
                    createTransaction(i);

            arrayList.add(transaction);
            linkedList.add(transaction);
            hashSet.add(transaction);
        }

        // Вставка 1000 елементів у середину ArrayList
        long start = System.nanoTime();

        for (int i = 0; i < OPERATIONS; i++) {
            arrayList.add(
                    arrayList.size() / 2,
                    createTransaction(SIZE + i)
            );
        }

        long arrayInsert = System.nanoTime() - start;

        // Вставка 1000 елементів у середину LinkedList
        start = System.nanoTime();

        for (int i = 0; i < OPERATIONS; i++) {
            linkedList.add(
                    linkedList.size() / 2,
                    createTransaction(SIZE + i)
            );
        }

        long linkedInsert = System.nanoTime() - start;

        // Додавання 1000 елементів у HashSet
        start = System.nanoTime();

        for (int i = 0; i < OPERATIONS; i++) {
            hashSet.add(createTransaction(SIZE + i));
        }

        long hashInsert = System.nanoTime() - start;

        // Перевірка contains()
        BankTransaction search =
                createTransaction(50_000);

        start = System.nanoTime();

        for (int i = 0; i < OPERATIONS; i++) {
            arrayList.contains(search);
        }

        long arrayContains = System.nanoTime() - start;

        start = System.nanoTime();

        for (int i = 0; i < OPERATIONS; i++) {
            linkedList.contains(search);
        }

        long linkedContains = System.nanoTime() - start;

        start = System.nanoTime();

        for (int i = 0; i < OPERATIONS; i++) {
            hashSet.contains(search);
        }

        long hashContains = System.nanoTime() - start;

        // Виведення результатів
        System.out.println("ArrayList insert: "
                + arrayInsert / 1_000_000.0 + " ms");

        System.out.println("LinkedList insert: "
                + linkedInsert / 1_000_000.0 + " ms");

        System.out.println("HashSet add: "
                + hashInsert / 1_000_000.0 + " ms");

        System.out.println("ArrayList contains: "
                + arrayContains / 1_000_000.0 + " ms");

        System.out.println("LinkedList contains: "
                + linkedContains / 1_000_000.0 + " ms");

        System.out.println("HashSet contains: "
                + hashContains / 1_000_000.0 + " ms");

        // Видалення через for-each
        System.out.println("\nRemoval using for-each:");

        List<BankTransaction> testList =
                new ArrayList<>(arrayList);

        try {
            for (BankTransaction transaction : testList) {
                if (transaction.getId() % 10_000 == 0) {
                    testList.remove(transaction);
                }
            }

        } catch (ConcurrentModificationException e) {
            System.out.println(
                    "ConcurrentModificationException occurred!"
            );

            e.printStackTrace();
        }

        // Правильне видалення через Iterator
        System.out.println("\nRemoval using Iterator:");

        Iterator<BankTransaction> iterator =
                arrayList.iterator();

        int removed = 0;

        while (iterator.hasNext()) {
            BankTransaction transaction = iterator.next();

            if (transaction.getId() % 10_000 == 0) {
                iterator.remove();
                removed++;
            }
        }

        System.out.println("Removed elements: " + removed);
    }

    // Клас без equals() і hashCode()
    static class DefaultTransaction {
        int id;

        DefaultTransaction(int id) {
            this.id = id;
        }
    }

    // Клас тільки з equals()
    static class EqualsOnlyTransaction {
        int id;

        EqualsOnlyTransaction(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;

            if (!(obj instanceof EqualsOnlyTransaction)) {
                return false;
            }

            EqualsOnlyTransaction other =
                    (EqualsOnlyTransaction) obj;

            return id == other.id;
        }
    }

    // Завдання 3. equals() та hashCode()
    public static void testHashSet() {

        System.out.println("\n===== TASK 3: HASHSET =====");

        // Варіант 1: без equals() і hashCode()
        Set<DefaultTransaction> set1 =
                new HashSet<>();

        set1.add(new DefaultTransaction(1));
        set1.add(new DefaultTransaction(1));

        System.out.println(
                "Without equals/hashCode: " + set1.size()
        );

        System.out.println(
                "Contains: " +
                        set1.contains(new DefaultTransaction(1))
        );

        // Варіант 2: тільки equals()
        Set<EqualsOnlyTransaction> set2 =
                new HashSet<>();

        set2.add(new EqualsOnlyTransaction(1));
        set2.add(new EqualsOnlyTransaction(1));

        System.out.println(
                "Only equals: " + set2.size()
        );

        System.out.println(
                "Contains: " +
                        set2.contains(new EqualsOnlyTransaction(1))
        );

        // Варіант 3: equals() та hashCode()
        Set<BankTransaction> set3 =
                new HashSet<>();

        set3.add(createTransaction(1));
        set3.add(createTransaction(1));

        System.out.println(
                "Equals and hashCode: " + set3.size()
        );

        System.out.println(
                "Contains: " +
                        set3.contains(createTransaction(1))
        );

        // Зміна ключа після додавання до HashSet
        BankTransaction transaction =
                createTransaction(100);

        Set<BankTransaction> set4 =
                new HashSet<>();

        set4.add(transaction);

        System.out.println(
                "Before changing id: "
                        + set4.contains(transaction)
        );

        transaction.setId(200);

        System.out.println(
                "After changing id: "
                        + set4.contains(transaction)
        );
    }

    public static void main(String[] args) {

        testGenerics();

        testCollections();

        testHashSet();
    }
}
