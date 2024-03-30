package one.digitalone.gof.service.impl;

import one.digitalone.gof.service.ConversaoService;

public class ConverterServiceAdapter {
    private ConversaoService converter;

    public void CurrencyAdapter(ConversaoService newConverter) {
        this.converter = newConverter;
    }

    public double ConverterLibraEsterlina(double reais){
        // covertendo Real para Libra
        return converter.converterBRparaDolar(reais)*0.79;
    }

    public double ConverterLibraEsterlinaToEuro(double reais){
        // covertendo Libra para Euro
        return reais*1.17;
    }

    public double ConverterEuro(double reais){
        // covertendo Real para Euro
        return converter.converterBRparaDolar(reais)*0.93;
    }

    public double ConverterEuroToLibraEsterlina(double reais){
        // covertendo Euro para Libra
        return reais*0.86;
    }

}
