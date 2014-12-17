package com.spotify.helios.master;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterators;

import com.spotify.helios.common.descriptors.Deployment;
import com.spotify.helios.common.descriptors.Descriptor;
import com.spotify.helios.common.descriptors.HostStatus;
import com.spotify.helios.common.descriptors.Job;
import com.spotify.helios.common.descriptors.JobId;
import com.spotify.helios.common.descriptors.JobIdParseException;
import com.spotify.helios.common.descriptors.JobStatus;
import com.spotify.helios.common.descriptors.TaskStatusEvent;
import com.spotify.helios.master.fleet.Fleet;
import com.spotify.helios.master.fleet.Machine;
import com.spotify.helios.master.fleet.MachinePage;
import com.spotify.helios.master.fleet.State;
import com.spotify.helios.master.fleet.Unit;
import com.spotify.helios.master.fleet.UnitPage;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

public class FleetMasterModel implements MasterModel {

  static final String X_HELIOS_SECTION = "X-Helios";
  static final String SERVICE_SECTION = "Service";
  static final String UNIT_SECTION = "Unit";
  public static final String DESCRIPTOR_NAME = "Descriptor";

  private final Fleet fleet;

  FleetMasterModel(Fleet fleet) {
    this.fleet = fleet;
  }

  /**
   * Creates a new Fleet-based master model.
   *
   * @param fleet The Fleet to use when implementing the model.
   */
  public static FleetMasterModel create(Fleet fleet) {
    return new FleetMasterModel(fleet);
  }

  @Override
  public void registerHost(String host, String id) {
    // TODO: implement
    throw new UnsupportedOperationException();
  }

  @Override
  public void deregisterHost(String host) throws HostNotFoundException, HostStillInUseException {
    // TODO: implement
    throw new UnsupportedOperationException();
  }

  @Override
  public ImmutableList<String> listHosts() {
    ImmutableList.Builder<String> resultBuilder = ImmutableList.builder();

    for (Machine machine : fetchMachines()) {
      resultBuilder.add(machine.getId());
    }

    return resultBuilder.build();
  }

  @Override
  public HostStatus getHostStatus(String host) {
    // TODO: implement
    throw new UnsupportedOperationException();
  }

  @Override
  public void addJob(Job job) throws JobExistsException {
    String name = toUnitName(job.getId());
    Unit unit = toUnit(job);

    fleet.putUnit(name, unit);
  }

  @Override
  public Job getJob(JobId job) {
    return toJob(fleet.getUnit(toUnitName(job)));
  }

  @Override
  public ImmutableMap<JobId, Job> getJobs() {
    ImmutableMap.Builder<JobId, Job> resultBuilder = ImmutableMap.builder();

    for (Unit unit : fetchUnits()) {
      String name = unit.getName();

      if (name == null) {
        throw new IllegalStateException("Fleet sent us a nameless unit");
      }

      resultBuilder.put(toJobId(name), toJob(unit));
    }

    return resultBuilder.build();
  }

  @Override
  public JobStatus getJobStatus(JobId jobId) {
    // TODO: implement
    throw new UnsupportedOperationException();
  }

  @Override
  public Job removeJob(JobId job) throws JobDoesNotExistException, JobStillDeployedException {
    String unitName = toUnitName(job);
    Job result = toJob(fleet.getUnit(unitName));
    fleet.deleteUnit(unitName);
    return result;
  }

  @Override
  public void deployJob(String host, Deployment job)
      throws HostNotFoundException, JobAlreadyDeployedException, JobDoesNotExistException,
             JobPortAllocationConflictException {
    // TODO: implement
    throw new UnsupportedOperationException();
  }

  @Override
  public Deployment getDeployment(String host, JobId job) {
    // TODO: implement
    throw new UnsupportedOperationException();
  }

