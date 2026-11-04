package org.example;

import java.time.LocalDate;
import java.util.Objects;

public class BankTransaction {
    private int id;
    private double amount;
    private LocalDate date;

    public BankTransaction(int id, double amount, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        BankTransaction that = (BankTransaction) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{id=" + id +
                ", amount=" + amount +
                ", date=" + date + '}';
    }
}
