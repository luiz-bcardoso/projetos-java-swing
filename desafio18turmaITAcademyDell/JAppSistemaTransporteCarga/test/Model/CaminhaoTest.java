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
public class CaminhaoTest {

    public CaminhaoTest() {
    }

    
    @Test
    public void calcular10KmCaminhaoPequeno() {
        // Define um caminhão de modalidade pequena:
        Caminhao caminhaoP = new Caminhao("Pequeno", 1000, 4.87);
        Double resultado = caminhaoP.calcularCustoQuilometragem(10.0);
        // Testa se o método de retornar quilometragem está correto para 10km para 4.87 de custo.
        assertEquals(resultado, 48.7, 2*Math.ulp(resultado));
    }

    @Test
    public void calcular10KmCaminhaoMedio() {
        // Define um caminhão de modalidade média:
        Caminhao caminhaoM = new Caminhao("Médio", 4000, 11.92);
        Double resultado = caminhaoM.calcularCustoQuilometragem(10.0);
        // Testa se o método de retornar quilometragem está correto para 10km para 11.92 de custo.
        assertEquals(resultado, 119.2, 2*Math.ulp(resultado));
    }

    @Test
    public void calcular10KmCaminhaoGrande() {
        // Define um caminhão de modalidade grande:
        Caminhao caminhaoG = new Caminhao("Grande", 10000, 27.44);
        Double resultado = caminhaoG.calcularCustoQuilometragem(10.0);
        // Testa se o método de retornar quilometragem está correto para 10km para 27.44 de custo.
        assertEquals(resultado, 274.4, 2*Math.ulp(resultado));
    }

}
