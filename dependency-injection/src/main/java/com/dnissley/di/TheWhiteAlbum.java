package com.dnissley.di;

import org.springframework.stereotype.Component;

/* @Component: When spring's component scan feature is on it will search for 
      classes with this annotation, instantiate them, and register them with 
      the application context. */

@Component
public class TheWhiteAlbum implements VinylRecord {
  public String readSurface() {
    return "Flew in from Miami Beach BOAC, didn't get to bed last night...";
  }
}
