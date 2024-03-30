package one.digitalone.gof.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import feign.FeignException;
import one.digitalone.gof.model.Cliente;
import one.digitalone.gof.model.ClienteRepository;
import one.digitalone.gof.model.Conta;
import one.digitalone.gof.model.ContaRepository;
import one.digitalone.gof.model.Endereco;
import one.digitalone.gof.model.EnderecoRepository;
import one.digitalone.gof.service.AcoesContaService;
import one.digitalone.gof.service.ClienteService;
import one.digitalone.gof.service.ConversaoService;
import one.digitalone.gof.service.ViaCepService;


@Service
public class ClienteServiceImpl implements ClienteService {

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	@Autowired
	private ContaRepository repositoryConta;
	@Autowired
	private ConversaoService converterService;
	

	@Override
	public Iterable<Cliente> buscarTodos() {
		// Buscar todos os Clientes.
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		// Buscar Cliente por ID.
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.get();
	}

	@Override
	public Conta buscarPorNumBancario(Long numBancario) {
		// Buscar Cliente por ID.
		Optional<Conta> conta = repositoryConta.findById(numBancario);
		return conta.get();
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCepEBanco(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		// Buscar Cliente por ID, se o mesmo existir:
		Optional<Cliente> clienteBdOptional = clienteRepository.findById(id);
		if (clienteBdOptional.isEmpty()) {
			throw new RuntimeException("Cliente não encontrado com o ID: " + id);
		}

		if (clienteBdOptional.isPresent()) {
			// Cliente encontrado, atualizar os campos relevantes
			Cliente clienteBd = clienteBdOptional.get();
			salvarClienteComCepEBanco(clienteBd);
		}
	}

	@Override
	public void deletar(Long id) {
		// Deletar cliente por ID.
		clienteRepository.deleteById(id);
	}

	// Depositando Valor Extra
	@Override
	public Conta depositar(Long numeroBanco, Double valor){
		AcoesContaService operacaoDepositar = new Depositar();
		AcoesContaServiceStrategy depositar = new AcoesContaServiceStrategy();
		depositar.setAcoesConta(operacaoDepositar);
		return depositar.acaoConta(numeroBanco, valor, repositoryConta);
	}

	// Sacar Valor 
	@Override
	public Conta sacar(Long numeroBanco, Double valor){
		AcoesContaService operacaoDepositar = new Sacar();
		AcoesContaServiceStrategy depositar = new AcoesContaServiceStrategy();
		depositar.setAcoesConta(operacaoDepositar);
		return depositar.acaoConta(numeroBanco, valor, repositoryConta);
	}

	private void salvarClienteComCepEBanco(Cliente cliente) {
		try {
			// Obter o CEP do endereço do cliente
			String cep = cliente.getEndereco().getCep();
			// Tentar encontrar o endereço no banco de dados
			Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
				// Caso não exista, integrar com o ViaCEP e persistir o retorno.
				Endereco novoEndereco = viaCepService.consultarCep(cep);
				enderecoRepository.save(novoEndereco);
				return novoEndereco;
			});
			// Obter a conta do cliente e salvá-la no repositório
			Conta conta = cliente.getConta();
			conta.setSaldo(converterService.converterBRparaDolar(conta.getSaldo()));
			conta.setTypeMoney("Dolar");
			conta = repositoryConta.save(conta);
			// Vincular o endereço ao cliente
			cliente.setEndereco(endereco);
			// Salvar o cliente no banco de dados
			clienteRepository.save(cliente);
		} catch (FeignException.BadRequest e) {
			// Tratamento específico para erro 400 (BadRequest) do serviço ViaCEP
			throw new RuntimeException("Erro ao consultar o serviço ViaCEP: " + e.getMessage(), e);
		} catch (Exception e) {
			// Outros tratamentos de exceção
			throw new RuntimeException("Erro ao salvar o cliente: " + e.getMessage(), e);
		}
	}

	@Override
	public Conta converterEuro(Long numeroBanco) {
		Optional<Conta> contaBd = repositoryConta.findById(numeroBanco);
		Conta contaB = contaBd.orElseThrow(() -> new RuntimeException("Conta não encontrada com o número bancário: " + numeroBanco));
		ConverterServiceAdapter adapter = new ConverterServiceAdapter();  
		adapter.CurrencyAdapter(converterService);
		if(contaB.getTypeMoney() == "Dolar")
			contaB.setSaldo(adapter.ConverterEuro(contaB.getSaldo()*5));
		else if(contaB.getTypeMoney() == "Libra")
			contaB.setSaldo(adapter.ConverterLibraEsterlinaToEuro(contaB.getSaldo()));
		contaB.setTypeMoney("Euro");
		repositoryConta.save(contaB);
		return contaB;
	}
	
	@Override
	public Conta converterLibraEsterlina(Long numeroBanco) {
		Optional<Conta> contaBd = repositoryConta.findById(numeroBanco);
		Conta contaB = contaBd.orElseThrow(() -> new RuntimeException("Conta não encontrada com o número bancário: " + numeroBanco));
		ConverterServiceAdapter adapter = new ConverterServiceAdapter();
		adapter.CurrencyAdapter(converterService); 
		if(contaB.getTypeMoney() == "Dolar")
			contaB.setSaldo(adapter.ConverterLibraEsterlina(contaB.getSaldo()*5));
		else if(contaB.getTypeMoney() == "Euro")
			contaB.setSaldo(adapter.ConverterEuroToLibraEsterlina(contaB.getSaldo()));
		contaB.setTypeMoney("Libra");
		repositoryConta.save(contaB);
		return contaB;
	}

}