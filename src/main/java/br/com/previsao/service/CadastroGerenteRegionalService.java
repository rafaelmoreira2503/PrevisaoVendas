package br.com.previsao.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.previsao.model.Permissao;
import br.com.previsao.model.Usuario;
import br.com.previsao.repository.Usuarios;
import br.com.previsao.util.jpa.Transactional;
import br.com.previsao.util.jsf.FacesUtil;

public class CadastroGerenteRegionalService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios encarregados;

	@Transactional
	public Usuario salvarEncarregado(Usuario gerenteRegional, Long codigo) {

		if (gerenteRegional.getChefe() == null) {
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

		
		

		Usuario usuarioExistente = encarregados
				.porEmail(gerenteRegional.getEmail());

		if (usuarioExistente != null && !usuarioExistente.equals(gerenteRegional)) {
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
			gerenteRegional.setEmpresa(gerenteRegional.getChefe().getEmpresa());
			gerenteRegional.setPermissao(Permissao.GERENTEREGIONAL);
			return encarregados.guardar(gerenteRegional);
		} catch (OptimisticLockException e) {

			throw new NegocioException(
					FacesUtil.getMensagemI18n("erro_concorrencia"));

		}

	}

	// Pegue o conteúdo da variável "hash" e atribua à propriedade de senha da
	// entidade Usuario.

}
