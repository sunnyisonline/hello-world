package com.ezdi.audioplayer.metadata.request.bean;

public class Demographic {
	int patientId, visitNumber;
	String patientMrn, patientName, patientAccountno, patientLocation,
			patientGender;
//	java.sql.Timestamp patientExamDate, patientDob;
	String patientExamDate, patientDob;

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public int getVisitNumber() {
		return visitNumber;
	}

	public void setVisitNumber(int visitNumber) {
		this.visitNumber = visitNumber;
	}

	public String getPatientMrn() {
		return patientMrn;
	}

	public void setPatientMrn(String patientMrn) {
		this.patientMrn = patientMrn;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientAccountno() {
		return patientAccountno;
	}

	public void setPatientAccountno(String patientAccountno) {
		this.patientAccountno = patientAccountno;
	}

	public String getPatientLocation() {
		return patientLocation;
	}

	public void setPatientLocation(String patientLocation) {
		this.patientLocation = patientLocation;
	}

	public String getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(String patientGender) {
		this.patientGender = patientGender;
	}

	public String getPatientExamDate() {
		return patientExamDate;
	}

	public void setPatientExamDate(String patientExamDate) {
		this.patientExamDate = patientExamDate;
	}

	public String getPatientDob() {
		return patientDob;
	}

	public void setPatientDob(String patientDob) {
		this.patientDob = patientDob;
	}

}
