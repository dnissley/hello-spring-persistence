package com.dnissley.di;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class AppConfig {

  @Bean
  public VinylRecord recordToPlay() {
    return new TheWhiteAlbum();
  }

  @Bean
  public VinylRecordPlayer recordPlayer() {
    return new VinylRecordPlayer(recordToPlay());
  }
}
