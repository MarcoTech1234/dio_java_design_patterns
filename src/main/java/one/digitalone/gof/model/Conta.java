package one.digitalone.gof.model;

import javax.persistence.*;

@Entity
public class Conta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_bancario")
    private Long numBancario;
    @Column(length = 20, nullable = false)
    private String typeMoney;
    private Double saldo;

    // Metodos especiais
    
    public Long getNumBancario() {
        return numBancario;
    }
    public void setNumBancario(Long numBancario) {
        this.numBancario = numBancario;
    }
    public Double getSaldo() {
        return saldo;
    }
    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
    public String getTypeMoney() {
        return typeMoney;
    }
    public void setTypeMoney(String typeMoney) {
        this.typeMoney = typeMoney;
    }
    
}
