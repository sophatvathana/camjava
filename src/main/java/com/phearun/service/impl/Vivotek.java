package com.phearun.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.phearun.service.PtzInterface;

public class Vivotek implements PtzInterface{

	private String host;
    private int    port;
    CloseableHttpClient httpclient;

    public Vivotek(String host, int port, String user, String pass) {
        this.host = host;
        this.port = port;

        if (user != null && user.length() > 0) {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(host, port),
                    new UsernamePasswordCredentials(user, pass));
            httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        }
        else
            httpclient = HttpClients.createDefault();

    }
	
    private HttpGet makeRequest(String move) {
        return new HttpGet(String.format("http://%s%s/cgi-bin/camctrl/camctrl.cgi?move=" + move, host, port == 80? "": String.format(":%d", port)));
    }

    /*private String makeString(int p, int t, int z) {    	
        return String.format("<PTZData version=\"2.0\" xmlns=\"http://www.isapi.org/ver20/XMLSchema\"><pan>%d</pan><tilt>%d</tilt><zoom>%d</zoom></PTZData>", p, t, z);
    }*/

    private boolean doExecute(String msg, String move) {
    	boolean rslt = false;
        HttpGet req = makeRequest(move);
        System.out.println(req);
        org.apache.http.entity.StringEntity ent = new org.apache.http.entity.StringEntity(msg, "UTF-8");
        try {

            CloseableHttpResponse response2 = httpclient.execute(req);

            try {
                HttpEntity entity2 = response2.getEntity();
                InputStreamReader isr = new InputStreamReader(entity2.getContent());
                BufferedReader br = new BufferedReader(isr);
                String l = br.readLine();
                while (l != null) {
                    System.out.println(l);
                    l = br.readLine();
                }
                rslt = (response2.getStatusLine().getStatusCode() >=200 &&
                        response2.getStatusLine().getStatusCode()< 300);
            } finally {
                response2.close();
            }
        } catch (IOException ex) {

        }

        return true;
    }

    
	@Override
	public boolean right(int speed) {
		return doExecute("Left", "right");
	}

	@Override
	public boolean left(int speed) {
		return doExecute("Left", "left");
	}

	@Override
	public boolean up(int speed) {
		return doExecute("Left", "up");
	}

	@Override
	public boolean down(int speed) {
		return doExecute("Left", "down");
	}

	@Override
	public boolean stop() {
		return doExecute("Left", "stop");
	}

}
