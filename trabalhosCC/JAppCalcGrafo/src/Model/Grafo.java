package Model;

import java.util.ArrayList;

/**
 *
 * @author Luiz
 */
public class Grafo {

    private ArrayList<String> vertices;
    private ArrayList<Aresta> arestas;

    public Grafo(ArrayList<String> vertices, ArrayList<Aresta> arestas) {
        this.vertices = vertices;
        this.arestas = arestas;
    }

    public ArrayList<String> getVertices() {
        return vertices;
    }

    public ArrayList<Aresta> getArestas() {
        return arestas;
    }
    
    public void setVertices(ArrayList vertices) {
        this.vertices = vertices;
    }

    public void setArestas(ArrayList arestas) {
        this.arestas = arestas;
    }

    // Método que pega a primeira linha de uma lista de Strings e o trata como vértices a serem populadas em outra lista.
    public static void popularVertices(ArrayList<String> origem, ArrayList<String> destino) {
        // Pega a primeira linha do arquivo (regra de negócio) par a popular os vértices.
        String[] vertices = origem.get(0).split(",");

        // Para cada vértice dentro da array de string, popula a lista.
        for (String vertice : vertices) {
            System.out.println("Carregado vértice: " + vertice);
            destino.add(vertice);
        }
    }
}
