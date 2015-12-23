package com.ezdi.audioplayer.bean;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

@Table("ez_document_master")
public class DocumentMaster {

	@Column(value = "worktype_desc")
	private String worktypeDesc;

	@Column(value = "worktype")
	private int worktype;

	@PrimaryKeyColumn(name = "audio_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String audioId;

	@PrimaryKeyColumn(name = "document_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String documentId;

	public String getAudioId() {
		return audioId;
	}

	public void setAudioId(String audioId) {
		this.audioId = audioId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public int getWorktype() {
		return worktype;
	}

	public void setWorktype(int worktype) {
		this.worktype = worktype;
	}

	public String getWorktypeDesc() {
		return worktypeDesc;
	}

	public void setWorktypeDesc(String worktypeDesc) {
		this.worktypeDesc = worktypeDesc;
	}
}
