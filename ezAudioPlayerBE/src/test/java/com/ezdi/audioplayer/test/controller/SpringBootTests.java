package com.ezdi.audioplayer.test.controller;
//package com.ezdi.ezdocportal.controller;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//
//import org.mockito.Matchers;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.boot.test.TestRestTemplate;
//import org.springframework.boot.test.WebIntegrationTest;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Test;
//
//import com.ezdi.ezdocportal.audioResponse.AudioResponseWrapper;
//import com.ezdi.ezdocportal.main.Application;
//import com.ezdi.ezdocportal.metadata.request.bean.Demographic;
//import com.ezdi.ezdocportal.metadata.request.bean.Level;
//import com.ezdi.ezdocportal.metadata.request.bean.MetaDataRequestWrapper;
//import com.ezdi.ezdocportal.metadata.request.bean.RouteInfo;
//import com.ezdi.ezdocportal.metadata.request.bean.WaveHeader;
//
//import static org.testng.Assert.*;
//
//@SpringApplicationConfiguration(classes=Application.class)
//@WebIntegrationTest("server.port:0")
//@DirtiesContext
//public class SpringBootTests extends AbstractTestNGSpringContextTests {
//
//	@Value("${local.server.port}")
//	  private int port;
//
//	/* All Running
//	 * @Test
//	public void testDemo() throws Exception {
//		ResponseEntity<MicroServiceResponse> entity = new TestRestTemplate().getForEntity(
//				"http://localhost:8085/demo", MicroServiceResponse.class);
//		assertEquals(HttpStatus.OK, entity.getStatusCode());
//        assertEquals(200, entity.getBody().getErrorCode());
//        assertEquals("Demo", entity.getBody().getResponseObject());
//	}
//
//    @Test
//    public void testGetAllPatient() throws Exception {
//        ResponseEntity<MicroServiceResponse> entity = new TestRestTemplate().getForEntity(
//                "http://localhost:8085/getAllPatient", MicroServiceResponse.class);
//        assertEquals(HttpStatus.OK, entity.getStatusCode());
//        assertEquals(200, entity.getBody().getErrorCode());
//    }
//
//    @Test
//    public void testGetPatientByMRN() throws Exception {
//        ResponseEntity<MicroServiceResponse> entity = new TestRestTemplate().getForEntity(
//                "http://localhost:8085/getPatientByMRN/100004", MicroServiceResponse.class);
//        assertEquals(HttpStatus.OK, entity.getStatusCode());
//        assertEquals(200, entity.getBody().getErrorCode());
//        LinkedHashMap p = (LinkedHashMap)entity.getBody().getResponseObject();
//        System.out.println("Check ID - "+p.get("patientId"));
//        System.out.println("Check firstName - "+p.get("firstName"));
//        assertEquals(new Integer(5), p.get("patientId"));
//        assertEquals("PAMELA", p.get("firstName"));
//    }
//
//	
//    @Test
//    public void testGetPatientByMRNmock() throws Exception {
//        ResponseEntity<MicroServiceResponse> entity = new TestRestTemplate().getForEntity(
//                "http://localhost:8085/getPatientByMRN/100004", MicroServiceResponse.class);
//        assertEquals(HttpStatus.OK, entity.getStatusCode());
//        assertEquals(200, entity.getBody().getErrorCode());
//        LinkedHashMap p = (LinkedHashMap)entity.getBody().getResponseObject();
//        System.out.println("Check ID - "+p.get("patientId"));
//        System.out.println("Check firstName - "+p.get("firstName"));
//        assertEquals(new Integer(5), p.get("patientId"));
//        assertEquals("PAMELA", p.get("firstName"));      
//        
//        // Mock Value
//        Patient pp = mock(Patient.class);
//        when(pp.getPatientId()).thenReturn(5);
//        System.out.println("Mock - "+pp.getPatientId());
//        assertEquals(p.get("patientId"), pp.getPatientId());
//        
//        // Invocation times
//        pp.getPatientId();
//        pp.getPatientId();        
//        
//        verify(pp, times(4)).getPatientId();   
//        
//        //verify mock value
//        pp.setAge(21);
//        verify(pp).setAge(Matchers.eq(21));
//    }
//    */
//	
//	@Test
//    public void insertMetaDataInCassandra() throws Exception {
//		TestRestTemplate testRestTemplate = new TestRestTemplate();
//
//		// Blank Case
///*				RequestData data = new RequestData();
//				data.setAudioId("");
//				data.setAudioText("||SHREE GANESH||");
//				ResponseEntity<MicroServiceResponse> entity = testRestTemplate.
//						 postForEntity("http://localhost:8081/file/saveTranscription", data, MicroServiceResponse.class);
//		        
//		        assertEquals(HttpStatus.OK, entity.getStatusCode());
//		        assertEquals(200, entity.getBody().getHeader().getCode());   
//		        assertEquals("Saved", entity.getBody().getHeader().getMessage());
//*/
//		
//		// Exception Case
///*		RequestData data = new RequestData();
//		data.setAudioId("audio_4");
//		data.setAudioText("||SHREE GANESH||");
//		ResponseEntity<MicroServiceResponse> entity = testRestTemplate.
//				 postForEntity("http://localhost:8081/file/saveTranscription", data, MicroServiceResponse.class);
//        
//        //assertEquals(HttpStatus.OK, entity.getStatusCode());
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), entity.getBody().getHeader().getCode());   
//        assertEquals("Unable to save the changes", entity.getBody().getHeader().getMessage());
//*/        
//
//		// Cassandra Case
////		RequestData data = new RequestData();
////		data.setAudioId("audio_4");
////		data.setAudioText("||SHREE GANESH||");
//		
//		MetaDataRequestWrapper metaDataRequestWarpper = new MetaDataRequestWrapper();
//		WaveHeader waveHeader = new WaveHeader();
//		waveHeader.setAudioFileCodec(1000);
//		waveHeader.setAudioFileDictationDate("10/16/2015 23:07:53");
//		waveHeader.setAudioFileDictationDateGMT("10/16/2015 23:07:53");
//		waveHeader.setAudioFileName("test.wav");
//		waveHeader.setAudioFilePlayTimeInSec(100);
//		waveHeader.setAudioFileRecievedDate("10/16/2015 23:07:53");
//		waveHeader.setAudioFileSize(5000);
//		waveHeader.setClinicId(10492);
//		Demographic demographic = new Demographic();
//		demographic.setPatientAccountno("1000");
//		demographic.setPatientDob("10/16/2015 23:07:53");
//		demographic.setPatientExamDate("10/16/2015 23:07:53");
//		demographic.setPatientGender("Male");
//		demographic.setPatientId(1000);
//		demographic.setPatientLocation("Test Location");
//		demographic.setPatientMrn("1000");
//		demographic.setPatientName("Test Patient Name");
//		demographic.setVisitNumber(1000);
//		waveHeader.setDemographic(demographic);
//		waveHeader.setDictatingPhysicianId(12334);		
//		waveHeader.setDictatingPhysicianName("David Smalling, RPA");
//		waveHeader.setDui(1000);
//		waveHeader.setGlobalDocumentId(1000);
//		waveHeader.setGlobalWaveId(1000);
//		waveHeader.setHospitalId(10491);
//		waveHeader.setJobNumber(1000);
//		waveHeader.setLocalWaveId(1000);
//		waveHeader.setPriorityId(1000);
//		waveHeader.setProcessingLevel(1000);
//		waveHeader.setResend(1000);
//		
//		RouteInfo routeInfo = new RouteInfo();
//		Level level= new Level();
//		level.setId(1);
//		level.setLevelStatusId("5000");
//		level.setSubGroupId("5000");
//		level.setTargetServerId("5000");
//		List<Level> levelList = new ArrayList<Level>();
//		levelList.add(level);
//		level= new Level();
//		level.setId(2);
//		level.setLevelStatusId("2000");
//		level.setSubGroupId("2000");
//		level.setTargetServerId("2000");
//		levelList.add(level);
//		routeInfo.setLevel(levelList);
//		
//		waveHeader.setRouteInfo(routeInfo);
//		waveHeader.setStationId(1000);
//		waveHeader.setStatusId(1000);
//		waveHeader.setTdsTrackingNumber(1000);
//		waveHeader.setTrackingNumber("0000012334-151016-68801-06");
//		waveHeader.setTransGroupId(1000);
//		waveHeader.setTurnAroundTime(1000);
//		waveHeader.setWorktype(1576);
//		
//		metaDataRequestWarpper.setWaveHeader(waveHeader);
//		System.out.println("PORT FOR TESTING"+port);
//		ResponseEntity<AudioResponseWrapper> entity = testRestTemplate.
//				 postForEntity("http://localhost:"+port+"/insertMetaDataInCassandra", metaDataRequestWarpper, AudioResponseWrapper.class);		
//        assertEquals("success", entity.getBody().getResponseCode());   
//	}
//	
//	@Test
//	public void insertAudioFileinAmazonS3() throws Exception {
//		System.out.println("Hitting - insertAudioFileinAmazonS3");
//		TestRestTemplate restTemplate = new TestRestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		 headers.set("fileName", "test.wav");
//		 HttpEntity<byte[]> entity = new HttpEntity<byte[]>(new byte[100], headers);
//		 //URI location = restTemplate.postForLocation("http://example.com", entity);
//		 ResponseEntity<AudioResponseWrapper> response = restTemplate.exchange("http://localhost:"+port+"/pushAudioToAmazonS3",
//			      HttpMethod.PUT,
////			      new HttpEntity<byte[]>(headers),
//			      entity,
//			      AudioResponseWrapper.class);
//		 
//		assertEquals("success", response.getBody().getResponseCode());
//	}
//}

