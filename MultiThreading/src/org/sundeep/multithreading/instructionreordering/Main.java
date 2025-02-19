package org.sundeep.multithreading.instructionreordering;

class Singleton {
    private static Singleton instance;
    private int data;

    private Singleton() {
        data = 100; // Step 3
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public int getData() {
        return data;
    }
}

public class Main {
    static Singleton instance;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> instance = Singleton.getInstance());
        Thread t2 = new Thread(() -> {
            Singleton obj = Singleton.getInstance();
            if (obj.getData() == 0) {
                System.out.println("Reordering happened! Read uninitialized data.");
            }
        });

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

