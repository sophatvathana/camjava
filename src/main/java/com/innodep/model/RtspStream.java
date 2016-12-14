package com.innodep.model;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by adminuser on 2016-11-22.
 */
public class RtspStream extends ExternalBase {
	static Logger logger = LoggerFactory.getLogger(RtspStream.class);
    private long handler = 0;

    static {
        String path = getNativePath();
        System.out.println(path);
        logger.info(System.getProperty("java.library.path"));
        logger.info(path);
        //if (getOSType().contains("windows"))
        //	loadNativeLibrary(path, "strmrecv.so.dll");
        //else
        	loadNativeLibrary(path, "strmrecv.dylib");
        
    }

    private native long    createContextInternal(String home);
    private native boolean openRtspInternal(long handler, String url);
    private native byte[]  readFrameInternal(long handler);
    private native void    releaseContextInternal(long handler);

    static private String getNativePath() {
        String homeFolder = new File(System.getProperty("user.dir")).getAbsolutePath();
        System.out.print(homeFolder);
        String arch = System.getProperty("os.arch");
        String path = String.format("%s%snative%s%s%s%s%s", homeFolder, File.separator, File.separator, getOSType(), File.separator, arch, File.separator);
        return path;
    }

    public void RtspSteam() {
    	
    }
    
    public void initialize() {
        release();
        System.out.println("********************** Scan for native path *******************************");
        System.out.println(getNativePath());
        handler = createContextInternal(getNativePath());
    }

    public void release() {
        if (handler != 0)
            releaseContextInternal(handler);

        handler = 0;
    }

    public boolean open(String url) {
    	logger.info("RTSP: {}", url);
        if (handler != 0)
            return openRtspInternal(handler, url);

        return false;
    }

    public byte[] read()
    {
        if (handler != 0)
            return readFrameInternal(handler);

        return null;
    }
}
