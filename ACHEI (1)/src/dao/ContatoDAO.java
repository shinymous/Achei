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
import model.Cliente;
import model.Contato;
import model.Endereco;


public class ContatoDAO {

	private Connection connection;

	public int incluirOuAlterarContato(Contato contato) {
		
		int idInserido = 0;
		String sql;
		
		if(contato.getIdContato() <=0){
			 sql = "INSERT INTO contato (telefone1, telefone2, telefone3, telefone4, contato_id_cliente) VALUES (?, ?, ?, ?, ?)";
		}else{
			 sql = "UPDATE contato SET telefone1 = ?, telefone2 = ?, telefone3 = ?, telefone4 = ?  WHERE id_contato = ?";
		}
		
		
		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt;
			// seta os valores
			
			if(contato.getIdContato() <=0){
				 stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				
				stmt.setString(1, contato.getTelefone1());
				stmt.setString(2, contato.getTelefone2());
				stmt.setString(3, contato.getTelefone3());
				stmt.setString(4, contato.getTelefone4());
				stmt.setInt(5, contato.getContato_id_cliente());
				
				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();

				if (rs.next()) {
					idInserido = rs.getInt(1);
				}

				if (idInserido > 0) {
					System.out.println("Contato inserido");
				} else {
					System.out.println("Erro ao inserir ou atualizar Contato \n" + sql);
				}

			}else{
				 stmt = connection.prepareStatement(sql);
				
				stmt.setString(1, contato.getTelefone1());
				stmt.setString(2, contato.getTelefone2());
				stmt.setString(3, contato.getTelefone3());
				stmt.setString(4, contato.getTelefone4());
				stmt.setInt(5, contato.getIdContato());
				
				
				int ok = stmt.executeUpdate();

				if (ok == 1) {
					System.out.println("Contato atualizado com sucesso no BD!");
					idInserido = contato.getIdContato();
				} else {
					System.out.println("Erro ao atualizar Contato no BD! \n sql");
				}
			}
			
			stmt.close();

			return idInserido;
			
		} catch (SQLException e) {
			System.out.println("Erro ao inserir Contato " +e);
			throw new RuntimeException(e);
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}


	public Contato obterContato(int idCliente) {
		Contato contato = null;
		
		String sql = "SELECT * FROM contato WHERE contato_id_cliente = ?";

		System.out.println("Buscando Contatos no BD");
		
		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setInt(1, idCliente);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				contato = new Contato();
				contato.setIdContato(rs.getInt("id_contato"));
				contato.setTelefone1(rs.getString("telefone1"));
				contato.setTelefone2(rs.getString("telefone2"));
				contato.setTelefone3(rs.getString("telefone3"));
				contato.setTelefone4(rs.getString("telefone4"));
				
			}

			
			return contato;

		} catch (SQLException e) {
			System.out.println("Erro ao buscar Contato no BD! "+e);
			e.printStackTrace();
			return null;
			
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}

}
