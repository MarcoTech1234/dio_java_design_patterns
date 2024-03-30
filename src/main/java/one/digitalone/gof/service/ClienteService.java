package one.digitalone.gof.service;

import one.digitalone.gof.model.Cliente;
import one.digitalone.gof.model.Conta;

public interface ClienteService {

	Iterable<Cliente> buscarTodos();

	Cliente buscarPorId(Long id);

	Conta buscarPorNumBancario(Long numBancario);

	void inserir(Cliente cliente);

	void atualizar(Long id, Cliente cliente);

	void deletar(Long id);

	Conta depositar(Long numeroBanco, Double valor);

	Conta sacar(Long numeroBanco, Double valor);

	Conta converterEuro(Long numeroBanco);

	Conta converterLibraEsterlina(Long numeroBanco);
}
