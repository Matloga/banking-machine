package com.bank.app.service;

import com.bank.app.model.Account;
import com.bank.app.model.Customer;
import com.bank.app.model.Transaction;
import com.bank.app.util.IdGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BankService implements Serializable {
    private final Map<String, Customer> customers = new HashMap<>();
    private final Map<String, Account> accounts = new HashMap<>();

    public Customer createCustomer(String name) {
        String id = IdGenerator.nextId("C");
        Customer c = new Customer(id, name);
        customers.put(id, c);
        return c;
    }

    public Account createAccount(String customerId, String accountType) {
        if (!customers.containsKey(customerId)) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        String id = IdGenerator.nextId("A");
        Account acc = new Account(id, customerId, accountType);
        accounts.put(id, acc);
        return acc;
    }

    public void deposit(String accountId, BigDecimal amount) {
        Account acc = getAccount(accountId);
        acc.deposit(amount.setScale(2, RoundingMode.HALF_UP));
    }

    public void withdraw(String accountId, BigDecimal amount) {
        Account acc = getAccount(accountId);
        acc.withdraw(amount.setScale(2, RoundingMode.HALF_UP));
    }

    public void transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }
        Account from = getAccount(fromAccountId);
        Account to = getAccount(toAccountId);
        BigDecimal amt = amount.setScale(2, RoundingMode.HALF_UP);
        if (from.getBalance().compareTo(amt) < 0) {
            throw new IllegalArgumentException("Insufficient funds in source account");
        }
        // Withdraw
        from.withdraw(amt);
        from.addTransaction(Transaction.createTransferOut(amt, from.getBalance(), toAccountId));
        // Deposit
        to.deposit(amt);
        to.addTransaction(Transaction.createTransferIn(amt, to.getBalance(), fromAccountId));
    }

    public Account getAccount(String accountId) {
        Account a = accounts.get(accountId);
        if (a == null) throw new IllegalArgumentException("Account not found: " + accountId);
        return a;
    }

    public Optional<Customer> findCustomer(String customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }

    public List<Account> getAccountsForCustomer(String customerId) {
        List<Account> list = new ArrayList<>();
        for (Account a : accounts.values()) {
            if (a.getCustomerId().equals(customerId)) list.add(a);
        }
        return list;
    }

    public Collection<Account> getAllAccounts() { return Collections.unmodifiableCollection(accounts.values()); }
    public Collection<Customer> getAllCustomers() { return Collections.unmodifiableCollection(customers.values()); }

    public void closeAccount(String accountId) {
        Account a = getAccount(accountId);
        if (a.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("Account has non-zero balance. withdraw before closing.");
        }
        accounts.remove(accountId);
    }
}