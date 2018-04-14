package controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import com.google.gson.Gson;
import dao.CodigoDAO;
import model.Cliente;

@ManagedBean
public class JsonControllerCodigo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String texto = "oi";
	Cliente cliente = new Cliente();
	Gson gson = new Gson();

	@PostConstruct
	public void localizar() {
		CodigoDAO codigoDAO = new CodigoDAO();

		int idCodigo = 0;
		String codigo = "";
		String idString = "";
		String valor = "";

		if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigojson") != null) {

			valor = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigojson");

			if (valor.equalsIgnoreCase("") || valor == null || valor.length() < 2) {
				this.setTexto("Código inválido");
			} else {

				// separa o ID do código
				for (int i = 1; i < valor.length(); i++) {

					try {
						Integer.parseInt(valor.substring(0, i));
						idString = valor.substring(0, i);
					} catch (Exception e) {
						codigo = valor.substring(i - 1, valor.length());
						break;
					}
				}

				if (idString.length() < 2) {
					this.setTexto("Código inválido");
				} else {
					// seta o id do codigo
					idCodigo = Integer.parseInt(idString);

					// localiza cliente com base no codigo
					this.cliente = codigoDAO.localizarDTO(codigo, idCodigo);
					this.texto = gson.toJson(cliente);
				}

			}

		} else {
			this.setTexto("Código inválido");
		}

	}		
		

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
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

}
