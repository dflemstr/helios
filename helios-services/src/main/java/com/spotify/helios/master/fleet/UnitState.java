package com.spotify.helios.master.fleet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;

public class UnitState {

  @Nonnull
  private final String name;
  @Nonnull
  private final String hash;
  @Nonnull
  private final String machineID;
  @Nonnull
  private final String systemdLoadState;
  @Nonnull
  private final String systemdActiveState;
  @Nonnull
  private final String systemdSubState;

  public UnitState(
      @Nonnull String name,
      @Nonnull String hash,
      @Nonnull String machineID,
      @Nonnull String systemdLoadState,
      @Nonnull String systemdActiveState,
      @Nonnull String systemdSubState) {
    this.name = name;
    this.hash = hash;
    this.machineID = machineID;
    this.systemdLoadState = systemdLoadState;
    this.systemdActiveState = systemdActiveState;
    this.systemdSubState = systemdSubState;
  }

  @JsonCreator
  public static UnitState create(
      @Nonnull @JsonProperty("name") String name,
      @Nonnull @JsonProperty("hash") String hash,
      @Nonnull @JsonProperty("machineID") String machineID,
      @Nonnull @JsonProperty("systemdLoadState") String systemdLoadState,
      @Nonnull @JsonProperty("systemdActiveState") String systemdActiveState,
      @Nonnull @JsonProperty("systemdSubState") String systemdSubState) {
    return new UnitState(name, hash, machineID, systemdLoadState, systemdActiveState,
                         systemdSubState);
  }

  @Nonnull
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @Nonnull
  @JsonProperty("hash")
  public String getHash() {
    return hash;
  }

  @Nonnull
  @JsonProperty("machineID")
  public String getMachineID() {
    return machineID;
  }

  @Nonnull
  @JsonProperty("systemdLoadState")
  public String getSystemdLoadState() {
    return systemdLoadState;
  }

  @Nonnull
  @JsonProperty("systemdActiveState")
  public String getSystemdActiveState() {
    return systemdActiveState;
  }

  @Nonnull
  @JsonProperty("systemdSubState")
  public String getSystemdSubState() {
    return systemdSubState;
  }
}
