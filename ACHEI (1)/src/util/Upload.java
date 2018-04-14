package util;

import java.io.File;
import java.io.IOException;
import java.util.function.LongToIntFunction;

import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import controller.LoginController;
import dao.ClienteDAO;
import model.Cliente;

public class Upload {

    private static final Upload INSTANCE = new Upload();

    private Upload() {}

    public void write(Part part) throws IOException {
        String fileName = extractFileName(part);
        
        String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("")+"/img/foto";

        File fileSaveDir = new File(filePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        part.write(filePath + File.separator + fileName.trim().replaceAll(" ", "_"));  
        
        Cliente cliente = new Cliente();
        LoginController lg = new LoginController();
        cliente = lg.getCliente();
        cliente.setCaminhoImg(fileName.trim().replaceAll(" ", "_"));
        ClienteDAO cDAO = new ClienteDAO();
        cDAO.atualizarFoto(cliente);
        
        System.out.println(filePath+File.separator+fileName.trim().replaceAll(" ", "_"));
    }

    public String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }

    public static Upload getInstance() {
        return INSTANCE;
    }

}