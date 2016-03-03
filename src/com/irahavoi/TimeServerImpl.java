package com.irahavoi;

import com.irahavoi.TimeServer;

import javax.jws.WebService;
import java.util.Date;

@WebService(endpointInterface = "com.irahavoi.TimeServer")
public class TimeServerImpl implements TimeServer {
    @Override
    public String getTimeAsString() {
        return new Date().toString();
    }

    @Override
    public long getTimeAsElapsed() {
        return new Date().getTime();
    }
}
