package com.ezdi.audioplayer.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezdi.audioplayer.audioResponse.AudioServiceResponse;
import com.ezdi.audioplayer.bean.Audio;
import com.ezdi.audioplayer.bean.AudioPhysicianMappingBean;
import com.ezdi.audioplayer.bean.PhysicianS3AudioPathBean;
import com.ezdi.audioplayer.metadata.request.bean.MetaDataRequestWrapper;

public interface AudioService {

	public List<Audio> fetchAllAudioFiles();

	public AudioServiceResponse deleteAudioFile(String fileName);

	public AudioServiceResponse fetchAudioFilePath(String audioFileId);

	//Meta Data - Sunny
	
	public boolean insertMetaDatatoCassandra(MetaDataRequestWrapper metaDataRequestWarpper) throws Exception;
	
	public boolean uploadAudioToS3(String waveBucket, String docBucket, String fileNameOnS3, byte[] data) throws Exception ;	
	
	public boolean prepareAmazonS3AudioFolderStructure(String bucketName) throws Exception;
	
	public int getPhysicianIdByAudioId(String audioId) throws Exception ;
	
	public String getAudioPathByPhysicianId(int physicianId) throws Exception;
	
	public String getDocPathByPhysicianId(int physicianId) throws Exception; 
	
	public boolean updateAudioWavePathAfterS3UploadSuccess(String bucketName, String audioId) throws Exception;	
}
