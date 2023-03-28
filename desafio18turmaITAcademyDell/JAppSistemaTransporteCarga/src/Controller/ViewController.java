package Controller;

// Importa todas as telas do pacote View
import Model.Arquivo;
import Model.Caminhao;
import Model.Produto;
import View.*;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luiz Batista Cardoso 22 mar 2023
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
    
    // Declaração inicial das classes Caminhao e Produto.
    private Caminhao caminhao = null;
    private Produto produto = null;

    // Strings, o nome do arquivo a ser lido, a cidade de orgiem e destino.
    private String nomeArquivo;
    private String cidadeOrigem;
    private String cidadeDestino;
    private String rotaIncialFinal;

    // Todas as cidades separadas, lida pelo arquivo CSV.
    private String[] listaCidades;

    // Variáveis que podem ser configuradas no menu "Config..."
    private Double[] capacidadeCaminhoes;
    private Double[] custosKm;
    
    // Estas listas armazenam informações locais ao invés do banco de dados ou arquivo.
    private ArrayList<Double> listaPesoTotal;                   // Contém todos os pesos combinados de cada produto inserido em determinada rota.
    private ArrayList listaCidadesSelecionadas;                  // Contém os trajetos A-B que o usuário escolheu (Ex: PORTO ALEGRE - SÃO PAULO)
    private ArrayList<Produto> listaProdutosCastrados;          // Contém todos os produtos cadastrados em determinada rota.
/**/private ArrayList<DefaultTableModel> modeloProdutosRota;    // Contém todos os modelos de tabela que podem ser usados para recuperar produtos de rotas já cadastradas (FEATURE REMOVED, LEGACY)

    // As duas tabelas do formulário de cadastro de transporte.
    private JTable tabelaRotasCadastradas;
    private JTable tabelaProdutosCadastrados;

    // Doubles, valor da distancia entre duas cidades, valor do custo de transporte simples, peso total da rota cadastrada (incluindo varios trajetos).
    private Double distancia;
    private Double custoTransporte;
    private Double pesoTotalRotas;
    
    // Doubles, armazendo o valor do resultado das rotas cadastradas do requisito 3.
    private Double custoTotalRotas;
    private Double custoUnitarioRotas;

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
        nomeArquivo = JOptionPane.showInputDialog(menu, "Por favor insira o nome do arquivo para ser lido.\nObs: Deve estar junto ao diretório raiz do projeto e incluir a extensão.\n\nPor exemplo como está escrito na linha abaixo.", "DNIT-distancias.csv") + "";

        inicializaVariaveis();
        
        descobrirCidades(nomeArquivo);

        // ActionListner caso o usuário clique no item do menu "About..."
        menu.getjMenuItem_About().addActionListener(e -> exibirTelaAbout());

        // ActionListner caso o usuário clique no botão OU item do menu "Consultar Rota..."
        menu.getjButton_SimularRota().addActionListener(e -> exibirSimularRota());
        menu.getjMenuItem_SimularRota().addActionListener(e -> exibirSimularRota());

        //ActionListener caso o usuário clique no botão OU no item do menu "Cadastrar Rota..."
        menu.getjButton_CalcularRota().addActionListener(e -> exibirCalcularRota());
        menu.getjMenuItem_CalcularRota().addActionListener(e -> exibirCalcularRota());

        //ActionListener caso o usuário clique no botão OU no item do menu "Cadastrar Rota..."
        menu.getjButton_DadosEstatistica().addActionListener(e -> exibirDadosEstatistica());
        menu.getjMenuItem_DadosEst().addActionListener(e -> exibirDadosEstatistica());
        
        // CADASTRO DE PRODUTO
        
        // Caso o usario clicar em CADASTRAR um produto sera aberto um form para cadastro.
        cadastrarProduto.getjButton_CadastarProduto().addActionListener(l -> cadastraProduto());

        // Caso o usuario clicar em DELETAR um produdo (linha na tabela produto)
        cadastrarRota.getjButton_RemoverProdutoTabela().addActionListener(l -> removeProduto());
        
        // Caso o usuário selecione um dos elementos na rota, deve atualizar/modificar a lista de produtos de acordo com o que se tem gravado.
