package Model;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Luiz Batista
 * 28 mar 2023
 */

public class Transporte {
    private String nomeRota;
    private ArrayList<Produto> listaProduto;
    private ArrayList<Caminhao> listaCaminhao;
    
    private double distanciaTotal;
    
    private double custoTotal;
    private double custoPorTecho;
    private double custoKm;
    private double custoProduto;
    private double custoTotalTrecho;
    private double custoModalidade;

    public Transporte(String nomeRota, ArrayList<Produto> listaProduto, ArrayList<Caminhao> listaCaminhao, double distanciaTotal, double custoTotal, double custoPorTecho, double custoKm, double custoProduto, double custoTotalTrecho, double custoModalidade) {
        this.nomeRota = nomeRota;
        this.listaProduto = listaProduto;
        this.listaCaminhao = listaCaminhao;
        this.distanciaTotal = distanciaTotal;
        this.custoTotal = custoTotal;
        this.custoPorTecho = custoPorTecho;
        this.custoKm = custoKm;
        this.custoProduto = custoProduto;
        this.custoTotalTrecho = custoTotalTrecho;
        this.custoModalidade = custoModalidade;
    }

    public int getNumeroVeiculos(){
        return listaCaminhao.size();
    }
    
    public int getNumeroProdutos(){
       return listaProduto.size();
    }

    public String getNomeRota() {
        return nomeRota;
    }

    public void setNomeRota(String nomeRota) {
        this.nomeRota = nomeRota;
    }

    public ArrayList<Produto> getListaProduto() {
        return listaProduto;
    }

    public void setListaProduto(ArrayList<Produto> listaProduto) {
        this.listaProduto = listaProduto;
    }

    public ArrayList<Caminhao> getListaCaminhao() {
        return listaCaminhao;
    }

    public void setListaCaminhao(ArrayList<Caminhao> listaCaminhao) {
        this.listaCaminhao = listaCaminhao;
    }

    public double getDistanciaTotal() {
        return distanciaTotal;
    }

    public void setDistanciaTotal(double distanciaTotal) {
        this.distanciaTotal = distanciaTotal;
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(double custoTotal) {
        this.custoTotal = custoTotal;
    }

    public double getCustoPorTecho() {
        return custoPorTecho;
    }

    public void setCustoPorTecho(double custoPorTecho) {
        this.custoPorTecho = custoPorTecho;
    }

    public double getCustoKm() {
        return custoKm;
    }

    public void setCustoKm(double custoKm) {
        this.custoKm = custoKm;
    }

    public double getCustoProduto() {
        return custoProduto;
    }

    public void setCustoProduto(double custoProduto) {
        this.custoProduto = custoProduto;
    }

    public double getCustoTotalTrecho() {
        return custoTotalTrecho;
    }

    public void setCustoTotalTrecho(double custoTotalTrecho) {
        this.custoTotalTrecho = custoTotalTrecho;
    }

    public double getCustoModalidade() {
        return custoModalidade;
    }

    public void setCustoModalidade(double custoModalidade) {
        this.custoModalidade = custoModalidade;
    }

    @Override
    public String toString() {
        return "Transporte{" + "nomeRota=" + nomeRota + ", listaProduto=" + listaProduto + ", listaCaminhao=" + listaCaminhao + ", distanciaTotal=" + distanciaTotal + ", custoTotal=" + custoTotal + ", custoPorTecho=" + custoPorTecho + ", custoKm=" + custoKm + ", custoProduto=" + custoProduto + ", custoTotalTrecho=" + custoTotalTrecho + ", custoModalidade=" + custoModalidade + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.nomeRota);
        hash = 71 * hash + Objects.hashCode(this.listaProduto);
        hash = 71 * hash + Objects.hashCode(this.listaCaminhao);
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.distanciaTotal) ^ (Double.doubleToLongBits(this.distanciaTotal) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.custoTotal) ^ (Double.doubleToLongBits(this.custoTotal) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.custoPorTecho) ^ (Double.doubleToLongBits(this.custoPorTecho) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.custoKm) ^ (Double.doubleToLongBits(this.custoKm) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.custoProduto) ^ (Double.doubleToLongBits(this.custoProduto) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.custoTotalTrecho) ^ (Double.doubleToLongBits(this.custoTotalTrecho) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.custoModalidade) ^ (Double.doubleToLongBits(this.custoModalidade) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transporte other = (Transporte) obj;
        if (Double.doubleToLongBits(this.distanciaTotal) != Double.doubleToLongBits(other.distanciaTotal)) {
            return false;
        }
        if (Double.doubleToLongBits(this.custoTotal) != Double.doubleToLongBits(other.custoTotal)) {
            return false;
        }
        if (Double.doubleToLongBits(this.custoPorTecho) != Double.doubleToLongBits(other.custoPorTecho)) {
            return false;
        }
        if (Double.doubleToLongBits(this.custoKm) != Double.doubleToLongBits(other.custoKm)) {
            return false;
        }
        if (Double.doubleToLongBits(this.custoProduto) != Double.doubleToLongBits(other.custoProduto)) {
            return false;
        }
        if (Double.doubleToLongBits(this.custoTotalTrecho) != Double.doubleToLongBits(other.custoTotalTrecho)) {
            return false;
        }
        if (Double.doubleToLongBits(this.custoModalidade) != Double.doubleToLongBits(other.custoModalidade)) {
            return false;
        }
        if (!Objects.equals(this.nomeRota, other.nomeRota)) {
            return false;
        }
        if (!Objects.equals(this.listaProduto, other.listaProduto)) {
            return false;
        }
        return Objects.equals(this.listaCaminhao, other.listaCaminhao);
    }
    
   
}
