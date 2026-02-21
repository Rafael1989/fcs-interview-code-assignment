package com.fulfilment.application.monolith.monitoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.management.OperatingSystemMXBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.jboss.logging.Logger;

@Path("monitoring")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class SystemMetricsResource {

  private static final Logger LOGGER = Logger.getLogger(SystemMetricsResource.class.getName());

  @Inject ObjectMapper objectMapper;

  @GET
  @Path("metrics")
  public ObjectNode getSystemMetrics() {
    try {
      ObjectNode response = objectMapper.createObjectNode();

      // Get CPU metrics
      OperatingSystemMXBean osBean =
          (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
      double cpuUsage = osBean.getProcessCpuLoad() * 100;
      double systemCpuUsage = osBean.getSystemCpuLoad() * 100;
      int availableProcessors = osBean.getAvailableProcessors();

      // Get Memory metrics
      MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
      long heapUsed = memoryBean.getHeapMemoryUsage().getUsed();
      long heapMax = memoryBean.getHeapMemoryUsage().getMax();
      long heapCommitted = memoryBean.getHeapMemoryUsage().getCommitted();

      long nonHeapUsed = memoryBean.getNonHeapMemoryUsage().getUsed();
      long nonHeapMax = memoryBean.getNonHeapMemoryUsage().getMax();

      // Runtime info
      Runtime runtime = Runtime.getRuntime();
      long totalMemory = runtime.totalMemory();
      long freeMemory = runtime.freeMemory();
      long maxMemory = runtime.maxMemory();

      // Thread metrics
      int threadCount = ManagementFactory.getThreadMXBean().getThreadCount();
      int peakThreadCount = ManagementFactory.getThreadMXBean().getPeakThreadCount();
      long totalStartedThreads = ManagementFactory.getThreadMXBean().getTotalStartedThreadCount();

      // Build response
      ObjectNode cpuNode = objectMapper.createObjectNode();
      cpuNode.put("processUsage", String.format("%.2f%%", cpuUsage));
      cpuNode.put("systemUsage", String.format("%.2f%%", systemCpuUsage));
      cpuNode.put("availableProcessors", availableProcessors);
      response.set("cpu", cpuNode);

      ObjectNode heapNode = objectMapper.createObjectNode();
      heapNode.put("used", formatBytes(heapUsed));
      heapNode.put("usedBytes", heapUsed);
      heapNode.put("max", formatBytes(heapMax));
      heapNode.put("maxBytes", heapMax);
      heapNode.put("committed", formatBytes(heapCommitted));
      heapNode.put("committedBytes", heapCommitted);
      heapNode.put("usagePercent", String.format("%.2f%%", (heapUsed * 100.0) / heapMax));
      response.set("heapMemory", heapNode);

      ObjectNode nonHeapNode = objectMapper.createObjectNode();
      nonHeapNode.put("used", formatBytes(nonHeapUsed));
      nonHeapNode.put("usedBytes", nonHeapUsed);
      nonHeapNode.put("max", formatBytes(nonHeapMax));
      nonHeapNode.put("maxBytes", nonHeapMax);
      response.set("nonHeapMemory", nonHeapNode);

      ObjectNode runtimeNode = objectMapper.createObjectNode();
      runtimeNode.put("totalMemory", formatBytes(totalMemory));
      runtimeNode.put("totalMemoryBytes", totalMemory);
      runtimeNode.put("freeMemory", formatBytes(freeMemory));
      runtimeNode.put("freeMemoryBytes", freeMemory);
      runtimeNode.put("maxMemory", formatBytes(maxMemory));
      runtimeNode.put("maxMemoryBytes", maxMemory);
      runtimeNode.put("usedMemory", formatBytes(totalMemory - freeMemory));
      runtimeNode.put("usedMemoryBytes", totalMemory - freeMemory);
      response.set("runtime", runtimeNode);

      ObjectNode threadNode = objectMapper.createObjectNode();
      threadNode.put("active", threadCount);
      threadNode.put("peak", peakThreadCount);
      threadNode.put("totalStarted", totalStartedThreads);
      response.set("threads", threadNode);

      response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

      LOGGER.info("System metrics retrieved successfully");
      return response;
    } catch (Exception e) {
      LOGGER.error("Error retrieving system metrics", e);
      ObjectNode errorNode = objectMapper.createObjectNode();
      errorNode.put("error", "Failed to retrieve system metrics: " + e.getMessage());
      return errorNode;
    }
  }

  @GET
  @Path("health")
  public ObjectNode getHealth() {
    ObjectNode response = objectMapper.createObjectNode();
    response.put("status", "UP");
    response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    response.put("application", "Java Code Assignment - Fulfillment");

    try {
      ObjectNode detailsNode = objectMapper.createObjectNode();

      OperatingSystemMXBean osBean =
          (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
      detailsNode.put("systemLoadAverage", String.format("%.2f", osBean.getSystemLoadAverage()));
      detailsNode.put("cpuAvailable", osBean.getAvailableProcessors());

      MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
      long heapUsed = memoryBean.getHeapMemoryUsage().getUsed();
      long heapMax = memoryBean.getHeapMemoryUsage().getMax();
      double heapUsagePercent = (heapUsed * 100.0) / heapMax;

      if (heapUsagePercent > 90) {
        response.put("status", "WARNING");
        detailsNode.put("warning", "High memory usage: " + String.format("%.2f%%", heapUsagePercent));
      }

      response.set("details", detailsNode);
    } catch (Exception e) {
      LOGGER.error("Error in health check", e);
      response.put("status", "DOWN");
    }

    return response;
  }

  private String formatBytes(long bytes) {
    if (bytes <= 0) return "0 B";
    final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
    int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
    return String.format("%.2f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
  }
}

