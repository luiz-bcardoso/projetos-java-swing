/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author newik
 */
public class JFrameCalcRota extends javax.swing.JFrame {

    /**
     * Creates new form JFrameCalcRota
     */
    public JFrameCalcRota() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel_Titulo = new javax.swing.JLabel();
        jSeparator = new javax.swing.JSeparator();
        jLabel_Desc = new javax.swing.JLabel();
        jLabel_CidadeOrigem = new javax.swing.JLabel();
        jComboBox_CidadeOrigem = new javax.swing.JComboBox<>();
        jLabel_CidadeDestino = new javax.swing.JLabel();
        jComboBox_CidadeDestino = new javax.swing.JComboBox<>();
        jLabel_PorteCaminhao = new javax.swing.JLabel();
        jComboBox_PorteCaminhao = new javax.swing.JComboBox<>();
        jLabel_Distancia = new javax.swing.JLabel();
        jTextField_Distancia = new javax.swing.JTextField();
        jButton_CalcDistancia = new javax.swing.JButton();
        jButton_CalcRota = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Caluclar rotas simples");

        jLabel_Titulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_Titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Titulo.setText("Calcular Rota Simples Por Entre Dois Trechos e Modalidade de Transporte");

        jLabel_Desc.setText("Por favor, selecione abaixo a cidade de origem/destino e modalidade para ser calulado:");

        jLabel_CidadeOrigem.setText("Cidade de origem:");

        jComboBox_CidadeOrigem.setPreferredSize(new java.awt.Dimension(72, 32));

        jLabel_CidadeDestino.setText("Cidade de destino:");

        jComboBox_CidadeDestino.setPreferredSize(new java.awt.Dimension(72, 32));

        jLabel_PorteCaminhao.setText("Porte de carga do caminhão");

        jComboBox_PorteCaminhao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pequeno", "Médio", "Grande" }));
        jComboBox_PorteCaminhao.setPreferredSize(new java.awt.Dimension(72, 32));

        jLabel_Distancia.setText("Distância:");

        jTextField_Distancia.setEditable(false);
        jTextField_Distancia.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextField_Distancia.setText("0 Km");

        jButton_CalcDistancia.setText("CALCULAR DISTÂNCIA");
        jButton_CalcDistancia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CalcDistanciaActionPerformed(evt);
            }
        });

        jButton_CalcRota.setText("CALCULAR VALOR DA ROTA");
        jButton_CalcRota.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBox_CidadeOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBox_CidadeDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel_Desc)
                                        .addGap(35, 35, 35)))
                                .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel_CidadeOrigem)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel_CidadeDestino)
                                .addGap(133, 133, 133))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox_PorteCaminhao, 0, 225, Short.MAX_VALUE)
                                    .addComponent(jLabel_PorteCaminhao)
                                    .addComponent(jButton_CalcDistancia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_Distancia)
                                        .addComponent(jTextField_Distancia, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton_CalcRota, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_Titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_Desc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_CidadeOrigem)
                    .addComponent(jLabel_CidadeDestino))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_CidadeOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox_CidadeDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_PorteCaminhao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_PorteCaminhao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_Distancia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_Distancia, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_CalcDistancia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_CalcRota, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_CalcDistanciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CalcDistanciaActionPerformed
        JOptionPane.showMessageDialog(this, "Distancia das duas cidades foi calculada com sucesso.");
        jButton_CalcRota.setEnabled(true);
    }//GEN-LAST:event_jButton_CalcDistanciaActionPerformed

    public JButton getCalcRota(){
        return jButton_CalcRota;
    }
    
    public JButton getCalcDestino(){
        return jButton_CalcDistancia;
    }

    public JComboBox<String> getjComboBox_CidadeDestino() {
        return jComboBox_CidadeDestino;
    }

    public JComboBox<String> getjComboBox_CidadeOrigem() {
        return jComboBox_CidadeOrigem;
    }

    public JComboBox<String> getjComboBox_PorteCaminhao() {
        return jComboBox_PorteCaminhao;
    }
    
    public void setJTextFieldDistancia(Double distancia){
        jTextField_Distancia.setText(distancia+ " Km");
    }
    
    public void atualizarCidades(String[] cidades){
        jComboBox_CidadeDestino.setModel(new DefaultComboBoxModel<>(cidades));
        jComboBox_CidadeOrigem.setModel(new DefaultComboBoxModel<>(cidades));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_CalcDistancia;
    private javax.swing.JButton jButton_CalcRota;
    private javax.swing.JComboBox<String> jComboBox_CidadeDestino;
    private javax.swing.JComboBox<String> jComboBox_CidadeOrigem;
    private javax.swing.JComboBox<String> jComboBox_PorteCaminhao;
    private javax.swing.JLabel jLabel_CidadeDestino;
    private javax.swing.JLabel jLabel_CidadeOrigem;
    private javax.swing.JLabel jLabel_Desc;
    private javax.swing.JLabel jLabel_Distancia;
    private javax.swing.JLabel jLabel_PorteCaminhao;
    private javax.swing.JLabel jLabel_Titulo;
    private javax.swing.JSeparator jSeparator;
    private javax.swing.JTextField jTextField_Distancia;
    // End of variables declaration//GEN-END:variables
}
