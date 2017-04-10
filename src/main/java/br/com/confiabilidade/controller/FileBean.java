package br.com.confiabilidade.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
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

import br.com.confiabilidade.model.Area;
import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.StatusItem;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.Empresas;
import br.com.confiabilidade.repository.Items;
import br.com.confiabilidade.repository.Usuarios;
import br.com.confiabilidade.service.CadastroItemService;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class FileBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String diretorioDestino = "c:\\temp\\";

	private List<Area> areasDoEncarregado;

	private Integer progress;

	private boolean desabilita = false;

	@Inject
	private Items items;

	private UploadedFile uploadedFile;

	public UploadedFile getFile() {
		return uploadedFile;
	}

	public void setFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	@Inject
	private Usuarios usuarios;

	// Create an ArrayList to store the data read from excel sheet.
	//
	@SuppressWarnings("rawtypes")
	List sheetData = new ArrayList();

	private Item item = new Item();

	@Inject
	private CadastroItemService cadastroItemService;

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

		todasEmpresas = empresas.empresasParaImportacaoCriticidade();

	}

	public boolean isDesabilitaBotao() {

		return desabilita;

	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {

		this.uploadedFile = event.getFile();

	}

	public void send() throws IOException {
		this.desabilita = true;
		
		if (getFile() == null) {
			FacesUtil.addErrorMessage(FacesUtil
					.getMensagemI18n("escolher_arquivo"));

		} else {
			String nomeArquivo = uploadedFile.getFileName().substring(
					uploadedFile.getFileName().lastIndexOf("\\") + 1);

			Upload(nomeArquivo, uploadedFile.getInputstream());

			FacesUtil.addInfoMessage(FacesUtil
					.getMensagemI18n("arquivo_processado")
					+ " : "
					+ nomeArquivo);
			buscaEmpresas();
			this.desabilita = false;
			
		}
	}

	private void Upload(String fileName, InputStream in) {
		try {

			if (fileName.endsWith(".xls")) {

				Excel2007(in);

			} else {
				Excel2010(in);
			}

			OutputStream out = new FileOutputStream(new File(diretorioDestino
					+ fileName));
			int reader = 0;
			byte[] bytes = new byte[(int) getFile().getSize()];
			while ((reader = in.read(bytes)) != -1) {
				out.write(bytes, 0, reader);
			}
			in.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public Integer getProgress() {
		if (progress == null) {
			progress = 0;
		}

		return progress;
	}

	@SuppressWarnings("unchecked")
	private void Excel2007(InputStream input) {

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

				this.sheetData.add(data);
			}

			List<Usuario> areasDoEncarregado = usuarios
					.buscaAreaEncarregado(empresaSelecionada.getCodigo());

			if (areasDoEncarregado.isEmpty()) {

				throw new NegocioException(
						FacesUtil.getMensagemI18n("areas_para_importacao"));
			}
			progress = 0;
			List<String> itensDaEmpresa = items.buscaCodigo(empresaSelecionada);
			for(String item :itensDaEmpresa){
				
				System.out.println("Codigo: "+ item);
			}
			
			for (Usuario area : areasDoEncarregado) {

				for (int i = 0; i < sheetData.size(); i++) {
					@SuppressWarnings("rawtypes")
					List list = (List) sheetData.get(i);

					HSSFCell codigoItem = (HSSFCell) list.get(0);
					HSSFCell descricao = (HSSFCell) list.get(1);
					
					String codigoParaVerificacao = "";
					
					
						if (codigoItem.getCellType() == 0) {// 0 - numero

							Integer codItem = ((int) codigoItem
									.getNumericCellValue());
							Integer codigo = codItem;
							//item.setCodigoItem(codigo.toString());
							codigoParaVerificacao = codigo.toString();
						}
						if (codigoItem.getCellType() == 1) { //1 - String alfanumerico

//							item.setCodigoItem(codigoItem.getStringCellValue()
//									.trim());
							codigoParaVerificacao = codigoItem.getStringCellValue().trim();
						}
						
						if (!itensDaEmpresa.contains(codigoParaVerificacao)
								) {
						item.setCodigoItem(codigoParaVerificacao);
						item.setDescricao(descricao.getStringCellValue().trim());
						item.setDataItem(new Date());
						item.setEmpresa(empresaSelecionada);

						item.setStatus(StatusItem.PENDENTE);
						item.setAreaItem(area.getAreaEncarregado());
						progress++;
						if (progress == sheetData.size()) {
							progress = 100;
						}

						cadastroItemService.importar(item);
					}
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private void Excel2010(InputStream input) {

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

				@SuppressWarnings("rawtypes")
				List data = new ArrayList();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					data.add(cell);

				}

				sheetData.add(data);
			}
			List<Usuario> areasDoEncarregado = usuarios
					.buscaAreaEncarregado(empresaSelecionada.getCodigo());

			if (areasDoEncarregado.isEmpty()) {

				throw new NegocioException(
						FacesUtil.getMensagemI18n("areas_para_importacao"));
			}
			progress = 0;
			List<String> itensDaEmpresa = items.buscaCodigo(empresaSelecionada);
			for (Usuario area : areasDoEncarregado) {

				for (int i = 0; i < sheetData.size(); i++) {
					@SuppressWarnings("rawtypes")
					List list = (List) sheetData.get(i);

					XSSFCell codigoItem = (XSSFCell) list.get(0);
					XSSFCell descricao = (XSSFCell) list.get(1);
					String codigoParaVerificacao = "";
					
					
					if (codigoItem.getCellType() == 0) {// 0 - numero

						Integer codItem = ((int) codigoItem
								.getNumericCellValue());
						Integer codigo = codItem;
						codigoParaVerificacao = codigo.toString();
					}
					if (codigoItem.getCellType() == 1) { //1 - String alfanumerico
						codigoParaVerificacao = codigoItem.getStringCellValue().trim();
					}
					
					
					if (!itensDaEmpresa.contains(codigoParaVerificacao)	) {
					
						item.setCodigoItem(codigoParaVerificacao);
						item.setDescricao(descricao.getStringCellValue().trim());
						item.setDataItem(new Date());
						item.setEmpresa(empresaSelecionada);

						item.setStatus(StatusItem.PENDENTE);
						item.setAreaItem(area.getAreaEncarregado());
						progress++;
						if (progress == sheetData.size()) {
							progress = 100;
						}

						cadastroItemService.importar(item);
					}
				}

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

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

	public List<Area> getAreasDoEncarregado() {
		return areasDoEncarregado;
	}

}
