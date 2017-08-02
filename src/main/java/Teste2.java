import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import br.com.previsao.model.vo.Filtro;

public class Teste2 {

	public static void main(String[] args) {
		try {
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("PrevisaoVendasPU");
			EntityManager manager = factory.createEntityManager();

			EntityTransaction trx = manager.getTransaction();
			trx.begin();

			// SELECT a2.produto , a2.descricao, a2.quantidade_recente, Soma
			// from
			// (SELECT p.codigoProduto, sum(p.quantidade_recente) aS Soma
			// from previsaovendas.produto aS p, previsaovendas.usuario u where
			// u.codigo = p.codigo_filial
			// and u.codigo_chefe = 3 group by p.codigoProduto) as a1,
			// (SELECT p1.codigoProduto aS produto, p1.descricao,
			// p1.quantidade_recente from previsaovendas.produto aS p1,
			// previsaovendas.usuario u
			// where u.codigo = p1.codigo_filial and u.codigo_chefe = 3 ) as a2
			// where a2.produto = a1.codigoProduto;

			// String jpql1 = "select new
			// br.com.previsao.model.vo.Filtro(a2.produto , a2.descricao,
			// a2.quantidadeRecente, Soma "
			// + " FROM (select p.codigoProduto, sum(p.quantidadeRecente) aS
			// Soma from Produto p where p.gerenteFilial.chefe.codigo =:codigo )
			// as a1,"
			// + " (select p1.codigoProduto aS produto, p1.descricao,
			// p1.quantidade_recente from Produto p1 where
			// p1.gerenteFilial.chefe.codigo =:codigo ) as a2"
			// + " where a2.produto = a1.codigoProduto)";

//			String jpql1 = "select p1.codigoProduto, p1.descricao, p1.quantidadeRecente, (select max(p.quantidadeRecente) as soma from Produto p where p.codigo = p1.codigo)"
//					+ " from Produto p1 where p1.gerenteFilial.chefe.codigo =3L";

			// String jpql1 = "select new
			// br.com.previsao.model.vo.Filtro(p.codigoProduto,
			// p.gerenteFilial.nome , p.quantidadeRecente)"
			// +" from Produto p "
			// + "join Produto p2 where p2.codigoProduto = p. codigoProduto and
			// p.gerenteFilial.chefe.codigo =:codigo ";

			// TypedQuery<Object[]> query = em.createQuery(
			// "SELECT c.name, c.capital.name FROM Country AS c",
			// Object[].class);
			// List<Object[]> results = query.getResultList();
			// for (Object[] result : results) {
			// System.out.println("Country: " + result[0] + ", Capital: " +
			// result[1]);
			// }
			// String jpql = "select a2.produto , a2.descricao,
			// a2.quantidadeRecente, Soma from"
			// + " (select p.codigoProduto, sum(p.quantidadeRecente) aS Soma
			// from Produto p previsaovendas.usuario u "
			// + "where u.codigo = p1.gerenteFilial.codigo and u.chefe.codigo =
			// 3L ) as a1,"
			// + " (SELECT p1.codigoProduto aS produto, p1.descricao,
			// p1.quantidadeRecente from previsaovendas.produto aS p1,
			// previsaovendas.usuario u "
			// + "where u.codigo = p1.gerenteFilial.codigo and u.chefe.codigo =
			// 3L ) as a2 "
			// + "where a2.produto = a1.codigoProduto";
			//
			//
			// String jpql1 = "select p.codigoProduto, sum(p.quantidadeRecente)
			// aS Soma from Produto p" u.codigo = p.codigo_filial and
			// u.codigo_chefe = 3 ;

//			StringBuilder hql1 = new StringBuilder();
//			hql1.append(
//					"select a2.codigoProduto , a2.descricao, a2.quantidadeRecente, Soma ,a2.valor, a2.valor * Soma from")
//					.append(" (select p.codigoProduto,  sum(p.quantidadeRecente) as Soma   from Produto p ,Usuario u ")
//					.append(" where u.codigo = p.gerenteFilial.codigo and u.chefe.codigo = 3L ) as a1,")
//					.append(" (SELECT p1.codigoProduto, p1.descricao, p1.quantidade_recente , p1.valor from Produto aS p1,  Usuario u ")
//					.append(" where u.codigo = p1.gerenteFilial.codigo and u.chefe.codigo = 3L ) as a2 ")
//					.append(" where a2.codigoProduto = a1.codigoProduto");
//			
			String hql = "select p1.codigoProduto as codigoProduto, p1.descricao as descricao,p1.codigoFilial as filial, p1.quantidadeRecente as quantidade,"
					+ " (select sum(p.quantidadeRecente) from Produto p where  p.empresa.codigo=:codigo and trim(p.codigoProduto) = trim(p1.codigoProduto)) as soma, "
					+ " p1.valor as valor,"
					+ " (select sum(p.quantidadeRecente) * p.valor from Produto p where p.empresa.codigo=:codigo and trim(p.codigoProduto) = trim(p1.codigoProduto)) as receita"
					+ " from Produto p1  where p1.empresa.codigo=:codigo order by trim(p1.codigoProduto)";


			Session session = manager.unwrap(Session.class);
			Query query =session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(Filtro.class));
			// metodo buscarPorPaginacao
			// Query query = manager.createNativeQuery(jpql1, Filtro.class);
			 query.setParameter("codigo", 1L);
			// @SuppressWarnings("unchecked")
			List<Filtro> resultado = query.list();
			for (Filtro prod : resultado) {
				System.out.println(" Impressão Filtro da Empresa: ");
				System.out.println(" CodigoProduto : " + prod.getCodigoProduto() + " Produto " + prod.getDescricao() + " Flial " + prod.getFilial()
						+ " Quantidade " + prod.getQuantidade() + " Soma " + prod.getSoma() + " Valor " + prod.getValor()
						+ " Receita " + prod.getReceita());

			}

		} catch (NoResultException e) {

			System.out.println(" Objeto não Encontrado!!! : ");

		} catch (NonUniqueResultException e) {

			System.out.println(" Mais de um Objeto Encontrado!!! : ");

		}
	}
}
