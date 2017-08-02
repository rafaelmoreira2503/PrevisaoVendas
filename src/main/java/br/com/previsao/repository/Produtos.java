package br.com.previsao.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Linha;
import br.com.previsao.model.Produto;
import br.com.previsao.model.Usuario;
import br.com.previsao.model.vo.Filtro;
import br.com.previsao.model.vo.ReceitaTotal;
import br.com.previsao.model.vo.ReceitaTotalGerente;
import br.com.previsao.security.UsuarioLogado;
import br.com.previsao.service.NegocioException;
import br.com.previsao.util.jpa.Transactional;

public class Produtos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	@Inject
	private EntityManager manager;

	public Produto porId(Long id) {

		return manager.find(Produto.class, id);

	}

	public void guardar(Produto produto) {

		manager.merge(produto);
	}

	@Transactional
	public void remover(Produto produto) {
		try {
			produto = porId(produto.getCodigo());
			manager.remove(produto);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Área não pode ser excluída.");
		}
	}

	public Produto porNome(String nome) {
		try {
			return manager.createNamedQuery(Produto.DESCRICAO, Produto.class)
					.setParameter("descricao", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Produto porCodigoeEmpresa(Produto produto) {
		try {
			return manager
					.createQuery(
							"select p1 from Produto p1 where upper(p1.codigoProduto) = :codigo and p1.codigoFilial =:codigoFil and p1.empresa.codigo = :codigoemp",
							Produto.class)
					.setParameter("codigo", produto.getCodigoProduto())
					.setParameter("codigoFil", produto.getCodigoFilial())
					.setParameter("codigoemp", produto.getEmpresa().getCodigo()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public Produto porCodigo(Produto produto) {

		try {
			return manager
					.createQuery(
							"select p from Produto p where upper(p.codigoProduto) = :codigo and p.empresa.codigo = :codigoemp",
							Produto.class)
					.setParameter("codigo", produto.getCodigoProduto())
					.setParameter("codigoemp", produto.getEmpresa().getCodigo()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Filtro> listarProdutosDoGerenteRegional(Long codigo) {
		String hql = "select p1.codigoProduto as codigoProduto, p1.descricao as descricao,p1.codigoFilial as filial, p1.quantidadeRecente as quantidade,"
				+ " (select sum(p.quantidadeRecente) from Produto p where  p.gerenteFilial.chefe.codigo =:codigo and trim(p.codigoProduto) = trim(p1.codigoProduto)) as soma, "
				+ " p1.valor as valor,"
				+ " (select sum(p.quantidadeRecente) * p.valor from Produto p where p.gerenteFilial.chefe.codigo =:codigo  and trim(p.codigoProduto) = trim(p1.codigoProduto)) as receita"
				+ " from Produto p1  where p1.gerenteFilial.chefe.codigo =:codigo order by trim(p1.codigoProduto)";

		Session session = manager.unwrap(Session.class);
		Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(Filtro.class));

		query.setParameter("codigo", codigo);

		return query.list();
	}
	
	public Double listarTotalProdutosDoGerenteRegional(Long codigo) {
		String hql = "select p1.codigoProduto as codigoProduto,"
				+ " (select sum(p.quantidadeRecente) from Produto p where  p.gerenteFilial.chefe.codigo =:codigo and trim(p.codigoProduto) = trim(p1.codigoProduto)) as qtdSolicitada, "
				+ " (select sum(p.quantidadeRecente) * p.valor from Produto p where p.gerenteFilial.chefe.codigo =:codigo  and trim(p.codigoProduto) = trim(p1.codigoProduto)) as total"
				+ " from Produto p1  where p1.gerenteFilial.chefe.codigo =:codigo group by trim(p1.codigoProduto) order by trim(p1.codigoProduto)";


		Session session = manager.unwrap(Session.class);
		Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(ReceitaTotalGerente.class));

		query.setParameter("codigo", codigo);

		List<ReceitaTotalGerente> resultado = query.list();
		Double totalReceita = 0.0;
		
		for (ReceitaTotalGerente p : resultado) {

			totalReceita += p.getTotal().doubleValue();

		}
		// double quantidadesRecentes2 =
		// produtos.stream().mapToDouble(Produto::getQuantidadeRecente).sum();
		System.out.println(totalReceita);
		
		return totalReceita;
	}

	public List<Filtro> listarProdutosDoDiretor(Long codigo) {

		String hql = "select p1.codigoProduto as codigoProduto, p1.descricao as descricao,p1.codigoFilial as filial, p1.quantidadeRecente as quantidade,"
				+ " (select sum(p.quantidadeRecente) from Produto p where  p.empresa.codigo=:codigo and trim(p.codigoProduto) = trim(p1.codigoProduto)) as soma, "
				+ " p1.valor as valor,"
				+ " (select sum(p.quantidadeRecente) * p.valor from Produto p where p.empresa.codigo=:codigo and trim(p.codigoProduto) = trim(p1.codigoProduto)) as receita"
				+ " from Produto p1  where p1.empresa.codigo=:codigo order by trim(p1.codigoProduto)";

		Session session = manager.unwrap(Session.class);
		Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(Filtro.class));

		query.setParameter("codigo", codigo);

		return query.list();
	}

	public int countAll(Empresa filtro) {

		Long total = 0L;// ((Number) criteria.uniqueResult()).intValue();

		total = manager.createQuery("select count(p) from Produto p where p.empresa.codigo =:codigo", Long.class)
				.setParameter("codigo", filtro.getCodigo()).getSingleResult();
		return total.intValue();
	}

	public List<Produto> buscaPorPaginacao(int posicaoPrimeiraLinha, int maximoPorPagina, String ordernarPeloCampo,
			String ordernarAscOuDesc, Empresa filtro) {
		try {
			return manager.createQuery("select p from Produto p where p.empresa.codigo =:codigo", Produto.class)
					.setParameter("codigo", filtro.getCodigo()).setFirstResult(posicaoPrimeiraLinha)
					.setMaxResults(maximoPorPagina).getResultList();
		} catch (NoResultException e) {
			return null;
		}

	}

	public int countAllPrevisao(Linha filtros) {
		String jpql = "select count(p) from Produto p where p.gerenteFilial.codigo=:codigo";

		if (filtros != null) {
			jpql += " and p.linha.codigo=:codigolinha";
		}

		TypedQuery<Long> query = manager.createQuery(jpql, Long.class);
		query.setParameter("codigo", usuario.getCodigo());
		if (filtros != null) {
			query.setParameter("codigolinha", filtros.getCodigo());
		}
		// Long total = 0L;// ((Number) criteria.uniqueResult()).intValue();

		// total = manager.createQuery("select count(p) from Produto p where
		// p.empresa.codigo =:codigo", Long.class)
		// .setParameter("codigo", filtro.getCodigo()).getSingleResult();
		return query.getSingleResult().intValue();
	}

	public List<Produto> buscaPorPaginacaoPrevisao(int posicaoPrimeiraLinha, int maximoPorPagina,
			String ordernarPeloCampo, String ordernarAscOuDesc, Linha filtros) {
		String jpql = "select p from Produto p where p.gerenteFilial.codigo=:codigo";

		if (filtros != null) {
			jpql += " and p.linha.codigo=:codigolinha";
		}
		// jpql = adicionarParametros(filtros, jpql);
		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernarAscOuDesc.contains("DESC")) {
				ordernarAscOuDesc = "DESC";
			} else {
				ordernarAscOuDesc = "ASC";
			}

			jpql += " ORDER BY p." + ordernarPeloCampo + " " + ordernarAscOuDesc;
		}

		// metodo buscarPorPaginacao
		TypedQuery<Produto> query = manager.createQuery(jpql, Produto.class);
		query.setParameter("codigo", usuario.getCodigo());
		if (filtros != null) {
			query.setParameter("codigolinha", filtros.getCodigo());
		}

		// popularParametros(query, filtros);

		query.setFirstResult(posicaoPrimeiraLinha);
		query.setMaxResults(maximoPorPagina);
		return query.getResultList();
	}

	// Evitando SQL Injection
	private void popularParametros(TypedQuery<?> query, Map<String, String> filtros) {
		for (Map.Entry<String, String> entry : filtros.entrySet()) {
			query.setParameter(entry.getKey(), "%" + entry.getValue() + "%");
		}
	}

	private String adicionarParametros(Map<String, String> filtros, String jpql) {
		if (filtros != null && !filtros.isEmpty()) {
			jpql += " where ";
			for (Map.Entry<String, String> entry : filtros.entrySet()) {
				jpql += entry.getKey() + " like :" + entry.getKey() + " and ";
			}

			// remove ultimo and desnecessario
			jpql = jpql.substring(0, jpql.length() - 4);
		}

		return jpql;
	}

	public Double buscaTotalParaDiretor(Long codigo) {
		String hql = "select p.codigoProduto as codigoProduto, sum(p.valor * p.quantidadeRecente) as total , sum(p.quantidadeRecente) as qtdSolicitada"
				+ " from Produto p " + "  where p.empresa.codigo =:codigo " + "  group by trim(p.codigoProduto) "
				+ "	 order by trim(p.codigoProduto) desc";

		Session session = manager.unwrap(Session.class);

		// .setParameter("codigo",1L).getResultList();
		Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(ReceitaTotal.class));
		query.setParameter("codigo", codigo);
		List<ReceitaTotal> resultado = query.list();
		Double totalReceita = 0.0;
		
		for (ReceitaTotal p : resultado) {

			totalReceita += p.getTotal();

		}
		// double quantidadesRecentes2 =
		// produtos.stream().mapToDouble(Produto::getQuantidadeRecente).sum();
		System.out.println(totalReceita);
		
		return totalReceita;
	}

}
