package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Luiz Batista Cardoso
 *      25 mar 2023
 */
public class Arquivo {
    
    private static String[] cidades = null;
    
    private static Map<String, Map<String, Double>> distanceMap = new HashMap<>();

    // Lê um arquivo qualquer, onde separa cada linha e reparte suas informações por ';' para cadastrar cidade(String) e sua distância(Double)
    public static void extrairDadosArquivo(String fileName) {
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            // Define uma string contento toda uma linha.
            String line;
            
            // Define uma lista de cidades no formato String.
            cidades = null;
            
            // Define a iteração das cidades ao reconhecar as distâncias.
            int j = -1;
            
            // Lê o que estiver em cada linha enquanto não houver nada.
            while ((line = br.readLine()) != null) {
               
                // Separa os dados lido por ';'
                String[] data = line.split(";");
                
                // Se a posição da linha for nula (0) reconhece o nome das cidades.
                if (cidades == null) {
                    cidades = data; // Armazena o nome de cada cidade encontrada.
                } else {
                    // Somente itera sobre as distâncias
                    // Iteração lê as informaçõs de coluna a coluna depois de linha a linha.
                    // Faz esta iteração 24 vezes (ou por quantos ; estiver no csv)
                    
                    String currentCity = cidades[j]; // Troca de cidade cada vez que termina a iteração
                    distanceMap.put(currentCity, new HashMap<>());
                    
                    // Cadastra a relação de distancia de uma cidade para outra no HashMap
                    for(int i = 0; i< data.length; i++){
                        String city = cidades[i];
                        double distance = Integer.parseInt(data[i]);
                        distanceMap.get(currentCity).put(city, distance);
                    }
                }
                j++; // Quando terminar de cadastrar faz o incremento de cidade
            }
        } catch (IOException e) {
            System.out.println("======================================================================================");
            System.out.println("ERRO! O sistema não pode encontrar o arquivo especificado.");
            System.out.println("Verifique se o arquivo possui o nome da extensão e que está digitado corretamente.");
            System.out.println("Também verifique se o arquivo está na pasta raiz do projeto/programa.");
            System.out.println("======================================================================================");
            System.out.println(e);
        }
    }
    
    // Método que retorna a distância entre duas cidades baseada em suas relações no HashMap.
    public static double descobirDistancia(String cidadeA, String cidadeB) {
        
        // Importa todas as distancias que a cidade informa tem com as outras.
        Map<String, Double> relacoesCidadeA = distanceMap.get(cidadeA);
        
        // Caso não houver nenhuma relação entre elas pode ser que não exista ou que esteja escrita errada.
        if (relacoesCidadeA == null) {
            throw new IllegalArgumentException("A cidade " + cidadeA + " não foi encontrada, talvez foi digitado errado ou não esteja cadastrada.");
        }
        
        // Define o valor de distancia baseado na relação com a cidade B
        Double distancia = relacoesCidadeA.get(cidadeB);
        
        // Se a distância for nula != 0 quer dizer que a distancia da cidade B não é conhecida ou que não existe.
        if (distancia == null) {
            throw new IllegalArgumentException("ERRO! Não existe distancia conhecida entre " + cidadeA + " e " + cidadeB);
        }
        
        return distancia;
    }
    
    public static String[] getCidades(){
        return cidades;
    }

}
