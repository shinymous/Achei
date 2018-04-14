package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import util.ConexaoBanco;
import util.Criptografia;
import model.Cliente;
import model.Contato;
import model.Endereco;


public class ClienteDAO {

	private Connection connection;

	public int incluirCliente(Cliente cliente) {
		
		int idInserido = 0;
		String sql = "INSERT INTO cliente (email_cliente, senha_cliente, caminho_img) VALUES (?, ?, ?)";
		
		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// seta os valores
			
			stmt.setString(1, cliente.getEmailCliente());
			stmt.setString(2, cliente.getSenhaCliente());
			stmt.setString(3, "foto_perfil.jpg");
		
			
			// executa
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				idInserido = rs.getInt(1);
			}

			if (idInserido > 0) {
				System.out.println("Cliente inserida com sucesso");
			} else {
				System.out.println("Erro ao inserir Cliente");
			}

			stmt.close();

			return idInserido;
			
		} catch (SQLException e) {
			System.out.println("Erro ao inserir Cliente " +e);
			throw new RuntimeException(e);
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}

	public boolean atualizarCliente(Cliente cliente) {

		java.sql.Date dataClienteSQL = null;
		boolean atualizadoSucesso = false;
		String sql = "UPDATE cliente SET nome_cliente = ?, sobrenome_cliente = ?, data_cliente = ?, qt_codigo_gratis = ? WHERE id_cliente = ?";
		
		if(cliente.getDataNascimenteCliente() != null){
			dataClienteSQL = new java.sql.Date(cliente.getDataNascimenteCliente().getTime());
			dataClienteSQL.setDate(dataClienteSQL.getDate()+1);
		}
		
		
		//Estou adicionando mais um dia na data de nascimento do cliente.
		//por quê por agum motivo a data inserida vai comuma dia a menos e eu não sei omotivo.
		//tentar arrumar isso futuramente. 
		//SQN.
		
		//
		
		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);

			// seta os valores

			stmt.setString(1, cliente.getNomeCliente());
			stmt.setString(2, cliente.getSobrenomeCliente());
			stmt.setDate(3, dataClienteSQL);
			stmt.setInt(4, cliente.getQtCodigoGratis());
			stmt.setInt(5, cliente.getIdCliente());
			
			// executa
			int ok = stmt.executeUpdate();

			if (ok == 1) {
				System.out.println("Cliente atualizado com sucesso no BD!");
				atualizadoSucesso = true;
			} else {
				System.out.println("Erro ao atualizar liente no BD! \n"+ stmt);
			}

			stmt.close();

			return atualizadoSucesso;
		} catch (SQLException e) {
			System.out.println("Erro ao atualizar Pessoa no BD! "+e);
			throw new RuntimeException(e);
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}

	
	public Cliente obterCliente(int idCliente) {
		Cliente cliente = null;
		Endereco endereco = null;
		Contato contato = null;
		
		String sql = "SELECT * FROM cliente as c INNER JOIN endereco as e ON c.id_cliente = e.endereco_id_cliente INNER JOIN contato as con ON c.id_cliente = con.contato_id_cliente WHERE id_cliente = ?";

		System.out.println("Obtendo Cliente");
		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setInt(1, idCliente);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				cliente = new Cliente();
				cliente.setIdCliente(rs.getInt("id_cliente"));
				cliente.setNomeCliente(rs.getString("nome_cliente"));
				cliente.setSobrenomeCliente(rs.getString("sobrenome_cliente"));
				cliente.setEmailCliente(rs.getString("email_cliente"));
				cliente.setSenhaCliente(rs.getString("senha_cliente"));
				cliente.setDataNascimenteCliente(rs.getDate("data_cliente"));
				cliente.setQtCodigoGratis(rs.getInt("qt_codigo_gratis"));
				cliente.setCaminhoImg(rs.getString("caminho_img"));
				
				endereco = new Endereco();
				endereco.setIdEndereco(rs.getInt("id_endereco"));
				endereco.setRuaEndereco(rs.getString("rua_endereco"));
				endereco.setNumeroEndereco(rs.getInt("numero_endereco"));
				endereco.setComplementoEndereco(rs.getString("complemento_endereco"));
				endereco.setBairroEndereoc(rs.getString("bairro_endereco"));
				endereco.setCidadeEndereco(rs.getString("cidade_endereco"));
				endereco.setEstadoEndereco(rs.getString("estado_endereco"));
				endereco.setCepEndereco(rs.getString("cep_endereco"));
				
				contato = new Contato();
				contato.setIdContato(rs.getInt("id_contato"));
				contato.setTelefone1(rs.getString("telefone1"));
				contato.setTelefone2(rs.getString("telefone2"));
				contato.setTelefone3(rs.getString("telefone3"));
				contato.setTelefone4(rs.getString("telefone4"));
				
				cliente.setEndereco(endereco);
				cliente.setContato(contato);
			}

			
			return cliente;

		} catch (SQLException e) {
			System.out.println("Erro ao buscar Cliente no BD!");
			e.printStackTrace();
			return null;
			
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}

	
	
	public boolean comparaEmail(String email) {

		String sql = "SELECT * FROM cliente WHERE email_cliente = ?";
		
		System.out.println("Comparando email");

		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, email);
			// executa
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			stmt.close();

		} catch (SQLException e) {
			System.out.println("Erro ao comparar email no BD!");
			e.printStackTrace();
		} finally {
			 ConexaoBanco.fecharConexao(this.connection);
		}

		return false;
	}



	
	
