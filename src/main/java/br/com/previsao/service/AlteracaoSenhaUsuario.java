package br.com.previsao.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.previsao.model.Usuario;
import br.com.previsao.repository.Usuarios;
import br.com.previsao.security.UsuarioLogado;
import br.com.previsao.util.jpa.Transactional;
import br.com.previsao.util.jsf.FacesUtil;

public class AlteracaoSenhaUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	@Inject
	private Usuarios usuarios;

	// MessageDigest m = MessageDigest.getInstance("MD5");
	// m.update(senha.getBytes(), 0, senha.length());
	// String hash = new BigInteger(1, m.digest()).toString(16);
	//
	// System.out.println(hash);
	//
	// oldPassword = HashUtil.generateMD5(oldPassword);

	@Transactional
	public void alterarSenha(String oldPassword, String newPassword) {

	
		if (this.usuario.getSenha().equals(oldPassword)) {

			Usuario logado = usuarios.porId(usuario.getCodigo());

			// criptografa a senha nova informada pelo usuário
			logado.setSenha(newPassword);

			usuarios.guardar(logado);

			FacesUtil.addInfoMessage("Senha"
					+ FacesUtil.getMensagemI18n("salvo"));

		} else {
			FacesUtil.addInfoMessage(FacesUtil
					.getMensagemI18n("senha_nao_confere"));
		}

	}

}