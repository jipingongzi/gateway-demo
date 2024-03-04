package com.sean.gatewaydemo;

import lombok.Data;

@Data
public class MessageBody {
    private String isin;
    private String correlationId;
}