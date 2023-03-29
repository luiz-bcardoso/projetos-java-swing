package Controller;

// Importa todas as telas do pacote View
import Model.Arquivo;
import Model.Caminhao;
import Model.Produto;
import Model.Transporte;
import View.*;
import java.awt.Component;
import java.util.ArrayList;
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
    private Caminhao caminhao;
    private Produto produto;

    // Strings, o nome do arquivo a ser lido, a cidade de orgiem e destino.
    private String nomeArquivo;
    private String cidadeOrigem;
    private String cidadeDestino;
    private String rotaIncialFinal;
    private String totalCaminhoesUtilizados;

    // Todas as cidades separadas, lida pelo arquivo CSV.
    private String[] listaCidades;

    // Variáveis que podem ser configuradas no menu "Config..."
    private Double[] capacidadeCaminhoes;
    private Double[] custosKm;

    // Estas listas armazenam informações locais ao invés do banco de dados ou arquivo.
    private ArrayList<String> listaCidadesSelecionadas;     // Contém todos os trajetos A-B que o usuário escolheu (Ex: PORTO ALEGRE - SÃO PAULO)
    private ArrayList<Double> listaPesosRota;               // Contém todos os pesos combinados de cada produto inserido em determinada rota.
    private ArrayList<Double> listaDistancias;              // Contém todas as distâncias entre as rotas inseridas no cadastro de transporte.   
    private ArrayList<Produto> listaProdutos;               // Contém todos os produtos cadastrados em determinada rota.
    private ArrayList<Caminhao> listaCaminhoes;             // Contém todos os caminhões que vão ser necessários para realizar determinado transporte.   
    private ArrayList<Transporte> listaTransportes;         // Contém todos os transportes realizado, ao final de cada transporte ele é cadastrado no relatório (requisito 3)

    // As duas tabelas do formulário de cadastro de transporte.
    private JTable tabelaRotasCadastradas;
    private JTable tabelaProdutosCadastrados;

    // Doubles, valor da distancia entre duas cidades, valor do custo de transporte simples, peso total da rota cadastrada (incluindo varios trajetos).
    private Double distancia;
    private Double custoTransporte;
    private Double pesoRotasTotal;

    // Doubles, armazendo o valor do resultado das rotas cadastradas do requisito 3.
    private Double distanciaTotal;
    private Double custoTotalRotas;
    private Double custoUnitarioRotas;
    private Double custoModalidadeCombinada;
    private Double distribuirPesoCaminhao;

    // Lista de caminhões para adicionar na lista quando for separar carga.
    Caminhao caminhaoP;
    Caminhao caminhaoM;
    Caminhao caminhaoG;

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

        // FUNCIONALIDADES DO REQUISITO 3:  
        // CADASTRO DE ROTAS:
        //
        //ActionListener caso clicar em CADASTRAR ROTA o programa vai liberar acesso ao cadastro de produtos e criar uma nova rota e adicionar na tabela.
        cadastrarRota.getjButton_CadastrarTrecho().addActionListener(l -> cadastarNovaRota());
        //ActionListener caso clicar em REMOVER ROTA ele vai checar em um metodo se ela esta selecionada e ira pedir confirmação para remover.
        cadastrarRota.getjButton_RemoverTrechoTabela().addActionListener(l -> removerUltimaLinha(tabelaRotasCadastradas, cadastrarRota));

        // CADASTRO DE PRODUTO:
        //
        //ActionListener caso o usario clicar em CADASTRAR PRODUTO sera aberto um form para cadastro.
        cadastrarProduto.getjButton_CadastarProduto().addActionListener(e -> cadastraProduto());
        //ActionListener caso o usuario clicar em REMOVER PRODUTO (linha na tabela produto)
        cadastrarRota.getjButton_RemoverProdutoTabela().addActionListener(e -> removeProduto());
        //ActionListener caso o usuário clicar em FINALIZAR PRODUTO com os cadastros (termina uma das rotas)
        cadastrarRota.getjButton_FinalizaCadastro().addActionListener(e -> finalizarCadastroProdutoRota());

        // CALCULAR TRANSPORTE:
        //
        //ActionListener caso o usuário clicar em CALCULAR o transporte com as rotas e o peso final.
        cadastrarRota.getjButton_CalcularTransporte().addActionListener(e -> calcularRotaTransporte());
        //ActionListener caso o usuário clicar em RECOMEÇAR o cálculo e começar um novo transporte.
        cadastrarRota.getjButton_CalcularNovaRota().addActionListener(e -> iniciaTransporte());

    }

    private void inicializaVariaveis() {

        // Inicializando variáveis do enunciado exercício:
        custosKm = new Double[]{4.87, 11.92, 27.44};
        capacidadeCaminhoes = new Double[]{1000.0, 4000.0, 10 - 00.0};

        // Inicializando variáveis ArrayLists:
        listaCidadesSelecionadas = new ArrayList();
        listaPesosRota = new ArrayList();
        listaDistancias = new ArrayList();
        listaProdutos = new ArrayList();
        listaCaminhoes = new ArrayList();
        listaTransportes = new ArrayList<>();

        // Inicializando variáveis Strings:
        cidadeOrigem = new String();
        cidadeDestino = new String();
        rotaIncialFinal = new String();

        // Incializando variáveis de peso e distancia total:
        pesoRotasTotal = 0.0;
        distanciaTotal = 0.0;

        // Inicializando variáveis do resultado do transporte:
        distanciaTotal = 0.0;
        pesoRotasTotal = 0.0;
        custoTotalRotas = 0.0;
        custoUnitarioRotas = 0.0;
        distribuirPesoCaminhao = 0.0;
        custoModalidadeCombinada = 0.0;

    }

    // REQUISITO 1: Simula o custo de uma rota simples entre duas cidades a baseado na modalidade de transporte (P, M, G)
    private void exibirSimularRota() {
        System.out.println("Abrindo formulário de simular rota...");

        // Atualiza as comboBox contendo a lista de cidades com base na leitura do arquivo.
        consultarRota.atualizarCidades(listaCidades);

        consultarRota.getCalcRota().addActionListener(e -> exibirRotaSimulada());

        consultarRota.getCalcDestino().addActionListener(e -> simularCustoTrecho());

        consultarRota.setVisible(true);
    }

    // REQUISITO 2: Faz o cálculo de 'n' rotas informadas pelo usuario e com base no peso determina o menor custo para Km rodado para 'n' caminhões. 
    private void exibirCalcularRota() {
        System.out.println("Abrindo formulário de calular rota...");

        // Atualiza as comboBox contendo a lista de cidades com base na leitura do arquivo.
        cadastrarRota.atualizarCidades(listaCidades);

        // Ao clicar em "Novo produto" o usuário será levado a uma interface que irá cadastar um novo produto e atrelar na rota selecionada.
        cadastrarRota.getjButton_CadastrarProduto().addActionListener(l -> exibirCadastroProduto());

        cadastrarRota.setVisible(true);

        // Incializa todas as variáveis.
        iniciaTransporte();
    }

    // REQUISITO 3: Mostra os dados coletados pelos transportes cadastrados.
    private void exibirDadosEstatistica() {
        System.out.println("Abrindo formulário de dados estatísticos...");

        atualizarDadosEstistica();

        dadosEstatistica.setVisible(true);
    }

    // TROCAR TELA:
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

    // TROCAR TELA:
    private void exibirCadastroProduto() {
        //TODO: formulário para cadastrar um novo produto, contendo nome peso e depois ele pede a quantidade. 
        System.out.println("Abrindo formulário de cadastro de produto...");

        cadastrarProduto.setVisible(true);
    }

    // TROCAR TELA: Exibe o form contendo informações do software como a versão, data da versão, quem desenvolveu e porque desenvolveu.
    private void exibirTelaAbout() {
        System.out.println("Abrindo formulário do about...");
        about.setVisible(true);
    }

    // REQUISITO 1: Método que a partir de duas ou mais cidades enviada pelo usário calcula a rota entre elas. 
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

        // Determina a cidade de origem e destino inicial.
        cidadeOrigem = String.valueOf(cadastrarRota.getjComboBox_CidadeOrigem().getSelectedItem());
        cidadeDestino = String.valueOf(cadastrarRota.getjComboBox_CidadeDestino().getSelectedItem());

        // Determina a distancia entre elas e guarda na lista (pode ser removida depois)
        distancia = Arquivo.descobirDistancia(cidadeOrigem, cidadeDestino);
        listaDistancias.add(distancia);

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
            if (confirm == JOptionPane.NO_OPTION) {
                // Tranca o usuário de modificar as rotas ja determinadas.
                cadastrarRota.getjButton_RemoverTrechoTabela().setEnabled(false);

                // Salva em uma lista todos os trajetos selecionado para referenciar depois aos produtos.
                for (int i = 0; i <= tabelaRotasCadastradas.getRowCount() - 1; i++) {
                    listaCidadesSelecionadas.add(tabelaRotasCadastradas.getValueAt(i, 0) + " - " + tabelaRotasCadastradas.getValueAt(i, 1)); // 0: Cidade Origem, 1: Cidade de destino.
                }

                // Descobrir distância total do trajeto:
                for (Double distancias : listaDistancias) {
                    distanciaTotal += distancias;
                }

                // Mostra a distância total das rotas:
                System.out.println("A distancia total entre todas as rotas é de " + distanciaTotal + " Kms.");
                JOptionPane.showMessageDialog(cadastrarRota, "A distancia total entre todas as rotas é de " + distanciaTotal + " Kms.");

                // Coloca a primeira rota cadastrada como selecionada no cadastro de produto.
                cadastrarRota.getjLabel_RotaAtual().setText(String.valueOf(listaCidadesSelecionadas.get(0)));

                // Atualiza a lista de rotas para selecionar no cadastro de produtos.
                cadastrarRota.getjButton_CadastrarTrecho().setEnabled(false);

                // Permite o usuario a cadastrar produtos para o destino atual.
                cadastrarRota.getjButton_CadastrarProduto().setEnabled(true);
                cadastrarRota.getjTable_TabelaProdutos().setEnabled(true);
            }
        }
    }

    // Método que remove a ultima linha de uma tabela informada, idependente de qual indexador está selecionada.
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
                System.out.println("Removendo última rota cadaststrada...");
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
                listaDistancias.remove(tabela.getRowCount() - 1);
                ((DefaultTableModel) tabela.getModel()).removeRow(tabela.getRowCount() - 1);
            }
        }
    }

    // Método que cadastra um novo produto, incluindo nome e peso. Depois pega a quantidade e calcula o peso total.
    private void cadastraProduto() {

        // Pega a tabela de trechos cada vez que for criado um novo cadastro.
        tabelaProdutosCadastrados = cadastrarRota.getjTable_TabelaProdutos();

        String nomeProduto;
        double pesoProduto;
        double pesoTotal;
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
                pesoTotal = produto.calcularPesoTotal();
                listaPesosRota.add(pesoTotal);

                listaProdutos.add(produto);

                System.out.println("Produto cadastrado com sucesso!");
                JOptionPane.showMessageDialog(cadastrarProduto, "Produto cadastrado com sucesso!");

                // Permite o usuario remover um produto, finalizar o cadastro;
                cadastrarRota.getjButton_RemoverProdutoTabela().setEnabled(true);
                cadastrarRota.getjButton_FinalizaCadastro().setEnabled(true);
            }

            // Sempre que terminar de cadastrar um produto as suas variáveis serão redefinida.
            cadastrarProduto.getjTextField_NomeProduto().setText("");
            cadastrarProduto.getjSpinner_PesoProduto().setValue(0.1);
            cadastrarProduto.getjSpinner_Quantidade().setValue(1);

            // Deixa a tabela de produtos não-editável.
            tabelaProdutosCadastrados.setDefaultEditor(Object.class, null);

            // Popula a tabela com os produtos cadastrado na lista (Tipo banco de dados)
            setProdutosTabela();
        }
    }

    //
    private void finalizarCadastroProdutoRota() {
        System.out.println("Cadastrando peso e distancia finais para produtos e rota...");

        // Checa se a quantidade de linhas (produtos cadastrados) na tabela é menor ou igual a 0
        if (cadastrarRota.getjTable_TabelaProdutos().getModel().getRowCount() <= 0) {
            System.out.println("ERRO! Não existem produtos cadastrados para esta rota.");
            JOptionPane.showMessageDialog(cadastrarRota, "Atenção! Não foi cadastrado nenhum produto, tente novamente.");
        } else {

            Double pesoTotalAtual = 0.0;

            // Descobrir peso total dos produtos de cada rota:
            for (Double pesos : listaPesosRota) {
                pesoRotasTotal += pesos;
                pesoTotalAtual += pesos;
            }

            // Descobrir peso total dos produtos da rota atual:
            System.out.println("O peso total para está rota é de " + pesoTotalAtual + " Kgs.");
            JOptionPane.showMessageDialog(cadastrarProduto, "O peso total para está rota é de " + pesoTotalAtual + " Kgs.");

            // Limpa a tabela de produtos e pesos para a nova rota não pegar valores errados.
            listaPesosRota.clear();
            listaProdutos.clear();

            trocarRotaSelecionada();
        }

    }

    // Popula a tabela seleciona com os produtos cadastrados e seus pesos combinados.
    private void setProdutosTabela() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Nome", "Peso", "Qtd.", "Total"});

        String[] row = new String[4];

        for (int i = 0; i < listaProdutos.size(); i++) {

            /* Nome  */ row[0] = listaProdutos.get(i).getNome();
            /* Peso  */ row[1] = Double.toString(listaProdutos.get(i).getPeso()) + " Kg.";
            /* Qtd   */ row[2] = Integer.toString(listaProdutos.get(i).getQuantidade()) + " Un.";
            /* Total */ row[3] = listaPesosRota.get(i) + " Kg.";

            model.addRow(row);
        }

        tabelaProdutosCadastrados.setModel(model);
    }

    // Método que remove a linha seleciona na tabela de produtos.
    private void removeProduto() {
        int elementoSelcionado = tabelaProdutosCadastrados.getSelectedRow();

        if (elementoSelcionado <= -1) {
            JOptionPane.showMessageDialog(cadastrarProduto, "Erro! Por favor selecione uma linha na tabela para ser removida.");
        } else {
            // Pergunta ao usuário se ele realmente quer remover a linha selecionada.
            int confirm = JOptionPane.showOptionDialog(cadastrarProduto,
                    "Você tem certeza que quer remover este produto?",
                    "Confirmar remoção do produto?", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            // Caso SIM: Remove a linha da tabela e depois o produto da lista de produto.
            if (confirm == JOptionPane.YES_OPTION) {
                // Verifica se o produto cadastrado é o unico, então trava o botão de cadastro de rota.
                if (tabelaProdutosCadastrados.getRowCount() <= 1) {
                    System.out.println("O Único produto na lista será removido, não e mais possível realizar o cadastro da rota.");
                    cadastrarRota.getjButton_FinalizaCadastro().setEnabled(false);
                }
                System.out.println("Produto removido: " + listaProdutos.toString() + " seu peso total era " + listaPesosRota.get(elementoSelcionado));
                listaProdutos.remove(elementoSelcionado);
                listaPesosRota.remove(elementoSelcionado);
                ((DefaultTableModel) tabelaProdutosCadastrados.getModel()).removeRow(elementoSelcionado);
                System.out.println("Produto selecionado foi removido com sucesso.");
                JOptionPane.showMessageDialog(cadastrarRota, "Produto selecionado foi removido com sucesso.");
            }
        }
    }

    // Sera chamada quando o usuario termiar de cadastrar os produtos na rota atual.
    private void trocarRotaSelecionada() {
        if (listaCidadesSelecionadas.size() >= 0) {
            // Possui rotas cadastradas:
            String mensagem;
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

            // Limpa o modelo da tabela anterior antes de seguir para a próxima.
            tabelaProdutosCadastrados.setModel(new DefaultTableModel());

            cadastrarRota.getjButton_RemoverProdutoTabela().setEnabled(false);
            cadastrarRota.getjButton_FinalizaCadastro().setEnabled(false);

        } else {
            System.out.println("ERRO! Não foi encontrada nenhuma rota selecionada, verifique se foi inserido alguma rota.");
        }
    }

    // Foi clicado no botão de calcular rota (endgame)
    private void calcularRotaTransporte() {
        System.out.println("Calculando custo de transporte para as rotas e peso informado...");
        distribuirPesoCaminhao = pesoRotasTotal;
        totalCaminhoesUtilizados = "";

        calculaTransporte();

        cadastrarRota.getjTextField_NomeRota().setText(rotaIncialFinal);
        cadastrarRota.getjTextField_DistanciaTotal().setText(distanciaTotal + " Km.");
        cadastrarRota.getjTextField_PesoTotal().setText(pesoRotasTotal + " Kg.");
        cadastrarRota.getjTextField_ValorTotal().setText("R$ " + custoTotalRotas);
        cadastrarRota.getjTextField_ValorUnitMedio().setText("R$ " + custoUnitarioRotas);
        cadastrarRota.getjTextArea_ListaCaminhoes().setText(totalCaminhoesUtilizados);

        JOptionPane.showMessageDialog(cadastrarRota, "Rota de transporte calculada com sucesso!\nClique no botão 'Calcular Nova Rota' abaixo para recomeçar com uma novo transporte.");
        cadastrarRota.getjButton_CalcularNovaRota().setEnabled(true);
    }

    private void calculaTransporte() {
        // Pega o nome da primeira e ultima cidade de maneira bem horrosa.
        rotaIncialFinal = cadastrarRota.getjTable_TabelaTrechos().getValueAt(0, 0) + " até " + cadastrarRota.getjComboBox_CidadeOrigem().getItemAt(cadastrarRota.getjComboBox_CidadeOrigem().getSelectedIndex());

        // Chama o método que vai dividir a carga nos caminhões e vai retornar em uma lista.
        while (distribuirPesoCaminhao > 0.0) {
            System.out.println("Peso ainda não é 0 ou negativo, alocando restante a mais um caminhão...");
            alocarPesoCaminhao();
        }

        ArrayList<Double> custoTrecho = new ArrayList<>();
        double custoKm = 0;
        double custoProduto = 0;
        double custoTotalTrecho = 0;
        // custo modalidade = custo modalidade combinada.

        System.out.println("Terminado de dividir peso entre os caminhões.");
        for (Caminhao caminhoes : listaCaminhoes) {
            custoModalidadeCombinada += custoModalidadeCombinada + caminhoes.getCustoQuilometragem();
            for (int i = 0; i < listaCidadesSelecionadas.size(); i++) {
                custoTrecho.add(listaDistancias.get(i)*custoModalidadeCombinada); // Atencão nao atuliza o custo da modalidade ainda, permanece o mesmo independete se foi descarregado ou nao.
            }
            totalCaminhoesUtilizados += "\n------------------------------------ \n"
                    + " Caminhão de " + caminhoes.getPorte().toUpperCase() + " porte.\n"
                    + " Carregando até " + caminhoes.getCarga() + " Kg.\n"
                    + " Com custo de R$ " + caminhoes.getCustoQuilometragem() + " / Km\n"
                    + "------------------------------------\n";
        }

        System.out.println("-- DEBUG MODE --");
        System.out.println("custoKmCombinado : " + custoModalidadeCombinada);
        System.out.println("listaCidadesSelecionadas (nº de trechos) : " + listaCidadesSelecionadas.size());
        // todo achar o lugar que troca de rota e tabela e atualizar a modalidade do caminhao. mas como ele é chamado antes de cadastrar a rota tem que improvisar de algum jeito.

        JOptionPane.showMessageDialog(cadastrarRota, "O método de calcular rota está incompleto.\n Não é possível exibir o custo de mais de uma rota e nem cadastrar seu relatório.", "Erro Método Incompleto!", JOptionPane.ERROR_MESSAGE);
        
    }

    private void iniciaTransporte() {
        System.out.println("Limpando dados e começando um novo transporte...");

        inicializaVariaveis();

        caminhaoG = new Caminhao("Grande", capacidadeCaminhoes[2], custosKm[2]);
        caminhaoM = new Caminhao("Médio", capacidadeCaminhoes[1], custosKm[1]);
        caminhaoP = new Caminhao("Pequeno", capacidadeCaminhoes[0], custosKm[0]);

        cadastrarRota.getjTextField_NomeRota().setText("??? até ???");
        cadastrarRota.getjTextField_DistanciaTotal().setText("??? Km.");
        cadastrarRota.getjTextField_PesoTotal().setText("??? Kg.");
        cadastrarRota.getjTextField_ValorTotal().setText("R$ ???");
        cadastrarRota.getjTextField_ValorUnitMedio().setText("R$ ???");

        JOptionPane.showMessageDialog(cadastrarRota, "Um novo cadastro de transporte foi iniciado, por favor insira os trechos para começar.");
    }

    // Método que tenta encontrar a melhor distribuição de carga, faz 9 comparações (ruim) mas funciona.
    private void alocarPesoCaminhao() {
        // Caso o distribuirPesoCaminhao for igual a 10.000Kg:
        if (distribuirPesoCaminhao.equals(10000.0)) {
            distribuirPesoCaminhao = distribuirPesoCaminhao - 10000;

            listaCaminhoes.add(caminhaoG);
            System.out.println("Adicionado novo CAMINHÃO GRANDE | Peso: " + distribuirPesoCaminhao);
        }
        // Caso o distribuirPesoCaminhao for igual a 4.000Kg:
        if (distribuirPesoCaminhao.equals(4000.0)) {
            distribuirPesoCaminhao = distribuirPesoCaminhao - 4000;
            listaCaminhoes.add(caminhaoM);
            System.out.println("Adicionado novo CAMINHÃO MÉDIO | Peso: " + distribuirPesoCaminhao);
        }
        // Caso o distribuirPesoCaminhao for igual a 1.000Kg:
        if (distribuirPesoCaminhao.equals(1000.0)) {
            distribuirPesoCaminhao = distribuirPesoCaminhao - 1000;
            listaCaminhoes.add(caminhaoP);
            System.out.println("Adicionado novo CAMINHÃO PEQUENO | Peso: " + distribuirPesoCaminhao);
        }

        // Se o montante couber no caminhão GRANDE:
        if (distribuirPesoCaminhao / 10000 > 1) {
            distribuirPesoCaminhao = distribuirPesoCaminhao - 10000;
            listaCaminhoes.add(caminhaoG);
            System.out.println("Adicionado novo CAMINHÃO GRANDE | Peso: " + distribuirPesoCaminhao);
        } else if (distribuirPesoCaminhao / 4000 > 1) {
            distribuirPesoCaminhao = distribuirPesoCaminhao - 4000;
            listaCaminhoes.add(caminhaoM);
            System.out.println("Adicionado novo CAMINHÃO MÉDIO | Peso: " + distribuirPesoCaminhao);
            //System.out.println("cG: " + cG + " cM: " + cM + " cP: " + cP);
            // distribuirPesoCaminhao cabe no 4000?
        } else {
            if (distribuirPesoCaminhao < 4000 && distribuirPesoCaminhao > 0) {
                distribuirPesoCaminhao = distribuirPesoCaminhao - 4000;
                listaCaminhoes.add(caminhaoM);
                System.out.println("Adicionado novo CAMINHÃO MÉDIO | Peso: " + distribuirPesoCaminhao);
                //System.out.println("cG: " + cG + " cM: " + cM + " cP: " + cP);
            }
            if (distribuirPesoCaminhao < 1000 && distribuirPesoCaminhao > 0) {
                distribuirPesoCaminhao = distribuirPesoCaminhao - 1000;
                listaCaminhoes.add(caminhaoP);
                System.out.println("Adicionado novo CAMINHÃO PEQUENO | Peso: " + distribuirPesoCaminhao);
                //System.out.println("cG: " + cG + " cM: " + cM + " cP: " + cP);
            }
            // se couber no pequeno...
            if (distribuirPesoCaminhao / 1000 > 1) {
                distribuirPesoCaminhao = distribuirPesoCaminhao - 1000;
                listaCaminhoes.add(caminhaoP);
                System.out.println("Adicionado novo CAMINHÃO PEQUENO | Peso: " + distribuirPesoCaminhao);
                //System.out.println("cG: " + cG + " cM: " + cM + " cP: " + cP);
            }
            if (distribuirPesoCaminhao < 1000 && distribuirPesoCaminhao > 0) {
                listaCaminhoes.add(caminhaoP);
                distribuirPesoCaminhao = distribuirPesoCaminhao - 1000;
                System.out.println("o restante colocar em um novo CAMINHÃO PEQUENO | Peso: :" + distribuirPesoCaminhao);

            }
        }
    }

    private void atualizarDadosEstistica() {
        JTable tabelaRelatorio = dadosEstatistica.getjTable_Transportes();
        if (listaTransportes.size() <= 0) {
            System.out.println("Erro sem produtos cadastrados.");
            JOptionPane.showMessageDialog(menu, "Atenção! Não existem produtos cadastrados no momento, tente cadastar algúm pelo segundo botão e tente novamente!");
        } else {
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"Nome Rota", "Custo Total", "Custo Trecho", "Custo / Km", "Custo / Produto", "Custo Total Trecho", "Custo Modalidade", "Numero Veículos", "Número Produtos"});

            String[] row = new String[9];

            for (int i = 0; i < listaTransportes.size(); i++) {

                row[0] = listaTransportes.get(i).getNomeRota();
                row[1] = Double.toString(listaTransportes.get(i).getCustoTotal());
                row[2] = Double.toString(listaTransportes.get(i).getCustoPorTecho());
                row[3] = Double.toString(listaTransportes.get(i).getCustoKm());
                row[4] = Double.toString(listaTransportes.get(i).getCustoProduto());
                row[5] = Double.toString(listaTransportes.get(i).getCustoTotalTrecho());
                row[6] = Double.toString(listaTransportes.get(i).getCustoModalidade());
                row[7] = Double.toString(listaTransportes.get(i).getNumeroVeiculos());
                row[8] = Double.toString(listaTransportes.get(i).getNumeroProdutos());

                model.addRow(row);
            }

            tabelaRelatorio.setModel(model);
        }
    }

}

