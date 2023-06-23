package Model;

import java.util.ArrayList;

/**
 *
 * @author Luiz
 */
public class Rota {

    private final String caminho;
    private String ultimoDestino;
    private final Double custo;
    private final Double transporte;

    public Rota(String caminho, Double custo, Double transporte) {
        this.caminho = caminho;
        this.custo = custo;
        this.transporte = transporte;
    }

    public String getCaminho() {
        return caminho;
    }

    public String getRotaFinal() {
        ultimoDestino = caminho.charAt(caminho.length() - 1) + "";
        return ultimoDestino;
    }

    public String getRotaInicial() {
        ultimoDestino = caminho.charAt(0) + "";
        return ultimoDestino;
    }

    public Double getCusto() {
        return custo;
    }

    public Double getTransporte() {
        return transporte;
    }

    public Double getCustoBeneficio() {
        return transporte / custo;
    }

    public static Double descobrirMelhorRota(ArrayList<Rota> rotas) {
        double menorCusto = rotas.get(0).getCustoBeneficio();
        for (Rota i : rotas) {
            if (menorCusto < i.getCustoBeneficio()) {
                menorCusto = i.getCustoBeneficio();
            }
        }
        return menorCusto;
    }

    public static void calcularRotas(ArrayList<Rota> listaRotas, Grafo g1) {

        // Necessário porque não se pode ampliar a lista original enquanto faz um iteração dentro dela. (ConcurrentModificationException)
        ArrayList<Rota> listaRotasAmpliada = new ArrayList<>();

        String caminhoTotal, origemInicial, origemAtual;
        Double custoTotal, transporteTotal;

        for (Rota rota : listaRotas) {
            System.out.println("\n==== INICIANDO DESCOBERTA AMPLIADA DE ROTAS PARA " + rota.getCaminho() + " ====");

            // Pega a origem inicial e final para condição de parada e validação.
            origemInicial = rota.getRotaInicial();
            origemAtual = rota.getRotaFinal();

            // Pega os valores da rota inicial, que serão reiniciados a cada iteração.
            caminhoTotal = rota.getCaminho();
            custoTotal = rota.getCusto();
            transporteTotal = rota.getTransporte();

            for (int i = 0; i < g1.getArestas().size(); i++) {
                if (!origemAtual.startsWith(origemInicial)) {
                    // se o 2 for igual ao 2 a origem da lista de arestas então é uma continuação válida 
                    if (origemAtual.equals(g1.getArestas().get(i).getOrigem())) {
                        
                        // CONCATENAÇÃO DE ROTA VÁLIDA Ex: 1-2 e 2-3 ficaria 1-2-3
                        // Compara o 2 da rota A e o 2 da rota B e adiciona no caminho o destino da rota B, que seria 3.

                        caminhoTotal = caminhoTotal + " - " + g1.getArestas().get(i).getDestino();
                        custoTotal = custoTotal + g1.getArestas().get(i).getCusto();
                        transporteTotal = transporteTotal + g1.getArestas().get(i).getTransporte();

                        listaRotasAmpliada.add(new Rota(caminhoTotal, custoTotal, transporteTotal));
                        System.out.println("Rota VÁLIDA encontrada em " + caminhoTotal + " com CUSTO de " + custoTotal + " e TRANSPORTE de " + transporteTotal);
                    }
                } else {
                    // Loop detectado já que a esta origem volta para a origem inicial, Ex: 1-4-1 que vai 1-4-1-4-1....
                    break;
                }
            }
            System.out.println("==== TERMINADO DESCOBERTA AMPLIADA DE ROTAS PARA " + rota.getCaminho() + " ====\n");
        }
        listaRotas.addAll(listaRotasAmpliada);
    }

    @Override
    public String toString() {
        return "Rota{" + "caminho=" + caminho + ", ultimoDestino=" + ultimoDestino + ", custo=" + custo + ", transporte=" + transporte + '}';
    }

}
