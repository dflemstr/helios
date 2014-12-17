package com.spotify.helios.master.fleet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * An interface for controlling a CoreOS Fleet.  The implementation might use the Fleet REST API, or
 * some other connection method.
 */
public interface Fleet {

  @PUT
  @Path("/units/{name}")
  @Consumes(MediaType.APPLICATION_JSON)
  void putUnit(
      @Nonnull @PathParam("name") String name,
      @Nonnull Unit unit);

  @GET
  @Path("/units")
  @Produces(MediaType.APPLICATION_JSON)
  UnitPage getUnits(
      @Nullable @QueryParam("nextPageToken") String nextPageToken);

  @GET
  @Path("/units/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  Unit getUnit(
      @Nonnull @PathParam("name") String name);

  @DELETE
  @Path("/units/{name}")
  void deleteUnit(
      @Nonnull @PathParam("name") String name);

  @GET
  @Path("/state")
  @Produces(MediaType.APPLICATION_JSON)
  UnitStatePage getState(
      @Nullable @QueryParam("machineID") String machineID,
      @Nullable @QueryParam("unitName") String unitName,
      @Nullable @QueryParam("nextPageToken") String nextPageToken);

  @GET
  @Path("/machines")
  @Produces(MediaType.APPLICATION_JSON)
  MachinePage getMachines(
      @Nullable @QueryParam("nextPageToken") String nextPageToken);

}
