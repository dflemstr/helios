package com.spotify.helios.master.fleet;

import com.google.common.collect.ImmutableList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MachinePage {

  @Nonnull
  private final ImmutableList<Machine> machines;
  @Nullable
  private final String nextPageToken;

  MachinePage(
      @Nonnull ImmutableList<Machine> machines,
      @Nullable String nextPageToken) {
    this.machines = machines;
    this.nextPageToken = nextPageToken;
  }

  @JsonCreator
  public static MachinePage create(
      @Nonnull @JsonProperty("machines") ImmutableList<Machine> machines,
      @Nullable @JsonProperty("nextPageToken") String nextPageToken) {
    return new MachinePage(machines, nextPageToken);
  }

  @Nonnull
  @JsonProperty("machines")
  public ImmutableList<Machine> getMachines() {
    return machines;
  }

  @Nullable
  @JsonProperty("nextPageToken")
  public String getNextPageToken() {
    return nextPageToken;
  }
}
