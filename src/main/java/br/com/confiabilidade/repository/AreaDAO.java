package br.com.confiabilidade.repository;

import java.util.HashMap;
import java.util.Map;

import br.com.confiabilidade.model.Area;

public class AreaDAO extends HibernateGenericDAO<Area> {

	public AreaDAO() {
		super(Area.class);
	}

	public Area findUserByEmail(String email) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("email", email);

		return super.findOneResult(Area.ALL, parameters);
	}

	public Area porNome(String nome) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("descricao", nome.toUpperCase());

		return super.findOneResult(Area.ALL, parameters);

	}
}
