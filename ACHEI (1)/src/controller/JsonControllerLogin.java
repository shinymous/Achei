package controller;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import com.google.gson.Gson;
import dao.ClienteDAO;
import model.Cliente;
import util.Criptografia;

@ManagedBean
public class JsonControllerLogin implements Serializable {

	private static final long serialVersionUID = 1L;
	private String resultado = "";
	Cliente cliente = new Cliente();
	Gson gson = new Gson();
	private String email = "";
	private String senha = "";
	private String texto = "";
	
	@PostConstruct
	public void localizar() {
		ClienteDAO clienteDAO = new ClienteDAO();


		if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("email") != null && FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("senha") != null) {

			
			System.out.println(email +" "+ senha);
			email = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("email");
			senha = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("senha");
			
			if (email.equalsIgnoreCase("") || email == null || email.length() < 2 || senha.equalsIgnoreCase("") || senha == null || senha.length() < 2) {
				this.setResultado("Invalido");
			} else {
					try {
						cliente = clienteDAO.verificaLoginCriptografia(email, senha);
						 this.resultado = gson.toJson(cliente);
			}catch (Exception e) {
				this.setResultado("Erro boca mole");
				System.out.println("Erro do boca mole "+e.getMessage());
			}
			}
		

		} else {
			this.setResultado("Erro");
		}

	}
			

	public String getResultado() {
		return resultado;
	}


	public void setResultado(String resultado) {
		this.resultado = resultado;
	}



	public Gson getGson() {
		return gson;
	}



	public void setGson(Gson gson) {
		this.gson = gson;
	}

	

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getTexto() {
		return texto;
	}


	public void setTexto(String texto) {
		this.texto = texto;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getSenha() {
		return senha;
	}


	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	

}
