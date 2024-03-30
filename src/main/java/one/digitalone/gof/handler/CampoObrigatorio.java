package one.digitalone.gof.handler;

public class CampoObrigatorio extends BusinessException{

    public CampoObrigatorio(String campo) {
        super("O campo %s é Obrigatorio", campo);
    }
    
}
