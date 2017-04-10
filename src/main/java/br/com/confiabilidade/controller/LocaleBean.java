package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

@Named
@SessionScoped
public class LocaleBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String locale ;
	//= FacesContext.getCurrentInstance().getViewRoot().getLocale().toString();

	public LocaleBean() {
		
	}
	
	@PostConstruct
    public void init() {
        //give the default value here
        locale = new Locale("pt").toString();
       
    }


	public void mudaLingua(ActionEvent event) {
		String localeId = event.getComponent().getId();
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot viewRoot = context.getViewRoot();
		
		viewRoot.setLocale(new Locale(localeId));
		locale = localeId;
	}

	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	
}