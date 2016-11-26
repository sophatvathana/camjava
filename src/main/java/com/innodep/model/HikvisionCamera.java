package com.innodep.model;

public class HikvisionCamera extends RtspCamera {

	public HikvisionCamera(String model, String host, int webPort, String user, String pass,
			int rtspPort, int streamId, boolean ptz) {
		super("HikVision",
				model,
				host,
				webPort,
				user,
				pass,
				rtspPort,
				String.format("/Streaming/Channels/10%d", streamId + 1),
				ptz ? HikvisionPtz.class : null);
	}
}
