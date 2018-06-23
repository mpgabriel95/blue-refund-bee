package br.com.academiadev.bluerefund;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.academiadev.bluerefund.controller.AutenticacaoController;
import br.com.academiadev.bluerefund.controller.UsuarioController;
import br.com.academiadev.bluerefund.dto.CadastroAdminDTO;
import br.com.academiadev.bluerefund.dto.CadastroPorCodigoDTO;
import br.com.academiadev.bluerefund.dto.LoginDTO;
import br.com.academiadev.bluerefund.exceptions.CodigosInconsistentesException;
import br.com.academiadev.bluerefund.exceptions.EmailInvalidoException;
import br.com.academiadev.bluerefund.exceptions.EmailJaCadastradoException;
import br.com.academiadev.bluerefund.exceptions.EmpresaNaoEncontradaException;
import br.com.academiadev.bluerefund.exceptions.SenhaInvalidaException;
import br.com.academiadev.bluerefund.model.Autorizacao;
import br.com.academiadev.bluerefund.repository.AutorizacaoRepository;
import br.com.academiadev.bluerefund.repository.CategoriaRepository;
import br.com.academiadev.bluerefund.repository.EmpresaRepository;
import br.com.academiadev.bluerefund.repository.ReembolsoRepository;
import br.com.academiadev.bluerefund.repository.UsuarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BluerefundApplicationTests {
	
	//Repositorys
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private EmpresaRepository empresaRepository;
	@Autowired
	private ReembolsoRepository reembolsoRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private AutorizacaoRepository autorizacaoRepository;
	
	//Controllers
	@Autowired
	private UsuarioController usuarioController;
	@Autowired
	private AutenticacaoController autenticacaoController;
	
	
//	@Test
//	public void apagaTudo() {
//		categoriaRepository.deleteAll();
//		reembolsoRepository.deleteAll();
//		empresaRepository.deleteAll();
//		usuarioRepository.deleteAll();
//		
//		Autorizacao aut1 = new Autorizacao();
//		aut1.setNome("ROLE_USER");
//		autorizacaoRepository.save(aut1);
//		
//		Autorizacao aut2 = new Autorizacao();
//		aut1.setNome("ROLE_ADMIN");
//		autorizacaoRepository.save(aut2);
//		
//	}
//	
//	@Test
//	public void cadastraAdminEEmpresaEmailInvalido() throws SenhaInvalidaException, EmailInvalidoException, EmailJaCadastradoException {
//		CadastroAdminDTO dto = new CadastroAdminDTO();
//		dto.setEmail("emailErrado");
//		dto.setNome("Admin1");
//		dto.setNomeEmpresa("Empresa1");
//		dto.setSenha("senha_admin1");
//		
//		try {
//			usuarioController.adminEEmpresa(dto);
//		} catch (EmailInvalidoException e) {
//			Assert.assertTrue(true);
//			return;
//		}
//		
//		Assert.assertTrue(false);
//	}
//	
//	@Test
//	public void cadastraAdminEEmpresaSenhaInvalida() throws SenhaInvalidaException, EmailInvalidoException, EmailJaCadastradoException {
//		CadastroAdminDTO dto = new CadastroAdminDTO();
//		dto.setEmail("admin1@dominio.com");
//		dto.setNome("Admin1");
//		dto.setNomeEmpresa("Empresa1");
//		dto.setSenha("senhaErrada");
//		
//		try {
//			usuarioController.adminEEmpresa(dto);
//		} catch (SenhaInvalidaException e) {
//			Assert.assertTrue(true);
//			return;
//		}
//		
//		Assert.assertTrue(false);
//	}
//	
	@Test
	public void cadastraAdminEEmpresa() throws SenhaInvalidaException, EmailInvalidoException, EmailJaCadastradoException {
		CadastroAdminDTO dto = new CadastroAdminDTO();
		dto.setEmail("admin1@dominio.com");
		dto.setNome("Admin1");
		dto.setNomeEmpresa("Empresa1");
		dto.setSenha("minha_senha1");
		usuarioController.adminEEmpresa(dto);
	}
//	
//	@Test
//	public void cadastraAdminEEmpresaEmailJaCadastrado() throws SenhaInvalidaException, EmailInvalidoException, EmailJaCadastradoException {
//		CadastroAdminDTO dto = new CadastroAdminDTO();
//		dto.setEmail("admin1@dominio.com");
//		dto.setNome("Admin1");
//		dto.setNomeEmpresa("Empresa1");
//		dto.setSenha("minha_senha1");
//		
//		try {
//			usuarioController.adminEEmpresa(dto);
//		} catch (EmailJaCadastradoException e) {
//			Assert.assertTrue(true);
//			return;
//		}
//		
//		Assert.assertTrue(false);
//	}
//	
//	@Test
//	public void cadastraPorCodigoEmpresaNaoEncontrada() throws SenhaInvalidaException, EmailInvalidoException, EmailJaCadastradoException, CodigosInconsistentesException, EmpresaNaoEncontradaException {
//		CadastroPorCodigoDTO dto = new CadastroPorCodigoDTO();
//		dto.setEmail("empregado1@dominio.com");
//		dto.setCodigoEmpresa(0);
//		dto.setNome("empregado1");
//		dto.setSenha("minha_senha1");
//		
//		try {
//			usuarioController.porCodigo(dto);
//		} catch (EmpresaNaoEncontradaException e) {
//			Assert.assertTrue(true);
//			return;
//		}
//		
//		Assert.assertTrue(false);
//	}
//	
//	@Test
//	public void cadastraPorCodigoSenhaInvalida() throws SenhaInvalidaException, EmailInvalidoException, EmailJaCadastradoException, CodigosInconsistentesException, EmpresaNaoEncontradaException {
//		CadastroPorCodigoDTO dto = new CadastroPorCodigoDTO();
//		dto.setEmail("empregado1@dominio.com");
//		dto.setCodigoEmpresa(empresaRepository.findByNome("Empresa1").getCodigo());
//		dto.setNome("empregado1");
//		dto.setSenha("senhaErrada");
//		
//		try {
//			usuarioController.porCodigo(dto);
//		} catch (SenhaInvalidaException e) {
//			Assert.assertTrue(true);
//			return;
//		}
//		
//		Assert.assertTrue(false);
//	}
//	
//	@Test
//	public void cadastraPorCodigoEmailJaCadastrado() throws SenhaInvalidaException, EmailInvalidoException, EmailJaCadastradoException, CodigosInconsistentesException, EmpresaNaoEncontradaException {
//		CadastroPorCodigoDTO dto = new CadastroPorCodigoDTO();
//		dto.setEmail("admin1@dominio.com");
//		dto.setCodigoEmpresa(empresaRepository.findByNome("Empresa1").getCodigo());
//		dto.setNome("empregado1");
//		dto.setSenha("minha_senha1");
//		
//		try {
//			usuarioController.porCodigo(dto);
//		} catch (EmailJaCadastradoException e) {
//			Assert.assertTrue(true);
//			return;
//		}
//		
//		Assert.assertTrue(false);
//	}
//	
//	@Test
//	public void cadastraPorCodigo() throws SenhaInvalidaException, EmailInvalidoException, EmailJaCadastradoException, CodigosInconsistentesException, EmpresaNaoEncontradaException {
//		CadastroPorCodigoDTO dto = new CadastroPorCodigoDTO();
//		dto.setEmail("empregado1@dominio.com");
//		dto.setCodigoEmpresa(empresaRepository.findByNome("Empresa1").getCodigo());
//		dto.setNome("empregado1");
//		dto.setSenha("minha_senha1");
//		
//		usuarioController.porCodigo(dto);
//	}
	
	
	@Test
	public void login() {
		LoginDTO dto = new LoginDTO();
		dto.setEmail("admin1@dominio.com");
		dto.setSenha("minha_senha1");
	}
	
	
}
