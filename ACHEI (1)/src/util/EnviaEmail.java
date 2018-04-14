package util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import model.Cliente;
import model.Notificacao;

public class EnviaEmail {
	
	private static String EMAIL_REMETENTE = "contato@codigoachei.online";
	private static String NOME_REMETENTE = "ACHEI";
	private static String SENHA_REMETENTE = "jjp937207";
	private static String SMTP_REMETENTE = "smtp.codigoachei.online";
	
	
	
	public void enviaEmailLocalizacaoObjeto(String link, Cliente cliente) throws EmailException, MalformedURLException{
		
		// Cria o e-mail
		HtmlEmail email = new HtmlEmail();
		//email.setHostName("smtp-mail.outlook.com");
		email.setHostName(SMTP_REMETENTE);
		email.setSmtpPort(587);
		email.addTo(cliente.getEmailCliente(), cliente.getNomeCliente());
		email.setCharset("utf-8");
		email.setFrom(EMAIL_REMETENTE, NOME_REMETENTE);
		email.setSubject("Achei - Seu objeto foi achado.");
		 
		// adiciona uma imagem ao corpo da mensagem e retorna seu id
		URL url = new URL("http://www.codigoachei.online/img/logo.png");
		String cid = email.embed(url, "Logo");
		//String cid = email.embed(new File("img/foto_perfil.jpg"));
		 
		// configura a mensagem para o formato HTML
		email.setHtmlMsg("<html>"
				+ "<body>"
				+ "<style>"
				+ ".rodape a{text-decoration:none; color:#eee;}"
				+ "</style>"
		+ "<div style='width: 600px; background-color: #00f'>"
			+ "<div style='width: 200px; float: left'>"
				+ "<img style='width: 200px; heigh:200px;' src=\"cid:"+cid+"\">"
			+ "</div>"
			+ "<div style='width: 400px; float: left;'>"
				+ "<h4 style='color:#fff; font-size:44px; margin-top: 0px; margin-bottom: 0px; background-color:#555; text-align: center;'> Achei</h4>"
			+ "</div>"
			+ "<div style='width: 400px;  float: left; text-align: center; min-height: 130px;'>"
				+ "<h5 style='margin-top: 15px; font-size: 15px;'>"
					+ "Seu objeto foi achadado, click no link abaixo para ver no mapa onde o mesmo foi localizado.<br/>"
					+ "<br/>"
					+ link
					+ "<br/>"
				+ "</h5>"
			+ "</div>"
			+ "<div class='rodape' style='width: 400px; background-color: #555; float: left;'>"
				+ "<div style='width: 200px; float: left'>"
					+ "<h6 style='color:#eee; margin-bottom: 0px; margin-top: 0px; margin-left: 10px; font-size: 12px; text-decoration: none;'>www.codigoachei.online</h6>"
				+ "</div>"
				+ "<div style='width: 200px; float: left'>"
					+ "<h6 style='color:#eee;  margin-bottom: 0px; margin-top: 0px; float: right; margin-right: 10px; font-size: 12px; text-decoration: none;'>contato@codigochei.online</h6>"
				+ "</div>"
			+ "</div>"
		+ "</div>"
	+ "</body>"
+ "</html>"
	);
		 
		// configure uma mensagem alternativa caso o servidor não suporte HTML
		email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
		email.setStartTLSEnabled(true);
		email.setAuthenticator(new DefaultAuthenticator(EMAIL_REMETENTE, SENHA_REMETENTE));
		// envia o e-mail
		System.out.println("enviando email de localizaçao");
		email.send();
		System.out.println("enviado.");
	}
	
