package com.spotify.helios.master.fleet;

import com.google.common.collect.ImmutableList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UnitStatePage {

  @Nonnull
  private final ImmutableList<UnitState> unitStates;
  @Nullable
  private final String nextPageToken;

  public UnitStatePage(
      @Nonnull ImmutableList<UnitState> unitStates,
      @Nullable String nextPageToken) {
    this.unitStates = unitStates;
    this.nextPageToken = nextPageToken;
  }

  @JsonCreator
  public static UnitStatePage create(
      @Nonnull @JsonProperty("states") ImmutableList<UnitState> unitStates,
      @Nullable @JsonProperty("nextPageToken") String nextPageToken) {
    return new UnitStatePage(unitStates, nextPageToken);
  }

  @Nonnull
  @JsonProperty("states")
  public ImmutableList<UnitState> getUnitStates() {
    return unitStates;
  }

  @Nullable
  @JsonProperty("nextPageToken")
  public String getNextPageToken() {
    return nextPageToken;
  }
}
