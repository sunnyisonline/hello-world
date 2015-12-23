package com.ezdi.audioplayer.bean;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

@Table("ez_audio_master")
public class AudioPhysicianMappingBean {

	@Column(value = "dictating_physician_id")
	private int dictatingPhysicianId;

	@PrimaryKeyColumn(name = "audio_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String audioId;

	public String getAudioId() {
		return audioId;
	}

	public void setAudioId(String audioId) {
		this.audioId = audioId;
	}

	public int getDictatingPhysicianId() {
		return dictatingPhysicianId;
	}

	public void setDictatingPhysicianId(int dictatingPhysicianId) {
		this.dictatingPhysicianId = dictatingPhysicianId;
	}
}
