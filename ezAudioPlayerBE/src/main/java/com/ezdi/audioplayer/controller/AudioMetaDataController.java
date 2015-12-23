package com.ezdi.audioplayer.controller;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ezdi.audioplayer.audioRequest.AudioServiceRequest;
import com.ezdi.audioplayer.audioResponse.AudioResponseWrapper;
import com.ezdi.audioplayer.audioResponse.AudioServiceResponse;
import com.ezdi.audioplayer.bean.Audio;
import com.ezdi.audioplayer.logger.LoggerUtil;
import com.ezdi.audioplayer.metadata.request.bean.Demographic;
import com.ezdi.audioplayer.metadata.request.bean.Level;
import com.ezdi.audioplayer.metadata.request.bean.MetaDataRequestWrapper;
import com.ezdi.audioplayer.service.AudioService;

@RestController
// @RequestMapping(value="audioMS")
public class AudioMetaDataController {
	private static final Logger logger = Logger
			.getLogger(AudioMetaDataController.class);

	@Autowired
	private AudioService audioService;

	public String waveBucketByAudioId(String audioId) throws Exception {
		try {
			int physicianId = audioService.getPhysicianIdByAudioId(audioId);
			String audioPath = audioService
					.getAudioPathByPhysicianId(physicianId);
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			String today = df.format(new java.util.Date());
			audioPath += "/" + today;
			if (audioService.prepareAmazonS3AudioFolderStructure(audioPath)) {
				return audioPath;
			}
		} catch (Exception p) {
			p.printStackTrace();
		}
		return "";
	}

