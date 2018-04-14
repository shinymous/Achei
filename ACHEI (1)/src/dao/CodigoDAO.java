package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.primefaces.push.impl.JSONEncoder;

import model.Cliente;
import model.Codigo;
import model.Contato;
import model.Endereco;
import model.Objeto;
import util.ConexaoBanco;

public class CodigoDAO {

	private Connection connection;

	public int incluirCodigo(Codigo codigo) {

		int idInserido = 0;
		String sql = "INSERT INTO codigo (codigo, codigo_id_cliente) VALUES (?, ?)";

		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// seta os valores

			stmt.setString(1, codigo.getCodigo());
			stmt.setInt(2, codigo.getCodigoIdCliente());

			// executa
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				idInserido = rs.getInt(1);
			}

			if (idInserido > 0) {
				System.out.println("Código inserido com sucesso");
			} else {
				System.out.println("Erro ao inserir Código");
			}

			stmt.close();

			return idInserido;

		} catch (SQLException e) {
			System.out.println("Erro ao inserir Código " + e);
			throw new RuntimeException(e);
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}
	
	
	

	public ArrayList<Codigo> listarCodigos(int idCliente) {
		
		Codigo codigo ;
		Objeto objeto;
		ArrayList<Codigo> codigos = new ArrayList<Codigo>();

		String sql = "SELECT * FROM codigo as c LEFT JOIN objeto as o ON c.id_codigo = o.objeto_id_codigo WHERE c.codigo_id_cliente =" + idCliente+" order by id_codigo;";

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
				codigo = new Codigo();
				codigo.setIdCodigo(rs.getInt("id_codigo"));
				codigo.setCodigoIdCliente(rs.getInt("codigo_id_cliente"));
				codigo.setCodigo(rs.getString("codigo"));
				codigo.setObjeto(objeto);
				
				codigos.add(codigo);

			}
			System.out.println("Buscando Array de codigos");
			stmt.close();
			return codigos;
		} catch (SQLException e) {
			System.out.println("Erro ao buscar codigos no BD! " + e);
			return codigos;
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}
	
	
	public Cliente localizarDTO(String codigoDigitado, int idCodigo) {
		Cliente cliente = null;
		Endereco endereco = null;
		Contato contato = null;
		Objeto objeto = null;
		Codigo codigo = null;
		ArrayList<Codigo> codigos = new ArrayList<Codigo>();
		
		String sql = "SELECT * FROM cliente INNER JOIN endereco ON cliente.id_cliente = endereco.endereco_id_cliente INNER JOIN contato ON cliente.id_cliente = contato.contato_id_cliente "
				+ "INNER JOIN codigo ON cliente.id_cliente = codigo.codigo_id_cliente INNER JOIN objeto ON codigo.id_codigo = objeto.objeto_id_codigo WHERE codigo.codigo = ? AND codigo.id_codigo =?";

		System.out.println("Buscando DTO no BD");
		
		try {
			this.connection = new ConexaoBanco().getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setString(1, codigoDigitado);
			stmt.setInt(2, idCodigo);
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
				//
				contato = new Contato();
				contato.setIdContato(rs.getInt("id_contato"));
				contato.setTelefone1(rs.getString("telefone1"));
				contato.setTelefone2(rs.getString("telefone2"));
				contato.setTelefone3(rs.getString("telefone3"));
				contato.setTelefone4(rs.getString("telefone4"));
				//
				objeto = new Objeto();
				objeto.setIdObjeto(rs.getInt("id_objeto"));
				objeto.setObjetoIdCodigo(rs.getInt("objeto_id_codigo"));
				objeto.setTipoObjeto(rs.getString("tipo_objeto"));
				objeto.setMensagemObjeto(rs.getString("mensagem_objeto"));
				objeto.setInformacaoObjeto(rs.getString("informacao_objeto"));
				//
				codigo = new Codigo();
				codigo.setIdCodigo(rs.getInt("id_codigo"));
				codigo.setCodigoIdCliente(rs.getInt("codigo_id_cliente"));
				codigo.setCodigo(rs.getString("codigo"));
				codigo.setObjeto(objeto);
				codigos.add(codigo);
				//
				cliente = new Cliente();
				cliente.setIdCliente(rs.getInt("id_cliente"));
				cliente.setNomeCliente(rs.getString("nome_cliente"));
				cliente.setSobrenomeCliente(rs.getString("sobrenome_cliente"));
				cliente.setEmailCliente(rs.getString("email_cliente"));
				cliente.setDataNascimenteCliente(rs.getDate("data_cliente"));
				cliente.setCaminhoImg(rs.getString("caminho_img"));
				cliente.setEndereco(endereco);
				cliente.setContato(contato);
				cliente.setListaCodigos(codigos);
			}
			
			return cliente;

		} catch (SQLException e) {
			System.out.println("Erro ao buscar DTO no BD! "+e);
			e.printStackTrace();
			return null;
			
		} finally {
			ConexaoBanco.fecharConexao(this.connection);
		}
	}

}
