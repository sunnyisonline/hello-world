package com.ezdi.audioplayer.daoimpl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.ezdi.s3connection.service.AmazonS3Service;
import com.ezdi.audioplayer.bean.Audio;
import com.ezdi.audioplayer.bean.AudioPhysicianMappingBean;
import com.ezdi.audioplayer.bean.PhysicianS3AudioPathBean;
import com.ezdi.audioplayer.dao.AudioDao;
import com.ezdi.audioplayer.logger.LoggerUtil;
import com.ezdi.audioplayer.metadata.request.bean.Level;
import com.ezdi.audioplayer.metadata.request.bean.MetaDataRequestWrapper;
import com.ezdi.audioplayer.solr.document.DocumentMasterDTO;
import com.ezdi.audioplayer.util.CassandraUtil;
import com.ezdi.cassandra.impl.CassandraService;

@Repository
public class AudioDaoImpl extends WebMvcConfigurerAdapter implements AudioDao {
	private static final Logger logger = Logger.getLogger(AudioDaoImpl.class);

	@Autowired
	@Qualifier("SolrTemplate")
	SolrTemplate solrTemplate;

	@Value("${cassandra.audio.keyspace}")
	private String audioKeySpace;

	@Value("${cassandra.document.keyspace}")
	private String documentKeySpace;

	@Value("${cassandra.master.keyspace}")
	private String masterKeySpace;

	@Autowired
	private AmazonS3Service amazonS3Service;

	@Value("${audio.wave.file.path}")
	private String audioWaveFilePath;

	@Value("${audio.file.path}")
	private String audioFilePath;

	@Autowired
	@Qualifier(value = "cassandraService")
	private CassandraOperations cassandraService;

	public Audio fetchAudioFilePath(String audioFileId) throws Exception {
		Audio audio = null;
		String cql = "select * from " + audioKeySpace
				+ ".ez_audio_master where audio_id='" + audioFileId + "'";
		logger.info(LoggerUtil.getMessage("fetch path from casandra" + cql));
		try {
			audio = cassandraService.selectOne(cql, Audio.class);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(LoggerUtil.getMessage("Id is not present in casandra"));
			throw new Exception("Id is not present in casandra");
		}
		return audio;
	}

