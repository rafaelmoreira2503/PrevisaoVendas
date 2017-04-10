package br.com.confiabilidade.controller;

import java.math.BigDecimal;
import java.util.Map;

import javax.inject.Inject;

import org.primefaces.model.chart.PieChartModel;

import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.Items;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jsf.FacesUtil;

public class CriaGraficoParaTecnico {
	private Usuario tecnicoSelecionado;

	@Inject
	private Items items;

	private BigDecimal resultado;
	private BigDecimal criticado;
	private BigDecimal justificado;
	private BigDecimal pendente;

	private BigDecimal duracaoProjetoParaTecnico;
	private BigDecimal decorridoParaTecnico;

	private PieChartModel graficoDoTecnico;
	public CriaGraficoParaTecnico(){
		
	}

	public CriaGraficoParaTecnico(Usuario tecnicoSelecionado,
			BigDecimal duracaoProjetoParaTecnico,
			BigDecimal decorridoParaTecnico) {
		this.tecnicoSelecionado = tecnicoSelecionado;
		this.decorridoParaTecnico = decorridoParaTecnico;
		this.duracaoProjetoParaTecnico = duracaoProjetoParaTecnico;
		this.graficoDoTecnico = new PieChartModel();

	}

	public BigDecimal calcularPercentualDoTecnico() {

		BigDecimal soma = criticado.add(justificado).add(pendente);

		BigDecimal percent = new BigDecimal("100");
		if (BigDecimal.ZERO.equals(soma)) {
			throw new NegocioException(
					"Calculo não pode ser feito deve existir pelo menos um Item Associado ou criticado.");
		}

		BigDecimal percentualFeitoDoTecnico = criticado.add(justificado)
				.divide(soma, 2, BigDecimal.ROUND_HALF_UP).multiply(percent);

		percentualFeitoDoTecnico = percentualFeitoDoTecnico.setScale(0,
				BigDecimal.ROUND_HALF_UP);// arredonda para cima e tira o
											// ponto
											// decimal resultando em um
											// inteiro
		System.out.println("Percentual do Grafico" + percentualFeitoDoTecnico);

		return percentualFeitoDoTecnico; // expansion;

	}

	// Calcula o percentual do Projeto baseado na data ini e fim da empresa
	public BigDecimal calcularPercentualDoProjetoParaTecnico() {

		resultado = BigDecimal.valueOf((decorridoParaTecnico.intValue()
				/ duracaoProjetoParaTecnico.intValue() * 100));
		resultado = resultado.setScale(0, BigDecimal.ROUND_HALF_UP);

		// /System.out.println("Percentual do Projeto para  Tecnico" +
		// resultado);

		return resultado;

	}

	public void criarGraficoPizzaDoTecnico() {

		graficoDoTecnico.clear();

		Map<String, Long> quantidadeCriticadaPorTecnico = this.items
				.quantidadeCriticadoFeitoParaEncarregado(tecnicoSelecionado);

		for (String tecnicos : quantidadeCriticadaPorTecnico.keySet()) {

			// System.out.println("Técnico :" + tecnicos + "Valores :"
			// + quantidadeCriticadaPorTecnico.get(tecnicos));

			criticado = new BigDecimal(
					quantidadeCriticadaPorTecnico.get(tecnicos));
			graficoDoTecnico.set(tecnicos,
					quantidadeCriticadaPorTecnico.get(tecnicos));

		}

		Map<String, Long> quantidadeJustificadoPorTecnico = this.items
				.quantidadeJustificadoFeitoParaEncarregado(tecnicoSelecionado);

		for (String justificativas : quantidadeJustificadoPorTecnico.keySet()) {

			
			justificado = new BigDecimal(
					quantidadeJustificadoPorTecnico.get(justificativas));
			graficoDoTecnico.set(justificativas,
					quantidadeJustificadoPorTecnico.get(justificativas));

		}

		Map<String, Long> quantidadePendente = this.items
				.quantidadePendenteParaEncarregado(tecnicoSelecionado);
		for (String total : quantidadePendente.keySet()) {

			

			pendente = new BigDecimal(quantidadePendente.get(total));

			graficoDoTecnico.set(total, quantidadePendente.get(total));

		
			
			if (isPrazoMaiorParaTecnico()) {

				FacesUtil
						.addErrorMessage("Prazo do Projeto Crítico Para o Técnico!!!");
			}

		}

	}

	public boolean isPrazoMaiorParaTecnico() {

		boolean prazoMaiorParaTecnico = false;

		if (calcularPercentualDoTecnico().intValue() <= calcularPercentualDoProjetoParaTecnico()
				.intValue()
				&& calcularPercentualDoProjetoParaTecnico().intValue() != 0) {

			prazoMaiorParaTecnico = true;
		}
		return prazoMaiorParaTecnico;
	}

	public BigDecimal getCriticado() {
		return criticado;
	}

	public BigDecimal getJustificado() {
		return justificado;
	}

	public BigDecimal getPendente() {
		return pendente;
	}

	public BigDecimal getDuracaoProjetoParaTecnico() {
		return duracaoProjetoParaTecnico;
	}

	public BigDecimal getDecorridoParaTecnico() {
		return decorridoParaTecnico;
	}

}
