package br.com.confiabilidade.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.SortOrder;

import br.com.confiabilidade.model.Area;
import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.filter.AssociaItemFilter;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jpa.Transactional;

public class Items implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	public Item porId(Long id) {
		return manager.find(Item.class, id);
	}

	public Item guardar(Item item) {

		return manager.merge(item);
	}

	@Transactional
	public void remover(Item item) {
		try {
			item = porId(item.getCodigo());
			manager.remove(item);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Item não pode ser excluído.");
		}
	}

	public Item buscaItemExistente(Item item, Area area) {
		try {
			return manager.createNamedQuery(Item.ITEMEXISTENTE, Item.class)
					.setParameter("codigo", item.getCodigo())
					.setParameter("area", area.getCodigo())

					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public List<String> buscaCodigo(Empresa empresa) {

		return manager
				.createQuery(
						"select i.codigoItem from Item i where i.empresa=:empresa group by i.codigoItem",
						String.class).setParameter("empresa", empresa)

				.getResultList();

	}
	
	public Item buscaItensDaEmpresa(Empresa empresa, String codigoItem) {

		System.out.println("CODIGO DO ITEM "+codigoItem);
		try {
		return manager
				.createQuery(
						"select i from Item i where i.empresa=:empresa and i.codigoItem=:codigoItem group by i.codigoItem",
						Item.class).setParameter("empresa", empresa)
						.setParameter("codigoItem", codigoItem.trim())

				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public Item buscaItemComTecnicoExistente(Long item, Long area, Long tecnico) {
		try {
			return manager.createNamedQuery(Item.TECNICOEXISTENTE, Item.class)
					.setParameter("codigo", item).setParameter("area", area)
					.setParameter("tecnico", tecnico)

					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public Long buscaTotalParaGerente(Long codigo) {
		try {
			return manager.createNamedQuery(Item.TOTALGERENTE, Long.class)
					.setParameter("codigo", codigo)

					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public Long buscaFeitoParaGerente(Long codigo) {
		try {
			return manager.createNamedQuery(Item.FEITOPARAGERENTE, Long.class)
					.setParameter("codigo", codigo)

					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public Long buscaTotalParaEncarregado(Long codigoArea, Long codigoEmpresa) {
		try {
			return manager.createNamedQuery(Item.TOTALENCARREGADO, Long.class)
					.setParameter("codigoArea", codigoArea)
					.setParameter("codigoEmpresa", codigoEmpresa)

					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public Long buscaTotalFeitoParaEncarregado(Long codigoArea,
			Long codigoEmpresa) {
		try {
			return manager
					.createNamedQuery(Item.FETOPARAENCARREGADO, Long.class)
					.setParameter("codigoArea", codigoArea)
					.setParameter("codigoEmpresa", codigoEmpresa)

					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public Long buscaTotalFeitoParaTecnico(Long codigo) {
		try {
			return manager.createNamedQuery(Item.FETOPARATECNICO, Long.class)
					.setParameter("codigo", codigo)

					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public Long buscaTotalAssociado(Long codigo) {
		try {
			return manager.createNamedQuery(Item.TOTALTECNICO, Long.class)
					.setParameter("codigo", codigo)

					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public List<Item> buscaItemsDaEmpresa(Empresa empresa) {

		return manager.createNamedQuery(Item.ITEMDAEMPRESA, Item.class)
				.setParameter("empresa", empresa)

				.getResultList();

	}

	

	// Abaixo se refere a Implementação do Lazy Loading Data Table

		public int countAll(Map<String, String> filtros) {
			String jpql = Item.TOTALTECNICO;

			jpql = adicionarParametros(filtros, jpql);

			TypedQuery<Long> query = manager.createNamedQuery(jpql, Long.class)
					.setParameter("codigo", usuario.getCodigo());

			popularParametros(query, filtros);

			return query.getSingleResult().intValue();
		}

		public List<Item> buscaPorPaginacao(int posicaoPrimeiraLinha,
				int maximoPorPagina, String ordernarPeloCampo,
				String ordernarAscOuDesc, Map<String, String> filtros) {

			// Todos os items do tecnico = codigo
			String jpql = "select distinct(i) from Item i join i.tecnico u where u.codigo=:codigo";
			jpql = adicionarParametros(filtros, jpql);

			if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

				if (ordernarAscOuDesc.contains("DESC")) {
					ordernarAscOuDesc = "DESC";
				} else {
					ordernarAscOuDesc = "ASC";
				}

				jpql += " ORDER BY i." + ordernarPeloCampo + " "
						+ ordernarAscOuDesc;
			}
			// metodo buscarPorPaginacao
			TypedQuery<Item> query = manager.createQuery(jpql, Item.class)
					.setParameter("codigo", usuario.getCodigo());

			popularParametros(query, filtros);

			query.setFirstResult(posicaoPrimeiraLinha);
			query.setMaxResults(maximoPorPagina);
			return query.getResultList();
		}

	// INICIO DE BUSCA OS ITEMS CRITICADOS
	// ===================================================================//
	public int countAllCriticados(Map<String, String> filtros) {
		String jpql = "select count(i) from Item i where i.criticidade=(select min(m.criticidade) "
				+ "from Item m where i.codigoItem=m.codigoItem) and i.empresa.codigo=:codigo group by i.codigoItem";
		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = manager.createQuery(jpql, Long.class)
				.setParameter("codigo", usuario.getEmpresa().getCodigo());
		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public List<Item> buscaPorPaginacaoCriticados(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo,
			String ordernarAscOuDesc, Map<String, String> filtros) {

		// Todos os items onde a area do item é a area do encarregado
		String jpql = "select i from Item i where i.criticidade=(select min(m.criticidade) "
				+ "from Item m where i.codigoItem=m.codigoItem) and i.empresa.codigo=:codigo group by i.codigoItem";

		jpql = adicionarParametros(filtros, jpql);

		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernarAscOuDesc.contains("DESC")) {
				ordernarAscOuDesc = "DESC";
			} else {
				ordernarAscOuDesc = "ASC";
			}

			jpql += " ORDER BY i." + ordernarPeloCampo + " "
					+ ordernarAscOuDesc;
		}
		// metodo buscarPorPaginacao
		TypedQuery<Item> query = manager.createQuery(jpql, Item.class)
				.setParameter("codigo", usuario.getEmpresa().getCodigo());

		popularParametros(query, filtros);

		query.setFirstResult(posicaoPrimeiraLinha);
		query.setMaxResults(maximoPorPagina);
		return query.getResultList();
	}

	// Evitando SQL Injection
	private void popularParametros(TypedQuery<?> query,
			Map<String, String> filtros) {
		for (Map.Entry<String, String> entry : filtros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue() + "%");
		}
	}

	private String adicionarParametros(Map<String, String> filtros, String jpql) {
		if (filtros != null && !filtros.isEmpty()) {
			if (!jpql.contains("where")) {
				jpql += " where ";
			} else {
				jpql += " and i.";
			}
			for (Map.Entry<String, String> entry : filtros.entrySet()) {
				jpql += entry.getKey() + " like :" + entry.getKey() + " and ";
			}

			// remove ultimo and desnecessario
			jpql = jpql.substring(0, jpql.length() - 4);
		}

		return jpql;
	}

	// ======================= FIM DE BUSCA ITEMS CRITICADOS================================//

	@SuppressWarnings("unchecked")
	public List<Item> buscaPorPaginacao(int first, int pageSize,
			String sortField, SortOrder sortOrder, AssociaItemFilter filtros) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Item.class);
		criteria.createAlias("empresa", "e");
		criteria.createAlias("areaItem", "a");

		criteria.add(Restrictions.eq("e.codigo", usuario.getEmpresa()
				.getCodigo()));
		criteria.add(Restrictions.eq("a.codigo", usuario.getAreaEncarregado()
				.getCodigo()));

		if (StringUtils.isNotBlank(filtros.getDescricao())) {

			criteria.add(Restrictions.ilike("descricao",
					filtros.getDescricao(), MatchMode.START));
		}

		if (filtros.getStatuses() != null && filtros.getStatuses().length > 0) {
			// adicionamos uma restrição "in", passando um array de constantes
			// da enum StatusItem
			criteria.add(Restrictions.in("status", filtros.getStatuses()));
		}

		criteria.setFirstResult(first);
		criteria.setMaxResults(pageSize);

		// eliminando resultados repetidos
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.addOrder(Order.asc("codigoItem")).list();

	}

	public int countAll(AssociaItemFilter filtros) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Item.class)

		.setProjection(Projections.rowCount());

		criteria.createAlias("empresa", "e");
		criteria.createAlias("areaItem", "a");

		criteria.add(Restrictions.eq("e.codigo", usuario.getEmpresa()
				.getCodigo()));

		criteria.add(Restrictions.eq("a.codigo", usuario.getAreaEncarregado()
				.getCodigo()));

		if (StringUtils.isNotBlank(filtros.getDescricao())) {

			criteria.add(Restrictions.ilike("descricao",
					filtros.getDescricao(), MatchMode.START));
		}

		if (filtros.getStatuses() != null && filtros.getStatuses().length > 0) {
			// adicionamos uma restrição "in", passando um array de constantes
			// da enum StatusItem
			criteria.add(Restrictions.in("status", filtros.getStatuses()));

		}
		// eliminando resultados repetidos
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		int total = ((Number) criteria.uniqueResult()).intValue();

		return total;
	}

	// --- INICIO RELATORIO PARA ENCARREGADO ---//

	public Map<String, Long> quantidadeCriticadoFeitoParaEncarregado(
			Usuario tecnico) {

		Map<String, Long> resultado = new TreeMap<>();

		List<Object[]> itemsdoEncarregado = manager
				.createQuery(
						"select  count(m), m from Item m where"
								+ " m.status='CRITICADO' and m.empresa.codigo=:codigo "
								+ "and m.tecnico.codigo=:tecnico",
						Object[].class)
				.setParameter("codigo", tecnico.getEmpresa().getCodigo())
				.setParameter("tecnico", tecnico.getCodigo())

				.getResultList();

		for (Object[] usuarioQuantidade : itemsdoEncarregado) {
			resultado.put(usuarioQuantidade[1] == null ? "CRITICADOS - 0"
					: "CRITICADOS" + " - " + usuarioQuantidade[0],
					(usuarioQuantidade[0] == null ? 0L
							: (Long) usuarioQuantidade[0]));

		}
		return resultado;
	}

	public Map<String, Long> quantidadeJustificadoFeitoParaEncarregado(
			Usuario tecnico) {

		Map<String, Long> resultado = new TreeMap<>();

		List<Object[]> itemsdoEncarregado = manager
				.createQuery(
						"select  count(m), m from Item m where"
								+ " m.status='JUSTIFICADO' and m.empresa.codigo=:codigo "
								+ "and m.tecnico.codigo=:tecnico",
						Object[].class)
				.setParameter("codigo", tecnico.getEmpresa().getCodigo())
				.setParameter("tecnico", tecnico.getCodigo())

				.getResultList();

		for (Object[] usuarioQuantidade : itemsdoEncarregado) {

			resultado.put(usuarioQuantidade[1] == null ? "JUSTIFICADOS - 0"
					: "JUSTIFICADOS" + " - " + usuarioQuantidade[0],
					usuarioQuantidade[0] == null ? 0L
							: ((Long) usuarioQuantidade[0]));

		}

		return resultado;
	}

	public Map<String, Long> quantidadePendenteParaEncarregado(Usuario tecnico) {

		Map<String, Long> resultado = new TreeMap<>();

		Long itemsdoEncarregado = manager
				.createQuery(
						"select count(i) from Item i where i.areaItem.codigo=:codigoArea and"
								+ " i.status='ASSOCIADO' and i.empresa.codigo=:codigo and i.tecnico.codigo=:codigoTecnico",
						Long.class)
				.setParameter("codigo", tecnico.getEmpresa().getCodigo())
				.setParameter("codigoArea",
						tecnico.getChefe().getAreaEncarregado().getCodigo())
				.setParameter("codigoTecnico", tecnico.getCodigo())

				.getSingleResult();

		resultado.put("PENDENTES" + " - " + itemsdoEncarregado,
				itemsdoEncarregado);

		return resultado;
	}

	public Map<String, Long> quantidadeNaoAplicavelParaEncarregado(
			Usuario encarregado) {

		Map<String, Long> resultado = new TreeMap<>();

		Long itemsdoEncarregado = manager
				.createQuery(
						"select count(i) from Item i where i.areaItem.codigo=:codigoArea and "
								+ "i.status='NAOSEAPLICA' and i.empresa.codigo=:codigo",
						Long.class)
				.setParameter("codigo", encarregado.getEmpresa().getCodigo())
				.setParameter("codigoArea",
						encarregado.getAreaEncarregado().getCodigo())

				.getSingleResult();

		resultado.put("NÃO SE APLICAM" + " - " + itemsdoEncarregado,
				itemsdoEncarregado);

		return resultado;
	}

	public Map<String, Long> quantidadeTecnicos(Usuario encarregado) {

		Map<String, Long> resultado = new TreeMap<>();
		List<Object[]> itemsdoTecnico = manager
				.createQuery(
						"select count(m),  m  from Item m where (m.status='CRITICADO'or m.status='JUSTIFICADO' or m.status='ASSOCIADO') and m.empresa.codigo=:codigo "
								+ " and m.areaItem.codigo=:codigoArea group by m.tecnico ",
						Object[].class)
				.setParameter("codigo", encarregado.getEmpresa().getCodigo())
				.setParameter("codigoArea",
						encarregado.getAreaEncarregado().getCodigo())

				.getResultList();

		for (Object[] usuarioQuantidade : itemsdoTecnico) {

			resultado.put(usuarioQuantidade[1] == null ? null
					: ((Item) usuarioQuantidade[1]).getTecnico().getNome()
							+ " - " + usuarioQuantidade[0],
					usuarioQuantidade[0] == null ? 0L
							: ((Long) usuarioQuantidade[0]));

		}

		return resultado;

	}

	public Map<String, Long> quantidadeTotalParaEncarregdo(Usuario encarregado) {

		Map<String, Long> resultado = new TreeMap<>();

		Long itemsdoGerente = manager
				.createQuery(
						"select count(i) from Item i where  i.empresa.codigo=:codigo and i.status='PENDENTE' and i.areaItem.codigo=:codigoArea",
						Long.class)
				.setParameter("codigo", encarregado.getEmpresa().getCodigo())
				.setParameter("codigoArea",
						encarregado.getAreaEncarregado().getCodigo())

				.getSingleResult();

		resultado.put("PENDENTES" + " - " + itemsdoGerente, itemsdoGerente);

		return resultado;
	}

	public Map<Date, Integer> quantidadePorDataParaEncarregado(Usuario tecnico) {

		Map<Date, Integer> resultado = new TreeMap<>();

		List<Object[]> itemsdoEncarregado = manager
				.createQuery(
						"select m.dataItem , count(m) "
								+ "from Item m where  m.empresa.codigo=:codigo and m.tecnico.codigo=tecnico and "
								+ "m.status<>'PENDENTE' group by m.dataItem",
						Object[].class)
				.setParameter("codigo", tecnico.getEmpresa().getCodigo())
				.setParameter("tecnico", tecnico.getCodigo())

				.getResultList();

		for (Object[] usuarioQuantidade : itemsdoEncarregado) {

			resultado.put(((Date) usuarioQuantidade[0]),
					((Number) usuarioQuantidade[1]).intValue());
		}

		return resultado;
	}

	public int duracaoDoProjeto(Usuario logado) {

		List<Object[]> itemsdoEncarregado = manager
				.createQuery(
						"select week(i.empresa.dataFim)-week(i.empresa.dataIni),week(current_date)-week(i.empresa.dataIni)   from Item i where"
								+ " i.empresa.codigo=:codigo ", Object[].class)
				.setParameter("codigo", logado.getEmpresa().getCodigo())

				.getResultList();
		int retorno = 0;
		if (retorno == 0) {

			for (Object[] usuarioQuantidade : itemsdoEncarregado) {

				retorno = ((Integer) usuarioQuantidade[0]);

			}
		}
		return retorno;
	}

	
	public int decorridoDoProjeto(Usuario logado) {

		List<Object[]> itemsdoEncarregado = manager
				.createQuery(
						"select week(i.empresa.dataFim)-week(i.empresa.dataIni),week(current_date)-week(i.empresa.dataIni)   from Item i where"
								+ " i.empresa.codigo=:codigo ", Object[].class)
				.setParameter("codigo", logado.getEmpresa().getCodigo())

				.getResultList();
		int retorno = 0;
		if (retorno == 0) {

			for (Object[] usuarioQuantidade : itemsdoEncarregado) {

				retorno = ((Integer) usuarioQuantidade[1]);

			}
		}
		return retorno;
	}

	// ---FIM RELATORIO ENCARREGADO---//

	// -----INICIO RELATORIO DE GERENTES-------------//

	public Map<String, Long> quantidadeCriticadaFeitoParaGerente(
			Usuario encarregado) {

		Map<String, Long> resultado = new TreeMap<>();

		List<Object[]> itemsdoEncarregado = manager
				.createQuery(
						"select count(i), i from Item i where i.status='CRITICADO' and i.empresa.codigo=:codigo and i.areaItem.codigo=:areaItem ",
						Object[].class)
				.setParameter("codigo", encarregado.getEmpresa().getCodigo())
				.setParameter("areaItem",
						encarregado.getAreaEncarregado().getCodigo())

				.getResultList();

		for (Object[] usuarioQuantidade : itemsdoEncarregado) {
			resultado.put(usuarioQuantidade[1] == null ? "CRITICADOS - 0"
					: "CRITICADOS" + " - " + usuarioQuantidade[0],
					(usuarioQuantidade[0] == null ? 0L
							: ((Long) usuarioQuantidade[0])));

		}
		return resultado;
	}

	public Map<String, Long> quantidadeJustificadaFeitoParaGerente(
			Usuario encarregado) {

		Map<String, Long> resultado = new TreeMap<>();

		List<Object[]> itemsdoEncarregado = manager
				.createQuery(
						"select count(i), i from Item i where i.status='JUSTIFICADO' and i.empresa.codigo=:codigo and i.areaItem.codigo=:areaItem ",
						Object[].class)
				.setParameter("codigo", encarregado.getEmpresa().getCodigo())
				.setParameter("areaItem",
						encarregado.getAreaEncarregado().getCodigo())

				.getResultList();

		for (Object[] usuarioQuantidade : itemsdoEncarregado) {
			resultado.put(usuarioQuantidade[1] == null ? "JUSTIFICADOS - 0"
					: "JUSTIFICADOS" + " - " + usuarioQuantidade[0],
					usuarioQuantidade[0] == null ? 0L
							: ((Long) usuarioQuantidade[0]));

		}
		return resultado;
	}

	public Map<String, Long> quantidadeTotalParaGerente(Usuario gerente) {

		Map<String, Long> resultado = new TreeMap<>();

		Long itemsdoGerente = manager
				.createQuery(
						"select count(i) from Item i where  i.empresa.codigo=:codigo group by i.areaItem",
						Long.class)
				.setParameter("codigo", gerente.getEmpresa().getCodigo())

				.getSingleResult();

		resultado.put("PENDENTES" + " - " + itemsdoGerente, itemsdoGerente);

		return resultado;
	}

	// ====FIM DE ESTATISTICA PARA GERENTE===//

	public Map<String, Long> quantidadeTotalFeitoParaTecnico(Long codigo) {

		Map<String, Long> resultado = new TreeMap<>();

		List<Object[]> itemsdoTecnico = manager
				.createQuery(
						"select count(distinct i)from Item i   "
								+ "where (i.status ='CRITICADO' or i.status ='JUSTIFICADO') and  i.tecnico.codigo=:codigo",
						Object[].class).setParameter("codigo", codigo)

				.getResultList();

		for (Object[] usuarioQuantidade : itemsdoTecnico) {
			resultado.put(((Item) usuarioQuantidade[1]).getStatus()
					.getDescricao(), ((Long) usuarioQuantidade[0]));

		}
		return resultado;
	}

}