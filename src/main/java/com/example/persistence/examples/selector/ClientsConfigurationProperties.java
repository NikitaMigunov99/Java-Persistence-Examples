package com.example.persistence.examples.selector;

import com.example.persistence.examples.config.ClientProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "generator")
@Data
public class ClientsConfigurationProperties {

    private List<ClientProperties> clients = new ArrayList<>();


}
