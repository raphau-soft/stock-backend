package com.raphau.springboot.stockExchange.entity;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="test", schema = "stock_exchange")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="timestamp")
    private long timestamp;
    
    @Column(name="finished")
    private boolean finished;

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TimeData> timeDatas;
    
    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CpuData> cpuDatas;
    
    public Test() {
    }

	public Test(int id, String name, long timestamp, boolean finished) {
		super();
		this.id = id;
		this.name = name;
		this.timestamp = timestamp;
		this.finished = finished;
	}

	public int getId() {
		return id;
	}

	public List<TimeData> getTimeDatas() {
		return timeDatas;
	}

	public void setTimeDatas(List<TimeData> timeDatas) {
		this.timeDatas = timeDatas;
	}

	public List<CpuData> getCpuDatas() {
		return cpuDatas;
	}

	public void setCpuDatas(List<CpuData> cpuDatas) {
		this.cpuDatas = cpuDatas;
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

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	@Override
	public String toString() {
		return "Test [id=" + id + ", name=" + name + ", timestamp=" + timestamp + ", finished=" + finished
				+ "]";
	}

}