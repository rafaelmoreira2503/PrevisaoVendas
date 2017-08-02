import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;

import br.com.previsao.model.Produto;

public class Teste {

	public static void main(String[] args) {
		try {
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("PrevisaoVendasPU");
			EntityManager manager = factory.createEntityManager();

			EntityTransaction trx = manager.getTransaction();
			trx.begin();

//			String jpql1 = "select new br.com.previsao.model.voFiltro(p, sum(p.quantidadeRecente)) from Produto p where p.gerenteFilial.chefe.codigo =:codigo group by p";
//
//			//
//
//			// metodo buscarPorPaginacao
//			TypedQuery<Filtro> query = manager.createQuery(jpql1, Filtro.class);
//			query.setParameter("codigo", 3L);
//			List<Filtro> resultado = query.getResultList();
//			for (Filtro prod : resultado) {
//				System.out.println(" Impressão Filtro da Empresa: ");
//				System.out.println(" Nome : " + prod.getProduto().getCodigoProduto() + "Valor" + prod.getProduto().getValor()
//						+ "Quantidade" + prod.getProduto().getQuantidadeRecente()+ "Filial" + prod.getProduto().getGerenteFilial() +"Total" + prod.getTotal());
//
//			}
//			String jpql = "select p from Produto p where P NOT IN(select p1  upper(p1.codigoProduto) = :codigo and p1.empresa.codigo = :codigoemp"
//					+ " and p1.gerenteFilial.codigo =:codigofilial and linha is not null or linha is null  ";
//			TypedQuery<Produto> query  manager.createQuery(jpql,Produto.class)
//
//			//
//
//			// metodo buscarPorPaginacao
//			
//			.setParameter("codigo", "0124940")
//			.setParameter("codigoemp", 1L)
//			.setParameter("codigofilial", 5L).getSingleResult();
//		     Produto usuGerente1 = query.getSingleResult();
////			for (Produto usuGerente1 : gerentes1) {
//				System.out.println(" Impressão Produto da Empresa: ");
//				System.out.println(" Nome : " + usuGerente1.getDescricao() + "Valor" + usuGerente1.getValor()
//						+ "Quantidade" + usuGerente1.getQuantidadeRecente()+ "Filial" + usuGerente1.getGerenteFilial());
//
//			//}

			// String jpql = "select (h) from Historico h where
			// h.mesesHistoricos = (select max(h2.mesesHistoricos) from
			// Historico h2 where h2.produto = h.produto and
			// h.produto.gerenteFilial.codigo=:codigo)";
			// TypedQuery<Historico> query3 = manager.createQuery(jpql,
			// Historico.class);
			// query3.setParameter("codigo", 5L);
			// List<Historico> gerentes = query3.getResultList();
			// for (Historico usuGerente : gerentes) {
			// System.out.println(" Impressão de Gerentes da Empresa: ");
			// System.out.println(" Nome : " +
			// usuGerente.getProduto().getDescricao());
			//
			// }
			//
			// String jpql2 = "select count(h) from Historico h where
			// h.mesesHistoricos = (select max(h2.mesesHistoricos) from
			// Historico h2 where h2.produto = h.produto and
			// h.produto.gerenteFilial.codigo=:codigo)";
			// TypedQuery<Long> query2 = manager.createQuery(jpql2, Long.class);
			// query2.setParameter("codigo", 5L);
			// int resultado = query2.getSingleResult().intValue();
			//
			// System.out.println(resultado);
			//
			// Produto query = manager
			// .createQuery(
			// "select p from Produto p where upper(p.codigoProduto) = :codigo
			// and p.empresa.codigo = :codigoemp",
			// Produto.class)
			// .setParameter("codigo", "0218308").setParameter("codigoemp",
			// 1L).getSingleResult();
			// System.out.println("produto" + query.getDescricao());
			//
			// Produto prod = manager
			// .createQuery(
			// "select p from Produto p where upper(p.codigoProduto) = :codigo
			// and p.empresa.codigo = :codigoemp and p.gerenteFilial.codigo
			// =:codigofilial and linha is not null ",
			// Produto.class)
			// .setParameter("codigo", "2001144").setParameter("codigoemp",
			// 1L).setParameter("codigofilial", 5L)
			// .getSingleResult();
			//
			// System.out.println(" Produto" + prod.getDescricao());
			//
			// List<Usuario> usuarios = manager.createQuery(
			// "select u from Usuario u where
			// u.permissao=br.com.previsao.model.Permissao.GERENTEFILIAL and
			// u.empresa.codigo=:codigo",
			// Usuario.class).setParameter("codigo", 1L).getResultList();
			//
			// for (Usuario usuario : usuarios) {
			// System.out.println(" codigoUsuario : " + usuario.getCodigo() + "
			// Nome : " + usuario.getNome());
			// }
		
			
			List<Produto> produtos = manager.createQuery("select p from Produto p where p.empresa.codigo =:codigo", Produto.class)
					.setParameter("codigo",1L).getResultList();
			long quantidadesRecentes = 0L;
			Long total = 0L;
			for(Produto p : produtos){
				
				
				quantidadesRecentes += p.getQuantidadeRecente();
				total = quantidadesRecentes * p.getValor().longValue();
				
				
			}
			//long quantidadesRecentes2 = produtos.stream().mapToLong(Produto::getQuantidadeRecente).sum();
			System.out.println(total);
		} catch (NoResultException e) {

			System.out.println(" Objeto não Encontrado!!! : ");

		} catch (NonUniqueResultException e) {

			System.out.println(" Mais de um Objeto Encontrado!!! : ");

		}

	}

}
