package com.example.persistence.examples.config;


import lombok.Data;

import java.time.temporal.ChronoUnit;

@Data
public class ClientProperties {

    private String name;
    private String code;
    private Long requestNumber;
    private ChronoUnit requestNumberUnit;

}
