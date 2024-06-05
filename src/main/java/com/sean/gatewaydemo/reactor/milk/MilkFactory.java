package com.sean.gatewaydemo.reactor.milk;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class MilkFactory extends SubmissionPublisher<String> {

    private final ScheduledFuture<?> periodicTask;
    private final ScheduledExecutorService scheduler;

    private static final List<String> milks = Arrays.asList("益力多", "酸牛奶", "原味奶", "低脂蛋奶", "羊奶", "甜牛奶");

    public MilkFactory() {
        super();
        //初始化定时器
        scheduler = new ScheduledThreadPoolExecutor(1);

        //每一天生产完牛奶并推送给消费者
        periodicTask = scheduler.scheduleAtFixedRate(
                () -> submit(produceMilk()), 0, 1, TimeUnit.SECONDS);
    }

    //随机生产牛奶
    private String produceMilk() {
        return milks.get((int) (Math.random() * milks.size()));
    }

    //关闭流
    public void close() {
        periodicTask.cancel(false);
        scheduler.shutdown();
        super.close();
    }
}
