package br.com.previsao.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import br.com.previsao.model.Empresa;
import br.com.previsao.repository.Empresas;
import br.com.previsao.service.CadastroEmpresaService;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEmpresasBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Empresas empresas;

	@Inject
	CadastroEmpresaService cadastrarEmpresaService;

	private List<Empresa> listaDeEmpresas = new ArrayList<>();

	private Empresa empresaSelecionada;

	public List<Empresa> getEmpresas() {
		return listaDeEmpresas;
	}

	public void inicializar() {

		listaDeEmpresas = empresas.todasEmpresas();

	}

	public void excluir() {
		empresas.remover(empresaSelecionada);
		this.listaDeEmpresas.remove(empresaSelecionada);
		FacesUtil.addInfoMessage("Empresa " + empresaSelecionada.getNome() + " excluída com sucesso.");
	}

	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}

	public List<Empresa> getListaDeEmpresas() {
		return listaDeEmpresas;
	}

	public void onRowEdit(RowEditEvent event) {
		
		Empresa emp = ((Empresa) event.getObject());
		System.out.println(emp.toString());
		cadastrarEmpresaService.salvar(emp);
		
        FacesMessage msg = new FacesMessage("Car Edited", ((Empresa) event.getObject()).getNome());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Empresa) event.getObject()).getNome());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
	
	public void onEdit(RowEditEvent event) {
		Empresa emp = ((Empresa) event.getObject());
		// System.out.println(ag.toString());
		/*
		 * if (dao.atualizar(ag) == null)
		 * this.setMensagem("Erro ao atualizar agência"); else
		 * this.setMensagem("Dados atualizados com sucesso.");
		 */
		empresas.guardar(emp);

	}

}
