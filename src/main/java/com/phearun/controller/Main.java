package com.phearun.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.phearun.service.PtzInterface;
import com.phearun.service.impl.HikvisionPtz;

public class Main {
    public static void main(String[] args) {
        try {
            PtzInterface ptz = new HikvisionPtz("192.168.178.146", 80, "admin", "12345");
            BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
            String line = "";
            while(!line.equalsIgnoreCase("exit")) {
                line = buffer.readLine();
                System.out.println(line);
                if (line.equalsIgnoreCase("left")) {
                    ptz.left(40);
                }
                else if (line.equalsIgnoreCase(("right"))) {
                    ptz.right(40);
                }
                else if (line.equalsIgnoreCase(("stop"))) {
                    ptz.stop();
                }
                else if (line.equalsIgnoreCase(("up"))) {
                    ptz.up(40);
                }
                else if (line.equalsIgnoreCase(("down"))) {
                    ptz.down(40);
                }
            }
        }
        catch (Exception ex) {

        }
    }
}