/*OLD*/ cadastrarRota.getjComboBox_ListaTransportes().addActionListener(l -> tentarRecuperarTabelaProdutos(cadastrarRota.getjComboBox_ListaTransportes().getSelectedIndex()));
        
    }
    
    private void inicializaVariaveis(){
        // TEMP: define os modificadores do enunciado.
        custosKm = new Double[]{4.87, 11.92, 27.44};
        capacidadeCaminhoes = new Double[]{1000.0, 4000.0, 1000.0};

        // Incializando variáveis de CADASTRAR PRODUTO
        listaProdutosCastrados = new ArrayList<>();
        listaPesoTotal = new ArrayList<>();
    }

    // REQUISITO 1: Simula o custo de uma rota simples entre duas cidades a baseado na modalidade de transporte (P, M, G)
    private void exibirSimularRota() {
        System.out.println("Abrindo formulário de simular rota...");

        // Atualiza as comboBox contendo a lista de cidades com base na leitura do arquivo.
        consultarRota.atualizarCidades(listaCidades);

        // 
        consultarRota.getCalcRota().addActionListener(e -> exibirRotaSimulada());

        //
        consultarRota.getCalcDestino().addActionListener(e -> simularCustoTrecho());

        consultarRota.setVisible(true);
    }

    // REQUISITO 2: Faz o cálculo de 'n' rotas informadas pelo usuario e com base no peso determina o menor custo para Km rodado para 'n' caminhões. 
    private void exibirCalcularRota() {
        System.out.println("Abrindo formulário de calular rota...");

        // Cria uma nova lista de cidades para ser adicionada.
        listaCidadesSelecionadas = new ArrayList();

        // Atualiza as comboBox contendo a lista de cidades com base na leitura do arquivo.
        cadastrarRota.atualizarCidades(listaCidades);

        // Ao clicar em adicionar em "Cadastrar Trecho" o programa vai liberar acesso ao cadastro de produtos e criar uma nova rota e adicionar na tabela.
        cadastrarRota.getjButton_CadastrarTrecho().addActionListener(l -> cadastarNovaRota());

        // Ao clicar em "Remover rota seleciona..." ele vai checar em um metodo se ela esta selecionada e ira pedir confirmação para remover.
        cadastrarRota.getjButton_RemoverTrechoTabela().addActionListener(l -> removerUltimaLinha(tabelaRotasCadastradas, cadastrarRota));

        // Ao clicar em "Novo produto" o usuário será levado a uma interface que irá cadastar um novo produto e atrelar na rota selecionada.
        cadastrarRota.getjButton_CadastrarProduto().addActionListener(l -> cadastrarNovoProduto());
        
        // Ao clicar em "Finalizar cadastro" a rota em questão estará configurada e estará pronta para popular a proxima.
        cadastrarRota.getjButton_FinalizaCadastro().addActionListener(l -> finalizarCadastroProdutoRota(setProdutosTabela(listaProdutosCastrados)));

        cadastrarRota.setVisible(true);
    }
    

    // REQUISITO 3: Mostra os dados coletados pelos transportes cadastrados.
    private void exibirDadosEstatistica() {
        System.out.println("Abrindo formulário de dados estatísticos...");

        // Todo requisito 3 (27/03)
        
        dadosEstatistica.setVisible(true);
    }

    // 
    private void exibirRotaSimulada() {
        System.out.println("Abrindo resulado de simular rota...");

        // Desabilita o botão caso o usuário tente re-calulcar um novo trajeto.
        consultarRota.getCalcRota().setEnabled(false);

        // Popula o campo de distancia e custo de Km
        rotaCalculada.setJTextField_Distancia(distancia);
        rotaCalculada.setJTextField_KmRodado(caminhao.getCustoQuilometragem());

        // Determina o custo de transporte com um metodo na classe Caminhão. Talvez ficasse melhor em outro lugar...
        custoTransporte = caminhao.calcularCustoQuilometragem(distancia);

        // Exibe uma mensagem no terminal (talvez até um messageDialog) e atualiza o jTextField com o custo do transporte.
        System.out.println("O custo de transporte das duas cidades, utilizando um caminhão de " + caminhao.getPorte() + " porte, a distância é de " + distancia + " km e o custo será de R$ " + custoTransporte);
        rotaCalculada.setJTextField_CustoTrecho(custoTransporte);

        rotaCalculada.setVisible(true);
    }

    // Cada um dos métodos que comecem por 'exibir' abrem suas respectivas telas pelo ActionListner no initController.
    private void exibirTelaAbout() {
        System.out.println("Abrindo formulário do about...");
        about.setVisible(true);
    }
    
    // Método que a partir de duas ou mais cidades enviada pelo usário calcula a rota entre elas. 
    private void simularCustoTrecho() {
        // Descobre qual o porte de caminhão que foi selecionado na ComboBox.
        String porteCaminhao = String.valueOf(consultarRota.getjComboBox_PorteCaminhao().getSelectedItem());

        // Descobre qual as cidades escolhidas na ArrayList e popula uma string para ser enviado para o método que calcula distancia.
        cidadeOrigem = String.valueOf(consultarRota.getjComboBox_CidadeOrigem().getSelectedItem());
        cidadeDestino = String.valueOf(consultarRota.getjComboBox_CidadeDestino().getSelectedItem());

        // Baseado no tipo de porte selecionado o caminhão receberá os atributos fornecidos no enunciado (estático).
        switch (porteCaminhao) {
            case "Pequeno" -> {
                caminhao = new Caminhao(porteCaminhao, capacidadeCaminhoes[0], custosKm[0]);
            }
            case "Médio" -> {
                caminhao = new Caminhao(porteCaminhao, capacidadeCaminhoes[1], custosKm[1]);
            }
            case "Grande" -> {
                caminhao = new Caminhao(porteCaminhao, capacidadeCaminhoes[2], custosKm[2]);
            }
            default ->
                throw new AssertionError();
        }

        // Faz a chamada ao arquivo com o método para retornar a distância.
        distancia = Arquivo.descobirDistancia(cidadeOrigem, cidadeDestino);
        consultarRota.setJTextFieldDistancia(distancia);
    }

    // Método responsável por calcular o custo das rotas informadas no JForm CalularRota.
    private void calcularCustoTrechos(){
        // TODO: calcular trechos indeterminados.
        System.out.println("Sim");
    }
    
    // Método que, ao chamado, irá ler as informações no arquivo de maneira sequencial por linha e salva em um vetor.
    private void descobrirCidades(String nomeArquivo) {
        // Faz a chamada ao aquivo contendo o método que le o arquivo pelo nome informado.
        Arquivo.extrairDadosArquivo(nomeArquivo);

        // Popula as cidades baseado no arquivo informado.
        listaCidades = Arquivo.getCidades();

        String mensagem;
        if (listaCidades != null) {
            mensagem = "Todas as cidades no arquivo informado foram cadastradas com sucesso!";

            // Ativa todos os botões da interface principal do programa somente se não houver problemas com o arquivo.
            menu.getjButton_SimularRota().setEnabled(true);
            menu.getjButton_CalcularRota().setEnabled(true);
            menu.getjButton_DadosEstatistica().setEnabled(true);

            // Ativa todos os items do menu fundamental ao programa para o o usário.
            menu.getjMenuItem_SimularRota().setEnabled(true);
            menu.getjMenuItem_CalcularRota().setEnabled(true);
            menu.getjMenuItem_DadosEst().setEnabled(true);

        } else {
            mensagem = "ERRO! Não foram cadastradas nenhuma cidade, verefique o terminal para mais informações.";
        }
        System.out.println(mensagem);
        JOptionPane.showMessageDialog(menu, mensagem);
    }

    //
    private void cadastarNovaRota() {
        System.out.println("Usuário cadastrou um novo trecho para a tabela.");

        JComboBox selecaoCidadeOrigem = cadastrarRota.getjComboBox_CidadeOrigem();

        // Pega a tabela de trechos cada vez que for criado um novo cadastro.
        tabelaRotasCadastradas = cadastrarRota.getjTable_TabelaTrechos();

        // Determina a cidade de origem e destino inicail e depois a distancia entre elas.
        cidadeOrigem = String.valueOf(cadastrarRota.getjComboBox_CidadeOrigem().getSelectedItem());
        cidadeDestino = String.valueOf(cadastrarRota.getjComboBox_CidadeDestino().getSelectedItem());
        distancia = Arquivo.descobirDistancia(cidadeOrigem, cidadeDestino);

        // Verifica se as duas cidades não são iguais (redundancia) 
        if (cidadeOrigem.equals(cidadeDestino)) {
            JOptionPane.showMessageDialog(cadastrarRota, "Atenção! A cidade de origem é a mesma de destino.\nEsta insersão foi ignorada. Por favor, tente novamente.");
        } else {
            // Importa o modelo da tabela e salva nela o valor!
            DefaultTableModel model = ((DefaultTableModel) tabelaRotasCadastradas.getModel());
            model.addRow(new String[]{cidadeOrigem, cidadeDestino, Double.toString(distancia) + " Km"});// {CidadeA, CidadeB, Distancia}
            cadastrarRota.getjButton_RemoverTrechoTabela().setEnabled(true);

            // Como a cidade de origem sempre vai ser a destino da anterior, ela sera tratada automaticamente aqui.
            selecaoCidadeOrigem.setEnabled(false);
            selecaoCidadeOrigem.setSelectedItem(String.valueOf(cidadeDestino));

            //Pergunta se quer adicionar outra rota.
            int confirm = JOptionPane.showOptionDialog(cadastrarRota,
                    "Rota inserida com sucesso!\nDeseja cadastrar outra rota?",
                    "Adicionar outra rota", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == JOptionPane.YES_OPTION) {

            } else {
                // Tranca o usuário de modificar as rotas ja determinadas.
                cadastrarRota.getjButton_RemoverTrechoTabela().setEnabled(false);

                // Salva em uma lista todos os trajetos selecionado para referenciar depois aos produtos.
                for (int i = 0; i <= tabelaRotasCadastradas.getRowCount() - 1; i++) {
                    listaCidadesSelecionadas.add(tabelaRotasCadastradas.getValueAt(i, 0) + " - " + tabelaRotasCadastradas.getValueAt(i, 1)); // 0: Cidade Origem, 1: Cidade de destino.
                }
                
                // Coloca a primeira rota cadastrada como selecionada no cadastro de produto.
                cadastrarRota.getjLabel_RotaAtual().setText(String.valueOf(listaCidadesSelecionadas.get(0)));
                
                // Atualiza a lista de rotas para selecionar no cadastro de produtos.
                cadastrarRota.getjComboBox_ListaTransportes().setModel(new DefaultComboBoxModel<>((String[]) listaCidadesSelecionadas.toArray(String[]::new)));
                cadastrarRota.getjButton_CadastrarTrecho().setEnabled(false);

                //Permite o usuario a cadastrar produtos para o destino atual.
                cadastrarRota.getjComboBox_ListaTransportes().setEnabled(true);
                cadastrarRota.getjButton_CadastrarProduto().setEnabled(true);
                cadastrarRota.getjTable_TabelaProdutos().setEnabled(true);
            }
        }
    }

    //
    private void removerLinhaSelecionada(JTable tabela, Component contexto, int id) {
        if (id <= -1) {
            JOptionPane.showMessageDialog(contexto, "Erro! Por favor selecione uma linha na tabela para ser removida.");
        } else {
            int confirm = JOptionPane.showOptionDialog(contexto,
                    "Você tem certeza que quer remover esta linha?",
                    "Confirmar remoção de linha?", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Removendo linha selecionada...");
                ((DefaultTableModel) tabela.getModel()).removeRow(id);
            }
        }
    }

    //
    private void removerUltimaLinha(JTable tabela, Component contexto) {
        System.out.println(tabela.getRowCount());
        if (tabela.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(contexto, "Erro! Não há nenhuma linha para remover.");
        } else {
            int confirm = JOptionPane.showOptionDialog(contexto,
                    "Você tem certeza que quer remover a última rota?",
                    "Remover última rota?", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Removendo linha selecionada...");
                // Caso a linha deletada for a primeira o método permite que a cidade de origem seja selecionada novamente para evitar softlock.
                // Obs: Isso é horrivél mas eu não consegui pensar em nada melhor no momento
                if (tabela.getName().equals("tabelaTrechos")) {
                    System.out.println(tabela.getName());
                    if (tabela.getRowCount() <= 1) {
                        System.out.println("Evitando softlock, ativando a seleção da cidade de origem");
                        cadastrarRota.getjComboBox_CidadeOrigem().setEnabled(true);
                    } else {
                        cadastrarRota.getjComboBox_CidadeOrigem().setSelectedItem(tabela.getValueAt(tabela.getRowCount() - 2, 1));
                    }
                }
                ((DefaultTableModel) tabela.getModel()).removeRow(tabela.getRowCount() - 1);
            }
        }
    }

    // 
    private void cadastrarNovoProduto() {
        //TODO: formulário para cadastrar um novo produto, contendo nome peso e depois ele pede a quantidade. 
        System.out.println("Abrindo formulário de cadastro de produto...");
        
        cadastrarProduto.setVisible(true);
    }
    
    // Método que cadastra um novo produto, incluindo nome e peso. Depois pega a quantidade e calcula o peso total.
    private void cadastraProduto() {

        // Pega a tabela de trechos cada vez que for criado um novo cadastro.
        tabelaProdutosCadastrados = cadastrarRota.getjTable_TabelaProdutos();

        String nomeProduto;
        double pesoProduto;
        int qtdProduto;

        // Verifica se o nome inserido não está vazio antes de abrir tela de cadastro.
        if (cadastrarProduto.getjTextField_NomeProduto().getText().isBlank()) {
            System.out.println("Erro! Por favor insira um nome válido ao produto.");
            JOptionPane.showMessageDialog(cadastrarProduto, "Erro! Por favor insira um nome válido ao produto.");
        } else {
            // Faz uma pergunta para confirmar se o usuário quer cadastrar o produto.
            int confirm = JOptionPane.showOptionDialog(cadastrarProduto,
                    "Você tem certeza que quer cadastrar este produto para esta rota?",
                    "Confirmar cadastro produto", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Cadastrando produto...");

                nomeProduto = cadastrarProduto.getjTextField_NomeProduto().getText().toUpperCase();
                pesoProduto = (double) cadastrarProduto.getjSpinner_PesoProduto().getValue();
                qtdProduto = (int) cadastrarProduto.getjSpinner_Quantidade().getValue();
                
                produto = new Produto(nomeProduto, pesoProduto, qtdProduto);
                
                // Guarda o valor total do peso de cada rota para ser somado depois.
                listaPesoTotal.add(produto.calcularPesoTotal());

                listaProdutosCastrados.add(produto);
                
                tabelaProdutosCadastrados.setModel(setProdutosTabela(listaProdutosCastrados));
                
                System.out.println("Produto cadastrado com sucesso!");
                
                JOptionPane.showMessageDialog(cadastrarProduto, "Produto cadastrado com sucesso!");
                cadastrarRota.getjButton_RemoverProdutoTabela().setEnabled(true);
                cadastrarRota.getjButton_FinalizaCadastro().setEnabled(true);
            }
            cadastrarProduto.getjTextField_NomeProduto().setText("");
            cadastrarProduto.getjSpinner_PesoProduto().setValue(0.1);
            cadastrarProduto.getjSpinner_Quantidade().setValue(1);
            tabelaProdutosCadastrados.setDefaultEditor(Object.class, null);
        }
    }

    private DefaultTableModel setProdutosTabela(ArrayList<Produto> listaProdutos){
        
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Nome", "Peso", "Qtd.", "Total"});
        
        String[] row = new String[4];
        for(int i = 0; i < listaProdutos.size(); i++){
            row[0] = listaProdutos.get(i).getNome();
            row[1] = Double.toString(listaProdutos.get(i).getPeso())+" Kg.";
            row[2] = Integer.toString(listaProdutos.get(i).getQuantidade())+" Un.";
            row[3] = listaPesoTotal.get(i)+" Kg.";
            model.addRow(row);
        } 
        return model;
    }
    
    // DEFAZADO
    private void getProdutosTabela(int id, ArrayList<DefaultTableModel> model, JTable tabela){
        System.out.println(id);
        System.out.println("Recuperando informações da rota nº "+(id-1)+" com o modelo: '"+Arrays.toString(model.toArray())+"' na tabela "+tabela.getName());
        tabela.setModel(model.get(id));
    }  

    //
    private void finalizarCadastroProdutoRota(DefaultTableModel model) {
        System.out.println("Clicou no botão de finalizar a rota atual.");
        for (Produto listaProdutosCastrado : listaProdutosCastrados) {
            System.out.println(listaProdutosCastrado);
        }
        if(cadastrarRota.getjTable_TabelaProdutos().getModel().getRowCount() <= 0){
            System.out.println("Erro! Não existe produtos para esta rota.");
            JOptionPane.showMessageDialog(cadastrarRota, "Atenção! Não foi cadastrado nenhum produto, tente novamente.");     
        }else{
            trocarRotaSelecionada();
        }
        
    }

    //
    private void removeProduto(){ 
        int elementoSelcionado = tabelaProdutosCadastrados.getSelectedRow();
        if(elementoSelcionado >= 0){
            System.out.println("Elemento selecionado: "+tabelaProdutosCadastrados.getSelectedRow()+"/ Qtd elementos na tabela: "+tabelaProdutosCadastrados.getRowCount());
            listaProdutosCastrados.remove(elementoSelcionado);
        }
        removerLinhaSelecionada(tabelaProdutosCadastrados, cadastrarProduto, elementoSelcionado);  
    }
    
    //
    private void tentarRecuperarTabelaProdutos(int indexQuandoChamado) {
        JComboBox listaTransportes = cadastrarRota.getjComboBox_ListaTransportes();
        System.out.println(indexQuandoChamado);
        //getProdutosTabela(0, modeloProdutosRota, tabelaProdutosCadastrados);
        //System.out.println(tabelaProdutosCadastrados.getRowCount());
        // Caso não tenha nada cadastrado ( de uma rota )
        if(modeloProdutosRota == null){
            System.out.println("ERRO! O modelo de tabelas que contém produtos de determinada rota está vazio (não foi cadastrado nenhum produto e clicado em finalizar)");
            listaTransportes.setSelectedIndex(0);
        }else{
            System.out.println("Foi cadastrado produtos pelo botão finalizar, mas sera que...");
        }
    }
    
    // Sera chamada quando o usuario termiar de cadastrar os produtos na rota atual.
    private void trocarRotaSelecionada() {
        if (listaCidadesSelecionadas.size() >= 0) {
            // Possui rotas cadastradas:
            String mensagem = "Nenhuma das condições foi executada...";
            int pos = listaCidadesSelecionadas.size();
            if (pos <= 1) {
                // somente um elemento na linha
                mensagem = "A lista de produtos de todas as rotas foram concluídas!\nAgora é possível calcluar o transporte e descobrir o menor custo pela quilometragem rodada.";
                cadastrarRota.getjLabel_RotaAtual().setText("Nenhuma rota restante.");
                cadastrarRota.getjButton_CadastrarProduto().setEnabled(false);
                cadastrarRota.getjButton_CalcularTransporte().setEnabled(true);
            } else {
                // se tiver mais de um elemento (2), tem dois trajeto então tem que mostrar o primeiro e o proximo
                mensagem = "A lista de produtos para a rota " + listaCidadesSelecionadas.get(pos - pos) + " foram salvas com sucesso.\nAgora deverão ser inseridas os produtos da próxima rota (" + listaCidadesSelecionadas.get((pos - pos) + 1) + ").";
                listaCidadesSelecionadas.remove((pos - pos)); 
                // Coloca a última rota cadastrada como selecionada no cadastro de produto.
                cadastrarRota.getjLabel_RotaAtual().setText(String.valueOf(listaCidadesSelecionadas.get(pos - pos)));
            }

            // Mostra mensagem de acordo com o número de rotas restantes.
            JOptionPane.showMessageDialog(cadastrarRota, mensagem);
            
            // Limpa a tabela e a lista dos produtos calculados na rota anterior antes de seguir para a próxima.
            tabelaProdutosCadastrados.setModel(new DefaultTableModel());
            listaProdutosCastrados.clear();

            cadastrarRota.getjButton_RemoverProdutoTabela().setEnabled(false);
            cadastrarRota.getjButton_FinalizaCadastro().setEnabled(false);

        } else {
            System.out.println("ERRO! Não foi encontrada nenhuma rota selecionada, verifique se foi inserido alguma rota.");
        }
    }
}


