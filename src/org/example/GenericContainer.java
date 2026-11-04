package org.example;

public class GenericContainer<T> {
    private T object;

    public void put(T object) {
        this.object = object;
    }

    public T get() {
        return object;
    }
}
