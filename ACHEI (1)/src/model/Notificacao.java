package model;

import java.util.Date;

public class Notificacao {

	private int idNotificacao;
	private Objeto objeto;
	private Date data;
	private String mensagem;
	private String nomeMensagem;
	private String assuntoMensagem;
	private int situacao;
	private double latitude;
	private double longitude;
	
	public int getIdNotificacao() {
		return idNotificacao;
	}
	public void setIdNotificacao(int idNotificacao) {
		this.idNotificacao = idNotificacao;
	}
	public Objeto getObjeto() {
		return objeto;
	}
	public void setObjeto(Objeto objeto) {
		this.objeto = objeto;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getNomeMensagem() {
		return nomeMensagem;
	}
	public void setNomeMensagem(String nomeMensagem) {
		this.nomeMensagem = nomeMensagem;
	}
	public String getAssuntoMensagem() {
		return assuntoMensagem;
	}
	public void setAssuntoMensagem(String assuntoMensagem) {
		this.assuntoMensagem = assuntoMensagem;
	}
	public int getSituacao() {
		return situacao;
	}
	public void setSituacao(int situacao) {
		this.situacao = situacao;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	
	
	
	
}
