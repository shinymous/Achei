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
import model.Codigo;
import model.Contato;
import model.Endereco;
import model.Notificacao;
import model.Objeto;


public class NotificacaoDAO {

	private Connection connection;

	public int incluirOuAlterarNotificacao(Notificacao notificacao) {
		
		int idInserido = 0;
		String sql;
		
		if(notificacao.getIdNotificacao() <=0){
			 sql = "INSERT INTO notificacao (notificacao_id_objeto, data_notificacao, situacao, latitude, longitude) VALUES (?, ?, ?, ?, ?)";
		}else{
			 sql = "UPDATE notificacao SET mensagem_notificacao = ?, assunto_notificacao = ?, nome_notificacao = ?, situacao = ?  WHERE id_notificacao = ?";
		}
		
		
		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt;
			// seta os valores
			
			if(notificacao.getIdNotificacao() <=0){
				 stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				
				java.sql.Date dataNotificacaoSQL = new java.sql.Date(notificacao.getData().getTime());
				 
				stmt.setInt(1, notificacao.getObjeto().getIdObjeto());
				stmt.setDate(2, dataNotificacaoSQL);
				stmt.setInt(3, notificacao.getSituacao());
				stmt.setDouble(4, notificacao.getLatitude());
				stmt.setDouble(5, notificacao.getLongitude());
				
				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();

				if (rs.next()) {
					idInserido = rs.getInt(1);
				}

				if (idInserido > 0) {
					System.out.println("Notificacao inserido");
				} else {
					System.out.println("Erro ao inserir Notificacao \n" + stmt);
				}

			}else{
				 stmt = connection.prepareStatement(sql);
				
				stmt.setString(1, notificacao.getMensagem());
				stmt.setString(2, notificacao.getAssuntoMensagem());
				stmt.setString(3, notificacao.getNomeMensagem());
				stmt.setInt(4, notificacao.getSituacao());
				stmt.setInt(5, notificacao.getIdNotificacao());
				
				
				int ok = stmt.executeUpdate();

				if (ok == 1) {
					System.out.println("Notificacao atualizado com sucesso no BD!");
					idInserido = notificacao.getIdNotificacao();
				} else {
					System.out.println("Erro ao atualizar Notificacao no BD! \n"+ stmt);
				}
			}
			
			stmt.close();

			return idInserido;
			
		} catch (SQLException e) {
			System.out.println("Erro ao inserir Notificacao " +e);
			throw new RuntimeException(e);
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}


	public ArrayList<Notificacao> listarNotificacoes(int idCliente) {
		
		Notificacao notificacao ;
		Objeto objeto;
		ArrayList<Notificacao> notificacoes = new ArrayList<Notificacao>();

		String sql = "SELECT * FROM notificacao as n INNER JOIN objeto as o ON n.notificacao_id_objeto = o.id_objeto INNER JOIN codigo as c on o.objeto_id_codigo = c.id_codigo INNER JOIN cliente on c.codigo_id_cliente = cliente.id_cliente WHERE cliente.id_cliente =" + idCliente;

		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				objeto = new Objeto();
				objeto.setIdObjeto(rs.getInt("id_objeto"));	
				objeto.setTipoObjeto(rs.getString("tipo_objeto"));
				objeto.setMensagemObjeto(rs.getString("mensagem_objeto"));
				objeto.setInformacaoObjeto(rs.getString("informacao_objeto"));;
				notificacao = new Notificacao();
				notificacao.setIdNotificacao(rs.getInt("id_notificacao"));
				notificacao.setData(rs.getDate("data_notificacao"));
				notificacao.setMensagem(rs.getString("mensagem_notificacao"));
				notificacao.setAssuntoMensagem(rs.getString("assunto_notificacao"));
				notificacao.setNomeMensagem(rs.getString("nome_notificacao"));
				notificacao.setSituacao(rs.getInt("situacao"));
				notificacao.setLatitude(rs.getDouble("latitude"));
				notificacao.setLongitude(rs.getDouble("longitude"));
				notificacao.setObjeto(objeto);
				
				notificacoes.add(notificacao);

			}
			System.out.println("Buscando Array de notificacoes");
			stmt.close();
			return notificacoes;
		} catch (SQLException e) {
			System.out.println("Erro ao buscar notificacoes no BD! " + e);
			return notificacoes;
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}
	

	public boolean deletaNotificacao(int idNotificacao) {
		
				boolean removidoSucesso = false;
				String sql = "DELETE FROM notificacao WHERE id_notificacao = ?";
		
				try {
					this.connection = new ConexaoBanco().getConnection();
					PreparedStatement stmt = connection.prepareStatement(sql);
					stmt.setInt(1, idNotificacao);
					int ok = stmt.executeUpdate();
		
					if (ok == 1) {
						System.out.println("Notificacao removida com sucesso");
						removidoSucesso = true;
					} else {
						System.out.println("Erro ao remover Notificacao");
					}
		
					stmt.close();
					return removidoSucesso;
				} catch (SQLException e) {
					System.out.println("Erro ao remover Notificacao no BD! "+e);
					throw new RuntimeException(e);
				}finally {
					ConexaoBanco.fecharConexao(this.connection);
				}
			}
	
}
