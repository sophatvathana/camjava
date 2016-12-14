package com.phearun.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.innodep.model.HikvisionCamera;
import com.innodep.model.IpCamera;
import com.innodep.svc.CameraManager;

@Configuration
public class BeanConfiguration {

	@Bean
	public CameraManager cameraManager(){
		CameraManager manager = new CameraManager();
		
		Map<String, IpCamera> cameras = new HashMap<>();
		cameras.put("hik", hikvisionCamera());
		manager.setCameras(cameras);
		
		return manager;
	}
	
	@Bean
	public HikvisionCamera hikvisionCamera(){
		HikvisionCamera hik = new HikvisionCamera("DS-2CD2Q10FD-IW", "192.168.0.44", 80, "admin", "12345", 554, 1, true);
		return hik;
	}
	
	@PreDestroy
	public void destroy(){
		hikvisionCamera().release();
	}
	
	@PostConstruct
	public void init(){
		hikvisionCamera().initialize();
	}
}
