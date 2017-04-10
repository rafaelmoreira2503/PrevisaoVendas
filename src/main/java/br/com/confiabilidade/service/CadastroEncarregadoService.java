package br.com.confiabilidade.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.confiabilidade.model.Permissao;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.Usuarios;
import br.com.confiabilidade.util.jpa.Transactional;
import br.com.confiabilidade.util.jsf.FacesUtil;

public class CadastroEncarregadoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios encarregados;

	@Transactional
	public Usuario salvarEncarregado(Usuario encarregado, Long codigo) {

		if (encarregado.getChefe() == null) {
			throw new NegocioException(
					FacesUtil.getMensagemI18n("gerente_obrigatorio")); // Este
			// código
			// é para
			// que seja
			// possível
			// não
			// colocar a
			// validação
			// na view
		}

		if (encarregado.getAreaEncarregado() == null) {
			throw new NegocioException(
					FacesUtil.getMensagemI18n("area_obrigatoria")); // Este
																	// código
			// é para
			// que seja
			// possível
			// não
			// colocar a
			// validação
			// na view
		}

		Usuario encarregadoExistente = encarregados.porArea(encarregado
				.getAreaEncarregado().getCodigo(), codigo);

		if (encarregadoExistente != null
				&& !encarregadoExistente.equals(encarregado)) {
			throw new NegocioException(
					FacesUtil.getMensagemI18n("encarregado_area_existente"));
		}

		Usuario usuarioExistente = encarregados
				.porEmail(encarregado.getEmail());

		if (usuarioExistente != null && !usuarioExistente.equals(encarregado)) {
			throw new NegocioException(
					FacesUtil.getMensagemI18n("usuario_email_existente"));
		}
		// String password = "mypassword";
		//
		// String salt = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";
		//
		// String hash = JavaHashMD5.md5(password + salt);
		//
		// System.out.println("Conteudo do SHA : " + hash);
		// encarregado.setSenha(hash);

		try {
			encarregado.setEmpresa(encarregado.getChefe().getEmpresa());
			encarregado.setPermissao(Permissao.ENCARREGADO);
			return encarregados.guardar(encarregado);
		} catch (OptimisticLockException e) {

			throw new NegocioException(
					FacesUtil.getMensagemI18n("erro_concorrencia"));

		}

	}

	// Pegue o conteúdo da variável "hash" e atribua à propriedade de senha da
	// entidade Usuario.

}
