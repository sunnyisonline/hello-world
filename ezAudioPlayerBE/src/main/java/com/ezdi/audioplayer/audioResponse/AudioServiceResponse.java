package com.ezdi.audioplayer.audioResponse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.ezdi.audioplayer.bean.Audio;

public class AudioServiceResponse
{
    

	private Object data;
	private Header header;
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	public Header getHeader()
	{
		return header;
	}
	public void setHeader(Header header) 
	{
		this.header = header;
		
	}
	
	 public static AudioServiceResponse createMicroServiceResponse(Object responseObject){
		 AudioServiceResponse audioServiceResponse = new AudioServiceResponse();
		
                if (responseObject instanceof BindingResult) 
                {

	            List<FieldError> fieldErrors = ((BindingResult) responseObject).getFieldErrors();
	            StringBuffer fieldErrorbuffer = new StringBuffer();
	            fieldErrorbuffer.append("There are "+fieldErrors.size()+" Errors.:");
	            int i=1;
	            for (FieldError error : fieldErrors) {
	            	fieldErrorbuffer.append(i+"."+error.getObjectName()+ " has an error: " + error.getDefaultMessage());
	                i++;
	            }
                responseObject=fieldErrorbuffer.toString();
                Header header = new Header();
                header.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                header.setMessage("Fail");
                header.setSuccess(false);
                audioServiceResponse.setData(responseObject);
                audioServiceResponse.setHeader(header);
                return audioServiceResponse;
	        }
	        if(responseObject instanceof AudioServiceResponse)
	        {
	        	
	        	
	        	if(  ( (AudioServiceResponse) responseObject).getData() != null){
	        		
	        		  Header header = new Header();
	        		  header.setCode(HttpStatus.OK.value());
	        		  header.setMessage("Success");
	        		  header.setSuccess(true);
	        		  audioServiceResponse.setHeader(header);
	        		 
	        		  audioServiceResponse.setData(((AudioServiceResponse) responseObject).getData());
			          return audioServiceResponse;
	        	}
	        	else{
	        		  Header header = new Header();
	        		  header.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	        		  header.setMessage("Fail");
	        		  header.setSuccess(false);
	        		  audioServiceResponse.setData(responseObject);
	        		  audioServiceResponse.setHeader(header);
	        		  return audioServiceResponse;
	        	}
		          
	        }
	        return (AudioServiceResponse)responseObject;
	        	
	        	
	        	
	        
	    }
	 

  
   public static boolean validateResponseObject(Object responseObject){

        String fullClassName = responseObject.getClass().toString();
        String className= fullClassName.substring(fullClassName.lastIndexOf(".")+1,fullClassName.length());
        boolean isValid;
        switch (className){
                case "Integer":
                                isValid=((Integer)responseObject)==0 ? false : true;
                                break;
                case "Boolean":
                                isValid=((Boolean)responseObject);
                                break;
                             

                case "ArrayList":
                                isValid=((ArrayList)responseObject)==null ||
                                        ((ArrayList)responseObject).isEmpty() ? false : true;
                                break;
                case "BeanPropertyBindingResult":
                                isValid=false;
                                break;
                default:
                                isValid=responseObject==null ? false : true;
        }

        return  isValid;
    }
    
   public static AudioServiceResponse deleteMicroServiceResponse(Object responseObject){
	   
	   AudioServiceResponse audioServiceResponse = new AudioServiceResponse();
	     if (responseObject instanceof BindingResult) 
         {

         List<FieldError> fieldErrors = ((BindingResult) responseObject).getFieldErrors();
         StringBuffer fieldErrorbuffer = new StringBuffer();
         fieldErrorbuffer.append("There are "+fieldErrors.size()+" Errors.:");
         int i=1;
         for (FieldError error : fieldErrors) {
         	fieldErrorbuffer.append(i+"."+error.getObjectName()+ " has an error: " + error.getDefaultMessage());
             i++;
         }
         responseObject=fieldErrorbuffer.toString();
         Header header = new Header();
         header.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
         header.setMessage("Fail");
         header.setSuccess(false);
         audioServiceResponse.setData(responseObject);
         audioServiceResponse.setHeader(header);
         return audioServiceResponse;
     }
     if(responseObject instanceof AudioServiceResponse)
     {
     	
     	
     	if(  ( (AudioServiceResponse) responseObject).getData() != null){
     		
     		  Header header = new Header();
     		  header.setCode(HttpStatus.OK.value());
     		  header.setMessage("Success");
     		  header.setSuccess(true);
     		  audioServiceResponse.setHeader(header);
     		 
     		  audioServiceResponse.setData(((AudioServiceResponse) responseObject).getData());
		          return audioServiceResponse;
     	}
     	else{
     		  Header header = new Header();
     		  header.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
     		  header.setMessage("Fail");
     		  header.setSuccess(false);
     		  audioServiceResponse.setData(responseObject);
     		  audioServiceResponse.setHeader(header);
     		  return audioServiceResponse;
     	}
	          
     }
     return (AudioServiceResponse)responseObject;
     	
   }
 	
     	
	   
	
	 

	 
   
   
}