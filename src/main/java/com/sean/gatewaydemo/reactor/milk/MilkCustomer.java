package com.sean.gatewaydemo.reactor.milk;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;

public class MilkCustomer implements Flow.Subscriber<String> {
    private Flow.Subscription subscription;
    private AtomicInteger available = new AtomicInteger(0);
    private int dayCount;

    public MilkCustomer(int dayCount) {
        this.dayCount = dayCount;
    }
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        //设置总量
        available.set(dayCount);

        //第一天
        subscription.request(1);
    }

    @Override
    public void onNext(String milk) {
        System.out.println("今天的牛奶到了: " + milk);

        //如果还有存量，继续请求
        if(available.decrementAndGet() > 0){
            subscription.request(1);
        }else{
            System.out.println("牛奶套餐已经派完，欢迎继续订购");
            this.subscription.cancel();
        }
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("closed.");
    }
}
