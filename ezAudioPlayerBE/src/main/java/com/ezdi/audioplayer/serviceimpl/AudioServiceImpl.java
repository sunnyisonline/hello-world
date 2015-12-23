package com.ezdi.audioplayer.serviceimpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.util.IOUtils;
import com.ezdi.audioplayer.audioResponse.AudioResponse;
import com.ezdi.audioplayer.audioResponse.AudioServiceResponse;
import com.ezdi.audioplayer.audioResponse.Data;
import com.ezdi.audioplayer.audioResponse.Header;
import com.ezdi.audioplayer.bean.Audio;
import com.ezdi.audioplayer.bean.AudioPhysicianMappingBean;
import com.ezdi.audioplayer.bean.PhysicianS3AudioPathBean;
import com.ezdi.audioplayer.dao.AudioDao;
import com.ezdi.audioplayer.logger.LoggerUtil;
import com.ezdi.audioplayer.metadata.request.bean.MetaDataRequestWrapper;
import com.ezdi.audioplayer.service.AudioService;
import com.ezdi.s3connection.service.AmazonS3Service;
import com.google.common.io.ByteStreams;

@Service
public class AudioServiceImpl implements AudioService {
	private static final Logger logger = Logger
			.getLogger(AudioServiceImpl.class);

	@Autowired
	private AudioDao audioDao;

	@Autowired
	private AmazonS3Service amazonS3Service;

	@Value("${audio.file.path}")
	private String audioFilePath;

	@Value("${audio.wave.file.path}")
	private String waveFilePath;

	@Value("${ffmpeg.installed.path}")
	private String ffmpegInstalledPath;

	@Value("${ffmpeg.bits.rate}")
	private String ffmpegBitsRate;

	@Override
	public List<Audio> fetchAllAudioFiles() {

		return audioDao.fetchAllAudioFiles();

	}

	@Override
	public AudioServiceResponse deleteAudioFile(String audioId) {
		logger.info(LoggerUtil.getMessage("Inside the delete service method"));
		Audio audio = null;
		AudioServiceResponse audioResponse = new AudioServiceResponse();
		try {
			audio = audioDao.fetchAudioFilePath(audioId);
		} catch (Exception e) {
			logger.info(LoggerUtil
					.getMessage("Database exception from cassandra."));
			e.printStackTrace();
			audioResponse.setData(null);
			return audioResponse;
		}
		if (audio != null && audio.getAudioFileName() != null
				&& audio.getAudioId() != null) {
			Data data = new Data();
			String dir = audioFilePath;
			File file = new File(dir + audio.getAudioFileName());
			Boolean flag = false;
			flag = file.delete();
			if (flag) {
				logger.info(LoggerUtil.getMessage(audio.getAudioFileName()
						+ " is deleted!"));
				logger.info(LoggerUtil
						.getMessage("file deleted successfully........"));

				data.setAudioFileName(audio.getAudioFileName());
				data.setAudioId(audio.getAudioId());
				audioResponse.setData(data);
				return audioResponse;
			} else {
				logger.info(LoggerUtil
						.getMessage("Unable to delete file ........"));
				audioResponse.setData(null);
				return audioResponse;
			}
		} else {
			logger.info(LoggerUtil.getMessage("Audio file data is null"));
			audioResponse.setData(null);
			return audioResponse;
		}

	}

	@Override
	public AudioServiceResponse fetchAudioFilePath(String audioFileId) {

		logger.info(LoggerUtil.getMessage("Inside the service method >>>> "
				+ audioFileId));
		logger.info(LoggerUtil.getMessage("Fetch the file path from cassandra"));
		Audio audio = null;

		AudioServiceResponse audioResponse = new AudioServiceResponse();

		try {
			audio = audioDao.fetchAudioFilePath(audioFileId);
		} catch (Exception e) {
			logger.info(LoggerUtil
					.getMessage("Database exception from cassandra."));
			e.printStackTrace();
			audioResponse.setData(null);
			return audioResponse;
		}

		Boolean flag = createDirectory();

		if (flag && null != audio && audio.getAudioFileName() != null
				&& audio.getAudioFilepath() != null) {

			audio.setAudioFileName(audio.getAudioFileName().replace(".wav",
					".mp3"));
			Data data = new Data();
			logger.info(LoggerUtil.getMessage("audio file name:"
					+ audio.getAudioFileName()));
			logger.info(LoggerUtil.getMessage("audio file path:"
					+ audio.getAudioFilepath()));

			Boolean flag1 = downloadFromS3(audio);

			if (flag1 == true) {
				logger.info(LoggerUtil
						.getMessage("file downloaded  successfully......"));
				data.setAudioFileName(audio.getAudioFileName());
				data.setAudioId(audio.getAudioId());
				audioResponse.setData(data);
				return audioResponse;
			} else {
				logger.info(LoggerUtil.getMessage("Amazon Exception"));
				audioResponse.setData(null);
				return audioResponse;
			}

		} else {

			logger.info(LoggerUtil.getMessage("Audio data is null....."));
			audioResponse.setData(null);
			return audioResponse;

		}

	}

