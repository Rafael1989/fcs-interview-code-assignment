# Health Check Implementation

## Overview

Health check endpoints have been implemented using Quarkus SmallRye Health. These endpoints provide Kubernetes-compatible probes for pod management and traffic routing.

## Available Endpoints

### 1. Overall Health - `/q/health`
Returns the combined health status of all components.

**Response Example:**
```json
{
  "status": "UP",
  "checks": [
    {
      "name": "Application Liveness",
      "status": "UP",
      "data": {
        "status": "Running",
        "version": "1.0.0-SNAPSHOT"
      }
    },
    {
      "name": "Application Readiness",
      "status": "UP",
      "data": {
        "status": "Ready to serve requests",
        "api": "Operational"
      }
    },
    {
      "name": "Database",
      "status": "UP",
      "data": {
        "status": "Connected"
      }
    }
  ]
}
```

### 2. Liveness Probe - `/q/health/live`
Indicates whether the application is running and should be restarted if DOWN.

**Purpose:**
- Used by Kubernetes `livenessProbe`
- If DOWN, Kubernetes will restart the pod
- Detects hard failures like deadlocks

**Response:** Same format as above, filtered to liveness checks only

### 3. Readiness Probe - `/q/health/ready`
Indicates whether the application is ready to accept traffic.

**Purpose:**
- Used by Kubernetes `readinessProbe`
- If DOWN, Kubernetes removes pod from load balancer
- Detects temporary issues like startup or maintenance

**Response:** Same format as above, filtered to readiness checks only

---

## Health Check Implementations

### 1. ApplicationLivenessCheck (`@Liveness`)
- **Purpose:** Basic liveness indicator
- **Status:** Always UP (application is running)
- **Data:**
  - `status`: Running
  - `version`: 1.0.0-SNAPSHOT

### 2. ApplicationReadinessCheck (`@Readiness`)
- **Purpose:** Readiness indicator
- **Status:** UP when API is operational
- **Data:**
  - `status`: Ready to serve requests
  - `api`: Operational

### 3. DatabaseHealthCheck (`@Health`)
- **Purpose:** Database connectivity verification
- **Check:** Executes `SELECT 1` query
- **Status:** 
  - UP if database is accessible
  - DOWN if connection fails
- **Data:**
  - `status`: Connected (if UP) or error message (if DOWN)

---

## Kubernetes Integration

### Liveness Probe Configuration

```yaml
livenessProbe:
  httpGet:
    path: /q/health/live
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10
  timeoutSeconds: 3
  failureThreshold: 3
```

### Readiness Probe Configuration

```yaml
readinessProbe:
  httpGet:
    path: /q/health/ready
    port: 8080
  initialDelaySeconds: 10
  periodSeconds: 5
  timeoutSeconds: 3
  failureThreshold: 3
```

---

## Monitoring & Alerts

### Expected Behavior

**Normal (UP):**
- All health checks return status: UP
- HTTP status code: 200 OK
- Kubernetes considers pod healthy and ready

**Degraded (Database DOWN):**
- Database check returns status: DOWN
- Overall health depends on configuration
- Pod remains running but may be removed from load balancer

**Critical (Liveness DOWN):**
- Application cannot respond to health checks
- Kubernetes restarts the pod
- HTTP connection timeout or refused

---

## Configuration

### application.properties

```properties
# Health check configuration
quarkus.smallrye-health.root-path=/q/health

# Show all health checks (not just status)
mp.health.checks.enabled=true

# Include implementation details
quarkus.smallrye-health.expose-details=all
```

### Testing

Run the integration tests:

```bash
mvn clean test -Dtest=HealthCheckTest
```

---

## Endpoints Reference

| Endpoint | Purpose | Used By | Status Code |
|----------|---------|---------|-------------|
| `/q/health` | Overall health | Monitoring tools | 200 (UP) or 503 (DOWN) |
| `/q/health/live` | Liveness probe | Kubernetes pod restart | 200 (UP) or 503 (DOWN) |
| `/q/health/ready` | Readiness probe | Kubernetes load balancer | 200 (UP) or 503 (DOWN) |

---

## Best Practices

1. **Liveness vs Readiness:**
   - Liveness = "Is the process running?"
   - Readiness = "Can this process handle requests?"

2. **Health Check Timeout:**
   - Keep health checks fast (< 1 second)
   - Avoid long database queries
   - Use simple validation queries

3. **Failure Thresholds:**
   - Set failureThreshold >= 3 to avoid false positives
   - Adjust based on network latency

4. **Monitoring:**
   - Monitor `/q/health` endpoint for anomalies
   - Alert on repeated failures
   - Track response times

---

## Future Enhancements

1. Add cache health check
2. Add external service health checks (legacy system)
3. Custom metrics collection
4. Distributed tracing integration