public Cliente verificaLogin(String email, String senha) {
			
		Cliente cliente = null;
		String sql = "SELECT * FROM cliente WHERE email_cliente = ?";

		System.out.println("Verificando Login");
		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, email);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				cliente = new Cliente();
				cliente.setIdCliente(rs.getInt("id_cliente"));
				cliente.setNomeCliente(rs.getString("nome_cliente"));
				cliente.setSobrenomeCliente(rs.getString("sobrenome_cliente"));
				cliente.setEmailCliente(rs.getString("email_cliente"));
				cliente.setDataNascimenteCliente(rs.getDate("data_cliente"));
				cliente.setSenhaCliente(rs.getString("senha_cliente"));
				cliente.setQtCodigoGratis(rs.getInt("qt_codigo_gratis"));
				cliente.setCaminhoImg(rs.getString("caminho_img"));
			}
			stmt.close();
			
			
			if(cliente != null){
				if(cliente.getSenhaCliente().equals(senha)){
					
					return cliente;
				}else{
					return null;
				}
			}
			

		} catch (SQLException e) {
			System.out.println("Erro ao verificar Login no BD!");
			e.printStackTrace();
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}

		return cliente;
	}



public Cliente verificaLoginCriptografia(String email, String senha) {
	
	Cliente cliente = null;
	String sql = "SELECT * FROM cliente WHERE email_cliente = ?";

	System.out.println("Verificando Login");
	try {
		this.connection = new ConexaoBanco().getConnection();
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, email);
		// executa
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			cliente = new Cliente();
			cliente.setIdCliente(rs.getInt("id_cliente"));
			cliente.setNomeCliente(rs.getString("nome_cliente"));
			cliente.setSobrenomeCliente(rs.getString("sobrenome_cliente"));
			cliente.setEmailCliente(rs.getString("email_cliente"));
			cliente.setDataNascimenteCliente(rs.getDate("data_cliente"));
			cliente.setSenhaCliente(rs.getString("senha_cliente"));
			cliente.setQtCodigoGratis(rs.getInt("qt_codigo_gratis"));
			cliente.setCaminhoImg(rs.getString("caminho_img"));
		}
		stmt.close();
		
		
		if(cliente != null){
			if(Criptografia.criptografar(cliente.getSenhaCliente()).equals(senha)){
				
				return cliente;
			}else{
				return null;
			}
		}
		

	} catch (SQLException e) {
		System.out.println("Erro ao verificar Login no BD!");
		e.printStackTrace();
	} finally {
		ConexaoBanco.fecharConexao(this.connection);
	}

	return cliente;
}