	public void enviaEmailSemLocalizacao(Cliente cliente) throws EmailException, MalformedURLException{
		
		// Cria o e-mail
				HtmlEmail email = new HtmlEmail();
				//email.setHostName("smtp-mail.outlook.com");
				email.setHostName(SMTP_REMETENTE);
				email.setSmtpPort(587);
				email.addTo(cliente.getEmailCliente(), cliente.getNomeCliente());
				email.setCharset("utf-8");
				email.setFrom(EMAIL_REMETENTE, NOME_REMETENTE);
				email.setSubject("Achei - Seu objeto foi achado.");
				 
				// adiciona uma imagem ao corpo da mensagem e retorna seu id
				URL url = new URL("http://www.codigoachei.online/img/logo.png");
				String cid = email.embed(url, "Logo");
				//String cid = email.embed(new File("img/foto_perfil.jpg"));
				 
				// configura a mensagem para o formato HTML
				email.setHtmlMsg("<html>"
						+ "<body>"
						+ "<style>"
						+ "a{text-decoration:none; color:#eee;}"
						+ "</style>"
				+ "<div style='width: 600px; background-color: #00f'>"
					+ "<div style='width: 200px; float: left'>"
						+ "<img style='width: 200px; heigh:200px;' src=\"cid:"+cid+"\">"
					+ "</div>"
					+ "<div style='width: 400px; float: left;'>"
						+ "<h4 style='color:#fff; font-size:44px; margin-top: 0px; margin-bottom: 0px; background-color:#555; text-align: center;'> Achei</h4>"
					+ "</div>"
					+ "<div style='width: 400px;  float: left; text-align: center; min-height: 130px;'>"
						+ "<h5 style='margin-top: 15px; font-size: 15px;'>"
							+ "Seu objeto foi achadado, no entanto a pessoa que achou não pode compartilhar sua localização, o mesmo poderá entrar em contato em um dos seus telefones ou e-mail cadastrado conosco"
							+ "<br/>"
							+ "Ficamos no aguardo."
							+ "<br/>"
						+ "</h5>"
					+ "</div>"
					+ "<div style='width: 400px; background-color: #555; float: left;'>"
						+ "<div style='width: 200px; float: left'>"
							+ "<h6 style='color:#eee; margin-bottom: 0px; margin-top: 0px; margin-left: 10px; font-size: 12px; text-decoration: none;'>www.codigoachei.online</h6>"
						+ "</div>"
						+ "<div style='width: 200px; float: left'>"
							+ "<h6 style='color:#eee;  margin-bottom: 0px; margin-top: 0px; float: right; margin-right: 10px; font-size: 12px; text-decoration: none;'>contato@codigochei.online</h6>"
						+ "</div>"
					+ "</div>"
				+ "</div>"
			+ "</body>"
		+ "</html>"
			);
				 
				// configure uma mensagem alternativa caso o servidor não suporte HTML
				email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
				email.setStartTLSEnabled(true);
				email.setAuthenticator(new DefaultAuthenticator(EMAIL_REMETENTE, SENHA_REMETENTE));
				// envia o e-mail
				System.out.println("enviando email sem localizaçao");
				email.send();
				System.out.println("enviado.");
		
	}
	
	
	public void enviarEmailRecuperacaoSenha(Cliente cliente) throws EmailException, MalformedURLException{
		// Cria o e-mail
		HtmlEmail email = new HtmlEmail();
		//email.setHostName("smtp-mail.outlook.com");
		email.setHostName(SMTP_REMETENTE);
		email.setSmtpPort(587);
		email.addTo(cliente.getEmailCliente(), cliente.getNomeCliente());
		email.setCharset("utf-8");
		email.setFrom(EMAIL_REMETENTE, NOME_REMETENTE);
		email.setSubject("Achei - Recuperação de senha.");
		 
		// adiciona uma imagem ao corpo da mensagem e retorna seu id
		URL url = new URL("http://www.codigoachei.online/img/logo.png");
		String cid = email.embed(url, "Logo");
		//String cid = email.embed(new File("/img/logo.png"));
		 
		// configura a mensagem para o formato HTML
		email.setHtmlMsg("<html>"
							+ "<body>"
							+ "<style>"
							+ "a{text-decoration:none; color:#eee;}"
							+ "</style>"
					+ "<div style='width: 600px; background-color: #00f'>"
						+ "<div style='width: 200px; float: left'>"
							+ "<img style='width: 200px; heigh:200px;' src=\"cid:"+cid+"\">"
						+ "</div>"
						+ "<div style='width: 400px; float: left;'>"
							+ "<h4 style='color:#fff; font-size:44px; margin-top: 0px; margin-bottom: 0px; background-color:#555; text-align: center;'> Achei</h4>"
						+ "</div>"
						+ "<div style='width: 400px;  float: left; text-align: center; min-height: 130px;'>"
							+ "<h5 style='margin-top: 15px; font-size: 15px;'>"
								+ "Você solicitou o reset de senha em nosso site.<br/>"
								+ "Sua nova senha para acesso ao site é:<br/>"
								+ "<br/>"
								+ "<b>"+cliente.getSenhaCliente()+"</b><br/>"
							+ "</h5>"
						+ "</div>"
						+ "<div style='width: 400px; background-color: #555; float: left;'>"
							+ "<div style='width: 200px; float: left'>"
								+ "<h6 style='color:#eee; margin-bottom: 0px; margin-top: 0px; margin-left: 10px; font-size: 12px; text-decoration: none;'>www.codigoachei.online</h6>"
							+ "</div>"
							+ "<div style='width: 200px; float: left'>"
								+ "<h6 style='color:#eee;  margin-bottom: 0px; margin-top: 0px; float: right; margin-right: 10px; font-size: 12px; text-decoration: none;'>contato@codigochei.online</h6>"
							+ "</div>"
						+ "</div>"
					+ "</div>"
				+ "</body>"
			+ "</html>"
				);
		 
		// configure uma mensagem alternativa caso o servidor não suporte HTML
		email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
		email.setStartTLSEnabled(true);
		email.setAuthenticator(new DefaultAuthenticator(EMAIL_REMETENTE, SENHA_REMETENTE));
		// envia o e-mail
		System.out.println("enviando email recuperacao de senha");
		email.send();
		System.out.println("enviado.");
	}
	
	
	public void enviaEmailMensagemParaDono(Cliente cliente, Notificacao notificacao) throws EmailException, MalformedURLException{
			
		// Cria o e-mail
				HtmlEmail email = new HtmlEmail();
				//email.setHostName("smtp-mail.outlook.com");
				email.setHostName(SMTP_REMETENTE);
				email.setSmtpPort(587);
				email.addTo(cliente.getEmailCliente(), cliente.getNomeCliente());
				email.setCharset("utf-8");
				email.setFrom(EMAIL_REMETENTE, NOME_REMETENTE);
				email.setSubject("Achei - Você recebeu uma nova mensagem.");
				 
				// adiciona uma imagem ao corpo da mensagem e retorna seu id
				URL url = new URL("http://www.codigoachei.online/img/logo.png");
				String cid = email.embed(url, "Logo");
				//String cid = email.embed(new File("img/foto_perfil.jpg"));
				 
				// configura a mensagem para o formato HTML
				email.setHtmlMsg("<html>"
						+ "<body>"
						+ "<style>"
						+ "a{text-decoration:none; color:#eee;}"
						+ "</style>"
				+ "<div style='width: 600px; background-color: #00f'>"
					+ "<div style='width: 200px; float: left'>"
						+ "<img style='width: 200px; heigh:200px;' src=\"cid:"+cid+"\">"
					+ "</div>"
					+ "<div style='width: 400px; float: left;'>"
						+ "<h4 style='color:#fff; font-size:44px; margin-top: 0px; margin-bottom: 0px; background-color:#555; text-align: center;'> Achei</h4>"
					+ "</div>"
					+ "<div style='width: 400px;  float: left; text-align: center; min-height: 130px;'>"
						+ "<h5 style='margin-top: 15px; font-size: 15px;'>"
							+ "A pessoa que achou seu objeto lhe enviou a mensagem abaixo.<br/>"
							+ "Nome da pessoa: "+notificacao.getNomeMensagem()+"<br/>"
							+ "Assunto: "+notificacao.getAssuntoMensagem()+"<br/>"
							+ "Mensagem: "+notificacao.getMensagem()+"</b><br/>"
						+ "</h5>"
					+ "</div>"
					+ "<div style='width: 400px; background-color: #555; float: left;'>"
						+ "<div style='width: 200px; float: left'>"
							+ "<h6 style='color:#eee; margin-bottom: 0px; margin-top: 0px; margin-left: 10px; font-size: 12px; text-decoration: none;'>www.codigoachei.online</h6>"
						+ "</div>"
						+ "<div style='width: 200px; float: left'>"
							+ "<h6 style='color:#eee;  margin-bottom: 0px; margin-top: 0px; float: right; margin-right: 10px; font-size: 12px; text-decoration: none;'>contato@codigochei.online</h6>"
						+ "</div>"
					+ "</div>"
				+ "</div>"
			+ "</body>"
		+ "</html>"
			);
				 
				// configure uma mensagem alternativa caso o servidor não suporte HTML
				email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
				email.setStartTLSEnabled(true);
				email.setAuthenticator(new DefaultAuthenticator(EMAIL_REMETENTE, SENHA_REMETENTE));
				// envia o e-mail
				System.out.println("enviando email de mensagem");
				email.send();
				System.out.println("enviado.");
	}
	
	
public void enviaEmailBemvindo(Cliente cliente) throws EmailException, MalformedURLException{
		
		// Cria o e-mail
		HtmlEmail email = new HtmlEmail();
		//email.setHostName("smtp-mail.outlook.com");
		email.setHostName(SMTP_REMETENTE);
		email.setSmtpPort(587);
		email.addTo(cliente.getEmailCliente(), cliente.getNomeCliente());
		email.setCharset("utf-8");
		email.setFrom(EMAIL_REMETENTE, NOME_REMETENTE);
		email.setSubject("Achei - Seja Bem Vindo.");
		 
		// adiciona uma imagem ao corpo da mensagem e retorna seu id
		URL url = new URL("http://www.codigoachei.online/img/logo.png");
		String cid = email.embed(url, "Logo");
		//String cid = email.embed(new File("img/foto_perfil.jpg"));
		 
		// configura a mensagem para o formato HTML
		email.setHtmlMsg("<html>"
				+ "<body>"
				+ "<style>"
				+ ".rodape a{text-decoration:none; color:#eee;}"
				+ "</style>"
		+ "<div style='width: 600px; background-color: #00f'>"
			+ "<div style='width: 200px; float: left'>"
				+ "<img style='width: 200px; heigh:200px;' src=\"cid:"+cid+"\">"
			+ "</div>"
			+ "<div style='width: 400px; float: left;'>"
				+ "<h4 style='color:#fff; font-size:44px; margin-top: 0px; margin-bottom: 0px; background-color:#555; text-align: center;'> Achei</h4>"
			+ "</div>"
			+ "<div style='width: 400px;  float: left; text-align: center; min-height: 130px;'>"
				+ "<h5 style='margin-top: 15px; font-size: 13px;'>"
					+ "Bem vindo ao Achei<br/>"
					+ "Seus objeto estarão mais seguros agora, lembre-se de manter seus dados atualizados para que a devolução de seu objeto "
					+ "seja feita da forma mais fácil possível.<br/>"
					+ "<br/>"
					+ "<a href='https://www.codigoachei.online/security/EditaPerfil.xhtml'>Clique aqui para editar seu perfil.</a>"
					+ "<br/>"
					+ "<br/>"
				+ "</h5>"
			+ "</div>"
			+ "<div class='rodape' style='width: 400px; background-color: #555; float: left;'>"
				+ "<div style='width: 200px; float: left'>"
					+ "<h6 style='color:#eee; margin-bottom: 0px; margin-top: 0px; margin-left: 10px; font-size: 12px; text-decoration: none;'>www.codigoachei.online</h6>"
				+ "</div>"
				+ "<div style='width: 200px; float: left'>"
					+ "<h6 style='color:#eee;  margin-bottom: 0px; margin-top: 0px; float: right; margin-right: 10px; font-size: 12px; text-decoration: none;'>contato@codigochei.online</h6>"
				+ "</div>"
			+ "</div>"
		+ "</div>"
	+ "</body>"
+ "</html>"
	);
		 
		// configure uma mensagem alternativa caso o servidor não suporte HTML
		email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
		email.setStartTLSEnabled(true);
		email.setAuthenticator(new DefaultAuthenticator(EMAIL_REMETENTE, SENHA_REMETENTE));
		// envia o e-mail
		System.out.println("enviando email de boas vindas");
		email.send();
		System.out.println("enviado.");
	}
	
	
public void enviaEmailParaNos(String nome, String assunto, String contato, String msg) throws EmailException, MalformedURLException{
	
	// Cria o e-mail
	HtmlEmail email = new HtmlEmail();
	//email.setHostName("smtp-mail.outlook.com");
	email.setHostName(SMTP_REMETENTE);
	email.setSmtpPort(587);
	email.addTo(EMAIL_REMETENTE, nome);
	email.setCharset("utf-8");
	email.setFrom(EMAIL_REMETENTE, nome);
	email.setSubject("Email enviado pelo Site");
	 
	// adiciona uma imagem ao corpo da mensagem e retorna seu id
	URL url = new URL("http://www.codigoachei.online/img/logo.png");
	String cid = email.embed(url, "Logo");
	//String cid = email.embed(new File("img/foto_perfil.jpg"));
	 
	// configura a mensagem para o formato HTML
	email.setHtmlMsg("<html>"
			+ "<body>"
			+ "<style>"
			+ ".rodape a{text-decoration:none; color:#eee;}"
			+ "</style>"
	+ "<div style='width: 600px; background-color: #00f'>"
		+ "<div style='width: 200px; float: left'>"
			+ "<img style='width: 200px; heigh:200px;' src=\"cid:"+cid+"\">"
		+ "</div>"
		+ "<div style='width: 400px; float: left;'>"
			+ "<h4 style='color:#fff; font-size:44px; margin-top: 0px; margin-bottom: 0px; background-color:#555; text-align: center;'> Achei</h4>"
		+ "</div>"
		+ "<div style='width: 400px;  float: left; text-align: center; min-height: 130px;'>"
			+ "<h5 style='margin-top: 15px; font-size: 13px;'>"
				+ "Nome de usuário: "+nome+"<br/>"
				+ "Contato: "+contato+"<br/>"
				+ "Assunto: "+assunto+"<br/>"
				+ "Mensagem: "+msg+"<br/>"
			+ "</h5>"
		+ "</div>"
		+ "<div class='rodape' style='width: 400px; background-color: #555; float: left;'>"
			+ "<div style='width: 200px; float: left'>"
				+ "<h6 style='color:#eee; margin-bottom: 0px; margin-top: 0px; margin-left: 10px; font-size: 12px; text-decoration: none;'>www.codigoachei.online</h6>"
			+ "</div>"
			+ "<div style='width: 200px; float: left'>"
				+ "<h6 style='color:#eee;  margin-bottom: 0px; margin-top: 0px; float: right; margin-right: 10px; font-size: 12px; text-decoration: none;'>contato@codigochei.online</h6>"
			+ "</div>"
		+ "</div>"
	+ "</div>"
+ "</body>"
+ "</html>"
);
	 
	// configure uma mensagem alternativa caso o servidor não suporte HTML
	email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
	email.setStartTLSEnabled(true);
	email.setAuthenticator(new DefaultAuthenticator(EMAIL_REMETENTE, SENHA_REMETENTE));
	// envia o e-mail
	System.out.println("enviando email para nós");
	email.send();
	System.out.println("enviado.");
}

}
