package Controller;

// Importa todas as telas do pacote View
import Model.Arquivo;
import Model.Caminhao;
import Model.Produto;
import View.*;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Luiz Batista Cardoso
 *      22 mar 2023
 */

// Esta classe é responsável por fazer o controle das telas e executar o meio termo dos métodos.
public class ViewController {
    
    // Declaração das telas JFrom:
    private final JFrameMenu menu;
    private final JFrameAbout about;
    
    private final JFrameConsultarTransporte consultarRota;
    private final JFrameCadastrarTransporte cadastrarRota;
    
    private final JFrameDadosEstatistica dadosEstatistica;
    
    private final JFrameRotaCalculada rotaCalculada;
    private final JFrameCadastrarProduto cadastrarProduto;
    
    // Variáveis do sistema:
    private Caminhao c1 = null;
    private Produto p1 = null;
    
    private String nomeArquivo;
    private String cidadeOrigem;
    private String cidadeDestino;
    
    private String[] listaCidades;
    
    private ArrayList listaCidadesSelecionada;
    
    private JTable tabelaTrechos;
    private JTable tabelaProdutos;
    
    private Double distancia;
    private Double custoTransporte;

    // Construtor das telas JForm;

    public ViewController(JFrameMenu menu, JFrameAbout about, JFrameConsultarTransporte consultarRota, JFrameCadastrarTransporte cadastrarRota, JFrameDadosEstatistica dadosEstatistica, JFrameRotaCalculada rotaCalculada, JFrameCadastrarProduto cadastrarProduto) {
        this.menu = menu;
        this.about = about;
        this.consultarRota = consultarRota;
        this.cadastrarRota = cadastrarRota;
        this.dadosEstatistica = dadosEstatistica;
        this.rotaCalculada = rotaCalculada;
        this.cadastrarProduto = cadastrarProduto;
    }
    

    // Controlador das telas pelo ActionListener
    public void initController() {
        // Inicia a tela padrão do programa.
        menu.setVisible(true);
        
        // Mostra uma mensgem (de maneira horrosa) pergutando o nome do arquivo para a classe Arquivo e para descobrir o nome das cidades.
        nomeArquivo = JOptionPane.showInputDialog(menu, "Por favor insira o nome do arquivo para ser lido.\nObs: Deve estar junto ao diretório raiz do projeto e incluir a extensão.\n\nPor exemplo como está escrito na linha abaixo.", "DNIT-distancias.csv")+"";
        descobrirCidades(nomeArquivo);
        
        // ActionListner caso o usuário clique no item do menu "About..."
        menu.getjMenuItem_About().addActionListener(e -> exibirTelaAbout());
        
        // ActionListner caso o usuário clique no botão OU item do menu "Consultar Rota..."
        menu.getjButton_SimularRota().addActionListener(e -> exibirSimularRota());
        menu.getjMenuItem_SimularRota().addActionListener(e -> exibirSimularRota());
        
        //ActionListener caso o usuário clique no botão OU no item do menu "Cadastrar Rota..."
        menu.getjButton_CalcularRota().addActionListener(e-> exibirCalcularRota());
        menu.getjMenuItem_CalcularRota().addActionListener(e-> exibirCalcularRota());
        
        //ActionListener caso o usuário clique no botão OU no item do menu "Cadastrar Rota..."
        menu.getjButton_DadosEstatistica().addActionListener(e-> exibirDadosEstatistica());
        menu.getjMenuItem_DadosEst().addActionListener(e-> exibirDadosEstatistica());
    }
    
    // Cada um dos métodos que comecem por 'exibir' abrem suas respectivas telas pelo ActionListner no initController.
    private void exibirTelaAbout() {
        System.out.println("Abrindo formulário do about...");
        about.setVisible(true);
    }
    
    private void exibirSimularRota() {
        System.out.println("Abrindo formulário de simular rota...");

        // Atualiza as comboBox contendo a lista de cidades com base na leitura do arquivo.
        consultarRota.atualizarCidades(listaCidades);
        
        // 
        consultarRota.getCalcRota().addActionListener(e->exibirRotaSimulada());
        
        //
        consultarRota.getCalcDestino().addActionListener(e->simularCustoTrecho());

        consultarRota.setVisible(true);   
    }
    
