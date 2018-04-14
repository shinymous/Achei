package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import util.ConexaoBanco;
import model.Cliente;
import model.Codigo;
import model.Endereco;
import model.Objeto;


public class ObjetoDAO {

	private Connection connection;

	public int incluirOuAlterarObjeto(Objeto objeto) {
		
		int idInserido = 0;
		String sql;
		
		if(objeto.getIdObjeto() <=0){
			 sql = "INSERT INTO objeto (objeto_id_codigo, tipo_objeto, mensagem_objeto, informacao_objeto) VALUES (?, ?, ?, ?)";
		}else{
			 sql = "UPDATE objeto SET tipo_objeto = ?, mensagem_objeto = ?, informacao_objeto = ? WHERE id_objeto = ?";
		}
		
		
		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt;
			// seta os valores
			
			if(objeto.getIdObjeto() <=0){
				 stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				
				stmt.setInt(1, objeto.getObjetoIdCodigo());
				stmt.setString(2, objeto.getTipoObjeto());
				stmt.setString(3, objeto.getMensagemObjeto());
				stmt.setString(4, objeto.getInformacaoObjeto());
				
				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();

				if (rs.next()) {
					idInserido = rs.getInt(1);
				}

				if (idInserido > 0) {
					System.out.println("Objeto inserido om sucesso");
				} else {
					System.out.println("Erro ao inserir ou atualizar Objeto \n" + stmt);
				}

			}else{
				 stmt = connection.prepareStatement(sql);
				
				stmt.setString(1, objeto.getTipoObjeto());
				stmt.setString(2, objeto.getMensagemObjeto());
				stmt.setString(3, objeto.getInformacaoObjeto());
				stmt.setInt(4, objeto.getIdObjeto());
				
				int ok = stmt.executeUpdate();

				if (ok == 1) {
					System.out.println("Objeto atualizado com sucesso no BD!");
					idInserido = objeto.getIdObjeto();
				} else {
					System.out.println("Erro ao atualizar Objeto no BD! \n"+ stmt);
				}
			}
			
			stmt.close();

			return idInserido;
			
		} catch (SQLException e) {
			System.out.println("Erro ao inserir Objetio " +e);
			throw new RuntimeException(e);
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}


	public Objeto obterObjeto(int idCodigo) {
		Objeto objeto = null;
		
		String sql = "SELECT * FROM objeto WHERE objeto_id_codigo = ?";

		System.out.println("Buscando objeeto no BD");
		
		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setInt(1, idCodigo);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				objeto = new Objeto();
				objeto.setIdObjeto(rs.getInt("id_objeto"));
				objeto.setObjetoIdCodigo(rs.getInt("objeto_id_codigo"));
				objeto.setTipoObjeto(rs.getString("tipo_objeto"));
				objeto.setMensagemObjeto(rs.getString("mensagem_objeto"));
				objeto.setInformacaoObjeto(rs.getString("informacao_objeto"));

			}
			
			return objeto;

		} catch (SQLException e) {
			System.out.println("Erro ao buscar Objeto no BD! "+e);
			e.printStackTrace();
			return null;
			
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}

}
