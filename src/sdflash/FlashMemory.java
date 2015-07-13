/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sdflash;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.usb4java.DeviceList;
import static sdflash.Flasher.packages;
import static sdflash.Flasher.supplier;


/**
 *
 * @author agustinquintanamurta
 */
public class FlashMemory extends javax.swing.JFrame {

    /**
     * Creates new form FlashMemory
     */
    
    Login login;
    static List<Package> packages = new ArrayList<Package>();
    Supplier supplier;
    String server = "104.236.88.136:3000";
    
    public FlashMemory(Supplier supp, List<Package> packagesListSent) {
        initComponents();
        this.supplier = supp;
        this.packages = packagesListSent;
        setIcon();
        this.setLocationRelativeTo(null);
        
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        
        paths = File.listRoots();
        
        for(File path:paths)
        {
            jComboBox1.addItem(("Drive Name: "+path+"       Description: "+fsv.getSystemTypeDescription(path)));
        }
        
    }

    FlashMemory(Login login) {
        initComponents();
        setIcon();
        this.login = login;
        this.setLocationRelativeTo(null);
        
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        
        paths = File.listRoots();
        
        for(File path:paths)
        {
            jComboBox1.addItem((path));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jLabel1.setText("Select the driver to flash:");

        jButton1.setText("<<Flash>>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String pathOfCopy = System.getProperty("user.dir") + File.separator + "FolderToCopy";
        String sn = jComboBox1.getSelectedItem().toString();
        String pathOfDestination = sn + "games";
        File fileToCopy= new File(pathOfCopy);
        File to = new File(pathOfDestination);
        try {
            FileUtils.copyDirectory(fileToCopy, to);
            FileUtils.deleteDirectory(fileToCopy);
        } catch (IOException ex) {
            Logger.getLogger(FlashMemory.class.getName()).log(Level.SEVERE, null, ex);
        }    
        this.setVisible(false);
        Flasher flash;
        
        try {
            String apiToken = supplier.getApiToken();
            InputStream in = new URL("http://"+server+"//api//v1//profile//?api_token=" + supplier.getApiToken()).openStream();
            String text = IOUtils.toString( in ) ;
            IOUtils.closeQuietly(in);
            String genreJson = text;
            JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(genreJson);
            String name = genreJsonObject.get("name").toString();
            int id = Integer.parseInt(genreJsonObject.get("id").toString());
            int wallet = Integer.parseInt(genreJsonObject.get("credit").toString());
            supplier = new Supplier(id, name, wallet, apiToken);
        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(null, "Tu contraseña ya no es valida o es incorrecta.", "Error",JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Los servidores estan siendo actualizados. Reintentar mas tarde.", "Error",JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {                    
            flash = new Flasher(supplier, packages);
            flash.show();
        } catch (JSONException ex) {
            Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(FlashMemory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(FlashMemory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(FlashMemory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(FlashMemory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FlashMemory().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    private void setIcon() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo.png")));
    }
}    

