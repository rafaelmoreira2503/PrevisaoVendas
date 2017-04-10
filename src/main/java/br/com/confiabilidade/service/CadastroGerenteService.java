package br.com.confiabilidade.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.confiabilidade.model.Permissao;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.Usuarios;
import br.com.confiabilidade.util.jpa.Transactional;
import br.com.confiabilidade.util.jsf.FacesUtil;

public class CadastroGerenteService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios gerentes;

	@Transactional
	public Usuario salvar(Usuario gerente) {

		
		
		if (gerente.getEmpresa() == null) {
			throw new NegocioException(FacesUtil.getMensagemI18n("empresa_obrigatoria")); // Este código
																	// é para
																	// que seja
																	// possível
																	// não
																	// colocar a
																	// validação
																	// na view
		}

		Usuario usuarioExistente = gerentes.porEmail(gerente.getEmail());

		if (usuarioExistente != null && !usuarioExistente.equals(gerente)) {
			throw new NegocioException(
					FacesUtil.getMensagemI18n("usuario_email_existente"));
		}
		// String password = "mypassword";
		// String salt = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";
		//
		// String hash = JavaHashMD5.md5(password + salt);
		//
		// System.out.println("Conteudo do MD5 : " + hash);
		// gerente.setSenha(hash);


		try {
			gerente.setPermissao(Permissao.GERENTE);
			return gerentes.guardar(gerente);
		} catch (OptimisticLockException e) {

			throw new NegocioException(
					FacesUtil.getMensagemI18n("erro_concorrencia"));

		}
		

	}

}
