package model;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Codigo {

	private int idCodigo;
	private int codigoIdCliente;
	private String codigo;
	private Objeto objeto;
	
	
	public String gerarCodigo(){
		boolean gerarSenha = true;
		String senha="";
		
		while (gerarSenha){
			String[] carct ={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

		    for (int x=0; x<7; x++){
		        int j = (int) (Math.random()*carct.length);
		        senha += carct[j];
		    }
		    
		    try {
		    	Integer.parseInt(senha.substring(0, 1));
			} catch (Exception e) {
				gerarSenha = false;
			}
		}
		
	    return senha;

	}
	
	public int getIdCodigo() {
		return idCodigo;
	}
	public void setIdCodigo(int idCodigo) {
		this.idCodigo = idCodigo;
	}
	public int getCodigoIdCliente() {
		return codigoIdCliente;
	}
	public void setCodigoIdCliente(int codigoIdCliente) {
		this.codigoIdCliente = codigoIdCliente;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Objeto getObjeto() {
		return objeto;
	}

	public void setObjeto(Objeto objeto) {
		this.objeto = objeto;
	}
	
	
	
}
