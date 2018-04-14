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
import model.Endereco;


public class EnderecoDAO {

	private Connection connection;

	public int incluirOuAlterarEndereco(Endereco endereco) {

		int idInserido = 0;
		String sql;
		
		if(endereco.getIdEndereco() <=0){
			 sql = "INSERT INTO endereco (endereco_id_cliente, rua_endereco, numero_endereco, complemento_endereco, bairro_endereco, cidade_endereco, estado_endereco, cep_endereco) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		}else{
			 sql = "UPDATE endereco SET rua_endereco = ?, numero_endereco = ?, complemento_endereco = ?, bairro_endereco = ?, cidade_endereco = ?, estado_endereco = ?, cep_endereco = ? WHERE id_endereco = ?";
		}
		
		
		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt;
			// seta os valores
			
			if(endereco.getIdEndereco() <=0){
				 stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				
				stmt.setInt(1, endereco.getIdCliente());
				stmt.setString(2, endereco.getRuaEndereco());
				stmt.setInt(3, endereco.getNumeroEndereco());
				stmt.setString(4, endereco.getComplementoEndereco());
				stmt.setString(5, endereco.getBairroEndereoc());
				stmt.setString(6, endereco.getCidadeEndereco());
				stmt.setString(7, endereco.getEstadoEndereco());
				stmt.setString(8, endereco.getCepEndereco());
				
				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();

				if (rs.next()) {
					idInserido = rs.getInt(1);
				}

				if (idInserido > 0) {
					System.out.println("Endereo inserido");
				} else {
					System.out.println("Erro ao inserir ou atualizar Endereco \n" + sql);
				}

			}else{
				 stmt = connection.prepareStatement(sql);
				
				stmt.setString(1, endereco.getRuaEndereco());
				stmt.setInt(2, endereco.getNumeroEndereco());
				stmt.setString(3, endereco.getComplementoEndereco());
				stmt.setString(4, endereco.getBairroEndereoc());
				stmt.setString(5, endereco.getCidadeEndereco());
				stmt.setString(6, endereco.getEstadoEndereco());
				stmt.setString(7, endereco.getCepEndereco());
				stmt.setInt(8, endereco.getIdEndereco());
				
				int ok = stmt.executeUpdate();

				if (ok == 1) {
					System.out.println("Endeeco atualizado com sucesso no BD!");
					idInserido = endereco.getIdEndereco();
				} else {
					System.out.println("Erro ao atualizar Endereco no BD! \n sql");
				}
			}
			
			stmt.close();

			return idInserido;
			
		} catch (SQLException e) {
			System.out.println("Erro ao inserir Endereco " +e);
			throw new RuntimeException(e);
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}


	public Endereco obterEndereco(int idCliente) {
		Endereco endereco = null;
		
		String sql = "SELECT * FROM endereco WHERE endereco_id_cliente = ?";

		System.out.println("Buscando endereco no BD");
		
		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setInt(1, idCliente);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				endereco = new Endereco();
				endereco.setIdEndereco(rs.getInt("id_endereco"));
				endereco.setRuaEndereco(rs.getString("rua_endereco"));
				endereco.setNumeroEndereco(rs.getInt("numero_endereco"));
				endereco.setComplementoEndereco(rs.getString("complemento_endereco"));
				endereco.setBairroEndereoc(rs.getString("bairro_endereco"));
				endereco.setCidadeEndereco(rs.getString("cidade_endereco"));
				endereco.setEstadoEndereco(rs.getString("estado_endereco"));
				endereco.setCepEndereco(rs.getString("cep_endereco"));
			}

			
			return endereco;

		} catch (SQLException e) {
			System.out.println("Erro ao buscar Endereco no BD! "+e);
			e.printStackTrace();
			return null;
			
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}

}
