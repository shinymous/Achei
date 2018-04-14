package model;

import java.util.ArrayList;
import java.util.Date;

public class Cliente {

	private int idCliente;
	private String nomeCliente;
	private String sobrenomeCliente;
	private String emailCliente;
	private String senhaCliente;
	private Date dataNascimenteCliente;
	private int qtCodigoGratis;
	private Endereco endereco;
	private Contato contato;
	private ArrayList<Codigo> listaCodigos;
	private ArrayList<Notificacao> listaNotificacao;
	private String caminhoImg;
	
	
	
	public int getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	public String getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public String getSobrenomeCliente() {
		return sobrenomeCliente;
	}
	public void setSobrenomeCliente(String sobrenomeCliente) {
		this.sobrenomeCliente = sobrenomeCliente;
	}
	public String getEmailCliente() {
		return emailCliente;
	}
	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}
	public String getSenhaCliente() {
		return senhaCliente;
	}
	public void setSenhaCliente(String senhaCliente) {
		this.senhaCliente = senhaCliente;
	}
	public Date getDataNascimenteCliente() {
		return dataNascimenteCliente;
	}
	public void setDataNascimenteCliente(Date dataNascimenteCliente) {
		this.dataNascimenteCliente = dataNascimenteCliente;
	}
	public int getQtCodigoGratis() {
		return qtCodigoGratis;
	}
	public void setQtCodigoGratis(int qtCodigoGratis) {
		this.qtCodigoGratis = qtCodigoGratis;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public Contato getContato() {
		return contato;
	}
	public void setContato(Contato contato) {
		this.contato = contato;
	}
	public ArrayList<Codigo> getListaCodigos() {
		return listaCodigos;
	}
	public void setListaCodigos(ArrayList<Codigo> listaCodigos) {
		this.listaCodigos = listaCodigos;
	}
	public ArrayList<Notificacao> getListaNotificacao() {
		return listaNotificacao;
	}
	public void setListaNotificacao(ArrayList<Notificacao> listaNotificacao) {
		this.listaNotificacao = listaNotificacao;
	}
	public String getCaminhoImg() {
		return caminhoImg;
	}
	public void setCaminhoImg(String caminhoImg) {
		this.caminhoImg = caminhoImg;
	}
	
	
	
}
