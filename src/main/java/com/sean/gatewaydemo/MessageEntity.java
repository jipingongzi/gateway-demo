package com.sean.gatewaydemo;

import lombok.Data;

@Data
public class MessageEntity {
    private String isin;
    private String correlationId;
}