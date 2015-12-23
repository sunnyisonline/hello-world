package com.ezdi.audioplayer.audioResponse;

public class AudioResponse {


    private Header header;
    private Object data;

    
	

	public Object getData() {
		return data;
	}

	
	public void setData(Object data) {
		this.data = data;
	}

	public AudioResponse(){}
    
    public Header getHeader(){
        return header;
    }

    public void setHeader(Header header){
        this.header = header;
    }

  

}