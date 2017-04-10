package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.modelolazy.AbcLazyItems;
import br.com.confiabilidade.repository.ItemsAbc;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.util.jsf.FacesUtil;
import br.com.confiabilidade.util.report.ExecutorRelatorio;

@Named
@ViewScoped
public class ItemsAbcBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;

	@Inject
	private ItemsAbc itemsAbc;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	private AbcLazyItems lazyItems;
	

	public void emitirPdf() {
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("empresa", this.usuario.getEmpresa().getCodigo());
	
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/relatorio_abc_pdf.jasper",
				this.response, parametros, usuario.getEmpresa().getNomeFantasia() + ".pdf");
		
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);
		
		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
		}
	}

	public void emitirExcel() {
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("empresa", this.usuario.getEmpresa().getCodigo());
	
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/relatorio_abc_excel.jasper",
				this.response, parametros, usuario.getEmpresa().getNomeFantasia() + ".xls");
		
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);
		
		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
		}
	}
	
	public void inicializar() {
		
		lazyItems = new AbcLazyItems(itemsAbc);
		
			}

	public AbcLazyItems getLazyModel() {
		return lazyItems;
	}

	
	
	
}
