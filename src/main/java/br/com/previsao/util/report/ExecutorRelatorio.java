package br.com.previsao.util.report;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.hibernate.jdbc.Work;

public class ExecutorRelatorio implements Work {

	private String caminhoRelatorio;
	private HttpServletResponse response;
	private Map<String, Object> parametros;
	private String nomeArquivoSaida;
	
	private boolean relatorioGerado;
	
	public ExecutorRelatorio(String caminhoRelatorio,
			HttpServletResponse response, Map<String, Object> parametros,
			String nomeArquivoSaida) {
		this.caminhoRelatorio = caminhoRelatorio;
		this.response = response;
		this.parametros = parametros;
		this.nomeArquivoSaida = nomeArquivoSaida;
		
		this.parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try {
			InputStream relatorioStream = this.getClass().getResourceAsStream(this.caminhoRelatorio);
			
			JasperPrint print = JasperFillManager.fillReport(relatorioStream, this.parametros, connection);
			this.relatorioGerado = print.getPages().size() > 0;
			
			if (this.relatorioGerado) {
				if (nomeArquivoSaida.endsWith("xls")){
					 JRXlsExporter exporter = new JRXlsExporter(); 
					 ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();  

				        try {  
				            
				exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,print);  
				exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,xlsReport);  
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);  
				exporter.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, 20000);  // aqui eu coloco o limite de linhas por planilha.   
				exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.TRUE);  
				exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);  
				  
				            exporter.exportReport();  
				  
				            byte[] buffer = xlsReport.toByteArray();  
				            xlsReport.close();  
				  
				            response.setContentType("application/xls");  
				            response.setHeader("Content-disposition", "filename=\"" 
									+ this.nomeArquivoSaida  + "\"");  
				  
				            ServletOutputStream out = response.getOutputStream();  
				            if (buffer != null) {  
				                out.write(buffer);  
				            }  
				            out.flush();  
				            out.close();  
				        } catch (Exception e) {  
				            e.printStackTrace();  
				        }  
					
				}else{
					JRExporter exportador = new JRPdfExporter();
					
					exportador.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
					exportador.setParameter(JRExporterParameter.JASPER_PRINT, print);
					
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "attachment; filename=\"" 
							+ this.nomeArquivoSaida  + "\"");
					exportador.exportReport();
					
				}
				
				
			}
		} catch (Exception e) {
			throw new SQLException("Erro ao executar relatório " + this.caminhoRelatorio, e);
		}
	}

	public boolean isRelatorioGerado() {
		return relatorioGerado;
	}

}
