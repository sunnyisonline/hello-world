package com.ezdi.audioplayer.bean;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

@Table("ez_physician_master")
public class PhysicianS3AudioPathBean {

	@Column(value = "physician_doc_path")
	private String physicianDocPath;
	
	@Column(value = "physician_wave_path")
	private String physicianWavePath;

	@PrimaryKeyColumn(name = "physician_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private int physicianId;

	public String getPhysicianWavePath() {
		return physicianWavePath;
	}

	public void setPhysicianWavePath(String physicianWavePath) {
		this.physicianWavePath = physicianWavePath;
	}

	public int getPhysicianId() {
		return physicianId;
	}

	public void setPhysicianId(int physicianId) {
		this.physicianId = physicianId;
	}

	public String getPhysicianDocPath(){
		return physicianDocPath;
	}
	
	public void setPhysicianDocPath(String physicianDocPath){
		this.physicianDocPath = physicianDocPath;
	}
}
