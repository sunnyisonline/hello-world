package com.ezdi.audioplayer.solr.document;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument
public class DocumentMasterDTO implements Serializable {

	private static final long serialVersionUID = -8243204987605562108L;

	@Field("document_current_status_id")
	private int documentCurrentStatusId;

	@Field("document_current_status")
	private String documentCurrentStatus;

	@Field("audio_id")
	private String audioId;

	@Field("id")
	private String id;

	@Field("document_id")
	private String documentId;

	@Field("document_name")
	private String documentName;

	@Field("attending_physician_id")
	private int attendingPhysicianId;

	@Field("attending_physician_login_id")
	private String attendingPhysicianLoginId;

	@Field("attending_physician_mnemonic")
	private String attendingPhysicianMnemonic;

	@Field("attending_physician_name")
	private String attendingPhysicianName;

	@Field("audio_file_codec")
	private int audioFileCodec;

	@Field("audio_file_dictation_date")
	private Timestamp audioFileDictationDate;

	@Field("audio_file_name")
	private String audioFileName;

	@Field("audio_file_path")
	private String audioFilePath;

	@Field("audio_file_play_time_in_sec")
	private int audioFilePlayTimeInSec;

	@Field("audio_file_size")
	private int audioFileSize;

	@Field("created_date")
	private Timestamp createdDate;

	@Field("dictating_physician_id")
	private int dictatingPhysicianId;

	@Field("dictating_physician_login_id")
	private String dictatingPhysicianLoginId;

	@Field("dictating_physician_mnemonic")
	private String dictatingPhysicianMnemonic;

	@Field("dictating_physician_name")
	private String dictatingPhysicianName;

	@Field("document_path")
	private String documentPath;

	@Field("global_document_id")
	private int globalDocumentId;

	@Field("hospital_id")
	private int hospitalId;

	@Field("hospital_name")
	private String hospitalName;

	@Field("patient_accountno")
	private String patientAccountno;

	@Field("patient_dob")
	private Timestamp patientDob;

	@Field("patient_gender")
	private String patientGender;

	@Field("patient_mrn")
	private String patientMrn;

	@Field("patient_ssn")
	private String patientSsn;

	@Field("tat_achieved")
	private int tatAchieved;

	@Field("tracking_number")
	private String trackingNumber;

	@Field("turn_around_time")
	private int turnAroundTime;

	@Field("updated_by")
	private String updatedBy;

	@Field("updated_by_id")
	private int updatedById;

	@Field("updated_date")
	private Timestamp updatedDate;

	@Field("visit_number")
	private int visitNumber;

	@Field("worktype")
	private int worktype;

	@Field("worktype_desc")
	private String worktypeDesc;

	public int getDocumentCurrentStatusId() {
		return documentCurrentStatusId;
	}

	public void setDocumentCurrentStatusId(int documentCurrentStatusId) {
		this.documentCurrentStatusId = documentCurrentStatusId;
	}

	public String getDocumentCurrentStatus() {
		return documentCurrentStatus;
	}

	public void setDocumentCurrentStatus(String documentCurrentStatus) {
		this.documentCurrentStatus = documentCurrentStatus;
	}

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

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public int getAttendingPhysicianId() {
		return attendingPhysicianId;
	}

	public void setAttendingPhysicianId(int attendingPhysicianId) {
		this.attendingPhysicianId = attendingPhysicianId;
	}

	public String getAttendingPhysicianLoginId() {
		return attendingPhysicianLoginId;
	}

	public void setAttendingPhysicianLoginId(String attendingPhysicianLoginId) {
		this.attendingPhysicianLoginId = attendingPhysicianLoginId;
	}

	public String getAttendingPhysicianMnemonic() {
		return attendingPhysicianMnemonic;
	}

	public void setAttendingPhysicianMnemonic(String attendingPhysicianMnemonic) {
		this.attendingPhysicianMnemonic = attendingPhysicianMnemonic;
	}

