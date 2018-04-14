package controller;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;
import org.primefaces.context.RequestContext;

import dao.ClienteDAO;
import dao.CodigoDAO;
import dao.ContatoDAO;
import dao.EnderecoDAO;
import dao.NotificacaoDAO;
import dao.ObjetoDAO;
import model.Cliente;
import model.Codigo;
import model.Contato;
import model.Endereco;
import model.Notificacao;
import model.Objeto;
import util.EnviaEmail;
import util.GeradorQr;

@ManagedBean
public class ControladorCodigo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Codigo codigo;
	private CodigoDAO codigoDAO;
	private Cliente cliente;
	private ClienteDAO clienteDAO;
	private Objeto objeto;
	private ObjetoDAO objetoDAO;
	private Notificacao notificacao;
	private NotificacaoDAO notificacaoDAO;
	// pega latitude e longitude do usuario
	private double latitude;
	private double longitude;
	// Verifica se cliente ja tem 3 codigos gratis
	private float precoCodigoGratis;
	private Integer qtCodigoParaComprar;
	private String codigoDigitado;

	public String comprarCodigo() {

		if (pegaClienteNaSessao().getQtCodigoGratis() < 3) {
			this.setPrecoCodigoGratis(0);
		} else {
			this.setPrecoCodigoGratis(1);
		}

		return "AdquirirCodigo.xhml";
	}

	public String adquirirCodigo() {

		// verifica se a quantidade desejada é maio que 0
		if (this.getQtCodigoParaComprar() <= 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite uma quantidade", ""));
			return comprarCodigo();
		}

		clienteDAO = new ClienteDAO();
		cliente = clienteDAO.obterCliente(pegaClienteNaSessao().getIdCliente());
		codigo = new Codigo();
		codigoDAO = new CodigoDAO();

		// Se cliente já tiver todo os codigos gratis redireciona para a pagna
		// de pagamento
		if (cliente.getQtCodigoGratis() >= 3) {
			System.out.println("REDIRECIONANDO PAR APAGINA DE PAGAMENTO");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "FAZER REDIRECIONAMENTO PARA PÁGINA DE PAGAMENTO", ""));

			cliente = null;
			clienteDAO = null;
			codigo = null;
			codigoDAO = null;

			return comprarCodigo();
		}

		// Verifica quantos codis gratis ainda pode ter
		if (this.getQtCodigoParaComprar() + cliente.getQtCodigoGratis() > 3) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Valor inválido, você pode Adquirir somente mais "
							+ (3 - cliente.getQtCodigoGratis()) + " códigos grátis", ""));

			cliente = null;
			clienteDAO = null;
			codigo = null;
			codigoDAO = null;

			return null;
		}

		GeradorQr geradorQr = new GeradorQr();
		while (qtCodigoParaComprar > 0) {
			String codigoGerado = codigo.gerarCodigo();
			codigo.setCodigo(codigoGerado);
			codigo.setCodigoIdCliente(cliente.getIdCliente());
			int retorno = codigoDAO.incluirCodigo(codigo);
			
			qtCodigoParaComprar = qtCodigoParaComprar - 1;

			cliente.setQtCodigoGratis(cliente.getQtCodigoGratis() + 1);
			clienteDAO.atualizarCliente(cliente);

			
			String codigoCompleto = Integer.toString(retorno)+codigoGerado;
			geradorQr.gerarQr(codigoCompleto);
			
			
			objeto = new Objeto();
			objeto.setObjetoIdCodigo(retorno);
			objetoDAO = new ObjetoDAO();
			objetoDAO.incluirOuAlterarObjeto(objeto);
		}

		atualizaClienteNaSessao(this.cliente);
		
		objeto = null;
		objetoDAO = null;
		cliente = null;
		clienteDAO = null;
		codigo = null;
		codigoDAO = null;

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Códigos adquiridos com sucesso.", ""));

		return verCodigos();

	}

	public String verCodigos() {
		cliente = pegaClienteNaSessao();
		codigoDAO = new CodigoDAO();
		cliente.setListaCodigos(codigoDAO.listarCodigos(cliente.getIdCliente()));

		atualizaClienteNaSessao(this.cliente);
		
		return "Codigos.xhtml";
	}

	public String localizar() {
		CodigoDAO codigoDAO = new CodigoDAO();

		int idCodigo = 0;
		String codigo = "";
		String idString = "";

		// verifica se o primeiro digito é um numero.
		try {
			Integer.parseInt(this.getCodigoDigitado().substring(0, 1));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido.", ""));
			return null;
		}

		// separa o ID do código
		for (int i = 1; i < this.getCodigoDigitado().length(); i++) {

			try {
				Integer.parseInt(this.getCodigoDigitado().substring(0, i));
				idString = this.getCodigoDigitado().substring(0, i);
			} catch (Exception e) {
				codigo = this.getCodigoDigitado().substring(i - 1, this.getCodigoDigitado().length());
				break;
			}
		}

		// seta o id do codigo
		idCodigo = Integer.parseInt(idString);

		// localiza cliente com base no codigo
		this.cliente = codigoDAO.localizarDTO(codigo, idCodigo);
		// se nao achar o cliente retorna
		if (this.cliente == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido.", ""));
			return null;
		}
		// seta o objeto com base no cliente achado
		this.objeto = this.cliente.getListaCodigos().get(0).getObjeto();

		// cria a notificacao
		this.notificacao = new Notificacao();
		this.notificacao.setObjeto(this.getObjeto());
		Date data = new Date(System.currentTimeMillis());
		this.notificacao.setData(data);
		this.notificacao.setLatitude(this.getLatitude());
		this.notificacao.setLongitude(this.longitude);
		// insere no bacno e pega o id inserido
		this.notificacaoDAO = new NotificacaoDAO();
		this.notificacao.setIdNotificacao(this.notificacaoDAO.incluirOuAlterarNotificacao(this.notificacao));

		// manda email para o cliente
		if(this.getLatitude() != 0 && this.getLongitude() != 0){
			 new Thread() {
					
				 @Override
				 public void run() {
				 String link =
				 "https://www.google.com/maps/place/"+latitude+","+longitude;
				 	EnviaEmail email = new EnviaEmail();
				 try {
					 email.enviaEmailLocalizacaoObjeto(link, cliente);
				 } catch (MalformedURLException | EmailException e) {
					 e.printStackTrace();
					 System.out.println("Erro ao enviar e-mail "+e);
				 }
				
				 }
				 }.start();
		}else{
			new Thread() {
				
				 @Override
				 public void run() {
					 EnviaEmail email = new EnviaEmail();
				 try {
					 email.enviaEmailSemLocalizacao(cliente);
				 } catch (MalformedURLException | EmailException e) {
					 e.printStackTrace();
				 	System.out.println("Erro ao enviar e-mail "+e);
				 }
				
				 }
				 }.start();
		}
		
		
		return "DadosCodigo.xhtml";
	}

	public String mandaMensagem() {
		if(this.notificacao.getAssuntoMensagem().equals("") && this.getNotificacao().getMensagem().equals("") && this.getNotificacao().getNomeMensagem().equals("")){
			return "Obrigado.xhtml";
		}
		this.notificacaoDAO = new NotificacaoDAO();
		this.notificacaoDAO.incluirOuAlterarNotificacao(this.notificacao);
		ClienteDAO clienteDAO = new ClienteDAO();
		this.cliente = clienteDAO.obterCliente(this.cliente.getIdCliente());
		new Thread() {

			@Override
			public void run() {
				EnviaEmail email = new EnviaEmail();
				try {
					email.enviaEmailMensagemParaDono(cliente, notificacao);

				} catch (MalformedURLException | EmailException e) {
					e.printStackTrace();
					System.out.println("Erro ao enviar e-mail " + e);
				}

			}
		}.start();

		return "Obrigado.xhtml";
	}

	public Cliente pegaClienteNaSessao() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

		HttpSession sessao = request.getSession();
		Cliente cliente = (Cliente) sessao.getAttribute("clientelogado");

		return cliente;
	}
	
	public void atualizaClienteNaSessao(Cliente cliente){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) 
				context.getExternalContext().getRequest();
		
		HttpSession sessao = request.getSession();
		sessao.removeAttribute("clientelogado");
		sessao.setAttribute("clientelogado", cliente);
	}
	
	
	
	@PostConstruct
	public void preencherCodigo() {

		String valor = "";
		
		if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigo") != null) {
			
			valor = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigo");
			
			if (valor.equalsIgnoreCase("") || valor == null || valor.length() < 1) {
			
			} else {
				this.codigoDigitado = valor;	
			}
		} 
		
	}		
	
	
	
	

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public float getPrecoCodigoGratis() {
		return precoCodigoGratis;
	}

	public void setPrecoCodigoGratis(float precoCodigoGratis) {
		this.precoCodigoGratis = precoCodigoGratis;
	}

	public Integer getQtCodigoParaComprar() {
		return qtCodigoParaComprar;
	}

	public void setQtCodigoParaComprar(Integer qtCodigoParaComprar) {
		this.qtCodigoParaComprar = qtCodigoParaComprar;
	}

	public Cliente getCliente() {
		if(cliente == null){
			cliente = new Cliente();
		}
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getCodigoDigitado() {
		return codigoDigitado;
	}

	public void setCodigoDigitado(String codigoDigitado) {
		this.codigoDigitado = codigoDigitado;
	}

	public Objeto getObjeto() {
		return objeto;
	}

	public void setObjeto(Objeto objeto) {
		this.objeto = objeto;
	}

	public Notificacao getNotificacao() {
		if(notificacao == null){
			notificacao = new Notificacao();
		}
			return notificacao;
	}

	public void setNotificacao(Notificacao notificacao) {
		this.notificacao = notificacao;
	}

	public NotificacaoDAO getNotificacaoDAO() {
		return notificacaoDAO;
	}

	public void setNotificacaoDAO(NotificacaoDAO notificacaoDAO) {
		this.notificacaoDAO = notificacaoDAO;
	}

}
