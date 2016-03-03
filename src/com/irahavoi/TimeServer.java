package com.irahavoi;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by irahavoi on 2016-03-02.
 */
@WebService
public interface TimeServer {
    @WebMethod String getTimeAsString();
    @WebMethod long getTimeAsElapsed();
}
