package Controller;

// Importa todas as telas do pacote View
import View.*;

/**
 *
 * @author Luiz Batista Cardoso
 *      24 mar 2023
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Seja bem-vindo ao Sistema de simulação de transporte interestadual (24/03/23).");
        ViewController cl = new ViewController(new JFrameMenu(), new JFrameAbout(), new JFrameCalcRota());
        
        cl.initController();
        
    }
}
