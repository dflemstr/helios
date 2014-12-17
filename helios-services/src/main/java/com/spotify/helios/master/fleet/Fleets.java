package com.spotify.helios.master.fleet;

import com.google.common.net.HostAndPort;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public final class Fleets {

  private Fleets() {
    throw new IllegalAccessError("This class may not be instantiated.");
  }

  public static Fleet atAddress(HostAndPort hostAndPort) {
    try {
      return atUri(
          new URI("http",
                  null, hostAndPort.getHostText(), hostAndPort.getPort(),
                  "fleet/v1", null, null));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid host/port to connect to", e);
    }
  }

  public static Fleet atUri(URI uri) {
    // TODO: re-use some of the Helios helper classes for this
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new GuavaModule());

    ClientConfig clientConfig = new ClientConfig();
    clientConfig.register(new JacksonJsonProvider());

    Client client = ClientBuilder.newBuilder()
        .withConfig(clientConfig)
        .build();

    return WebResourceFactory.newResource(Fleet.class, client.target(uri));
  }
}
