package com.innodep.model;


/**
 * Created by adminuser on 2016-11-21.
 */
public interface PtzInterface {
	void    setConnection(String host, int port, String user, String pass);
    boolean right(int speed);
    boolean left(int speed);
    boolean up(int speed);
    boolean down(int speed);
    boolean stop();
    boolean wide(int speed);
    boolean tele(int speed);
}
