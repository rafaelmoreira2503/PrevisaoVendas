
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.com.previsao.model.Historico;
import br.com.previsao.model.Linha;
import br.com.previsao.model.Produto;

public class LendoProduto {

	public static double calculoPrevisao(double a, double... d) {
		double soma = 0.0;
		double potencia = 1.0;
		for (int i = d.length - 1; i >= 0; i--) {
			soma += a * potencia * d[i];
			potencia *= 1.0 - a;
		}
		return soma;
	}

	public static double fatorAmortecimentoExponencial(double... d) {
		double v1 = 0.0;
		double v5 = 1.0;

		while (true) {
			double v3 = (v1 + v5) / 2;
			if (v3 == v1 || v3 == v5)
				return v3;
			double s1 = calculoPrevisao(v1, d);
			double s3 = calculoPrevisao(v3, d);
			double s5 = calculoPrevisao(v5, d);
			if (s5 >= s3 && s5 >= s1) {
				v1 = v3;
			} else if (s1 >= s3 && s1 >= s5) {
				v5 = v3;
			} else {
				double v2 = (v1 + v3) / 2;
				double v4 = (v3 + v5) / 2;
				double s2 = calculoPrevisao(v2, d);
				double s4 = calculoPrevisao(v4, d);
				if (s4 >= s3 && s4 >= s5) {
					v1 = v3;
					v3 = v4;
				} else if (s2 >= s3 && s2 >= s1) {
					v5 = v3;
					v3 = v2;
				} else if (s3 >= s2 && s3 >= s4) {
					v1 = v2;
					v5 = v4;
				}
			}
		}

	}

	static List<Historico> listaComCincoUltimosMeses = new ArrayList<Historico>();
	static List<Historico> listaComQuantidade = new ArrayList<Historico>();
	static List<Date> mesesDoProduto = new ArrayList<Date>();

	static void adicionaUltimosCincoHistorico(Produto prod) {

		for (int i = prod.getHistoricos().size() - 5; i < prod.getHistoricos().size(); i++) {

//			System.out.println("Mes " + prod.getHistoricos().get(i).getMesesHistoricos() == null ? "Erro"
//					: prod.getHistoricos().get(i).getMesesHistoricos());
//			System.out.println("Quantidade " + prod.getHistoricos().get(i).getQuantidade());

			listaComCincoUltimosMeses.add(prod.getHistoricos().get(i));
			

		}

	}

	static void adicionaHistorico(Produto prod) {

		for (int i = 0; i < prod.getHistoricos().size(); i++) {

//			System.out.println("Mes " + prod.getHistoricos().get(i).getMesesHistoricos() == null ? "Erro"
//					: prod.getHistoricos().get(i).getMesesHistoricos());
	//		System.out.println("Toda Quantidade " + prod.getHistoricos().get(i).getQuantidade());

			listaComQuantidade.add(prod.getHistoricos().get(i));

		}
	}

	public static void main(String[] args) throws FileNotFoundException {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("PrevisaoVendasPU");
		EntityManager manager = factory.createEntityManager();

		EntityTransaction trx = manager.getTransaction();

		// =================== LER ================================
		XStream ler = new XStream(new DomDriver());
		ler.alias("lista", List.class);
		ler.alias("produto", Produto.class);
		ler.alias("linha", Linha.class);
		ler.alias("historico", Historico.class);

		@SuppressWarnings("unchecked")
		List<Produto> listaProduto = (List<Produto>) ler.fromXML(new FileInputStream("./produto3.xml"));

		trx.begin();
		for (Iterator<Produto> p = listaProduto.iterator(); p.hasNext();) {

			Produto prod = p.next();
			System.out.println("CODIGO" + prod.getCodigoProduto() == null ? "Erro" : prod.getCodigoProduto());
			System.out.println("DESCRICÂO " + prod.getDescricao());
//			System.out.println("LINHA " + prod.getLinha().getDescricao());
//			System.out.println("Tamanho" + prod.getHistoricos().size());

			adicionaHistorico(prod);
			int j = 0;
			double[] arrayComQuantidades = new double[listaComQuantidade.size()];
			for (Historico hist : listaComQuantidade) {
				// System.out.println(hist.getMesesHistoricos() == null ? "Erro"
				// : hist.getMesesHistoricos());
				 System.out.println("Quantidades" + hist.getQuantidade());
				arrayComQuantidades[j] = hist.getQuantidade();
				// System.out.println(arrayinvertidoComUltimosCincoMeses.length);
				//System.out.println("Array " + arrayComQuantidades[j]);
				j++;
			}

			adicionaUltimosCincoHistorico(prod);

			int k = 0;
			double[] arrayComUltimosCincoMeses = new double[listaComCincoUltimosMeses.size()];
			Collections.reverse(listaComCincoUltimosMeses);
			for (Historico hist : listaComCincoUltimosMeses) {
				 System.out.println("5 Quantidades" + hist.getQuantidade());
				arrayComUltimosCincoMeses[k] = hist.getQuantidade();

				k++;
			}

			// adiciona o tre ultimo meses e o 12 me anterior
			int tamanho = prod.getHistoricos().size();
			for (int i = prod.getHistoricos().size(); i >= 0; i--) {

				
				if (i ==  prod.getHistoricos().size()-1 || i ==  prod.getHistoricos().size() -2 || i ==  prod.getHistoricos().size() - 3 || i ==  prod.getHistoricos().size() -13) {
					mesesDoProduto.add(prod.getHistoricos().get(i).getMesesHistoricos());
				}
			}

			for(Date data : mesesDoProduto){
				System.out.println("====Mes===== " + data);
			}
			Double alfa = BigDecimal.valueOf((fatorAmortecimentoExponencial(arrayComUltimosCincoMeses)))
					.setScale(3, RoundingMode.HALF_UP).doubleValue();
			System.out.println("Alfa " + alfa);
			System.out.println("Melhor taxa: " + alfa + " - resultado: "
					+ Math.round(calculoPrevisao(alfa.doubleValue(), arrayComQuantidades)));

			Long previsao = Math.round(calculoPrevisao(alfa.doubleValue(), arrayComQuantidades));
			System.out.println("Previao: " + previsao);
			listaComCincoUltimosMeses.clear();
			listaComQuantidade.clear();
			mesesDoProduto.clear();
			prod.setFatorAmortecimentoExponencial(alfa);
			prod.setQuantidadeRecente((calculoPrevisao(alfa.doubleValue(), arrayComQuantidades)));
			
			System.out.println("Receita" + prod.getValor().multiply(new BigDecimal(previsao)));
			prod.setQuantidadeReais(prod.getValor().multiply(new BigDecimal(previsao)));
			// manager.persist(prod);// persistir após calcular o alfa

		}

		trx.commit();

	}
}
