package com.ezdi.audioplayer.controller;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezdi.audioplayer.audioRequest.AudioServiceRequest;
import com.ezdi.audioplayer.audioResponse.AudioServiceResponse;
import com.ezdi.audioplayer.bean.Audio;
import com.ezdi.audioplayer.logger.LoggerUtil;
import com.ezdi.audioplayer.service.AudioService;
import com.ezdi.s3connection.service.AmazonS3Service;

@RestController
public class AudioController {
	private static final Logger logger = Logger
			.getLogger(AudioController.class);

	@Autowired
	private AudioService audioService;

	@Autowired
	private AmazonS3Service amazonS3Service;

//	@RequestMapping(value="/streaming", method=RequestMethod.GET)
//	public byte[] checkStreaming() throws Exception {
////		String fileName = audioServiceRequest.getAudioId()+".mp3";
//		String fileName = "test.mp3";
//		return amazonS3Service.streamAudio("ezdi-integration-bucket/ezdocportal/client_data/tyk/FRHG/798/12334/mp3/20151211", 
//				fileName);
//	}
	
	@RequestMapping(value = "/getAudio", method = RequestMethod.POST)
	public AudioServiceResponse fetchAudioFilePath(
			@Validated @RequestBody AudioServiceRequest audioServiceRequest,
			BindingResult result) {

		logger.info(LoggerUtil.getMessage("Inside /getAudio"));

		if (result.hasErrors()) {
			return AudioServiceResponse.createMicroServiceResponse(result);
		}

		String audioId = audioServiceRequest.getAudioId();

		AudioServiceResponse audioResponse = audioService
				.fetchAudioFilePath(audioId);
		return AudioServiceResponse.createMicroServiceResponse(audioResponse);

	}

	@RequestMapping(value = "/getallAudioFiles", method = RequestMethod.GET)
	public List<Audio> fetchAllAudioFiles() {
		List<Audio> audioFileList = audioService.fetchAllAudioFiles();
		return audioFileList;

	}

	@RequestMapping(value = "/deleteAudio", method = RequestMethod.POST)
	public AudioServiceResponse deleteAudioFile(
			@Validated @RequestBody AudioServiceRequest audioServiceRequest,
			BindingResult result) {

		logger.info(LoggerUtil.getMessage("Inside /deleteAudio method"));
		if (result.hasErrors()) {
			return AudioServiceResponse.createMicroServiceResponse(result);
		}

		String audioId = audioServiceRequest.getAudioId();

		return AudioServiceResponse.deleteMicroServiceResponse(audioService
				.deleteAudioFile(audioId));

	}

}