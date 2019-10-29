package org.anized.umf.config;

import org.anized.common.MessageBundle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
public class MessageConfig {

    @Bean
    MessageBundle resourceBundle() {
        return new MessageBundle(ResourceBundle.getBundle("messages", Locale.getDefault()));
    }

}