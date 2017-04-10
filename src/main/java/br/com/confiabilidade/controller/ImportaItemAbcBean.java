package br.com.confiabilidade.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.ItemAbc;
import br.com.confiabilidade.repository.Empresas;
import br.com.confiabilidade.repository.Items;
import br.com.confiabilidade.repository.ItemsAbc;
import br.com.confiabilidade.service.CadastroItemAbcService;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jsf.FacesUtil;

@ViewScoped
@Named
public class ImportaItemAbcBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String diretorioDestino = "c:\\temp\\";

	@Inject
	private ItemsAbc itemsAbc;

	@Inject
	private Items items;

	private Integer progress;

	private Boolean fabricado;

	@SuppressWarnings("rawtypes")
	List sheetData = new ArrayList();

	@SuppressWarnings("unchecked")
	List<ItemAbc> listadeItemsAbc = new ArrayList();

	private UploadedFile uploadedFile;

	public UploadedFile getFile() {
		return uploadedFile;
	}

	public void setFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	@Inject
	private CadastroItemAbcService cadastroItemAbcService;

	@Inject
	Empresas empresas;

	private Empresa empresaSelecionada;

	private List<Empresa> todasEmpresas;

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			buscaEmpresas();
		}

	}

	private void buscaEmpresas() {

		todasEmpresas = empresas.empresasParaImportacaoAbc();

	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		this.uploadedFile = event.getFile();

	}

	public void send() throws IOException {

		if (getFile() == null) {
			FacesUtil.addErrorMessage(FacesUtil
					.getMensagemI18n("escolher_arquivo"));

		} else {
			String nomeArquivo = uploadedFile.getFileName().substring(
					uploadedFile.getFileName().lastIndexOf("\\") + 1);

			try {
				Upload(nomeArquivo, uploadedFile.getInputstream());
				
				FacesUtil.addInfoMessage(FacesUtil
						.getMensagemI18n("arquivo_processado")
						+ " : "
						+ nomeArquivo);


			} catch (IllegalStateException e) {
				FacesUtil
						.addErrorMessage("Coluna Fora da Ordem ou Planilha Incorreta.");
			} catch (IndexOutOfBoundsException e) {
				FacesUtil
						.addErrorMessage("Código ou Descrição do Item não existem na última linha do arquivo. Favor Verificá-lo!");
			}

			
		}
	}

	private void Upload(String fileName, InputStream in)
			throws IllegalStateException, IndexOutOfBoundsException {
		// try {

		if (fileName.endsWith(".xls")) {

			excel2007(in);

		} else if (fileName.endsWith(".xlsx")) {
			excel2010(in);
		} else {
			throw new NegocioException("Arquivo inválido");
		}

		// OutputStream out = new FileOutputStream(new File(diretorioDestino
		// + fileName));
		// int reader = 0;
		// byte[] bytes = new byte[(int) getFile().getSize()];
		// while ((reader = in.read(bytes)) != -1) {
		// out.write(bytes, 0, reader);
		// }
		// in.close();
		// out.flush();
		// out.close();
		// } catch (IOException e) {
		// System.out.println(e.getMessage());
		// }
	}

	private void excel2007(InputStream input) {

		HSSFWorkbook workbook;
		try {
			workbook = new HSSFWorkbook(input);

			HSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.rowIterator();
			rowIterator.next();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				@SuppressWarnings("rawtypes")
				List data = new ArrayList();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					data.add(cell);

				}

				sheetData.add(data);

			}

			double demanda = 0L;
			for (int i = 0; i < sheetData.size(); i++) {
				@SuppressWarnings("rawtypes")
				List list = (List) sheetData.get(i);
				HSSFCell codigoItem = (HSSFCell) list.get(0);
				HSSFCell descricaoAbc = (HSSFCell) list.get(1);
				HSSFCell preco = (HSSFCell) list.get(list.size() - 1);
				int totalColunas = 0;
				double somaCelula = 0d;
				double media = 0d;
				int contaDE = 0;

				for (int j = 2; j < list.size() - 1; j++) {
					totalColunas = list.size() - 3;

					HSSFCell numero = (HSSFCell) list.get(j);
					if (numero.getNumericCellValue() == 0.0) { // 0 - Numerico
						++contaDE;

					} else {
						somaCelula += (numero.getNumericCellValue());
					}

				}
				ItemAbc itemAbc = new ItemAbc();
				
				int contaCelulasPreenchidas = totalColunas - contaDE;
				if (codigoItem.getCellType() == 1) {// 1- String

					if (contaCelulasPreenchidas <= 6
							&& contaCelulasPreenchidas >= 4) {

						itemAbc.setAbc("D");

					} else if (contaCelulasPreenchidas <= 3
							&& contaCelulasPreenchidas >= 0) {

						itemAbc.setAbc("E");

					} else {

						itemAbc.setAbc(null);

					}

				}

				if (codigoItem.getCellType() == 0) {// 0 - numero

					if (contaCelulasPreenchidas >= 4
							&& contaCelulasPreenchidas <= 6) {

						itemAbc.setAbc("D");

					}
					if (contaCelulasPreenchidas <= 3
							&& contaCelulasPreenchidas >= 0) {

						itemAbc.setAbc("E");

					}
					if (contaCelulasPreenchidas >= 5) {

						itemAbc.setAbc(null);

					}

				}
				itemAbc.setDataItemAbc(new Date());
				itemAbc.setEmpresa(empresaSelecionada);

				if (contaCelulasPreenchidas == 0) {

					media = 0L;

				} else {

					// double soma = Math.round(somaCelula);
					demanda = round2(somaCelula / (contaCelulasPreenchidas));

					media = Math.round(demanda * preco.getNumericCellValue());

				}

				if (codigoItem.getCellType() == 0) {// 0 - numero

					int codItem = ((int) codigoItem.getNumericCellValue());
					Integer codigo = codItem;
					itemAbc.setCodigoItemAbc(codigo.toString());
				}
				if (codigoItem.getCellType() == 1) {
					itemAbc.setCodigoItemAbc(codigoItem.getStringCellValue()
							.trim());
				}

				itemAbc.setDemanda(demanda);
				itemAbc.setMedia(media);

				itemAbc.setDescricaoAbc(descricaoAbc.getStringCellValue()
						.trim());
				listadeItemsAbc.add(itemAbc);

			}

			calcularPercentual(listadeItemsAbc);
			calcularPorcentagem(listadeItemsAbc);
			calcularAbc(listadeItemsAbc);

			List<Item> itemsDaEmpresa = items
					.buscaItemsDaEmpresa(empresaSelecionada);

			for (Item item : itemsDaEmpresa) {

				itemsAbc.update(item);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private void excel2010(InputStream input) throws IllegalStateException {
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(input);

			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.rowIterator();
			rowIterator.next();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				// .buscaCodigo(empresaSelecionada);
				// if (!verificaCodigosItemXItemAbc(listaDeCodigos,
				// codigoItemParaVerificar)) {
				// throw new NegocioException(
				// "Os Códigos dos Items ABCDE importados devem Corresponder Aos Criticados!!!");
				// }
				@SuppressWarnings("rawtypes")
				List data = new ArrayList();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					data.add(cell);

				}

				sheetData.add(data);

			}
			double media = 0L;
			for (int i = 0; i < sheetData.size(); i++) {
				@SuppressWarnings("rawtypes")
				List list = (List) sheetData.get(i);
				XSSFCell codigoItem = (XSSFCell) list.get(0);
				XSSFCell descricaoAbc = (XSSFCell) list.get(1);
				XSSFCell preco = (XSSFCell) list.get(list.size() - 1);
				int totalColunas = 0;
				double somaCelula = 0d;
				double demanda = 0d;
				int contaDE = 0;

				for (int j = 2; j < list.size() - 1; j++) {

					totalColunas = list.size() - 3;

					XSSFCell numero = (XSSFCell) list.get(j);
					if (numero.getNumericCellValue() == 0.0) { // 0 - Numerico
						++contaDE;

					} else {
						somaCelula += (numero.getNumericCellValue());
					}

				}

				ItemAbc itemAbc = new ItemAbc();

				int contaCelulasPreenchidas = totalColunas - contaDE;

				if (codigoItem.getCellType() == 1) {// 1- String

					if (contaCelulasPreenchidas >= 4
							&& contaCelulasPreenchidas <= 6) {

						itemAbc.setAbc("D");

					} else if (contaCelulasPreenchidas <= 3
							&& contaCelulasPreenchidas >= 0) {

						itemAbc.setAbc("E");

					} else {

						itemAbc.setAbc(null);

					}

				}

				if (codigoItem.getCellType() == 0) {// 0 - numero

					if (contaCelulasPreenchidas >= 4
							&& contaCelulasPreenchidas <= 6) {

						itemAbc.setAbc("D");

					}
					if (contaCelulasPreenchidas <= 3
							&& contaCelulasPreenchidas >= 0) {

						itemAbc.setAbc("E");

					}
					if (contaCelulasPreenchidas >= 5) {

						itemAbc.setAbc(null);

					}

				}
				itemAbc.setDataItemAbc(new Date());
				itemAbc.setEmpresa(empresaSelecionada);

				if (contaCelulasPreenchidas == 0) {

					media = 0L;
				} else {

					// double soma = Math.round(somaCelula);
					demanda = round2(somaCelula / (contaCelulasPreenchidas));
					media = Math.round(demanda * preco.getNumericCellValue());

				}

				if (codigoItem.getCellType() == 0) {// 0 - numero

					int codItem = ((int) codigoItem.getNumericCellValue());
					Integer codigo = codItem;
					itemAbc.setCodigoItemAbc(codigo.toString());
				}
				if (codigoItem.getCellType() == 1) {
					itemAbc.setCodigoItemAbc(codigoItem.getStringCellValue()
							.trim());
				}

				itemAbc.setDemanda(demanda);
				itemAbc.setMedia(media);

				itemAbc.setDescricaoAbc(descricaoAbc.getStringCellValue()
						.trim());
				listadeItemsAbc.add(itemAbc);

			}

			calcularPercentual(listadeItemsAbc);
			calcularPorcentagem(listadeItemsAbc);
			calcularAbc(listadeItemsAbc);

			List<Item> itemsDaEmpresa = items
					.buscaItemsDaEmpresa(empresaSelecionada);

			for (Item item : itemsDaEmpresa) {

				itemsAbc.update(item);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean verificaCodigosItemXItemAbc(List<String> listaDeCodigos,
			XSSFCell codigoItem) {

		boolean encontrou = false;
		if (!listaDeCodigos.isEmpty()) { // Só verifica se Existir Items em
											// Criticidade
			if (listaDeCodigos.contains(codigoItem.getRichStringCellValue()
					.toString())) {

				encontrou = true;

			}

		} else {// Só Quero importar ABCDE sem verificar Item x ItemAbc
			encontrou = true;
		}

		return encontrou;
	}

	private void calcularPercentual(List<ItemAbc> listadeItemsAbc) {

		Collections.sort(listadeItemsAbc);
		int c = 0;
		double anterior = 0;

		for (ItemAbc itemsAbcAtual : listadeItemsAbc) {

			if (c == 0) {
				itemsAbcAtual.setPercentual(itemsAbcAtual.getMedia());
				anterior = itemsAbcAtual.getMedia();

			} else {

				if (!("D".equals(itemsAbcAtual.getAbc()) || ("E"
						.equals(itemsAbcAtual.getAbc())))) {

					itemsAbcAtual
							.setPercentual((itemsAbcAtual.getMedia() + anterior));
					anterior = itemsAbcAtual.getPercentual();

				} else {

					itemsAbcAtual.setPercentual((0d));

				}
			}

			++c;

		}

	}

	private double calculaMaiorPercentual(List<ItemAbc> listadeItemsAbc) {
		double maiorPercentual = 0d;
		int c = 0;
		for (ItemAbc itemsAbcAtual : listadeItemsAbc) {
			if (c == 0) {
				maiorPercentual = itemsAbcAtual.getPercentual();
			}

			if (maiorPercentual < itemsAbcAtual.getPercentual()) {
				maiorPercentual = itemsAbcAtual.getPercentual();
			}
			++c;
		}

		return maiorPercentual;
	}

	private void calcularPorcentagem(List<ItemAbc> listadeItemsAbc) {

		Collections.sort(listadeItemsAbc);
		double ultimoPercentual = 0d;

		ultimoPercentual = calculaMaiorPercentual(listadeItemsAbc);
		for (ItemAbc itemsAbcAtual : listadeItemsAbc) {

			int percentagem = (int) Math.round((itemsAbcAtual.getPercentual()
					/ ultimoPercentual * 100));

			itemsAbcAtual.setPercentagem(percentagem);

		}
	}

	private void calcularAbc(List<ItemAbc> listadeItemsAbc) {

		for (ItemAbc itemAbc : listadeItemsAbc) {
			if ((itemAbc.getPercentagem() > 75 && itemAbc.getPercentagem() <= 95)
					&& (!"D".equals(itemAbc.getAbc()) && (!"E".equals(itemAbc
							.getAbc()))))
				itemAbc.setAbc("B");
			if (itemAbc.getPercentagem() > 95
					&& (!"D".equals(itemAbc.getAbc()) && (!"E".equals(itemAbc
							.getAbc())))) {
				itemAbc.setAbc("C");

			}
			if (itemAbc.getPercentagem() <= 75
					&& (!"D".equals(itemAbc.getAbc()) && (!"E".equals(itemAbc
							.getAbc())))) {
				itemAbc.setAbc("A");
			}

			cadastroItemAbcService.salvar(itemAbc);

		}
	}

	double round2(double x) { // Arredonda 2 digitos

		final double factor = 1e2;

		return (long) (x * factor + 0.5) / factor;

	}

	public Boolean getFabricado() {
		return fabricado;
	}

	public void setFabricado(Boolean fabricado) {
		this.fabricado = fabricado;
	}

	@NotNull
	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}

	public List<Empresa> getTodasEmpresas() {
		return todasEmpresas;
	}

}
