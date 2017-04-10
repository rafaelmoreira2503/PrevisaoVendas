package br.com.confiabilidade.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.confiabilidade.model.Permissao;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.Usuarios;
import br.com.confiabilidade.util.jpa.Transactional;
import br.com.confiabilidade.util.jsf.FacesUtil;

public class CadastroTecnicoService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios tecnicos;

	@Transactional
	public Usuario salvarTecnico(Usuario tecnico) {
		if (tecnico.getEspecialidade() == null ) {
			throw new NegocioException(FacesUtil.getMensagemI18n("especialidade_obrigatoria")); // Este código
																	// é para
																	// que seja
																	// possível
																	// não
																	// colocar a
																	// validação
																	// na view
		}
		
		if (tecnico.getChefe()== null ) {
			throw new NegocioException(FacesUtil.getMensagemI18n("especialidade_obrigatoria")); 
		}
		Usuario usuarioExistente = tecnicos.porEmail(tecnico.getEmail());

		if (usuarioExistente != null && !usuarioExistente.equals(tecnico)) {
			throw new NegocioException(
					FacesUtil.getMensagemI18n("usuario_email_existente"));
		
		}
		
		Usuario tecnicoComEspecialidade = tecnicos.porEspecialidade(tecnico, tecnico.getEspecialidade());

		if (tecnicoComEspecialidade != null && !tecnicoComEspecialidade.equals(tecnico)) {
			throw new NegocioException(
					FacesUtil.getMensagemI18n("especialidade_existente")+tecnico.getEspecialidade().getDescricao()+	FacesUtil.getMensagemI18n("especialidade_existente") + tecnico.getChefe());
		}
		// String password = "mypassword";
		//
		// String salt = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";
		//
		// String hash = JavaHashMD5.md5(password + salt);
		//
		// System.out.println("Conteudo do MD5 : " + hash);
		//
		// tecnico.setSenha(hash);
		try {
			tecnico.setPermissao(Permissao.TECNICO);
			tecnico.setEmpresa(tecnico.getChefe().getEmpresa());
			return tecnicos.guardar(tecnico);
		} catch (OptimisticLockException e) {

			throw new NegocioException(
					FacesUtil.getMensagemI18n("erro_concorrencia"));

		}
		
	}
}