	private Boolean createDirectory() {

		String dir = audioFilePath;
		File directory = new File(dir);

		if (directory.exists()) {
			logger.info(LoggerUtil.getMessage("Directory already exists ..."));
			return true;
		} // if
		else {
			logger.info(LoggerUtil
					.getMessage("Directory not exists, creating now"));
			try {
				directory.mkdir();
				return true;
			} catch (Exception e) {
				logger.info(LoggerUtil.getMessage("Directory creation failed"));
				// e.printStackTrace();
				return false;
			}

		}

	}

	private Boolean downloadFromS3(Audio audio) {
		String fileName = audio.getAudioFileName();
		String filePath = audio.getAudioFilepath();

		try {
			logger.info(LoggerUtil
					.getMessage("\n Before Download........... \n File Path - "
							+ filePath + "\n Name - " + fileName));
			amazonS3Service.downloadFile(filePath, fileName, audioFilePath
					+ fileName);
			logger.info(LoggerUtil.getMessage("\n After Download..........."));
			return true;
		} catch (AmazonClientException | InterruptedException e) {

			return false;

		}

	}

	// Meta Data - Sunny
	// To Cassandra
	public boolean insertMetaDatatoCassandra(
			MetaDataRequestWrapper metaDataRequestWarpper) throws Exception {
		try {
			return audioDao.insertMetaDatatoCassandra(metaDataRequestWarpper);
		} catch (Exception p) {
			throw new Exception(
					"Error - Failed to Insert Meta-Data in Audio Master");
		}
	}

