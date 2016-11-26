package com.phearun.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phearun.service.PtzInterface;
import com.phearun.service.impl.HikvisionPtz;
import com.phearun.service.impl.Vivotek;

@RestController
@RequestMapping("/remote")
public class HikvisionController {

	@RequestMapping(value = "/{model}", method = RequestMethod.GET)
	public ResponseEntity<String> remote(@PathVariable("model") String model, 
			@RequestParam("command") String move,
			@RequestParam(defaultValue = "50", value="speed", required=false) int speed) {
		
		System.out.println("remoting...");
		PtzInterface ptz = null;
		
		switch(model){
			case "hikvision": ptz = new HikvisionPtz("192.168.178.146", 80, "admin", "12345"); break;
			case "vivotek"  : ptz = new Vivotek("192.168.178.158", 80, "root", "pass");        break;
		}
		
		switch(move){
			case "left"  : ptz.left(speed);  break;
			case "right" : ptz.right(speed); break;
			case "up"    : ptz.up(speed);    break;
			case "down"  : ptz.down(speed);  break;
			case "stop"  : ptz.stop();    break;
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	
	@RequestMapping(value = "/stream", method = RequestMethod.GET)
	public void streamVideo(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.addHeader("Cache-Control", "no-cache");
		response.setContentType("multipart/x-mixed-replace;boundary=imageframes");
		ServletOutputStream out = response.getOutputStream();
		int i=1;
		while(true){
			FileInputStream fis = new FileInputStream(new File("image/1 (" + i + ").jpg"));
			BufferedInputStream bif = new BufferedInputStream(fis);
			int data = 0;
			out.println();
			out.println("--imageframes");
			out.println("Content-Type: image/jpg");
			out.println();
			//out.println("Content-Length: ");
			while((data = bif.read())!=-1){
				out.write(data);				
			}
			out.flush();
			bif.close();
			i++;
			
			if(i>29) i=1;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
