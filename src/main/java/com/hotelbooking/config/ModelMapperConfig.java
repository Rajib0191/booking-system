package com.hotelbooking.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration() //Accesses the configuration of the ModelMapper instance.
                .setFieldMatchingEnabled(true) //Allows matching fields directly, not just getter/setter methods.
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE) //Allows access to private fields
                .setMatchingStrategy(MatchingStrategies.STANDARD); //Uses the default (standard) strategy: strict but practical field-name matching.

        return modelMapper;
    }
}
