package com.raphau.springboot.stockExchange.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="cpu_data", schema="stock_exchange")
public class CpuData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    
    @Column(name = "name")
    private String name;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "cpu_usage")
    private Double cpuUsage;

    public CpuData() {
    }

    @Override
    public String toString() {
        return "CpuData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", timestamp=" + timestamp +
                ", cpuUsage=" + cpuUsage +
                '}';
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public CpuData(int id, String name, long timestamp, Double cpuUsage) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.cpuUsage = cpuUsage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
