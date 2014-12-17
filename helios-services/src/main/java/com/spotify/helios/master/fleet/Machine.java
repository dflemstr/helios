package com.spotify.helios.master.fleet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;

public class Machine {

  @Nonnull
  private final String id;
  @Nonnull
  private final String primaryIP;
  // TODO: metadata not implemented

  public Machine(@Nonnull String id, @Nonnull String primaryIP) {
    this.id = id;
    this.primaryIP = primaryIP;
  }

  @JsonCreator
  public static Machine create(
      @Nonnull @JsonProperty("id") String id,
      @Nonnull @JsonProperty("primaryIP") String primaryIP) {
    return new Machine(id, primaryIP);
  }

  @Nonnull
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @Nonnull
  @JsonProperty("primaryIP")
  public String getPrimaryIP() {
    return primaryIP;
  }
}
