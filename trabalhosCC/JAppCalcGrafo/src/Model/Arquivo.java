package Model;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Luiz
 */
public class Arquivo {

    // 1º recebe um diretorio
    // 2º abre e le o arquivo
    // 3º retorna os valores para a classe grafo que ira tratalos
    
    // Método que pede ao usuário para escolher um arquvio para ser lido.
    public static String escolherArquivo(Component tela){
        String dir = "grafo.txt";
        JFileChooser j = new JFileChooser();
        int status = j.showOpenDialog(tela);
        if (status == JFileChooser.APPROVE_OPTION){
              dir = j.getSelectedFile().getName();
        }else{
               JOptionPane.showMessageDialog(tela, "ATENÇÃO! Nenhum arquivo foi selecionado. \nO arquivo padrão do projeto foi carregado.");      
        }
        System.out.println(dir);
        return dir;
    }
    
    // Método que recebe um arquivo e retorna os dados dentro em uma lista.
    public static ArrayList lerArquivo(String nomeArquivo) throws Exception {

        FileReader fr = new FileReader(nomeArquivo);
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> dadosArquivo = new ArrayList<>();

        // Lê todos as linhas no arquivo e adiciona a lista.
        while (br.ready()) { dadosArquivo.add(br.readLine()); }

        br.close();
        fr.close();

        return dadosArquivo;
    }
    
    // Método que popula as classes com as linhas de cada arquivo.
    public static void tratarArquivo(ArrayList linhasArquivo){
        
    }
}
