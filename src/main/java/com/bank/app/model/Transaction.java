package com.bank.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {
    public enum Type { DEPOSIT, WITHDRAWAL, TRANSFARE_IN, TRANSFARE_OUT }

    private final Type type;
    private final BigDecimal amount;
    private final BigDecimal postBalance;
    private final LocalDateTime timestamp;
    private final String note;

    public Transaction(Type type, BigDecimal amount, BigDecimal postBalance, String note) {
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
        this.timestamp = LocalDateTime.now();
        this.note = note;
    }

    public static Transaction createDeposit(BigDecimal amount, BigDecimal postBalance) {
        return new Transaction(Type.DEPOSIT, amount, postBalance, "Deposit");
    }

    public static Transaction createWithdrawal(BigDecimal amount, BigDecimal postBalance) {
        return new Transaction(Type.WITHDRAWAL, amount, postBalance, "Withdrawal");
    }

    public static Transaction createTransferIn(BigDecimal amount, BigDecimal postBalance, String fromAccount) {
        return new Transaction(Type.TRANSFARE_IN, amount, postBalance, "Transfer from " + fromAccount);
    }

    public static Transaction createTransferOut(BigDecimal amount, BigDecimal postBalance, String toAccount) {
        return new Transaction(Type.TRANSFARE_OUT, amount, postBalance, "Transfer to " + toAccount);
    }

    @Override
    public String toString() {
        String time = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format("[%s] %s: %s | balance=%s | %s",
                time, type, amount.setScale(2), postBalance.setScale(2), note);
    }
}