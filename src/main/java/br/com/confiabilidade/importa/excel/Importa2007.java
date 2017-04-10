package br.com.confiabilidade.importa.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.UploadedFile;

import br.com.confiabilidade.model.Area;
import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.StatusItem;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.Empresas;
import br.com.confiabilidade.repository.Usuarios;
import br.com.confiabilidade.service.CadastroItemService;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jsf.FacesUtil;

public class Importa2007 implements Importar {

	private String diretorioDestino = "c:\\temp\\";

	private List<Area> areasDoEncarregado;

	private Integer progress;

	private boolean desabilita = false;

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

	@Override
	public void importar(InputStream input) {
	

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

			//	List<Usuario> areasDoEncarregado = usuarios
			//			.buscaAreaEncarregado(empresaSelecionada.getCodigo());// na
			///																	// empresa
																				// nde
																				// a
																				// area
																				// nao
																				// é
																				// nula
																				// para
																				// o
																				// encarregado

				if (areasDoEncarregado.isEmpty()) {

					throw new NegocioException(
							FacesUtil.getMensagemI18n("areas_para_importacao"));
				}
				progress = 0;

//				for (Usuario area : areasDoEncarregado) {
//					
//					for (int i = 0; i < sheetData.size(); i++) {
//						@SuppressWarnings("rawtypes")
//						List list = (List) sheetData.get(i);
//
//						HSSFCell codigoItem = (HSSFCell) list.get(0);
//						HSSFCell descricao = (HSSFCell) list.get(1);
//						
//						if (codigoItem.getCellType() == 0) {// 0 - numero
//
//							int codItem = ((int) codigoItem.getNumericCellValue());
//							Integer codigo = codItem;
//							item.setCodigoItem(codigo.toString());
//						}
//						if (codigoItem.getCellType() == 1) {
//							
//							item.setCodigoItem(codigoItem.getStringCellValue()
//									.trim());
//						}
//
//						
//						item.setDescricao(descricao.getStringCellValue().trim());
//						item.setDataItem(new Date());
//						item.setEmpresa(empresaSelecionada);
//
//						item.setStatus(StatusItem.PENDENTE);
//						item.setAreaItem(area.getAreaEncarregado());
//						progress++;
//						if (progress == sheetData.size() ) {
//							progress = 100;
//						}
//
//						cadastroItemService.importar(item);
//						
//
//					}
//				}
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

		

}