	// fetch all audio files
	@Override
	public List<Audio> fetchAllAudioFiles() {
		List audioList = null;
		try {
			audioList = cassandraService.selectAll(Audio.class);
			return audioList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return audioList;

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		try {
			logger.info(LoggerUtil.getMessage("Serving static content from "
					+ audioFilePath));
			registry.addResourceHandler("/**").addResourceLocations(
					"file:///" + audioFilePath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Meta Data - Sunny

	public boolean insertMetaDatatoCassandra(
			MetaDataRequestWrapper metaDataRequestWarpper) throws Exception {
		cassandraService
				.execute(insertMetaDatatoAudioMaster(metaDataRequestWarpper));
		return true;
	}

	public boolean isRecordExistsInAudioMaster(String id) throws Exception {
		String cql = null;
		try {
			cql = "select * from " + audioKeySpace
					+ ".ez_audio_master where audio_id = '" + id + "'";
			logger.info(LoggerUtil.getMessage("CQL - " + cql));
			if (cassandraService.query(cql).all().size() > 0) {
				return true;
			}
		} catch (Exception p) {
			p.printStackTrace();
			throw new Exception(
					"Error - Fail to Check Audio Master Record Already Exists");
		}
		return false;
	}

	public boolean isRecordExistsInDocMaster(String id) throws Exception {
		String cql = null;
		try {
			cql = "select * from " + documentKeySpace
					+ ".ez_document_master where " + "audio_id = '" + id
					+ "' and document_id = '" + id + "'";
			logger.info(LoggerUtil
					.getMessage("\n\n isRecordExistsInDocMaster CQL - " + cql));
			if (cassandraService.query(cql).all().size() > 0) {
				return true;
			}
		} catch (Exception p) {
			p.printStackTrace();
			throw new Exception(
					"Error - Fail to Check Document Master Record Already Exists");
		}
		return false;
	}

	// Audio Master
	public String insertMetaDatatoAudioMaster(
			MetaDataRequestWrapper metaDataRequestWarpper) throws Exception {
		String audioPath = null;
		StringBuilder query = new StringBuilder();
		String audioId = metaDataRequestWarpper.getWaveHeader()
				.getAudioFileName();

		if (!audioId.contains("."))
			logger.info(LoggerUtil.getMessage("File Name=" + audioId));
		else {
			audioId = audioId.substring(0, audioId.lastIndexOf("."));
			// Because extension is always after the last '.'
			logger.info(LoggerUtil.getMessage("File Name=" + audioId));
		}

		boolean audioRecordsExists = isRecordExistsInAudioMaster(audioId);
		logger.info(LoggerUtil.getMessage("audioRecordsExists"
				+ audioRecordsExists));

		query.append("insert into " + audioKeySpace + ".ez_audio_master (");
		if (!audioRecordsExists) {
			query.append("status_id  , status ,  ");
		}
		query.append("audio_id, attending_physician_id, attending_physician_login_id, attending_physician_mnemonic, attending_physician_name,"
				+ "audio_file_codec , audio_file_dictation_date, audio_file_dictation_date_gmt, audio_file_name,  "
				+ "audio_file_play_time_in_sec, audio_file_priority  ,  audio_file_recieved_date  ,  "
				+ "audio_file_recieved_time  ,  audio_file_size  ,  clinic_id  ,  created_date  ,  dictating_physician_id  ,  "
				+ "dictating_physician_login_id  ,  dictating_physician_mnemonic  ,  dictating_physician_name  ,  dui  ,  "
				+ "global_document_id  , global_wave_id  ,  hospital_id  ,  hospital_name  ,  job_number  ,  "
				+ "local_wave_id  ,  patient_accountno  ,  patient_dob  ,  patient_exam_date  ,  patient_gender  ,  patient_id  ,  "
				+ "patient_location  ,  patient_mrn  ,  patient_name  ,  patient_ssn  ,"
				+ "priority_id  ,  processing_level  ,  resend  ,  station_id  ,  tat_achieved  ,  tds_tracking_number  ,  tracking_number  ,  trans_group_id  ,  "
				+ "turn_around_time  ,  updated_by  ,  updated_by_id  ,  updated_date  ,  visit_number  ,  audio_intermediate_id, "
				+ "worktype ,  level_status_id   ,  subgroup_id  ,  "
				+ "target_server_id ) values (");

		if (!audioRecordsExists) {
			query.append(metaDataRequestWarpper.getWaveHeader().getStatusId());
			query.append(",");
			query.append(processNullString(getStatusByStatusId(metaDataRequestWarpper
					.getWaveHeader().getStatusId())));
			query.append(",");
		}

		query.append(processNullString(audioId));
		query.append(",");
		query.append(audioPath); // Attending physicial Id unknown *****
		query.append(",");
		query.append(audioPath);
		query.append(",");
		query.append(audioPath);
		query.append(",");
		query.append(audioPath);
		query.append(","); // Attending but dictating *****
		query.append(metaDataRequestWarpper.getWaveHeader().getAudioFileCodec());
		query.append(",");

		query.append(processNullString(dateConverter(metaDataRequestWarpper
				.getWaveHeader().getAudioFileDictationDate())));
		query.append(",");
		query.append(processNullString(dateConverter(metaDataRequestWarpper
				.getWaveHeader().getAudioFileDictationDateGMT())));
		query.append(",");

		query.append(processNullString(metaDataRequestWarpper.getWaveHeader()
				.getAudioFileName()));
		// query.append(",");
		// query.append(audioPath);
		// query.append(",");
		// query.append(audioPath);
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader()
				.getAudioFilePlayTimeInSec());
		query.append(",");
		query.append(audioPath); // audio_file_priority unknown
		query.append(",");
		query.append(processNullString(dateConverter(metaDataRequestWarpper
				.getWaveHeader().getAudioFileRecievedDate())));
		query.append(",");
		query.append(audioPath); // timestamp audio_file_recieved_time unknown
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getAudioFileSize());
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getClinicId());
		query.append(",");
		query.append(processNullString((new java.sql.Timestamp(
				new java.util.Date().getTime())))); // timestamp - created_date
													// unknown
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader()
				.getDictatingPhysicianId());
		query.append(",");
		query.append(audioPath);
		query.append(",");
		query.append(audioPath);
		query.append(",");
		query.append(processNullString(metaDataRequestWarpper.getWaveHeader()
				.getDictatingPhysicianName()));
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getDui());
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader()
				.getGlobalDocumentId());
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getGlobalWaveId());
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getHospitalId());
		query.append(",");
		query.append("'"
				+ getHospitalByHospitalId(metaDataRequestWarpper
						.getWaveHeader().getHospitalId()) + "'");
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getJobNumber());
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getLocalWaveId());
		query.append(",");
		query.append(processNullString(metaDataRequestWarpper.getWaveHeader()
				.getDemographic().getPatientAccountno()));
		query.append(",");
		query.append(processNullString(dateConverter(metaDataRequestWarpper
				.getWaveHeader().getDemographic().getPatientDob())));
		query.append(",");
		query.append(processNullString(dateConverter(metaDataRequestWarpper
				.getWaveHeader().getDemographic().getPatientExamDate())));
		query.append(",");
		query.append(processNullString(metaDataRequestWarpper.getWaveHeader()
				.getDemographic().getPatientGender()));
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getDemographic()
				.getPatientId());
		query.append(",");
		query.append(processNullString(metaDataRequestWarpper.getWaveHeader()
				.getDemographic().getPatientLocation()));
		query.append(",");
		query.append(processNullString(metaDataRequestWarpper.getWaveHeader()
				.getDemographic().getPatientMrn()));
		query.append(",");
		query.append(processNullString(metaDataRequestWarpper.getWaveHeader()
				.getDemographic().getPatientName()));
		query.append(",");
		query.append(audioPath);
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getPriorityId());
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader()
				.getProcessingLevel());
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getResend());
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getStationId());

		query.append(",");
		query.append(audioPath); // "'tat_achieved unknown'"
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader()
				.getTdsTrackingNumber());
		query.append(",");
		query.append(processNullString(metaDataRequestWarpper.getWaveHeader()
				.getTrackingNumber()));
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getTransGroupId());
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getTurnAroundTime());
		query.append(",");
		query.append(audioPath);
		query.append(",");
		query.append(audioPath); // "'updated_by_id unknown'"
		query.append(",");
		query.append(processNullString(new java.sql.Timestamp(
				new java.util.Date().getTime())));
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getDemographic()
				.getVisitNumber());
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader()
				.getAudioIdInIntermediateDB());
		query.append(",");
		query.append(metaDataRequestWarpper.getWaveHeader().getWorktype());
		query.append(",");

		String query1 = query.toString();
		List<Level> levelList = metaDataRequestWarpper.getWaveHeader()
				.getRouteInfo().getLevel();
		String finalQuery = null;
		StringBuilder statusId = new StringBuilder();
		StringBuilder subGroupId = new StringBuilder();
		StringBuilder targetServerId = new StringBuilder();

		statusId.append("{");
		subGroupId.append("{");
		targetServerId.append("{");

		for (int i = 0; i < levelList.size(); i++) {
			Level ll = levelList.get(i);
			statusId.append(ll.getId());
			statusId.append(":");
			String levelStatusId = processNullString(ll.getLevelStatusId());
			statusId.append(levelStatusId == null ? "''" : levelStatusId);

			if (i < levelList.size() - 1) {
				statusId.append(",");
			}
			subGroupId.append(ll.getId());
			subGroupId.append(":");
			String groupId = processNullString(ll.getSubGroupId());
			subGroupId.append(groupId == null ? "''" : groupId);

			if (i < levelList.size() - 1) {
				subGroupId.append(",");
			}
			targetServerId.append(ll.getId());
			targetServerId.append(":");
			String serverId = processNullString(ll.getTargetServerId());
			targetServerId.append(serverId == null ? "''" : serverId);

			if (i < levelList.size() - 1) {
				targetServerId.append(",");
			}
		}

		statusId.append("},");
		subGroupId.append("},");
		targetServerId.append("})");

		finalQuery = query.append(statusId).append(subGroupId)
				.append(targetServerId).toString();

		logger.info(LoggerUtil.getMessage("\n Audio Master FINAL Query - "
				+ finalQuery));

		// cassandraService.insert(finalQuery);

		// cassandraService.insert(query.toString());
		return finalQuery;
	}

	// Audio Log
	public String insertMetaDatatoAudioLog(HashMap map) throws Exception {
		StringBuilder query = new StringBuilder();
		String audioId = (String) map.get("audio_id");
		boolean existsInDocumentMaster = isRecordExistsInDocMaster(audioId);
		query.append("insert into " + audioKeySpace + ".ez_audio_log ( ");
		if (!existsInDocumentMaster) {
			query.append("status_id, status, ");
		}
		query.append("audio_id, attending_physician_id, attending_physician_login_id, attending_physician_mnemonic, attending_physician_name,"
				+ "audio_file_codec , audio_file_dictation_date, audio_file_dictation_date_gmt, audio_file_name, audio_file_original_path, "
				+ "audio_file_path , audio_file_play_time_in_sec, audio_file_priority  ,  audio_file_recieved_date  ,  "
				+ "audio_file_recieved_time  ,  audio_file_size  ,  clinic_id  ,  created_date  ,  dictating_physician_id  ,  "
				+ "dictating_physician_login_id  ,  dictating_physician_mnemonic  ,  dictating_physician_name  ,  dui  ,  "
				+ "global_document_id  , global_wave_id  ,  hospital_id  ,  hospital_name  ,  job_number  ,   "
				+ "local_wave_id  ,  patient_accountno  ,  patient_dob  ,  patient_exam_date  ,  patient_gender  ,  patient_id  ,  "
				+ "patient_location  ,  patient_mrn  ,  patient_name  ,  patient_ssn  ,"
				+ "priority_id  ,  processing_level  ,  resend  ,  station_id  ,   tat_achieved  ,  tds_tracking_number  ,  tracking_number  ,  trans_group_id  ,  "
				+ "turn_around_time  ,  updated_by  ,  updated_by_id  ,  updated_date  ,  visit_number  ,  audio_file_mp3_path, audio_intermediate_id, "
				+ "worktype ,  level_status_id   ,  subgroup_id  ,  "
				+ "target_server_id ) " + "values (");
		if (!existsInDocumentMaster) {
			query.append(map.get("status_id"));
			query.append(",");
			query.append(processNullString(getStatusByStatusId((Integer) map
					.get("status_id"))));
			query.append(",");
		}
		query.append(processNullString(audioId));
		query.append(",");
		query.append(map.get("attending_physician_id"));
		query.append(",");
		query.append(map.get("attending_physician_login_id"));
		query.append(",");
		query.append(map.get("attending_physician_mnemonic"));
		query.append(",");
		query.append(map.get("attending_physician_name"));
		query.append(","); // Attending but dictating *****
		query.append(map.get("audio_file_codec"));
		query.append(",");

		query.append(processNullString(map.get("audio_file_dictation_date")));
		query.append(",");
		query.append(processNullString(map.get("audio_file_dictation_date_gmt")));
		query.append(",");

		query.append(processNullString(map.get("audio_file_name")));
		query.append(",");
		query.append(processNullString(map.get("audio_file_original_path")));
		query.append(",");
		query.append(processNullString(map.get("audio_file_path")));
		query.append(",");
		query.append(map.get("audio_file_play_time_in_sec"));
		query.append(",");
		query.append(map.get("audio_file_priority")); // audio_file_priority
														// unknown
		query.append(",");
		query.append(processNullString(map.get("audio_file_recieved_date")));
		query.append(",");
		query.append(processNullString(map.get("audio_file_recieved_time")));
		query.append(",");
		query.append(map.get("audio_file_size"));
		query.append(",");
		query.append(map.get("clinic_id"));
		query.append(",");
		query.append(processNullString((new java.sql.Timestamp(
				new java.util.Date().getTime())))); // timestamp - created_date
													// unknown
		query.append(",");
		query.append(map.get("dictating_physician_id"));
		query.append(",");
		query.append(map.get("dictating_physician_login_id"));
		query.append(",");
		query.append(map.get("dictating_physician_mnemonic"));
		query.append(",");
		query.append(processNullString(map.get("dictating_physician_name")));
		query.append(",");
		query.append(map.get("dui"));
		query.append(",");
		query.append(map.get("global_document_id"));
		query.append(",");
		query.append(map.get("global_wave_id"));
		query.append(",");
		query.append(map.get("hospital_id"));
		query.append(",");
		query.append("'"
				+ getHospitalByHospitalId((Integer) map.get("hospital_id"))
				+ "'");
		query.append(",");
		query.append(map.get("job_number"));

		query.append(",");
		query.append(map.get("local_wave_id"));
		query.append(",");
		query.append(processNullString(map.get("patient_accountno")));
		query.append(",");
		query.append(processNullString(map.get("patient_dob")));
		query.append(",");
		query.append(processNullString(map.get("patient_exam_date")));
		query.append(",");
		query.append(processNullString(map.get("patient_gender")));
		query.append(",");
		query.append(map.get("patient_id"));
		query.append(",");
		query.append(processNullString(map.get("patient_location")));
		query.append(",");
		query.append(processNullString(map.get("patient_mrn")));
		query.append(",");
		query.append(processNullString(map.get("patient_name")));
		query.append(",");
		query.append(map.get("patient_ssn"));
		query.append(",");
		query.append(map.get("priority_id"));
		query.append(",");
		query.append(map.get("processing_level"));
		query.append(",");
		query.append(map.get("resend"));
		query.append(",");
		query.append(map.get("station_id"));
		query.append(",");
		query.append(map.get("tat_achieved")); // "'tat_achieved unknown'"
		query.append(",");
		query.append(map.get("tds_tracking_number"));
		query.append(",");
		query.append(processNullString(map.get("tracking_number")));
		query.append(",");
		query.append(map.get("trans_group_id"));
		query.append(",");
		query.append(map.get("turn_around_time"));
		query.append(",");
		query.append(map.get("updated_by"));
		query.append(",");
		query.append(map.get("updated_by_id"));
		query.append(",");
		query.append(processNullString(new java.sql.Timestamp(
				new java.util.Date().getTime())));
		query.append(",");
		query.append(map.get("visit_number"));
		query.append(",");
		query.append(processNullString(map.get("audio_file_mp3_path")));
		query.append(",");
		query.append(map.get("audio_intermediate_id"));
		query.append(",");
		query.append(map.get("worktype"));
		query.append(",");
		query.append(cassandraMapBuilder(map.get("level_status_id")));
		query.append(",");
		query.append(cassandraMapBuilder(map.get("subgroup_id")));
		query.append(",");
		query.append(cassandraMapBuilder(map.get("target_server_id")));
		query.append(")");

		logger.info(LoggerUtil
				.getMessage("\n Audio Log FINAL Query - " + query));

		// cassandraService.insert(finalQuery);

		return query.toString();
	}

	// Document Master
	public String insertMetaDatatoDocumentMaster(HashMap map) throws Exception {
		StringBuilder query = new StringBuilder();
		String audioId = (String) map.get("audio_id");
		boolean existsInDocumentMaster = isRecordExistsInDocMaster(audioId);

		query.append("insert into  " + documentKeySpace
				+ ".ez_document_master (");
		if (!existsInDocumentMaster) {
			query.append("document_current_status_id, document_current_status, ");
		}
		query.append("audio_id , document_id , document_name, attending_physician_id , attending_physician_login_id , attending_physician_mnemonic , "
				+ "attending_physician_name , audio_file_codec , audio_file_dictation_date , audio_file_name , audio_file_path , "
				+ "audio_file_play_time_in_sec , audio_file_size ,  created_date , dictating_physician_id , dictating_physician_login_id , "
				+ "dictating_physician_mnemonic , dictating_physician_name , global_document_id , hospital_id , hospital_name , "
				+ "patient_accountno , patient_dob  , patient_gender , patient_mrn , patient_ssn , tat_achieved , tat_change_request_flag, tracking_number ,  "
				+ "turn_around_time , updated_by , updated_by_id , updated_date , visit_number , worktype,"
				+ "worktype_desc, " + "document_path" + " ) values (");

		if (!existsInDocumentMaster) {
			query.append(map.get("status_id"));
			query.append(",");
			query.append(processNullString(getStatusByStatusId((Integer) map
					.get("status_id"))));
			query.append(",");
		}
		query.append(processNullString(audioId));
		query.append(",");
		query.append(processNullString(audioId));
		query.append(",");
		query.append(processNullString("" + map.get("audio_id") + ".doc"));
		query.append(",");
		query.append(map.get("attending_physician_id"));
		query.append(",");
		query.append(map.get("attending_physician_login_id"));
		query.append(",");
		query.append(map.get("attending_physician_mnemonic"));
		query.append(",");
		query.append(map.get("attending_physician_name"));
		query.append(","); // Attending but dictating *****
		query.append(map.get("audio_file_codec"));
		query.append(",");

		query.append(processNullString(map.get("audio_file_dictation_date")));
		query.append(",");

		query.append(processNullString(map.get("audio_file_name")));
		query.append(",");
		query.append(processNullString(map.get("audio_file_path")));
		query.append(",");
		query.append(map.get("audio_file_play_time_in_sec"));
		query.append(",");
		query.append(map.get("audio_file_size"));
		query.append(",");
		query.append(processNullString((new java.sql.Timestamp(
				new java.util.Date().getTime())))); // timestamp - created_date
													// unknown
		query.append(",");
		query.append(map.get("dictating_physician_id"));
		query.append(",");
		query.append(map.get("dictating_physician_login_id"));
		query.append(",");
		query.append(map.get("dictating_physician_mnemonic"));
		query.append(",");
		query.append(processNullString(map.get("dictating_physician_name")));
		query.append(",");
		query.append(map.get("global_document_id"));
		query.append(",");
		query.append(map.get("hospital_id"));
		query.append(",");
		query.append("'"
				+ getHospitalByHospitalId((Integer) map.get("hospital_id"))
				+ "'");
		query.append(",");
		query.append(processNullString(map.get("patient_accountno")));
		query.append(",");
		query.append(processNullString(map.get("patient_dob")));
		query.append(",");
		query.append(processNullString(map.get("patient_gender")));
		query.append(",");
		query.append(processNullString(map.get("patient_mrn")));
		query.append(",");
		query.append(map.get("patient_ssn"));
		query.append(",");
		query.append(map.get("tat_achieved"));
		query.append(",");
		query.append("'N'");
		query.append(",");
		query.append(processNullString(map.get("tracking_number")));
		query.append(",");
		query.append(map.get("turn_around_time"));
		query.append(",");
		query.append(map.get("updated_by"));
		query.append(",");
		query.append(map.get("updated_by_id"));
		query.append(",");
		query.append(processNullString(new java.sql.Timestamp(
				new java.util.Date().getTime())));
		query.append(",");
		query.append(map.get("visit_number"));
		query.append(",");
		query.append(map.get("worktype"));
		query.append(",");
		query.append(processNullString(getWorkTypeDescByWorktype((Integer) map
				.get("worktype"))));

		query.append(",");
		String audioPath = (String) map.get("audio_file_path");
		query.append(processNullString(audioPath.replace("wave", "docs")));

		query.append(")");

		logger.info(LoggerUtil.getMessage("\n Document Master FINAL Query - "
				+ query));

		return query.toString();
	}

	// Document Master Solr
	public int insertMetaDatatoDocumentMasterSolr(HashMap map) throws Exception {
		String audioId = (String) map.get("audio_id");
		boolean existsInDocumentMaster = isRecordExistsInDocMaster(audioId);
		DocumentMasterDTO documentMasterDTO = new DocumentMasterDTO();

		if (!existsInDocumentMaster) {
			logger.info(LoggerUtil.getMessage("New Record"));
			documentMasterDTO.setDocumentCurrentStatusId((Integer) map
					.get("status_id")); 
			documentMasterDTO
					.setDocumentCurrentStatus(getStatusByStatusId((Integer) map
							.get("status_id"))); 
		} else {
			logger.info(LoggerUtil.getMessage("Existing Record"));
			documentMasterDTO
					.setDocumentCurrentStatusId(getStatusIdByDocumentId(audioId)); 
			documentMasterDTO
					.setDocumentCurrentStatus(getStatusByStatusId(documentMasterDTO
							.getDocumentCurrentStatusId())); 
		}

		documentMasterDTO.setId(audioId);
		documentMasterDTO.setAudioId(audioId);
		documentMasterDTO.setDocumentId(audioId);
		documentMasterDTO.setDocumentName(audioId + ".doc");
		documentMasterDTO.setAttendingPhysicianId((Integer) map
				.get("attending_physician_id"));
		documentMasterDTO.setAttendingPhysicianLoginId((String) map
				.get("attending_physician_login_id"));
		documentMasterDTO.setAttendingPhysicianMnemonic((String) map
				.get("attending_physician_mnemonic"));
		documentMasterDTO.setAttendingPhysicianName((String) map
				.get("attending_physician_name"));
		documentMasterDTO.setAudioFileCodec((Integer) map
				.get("audio_file_codec"));
		documentMasterDTO.setAudioFileDictationDate((java.sql.Timestamp) map
				.get("audio_file_dictation_date"));
		documentMasterDTO.setAudioFileName((String) map.get("audio_file_name"));
		documentMasterDTO.setAudioFilePath((String) map.get("audio_file_path"));
		documentMasterDTO.setAudioFilePlayTimeInSec((Integer) map
				.get("audio_file_play_time_in_sec"));
		documentMasterDTO
				.setAudioFileSize((Integer) map.get("audio_file_size"));
		documentMasterDTO.setCreatedDate(new java.sql.Timestamp(
				new java.util.Date().getTime()));
		documentMasterDTO.setDictatingPhysicianId((Integer) map
				.get("dictating_physician_id"));
		documentMasterDTO.setDictatingPhysicianLoginId((String) map
				.get("dictating_physician_login_id"));
		documentMasterDTO.setDictatingPhysicianMnemonic((String) map
				.get("dictating_physician_mnemonic"));
		documentMasterDTO.setDictatingPhysicianName((String) map
				.get("dictating_physician_name"));
		documentMasterDTO.setGlobalDocumentId((Integer) map
				.get("global_document_id"));
		documentMasterDTO.setHospitalId((Integer) map.get("hospital_id"));
		documentMasterDTO.setHospitalName(getHospitalByHospitalId((Integer) map
				.get("hospital_id")));
		documentMasterDTO.setPatientAccountno((String) map
				.get("patient_accountno"));
		documentMasterDTO.setPatientDob((java.sql.Timestamp) map
				.get("patient_dob"));
		documentMasterDTO.setPatientGender((String) map.get("patient_gender"));
		documentMasterDTO.setPatientMrn((String) map.get("patient_mrn"));
		documentMasterDTO.setPatientSsn((String) map.get("patient_ssn"));
		documentMasterDTO.setTatAchieved((Integer) map.get("tat_achieved"));
		documentMasterDTO
				.setTrackingNumber((String) map.get("tracking_number"));
		documentMasterDTO.setTurnAroundTime((Integer) map
				.get("turn_around_time"));
		documentMasterDTO.setUpdatedBy((String) map.get("updated_by"));
		documentMasterDTO.setUpdatedById((Integer) map.get("updated_by_id"));
		documentMasterDTO.setUpdatedDate(new java.sql.Timestamp(
				new java.util.Date().getTime()));
		documentMasterDTO.setVisitNumber((Integer) map.get("visit_number"));
		documentMasterDTO.setWorktype((Integer) map.get("worktype"));
		documentMasterDTO
				.setWorktypeDesc(getWorkTypeDescByWorktype((Integer) map
						.get("worktype")));
		String audioPath = (String) map.get("audio_file_path");
		documentMasterDTO.setDocumentPath(audioPath.replace("wave", "docs"));

		UpdateResponse res = solrTemplate.getSolrServer().add(
				solrTemplate.convertBeanToSolrInputDocument(documentMasterDTO));
		solrTemplate.getSolrServer().commit();

		logger.info(LoggerUtil.getMessage("add document status - "
				+ res.getStatus()));
		if (res.getStatus() != 0) {
			throw new Exception(
					"Error - Insert Document Master Document In Solr");
		}
		return res.getStatus();
	}

	// Document Log
	public String insertMetaDatatoDocumentLog(HashMap map) throws Exception {
		StringBuilder query = new StringBuilder();
		String audioId = (String) map.get("audio_id");
		boolean existsInDocumentMaster = isRecordExistsInDocMaster(audioId);

		query.append("insert into  " + documentKeySpace + ".ez_document_log (");
		if (!existsInDocumentMaster) {
			query.append("document_current_status_id, document_current_status, ");
		}
		query.append("audio_id , document_id , document_name, attending_physician_id , attending_physician_login_id , attending_physician_mnemonic , "
				+ "attending_physician_name , audio_file_codec , audio_file_dictation_date , audio_file_name , audio_file_path , "
				+ "audio_file_play_time_in_sec , audio_file_size ,  created_date , dictating_physician_id , dictating_physician_login_id , "
				+ "dictating_physician_mnemonic , dictating_physician_name , global_document_id , hospital_id , hospital_name , "
				+ "patient_accountno , patient_dob  , patient_gender , patient_mrn , patient_ssn , tat_achieved , tat_change_request_flag, tracking_number ,  "
				+ "turn_around_time , updated_by , updated_by_id , updated_date , visit_number , worktype,"
				+ "worktype_desc, " + "document_path" + " ) values (");

		if (!existsInDocumentMaster) {
			query.append(map.get("status_id"));
			query.append(",");
			query.append(processNullString(getStatusByStatusId((Integer) map
					.get("status_id"))));
			query.append(",");
		}
		query.append(processNullString(audioId));
		query.append(",");
		query.append(processNullString(audioId));
		query.append(",");
		query.append(processNullString("" + map.get("audio_id") + ".doc"));
		query.append(",");
		query.append(map.get("attending_physician_id"));
		query.append(",");
		query.append(map.get("attending_physician_login_id"));
		query.append(",");
		query.append(map.get("attending_physician_mnemonic"));
		query.append(",");
		query.append(map.get("attending_physician_name"));
		query.append(","); // Attending but dictating *****
		query.append(map.get("audio_file_codec"));
		query.append(",");

		query.append(processNullString(map.get("audio_file_dictation_date")));
		query.append(",");

		query.append(processNullString(map.get("audio_file_name")));
		query.append(",");
		query.append(processNullString(map.get("audio_file_path")));
		query.append(",");
		query.append(map.get("audio_file_play_time_in_sec"));
		query.append(",");
		query.append(map.get("audio_file_size"));
		query.append(",");
		query.append(processNullString((new java.sql.Timestamp(
				new java.util.Date().getTime())))); // timestamp - created_date
													// unknown
		query.append(",");
		query.append(map.get("dictating_physician_id"));
		query.append(",");
		query.append(map.get("dictating_physician_login_id"));
		query.append(",");
		query.append(map.get("dictating_physician_mnemonic"));
		query.append(",");
		query.append(processNullString(map.get("dictating_physician_name")));
		query.append(",");
		query.append(map.get("global_document_id"));
		query.append(",");
		query.append(map.get("hospital_id"));
		query.append(",");
		query.append("'"
				+ getHospitalByHospitalId((Integer) map.get("hospital_id"))
				+ "'");
		query.append(",");
		query.append(processNullString(map.get("patient_accountno")));
		query.append(",");
		query.append(processNullString(map.get("patient_dob")));
		query.append(",");
		query.append(processNullString(map.get("patient_gender")));
		query.append(",");
		query.append(processNullString(map.get("patient_mrn")));
		query.append(",");
		query.append(map.get("patient_ssn"));
		query.append(",");
		query.append(map.get("tat_achieved"));
		query.append(",");
		query.append("'N'");
		query.append(",");
		query.append(processNullString(map.get("tracking_number")));
		query.append(",");
		query.append(map.get("turn_around_time"));
		query.append(",");
		query.append(map.get("updated_by"));
		query.append(",");
		query.append(map.get("updated_by_id"));
		query.append(",");
		query.append(processNullString(new java.sql.Timestamp(
				new java.util.Date().getTime())));
		query.append(",");
		query.append(map.get("visit_number"));
		query.append(",");
		query.append(map.get("worktype"));
		query.append(",");
		query.append(processNullString(getWorkTypeDescByWorktype((Integer) map
				.get("worktype"))));

		query.append(",");
		String audioPath = (String) map.get("audio_file_path");
		query.append(processNullString(audioPath.replace("wave", "docs")));

		query.append(")");

		logger.info(LoggerUtil.getMessage("\n Document Master FINAL Query - "
				+ query));

		return query.toString();
	}

	public String insertMetaDataToDocPhysicianLookup(HashMap map)
			throws Exception {
		StringBuilder query = new StringBuilder();
		String audioId = (String) map.get("audio_id");
		int physicianId = (Integer) map.get("dictating_physician_id");
		int statusId = (Integer) map.get("status_id");
		query.append("");
		boolean recordExistsInDocMaster = isRecordExistsInDocMaster(audioId);
		if (!recordExistsInDocMaster) {
			logger.info(LoggerUtil.getMessage("Inserting into Lookup table"));
			query.append("insert into  " + documentKeySpace
					+ ".ez_document_physician_lookup ( "
					+ "dictating_physician_id, document_current_status_id, "
					+ "document_id) " + "values(" + physicianId + ","
					+ statusId + "," + processNullString(audioId) + ")");
		}
		logger.info(LoggerUtil
				.getMessage("insertMetaDataToDocPhysicianLookup - " + query));
		return query.toString();
	}

	public java.sql.Timestamp dateConverter(String input) throws Exception {
		java.sql.Timestamp sqlDate = null;
		try {
			logger.info(LoggerUtil.getMessage("Input DateConverter - " + input));
			if (input == null || input.equals("")
					|| input.toString().equals("null")) {
				logger.info(LoggerUtil.getMessage("returning null"));
				return null;
			}
			SimpleDateFormat format = new SimpleDateFormat(
					"MM/dd/yyyy HH:mm:SS");
			java.util.Date parsed = format.parse(input);
			sqlDate = new java.sql.Timestamp(parsed.getTime());
			logger.info(LoggerUtil.getMessage("OUT - " + sqlDate));
		} catch (Exception p) {
			throw new Exception("Error : Invalid Date Format.");
		}
		return sqlDate;
	}

	public String processNullString(Object value) {
		if (value == null || value.equals("") || value.equals("null")) {
			return null;
		} else {
			return "'" + value + "'";
		}
	}

	public Audio fetchAudioFilePathById(String audioFileId) {
		Audio audio = null;
		String cql = "select * from " + audioKeySpace
				+ ".ez_audio_master where audio_id='" + audioFileId + "'";
		logger.info(LoggerUtil.getMessage("fetch path from casandra" + cql));
		try {
			audio = cassandraService.selectOne(cql, Audio.class);

		} catch (Exception e) {
			logger.info(LoggerUtil.getMessage("Id is not present in casandra"));
		}
		return audio;
	}

	// fetch audio file path from casandra
	public AudioPhysicianMappingBean getPhysicianIdByAudioId(String audioId)
			throws Exception {
		AudioPhysicianMappingBean audioPhysicianMappingBean = null;
		String cql = "select * from " + audioKeySpace
				+ ".ez_audio_master where audio_id='" + audioId + "'";
		logger.info(LoggerUtil.getMessage("Query - " + cql));
		try {
			audioPhysicianMappingBean = cassandraService.selectOne(cql,
					AudioPhysicianMappingBean.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"Error - Failed to fetch Physician Id by Audio Id");
		}
		return audioPhysicianMappingBean;
	}

	public PhysicianS3AudioPathBean getAudioPathByPhysicianId(int physicianId)
			throws Exception {
		PhysicianS3AudioPathBean PhysicianS3AudioPathBean = null;
		String cql = "select physician_wave_path from " + masterKeySpace.trim()
				+ ".ez_physician_master where physician_id=" + physicianId + "";
		logger.info(LoggerUtil.getMessage("Query - " + cql));
		try {
			PhysicianS3AudioPathBean = cassandraService.selectOne(cql,
					PhysicianS3AudioPathBean.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"Error - Failed to fetch Audio Path by Physician Id");
		}
		return PhysicianS3AudioPathBean;
	}

	public PhysicianS3AudioPathBean getDocPathByPhysicianId(int physicianId)
			throws Exception {
		PhysicianS3AudioPathBean PhysicianS3AudioPathBean = null;
		String cql = "select physician_doc_path from " + masterKeySpace.trim()
				+ ".ez_physician_master where physician_id=" + physicianId + "";
		logger.info(LoggerUtil.getMessage("Query - " + cql));
		try {
			PhysicianS3AudioPathBean = cassandraService.selectOne(cql,
					PhysicianS3AudioPathBean.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"Error - Failed to fetch Audio Path by Physician Id");
		}
		return PhysicianS3AudioPathBean;
	}

	public boolean updateAudioWavePathAfterS3UploadSuccess(String bucketName,
			String audioId) throws Exception {
		StringBuilder query = new StringBuilder();
		try {
			HashMap map = getAudioMasterData(processNullString(audioId));
			map.put("audio_file_path", bucketName);
			map.put("audio_file_original_path", bucketName);
			map.put("audio_file_mp3_path", bucketName.replace("wave", "mp3"));

			query.append("begin batch ");
			query.append("update " + audioKeySpace + ".ez_audio_master set "
					+ "audio_file_original_path= '" + bucketName + "', "
					+ "audio_file_path= '" + bucketName + "', "
					+ "audio_file_mp3_path= '"
					+ bucketName.replace("wave", "mp3") + "' "
					+ "where audio_id='" + audioId + "'");
			query.append(insertMetaDatatoAudioLog(map));
			query.append(insertMetaDatatoDocumentMaster(map));
			query.append(insertMetaDatatoDocumentLog(map));
			query.append(insertMetaDataToDocPhysicianLookup(map));
			query.append(" apply batch;");

			logger.info(LoggerUtil.getMessage("final query - " + query));
			cassandraService.execute(query.toString());
			insertMetaDatatoDocumentMasterSolr(map);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error - Update Audio Wave Path in DB");
		} finally {
			File tempMP3 = new File(audioWaveFilePath + audioId + ".mp3");
			if (tempMP3.exists())
				tempMP3.delete(); // Flushing Temp

			tempMP3 = new File(audioWaveFilePath + audioId + ".wav");
			if (tempMP3.exists())
				tempMP3.delete(); // Flushing Temp
		}

		return true;
	}

	public String getStatusByStatusId(int statusId) throws Exception {
		String cql = null;
		try {
			cql = "select * from " + masterKeySpace.trim()
					+ ".ez_status_master where status_id=" + statusId;
			logger.info(LoggerUtil.getMessage("CQL - " + cql));
			ResultSet rs = cassandraService.query(cql);
			Iterator<Row> itr = rs.iterator();
			while (itr.hasNext()) {
				return (String) itr.next().getString("status_value");
			}
		} catch (Exception p) {
		}
		return "";
	}

	public String getWorkTypeDescByWorktype(int worktype) throws Exception {
		String cql = null;
		try {
			cql = "select * from " + masterKeySpace.trim()
					+ ".ez_worktype_master where template_id=" + worktype;
			logger.info(LoggerUtil.getMessage("CQL - " + cql));
			ResultSet rs = cassandraService.query(cql);
			Iterator<Row> itr = rs.iterator();
			while (itr.hasNext()) {
				return (String) itr.next().getString("template_name");
			}
		} catch (Exception p) {
			p.printStackTrace();
			throw new Exception("Error - Fetching Work Type Description");
		}
		return "";
	}

	public String getHospitalByHospitalId(int hospitalId) throws Exception {
		String cql = null;
		try {
			cql = "select * from " + masterKeySpace.trim()
					+ ".ez_hospital_master where hd_vsglobal_id=" + hospitalId;
			logger.info(LoggerUtil.getMessage("CQL - " + cql));
			ResultSet rs = cassandraService.query(cql);
			Iterator<Row> itr = rs.iterator();
			while (itr.hasNext()) {
				return (String) itr.next().getString("hd_hospital_name");
			}
		} catch (Exception p) {
			p.printStackTrace();
			throw new Exception("Error - Fetching Hospital by Hospital ID");
		}
		return "";
	}

	public String cassandraMapBuilder(Object map) {
		StringBuilder output = new StringBuilder();
		try {
			logger.info(LoggerUtil.getMessage("Hitting - " + map));
			Map<Integer, String> mapValue = (Map<Integer, String>) map;

			output.append("{");
			List<Integer> keys = new ArrayList<Integer>(mapValue.keySet());
			for (int i = 0; i < keys.size(); i++) {
				output.append(keys.get(i) + ":'" + mapValue.get(keys.get(i))
						+ "'");
				if (i == keys.size() - 1) {
					output.append("}");
					break;
				}
				output.append(",");
			}
			logger.info(LoggerUtil.getMessage("OUTPUT - " + output));
		} catch (Exception p) {
			p.printStackTrace();
		}
		return output.toString();
	}

	public HashMap getAudioMasterData(String audioId) throws Exception {
		String cql = null;
		HashMap map = null;
		try {
			map = new HashMap();
			cql = "select * from " + audioKeySpace
					+ ".ez_audio_master where audio_id=" + audioId;
			logger.info(LoggerUtil.getMessage("CQL - " + cql));
			ResultSet rs = cassandraService.query(cql);
			Iterator<Row> itr = rs.iterator();
			while (itr.hasNext()) {
				Row row = itr.next();

				map.put("audio_id", row.getString("audio_id"));
				map.put("attending_physician_id",
						row.getInt("attending_physician_id"));
				map.put("attending_physician_login_id",
						row.getString("attending_physician_login_id"));
				map.put("attending_physician_mnemonic",
						row.getString("attending_physician_mnemonic"));
				map.put("attending_physician_name",
						row.getString("attending_physician_name"));
				map.put("audio_file_codec", row.getInt("audio_file_codec"));
				map.put("audio_file_dictation_date",
						getCheckedDate(row.getDate("audio_file_dictation_date")));
				map.put("audio_file_dictation_date_gmt", getCheckedDate(row
						.getDate("audio_file_dictation_date_gmt")));
				map.put("audio_file_name", row.getString("audio_file_name"));
				map.put("audio_file_original_path",
						row.getString("audio_file_original_path"));
				map.put("audio_file_path", row.getString("audio_file_path"));
				map.put("audio_file_play_time_in_sec",
						row.getInt("audio_file_play_time_in_sec"));
				map.put("audio_file_priority",
						row.getInt("audio_file_priority"));
				map.put("audio_file_recieved_date",
						getCheckedDate(row.getDate("audio_file_recieved_date")));
				map.put("audio_file_recieved_time",
						getCheckedDate(row.getDate("audio_file_recieved_time")));
				map.put("audio_file_size", row.getInt("audio_file_size"));
				map.put("clinic_id", row.getInt("clinic_id"));
				map.put("created_date",
						getCheckedDate(row.getDate("created_date")));
				map.put("dictating_physician_id",
						row.getInt("dictating_physician_id"));
				map.put("dictating_physician_login_id",
						row.getString("dictating_physician_login_id"));
				map.put("dictating_physician_mnemonic",
						row.getString("dictating_physician_mnemonic"));
				map.put("dictating_physician_name",
						row.getString("dictating_physician_name"));
				map.put("dui", row.getInt("dui"));
				map.put("global_document_id", row.getInt("global_document_id"));
				map.put("global_wave_id", row.getInt("global_wave_id"));
				map.put("hospital_id", row.getInt("hospital_id"));
				map.put("hospital_name", row.getString("hospital_name"));
				map.put("job_number", row.getInt("job_number"));
				map.put("level_status_id", row.getMap("level_status_id",
						Integer.class, String.class));
				map.put("local_wave_id", row.getInt("local_wave_id"));
				map.put("patient_accountno", row.getString("patient_accountno"));
				map.put("patient_dob",
						getCheckedDate(row.getDate("patient_dob")));
				map.put("patient_exam_date",
						getCheckedDate(row.getDate("patient_exam_date")));
				map.put("patient_gender", row.getString("patient_gender"));
				map.put("patient_id", row.getInt("patient_id"));
				map.put("patient_location", row.getString("patient_location"));
				map.put("patient_mrn", row.getString("patient_mrn"));
				map.put("patient_name", row.getString("patient_name"));
				map.put("patient_ssn", row.getString("patient_ssn"));
				map.put("priority_id", row.getInt("priority_id"));
				map.put("processing_level", row.getInt("processing_level"));
				map.put("resend", row.getInt("resend"));
				map.put("station_id", row.getInt("station_id"));
				map.put("status", row.getString("status"));
				map.put("status_id", row.getInt("status_id"));
				map.put("subgroup_id",
						row.getMap("subgroup_id", Integer.class, String.class));
				map.put("target_server_id", row.getMap("target_server_id",
						Integer.class, String.class));
				map.put("tat_achieved", row.getInt("tat_achieved"));
				map.put("tds_tracking_number",
						row.getInt("tds_tracking_number"));
				map.put("tracking_number", row.getString("tracking_number"));
				map.put("trans_group_id", row.getInt("trans_group_id"));
				map.put("turn_around_time", row.getInt("turn_around_time"));
				map.put("updated_by", row.getString("updated_by"));
				map.put("updated_by_id", row.getInt("updated_by_id"));
				map.put("updated_date",
						getCheckedDate(row.getDate("updated_date")));
				map.put("visit_number", row.getInt("visit_number"));
				map.put("worktype", row.getInt("worktype"));
				map.put("audio_intermediate_id",
						row.getInt("audio_intermediate_id"));

				return map;
			}
		} catch (Exception p) {
			p.printStackTrace();
		}
		return null;
	}

	public java.sql.Timestamp getCheckedDate(java.util.Date date) {
		if (date == null) {
			return new java.sql.Timestamp(new java.util.Date().getTime());
		} else {
			return new java.sql.Timestamp(date.getTime());
		}
	}

	public int getStatusIdByDocumentId(String docId) throws Exception {
		String cql = null;
		try {
			cql = "select document_current_status_id from " + documentKeySpace
					+ ".ez_document_master where document_id='" + docId + "' "
					+ "and audio_id='" + docId + "'";
			logger.info(LoggerUtil.getMessage("Get Status Id from Doc Id - "
					+ cql));
			ResultSet rs = cassandraService.query(cql);
			Iterator<Row> itr = rs.iterator();
			while (itr.hasNext()) {
				return itr.next().getInt("document_current_status_id");
			}
		} catch (Exception p) {
			p.printStackTrace();
			throw new Exception("Error : Fetching Status Id For Doc Id");
		}
		return 0;
	}

}
