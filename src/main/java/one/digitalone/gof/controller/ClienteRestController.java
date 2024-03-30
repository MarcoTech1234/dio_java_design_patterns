package one.digitalone.gof.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import one.digitalone.gof.handler.CampoObrigatorio;
import one.digitalone.gof.model.Cliente;
import one.digitalone.gof.model.Conta;
import one.digitalone.gof.service.ClienteService;

@RestController
@RequestMapping("clientes")
public class ClienteRestController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping
	public ResponseEntity<Iterable<Cliente>> buscarTodos() {
		return ResponseEntity.ok(clienteService.buscarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
		if(id == null)
			throw new CampoObrigatorio("id");
		return ResponseEntity.ok(clienteService.buscarPorId(id));
	}

	@PostMapping
	public ResponseEntity<Cliente> inserir(@RequestBody Cliente cliente) {
		if(cliente.getNome() == null)
			throw new CampoObrigatorio("nome");
		if(cliente.getEndereco() == null)
			throw new CampoObrigatorio("cep");
			
		clienteService.inserir(cliente);
		return ResponseEntity.ok(cliente);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
		if (id == null) 
			throw new CampoObrigatorio("id");
		if (cliente == null) 
			throw new CampoObrigatorio("cliente");
		if (cliente.getNome() == null || cliente.getNome().isEmpty()) 
			throw new CampoObrigatorio("nome");
		if (cliente.getEndereco() == null || cliente.getEndereco().getCep() == null || cliente.getEndereco().getCep().isEmpty()) 
			throw new CampoObrigatorio("endereço");
		clienteService.atualizar(id, cliente);
		return ResponseEntity.ok(cliente);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		if(id == null)
			throw new CampoObrigatorio("id");
		clienteService.deletar(id);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/depositar/{numeroBanco}&{saldo}")
	public ResponseEntity<Conta> depositar(@PathVariable Long numeroBanco, @PathVariable Double saldo) {
		if(numeroBanco == null)
			throw new CampoObrigatorio("numero da conta bancaria");
		if(saldo == null)
			throw new CampoObrigatorio("saldo");

		// Conta clienteBd = clienteService.buscarPorNumBancario(numeroBanco);
		// clienteBd.getConta().setSaldo(clienteBd.getConta().getSaldo() + saldo);
		Conta clienteBd = clienteService.depositar(numeroBanco, saldo);
		// clienteService.atualizar(numBancario, cliente);
		return ResponseEntity.ok(clienteBd);
	}

	@PutMapping("/coverterEuro/{numeroBanco}")
	public ResponseEntity<Conta> converterEuro(@PathVariable Long numeroBanco){
		if(numeroBanco == null)
			throw new CampoObrigatorio("numero da conta bancaria");
		Conta conta = clienteService.converterEuro(numeroBanco);
		return ResponseEntity.ok(conta);
	}

	@PutMapping("/coverterLibraEsterlina/{numeroBanco}")
	public ResponseEntity<Conta> converterLibraEsterlina(@PathVariable Long numeroBanco){
		if(numeroBanco == null)
			throw new CampoObrigatorio("numero da conta bancaria");
		Conta conta = clienteService.converterLibraEsterlina(numeroBanco);
		return ResponseEntity.ok(conta);
	}
	
	@PutMapping("/sacar/{numeroBanco}&{saldo}")
	public ResponseEntity<Conta> sacar(@PathVariable Long numeroBanco, @PathVariable Double saldo) {
		if(numeroBanco == null)
			throw new CampoObrigatorio("numero da conta bancaria");
		if(saldo == null)
			throw new CampoObrigatorio("saldo");

		// Conta clienteBd = clienteService.buscarPorNumBancario(numeroBanco);
		// clienteBd.getConta().setSaldo(clienteBd.getConta().getSaldo() + saldo);
		Conta clienteBd = clienteService.sacar(numeroBanco, saldo);
		// clienteService.atualizar(numBancario, cliente);
		return ResponseEntity.ok(clienteBd);
	}
}
