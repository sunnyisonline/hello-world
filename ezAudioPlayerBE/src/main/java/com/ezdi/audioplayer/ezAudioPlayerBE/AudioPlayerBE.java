package com.ezdi.audioplayer.ezAudioPlayerBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @author EZDI\pradnya.d
 *
 */
@SpringBootApplication
@ComponentScan({ "com.ezdi.audioplayer" })
public class AudioPlayerBE {
	public static void main(String[] args) {
		SpringApplication.run(AudioPlayerBE.class, args);

	}
}
