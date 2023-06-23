package Model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Luiz
 */
public class Aresta {

    private final String origem;
    private final String destino;
    private final double custo;
    private final double transporte;

    // checar se origem e destino estao corretos para todos os grafos (lista)
    // ou seja, grafo.vertices = origem e grafo.vertice = destino
    public Aresta(String origem, String destino, double custo, double transporte) {
        this.origem = origem;
        this.destino = destino;
        this.custo = custo;
        this.transporte = transporte;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public double getCusto() {
        return custo;
    }

    public double getTransporte() {
        return transporte;
    }

    @Override
    public String toString() {
        return "Aresta{" + "origem=" + origem + ", destino=" + destino + ", custo=" + custo + ", transporte=" + transporte + '}';
    }

    // Método que pega a segunda linha de uma lista de Strings e o trata como arestas a serem populadas em outra lista.
    public static ArrayList<Rota> popularArestas(ArrayList<String> listaOrigem, ArrayList<Aresta> listaDestino, ArrayList<String> listaVertices) {
        // Pega a primeira linha do arquivo (regra de negócio) par a popular os vértices.
        String[] listaArestas = listaOrigem.get(1).split(";");
        ArrayList<Rota> listaRotas = new ArrayList<>();
        // Para cada vértice dentro da array de string, popula a lista.
        for (String aresta : listaArestas) {
           
            System.out.println("Carregado aresta: " + aresta);
            ArrayList<String> dadosAresta = new ArrayList<>();

            String[] arrayAresta = aresta.split(",");
            dadosAresta.addAll(Arrays.asList(arrayAresta));
            
            // Extrai os valores de cada Aresta separado por ;
            String origem = dadosAresta.get(0);
            String destino = dadosAresta.get(1);
            double custo = Double.parseDouble(dadosAresta.get(2));
            double transporte = Double.parseDouble(dadosAresta.get(3));
            
            listaRotas.add(new Rota(origem+" - "+destino, custo, transporte));
            // Popula a lista de Aresta com uma nova aresta para cada ; na string de origem. SE e SOMENTE SE a origem e destino estiver presente na lista de vértices. 
            if(listaVertices.contains(origem) && listaVertices.contains(destino)){
                System.out.println("Dados de origem e destino da aresta ("+dadosAresta+") validados com sucesso. Adicionando a lista de arestas.");
                listaDestino.add(new Aresta(origem, destino, custo, transporte));
                System.out.println("Aresta adicionada com sucesso na lista.");
            }else{
                System.out.println("Erro, esta rota ("+dadosAresta+") possúi uma origem/destino inválida, logo não será adicionado a lista.");
            }
            
            //Limpa os dados da aresta já ou não adicionada.
            dadosAresta.clear();
            
        }
        return listaRotas;
    }

}
