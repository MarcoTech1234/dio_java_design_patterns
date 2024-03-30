package one.digitalone.gof.service;

import org.springframework.stereotype.Service;

@Service
public class ConversaoService {
    public double converterBRparaDolar(double amount) {
        return amount * 0.20; 
    } 
}
