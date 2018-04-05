package com.dnissley.di;

import java.io.OutputStream;

public class VinylRecordPlayer {
  private VinylRecord record;

  public VinylRecordPlayer(VinylRecord record) {
    this.record = record;
    play(System.out);
  }

  public void play(OutputStream stream) {
    try {
      stream.write(record.readSurface().getBytes());
    }
    catch (java.io.IOException e) {
      // silence
    }
  }
}
