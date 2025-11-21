package com.bank.app.service;

import java.io.*;

public final class PersistenceService {
    private PersistenceService() {}

    public static void save(Object obj, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(obj);
        } catch (IOException e) {
            System.err.println("Failed to save: " + e.getMessage());
        }
    }

    public static Object load(String filename) {
        File f = new File(filename);
        if (!f.exists()) return null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load: " + e.getMessage());
            return null;
        }
    }
}