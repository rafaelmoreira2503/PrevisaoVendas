package br.com.previsao.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.previsao.model.Permissao;
import br.com.previsao.model.Usuario;
import br.com.previsao.repository.Usuarios;
import br.com.previsao.util.jpa.Transactional;
import br.com.previsao.util.jsf.FacesUtil;

public class CadastroGerenteFilialService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios tecnicos;

	@Transactional
	public Usuario salvarTecnico(Usuario tecnico) {
		
		
		if (tecnico.getChefe()== null ) {
			throw new NegocioException(FacesUtil.getMensagemI18n("especialidade_obrigatoria")); 
		}
		Usuario usuarioExistente = tecnicos.porEmail(tecnico.getEmail());

		if (usuarioExistente != null && !usuarioExistente.equals(tecnico)) {
			throw new NegocioException(
					FacesUtil.getMensagemI18n("usuario_email_existente"));
		
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
			tecnico.setPermissao(Permissao.GERENTEFILIAL);
			tecnico.setEmpresa(tecnico.getChefe().getEmpresa());
			return tecnicos.guardar(tecnico);
		} catch (OptimisticLockException e) {

			throw new NegocioException(
					FacesUtil.getMensagemI18n("erro_concorrencia"));

		}
		
	}
}