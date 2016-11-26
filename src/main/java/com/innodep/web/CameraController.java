package com.innodep.web;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.innodep.model.IpCamera;
import com.innodep.model.PtzInterface;
import com.innodep.model.StreamConsumer;
import com.innodep.svc.CameraManager;


@Controller
public class CameraController {
	private final Logger logger = LoggerFactory.getLogger(CameraController.class);
	CameraManager manager;
	@Autowired
	public CameraController(CameraManager manager) {
		logger.debug("set {}", manager);
		this.manager = manager;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public String index() {

		logger.debug("index() is executed!");
		
		return "index";
	}
	
	@RequestMapping(value = "/stream/{id}", method = RequestMethod.GET)
	public void stream(@PathVariable String id, HttpServletResponse response, HttpServletRequest request)  {
		logger.debug("\nRES:{}", response);
		System.out.println("Hello world!!!");
		do {
			IpCamera cam = manager.getCamera(id);
			if (cam == null) {
				logger.info("stream command: {} was failed: object not found", id);
				break;
			}
			
			try {
				OutputStream os = response.getOutputStream();
				logger.debug("Start Consumer");
				response.addHeader("Cache-Control", "no-cache");
				response.setContentType("multipart/x-mixed-replace;boundary=boundary");
				StreamConsumer cons = new StreamConsumer(os);
				cam.addStreamConsumer(cons);
				long count = 0, sendSize = 0;
				while(cons.getContinue()) {
					if (!cons.exist()) {
						Thread.sleep(3);
						continue;
					}
					byte[] packet = cons.take();
					String head = String.format("--boundary\r\nContent-Type: image/jpeg\r\nContent-Length: %d\r\n\r\n", packet.length);
					os.write(head.getBytes("UTF-8"));
					os.write(packet);
					os.flush();
					sendSize += packet.length;
					count++;
					if (count % 100 == 0)
						logger.info("{} stream sending: {}/{}", cam.getModelName(), count, sendSize);
				}

			} catch (Exception e) {
				logger.info(e.getMessage());
			}
			
		} while(false);
	}

	@RequestMapping(value = "/ptz/{id}/{method}", method = RequestMethod.GET)
	@ResponseBody
	public String ptz(@PathVariable String id, @PathVariable String method, @RequestParam(defaultValue="50", required=false) int speed) {

		String mesg = "FAILED";
		do {
			IpCamera cam = manager.getCamera(id);
			if (cam == null) {
				logger.info("ptz command: {} {} {} was failed: object not found", id, method, speed);
				break;
			}
			
			PtzInterface ptz = cam.getPtzInterface();
			if (ptz == null) {
				logger.info("ptz command: {} {} {} was failed: object not found", id, method, speed);
				break;
			}
			if (method.equalsIgnoreCase("left") && ptz.left(speed))
				mesg = "SUCCESS";
			else if(method.equalsIgnoreCase("right") && ptz.right(speed))
				mesg = "SUCCESS";
			else if(method.equalsIgnoreCase("up") && ptz.up(speed))
				mesg = "SUCCESS";
			else if(method.equalsIgnoreCase("down") && ptz.down(speed))
				mesg = "SUCCESS";
			else if (method.equalsIgnoreCase("tele") && ptz.tele(speed))
				mesg = "SUCCESS";
			else if (method.equalsIgnoreCase("wide") && ptz.wide(speed))
				mesg = "SUCCESS";
			else if (method.equalsIgnoreCase("stop") && ptz.stop())
				mesg = "SUCCESS";
			else {
				logger.info("ptz command: {} {} {} was failed: invalid command", id, method, speed);
			}
		} while(false);
		
		return mesg;
	}

}
