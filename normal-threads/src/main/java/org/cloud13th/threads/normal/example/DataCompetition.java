package org.cloud13th.threads.normal.example;

import java.util.concurrent.TimeUnit;

public class DataCompetition extends Thread {

    private static final Account account = new Account(0);

    public static void main(String[] args) throws InterruptedException {
        new DataCompetition().start();
        new DataCompetition().start();
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println(account.balance);
    }

    @Override
    public void run() {
        try {
            account.modify(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class Account {
    protected float balance;

    public Account(float balance) {
        this.balance = balance;
    }

    public void modify(float difference) throws InterruptedException {
        float value = this.balance;
        TimeUnit.MILLISECONDS.sleep(1);
        this.balance = value + difference;
    }
}
