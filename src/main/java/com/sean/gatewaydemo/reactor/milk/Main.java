package com.sean.gatewaydemo.reactor.milk;

public class Main {
    public static void main(String[] args) {
        MilkFactory factory = new MilkFactory();

        //订阅1周
        MilkCustomer customer = new MilkCustomer(7);

        factory.subscribe(customer);
    }
}
