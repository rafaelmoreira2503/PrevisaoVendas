
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Endereco;

public class ExemplosCascata {

	private static EntityManagerFactory factory;

	private EntityManager manager;

	@BeforeClass
	public static void init() {
		factory = Persistence.createEntityManagerFactory("ConfiabilidadePU");
	}

	@Before
	public void setUp() {
		this.manager = factory.createEntityManager();
	}

	@After
	public void tearDown() {
		this.manager.close();
	}

	@Test
	public void exemploEntidadeTransiente() {

		Endereco endereco = new Endereco();
		endereco.setCep("22230-061");
		endereco.setComplemento("apartamento 209");

		endereco.setNumero("4000");
		endereco.setLogradouro("Rua da Carioca");

		Endereco endereco2 = new Endereco();
		endereco2.setCep("22210-080");
		endereco2.setComplemento("viela 1009");

		endereco2.setNumero("330");
		endereco2.setLogradouro("Avenida Paulista");
		Empresa empresa = new Empresa();
		empresa.setCnpj("86.110.219/0001-69");
		empresa.setSegmento("Mistura");
		empresa.setSponsor("Sponsor Francisco");
		empresa.setDataIni(new Date());
		empresa.setDataIni(new Date());
		empresa.setIscricaoEstadual("4559.2283.023");
		empresa.setNome("Metais LTDA");
		empresa.setNomeFantasia("Nome Fantasia Da Empresa");

		this.manager.getTransaction().begin();
		this.manager.persist(empresa);
		this.manager.getTransaction().commit();
	}

}
