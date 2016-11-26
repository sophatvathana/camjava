package com.innodep.svc;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.innodep.model.IpCamera;


public class CameraManager {
	private final Logger logger = LoggerFactory.getLogger(CameraManager.class);
	
	Map<String, IpCamera> cameras;

	public void setCameras(Map<String, IpCamera> cameras) {
		this.cameras = cameras;
		logger.info("Created with: {}", cameras);
	}
	
	public CameraManager() {
		// TODO Auto-generated constructor stub
		//logger.info("Created with: {}", cameraList);
	}
	
	public IpCamera getCamera(String id) {
		if (this.cameras.containsKey(id))
			return this.cameras.get(id);
		
		return null;
	}

}
