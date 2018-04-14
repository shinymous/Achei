package test;

import java.net.MalformedURLException;

import org.apache.commons.mail.EmailException;

import model.Cliente;
import model.Codigo;
import util.Criptografia;
import util.EnviaEmail;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//teste de email
//		EnviaEmail enviaEmail = new EnviaEmail();
//		
//		try {
//			Cliente c =  new Cliente();
//			c.setNomeCliente("");
//			c.setEmailCliente("");
//			enviaEmail.enviaEmailBemvindo(c);
//		} catch (EmailException | MalformedURLException e) {
//			// TODO Auto-generated catch block
//			System.out.println(e);
//		}
		
		//teste de codigo
		//Codigo c = new Codigo();
		//System.out.println(c.gerarCodigo());
		System.out.println(Criptografia.criptografar("jjp937207"));
	}

}
