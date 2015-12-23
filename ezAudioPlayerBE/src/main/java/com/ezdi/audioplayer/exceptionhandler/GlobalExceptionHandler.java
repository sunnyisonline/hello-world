package com.ezdi.audioplayer.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ezdi.audioplayer.audioResponse.AudioResponseWrapper;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value=Exception.class)
	public @ResponseBody AudioResponseWrapper handlerExcpetion(Exception p){
		AudioResponseWrapper responseWrapper = new AudioResponseWrapper();
		responseWrapper.setResponseCode("error");
		responseWrapper.setResponseMessage(p.getMessage());
		return responseWrapper;
	}
}
