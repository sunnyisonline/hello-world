package com.ezdi.audioplayer.audioRequest;



import org.hibernate.validator.constraints.NotEmpty;



public class AudioServiceRequest 
{
     @NotEmpty(message="Id should not be null")
	private String audioId;

	public String getAudioId() 
	{
		return audioId;
	}

	public void setAudioId(String audioId)
	{
		this.audioId = audioId;
	}
  

	
 
}
