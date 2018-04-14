package controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import dao.ClienteDAO;
import dao.NotificacaoDAO;
import model.Cliente;
import model.Notificacao;

@ManagedBean
@SessionScoped
public class LoginController implements Serializable {

	private static final long serialVersionUID = 1652049856110915048L;
	private Cliente cliente;
	private String loginInformado;
	private String senhaInformada;
	private int quantidadeNotificacoes;

	public LoginController() {
		super();
	}

	public String voltar() {
		return "/index.xhtml";
	}

	public String autenticar() {

		String paginaDestino;

		// Chamar o DAO para verificar se o login e a senha est�o corretos
		ClienteDAO dao = new ClienteDAO();
		Cliente clienteFazendoLogin = dao.verificaLogin(loginInformado, senhaInformada);

		// Obtem a Sessao
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

		HttpSession sessao = request.getSession();
		// Caso sim:
		if (clienteFazendoLogin != null) {
			// Adicionar a pessoa na Session
			NotificacaoDAO nDAO = new NotificacaoDAO();
			clienteFazendoLogin.setListaNotificacao(nDAO.listarNotificacoes(clienteFazendoLogin.getIdCliente()));
			sessao.setAttribute("clientelogado", clienteFazendoLogin);

			// encaminhar para a p�gina de sucesso
			paginaDestino = "security/Perfil.xhtml";
		} else {
			sessao.invalidate();

			// Caso contr�rio: encaminhar para a p�gina de erro
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nome de usuário ou senha inválidos.", ""));
			paginaDestino = "Entrar.xhtml";
		}

		return paginaDestino;
	}

	public Cliente getCliente() {

		if (this.cliente == null) {
			cliente = new Cliente();
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession sessao = request.getSession();
			cliente = (Cliente) sessao.getAttribute("clientelogado");
		}else{
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession sessao = request.getSession();
			cliente = (Cliente) sessao.getAttribute("clientelogado");
		}
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String sair() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

		HttpSession sessao = request.getSession();

		sessao.invalidate();

		return "/inicio.xhtml";
	}

	public String getLoginInformado() {
		return loginInformado;
	}

	public void setLoginInformado(String loginInformado) {
		this.loginInformado = loginInformado;
	}

	public String getSenhaInformada() {
		return senhaInformada;
	}

	public void setSenhaInformada(String senhaInformada) {
		this.senhaInformada = senhaInformada;
	}

	public int getQuantidadeNotificacoes() {
		Cliente cliente = this.getCliente();
		this.setQuantidadeNotificacoes(0);
		if(cliente.getListaNotificacao() != null){
			for (int i = 0; i < cliente.getListaNotificacao().size(); i++) {
				if(cliente.getListaNotificacao().get(i).getSituacao() == 0){
					this.quantidadeNotificacoes +=1;
				}
			}
		}
		
		return quantidadeNotificacoes;
	}

	public void setQuantidadeNotificacoes(int quantidadeNotificacoes) {
		this.quantidadeNotificacoes = quantidadeNotificacoes;
	}

}
