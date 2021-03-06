package br.com.academiadev.bluerefund.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.academiadev.bluerefund.converter.ReembolsoConverter;
import br.com.academiadev.bluerefund.dto.CadastroReembolsoDTO;
import br.com.academiadev.bluerefund.dto.EditaReembolsoDTO;
import br.com.academiadev.bluerefund.dto.ReembolsoDTO;
import br.com.academiadev.bluerefund.exceptions.CategoriaNaoCadastradaException;
import br.com.academiadev.bluerefund.exceptions.EmailNaoEncontradoException;
import br.com.academiadev.bluerefund.exceptions.EmpregadoNaoEncontradoException;
import br.com.academiadev.bluerefund.exceptions.EmpresaNaoEncontradaException;
import br.com.academiadev.bluerefund.exceptions.ReembolsoJaAvaliadoException;
import br.com.academiadev.bluerefund.exceptions.ReembolsoNaoEncontradoException;
import br.com.academiadev.bluerefund.exceptions.ValorInvalidoException;
import br.com.academiadev.bluerefund.model.Categoria;
import br.com.academiadev.bluerefund.model.Empresa;
import br.com.academiadev.bluerefund.model.Reembolso;
import br.com.academiadev.bluerefund.model.StatusReembolso;
import br.com.academiadev.bluerefund.model.Usuario;
import br.com.academiadev.bluerefund.repository.CategoriaRepository;
import br.com.academiadev.bluerefund.repository.EmpresaRepository;
import br.com.academiadev.bluerefund.repository.ReembolsoRepository;
import br.com.academiadev.bluerefund.repository.UsuarioRepository;

@Service
public class ReembolsoService {

	@Autowired
	ReembolsoRepository reembolsoRepository;
	@Autowired
	CategoriaRepository categoriaRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	EmpresaRepository empresaRepository;
	
	public void adiciona(CadastroReembolsoDTO dto) throws EmpregadoNaoEncontradoException, CategoriaNaoCadastradaException {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String email_token = currentUser.getName();
		Usuario usuario = usuarioRepository.findByEmail(email_token);
		
		Categoria categoria = categoriaRepository.findByNome(dto.getCategoria());

		validacoesAdiciona(categoria, usuario);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		Reembolso reembolso = new Reembolso(dto.getNome(),categoria , new BigDecimal(dto.getValorSolicitado()),
				dto.getUploadUrl(), usuario, LocalDate.parse(dto.getData(), formatter));
		
		reembolsoRepository.save(reembolso);
	}

	private void validacoesAdiciona(Categoria categoria, Usuario usuario)
			throws CategoriaNaoCadastradaException, EmpregadoNaoEncontradoException {
		if(categoria == null)
			throw new CategoriaNaoCadastradaException("Categoria não encontrada");
		if(usuario == null)
			throw new EmpregadoNaoEncontradoException("Empregado não encontrado");
	}
	
	public List<ReembolsoDTO> buscaPorEmpregado() throws EmailNaoEncontradoException{
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String email_token = currentUser.getName();
		Usuario usuario = usuarioRepository.findByEmail(email_token);
		
		if(usuario==null)
			throw new EmailNaoEncontradoException("E-mail não encontrado");
			
		List<Reembolso> reembolsos = reembolsoRepository.findByUsuario(usuario);
		
		Collections.sort(reembolsos, new Comparator<Reembolso>() {
			@Override
			public int compare(Reembolso o1, Reembolso o2) {
				if(o1.getStatus().compareTo(o2.getStatus()) == -1)
					return 1;
				if(o1.getStatus().compareTo(o2.getStatus()) == 1)
					return -1;
				return 0;
			}
		});
		
		List<ReembolsoDTO> dtos = new ArrayList<>();
		
		for (Reembolso reembolso : reembolsos) {
			dtos.add(new ReembolsoConverter().toDTO(reembolso));
		}
		return dtos;
	}
	