    private void exibirRotaSimulada() {
        System.out.println("Abrindo resulado de simular rota...");
        
        // Desabilita o botão caso o usuário tente re-calulcar um novo trajeto.
        consultarRota.getCalcRota().setEnabled(false);
        
        // Popula o campo de distancia e custo de Km
        rotaCalculada.setJTextField_Distancia(distancia);
        rotaCalculada.setJTextField_KmRodado(c1.getCustoQuilometragem());
        
        // Determina o custo de transporte com um metodo na classe Caminhão. Talvez ficasse melhor em outro lugar...
        custoTransporte = c1.calcularCustoQuilometragem(distancia);
        
        // Exibe uma mensagem no terminal (talvez até um messageDialog) e atualiza o jTextField com o custo do transporte.
        System.out.println("O custo de transporte das duas cidades, utilizando um caminhão de "+c1.getPorte()+" porte, a distância é de "+distancia+" km e o custo será de R$ "+custoTransporte);
        rotaCalculada.setJTextField_CustoTrecho(custoTransporte);
        
        rotaCalculada.setVisible(true);
    }
    
    private void exibirCalcularRota() {
        System.out.println("Abrindo formulário de calular rota...");
        
        // Cria uma nova lista de cidades para ser adicionada.
        listaCidadesSelecionada = new ArrayList();
        
        // Atualiza as comboBox contendo a lista de cidades com base na leitura do arquivo.
        cadastrarRota.atualizarCidades(listaCidades); 
        
        // Ao clicar em adicionar em "Cadastrar Trecho" o programa vai liberar acesso ao cadastro de produtos e criar uma nova rota e adicionar na tabela.
        cadastrarRota.getjButton_CadastrarTrecho().addActionListener(l -> cadastarNovaRota());
        
        // Ao clicar em "Remover rota seleciona..." ele vai checar em um metodo se ela esta selecionada e ira pedir confirmação para remover.
        cadastrarRota.getjButton_RemoverTrechoTabela().addActionListener(l -> removerLinhaSelecionada(tabelaTrechos, cadastrarRota));
        
        // Ao clicar em "Novo produto" o usuário será levado a uma interface que irá cadastar um novo produto e atrelar na rota selecionada.
        cadastrarRota.getjButton_CadastrarProduto().addActionListener(l -> cadastrarNovoProduto());
        
        cadastrarRota.setVisible(true);
    } 
   
    private void exibirDadosEstatistica() {
        System.out.println("Abrindo formulário de dados estatísticos...");
        
        // Todo requisito 3 (27/03)
        
        dadosEstatistica.setVisible(true);
    }
    
    // Método que a partir de duas ou mais cidades enviada pelo usário calcula a rota entre elas. 
    private void simularCustoTrecho(){
        // Descobre qual o porte de caminhão que foi selecionado na ComboBox.
        String porteCaminhao = String.valueOf(consultarRota.getjComboBox_PorteCaminhao().getSelectedItem());

        // Descobre qual as cidades escolhidas na ArrayList e popula uma string para ser enviado para o método que calcula distancia.
        cidadeOrigem = String.valueOf(consultarRota.getjComboBox_CidadeOrigem().getSelectedItem());
        cidadeDestino = String.valueOf(consultarRota.getjComboBox_CidadeDestino().getSelectedItem());
        
        // Baseado no tipo de porte selecionado o caminhão receberá os atributos fornecidos no enunciado (estático).
        switch (porteCaminhao) {
            case "Pequeno" -> {
                c1 = new Caminhao(porteCaminhao, 1000, 4.87);
            }
            case "Médio" -> {
                c1 = new Caminhao(porteCaminhao, 4000, 11.92);
            }
            case "Grande" -> {
                c1 = new Caminhao(porteCaminhao, 10000, 27.44);
            }
            // TODO: Generazilar os valores do custo de quilometragem e peso nas configurações.
            default -> throw new AssertionError();
        }

        // Faz a chamada ao arquivo com o método para retornar a distância.
        distancia =  Arquivo.descobirDistancia(cidadeOrigem, cidadeDestino); 
        consultarRota.setJTextFieldDistancia(distancia);
    }
    
    // Método responsável por calcular o custo das rotas informadas no JForm CalularRota.
    private void calcularCustoTrechos(){
        // TODO: calcular trechos indeterminados.
        System.out.println("Sim");
    }
    
    // Método que, ao chamado, irá ler as informações no arquivo de maneira sequencial por linha e salva em um vetor.
    private void descobrirCidades(String nomeArquivo){
        // Faz a chamada ao aquivo contendo o método que le o arquivo pelo nome informado.
        Arquivo.extrairDadosArquivo(nomeArquivo);
        
        // Popula as cidades baseado no arquivo informado.
        listaCidades = Arquivo.getCidades();
      
        String mensagem;
        if(listaCidades != null){
            mensagem = "Todas as cidades no arquivo informado foram cadastradas com sucesso!";
            // Ativa todos os botões da interface principal do programa somente se não houver problemas com o arquivo.
            menu.getjButton_SimularRota().setEnabled(true);
            menu.getjButton_CalcularRota().setEnabled(true);
            menu.getjButton_DadosEstatistica().setEnabled(true);
            // Ativa todos os items do menu fundamental ao programa para o o usário.
            menu.getjMenuItem_SimularRota().setEnabled(true);
            menu.getjMenuItem_CalcularRota().setEnabled(true);
            menu.getjMenuItem_DadosEst().setEnabled(true);
        }else{
            mensagem = "ERRO! Não foram cadastradas nenhuma cidade, verefique o terminal para mais informações.";
        }
        System.out.println(mensagem);
        JOptionPane.showMessageDialog(menu, mensagem);
    } 
    