	public String docBucketByAudioId(String audioId) throws Exception {
		try {
			int physicianId = audioService.getPhysicianIdByAudioId(audioId);
			String audioPath = audioService
					.getDocPathByPhysicianId(physicianId);
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			String today = df.format(new java.util.Date());
			audioPath += "/" + today;
			if (audioService.prepareAmazonS3AudioFolderStructure(audioPath)) {
				return audioPath;
			}
		} catch (Exception p) {
			p.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "/pushAudioToAmazonS3", method = RequestMethod.PUT)
	public @ResponseBody AudioResponseWrapper insertAudioFileinAmazonS3(
			HttpEntity<byte[]> requestEntity) throws Exception {

		logger.info(LoggerUtil.getMessage("Inside /pushAudioToAmazonS3"));

		if (requestEntity.getHeaders() == null
				|| requestEntity.getHeaders().get("fileName") == null) {
			throw new Exception("Audio File Name is Empty");
		}

		String fileName = requestEntity.getHeaders().get("fileName").get(0);
		String audioId = "";
		logger.info(LoggerUtil.getMessage("File Name=" + fileName));
		if (fileName.contains(".")) {
			audioId = fileName.substring(0, fileName.lastIndexOf("."));
		}
		
		logger.info(LoggerUtil.getMessage("Before waveBucketByAudioId " + audioId));
		String waveBucketName = waveBucketByAudioId(audioId);
		logger.info(LoggerUtil.getMessage("After waveBucketByAudioId " + waveBucketName));
		String docBucketName = docBucketByAudioId(audioId);
		logger.info(LoggerUtil.getMessage("After waveBucketByAudioId " + docBucketName));
		if (waveBucketName == null || waveBucketName.equals("")) {
			throw new Exception("Error - Audio Wave Path doesn't exists");
		}
		if (docBucketName == null || docBucketName.equals("")) {
			throw new Exception("Error - Doc Path doesn't exists");
		}
		logger.info(LoggerUtil.getMessage("Before upload wave bucketName - "
				+ waveBucketName));
		logger.info(LoggerUtil.getMessage("Before upload doc bucketName - "
				+ docBucketName));
		
		if (audioService.uploadAudioToS3(waveBucketName, docBucketName,
				fileName, requestEntity.getBody())) {
			if (audioService.updateAudioWavePathAfterS3UploadSuccess(
					waveBucketName, audioId)) {
				AudioResponseWrapper audioResponseWrapper = new AudioResponseWrapper();
				audioResponseWrapper.setResponseCode("success");
				audioResponseWrapper
						.setResponseMessage("Audio Streaming was Successful");
				return audioResponseWrapper;
			} else {
				throw new Exception(
						"Error - Updating Audio Path in Cassandra Failed");
			}
		} else {
			throw new Exception("Error - Uploading Audio on S3 Failed");
		}

	}

	// @RequestMapping(value = "/updateAudioMetadata", method =
	// RequestMethod.POST, consumes = "application/json")
	@RequestMapping(value = "/insertMetaDataInCassandra", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody AudioResponseWrapper insertMetaDataInCassandra(
			@RequestBody MetaDataRequestWrapper metaDataRequestWarpper)
			throws Exception {
		logger.info(LoggerUtil.getMessage("Inside /insertMetaDataInCassandra"));
		AudioResponseWrapper audioResponseWrapper = new AudioResponseWrapper();
		boolean status = audioService
				.insertMetaDatatoCassandra(metaDataRequestWarpper);
		if (status) {
			audioResponseWrapper.setResponseCode("success");
			audioResponseWrapper
					.setResponseMessage("Audio MetaData successfully inserted");
		}

		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getAudioFileCodec()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getAudioFileName()));
		// logger.info(LoggerUtil.getMessage(
		// +
		// metaDataRequestWarpper.getWaveHeader().getAudioFilePlayTimeInSec()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getAudioFileSize()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getClinicId()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getDictatingPhysicianId()));
		// logger.info(LoggerUtil.getMessage(
		// +
		// metaDataRequestWarpper.getWaveHeader().getDictatingPhysicianName()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getDui()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getGlobalDocumentId()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getGlobalWaveId()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getHospitalId()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getJobNumber()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getLocalWaveId()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getPriorityId()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getProcessingLevel()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getResend()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getStationId()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getStatusId()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getTdsTrackingNumber()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getTrackingNumber()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getTransGroupId()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getTurnAroundTime()));
		// logger.info(LoggerUtil.getMessage(
		// + metaDataRequestWarpper.getWaveHeader().getWorktype()));
		// logger.info(LoggerUtil.getMessage(
		// +
		// metaDataRequestWarpper.getWaveHeader().getAudioFileDictationDate()));
		// logger.info(LoggerUtil.getMessage(
		// +
		// metaDataRequestWarpper.getWaveHeader().getAudioFileRecievedDate()));
		//
		// Demographic d =
		// metaDataRequestWarpper.getWaveHeader().getDemographic();
		// logger.info("hospitalId11 - " + d.getPatientAccountno()));
		// logger.info(LoggerUtil.getMessage( + d.getPatientGender()));
		// logger.info(LoggerUtil.getMessage( + d.getPatientId()));
		// logger.info(LoggerUtil.getMessage( + d.getPatientLocation()));
		// logger.info(LoggerUtil.getMessage( + d.getPatientMrn()));
		// logger.info(LoggerUtil.getMessage( + d.getPatientName()));
		// logger.info(LoggerUtil.getMessage( + d.getVisitNumber()));
		// logger.info(LoggerUtil.getMessage( + d.getPatientDob()));
		// logger.info(LoggerUtil.getMessage( + d.getPatientExamDate()));
		//
		// List<Level> l =
		// metaDataRequestWarpper.getWaveHeader().getRouteInfo().getLevel();
		// for (Level ll : l) {
		// logger.info("hospitalId21 - " + ll.getId()));
		// logger.info("hospitalId21 - " + ll.getLevelStatusId()));
		// logger.info("hospitalId21 - " + ll.getSubGroupId()));
		// logger.info("hospitalId21 - " + ll.getTargetServerId()));
		// }
		return audioResponseWrapper;
	}

}