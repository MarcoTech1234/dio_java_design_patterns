package one.digitalone.gof.service;

import one.digitalone.gof.model.Conta;
import one.digitalone.gof.model.ContaRepository;

/*
    vou usar essa interface para criar as ações
    usando a design de Strategy para lidar com
    essas possivies ações na conta.
*/ 

public interface AcoesContaService {
    Conta acaoSaldo(Long numeroBanco, Double valor, ContaRepository repositoryConta);
}
