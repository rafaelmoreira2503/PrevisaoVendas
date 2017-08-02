package br.com.previsao.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Familia;
import br.com.previsao.model.Historico;
import br.com.previsao.model.Linha;
import br.com.previsao.model.Produto;
import br.com.previsao.model.Usuario;
import br.com.previsao.repository.Empresas;
import br.com.previsao.repository.Familias;
import br.com.previsao.repository.Linhas;
import br.com.previsao.repository.Usuarios;
import br.com.previsao.service.CadastroProdutoService;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class FileBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String diretorioDestino = "c:\\temp\\";

	@Inject
	private Empresas empresas;

	@Inject
	private Usuarios usuarios;

	private List<Usuario> usuariosRaizes;

	public List<Usuario> getUsuariosRaizes() {
		return usuariosRaizes;
	}

	private Empresa empresaSelecionada;

	private List<Empresa> todasEmpresas;

	@Inject
	private Familias familias;

	private Familia familiaSelecionada;

	private List<Familia> familiasRaizes;

	@Inject
	private Linhas linhas;

	private Linha linhaSelecionada;

	private List<Linha> linhasRaizes;

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			buscaEmpresas();

		}
	}

	private void buscaEmpresas() {
		todasEmpresas = empresas.empresasParaImportacao();
	}

	public void carregarFamilia() {
		familiasRaizes = familias.carregaFamilia(empresaSelecionada.getCodigo());

	}

	public void carregarLinha() {
		linhasRaizes = linhas.carregaLinhas(familiaSelecionada.getCodigo());

	}

	private List<Historico> listaComCincoUltimosMeses = new ArrayList<Historico>();
	private List<Historico> listaComQuantidade = new ArrayList<Historico>();
	private List<Date> mesesDoProduto = new ArrayList<Date>();
	private List<Integer> quantidade = new ArrayList<Integer>();

	@Inject
	CadastroProdutoService cadastroProdutoService;

	public List<Historico> getListaComCincoUltimosMeses() {
		return listaComCincoUltimosMeses;
	}

	public List<Historico> getListaComQuantidade() {
		return listaComQuantidade;
	}

	public void setListaComQuantidade(List<Historico> listaComQuantidade) {
		this.listaComQuantidade = listaComQuantidade;
	}

	public void setListaComCincoUltimosMeses(List<Historico> listaComCincoUltimosMeses) {
		this.listaComCincoUltimosMeses = listaComCincoUltimosMeses;
	}

	private UploadedFile uploadedFile;

	public UploadedFile getFile() {
		return uploadedFile;
	}

	public void setFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {

		this.uploadedFile = event.getFile();

	}

	@SuppressWarnings("unchecked")
	public void send() throws IOException {

		if (getFile() == null) {
			FacesUtil.addErrorMessage(FacesUtil.getMensagemI18n("escolher_arquivo"));

		} else {
			String nomeArquivo = uploadedFile.getFileName().substring(uploadedFile.getFileName().lastIndexOf("\\") + 1);

			XStream ler = new XStream(new DomDriver());
			ler.alias("lista", List.class);
			ler.alias("historico", Historico.class);
			ler.alias("produto", Produto.class);
			System.out.println(empresaSelecionada.getNome());
			// usuariosRaizes =
			// usuarios.carregarGerentesFiliais(empresaSelecionada.getCodigo());
			// for (Usuario gerenteFilial : usuariosRaizes) {
			// System.out.println(gerenteFilial.getNome());
			//
			// }

			List<Produto> listaProduto = (List<Produto>) ler
					.fromXML(new InputStreamReader(uploadedFile.getInputstream()));

			// for (Usuario gerenteFilial : usuariosRaizes) {
			//
			for (Iterator<Produto> p = listaProduto.iterator(); p.hasNext();) {

				Produto prod = p.next();
				System.out.println("CODIGO" + prod.getCodigoProduto() == null ? "Erro" : prod.getCodigoProduto());
				System.out.println("DESCRICÂO " + prod.getDescricao());

				adicionaHistorico(prod);
				int j = 0;
				double[] arrayComQuantidades = new double[listaComQuantidade.size()];
				for (Historico hist : listaComQuantidade) {

					arrayComQuantidades[j] = hist.getQuantidade();

					System.out.println("Array " + arrayComQuantidades[j]);
					j++;
				}

				adicionaUltimosCincoHistorico(prod);

				int k = 0;
				double[] arrayComUltimosCincoMeses = new double[listaComCincoUltimosMeses.size()];
				Collections.reverse(listaComCincoUltimosMeses);
				for (Historico hist : listaComCincoUltimosMeses) {

					arrayComUltimosCincoMeses[k] = hist.getQuantidade();
					// System.out.println(arrayinvertidoComUltimosCincoMeses.length);
					// System.out.println("Array " +
					// arrayComUltimosCincoMeses[k]);
					k++;
				}

				for (int i = prod.getHistoricos().size(); i >= 0; i--) {

					if (i == prod.getHistoricos().size() - 1 || i == prod.getHistoricos().size() - 2
							|| i == prod.getHistoricos().size() - 3 || i == prod.getHistoricos().size() - 13) {
						mesesDoProduto.add(prod.getHistoricos().get(i).getMesesHistoricos());
					}
				}
				for (int i = 0; i < mesesDoProduto.size(); i++) {

					if (i == 0) {
						prod.setPrimeiroMesAnterior(mesesDoProduto.get(i));
						System.out.println("====Mes===== " + mesesDoProduto.get(i));
					}
					if (i == 1) {
						prod.setSegundoMesAnterior(mesesDoProduto.get(i));
						System.out.println("====Mes===== " + mesesDoProduto.get(i));
					}
					if (i == 2) {
						prod.setTerceiroMesAnterior(mesesDoProduto.get(i));
						System.out.println("====Mes===== " + mesesDoProduto.get(i));
					}
					if (i == 3) {
						prod.setAnoAnterior(mesesDoProduto.get(i));
						System.out.println("====Mes===== " + mesesDoProduto.get(i));
					}

				}

				for (int i = prod.getHistoricos().size(); i >= 0; i--) {

					if (i == prod.getHistoricos().size() - 1 || i == prod.getHistoricos().size() - 2
							|| i == prod.getHistoricos().size() - 3 || i == prod.getHistoricos().size() - 13) {
						quantidade.add(prod.getHistoricos().get(i).getQuantidade());
					}
				}
				for (int i = 0; i < quantidade.size(); i++) {

					if (i == 0) {
						prod.setPrimeiraQuantidade(quantidade.get(i));
						System.out.println("====Quantidade===== " + quantidade.get(i));
					}
					if (i == 1) {
						prod.setSegundaQuantidade(quantidade.get(i));
						System.out.println("====Quantidade===== " + quantidade.get(i));
					}
					if (i == 2) {
						prod.setTerceiraQuantidade(quantidade.get(i));
						System.out.println("====Quantidade===== " + quantidade.get(i));
					}
					if (i == 3) {
						prod.setUltimaQuantidade(quantidade.get(i));
						System.out.println("====Quantidade===== " + quantidade.get(i));
					}

				}
				Double alfa = BigDecimal.valueOf((fatorAmortecimentoExponencial(arrayComUltimosCincoMeses)))
						.setScale(3, RoundingMode.HALF_UP).doubleValue();
				System.out.println("Alfa " + alfa);
				System.out.println("Melhor taxa: " + alfa + " - resultado: "
						+ Math.round(calculoPrevisao(alfa.doubleValue(), arrayComQuantidades)));
				Double previsao = (calculoPrevisao(alfa.doubleValue(), arrayComQuantidades));
				listaComCincoUltimosMeses.clear();
				listaComQuantidade.clear();
				mesesDoProduto.clear();
				quantidade.clear();
				prod.setEmpresa(empresaSelecionada);
				// prod.setGerenteFilial(gerenteFilial);
				prod.setFatorAmortecimentoExponencial(alfa);
				prod.setQuantidadeRecente(previsao);
				prod.setQuantidadeReais(prod.getValor().multiply(new BigDecimal(previsao)));

				cadastroProdutoService.importar(prod);// persistir
														// após
														// calcular
														// o
														// alfa

			}
			FacesUtil.addInfoMessage(FacesUtil.getMensagemI18n("arquivo_processado") + " : " + nomeArquivo);

		}
	}

	public double calculoPrevisao(double a, double... d) {
		double soma = 0.0;
		double potencia = 1.0;
		for (int i = d.length - 1; i >= 0; i--) {
			soma += a * potencia * d[i];
			potencia *= 1.0 - a;
		}
		return soma;
	}

	public double fatorAmortecimentoExponencial(double... d) {
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

	private void adicionaHistorico(Produto prod) {

		for (int i = 0; i < prod.getHistoricos().size(); i++) {

			System.out.println("Mes " + prod.getHistoricos().get(i).getMesesHistoricos() == null ? "Erro"
					: prod.getHistoricos().get(i).getMesesHistoricos());
			System.out.println("Quantidade " + prod.getHistoricos().get(i).getQuantidade());

			listaComQuantidade.add(prod.getHistoricos().get(i));

		}
	}

	private void adicionaUltimosCincoHistorico(Produto prod) {

		for (int i = prod.getHistoricos().size() - 5; i < prod.getHistoricos().size(); i++) {

			System.out.println("Mes " + prod.getHistoricos().get(i).getMesesHistoricos() == null ? "Erro"
					: prod.getHistoricos().get(i).getMesesHistoricos());
			System.out.println("Quantidade " + prod.getHistoricos().get(i).getQuantidade());

			listaComCincoUltimosMeses.add(prod.getHistoricos().get(i));

		}

	}

	@NotNull
	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}

	@NotNull
	public Familia getFamiliaSelecionada() {
		return familiaSelecionada;
	}

	public List<Familia> getFamiliasRaizes() {
		return familiasRaizes;
	}

	public void setFamiliaSelecionada(Familia familiaSelecionada) {
		this.familiaSelecionada = familiaSelecionada;
	}

	public List<Empresa> getTodasEmpresas() {
		return todasEmpresas;
	}

	@NotNull
	public Linha getLinhaSelecionada() {
		return linhaSelecionada;
	}

	public List<Linha> getLinhasRaizes() {
		return linhasRaizes;
	}

	public void setLinhaSelecionada(Linha linhaSelecionada) {
		this.linhaSelecionada = linhaSelecionada;
	}
}
