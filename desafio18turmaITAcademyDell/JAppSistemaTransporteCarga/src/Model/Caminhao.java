/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Objects;

/**
 *
 * @author newik
 */
public class Caminhao {
    private int placa;
    private String porte;
    private double carga;
    double custoQuilometragem;

    public Caminhao(int placa, String porte, double carga, double custoQuilometragem) {
        this.placa = placa;
        this.porte = porte;
        this.carga = carga;
        this.custoQuilometragem = custoQuilometragem;
    }
    
    public Caminhao(String porte, double carga, double custoQuilometragem) {
        this.porte = porte;
        this.carga = carga;
        this.custoQuilometragem = custoQuilometragem;
    }

    public int getPlaca() {
        return placa;
    }

    public void setPlaca(int placa) {
        this.placa = placa;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public double getCarga() {
        return carga;
    }

    public void setCarga(double carga) {
        this.carga = carga;
    }

    public double getCustoQuilometragem() {
        return custoQuilometragem;
    }

    public void setCustoQuilometragem(double custoQuilometragem) {
        this.custoQuilometragem = custoQuilometragem;
    }
    
    public double calcularCustoQuilometragem(Double distancia) {
        return custoQuilometragem*distancia;
    }

    @Override
    public String toString() {
        return "Caminhao{" + "placa=" + placa + ", porte=" + porte + ", carga=" + carga + ", custoQuilometragem=" + custoQuilometragem + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.placa;
        hash = 59 * hash + Objects.hashCode(this.porte);
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.carga) ^ (Double.doubleToLongBits(this.carga) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.custoQuilometragem) ^ (Double.doubleToLongBits(this.custoQuilometragem) >>> 32));
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
        final Caminhao other = (Caminhao) obj;
        if (this.placa != other.placa) {
            return false;
        }
        if (Double.doubleToLongBits(this.carga) != Double.doubleToLongBits(other.carga)) {
            return false;
        }
        if (Double.doubleToLongBits(this.custoQuilometragem) != Double.doubleToLongBits(other.custoQuilometragem)) {
            return false;
        }
        return Objects.equals(this.porte, other.porte);
    }
  
}