/* TODO LIST:

 * Pegar a primeira cidade inserida e registrada com a ultima cidade inserida registrada para mostrar no jTextLabel no resultado.
 * Podar o codigo do antigo metodo de cadastrar produto.
 
 * BUG: Limpar todos os campos caso uma das interfaces de cada requisito for fechada (dispose não funciona >:c)
PRONTO * BUG: Quando cadastrando novo produto as vezes ele diz que tentou cadastrar em branco
PRONTO * BUG: O peso salvo depois que cadastra um produto sobreescreve o anterior. Possivel causa do problema no cadastra produto.
PRONTO * BUG: Ao remover um produto da linha selecionada (botão no produto) ele so remove lá e não na lista de produtos.

 * REQUISITO 2: Implementar método que calcula o menor preço de uma lista de rotas (somente o peso total sera informado) com os caminhoes. divide peso por modalidade (G -> M -> P)

 * REQUISITO 3: Fazer a interface dos dados de estatisticas
 * REQUISITO 3: Fazer a lógica (referencia de dados) para o requisito 3, dados estatistica.

 * BÔNUS: Fazer os testes jUnit para classe de arquivo, caminhão, resultado simulado e resultado composto (com os dois cenarios).
 * BÔNUS: Fazer a interface grafica do menu config, onde nele são recebidos e podem ser alterado os valores dos dados como peso de cada caminhao e o custo por quilometragem.

*/