/* TODO LIST:

 pronto* Pegar a primeira cidade inserida e registrada com a ultima cidade inserida registrada para mostrar no jTextLabel no resultado.
 pronto* Podar o codigo do antigo metodo de cadastrar produto.
 
 * BUG: Limpar todos os campos caso uma das interfaces de cada requisito for fechada (dispose não funciona >:c)
PRONTO * BUG: Quando cadastrando novo produto as vezes ele diz que tentou cadastrar em branco
PRONTO * BUG: O peso salvo depois que cadastra um produto sobreescreve o anterior. Possivel causa do problema no cadastra produto.
PRONTO * BUG: Ao remover um produto da linha selecionada (botão no produto) ele so remove lá e não na lista de produtos.

 * REQUISITO 2: Implementar método que calcula o menor preço de uma lista de rotas (somente o peso total sera informado) com os caminhoes. divide peso por modalidade (G -> M -> P)

 pronto* REQUISITO 3: Fazer a interface dos dados de estatisticas
 pronto* REQUISITO 3: Fazer a lógica (referencia de dados) para o requisito 3, dados estatistica.

 * BÔNUS: Fazer os testes jUnit para classe de arquivo, caminhão, resultado simulado e resultado composto (com os dois cenarios).
sem tempo* BÔNUS: Fazer a interface grafica do menu config, onde nele são recebidos e podem ser alterado os valores dos dados como peso de cada caminhao e o custo por quilometragem.

 */
