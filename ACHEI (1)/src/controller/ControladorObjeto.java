package controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import dao.ObjetoDAO;
import model.Codigo;
import model.Objeto;

@ManagedBean 
public class ControladorObjeto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Objeto objeto = new Objeto();
	private Codigo codigo = new Codigo();
	private String codigoString;
	
	
	
	public String EditarObjeto(){
	
		return "EditarCodigo.xhtml";
	}
	
	public void alteraObjeto(){
		ObjetoDAO o =  new ObjetoDAO();
		if(o.incluirOuAlterarObjeto(this.getObjeto()) >0){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dados atualizados.",""));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar objeto.",""));
		}
	}
	
	public String imprimir(){
		this.codigoString = Integer.toString(this.getCodigo().getIdCodigo())+this.getCodigo().getCodigo();
		return "Imprimir.xhtml";
	}
	
	
	public Objeto getObjeto() {
		return objeto;
	}
	public void setObjeto(Objeto objeto) {
		this.objeto = objeto;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Codigo getCodigo() {
		return codigo;
	}

	public void setCodigo(Codigo codigo) {
		this.codigo = codigo;
	}

	public String getCodigoString() {
		return codigoString;
	}

	public void setCodigoString(String codigoString) {
		this.codigoString = codigoString;
	}

	
}
