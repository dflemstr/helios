package com.spotify.helios.master.fleet;

import com.google.common.collect.ImmutableList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UnitPage {

  @Nonnull
  private final ImmutableList<Unit> units;
  @Nullable
  private final String nextPageToken;

  public UnitPage(
      @Nonnull ImmutableList<Unit> units,
      @Nullable String nextPageToken) {
    this.units = units;
    this.nextPageToken = nextPageToken;
  }

  @JsonCreator
  public static UnitPage create(
      @Nonnull @JsonProperty("units") ImmutableList<Unit> units,
      @Nullable @JsonProperty("nextPageToken") String nextPageToken) {
    return new UnitPage(units, nextPageToken);
  }

  @Nonnull
  @JsonProperty("units")
  public ImmutableList<Unit> getUnits() {
    return units;
  }

  @Nullable
  @JsonProperty("nextPageToken")
  public String getNextPageToken() {
    return nextPageToken;
  }
}
