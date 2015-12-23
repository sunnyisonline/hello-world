package com.ezdi.audioplayer.metadata.request.bean;

public class WaveHeader {
	Demographic demographic;
	RouteInfo routeInfo;
	int hospitalId, clinicId, worktype, turnAroundTime, localWaveId, dui,
			audioFileSize, audioFilePlayTimeInSec, globalWaveId,
			processingLevel, transGroupId, audioFileCodec, resend,
			globalDocumentId, priorityId, stationId, jobNumber, statusId,
			tdsTrackingNumber, audioIdInIntermediateDB, 
			dictatingPhysicianId;
	String trackingNumber, audioFileName,
			dictatingPhysicianName;
	// java.sql.Timestamp audioFileDictationDate, audioFileDictationDateGMT,
	// audioFileRecievedDate;

	String audioFileDictationDate, audioFileDictationDateGMT,
			audioFileRecievedDate;

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public int getClinicId() {
		return clinicId;
	}

	public void setClinicId(int clinicId) {
		this.clinicId = clinicId;
	}

	public int getWorktype() {
		return worktype;
	}

	public void setWorktype(int worktype) {
		this.worktype = worktype;
	}

	public int getTurnAroundTime() {
		return turnAroundTime;
	}

	public void setTurnAroundTime(int turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}

	public int getLocalWaveId() {
		return localWaveId;
	}

	public void setLocalWaveId(int localWaveId) {
		this.localWaveId = localWaveId;
	}

	public int getDui() {
		return dui;
	}

	public void setDui(int dui) {
		this.dui = dui;
	}

	public int getAudioFileSize() {
		return audioFileSize;
	}

	public void setAudioFileSize(int audioFileSize) {
		this.audioFileSize = audioFileSize;
	}

	public int getAudioFilePlayTimeInSec() {
		return audioFilePlayTimeInSec;
	}

	public void setAudioFilePlayTimeInSec(int audioFilePlayTimeInSec) {
		this.audioFilePlayTimeInSec = audioFilePlayTimeInSec;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public int getGlobalWaveId() {
		return globalWaveId;
	}

	public void setGlobalWaveId(int globalWaveId) {
		this.globalWaveId = globalWaveId;
	}

	public int getProcessingLevel() {
		return processingLevel;
	}

	public void setProcessingLevel(int processingLevel) {
		this.processingLevel = processingLevel;
	}

	public int getTransGroupId() {
		return transGroupId;
	}

	public void setTransGroupId(int transGroupId) {
		this.transGroupId = transGroupId;
	}

	public int getAudioFileCodec() {
		return audioFileCodec;
	}

	public void setAudioFileCodec(int audioFileCodec) {
		this.audioFileCodec = audioFileCodec;
	}

	public int getResend() {
		return resend;
	}

	public void setResend(int resend) {
		this.resend = resend;
	}

	public Demographic getDemographic() {
		return demographic;
	}

	public void setDemographic(Demographic demographic) {
		this.demographic = demographic;
	}

	public RouteInfo getRouteInfo() {
		return routeInfo;
	}

	public void setRouteInfo(RouteInfo routeInfo) {
		this.routeInfo = routeInfo;
	}

	public void setAudioFileDictationDateGMT(String audioFileDictationDateGMT) {
		this.audioFileDictationDateGMT = audioFileDictationDateGMT;
	}

	public int getGlobalDocumentId() {
		return globalDocumentId;
	}

	public void setGlobalDocumentId(int globalDocumentId) {
		this.globalDocumentId = globalDocumentId;
	}

	public int getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(int priorityId) {
		this.priorityId = priorityId;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public int getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(int jobNumber) {
		this.jobNumber = jobNumber;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getTdsTrackingNumber() {
		return tdsTrackingNumber;
	}

	public void setTdsTrackingNumber(int tdsTrackingNumber) {
		this.tdsTrackingNumber = tdsTrackingNumber;
	}

	public int getDictatingPhysicianId() {
		return dictatingPhysicianId;
	}

	public void setDictatingPhysicianId(int dictatingPhysicianId) {
		this.dictatingPhysicianId = dictatingPhysicianId;
	}

	public String getAudioFileName() {
		return audioFileName;
	}

	public void setAudioFileName(String audioFileName) {
		this.audioFileName = audioFileName;
	}

	public String getDictatingPhysicianName() {
		return dictatingPhysicianName;
	}

	public void setDictatingPhysicianName(String dictatingPhysicianName) {
		this.dictatingPhysicianName = dictatingPhysicianName;
	}

	public String getAudioFileDictationDate() {
		return audioFileDictationDate;
	}

	public void setAudioFileDictationDate(String audioFileDictationDate) {
		this.audioFileDictationDate = audioFileDictationDate;
	}

	public String getAudioFileRecievedDate() {
		return audioFileRecievedDate;
	}

	public void setAudioFileRecievedDate(String audioFileRecievedDate) {
		this.audioFileRecievedDate = audioFileRecievedDate;
	}

	public String getAudioFileDictationDateGMT() {
		return audioFileDictationDateGMT;
	}

	public int getAudioIdInIntermediateDB(){
		return audioIdInIntermediateDB;
	}
	
	public void setAudioIdInIntermediateDB(int audioIdInIntermediateDB){
		this.audioIdInIntermediateDB = audioIdInIntermediateDB;
	}
	
}