public Cliente recuperarSenha(String email) {
	
	Cliente cliente = null;
	String sql = "SELECT * FROM cliente WHERE email_cliente = ?";

	System.out.println("Verificando Login");
	try {
		this.connection = new ConexaoBanco().getConnection();
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, email);
		// executa
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			cliente = new Cliente();
			cliente.setIdCliente(rs.getInt("id_cliente"));
			cliente.setNomeCliente(rs.getString("nome_cliente"));
			cliente.setSobrenomeCliente(rs.getString("sobrenome_cliente"));
			cliente.setEmailCliente(rs.getString("email_cliente"));
			cliente.setDataNascimenteCliente(rs.getDate("data_cliente"));
			cliente.setSenhaCliente(rs.getString("senha_cliente"));
			cliente.setQtCodigoGratis(rs.getInt("qt_codigo_gratis"));
			cliente.setCaminhoImg(rs.getString("caminho_img"));
		}
		stmt.close();
		
		
		if(cliente != null){				
			return cliente;
		}
		

	} catch (SQLException e) {
		System.out.println("Erro ao verificar Login no BD!");
		e.printStackTrace();
	} finally {
		ConexaoBanco.fecharConexao(this.connection);
	}

	return cliente;
}
	
	
public boolean atualizarEmail(Cliente cliente) {

	boolean atualizadoSucesso = false;
	String sql = "UPDATE cliente SET email_cliente = ? WHERE id_cliente = ?";
	
	try {
		this.connection = new ConexaoBanco().getConnection();
		PreparedStatement stmt = connection.prepareStatement(sql);

		// seta os valores

		stmt.setString(1, cliente.getEmailCliente());
		stmt.setInt(2, cliente.getIdCliente());
		
		// executa
		int ok = stmt.executeUpdate();

		if (ok == 1) {
			System.out.println("Email atualizado com sucesso no BD!");
			atualizadoSucesso = true;
		} else {
			System.out.println("Erro ao atualizar email no BD! \n"+ stmt);
		}

		stmt.close();

		return atualizadoSucesso;
	} catch (SQLException e) {
		System.out.println("Erro ao atualizar email no BD! "+e);
		throw new RuntimeException(e);
	} finally {
		ConexaoBanco.fecharConexao(this.connection);
	}
}
	
public boolean atualizarSenha(Cliente cliente) {

	boolean atualizadoSucesso = false;
	String sql = "UPDATE cliente SET senha_cliente = ? WHERE id_cliente = ?";
	
	try {
		this.connection = new ConexaoBanco().getConnection();
		PreparedStatement stmt = connection.prepareStatement(sql);

		// seta os valores

		stmt.setString(1, cliente.getSenhaCliente());
		stmt.setInt(2, cliente.getIdCliente());
		
		// executa
		int ok = stmt.executeUpdate();

		if (ok == 1) {
			System.out.println("Senha atualizada com sucesso no BD!");
			atualizadoSucesso = true;
		} else {
			System.out.println("Erro ao atualizar senha no BD! \n"+ stmt);
		}

		stmt.close();

		return atualizadoSucesso;
	} catch (SQLException e) {
		System.out.println("Erro ao atualizar senha no BD! "+e);
		throw new RuntimeException(e);
	} finally {
		ConexaoBanco.fecharConexao(this.connection);
	}
}

public boolean atualizarFoto(Cliente cliente) {

	boolean atualizadoSucesso = false;
	String sql = "UPDATE cliente SET caminho_img = ? WHERE id_cliente = ?";
	
	
	
	try {
		this.connection = new ConexaoBanco().getConnection();
		PreparedStatement stmt = connection.prepareStatement(sql);

		// seta os valores

		stmt.setString(1, cliente.getCaminhoImg());
		stmt.setInt(2, cliente.getIdCliente());
	
		
		// executa
		int ok = stmt.executeUpdate();

		if (ok == 1) {
			System.out.println("Imagem atualizado com sucesso no BD!");
			atualizadoSucesso = true;
		} else {
			System.out.println("Erro ao atualizar IMG no BD! \n"+ stmt);
		}

		stmt.close();

		return atualizadoSucesso;
	} catch (SQLException e) {
		System.out.println("Erro ao atualizar IMG no BD! "+e);
		throw new RuntimeException(e);
	} finally {
		ConexaoBanco.fecharConexao(this.connection);
	}
}

	
}