  @Override
  public Deployment undeployJob(String host, JobId job)
      throws HostNotFoundException, JobNotDeployedException {
    // TODO: implement
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateDeployment(String host, Deployment deployment)
      throws HostNotFoundException, JobNotDeployedException {
    // TODO: implement
    throw new UnsupportedOperationException();
  }

  @Override
  public List<String> getRunningMasters() {
    // TODO
    throw new UnsupportedOperationException();
  }

  @Override
  public List<TaskStatusEvent> getJobHistory(JobId jobId) throws JobDoesNotExistException {
    // TODO: implement
    throw new UnsupportedOperationException();
  }

  Iterable<Machine> fetchMachines() {
    return new Iterable<Machine>() {
      @Override
      public Iterator<Machine> iterator() {
        return new MachineIterator();
      }
    };
  }

  Iterable<Unit> fetchUnits() {
    return new Iterable<Unit>() {
      @Override
      public Iterator<Unit> iterator() {
        return new UnitIterator();
      }
    };
  }

  String toUnitName(@Nonnull JobId id) {
    return "helios:" + id.toString() + ".service";
  }

  boolean hasJobId(@Nonnull String unitName) {
    return unitName.startsWith("helios:") && unitName.endsWith(".service");
  }

  JobId toJobId(@Nonnull String unitName) {
    Preconditions.checkArgument(
        hasJobId(unitName),
        "The specified unit name does not conform to the Helios name standard");

    try {
      return JobId.parse(
          unitName.substring("helios".length(), unitName.length() - ".service".length()));
    } catch (JobIdParseException e) {
      throw new IllegalArgumentException("The encapsulated job ID was not valid", e);
    }
  }

  Unit toUnit(@Nonnull Job job) {
    String args = toDockerArgs(job);
    String container = toDockerContainerName(job);

    String killCommand = MessageFormat.format("/usr/bin/docker kill {0}", container);
    String removeCommand = MessageFormat.format("/usr/bin/docker rm {0}", container);
    String pullCommand = MessageFormat.format("/usr/bin/docker pull {0}", job.getImage());
    String runCommand =
        MessageFormat.format("/usr/bin/docker run --name {0} {1} {2}",
                             container, job.getImage(), args);
    String stopCommand = MessageFormat.format("/usr/bin/docker stop {0}", container);

    ImmutableMultimap<String, String> unitMetadata =
        ImmutableMultimap.<String, String>builder()
            .put("After", "docker.service")
            .put("Requires", "docker.service")
            .build();

    ImmutableMultimap<String, String> serviceMetadata =
        ImmutableMultimap.<String, String>builder()
            .put("TimeoutStartSec", "0")
            .put("ExecStartPre", "-" + killCommand)
            .put("ExecStartPre", "-" + removeCommand)
            .put("ExecStartPre", pullCommand)
            .put("ExecStart", runCommand)
            .put("ExecStop", stopCommand)
            .build();

    // TODO: maybe break this out into individual fields as well for easy debugging
    ImmutableMultimap<String, String> heliosMetadata =
        ImmutableMultimap.<String, String>builder()
            .put(DESCRIPTOR_NAME, job.toJsonString())
            .build();

    ImmutableList<Unit.Option> options =
        ImmutableList.<Unit.Option>builder()
            .addAll(Unit.Option.section(UNIT_SECTION, unitMetadata))
            .addAll(Unit.Option.section(SERVICE_SECTION, serviceMetadata))
            .addAll(Unit.Option.section(X_HELIOS_SECTION, heliosMetadata))
            .build();

    return Unit.created(State.LOADED, options);
  }

  Job toJob(@Nonnull Unit unit) {
    String descriptor = null;

    for (Unit.Option option : unit.getOptions()) {
      if (X_HELIOS_SECTION.equals(option.getSection()) &&
          DESCRIPTOR_NAME.equals(option.getName())) {
        descriptor = option.getValue();
      }
    }

    if (descriptor == null) {
      throw new IllegalArgumentException("The provided unit was not created by Helios");
    }

    try {
      return Descriptor.parse(descriptor, Job.class);
    } catch (IOException e) {
      throw new IllegalArgumentException("The provided unit contained an invalid Helios descriptor",
                                         e);
    }
  }

  String toDockerContainerName(@Nonnull Job job) {
    // TODO: implement
    return "postgres:1.2.0:0123456789abcdef0123456789abcdef";
  }

  String toDockerArgs(@Nonnull Job job) {
    // TODO: implement
    return "postgres";
  }

  class MachineIterator extends AbstractIterator<Machine> {

    private MachinePage machinePage;
    private Iterator<Machine> pageIterator = Iterators.emptyIterator();

    @Override
    protected Machine computeNext() {
      while (!pageIterator.hasNext()) {
        if (machinePage == null) {
          machinePage = fleet.getMachines(null);
        } else if (machinePage.getNextPageToken() != null) {
          machinePage = fleet.getMachines(machinePage.getNextPageToken());
        } else {
          return endOfData();
        }
        pageIterator = machinePage.getMachines().iterator();
      }

      return pageIterator.next();
    }
  }

  class UnitIterator extends AbstractIterator<Unit> {

    private UnitPage unitPage;
    private Iterator<Unit> pageIterator = Iterators.emptyIterator();

    @Override
    protected Unit computeNext() {
      while (!pageIterator.hasNext()) {
        if (unitPage == null) {
          unitPage = fleet.getUnits(null);
        } else if (unitPage.getNextPageToken() != null) {
          unitPage = fleet.getUnits(unitPage.getNextPageToken());
        } else {
          return endOfData();
        }
        pageIterator = unitPage.getUnits().iterator();
      }

      return pageIterator.next();
    }
  }
}
