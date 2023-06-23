package Controller;

import Model.*;
import View.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luiz
 */
public class ViewController {

    private final JFrameMenu menu;
    private final JFrameAbout about;
    private final JFrameGrafo grafo;

    private ArrayList<String> dadosArquivo;

    private ArrayList<Aresta> listaArestas;
    private ArrayList<String> listaVertices;
    
    private ArrayList<Rota> listaRotas;

    private Grafo g1;

    public ViewController(JFrameMenu menu, JFrameAbout about, JFrameGrafo grafo) {
        this.menu = menu;
        this.about = about;
        this.grafo = grafo;
    }

    // Inicializa o controlador de telas.
    public void initController() {
        // Inicia a tela padrão do programa.
        menu.setVisible(true);

        menu.getjButton_CarregarAqruivo().addActionListener(e -> carregarArquivo());
        menu.getjButton_MostrarRotas().addActionListener(e -> popularGrafo());
    }

    // Inicializa as variáveis.
    public void initVariables() {
        dadosArquivo = new ArrayList<>();
        listaVertices = new ArrayList<>();
        listaArestas = new ArrayList<>();
    }

    private void carregarArquivo() {
        System.out.println("Tentando carregar arquivo");

        // Tenta carregar o arquivo cujo o nome é dado pelo jFileChooser.
        try {
            dadosArquivo = Arquivo.lerArquivo(Arquivo.escolherArquivo(menu));
        } catch (Exception ex) {
            System.err.println("Erro ao ler arquivo.");
            JOptionPane.showMessageDialog(menu, "ERRO AO LER ARQUIVO. Certifique-se que o nome e diretório foram inseridos corretamente, não é necessário o uso de extensão.");
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Mostra no console todas as linhas do arquivo. (Substituir depois)
        for (String linha : dadosArquivo) {
            System.out.println(linha);
        }

        // Troca o estado dos botões, agora que o arquivo foi carregado pode ser calculado.
        menu.getjButton_CarregarAqruivo().setEnabled(false);
        menu.getjButton_MostrarRotas().setEnabled(true);
    }

    private void popularGrafo() {
        grafo.setVisible(true);

        System.out.println("Populando lista de VERTICES da primeira linha do arquivo...");

        Grafo.popularVertices(dadosArquivo, listaVertices);
        System.out.println("Vértices carregados com sucesso!");

        System.out.println("Populando lista de ARESTAS da segunda linha do arquivo...");
        listaRotas = Aresta.popularArestas(dadosArquivo, listaArestas, listaVertices);

        g1 = new Grafo(listaVertices, listaArestas);
        menu.getjButton_MostrarRotas().setEnabled(false);
        Rota.calcularRotas(listaRotas, g1);
        popularTabelas();
    }

    public void popularTabelas() {
        // Popula a tabela de vértics com os dados do grafo adicionado.
        DefaultTableModel tabelaVertices = (DefaultTableModel) grafo.getjTable_vertices().getModel();

        for (int i = 0; i < g1.getVertices().size(); i++) {
            tabelaVertices.addRow(new Object[]{g1.getVertices().get(i)});
        }

        // Popula a tabela de arestas com os dados do grafo adicionado.
        DefaultTableModel tabelaArestas = (DefaultTableModel) grafo.getjTable_arestas().getModel();

        // Pega todas as arestas
        for (int i = 0; i < g1.getArestas().size(); i++) {
            Aresta a = g1.getArestas().get(i);
            tabelaArestas.addRow(new Object[]{a.getOrigem(), a.getDestino(), a.getCusto(), a.getTransporte()});
        }

        // Popula a tabela de arestas com os dados do grafo adicionado.
        DefaultTableModel tabelaRotas = (DefaultTableModel) grafo.getjTable_rotas().getModel();

        // Pega todas as arestas
        for (int i = 0; i < listaRotas.size(); i++) {     
            tabelaRotas.addRow(new Object[]{listaRotas.get(i).getCaminho(), listaRotas.get(i).getCusto(), listaRotas.get(i).getTransporte(), listaRotas.get(i).getCustoBeneficio()});
        }

        // Popula o campo da melhor rota com o valor dela referenciado na tabela de rotas.
        grafo.setjTextField_melhorRota("ROTAS COM " + Rota.descobrirMelhorRota(listaRotas) + " TRANSPORTE / CUSTO");
    }
    
    
   
 
}
