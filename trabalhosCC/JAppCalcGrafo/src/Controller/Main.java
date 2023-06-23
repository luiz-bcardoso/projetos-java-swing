package Controller;

import View.JFrameAbout;
import View.JFrameGrafo;
import View.JFrameMenu;

/**
 *
 * @author laboratorio
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Seja bem-vindo ao otmizador de grafos para fluxos de rede.");
        ViewController c1 = new ViewController(new JFrameMenu(),new JFrameAbout(),new JFrameGrafo());
        c1.initVariables();
        c1.initController();    
    }
}
