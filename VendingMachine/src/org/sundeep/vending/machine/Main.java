package org.sundeep.vending.machine;

public class Main {
    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine();

        vendingMachine.addProduct(new Item("001", "Frooti", 20), 2);
        vendingMachine.addProduct(new Item("002", "Pepsi", 40), 3);
        vendingMachine.addProduct(new Item("003", "Coke", 35), 2);

        vendingMachine.insertCash(50);
        vendingMachine.insertCash(50);
        System.out.println("Available balance: " + vendingMachine.getAvailableBalance());

        vendingMachine.startProductSelection();

        vendingMachine.purchaseProduct("003");
        vendingMachine.purchaseProduct("003");
        vendingMachine.purchaseProduct("003");

        vendingMachine.insertCash(60);
        vendingMachine.startProductSelection();
        vendingMachine.purchaseProduct("002");

//        vendingMachine.insertCash(60);

        vendingMachine.purchaseProduct("002");
        vendingMachine.purchaseProduct("001");

        vendingMachine.checkOut();
    }
}
