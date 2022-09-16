
import java.util.ArrayList;

public class Account {
    /**
     * He.
     */
    private double balance;
    private ArrayList<Transaction> transitionList = new ArrayList<Transaction>();

    /**
     * He.
     */
    private void deposit(double amount1) {
        if (amount1 <= 0) {
            System.out.println("So tien ban nap vao khong hop le!");
            return;
        }

        this.balance += amount1;
    }

    /**
     * He.
     */
    private void withdraw(double amount1) {
        if (amount1 <= 0) {
            System.out.println("So tien ban rut ra khong hop le!");
            return;
        } else if (amount1 > this.balance) {
            System.out.println("So tien ban rut vuot qua so du!");
            return;
        } else {
            this.balance -= amount1;
        }
    }

    /**
     * He.
     */
    public void addTransaction(double amount1, String operation1) {
        if ((operation1 != "deposit") && (operation1 != "withdraw")) {
            System.out.println("Yeu cau khong hop le!");
            return;
        }

        if (operation1 == "deposit") {
            deposit(amount1);
        } else {
            withdraw(amount1);
        }

        Transaction tmp = new Transaction(operation1, amount1, this.balance);
        transitionList.add(tmp);
    }

    /**
     * He.
     */
    public void printTransaction() {
        int num = 0;
        for (Transaction tmp : transitionList) {
            num++;
            System.out.print("Giao dich " + num);

            if (tmp.getOperation() == "deposit") {
                System.out.print(": Nap tien $");
            } else {
                System.out.print(": Rut tien $");
            }
            System.out.printf("%.2f", tmp.getAmount());
            System.out.print(". So du luc nay: $");
            System.out.printf("%.2f.\n", tmp.getBalance());
        }
    }
}