    private void cadastarNovaRota() {
        System.out.println("Usuário cadastrou um novo trecho para a tabela.");
        
        JComboBox selecaoCidadeOrigem = cadastrarRota.getjComboBox_CidadeOrigem();

        // Pega a tabela de trechos cada vez que for criado um novo cadastro.
        tabelaTrechos = cadastrarRota.getjTable_TabelaTrechos();
        
        // Determina a cidade de origem e destino inicail e depois a distancia entre elas.
        cidadeOrigem = String.valueOf(cadastrarRota.getjComboBox_CidadeOrigem().getSelectedItem());
        cidadeDestino = String.valueOf(cadastrarRota.getjComboBox_CidadeDestino().getSelectedItem());
        distancia = Arquivo.descobirDistancia(cidadeOrigem, cidadeDestino);
        
        // Verifica se as duas cidades não são iguais (redundancia) 
        if(cidadeOrigem.equals(cidadeDestino)){
            JOptionPane.showMessageDialog(cadastrarRota, "Atenção! A cidade de origem é a mesma de destino.\nEsta insersão foi ignorada. Por favor, tente novamente.");
        } else {
            // Importa o modelo da tabela e salva nela o valor!
            DefaultTableModel model = ((DefaultTableModel) tabelaTrechos.getModel());
            model.addRow(new String[]{cidadeOrigem, cidadeDestino, Double.toString(distancia) + " Km"});// {CidadeA, CidadeB, Distancia}
            cadastrarRota.getjButton_RemoverTrechoTabela().setEnabled(true);

            // Salva em uma lista cada trajeto selecionado para referenciar depois aos produtos.
            listaCidadesSelecionada.add(cidadeOrigem + " - " + cidadeDestino);

            // Como a cidade de origem sempre vai ser a destino da anterior, ela sera tratada automaticamente aqui.
            selecaoCidadeOrigem.setEnabled(false);
            selecaoCidadeOrigem.setSelectedItem(String.valueOf(cidadeDestino));

            //Permite o usuario a cadastrar produtos para o destino atual.
            cadastrarRota.getjComboBox_ListaTransportes().setEnabled(true);
            cadastrarRota.getjButton_CadastrarProduto().setEnabled(true);
            cadastrarRota.getjButton_RemoverProdutoTabela().setEnabled(true);
            cadastrarRota.getjTable_TabelaProdutos().setEnabled(true);
            
            // Atualiza a lista de rotas para selecionar no cadastro de produtos.
            cadastrarRota.getjComboBox_ListaTransportes().setModel(new DefaultComboBoxModel<>((String[]) listaCidadesSelecionada.toArray(String[]::new)));
            
            // Chama 
        }
    }

    private int removerLinhaSelecionada(JTable tabela, Component contexto) {
        if (tabela.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(contexto, "Erro! Por favor selecione uma linha na tabela para ser removida.");
        } else {
            int confirm = JOptionPane.showOptionDialog(contexto,
                    "Você tem certeza que quer remover esta rota?",
                    "Confirmar remoção de rota?", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Removendo linha selecionada...");
                // Caso a linha deletada for a primeira o método permite que a cidade de origem seja selecionada novamente para evitar softlock.
                // Obs: Isso é horrivél mas eu não consegui pensar em nada melhor no momento
                if(tabela.getName().equals("tabelaTrechos")){  
                    System.out.println(tabela.getName());
                    if (tabela.getRowCount() <= 1) {
                        System.out.println("Evitando softlock, ativando a seleção da cidade de origem");
                        cadastrarRota.getjComboBox_CidadeOrigem().setEnabled(true);
                    }
                }
                ((DefaultTableModel) tabela.getModel()).removeRow(tabela.getSelectedRow()); 
            }
        }
        return tabela.getSelectedRow();
    }

    private void cadastrarNovoProduto() {
        //TODO: formulário para cadastrar um novo produto, contendo nome peso e depois ele pede a quantidade. 
        System.out.println("Abrindo formulário de cadastro de produto...");
        cadastrarProduto.setVisible(true);
    }
}
