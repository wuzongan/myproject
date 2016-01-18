package com.kunlun.poker.recharge.service;

import java.io.IOException;

public interface FCGIService {
    /**
     * @param args the command line arguments
     * @throws IOException 
     */
    public boolean validateToken(String token); 
}
