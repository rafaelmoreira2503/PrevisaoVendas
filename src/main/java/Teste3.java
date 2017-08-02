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

import br.com.previsao.model.Produto;
import br.com.previsao.model.vo.Filtro;
import br.com.previsao.model.vo.ReceitaTotal;
import br.com.previsao.model.vo.ReceitaTotalGerente;

public class Teste3 {

	public static void main(String[] args) {
		try {
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("PrevisaoVendasPU");
			EntityManager manager = factory.createEntityManager();

			EntityTransaction trx = manager.getTransaction();
			trx.begin();
//			String hql = "select p.codigoProduto as codigoProduto, sum(p.valor * p.quantidadeRecente) as total , sum(p.quantidadeRecente) as qtdSolicitada"
//					+ " from Produto p "
//					+"  where p.empresa.codigo =:codigo "
//					+"  group by trim(p.codigoProduto) " 
//					+ "	 order by trim(p.codigoProduto) desc";
			
			String hql = "select p1.codigoProduto as codigoProduto,"
					+ " (select sum(p.quantidadeRecente) from Produto p where  p.gerenteFilial.chefe.codigo =:codigo and trim(p.codigoProduto) = trim(p1.codigoProduto)) as qtdSolicitada, "
					+ " (select sum(p.quantidadeRecente) * p.valor from Produto p where p.gerenteFilial.chefe.codigo =:codigo  and trim(p.codigoProduto) = trim(p1.codigoProduto)) as total"
					+ " from Produto p1  where p1.gerenteFilial.chefe.codigo =:codigo group by trim(p1.codigoProduto) order by trim(p1.codigoProduto)";

			

			Session session = manager.unwrap(Session.class);

			// .setParameter("codigo",1L).getResultList();
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(ReceitaTotalGerente.class));
			query.setParameter("codigo", 3L);
			List<ReceitaTotalGerente> resultado = query.list();
			Double totalReceita = 0.0;
			Long total = 0L;
			for (ReceitaTotalGerente p : resultado) {

				totalReceita += p.getTotal();

			}
			// double quantidadesRecentes2 =
			// produtos.stream().mapToDouble(Produto::getQuantidadeRecente).sum();
			System.out.println(totalReceita);

		} catch (NoResultException e) {

			System.out.println(" Objeto não Encontrado!!! : ");

		} catch (NonUniqueResultException e) {

			System.out.println(" Mais de um Objeto Encontrado!!! : ");

		}
	}
}