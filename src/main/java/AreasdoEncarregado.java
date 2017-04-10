import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.confiabilidade.model.Area;

public class AreasdoEncarregado {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("ConfiabilidadePU");
		EntityManager manager = factory.createEntityManager();

		EntityTransaction trx = manager.getTransaction();
		trx.begin();

		Query query = manager
				.createQuery("select u.areaEncarregado from Usuario u where   u.empresa.codigo =1L");
		List<Area> areas = query.getResultList();
		System.out.println("---Impressão de Epecialidade----");

		for (Area area : areas)
			System.out.println(" Tem  : " + area.getDescricao());

	}
}