	public void exclui(Long id) throws ReembolsoNaoEncontradoException{
		Reembolso reembolso = reembolsoRepository.findById(id);
		
		if(reembolso == null)
			throw new ReembolsoNaoEncontradoException("Reembolso não encontrado");
		
		reembolsoRepository.delete(reembolso);
		
	}
	
	public void reprova(Long id) throws ReembolsoNaoEncontradoException {
		Reembolso reembolso = reembolsoRepository.findById(id);
		
		if(reembolso == null)
			throw new ReembolsoNaoEncontradoException("Reembolso não encontrado");
		
		reembolso.setStatus(StatusReembolso.REPROVADO);
		reembolsoRepository.save(reembolso);
	}
	
	public void aprova(Long id, BigDecimal valorReembolsado) throws ReembolsoNaoEncontradoException, ValorInvalidoException {
		Reembolso reembolso = reembolsoRepository.findById(id);
		
		if(reembolso == null)
			throw new ReembolsoNaoEncontradoException("Reembolso não encontrado");
		
		if(valorReembolsado.compareTo(reembolso.getValorSolicitado()) == 1)
			throw new ValorInvalidoException("Valor inválido, o valor reembolsado não pode ser maior que o valor solicitado");
		
		if(valorReembolsado.equals(new BigDecimal(0))) {
			reembolso.setValorReembolsado(reembolso.getValorSolicitado());
		}else {
			reembolso.setValorReembolsado(valorReembolsado);
		}
		
		reembolso.setStatus(StatusReembolso.APROVADO);
		reembolsoRepository.save(reembolso);
	}
	
	public List<ReembolsoDTO> buscaPorEmpresa() throws EmpresaNaoEncontradaException{
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String email_token = currentUser.getName();
		Usuario usuario_token = usuarioRepository.findByEmail(email_token);
		Empresa empresa = usuario_token.getEmpresa();
		
		if(empresa == null)
			throw new EmpresaNaoEncontradaException("Empresa não encontrada");
		
		List<Usuario> usuarios = empresa.getUsuarios();
		List<Reembolso> reembolsos = new ArrayList<>();
		for (Usuario usuario : usuarios) {
			reembolsos.addAll(reembolsoRepository.findByUsuario(usuario));
		}
		
		Collections.sort(reembolsos, new Comparator<Reembolso>() {
			@Override
			public int compare(Reembolso o1, Reembolso o2) {
				if(o1.getStatus().compareTo(o2.getStatus()) == -1)
					return 1;
				if(o1.getStatus().compareTo(o2.getStatus()) == 1)
					return -1;
				return 0;
			}
		});

		List<ReembolsoDTO> dtos = new ArrayList<>();
		
		for (Reembolso reembolso : reembolsos) {
			dtos.add(new ReembolsoConverter().toDTO(reembolso));
		}
		return dtos;
	}
	
	public void edita(EditaReembolsoDTO dto) throws CategoriaNaoCadastradaException, EmpregadoNaoEncontradoException, ReembolsoJaAvaliadoException {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String email_token = currentUser.getName();
		Usuario usuario = usuarioRepository.findByEmail(email_token);
		

		Categoria categoria = categoriaRepository.findByNome(dto.getCategoria());

		validacoesAdiciona(categoria, usuario);
		
		Reembolso reembolso = reembolsoRepository.findById(dto.getId().longValue());
		
		if(!reembolso.getStatus().equals(StatusReembolso.AGUARDANDO))
			throw new ReembolsoJaAvaliadoException("O seu rembolso já foi avaliado e não pode ser editado");
			
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		reembolso.setNome(dto.getNome());
		reembolso.setCategoria(categoria);
		reembolso.setValorSolicitado(new BigDecimal(dto.getValorSolicitado()));
		reembolso.setUrlUpload(dto.getUploadUrl());
		reembolso.setData(LocalDate.parse(dto.getData(), formatter));
		
		reembolsoRepository.save(reembolso);
	}
	
	
	
}
