package controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.mail.EmailException;
import org.primefaces.context.RequestContext;

import dao.ClienteDAO;
import dao.ContatoDAO;
import dao.EnderecoDAO;
import dao.NotificacaoDAO;
import model.Cliente;
import model.Contato;
import model.Endereco;
import model.Notificacao;
import util.EnviaEmail;
import util.Upload;

@ManagedBean
public class ControladorCliente implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Cliente cliente = new Cliente();
	private Endereco endereco = new Endereco();
	private Contato contato = new Contato();
	private Notificacao notificacao = new Notificacao();
	private String novoEmail;
	private String confirmaNovoEmail;
	private String emialAtual;
	private String senhaAtual;
	private String novaSenha;
	private String confirmaNovaSenha;
	//
	private String nomeRemetente;
	private String contatoRemetente;
	private String assuntoMsg;
	private String msg;
	//
	private Part arquivo;
	private static final int MAX_SIZE = 2 * 1024 * 1024;
	
	
	public String cadastrarCliente(){
		
		ClienteDAO clienteDAO = new ClienteDAO();
		
		if(clienteDAO.comparaEmail(this.cliente.getEmailCliente())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-mail já cadastrado.",""));
		}else{
			int retorno = clienteDAO.incluirCliente(this.cliente);
			
			if(retorno > 0){
				
				//destroi a sessao anterior caso tenha alguem logado.
				FacesContext context = FacesContext.getCurrentInstance();
				HttpServletRequest request = (HttpServletRequest) 
						context.getExternalContext().getRequest();
				
				HttpSession sessao = request.getSession();
				
				sessao.invalidate();
				
				//Cria uma nova sessao com o novo cliente.
				this.cliente.setIdCliente(retorno);
				this.cliente.setCaminhoImg("foto_perfil.jpg");
				
				sessao = request.getSession();
				sessao.setAttribute("clientelogado", this.cliente);
				
				//ligo o id do cliente com a tabaela endereco
				EnderecoDAO edao = new EnderecoDAO();
				this.endereco.setIdCliente(retorno);
				edao.incluirOuAlterarEndereco(endereco);
				
				//ligo o id cliente com a tabela contato
				ContatoDAO cdao = new ContatoDAO();
				this.contato.setContato_id_cliente(retorno);
				cdao.incluirOuAlterarContato(contato);
			
				clienteDAO = null;
				
				
				new Thread() {
					
					 @Override
					 public void run() {
						 EnviaEmail email = new EnviaEmail();
					 try {
						 email.enviaEmailBemvindo(cliente);
					 } catch (MalformedURLException | EmailException e) {
						 e.printStackTrace();
					 	System.out.println("Erro ao enviar e-mail "+e);
					 }
					
					 }
			 }.start();
			
				
				return "security/EditaPerfil.xhtml";
				
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar cliente.", ""));

			}
		}
		 
		return "inicio.xhtml";
	}
	
	public String editaPerfil(){
		
		this.cliente = pegaClienteNaSessao();
		ClienteDAO clienteDAO = new ClienteDAO();
		this.cliente = clienteDAO.obterCliente(this.cliente.getIdCliente());
		this.endereco = this.cliente.getEndereco();
		this.contato = this.cliente.getContato();
		
		return "EditaPerfil.xhtml";
	}
	
	
	public void alteraCliente(){
	
		ClienteDAO cDAO = new ClienteDAO();
		if(cDAO.atualizarCliente(this.cliente)){
			
			Cliente clienteAtualizado = pegaClienteNaSessao();
			clienteAtualizado.setNomeCliente(this.cliente.getNomeCliente());
			clienteAtualizado.setSobrenomeCliente(this.cliente.getSobrenomeCliente());
			clienteAtualizado.setDataNascimenteCliente(this.cliente.getDataNascimenteCliente());
			
			atualizaClienteNaSessao(clienteAtualizado);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dados atualizados.", ""));	
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar dados.", ""));
		}
		
		editaPerfil();
	}

	
	public void alterarEndereco(){
		
		EnderecoDAO enderecoDAO = new EnderecoDAO();
		if(enderecoDAO.incluirOuAlterarEndereco(this.endereco) > 0){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Endereço atualizados.", ""));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao atualizar dados.", ""));
		}
		
		editaPerfil();
	}
	
	public void alterarContato(){
		
		ContatoDAO contatoDAO = new ContatoDAO();
		if(contatoDAO.incluirOuAlterarContato(this.contato) > 0){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contatos atualizados.", ""));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao atualizar dados.", ""));
		}

		editaPerfil();
	}
	
	public void alteraEmail(){
		this.cliente = pegaClienteNaSessao();
		
		if(this.cliente.getEmailCliente().equals(this.emialAtual)){
			if(this.novoEmail.equals(this.confirmaNovoEmail) && !this.novoEmail.equals("") && !this.confirmaNovoEmail.equals("")){
				if(this.novoEmail.length() >=8 && this.confirmaNovoEmail.length() >=8){
					
					this.cliente.setEmailCliente(this.getNovoEmail());
					ClienteDAO clienteDAO = new ClienteDAO();
					
					if(clienteDAO.atualizarEmail(this.cliente)){
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "E-mail atualizado com sucesso.", ""));
						this.setNovoEmail(null);
						this.setConfirmaNovoEmail(null);
						
						atualizaClienteNaSessao(this.cliente);
						
						editaPerfil();
					}else{
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar e-mail", ""));
						editaPerfil();
					}	
					
				}else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite um e-mail válido", ""));
					editaPerfil();
				}
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Os campos novo email e confirme novo e-mail não conferem. Por favor tente novamente.", ""));
				editaPerfil();
			}
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-mail atual inválido. Por favor tente novamente.", ""));
			editaPerfil();
		}
	}
	
	public void alteraSenha(){
		this.cliente = pegaClienteNaSessao();
		
		if(this.cliente.getSenhaCliente().equals(this.senhaAtual)){
			if(this.novaSenha.equals(this.confirmaNovaSenha) && !this.novaSenha.equals("") && !this.confirmaNovaSenha.equals("")){
				if(this.novaSenha.length() >=8 && this.confirmaNovaSenha.length()>=8){
					
					this.cliente.setSenhaCliente(this.getNovaSenha());
					ClienteDAO clienteDAO = new ClienteDAO();
					
					if(clienteDAO.atualizarSenha(this.cliente)){
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Senha atualizada com sucesso.", ""));
						this.setNovaSenha(null);
						this.setConfirmaNovaSenha(null);
						
						atualizaClienteNaSessao(this.cliente);
						
						editaPerfil();
					}else{
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar e-mail", ""));
						editaPerfil();
					}	
					
				}else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Digite uma senha com no mínimo 8 caracteres", ""));
					editaPerfil();
				}
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Os campos nova senha e confirme nova senha não conferem. Por favor tente novamente.", ""));
				editaPerfil();
			}
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha atual inválida. Por favor tente novamente.", ""));
			editaPerfil();
		}
	}
	
	public String recuperarSenha(){
		ClienteDAO clienteDAO = new ClienteDAO();
		this.cliente = clienteDAO.recuperarSenha(this.emialAtual);
		if(this.cliente != null){
			String novaSenha = gerarNovaSenha();
			this.cliente.setSenhaCliente(novaSenha);
			clienteDAO.atualizarSenha(this.cliente);
			 new Thread() {
					
				 @Override
				 public void run() {
				 	EnviaEmail email = new EnviaEmail();
				 try {
					 email.enviarEmailRecuperacaoSenha(cliente);
				 } catch (MalformedURLException | EmailException e) {
					 e.printStackTrace();
					 System.out.println("Erro ao enviar e-mail "+e);
				 }
				
				 }
			}.start();
			return"SenhaRecuperada.xhtml";	 
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-mail inválido.", ""));
			return"inicio.xhtml";
		}
		
	}
	
	public String gerarNovaSenha(){
		boolean gerarSenha = true;
		String senha="";
		
		while (gerarSenha){
			String[] carct ={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

		    for (int x=0; x<8; x++){
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
	
	
	public Cliente pegaClienteNaSessao(){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) 
				context.getExternalContext().getRequest();
		
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
	
	
	public String listarNotificacao(){
		NotificacaoDAO notificacaoDAO = new NotificacaoDAO();
		this.cliente = pegaClienteNaSessao();
		ArrayList<Notificacao> notificacoes = new ArrayList<Notificacao>();
		notificacoes = notificacaoDAO.listarNotificacoes(this.cliente.getIdCliente());
		Collections.reverse(notificacoes);
		this.cliente.setListaNotificacao(notificacoes);
		
		atualizaClienteNaSessao(this.cliente);
		
		return "VerNotificacao.xhtml";
	}
	
	public String verNotificacao(){
		NotificacaoDAO notificacaoDAO = new NotificacaoDAO();
		Notificacao notificao = this.getNotificacao();
		if(notificacao.getSituacao() == 1){
			return "Notificacao.xhtml";
		}
		notificao.setSituacao(1);
		notificacaoDAO.incluirOuAlterarNotificacao(notificao);
		return "Notificacao.xhtml";
	}
	
	public String deletaNotificacao(){
		
		NotificacaoDAO notificacaoDAO = new NotificacaoDAO();
		Notificacao notificao = this.getNotificacao();
		if(notificacaoDAO.deletaNotificacao(notificao.getIdNotificacao()) == true){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Notificação Deletada.", ""));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao deletar notificação.", ""));
		}
		return listarNotificacao();
	}
	
	public String voltar(){
		NotificacaoDAO nDAO = new NotificacaoDAO();
		this.cliente = pegaClienteNaSessao();
		this.cliente.setListaNotificacao(nDAO.listarNotificacoes(cliente.getIdCliente()));
		atualizaClienteNaSessao(this.cliente);
		return "Perfil.xhtml";
	}
	
	public String enviaEmailParaNos(){
		
		 new Thread() {
				
			 @Override
			 public void run() {
			 	EnviaEmail email = new EnviaEmail();
			 try {
				 email.enviaEmailParaNos(nomeRemetente, assuntoMsg, contatoRemetente, msg);
			 } catch (MalformedURLException | EmailException e) {
				 e.printStackTrace();
				 System.out.println("Erro ao enviar e-mail "+e);
			 }
			
			 }
		}.start();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem enviada. Obrigado.", ""));
		return"inicio.xhtml";
	}
	
	
	// pega o none do aruiqvo.
		 public String extractFileName(Part part) {
		        String contentDisp = part.getHeader("content-disposition");
		        String[] items = contentDisp.split(";");
		        for (String s : items) {
		            if (s.trim().startsWith("filename")) {
		                return s.substring(s.indexOf("=") + 2, s.length()-1);
		            }
		        }
		        return "";
		    }
		 
		 
		 public String uploadArquivo(){
			 
			 //verifica se o arquivo está nulo
			 if(arquivo == null || arquivo.getSize() <=0 || arquivo.getContentType().isEmpty()){
				 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione um arquivo válido.", ""));	
					return editaPerfil();
			
				}
				
				//verifica o tamanho do arquivo
				if (arquivo.getSize() > MAX_SIZE) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O arquivo deve ter no máximo 2mb.", ""));	
					return editaPerfil();
					
				}
				//verifica o tipo do arquivo
				 String fileName = extractFileName(arquivo);
				if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipo de arquivo inválido", ""));	
					return editaPerfil();
				}
			 
			 try {
			
			 Upload upload = Upload.getInstance();
			 upload.write(arquivo);
			 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Arquivo enviado.", ""));	
			 return editaPerfil();
			 } catch (IOException e) {
			 e.printStackTrace();
			 }
			 return editaPerfil();
			 }

	
	
	//-----Gets e Sets--------
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Cliente getCliente() {
		return cliente;

	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

	public Notificacao getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(Notificacao notificacao) {
		this.notificacao = notificacao;
	}

	public String getNovoEmail() {
		return novoEmail;
	}

	public void setNovoEmail(String novoEmail) {
		this.novoEmail = novoEmail;
	}

	public String getConfirmaNovoEmail() {
		return confirmaNovoEmail;
	}

	public void setConfirmaNovoEmail(String confirmaNovoEmail) {
		this.confirmaNovoEmail = confirmaNovoEmail;
	}

	public String getEmialAtual() {
		return emialAtual;
	}

	public void setEmialAtual(String emialAtual) {
		this.emialAtual = emialAtual;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmaNovaSenha() {
		return confirmaNovaSenha;
	}

	public void setConfirmaNovaSenha(String confirmaNovaSenha) {
		this.confirmaNovaSenha = confirmaNovaSenha;
	}

	public String getNomeRemetente() {
		return nomeRemetente;
	}

	public void setNomeRemetente(String nomeRemetente) {
		this.nomeRemetente = nomeRemetente;
	}

	public String getContatoRemetente() {
		return contatoRemetente;
	}

	public void setContatoRemetente(String contatoRemetente) {
		this.contatoRemetente = contatoRemetente;
	}

	public String getAssuntoMsg() {
		return assuntoMsg;
	}

	public void setAssuntoMsg(String assuntoMsg) {
		this.assuntoMsg = assuntoMsg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Part getArquivo() {
		return arquivo;
	}

	public void setArquivo(Part arquivo) {
		this.arquivo = arquivo;
	}

	public static int getMaxSize() {
		return MAX_SIZE;
	}
	
	
	
}
