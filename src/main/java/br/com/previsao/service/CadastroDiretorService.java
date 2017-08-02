package br.com.previsao.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.previsao.model.Permissao;
import br.com.previsao.model.Usuario;
import br.com.previsao.repository.Usuarios;
import br.com.previsao.util.jpa.Transactional;
import br.com.previsao.util.jsf.FacesUtil;

public class CadastroDiretorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios diretores;

	@Transactional
	public Usuario salvar(Usuario diretor) {

		if (diretor.getEmpresa() == null) {
			throw new NegocioException(FacesUtil.getMensagemI18n("empresa_obrigatoria")); // Este
																							// código
			// é para
			// que seja
			// possível
			// não
			// colocar a
			// validação
			// na view
		}

		Usuario usuarioExistente = diretores.porEmail(diretor.getEmail());

		if (usuarioExistente != null && !usuarioExistente.equals(diretor)) {
			throw new NegocioException(FacesUtil.getMensagemI18n("usuario_email_existente"));
		}
		// String password = "mypassword";
		// String salt = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";
		//
		// String hash = JavaHashMD5.md5(password + salt);
		//
		// System.out.println("Conteudo do MD5 : " + hash);
		// gerente.setSenha(hash);

		try {
			diretor.setPermissao(Permissao.DIRETOR);
			return diretores.guardar(diretor);
		} catch (OptimisticLockException e) {

			throw new NegocioException(FacesUtil.getMensagemI18n("erro_concorrencia"));

		}

	}

}
