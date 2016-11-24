package com.phearun.service;


/**
 * Created by adminuser on 2016-11-21.
 */
public interface PtzInterface {
    boolean right(int speed);
    boolean left(int speed);
    boolean up(int speed);
    boolean down(int speed);
    boolean stop();
}
