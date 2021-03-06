/*
 * Copyright (c) 2015 Spotify AB.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.spotify.helios.common.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.TimeUnit;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RolloutOptions {

  public static final long DEFAULT_TIMEOUT = TimeUnit.MINUTES.toSeconds(5);
  public static final int DEFAULT_PARALLELISM = 1;

  private final long timeout;
  private final int parallelism;
  private final boolean migrate;

  public RolloutOptions(@JsonProperty("timeout") final long timeout,
                        @JsonProperty("parallelism") final int parallelism,
                        @JsonProperty("migrate") final boolean migrate) {
    this.timeout = timeout;
    this.parallelism = parallelism;
    this.migrate = migrate;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .setTimeout(timeout)
        .setParallelism(parallelism)
        .setMigrate(migrate);
  }

  public long getTimeout() {
    return timeout;
  }

  public int getParallelism() {
    return parallelism;
  }

  public boolean getMigrate() {
    return migrate;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final RolloutOptions that = (RolloutOptions) o;

    if (migrate != that.migrate) {
      return false;
    }
    if (parallelism != that.parallelism) {
      return false;
    }
    if (timeout != that.timeout) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (timeout ^ (timeout >>> 32));
    result = 31 * result + parallelism;
    result = 31 * result + (migrate ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "RolloutOptions{" +
           "timeout=" + timeout +
           ", parallelism=" + parallelism +
           ", migrate=" + migrate +
           '}';
  }

  public static class Builder {

    private long timeout;
    private int parallelism;
    private boolean migrate;

    public Builder() {
      this.timeout = DEFAULT_TIMEOUT;
      this.parallelism = DEFAULT_PARALLELISM;
      this.migrate = false;
    }


    public Builder setTimeout(final long timeout) {
      this.timeout = timeout;
      return this;
    }

    public Builder setParallelism(final int parallelism) {
      this.parallelism = parallelism;
      return this;
    }

    public Builder setMigrate(final boolean migrate) {
      this.migrate = migrate;
      return this;
    }


    public RolloutOptions build() {
      return new RolloutOptions(timeout, parallelism, migrate);
    }
  }
}
