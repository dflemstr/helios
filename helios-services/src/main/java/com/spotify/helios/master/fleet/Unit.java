package com.spotify.helios.master.fleet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Represents a systemd unit according to the Fleet API.  Newly (un-submitted) units only have a
 * {@code desiredState} and {@code options}, and can be created by using {@link
 * #created(com.spotify.helios.master.fleet.State, com.google.common.collect.ImmutableList)}. Units that are submitted
 * will have more fields set, and can be created using {@link #submitted(String, com.spotify.helios.master.fleet.State,
 * com.spotify.helios.master.fleet.State, com.google.common.collect.ImmutableList, String)}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class Unit {

  @Nullable
  private final String name;
  @Nonnull
  private final State desiredState;
  @Nullable
  private final State currentState;
  @Nonnull
  private final ImmutableList<Option> options;
  @Nullable
  private final String machineID;

  public Unit(@Nullable String name,
              @Nonnull State desiredState,
              @Nullable State currentState,
              @Nonnull ImmutableList<Option> options,
              @Nullable String machineID) {
    this.name = name;
    this.options = options;
    this.desiredState = desiredState;
    this.currentState = currentState;
    this.machineID = machineID;
  }

  /**
   * Constructs an unit that is in the "created" state, i.e. is not known by Fleet yet.
   *
   * @param desiredState The state the unit should have when it is submitted.
   * @param options      The options controlling the behavior of the unit.
   * @return A newly constructed unit.
   */
  public static Unit created(
      @Nonnull State desiredState,
      @Nonnull ImmutableList<Option> options) {
    return new Unit(null, desiredState, null, options, null);
  }

  @JsonCreator
  public static Unit submitted(
      @Nullable @JsonProperty("name") String name,
      @Nonnull @JsonProperty("desiredState") State desiredState,
      @Nullable @JsonProperty("currentState") State currentState,
      @Nonnull @JsonProperty("options") ImmutableList<Option> options,
      @Nullable @JsonProperty("machineID") String machineID) {
    return new Unit(name, desiredState, currentState, options, machineID);
  }

  @Nullable
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @Nonnull
  @JsonProperty("desiredState")
  public State getDesiredState() {
    return desiredState;
  }

  @Nullable
  @JsonProperty("currentState")
  public State getCurrentState() {
    return currentState;
  }

  @Nonnull
  @JsonProperty("options")
  public ImmutableList<Option> getOptions() {
    return options;
  }

  @Nullable
  @JsonProperty("machineID")
  public String getMachineID() {
    return machineID;
  }

  public Unit withState(State state) {
    return new Unit(name, state, currentState, options, machineID);
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Option {

    @Nonnull
    private final String section;
    @Nonnull
    private final String name;
    @Nonnull
    private final String value;

    Option(@Nonnull String section, @Nonnull String name, @Nonnull String value) {
      this.section = section;
      this.name = name;
      this.value = value;
    }

    @JsonCreator
    public static Option single(
        @Nonnull @JsonProperty("section") String section,
        @Nonnull @JsonProperty("name") String name,
        @Nonnull @JsonProperty("value") String value) {
      return new Option(section, name, value);
    }

    public static ImmutableList<Option> section(
        @Nonnull String section,
        @Nonnull ImmutableMultimap<String, String> contents) {
      ImmutableList.Builder<Option> resultBuilder = ImmutableList.builder();

      for (Map.Entry<String, String> entry : contents.entries()) {
        resultBuilder.add(Option.single(section, entry.getKey(), entry.getValue()));
      }

      return resultBuilder.build();
    }

    @Nonnull
    @JsonProperty("section")
    public String getSection() {
      return section;
    }

    @Nonnull
    @JsonProperty("name")
    public String getName() {
      return name;
    }

    @Nonnull
    @JsonProperty("value")
    public String getValue() {
      return value;
    }
  }
}
