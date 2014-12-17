package com.spotify.helios.master.fleet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum State {
  INACTIVE("inactive"), LOADED("loaded"), LAUNCHED("launched");
  private final String jsonValue;

  State(String jsonValue) {
    this.jsonValue = jsonValue;
  }

  @JsonValue
  public String getJsonValue() {
    return jsonValue;
  }

  @JsonCreator
  public static State fromJsonValue(String jsonValue) {
    for (State state : values()) {
      if (state.jsonValue.equals(jsonValue)) {
        return state;
      }
    }

    throw new IllegalArgumentException("No enum member with that JSON value");
  }
}
