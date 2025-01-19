package org.sundeep.atm.machine.processor;

import java.util.Scanner;

public class OperationProcessor {
    private final Scanner scanner = new Scanner(System.in);

    public void handleUserOperation(OperationDto operationDto) {
        boolean isOperationDone = false;
        while (!isOperationDone) {
            // Provide menu to select the operation
            System.out.println("\n1. Check balance\t2. Withdraw cash");
            System.out.println("Choose the operation that you want to perform: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    new CheckBalanceOperation(operationDto.getCard().getAssociatedBankAccount()).perform();
                    isOperationDone = true;
                    break;
                case 2:
                    System.out.println("Enter amount: ");
                    double amount = scanner.nextDouble();

                    new WithdrawMoneyOperation(operationDto.getAtm(), operationDto.getCard().getAssociatedBankAccount(), amount).perform();

                    isOperationDone = true;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
