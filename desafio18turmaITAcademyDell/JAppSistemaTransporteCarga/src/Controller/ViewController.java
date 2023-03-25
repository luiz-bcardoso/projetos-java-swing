package Controller;

// Importa todas as telas do pacote View
import View.*;

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

    // Construtor das telas JForm;
    public ViewController(JFrameMenu menu, JFrameAbout about, JFrameCalcRota calcularRota) {
        this.menu = menu;
        this.about = about;
        this.calcularRota = calcularRota;
    }

    // Controlador das telas pelo ActionListener
    public void initController(){
        
        // Inicia a tela padrão do programa.
        menu.setVisible(true);
        
        // ActionListner caso o usuário clique no item do menu "About..."
        menu.getJMenuItemAbout().addActionListener(e -> exibirTelaAbout());
        
        // ActionListner caso o usuário clique no botão OU item do menu "Calcular Rota..."
        menu.getJMenuCalcRota().addActionListener(e -> calcularRota());
        menu.getJButtonCalcRota().addActionListener(e -> calcularRota());
    }
    
    // Exibe o JFrameAbout, chamado pelo initController
    private void exibirTelaAbout() {
        System.out.println("Abrindo form do about...");
        about.setVisible(true);
    }

    private void calcularRota() {
        System.out.println("Abrindo form de calular rota...");
        calcularRota.setVisible(true);
    }

}
