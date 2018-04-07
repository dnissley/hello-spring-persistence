package com.dnissley.di;

import java.io.OutputStream;
import java.io.IOException;

public class VinylRecordPlayer {
  private VinylRecord record;
  private OutputStream speaker;

  public VinylRecordPlayer(VinylRecord record, OutputStream speaker) {
    this.record = record;
    this.speaker = speaker;
  }

  public void play() {
    try {
      speaker.write(record.readSurface().getBytes());
    }
    catch (IOException e) {
      // silence...
    }
  }
}
