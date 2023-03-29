/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author newik
 */
public class ArquivoTest {
    
    public ArquivoTest() {
    }

    // Testando se na leitura de arquivo ele esta pegando as distancias corretas para os cenários propostos.
    @Test
    public void descobrirDistancaEntrePortoAlegre_SaoPaulo() {
        Arquivo.extrairDadosArquivo("DNIT-Distancias.csv");
        double resultado = Arquivo.descobirDistancia("PORTO ALEGRE", "SAO PAULO");
        assertEquals(resultado, 1109, 2*Math.ulp(resultado));
    }
    
    @Test
    public void descobrirDistancaEntrePortoAlegre_Florianopolis() {
        Arquivo.extrairDadosArquivo("DNIT-Distancias.csv");
        double resultado = Arquivo.descobirDistancia("PORTO ALEGRE", "FLORIANOPOLIS");
        assertEquals(resultado, 476, 2*Math.ulp(resultado));
    }
    
    @Test
    public void descobrirDistancaEntreFlorianopolis_Curitiba() {
        Arquivo.extrairDadosArquivo("DNIT-Distancias.csv");
        double resultado = Arquivo.descobirDistancia("FLORIANOPOLIS", "CURITIBA");
        assertEquals(resultado, 300, 2*Math.ulp(resultado));
    }
    
    @Test
    public void descobrirDistancaEntreMaceio_Goania() {
        Arquivo.extrairDadosArquivo("DNIT-Distancias.csv");
        double resultado = Arquivo.descobirDistancia("MACEIO", "GOIANIA");
        assertEquals(resultado, 2105, 2*Math.ulp(resultado));
    }
    
    @Test
    public void descobrirDistancaEntreGoiania_SaoPaulo() {
        Arquivo.extrairDadosArquivo("DNIT-Distancias.csv");
        double resultado = Arquivo.descobirDistancia("GOIANIA", "SAO PAULO");
        assertEquals(resultado, 926, 2*Math.ulp(resultado));
    }
    
}
