package com.ezdi.audioplayer.bean;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

@Table("ez_audio_master")
public class Audio {

//	@Column(value = "audio_file_path")
	@Column(value="audio_file_mp3_path")
	private String audioFilepath;

	@PrimaryKeyColumn(name = "audio_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String audioId;

	@PrimaryKeyColumn(name = "audio_file_name", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String audioFileName;

	public String getAudioFilepath() {
		return audioFilepath;
	}

	public String getAudioId() {
		return audioId;
	}

	public void setAudioId(String audioId) {
		this.audioId = audioId;
	}

	public void setAudioFilepath(String audioFilepath) {
		this.audioFilepath = audioFilepath;
	}

	public String getAudioFileName() {
		return audioFileName;
	}

	public void setAudioFileName(String audioFileName) {
		this.audioFileName = audioFileName;
	}

}