	public String getAttendingPhysicianName() {
		return attendingPhysicianName;
	}

	public void setAttendingPhysicianName(String attendingPhysicianName) {
		this.attendingPhysicianName = attendingPhysicianName;
	}

	public int getAudioFileCodec() {
		return audioFileCodec;
	}

	public void setAudioFileCodec(int audioFileCodec) {
		this.audioFileCodec = audioFileCodec;
	}

	public Timestamp getAudioFileDictationDate() {
		return audioFileDictationDate;
	}

	public void setAudioFileDictationDate(Timestamp audioFileDictationDate) {
		this.audioFileDictationDate = audioFileDictationDate;
	}

	public String getAudioFileName() {
		return audioFileName;
	}

	public void setAudioFileName(String audioFileName) {
		this.audioFileName = audioFileName;
	}

	public String getAudioFilePath() {
		return audioFilePath;
	}

	public void setAudioFilePath(String audioFilePath) {
		this.audioFilePath = audioFilePath;
	}

	public int getAudioFilePlayTimeInSec() {
		return audioFilePlayTimeInSec;
	}

	public void setAudioFilePlayTimeInSec(int audioFilePlayTimeInSec) {
		this.audioFilePlayTimeInSec = audioFilePlayTimeInSec;
	}

	public int getAudioFileSize() {
		return audioFileSize;
	}

	public void setAudioFileSize(int audioFileSize) {
		this.audioFileSize = audioFileSize;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public int getDictatingPhysicianId() {
		return dictatingPhysicianId;
	}

	public void setDictatingPhysicianId(int dictatingPhysicianId) {
		this.dictatingPhysicianId = dictatingPhysicianId;
	}

	public String getDictatingPhysicianLoginId() {
		return dictatingPhysicianLoginId;
	}

	public void setDictatingPhysicianLoginId(String dictatingPhysicianLoginId) {
		this.dictatingPhysicianLoginId = dictatingPhysicianLoginId;
	}

	public String getDictatingPhysicianMnemonic() {
		return dictatingPhysicianMnemonic;
	}

	public void setDictatingPhysicianMnemonic(String dictatingPhysicianMnemonic) {
		this.dictatingPhysicianMnemonic = dictatingPhysicianMnemonic;
	}

	public String getDictatingPhysicianName() {
		return dictatingPhysicianName;
	}

	public void setDictatingPhysicianName(String dictatingPhysicianName) {
		this.dictatingPhysicianName = dictatingPhysicianName;
	}

	public int getGlobalDocumentId() {
		return globalDocumentId;
	}

	public void setGlobalDocumentId(int globalDocumentId) {
		this.globalDocumentId = globalDocumentId;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getPatientAccountno() {
		return patientAccountno;
	}

	public void setPatientAccountno(String patientAccountno) {
		this.patientAccountno = patientAccountno;
	}

	public Timestamp getPatientDob() {
		return patientDob;
	}

	public void setPatientDob(Timestamp patientDob) {
		this.patientDob = patientDob;
	}

	public String getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(String patientGender) {
		this.patientGender = patientGender;
	}

	public String getPatientMrn() {
		return patientMrn;
	}

	public void setPatientMrn(String patientMrn) {
		this.patientMrn = patientMrn;
	}

	public String getPatientSsn() {
		return patientSsn;
	}

	public void setPatientSsn(String patientSsn) {
		this.patientSsn = patientSsn;
	}

	public int getTatAchieved() {
		return tatAchieved;
	}

	public void setTatAchieved(int tatAchieved) {
		this.tatAchieved = tatAchieved;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public int getTurnAroundTime() {
		return turnAroundTime;
	}

	public void setTurnAroundTime(int turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(int updatedById) {
		this.updatedById = updatedById;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getVisitNumber() {
		return visitNumber;
	}

	public void setVisitNumber(int visitNumber) {
		this.visitNumber = visitNumber;
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

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
