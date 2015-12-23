package com.ezdi.audioplayer.audioResponse;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by sunny.k on 10/19/2015.
 */
public class Data 
{

    @NotEmpty(message = "Please provide Audio Id.")
    String audioId;
   
    String audioFileName;
    
	public String getAudioFileName() {
		return audioFileName;
	}

	public void setAudioFileName(String audioFileName) {
		this.audioFileName = audioFileName;
	}

	public String getAudioId() {
		return audioId;
	}

	public void setAudioId(String audioId) {
		this.audioId = audioId;
	}


	

  
	
}
