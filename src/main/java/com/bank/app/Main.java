package com.bank.app;

import com.bank.app.model.Account;
import com.bank.app.model.Customer;
import com.bank.app.service.BankService;
import com.bank.app.service.PersistenceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DB_FILE = "bank.db";
    private static BankService bank;

    public static void main(String[] args) {
        // Load if present
        Object loaded = PersistenceService.load(DB_FILE);
        if (loaded instanceof BankService) {
            bank = (BankService) loaded;
            System.out.println("Loaded existing bank state.");
        } else {
            bank = new BankService();
            System.out.println("Started new bank.");
        }

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> createCustomer(sc);
                    case "2" -> createAccount(sc);
                    case "3" -> deposit(sc);
                    case "4" -> withdraw(sc);
                    case "5" -> transfer(sc);
                    case "6" -> viewAccount(sc);
                    case "7" -> listCustomers();
                    case "8" -> listAccounts();
                    case "9" -> saveState();
                    case "0" -> { saveState(); running = false; System.out.println("Existing...");}
                    default -> System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("====== Banking Machine ======");
        System.out.println("1. Create customer");
        System.out.println("2. Create account for customer");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Transfer");
        System.out.println("6. View account (balance & transactions");
        System.out.println("7. List customers");
        System.out.println("8. List all accounts");
        System.out.println("9. Save state");
        System.out.println("0. Exit");
        System.out.println("Choose");
    }

    private static void createCustomer(Scanner sc) {
        System.out.print("Customer name: ");
        String name = sc.nextLine().trim();
        Customer c = bank.createCustomer(name);
        System.out.println("Created: " + c);
    }

    private static void createAccount(Scanner sc) {
        System.out.print("Customer ID: ");
        String cid = sc.nextLine().trim();
        System.out.print("Account type (SAVINGS/CHECKING): ");
        String type = sc.nextLine().trim().toUpperCase();
        Account a = bank.createAccount(cid, type);
        System.out.println("Created account: " + a);
    }

    private static void deposit(Scanner sc) {
        System.out.print("Account ID: ");
        String aid = sc.nextLine().trim();
        System.out.print("Amount: ");
        BigDecimal amt = new BigDecimal(sc.nextLine().trim());
        bank.deposit(aid, amt);
        System.out.println("Deposited. New balance: " + bank.getAccount(aid).getBalance());
    }

    private static void withdraw(Scanner sc) {
        System.out.print("Account ID: ");
        String aid = sc.nextLine().trim();
        System.out.print("Amount: ");
        BigDecimal amt = new BigDecimal(sc.nextLine().trim());
        bank.withdraw(aid, amt);
        System.out.println("Withdrawn. New balance: " + bank.getAccount(aid).getBalance());
    }

    private static void transfer(Scanner sc) {
        System.out.print("From account ID: ");
        String from = sc.nextLine().trim();
        System.out.print("To Amount ID: ");
        String to = sc.nextLine().trim();
        System.out.print("Amount: ");
        BigDecimal amt = new BigDecimal(sc.nextLine().trim());
        bank.transfer(from, to, amt);
        System.out.println("Transfer complete: ");
    }

    private static void viewAccount(Scanner sc) {
        System.out.print("Account ID: ");
        String aid = sc.nextLine().trim();
        Account a = bank.getAccount(aid);
        System.out.println(a);
        System.out.println("Transactions:");
        List.of(a.getTransactions().toArray()).forEach(System.out::println);
    }

    private static void listCustomers() {
        System.out.println("Customers");
        for (Customer c : bank.getAllCustomers()) {
            System.out.println(c);
        }
    }

    private static void listAccounts() {
        System.out.println("Accounts");
        for (Account a : bank.getAllAccounts()) {
            System.out.println(a);
        }
    }

    private static void saveState() {
        PersistenceService.save(bank, DB_FILE);
        System.out.println("State saved to " + DB_FILE);
    }
}