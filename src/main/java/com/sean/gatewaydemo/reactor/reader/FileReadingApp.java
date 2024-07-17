package com.sean.gatewaydemo.reactor.reader;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.publisher.Flux;

import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class FileReadingApp {

    private static final int CHUNK_SIZE = 1024; // 1KB
    private static final Duration DELAY_DURATION = Duration.ofSeconds(1);

    public static void main(String[] args) {
        Path path = Paths.get("C:\\Users\\Sean_Xiao\\Desktop\\article\\text.txt");
        CountDownLatch latch = new CountDownLatch(1);

        Flux<DataBuffer> dataBufferFlux = DataBufferUtils.readAsynchronousFileChannel(
                () -> AsynchronousFileChannel.open(path, StandardOpenOption.READ),
                new DefaultDataBufferFactory(), CHUNK_SIZE);

        dataBufferFlux.delayElements(DELAY_DURATION)
                .doOnNext(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    System.out.println(new String(bytes));
                    DataBufferUtils.release(dataBuffer); // 释放DataBuffer防止内存泄漏
                })
                .doOnError(e -> {
                    System.err.println("Error occurred while reading the file: " + e.getMessage());
                    latch.countDown();
                })
                .doOnComplete(() -> {
                    System.out.println("Finished reading the file.");
                    latch.countDown();
                })
                .subscribe();
        try {
            latch.await(); // 等待数据流处理完成
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for the data stream to finish.");
        }
    }
}