	public static byte[] getBytesFromInputStream(InputStream is)
			throws IOException {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
			byte[] buffer = new byte[0xFFFF];

			for (int len; (len = is.read(buffer)) != -1;)
				os.write(buffer, 0, len);

			os.flush();

			return os.toByteArray();
		}
	}

	// Meta Data - Sunny
	// To Amazon S3
	public boolean uploadAudioToS3(String waveBucket, String docBucket,
			String fileNameOnS3, byte[] data) throws Exception {
		try {
			String audioId = fileNameOnS3.split(".wav")[0];
			if (handleGSMtoMp3(audioId, data)) {
				String mp3FilePath = waveFilePath + audioId + ".mp3";
				File nn = new File(mp3FilePath);
				amazonS3Service.uploadData(waveBucket, fileNameOnS3, data);
				logger.info(LoggerUtil.getMessage("mp3FilePath - "
						+ mp3FilePath));
				logger.info(LoggerUtil.getMessage("S3 Mp3 Path - "
						+ waveBucket.replace("wave", "mp3")));
				boolean flag = audioDao.isRecordExistsInDocMaster(audioId);
				logger.info(LoggerUtil.getMessage("Check Audio Id - " + audioId
						+ "Flag -" + flag));
				if (!flag) {
					logger.info(LoggerUtil.getMessage("Creating Doc @ S3"));
					amazonS3Service.uploadData(docBucket,
							fileNameOnS3.replace(".wav", ".doc"), "");
				}
				amazonS3Service.uploadFile(waveBucket.replace("wave", "mp3"),
						audioId + ".mp3", mp3FilePath);
			}
		} catch (Exception p) {
			throw new Exception("Error : Uploading Audio File to Amazon S3");
		}
		return true;
	}

	public boolean handleGSMtoMp3(String audioId, byte[] data) throws Exception {
		try {
			String waveFilePath1 = waveFilePath + audioId + ".wav";
			String mp3FilePath = waveFilePath + audioId + ".mp3";
			logger.info(LoggerUtil.getMessage("PATH - " + waveFilePath1));
			File waveFile = new File(waveFilePath);
			if (!waveFile.exists()) {
				waveFile.mkdirs();
			}

			waveFile = new File(waveFilePath1);
			FileOutputStream outStream = new FileOutputStream(waveFile);
			outStream.write(data);
			outStream.flush();
			outStream.close();

			Runtime rt = Runtime.getRuntime();
			String osName = System.getProperty("os.name");
			logger.info(LoggerUtil.getMessage(osName));
			Process process = null;

			logger.info(LoggerUtil.getMessage("ffmpegInstalledPath - "
					+ ffmpegInstalledPath));

			logger.info(LoggerUtil.getMessage("ffmpegBitsRate - "
					+ Integer.parseInt(ffmpegBitsRate)));
			
			if (osName != null && osName.equalsIgnoreCase("linux")) {
				process = rt.exec(ffmpegInstalledPath + " -i " + waveFilePath1
						+ " -ac 1 -ab " + Integer.parseInt(ffmpegBitsRate) + " -ar 22050 "
						+ mp3FilePath + " -y");
			} else {
				if (osName.startsWith("Windows")) {
					process = rt.exec(ffmpegInstalledPath + " -i "
							+ waveFilePath1 + " -ac 1 -ab "
							+ Integer.parseInt(ffmpegBitsRate) + " -ar 22050 " + mp3FilePath
							+ " -y");

				}
			}

			if (process != null)
				process.waitFor();

			logger.info(LoggerUtil.getMessage("Conversion Done - "
					+ mp3FilePath));

		} catch (Exception p) {
			throw new Exception("Error : Fail to Convert GSM to Mp3 Format");
		}
		return true;
	}

	public boolean prepareAmazonS3AudioFolderStructure(String bucketName)
			throws Exception {
		try {
			boolean audioFolderStatus = amazonS3Service
					.doesBucketExists(bucketName);
			if (!audioFolderStatus) {
				StringTokenizer st = new StringTokenizer(bucketName, "/");
				String finalBucket = "", previousBucket = "";
				while (st.hasMoreTokens()) {
					String folderName = st.nextToken();
					logger.info(LoggerUtil.getMessage("\n" + folderName));

					finalBucket += folderName;
					audioFolderStatus = amazonS3Service
							.doesBucketExists(finalBucket);
					logger.info(LoggerUtil
							.getMessage("Exists Check finalBucket- "
									+ finalBucket + " STATUS - "
									+ audioFolderStatus + "Previous Bucket - "
									+ previousBucket));
					if (!audioFolderStatus) {
						amazonS3Service
								.createBucket(previousBucket, folderName);
						logger.info(LoggerUtil.getMessage(finalBucket
								+ "Created"));
					}
					previousBucket = finalBucket;
					finalBucket += "/";
				}
			}
		} catch (Exception p) {
			throw new Exception("Error : Uploading Audio File to Amazon S3");
		}
		return true;
	}

	public int getPhysicianIdByAudioId(String audioId) throws Exception {
		int physicianId = 0;
		try {
			logger.info(LoggerUtil.getMessage("audioId : " + audioId));
			physicianId = audioDao.getPhysicianIdByAudioId(audioId)
					.getDictatingPhysicianId();
			logger.info(LoggerUtil.getMessage("Physician id  - " + physicianId));
		} catch (Exception p) {
			throw new Exception(p);
		}
		return physicianId;
	}

	public String getAudioPathByPhysicianId(int physicianId) throws Exception {
		String audioPath = "";
		try {
			PhysicianS3AudioPathBean bean = audioDao
					.getAudioPathByPhysicianId(physicianId);
			if (bean != null) {
				audioPath = bean.getPhysicianWavePath();
			}
			logger.info(LoggerUtil.getMessage("Bean X - " + bean));
			logger.info(LoggerUtil.getMessage("Audio Path - " + audioPath));
		} catch (Exception p) {
			p.printStackTrace();
			throw new Exception(p);
		}
		return audioPath;
	}

	public String getDocPathByPhysicianId(int physicianId) throws Exception {
		String docPath = "";
		try {
			PhysicianS3AudioPathBean bean = audioDao
					.getDocPathByPhysicianId(physicianId);
			if (bean != null) {
				docPath = bean.getPhysicianDocPath();
			}
			logger.info(LoggerUtil.getMessage("Bean X - " + bean));
			logger.info(LoggerUtil.getMessage("Doc Path - " + docPath));
		} catch (Exception p) {
			p.printStackTrace();
			throw new Exception(p);
		}
		return docPath;
	}

	public boolean updateAudioWavePathAfterS3UploadSuccess(String bucketName,
			String audioId) throws Exception {
		return audioDao.updateAudioWavePathAfterS3UploadSuccess(bucketName,
				audioId);
	}
}
