

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

    // ==========================================
    // TASK 1. GENERICS
    // ==========================================

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

    // ==========================================
    // TASK 2. COLLECTIONS
    // ==========================================

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

    // ==========================================
    // TASK 3. EQUALS() AND HASHCODE()
    // ==========================================

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

    // ==========================================
    // PRACTICAL WORK 7
    // FUNCTIONAL STYLE IN JAVA
    // ==========================================

    public static void testFunctionalStyle() {

        System.out.println("\n==================================");
        System.out.println("PRACTICAL WORK 7");
        System.out.println("FUNCTIONAL STYLE IN JAVA");
        System.out.println("==================================");

        // Створення тестового списку транзакцій
        List<BankTransaction> transactions = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            transactions.add(createTransaction(i * 100));
        }

        // ==========================================
        // TASK 1. SORTING
        // ==========================================

        System.out.println("\n===== TASK 1: SORTING =====");

        // 1. Анонімний клас Comparator
        List<BankTransaction> sort1 =
                new ArrayList<>(transactions);

        sort1.sort(new Comparator<BankTransaction>() {
            @Override
            public int compare(
                    BankTransaction t1,
                    BankTransaction t2
            ) {
                return Double.compare(
                        t1.getAmount(),
                        t2.getAmount()
                );
            }
        });

        System.out.println("\nSorting using anonymous Comparator:");
        printList(sort1);

        // 2. Лямбда-вираз
        List<BankTransaction> sort2 =
                new ArrayList<>(transactions);

        sort2.sort((t1, t2) ->
                Double.compare(t1.getAmount(), t2.getAmount())
        );

        System.out.println("\nSorting using lambda expression:");
        printList(sort2);

        // 3. Comparator.comparing
        List<BankTransaction> sort3 =
                new ArrayList<>(transactions);

        sort3.sort(
                Comparator.comparingDouble(
                        BankTransaction::getAmount
                )
        );

        System.out.println("\nSorting using Comparator.comparing:");
        printList(sort3);

        // ==========================================
        // TASK 2. STREAM API
        // ==========================================

        System.out.println("\n===== TASK 2: STREAM API =====");

        // Створення великого списку
        List<BankTransaction> largeList =
                new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            largeList.add(createTransaction(i));
        }

        // Обробка через звичайний цикл for-each
        double sumForEach = 0;

        for (BankTransaction transaction : largeList) {
            if (transaction.getAmount() > 5000) {
                sumForEach += transaction.getAmount();
            }
        }

        System.out.println(
                "Sum using for-each: " + sumForEach
        );

        // Обробка через Stream API
        double sumStream = largeList.stream()
                .filter(t -> t.getAmount() > 5000)
                .mapToDouble(BankTransaction::getAmount)
                .sum();

        System.out.println(
                "Sum using Stream API: " + sumStream
        );

        System.out.println(
                "Results are equal: " +
                        (Double.compare(sumForEach, sumStream) == 0)
        );

        // ==========================================
        // TASK 3. OPTIONAL
        // ==========================================

        System.out.println("\n===== TASK 3: OPTIONAL =====");

        // Пошук через звичайний цикл
        BankTransaction loopResult = null;

        for (BankTransaction transaction : largeList) {
            if (transaction.getAmount() > 10_000_000) {
                loopResult = transaction;
                break;
            }
        }

        System.out.println(
                "Search using loop: " +
                        (loopResult == null ? "Not found" : loopResult)
        );

        // Пошук через Stream API та Optional
        Optional<BankTransaction> result = largeList.stream()
                .filter(t -> t.getAmount() > 10_000_000)
                .findFirst();

        System.out.println(
                "Search using Optional: " + result
        );

        // Перевірка get()
        try {
            BankTransaction found = result.get();

            System.out.println(
                    "Found transaction: " + found
            );

        } catch (NoSuchElementException e) {
            System.out.println(
                    "get() exception: " + e
            );
        }

        // Перевірка orElse()
        BankTransaction defaultTransaction =
                new BankTransaction(
                        -1,
                        0,
                        LocalDate.now()
                );

        BankTransaction safeResult =
                result.orElse(defaultTransaction);

        System.out.println(
                "Result using orElse(): " + safeResult
        );
    }

    // ==========================================
    // MAIN
    // ==========================================

    public static void main(String[] args) {

        testGenerics();

        testCollections();

        testHashSet();

        testFunctionalStyle();
    }
}
