import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.confiabilidade.model.Item;

public class BuscaDeItemsLazy {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("ConfiabilidadePU");
		EntityManager manager = factory.createEntityManager();
		// "select m from Item m where (select count(i) from Item i where
		// i.codigoItem = m.codigoItem)=1

		Query query = manager
				.createQuery(
						"select count(i) from Item i where i.criticidade=(select min(criticidade) from Item m where m.empresa=1L and i.codigoItem=m.codigoItem)",
						Long.class);

		Long items = ((Number)query.getSingleResult()).longValue();
		System.out.println("---Impressão de Items----");
		int i = 0;
		//for (Item item : items)
			System.out.println(++i + " ITem  : " + "Codigo  "
					+ items);

		//	);

		
	}
}
