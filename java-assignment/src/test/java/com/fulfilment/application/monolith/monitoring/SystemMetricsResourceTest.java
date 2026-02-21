package com.fulfilment.application.monolith.monitoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SystemMetricsResourceTest {

    @Inject
    SystemMetricsResource resource;

    @Test
    void testGetSystemMetrics() {
        // When
        ObjectNode metrics = resource.getSystemMetrics();

        // Then
        assertNotNull(metrics);
        assertTrue(metrics.has("cpu"));
        assertTrue(metrics.has("heapMemory"));
        assertTrue(metrics.has("threads"));
        assertTrue(metrics.has("timestamp"));
    }

    @Test
    void testGetHealth() {
        // When
        ObjectNode health = resource.getHealth();

        // Then
        assertNotNull(health);
        assertTrue(health.has("status"));
        assertTrue(health.has("timestamp"));
        assertTrue(health.has("application"));
        assertNotNull(health.get("status").asText());
    }

    @Test
    void testMetricsContainsCpuData() {
        // When
        ObjectNode metrics = resource.getSystemMetrics();

        // Then
        assertTrue(metrics.has("cpu"));
        ObjectNode cpuData = (ObjectNode) metrics.get("cpu");
        assertTrue(cpuData.has("processUsage"));
        assertTrue(cpuData.has("systemUsage"));
        assertTrue(cpuData.has("availableProcessors"));
    }

    @Test
    void testMetricsContainsMemoryData() {
        // When
        ObjectNode metrics = resource.getSystemMetrics();

        // Then
        assertTrue(metrics.has("heapMemory"));
        ObjectNode heapData = (ObjectNode) metrics.get("heapMemory");
        assertTrue(heapData.has("used"));
        assertTrue(heapData.has("max"));
        assertTrue(heapData.has("usagePercent"));
    }

    @Test
    void testMetricsContainsThreadData() {
        // When
        ObjectNode metrics = resource.getSystemMetrics();

        // Then
        assertTrue(metrics.has("threads"));
        ObjectNode threadData = (ObjectNode) metrics.get("threads");
        assertTrue(threadData.has("active"));
        assertTrue(threadData.has("peak"));
        assertTrue(threadData.has("totalStarted"));
    }
}

