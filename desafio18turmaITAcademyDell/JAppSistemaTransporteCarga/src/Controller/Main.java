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
         System.out.println("Seja bem-vindo ao Sistema de simulação de transporte interestadual versão 0.3 (27/03/23).");
        ViewController cl = new ViewController(new JFrameMenu(), new JFrameAbout(), new JFrameConsultarTransporte(), new JFrameCadastrarTransporte(), new JFrameDadosEstatistica(), new JFrameRotaCalculada(), new JFrameCadastrarProduto());
        
        cl.initController();
        
    }
}
