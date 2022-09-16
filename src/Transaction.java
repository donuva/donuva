import java.util.ArrayList;

public class Transaction {
     /**
     * He.
     **/
     private String operation;
     private double amount;
     private double balance;

    public static final String DEPOSIT = "deposit";
    public static final String WITHDRAW = "withdraw";

    /**
     * T.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * T.
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * T.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * T.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * T.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * T.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * He.
     */
    Transaction(String operation1, double amount1, double balance1) {
        this.operation = operation1;
        this.amount = amount1;
        this.balance = balance1;
    }

}

