package Controller;

// Importa todas as telas do pacote View
import Model.Arquivo;
import Model.Caminhao;
import View.*;
import java.util.ArrayList;


/**
 *
 * @author Luiz Batista Cardoso
 *      22 mar 2023
 */

// Esta classe é responsável por fazer o controle das telas e executar o meio termo dos métodos.
public class ViewController {
    
    // Declaração das telas JFrom;
    private final JFrameMenu menu;
    private final JFrameAbout about;
    private final JFrameCalcRota calcularRota;
    private final JFrameRotaCalculada rotaCalculada;
    
    private Caminhao c1 = null;
    
    private String[] listaCidades;
    
    private Double distancia;
    private Double custoTransporte;
    

    // Construtor das telas JForm;
    public ViewController(JFrameMenu menu, JFrameAbout about, JFrameCalcRota calcularRota, JFrameRotaCalculada rotaCalculada){           
        this.menu = menu;
        this.about = about;
        this.calcularRota = calcularRota;
        this.rotaCalculada = rotaCalculada;
    }

    // Controlador das telas pelo ActionListener
    public void initController() {
        // Inicia a tela padrão do programa.
        menu.setVisible(true);
        
        // ActionListner caso o usuário clique no item do menu "About..."
        menu.getJMenuItemAbout().addActionListener(e -> exibirTelaAbout());
        
        // ActionListner caso o usuário clique no botão OU item do menu "Calcular Rota..."
        menu.getJMenuCalcRota().addActionListener(e -> exibirCalcularRota());
        menu.getJButtonCalcRota().addActionListener(e -> exibirCalcularRota());
        
        calcularRota.getCalcRota().addActionListener(e->exibirRotaCalculada());
        calcularRota.getCalcDestino().addActionListener(e->calcularDistancia());
    }
    
    // Exibe o JFrameAbout, chamado pelo initController
    private void exibirTelaAbout() {
        System.out.println("Abrindo formulário do about...");
        about.setVisible(true);
    }

    private void exibirCalcularRota() {
        System.out.println("Abrindo formulário de calular rota...");
        
        
        // Faz a chamada ao aquivo contendo o método que le o arquivo pelo nome informado.
        Arquivo.extrairDadosArquivo("DNIT-Distancias.csv"); // TODO: Generalizar nome do arquivo nas configurações.
        
        // Popula as cidades baseado no arquivo informado.
        listaCidades = Arquivo.getCidades();
        for(int i=0; i==listaCidades.length; i++){
            System.out.println(listaCidades[i]);
        }
        // Atualiza as comboBox contendo a lista de cidades com base na leitura do arquivo.
        calcularRota.atualizarCidades(listaCidades);

        calcularRota.setVisible(true);   
    }
    
    private void exibirRotaCalculada() {
        System.out.println("Abrindo formulário de calular rota...");
        
        // Desabilita o botão caso o usuário tente re-calulcar um novo trajeto.
        calcularRota.getCalcRota().setEnabled(false);
        
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
    
    // Método que a partir de duas ou mais cidades enviada pelo usário calcula a rota entre elas. 
    private void calcularDistancia(){

        String cidadeA;
        String cidadeB;
        
        // Descobre qual o porte de caminhão que foi selecionado na ComboBox.
        String porteCaminhao = String.valueOf(calcularRota.getjComboBox_PorteCaminhao().getSelectedItem());

        // Descobre qual as cidades escolhidas na ArrayList e popula uma string para ser enviado para o método que calcula distancia.
        cidadeA = String.valueOf(calcularRota.getjComboBox_CidadeOrigem().getSelectedItem());
        cidadeB = String.valueOf(calcularRota.getjComboBox_CidadeDestino().getSelectedItem());
        
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
        distancia =  Arquivo.descobirDistancia(cidadeA, cidadeB); 
        calcularRota.setJTextFieldDistancia(distancia);
    }
    
 /*    
    private static ArrayList<ArrayList<Double>> biDemArrList = new ArrayList<ArrayList<Double>>();
   
    private void dicionario(){
        Map<String, String> map = new HashMap<>();
        map.put("santa maria", String.valueOf(0));
        System.out.println(map.get("santa maria"));
        ArrayList<Double> distanciaCidade = new ArrayList<>();
        double x = 10+Integer.parseInt(map.get("santa maria"));
        distanciaCidade.add(x);
        //System.out.println(distanciaCidade.get(Integer.parseInt(map.get("santa maria"))));
        biDemArrList.add(distanciaCidade);   
    }
*/

    
}
