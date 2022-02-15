package com.raphau.springboot.stockExchange.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="cpu_data", schema="stock_exchange")
public class CpuData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    
    @ManyToOne(targetEntity = Test.class, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="test_id", nullable = false)
    @JsonBackReference
	private Test test;	

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "cpu_usage")
    private Double cpuUsage;

    public CpuData() {
    }

	public CpuData(int id, Test test, long timestamp, Double cpuUsage) {
		super();
		this.id = id;
		this.test = test;
		this.timestamp = timestamp;
		this.cpuUsage = cpuUsage;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Double getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(Double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	@Override
	public String toString() {
		return "CpuData [id=" + id + ", test=" + test + ", timestamp=" + timestamp + ", cpuUsage=" + cpuUsage + "]";
	}


}