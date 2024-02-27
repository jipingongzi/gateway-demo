package com.sean.gatewaydemo.reactor;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Stream;

public class SubmissionPublishTest {
    public static void main(String[] args) {
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        publisher.subscribe(new Flow.Subscriber<>() {
            private Integer sum = 0;
            Flow.Subscription subscription = null;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {

                sum += item;
                System.out.println(sum);
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("ERROR:" + throwable.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                System.out.println(sum);
            }

        });

        Stream.of(3, 4).forEach(publisher::submit);
//        publisher.closeExceptionally(null);
    }
}
