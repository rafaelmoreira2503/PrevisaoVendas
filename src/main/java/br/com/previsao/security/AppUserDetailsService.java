package br.com.previsao.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.previsao.model.Usuario;
import br.com.previsao.repository.Usuarios;
import br.com.previsao.util.cdi.CDIServiceLocator;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {
		Usuarios usuarios = CDIServiceLocator.getBean(Usuarios.class);
		Usuario usuario = usuarios.porEmail(email);
		
		UsuarioSistema user = null;

		if (usuario != null) {
			user = new UsuarioSistema(usuario, getPermissoes(usuario));
		}

		return user;
	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		authorities.add(new SimpleGrantedAuthority(usuario.getPermissao()
				.getDescricao().toUpperCase()));

		return authorities;
	}

}
