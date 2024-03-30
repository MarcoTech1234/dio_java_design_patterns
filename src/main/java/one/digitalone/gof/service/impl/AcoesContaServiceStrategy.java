package one.digitalone.gof.service.impl;

import java.util.Optional;

import one.digitalone.gof.model.Conta;
import one.digitalone.gof.model.ContaRepository;
import one.digitalone.gof.service.AcoesContaService;

public class AcoesContaServiceStrategy {
    private AcoesContaService operacao;
    
    public void setAcoesConta(AcoesContaService operation){
        this.operacao = operation;
    }

    public Conta acaoConta(Long numeroBanco, Double valor,ContaRepository repositoryConta){
        return this.operacao.acaoSaldo(numeroBanco, valor, repositoryConta);
    }
}

class Sacar implements AcoesContaService{

    @Override
    public Conta acaoSaldo(Long numeroBanco, Double valor, ContaRepository repositoryConta) {
        Optional<Conta> contaBd = repositoryConta.findById(numeroBanco);
		Conta contaB = contaBd.orElseThrow(() -> new RuntimeException("Conta não encontrada com o número bancário: " + numeroBanco));
		contaB.setSaldo(contaB.getSaldo() - valor);
		repositoryConta.save(contaB);
        return contaB;
    }
    
}

class Depositar implements AcoesContaService{

    // Depositando Valor Extra
    @Override
	public Conta acaoSaldo(Long numeroBanco, Double valor, ContaRepository repositoryConta){
		Optional<Conta> contaBd = repositoryConta.findById(numeroBanco);
		Conta contaB = contaBd.orElseThrow(() -> new RuntimeException("Conta não encontrada com o número bancário: " + numeroBanco));
		contaB.setSaldo(contaB.getSaldo() + valor);
		repositoryConta.save(contaB);
        return contaB;
	}
    
}