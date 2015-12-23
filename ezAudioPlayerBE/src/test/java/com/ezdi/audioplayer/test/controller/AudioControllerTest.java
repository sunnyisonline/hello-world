package com.ezdi.audioplayer.test.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.ezdi.audioplayer.audioRequest.AudioServiceRequest;
import com.ezdi.audioplayer.audioResponse.AudioServiceResponse;
import com.ezdi.audioplayer.controller.AudioController;
import com.ezdi.audioplayer.service.AudioService;

@RunWith(MockitoJUnitRunner.class)
public class AudioControllerTest {

	@InjectMocks
	private AudioController audioController;

	@Mock
	private AudioService audioService;

	@Mock
	private BindingResult result;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		// result = mock(BindingResult.class);
	}

	@Test
	public void testFetchAudioFilePath() throws Exception {

		when(audioService.fetchAudioFilePath("audio_1")).thenReturn(getAudio());
		when(result.hasErrors()).thenReturn(true);
		AudioServiceResponse data = audioController.fetchAudioFilePath(
				AudioServiceRequest(), result);
		audioController.fetchAudioFilePath(AudioServiceRequest(), result);
		assertNotNull(data);
	}

	private AudioServiceRequest AudioServiceRequest() {
		AudioServiceRequest audioServiceRequest = new AudioServiceRequest();
		audioServiceRequest.setAudioId("audio_1");
		return audioServiceRequest;
	}

	private AudioServiceResponse getAudio() {
		AudioServiceResponse audioServiceResponse = new AudioServiceResponse();
		return audioServiceResponse;
	}
}
