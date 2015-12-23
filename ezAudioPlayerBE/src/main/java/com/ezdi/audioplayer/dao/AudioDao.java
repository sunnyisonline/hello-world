package com.ezdi.audioplayer.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezdi.audioplayer.audioResponse.AudioServiceResponse;
import com.ezdi.audioplayer.bean.Audio;
import com.ezdi.audioplayer.bean.AudioPhysicianMappingBean;
import com.ezdi.audioplayer.bean.PhysicianS3AudioPathBean;
import com.ezdi.audioplayer.metadata.request.bean.MetaDataRequestWrapper;

public interface AudioDao {

	public List<Audio> fetchAllAudioFiles();

	public Audio fetchAudioFilePath(String audioFileId) throws Exception;
	
	// Meta Data - Sunny
	
	public boolean insertMetaDatatoCassandra(MetaDataRequestWrapper metaDataRequestWarpper) throws Exception ;
	
	public Audio fetchAudioFilePathById(String audioFileId);
	
	public AudioPhysicianMappingBean getPhysicianIdByAudioId(String audioId) throws Exception;

	public PhysicianS3AudioPathBean getAudioPathByPhysicianId(int physicianId) throws Exception;
	
	public PhysicianS3AudioPathBean getDocPathByPhysicianId(int physicianId) throws Exception ;
	
	public boolean updateAudioWavePathAfterS3UploadSuccess(String bucketName, String audioId) throws Exception;

	// Sprint 3
	public boolean isRecordExistsInDocMaster(String id) throws Exception;
}
