package com.dnissley.di;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import java.io.OutputStream;

/* @SpringBootApplication: Combines the following annotations:
      - @EnableAutoConfiguration 
      - @ComponentScan
      - @Configuration */

/* org.springframework.boot.autoconfigure.@EnableAutoConfiguration: Turns on 
      the spring boot feature which searches for additional @Configuration 
      annotated classes among your dependencies based on the types of objects
      registered with your application context. */

/* org.springframework.context.annotation.@ComponentScan: Turns on the feature 
      of spring that causes it to search for @Component annotated classes to 
      instantiate and register with the application context. */

/* org.springframework.context.annotation.@Configuration: Indicates to spring 
      that this class contains @Bean annotated methods which provide instances 
      of objects that should be registered with the application context. 
      
      You can annotate multiple classes with @Configuration and the instances 
      of objects provided by each will be registered with a shared application
      context. */

@SpringBootApplication
public class DependencyInjectionApplication {

  /* @Autowired: Indicates to spring that this field should be injected with 
        an instance of the declared type from the application context. */
  @Autowired
  private VinylRecord record;

  /* @Bean: Indicates to spring that this method provides an instance of the
        method's return type to be registered with the application context. */
  @Bean
  public OutputStream speaker() {
    return System.out;
  }

  @Bean
  public VinylRecordPlayer recordPlayer() {
    return new VinylRecordPlayer(record, speaker());
  }

  @Bean
  public ApplicationListener<ApplicationReadyEvent> playRecordWhenSetupIsComplete() {
    return new ApplicationListener<ApplicationReadyEvent>() {
      @Override
      public void onApplicationEvent(final ApplicationReadyEvent event) {
        recordPlayer().play();
      }
    };
  }

  public static void main(String[] args) {
    SpringApplication.run(DependencyInjectionApplication.class, args);
  }
}
