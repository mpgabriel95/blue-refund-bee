package br.com.academiadev.bluerefund.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.academiadev.bluerefund.dto.UsuarioDTO;
import br.com.academiadev.bluerefund.dto.CadastroAdminDTO;
import br.com.academiadev.bluerefund.dto.CadastroPorCodigoDTO;
import br.com.academiadev.bluerefund.dto.EmpresaDTO;
import br.com.academiadev.bluerefund.dto.LoginDTO;
import br.com.academiadev.bluerefund.dto.NovaSenhaDTO;
import br.com.academiadev.bluerefund.dto.RecuperaSenhaDTO;
import br.com.academiadev.bluerefund.dto.RoleDTO;
import br.com.academiadev.bluerefund.exceptions.CodigosInconsistentesException;
import br.com.academiadev.bluerefund.exceptions.EmailInvalidoException;
import br.com.academiadev.bluerefund.exceptions.EmailJaCadastradoException;
import br.com.academiadev.bluerefund.exceptions.EmailNaoEncontradoException;
import br.com.academiadev.bluerefund.exceptions.EmpresaNaoEncontradaException;
import br.com.academiadev.bluerefund.exceptions.SenhaIncorretaException;
import br.com.academiadev.bluerefund.exceptions.SenhaInvalidaException;
import br.com.academiadev.bluerefund.exceptions.SenhaTrocadaRecentementeException;
import br.com.academiadev.bluerefund.exceptions.SenhasDiferentesException;
import br.com.academiadev.bluerefund.service.AdminService;
import br.com.academiadev.bluerefund.service.CadastroService;
import br.com.academiadev.bluerefund.service.EmpresaService;
import br.com.academiadev.bluerefund.service.SenhaService;
import br.com.academiadev.bluerefund.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api("Usuario")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private SenhaService senhaService;
	@Autowired
	private CadastroService cadastroService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private EmpresaService empresaService;
	
	@ApiImplicitParams({ //
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") //
	})
	@ApiOperation(value = "Troca a senha do usuário")
	@PostMapping("/novasenha")
	public void novaSenha(@RequestBody NovaSenhaDTO dto)
			throws EmailNaoEncontradoException, SenhaIncorretaException, SenhaInvalidaException, SenhasDiferentesException, EmailInvalidoException, SenhaTrocadaRecentementeException {
		senhaService.novaSenha(dto.getSenhaAntiga(), dto.getNovaSenha(), dto.getEmail());
	}
	
	@ApiOperation(value = "Troca a senha do usuário e a envia por e-mail")
	@PostMapping("/recuperasenha")
	public void recuperaSenha(@RequestBody RecuperaSenhaDTO dto) throws EmailNaoEncontradoException, MessagingException {
		senhaService.recuperaSenha(dto.getEmail());
	}
	
	@ApiOperation(value = "Cadastra um admin ou empregado pelo código da empresa")
	@PostMapping("/cadastro/porcodigo")
	public void porCodigo(@RequestBody CadastroPorCodigoDTO dto) throws CodigosInconsistentesException, SenhaInvalidaException, EmailInvalidoException, EmailJaCadastradoException, EmpresaNaoEncontradaException {
		cadastroService.cadastroComCodigo(dto);
	}
	
	@ApiOperation(value = "Cadastra um admin e uma empresa")
	@PostMapping("/cadastro/admineempresa")
	public void adminEEmpresa(@RequestBody CadastroAdminDTO cadastroAdminDTO) 
			throws SenhaInvalidaException, EmailInvalidoException, EmailJaCadastradoException {
		adminService.cadastrarComEmpresa(cadastroAdminDTO.getNome(), cadastroAdminDTO.getEmail(), cadastroAdminDTO.getSenha(), cadastroAdminDTO.getEmpresa());
	}
	
	@ApiImplicitParams({ //
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") //
	})
	@ApiOperation(value = "Exclui um usuário")
	@DeleteMapping("/excluir")
	public void exclui(@RequestBody LoginDTO loginDTO) throws EmailInvalidoException, SenhaIncorretaException  {
		cadastroService.excluiCadastro(loginDTO);
	}
	
	@ApiImplicitParams({ //
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") //
	})
	@ApiOperation(value = "Busca dados do usuário")
	@GetMapping("/dadosUsuario")
	public UsuarioDTO dadosUsuario(){
		return usuarioService.dadosUsuario();
	}
	
	@ApiImplicitParams({ //
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") //
	})
	@ApiOperation(value = "Busca dados da empresa referente ao admin")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/dadosEmpresaAdmin")
	public EmpresaDTO dadosEmpresaAdmin(){
		return empresaService.dadosEmpresa();
	}
	
	@ApiImplicitParams({ //
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header") //
	})
	@ApiOperation(value = "Retorna a role do usuário")
	@GetMapping("/role")
	public RoleDTO role() {
		return usuarioService.roleUsuario();
	}
	
	
}
