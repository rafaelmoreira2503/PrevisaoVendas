import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.confiabilidade.model.Pergunta;
import br.com.confiabilidade.model.PerguntaEmpresa;

public class ConsultaDePerguntas {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("ConfiabilidadePU");
		EntityManager manager = factory.createEntityManager();
		Query query = manager

				.createQuery(
						" select distinct(pe) from PerguntaEmpresa pe where pe.codigo_empresa.codigo=:codigo and pe.itemCriticado.codigo=:codigoItem",
						PerguntaEmpresa.class).setParameter("codigo", 1L)
				.setParameter("codigoItem", 7L);
		List<PerguntaEmpresa> especialidades = query.getResultList();
		System.out.println("---Impressão de Pergunta----");

		for (PerguntaEmpresa especialidade : especialidades) {
			System.out.println(" Codigo : "
					+ especialidade.getCodigo_pergunta().getDescricao());
			// System.out.println(" Descrição : "
			// + especialidade.getResposta().getValor());

		}

		// Query query4 = manager.createQuery(" from Pergunta");
		// List<Pergunta> especialidades2 = query4.getResultList();
		// System.out.println("---Impressão de Pergunta----");
		//
		// for (Pergunta especialidade : especialidades2) {
		// System.out.println(" Codigo : " + especialidade.getCodigo());
		// System.out.println(" Descrição : " + especialidade.getDescricao());
		//
		// }
		//
		 Query query2 = manager
		 .createQuery("select distinct(count(p)) from Pergunta p left join p.perguntaEmpresas ");
		 int count = ((Long) query2.getSingleResult()).intValue();
		
		 System.out.println("count pergunta  :" + count);
		
		 //		 -----------------------------------------------------------------------------//
		
		
		//PERGUNTAS ASSOCIADAS (select count(pe.codigo_pergunta) from PerguntaEmpresa pe where pe.codigo_pergunta.codigo = p.codigo )>=1"
		Query query3 = manager
				.createQuery("select p from Pergunta p where (select count(pe.codigo_pergunta) from PerguntaEmpresa pe where pe.codigo_pergunta.codigo = p.codigo and pe.codigo_empresa=1L )>=1" );
		List<Pergunta> empresas = query3.getResultList();
		System.out.println("---Impressão de Empresa----");

		for (Pergunta empresa : empresas) {
			System.out.println(" Codigo : " + empresa.getCodigo());
			System.out.println(" Descrição : " + empresa.getDescricao());

		}
		//
		// Query query5 = manager
		// .createQuery("select distinct(count(e)) from Empresa e  join e.perguntaEmpresas");
		// int count4 = ((Long) query5.getSingleResult()).intValue();
		//
		// System.out.println("count Empresa  :" + count4);
	}
}
