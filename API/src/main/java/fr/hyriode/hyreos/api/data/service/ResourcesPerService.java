package fr.hyriode.hyreos.api.data.service;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import fr.hyriode.hyreos.api.data.IHyreosMetric;

import java.time.Instant;
import java.util.List;

@Measurement(name = "resources_per_service")
public class ResourcesPerService implements IHyreosMetric {

    @Column(name = "service_type", tag = true)
    private final ServiceType type;
    @Column(name = "service_name", tag = true)
    private final String name;

    @Column(name = "total_cpu_usage")
    private final long totalCpuUsage;
    @Column(name = "cpu_usages")
    private final List<Long> cpuUsages;
    @Column(name = "system_cpu_usage")
    private final long systemCpuUsage;
    @Column(name = "available_cpus")
    private final long availableCpus;

    @Column(name = "memory_usage")
    private final long memoryUsage;
    @Column(name = "memory_max")
    private final long memoryMax;
    @Column(name = "memory_limit")
    private final long memoryLimit;

    @Column(timestamp = true)
    private Instant time;

    public ResourcesPerService(ServiceType type, String name, long totalCpuUsage, List<Long> cpuUsages, long systemCpuUsage, long availableCpus, long memoryUsage, long memoryMax, long memoryLimit) {
        this.type = type;
        this.name = name;
        this.totalCpuUsage = totalCpuUsage;
        this.cpuUsages = cpuUsages;
        this.systemCpuUsage = systemCpuUsage;
        this.availableCpus = availableCpus;
        this.memoryUsage = memoryUsage;
        this.memoryMax = memoryMax;
        this.memoryLimit = memoryLimit;
    }

    public ServiceType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public long getTotalCpuUsage() {
        return this.totalCpuUsage;
    }

    public List<Long> getCpuUsages() {
        return this.cpuUsages;
    }

    public long getSystemCpuUsage() {
        return this.systemCpuUsage;
    }

    public long getAvailableCpus() {
        return this.availableCpus;
    }

    public long getMemoryUsage() {
        return this.memoryUsage;
    }

    public long getMemoryMax() {
        return this.memoryMax;
    }

    public long getMemoryLimit() {
        return this.memoryLimit;
    }

    public Instant getTime() {
        return this.time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
