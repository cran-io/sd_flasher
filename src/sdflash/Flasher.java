package sdflash;

import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import static java.lang.System.exit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

/**
 *
 * @author cran.io
 */

public class Flasher extends JFrame{
//  First we defined all the variables that we are going to use.
    int sale= 0 ;
    int salesPackages[] = new int[0];
    DefaultListModel packagesToSale = new DefaultListModel();
    String server = "104.236.88.136:3000";
    static Supplier supplier;    
    static List<Game> games = new ArrayList<Game>();
    static List<Package> packages = new ArrayList<Package>();
    int gamesSelects = 0;
    DefaultListModel modelOfSale = new DefaultListModel();
    int gameToSale = 0;
    int controlPackages = 0;
    
    public Flasher() {
        initComponents();   
    }

    public Flasher(final Supplier supplier, final List<Package> packagesListSent) throws JSONException {
//      First we initialize the values of the variables and another for the JFrame.  
        initComponents();
        setIcon();
        this.setLocationRelativeTo(null);
        this.supplier = supplier;        
        this.packages = packagesListSent;
        int wallet = this.supplier.getWallet()/100;
        int cents = this.supplier.getWallet() - wallet*100;
        String text = "";
        String name = "";
        int id = 0;
//      Here we set the text of the labels.  
        usernameLabel.setText("Usuario: " + supplier.getName());
        walletLabel.setText("Credito: " + Integer.toString(wallet) + "." + Integer.toString(cents));
        
        DefaultListModel model = new DefaultListModel();

        final ArrayList<String> ar = new ArrayList<String>();
        
        for (int i = 0; i < packages.size(); i++) {
            ar.add(packages.get(i).toString());
            model.add(i,packages.get(i).toString() + " - precio: " + packages.get(i).getPrice());
        } 
        
        int totalPrice = 0;
        packageList.setModel(model);
        
        packageList.addListSelectionListener(new ListSelectionListener()
        {
            DefaultListModel model = new DefaultListModel();
            @Override
            public void valueChanged(ListSelectionEvent ev) {
                
                String selectedOption = "";
                int gameSelected = 0;
                
                if ((!ev.getValueIsAdjusting()) && ((((JList)ev.getSource()).getModel().getSize())!=0)){
                    JList source = (JList)ev.getSource();
                    selectedOption = source.getSelectedValue().toString();
                    gameSelected = source.getSelectedIndex();

                    for (int i = 0; i < packages.size(); i++) {
                        if(packages.get(i).toString().equals(ar.get(gameSelected))){
                            gameToSale = i;
                            int id = packages.get(i).getId();
                            try {
                                DefaultListModel gamesModel = new DefaultListModel();
                                InputStream in = new URL("http://"+server+"//api//v1//packages//"+Integer.toString(id)+"//?api_token=" + supplier.getApiToken()).openStream();
                                String text = IOUtils.toString( in ) ;
                                IOUtils.closeQuietly(in);
                                    
                                JSONObject obj = new JSONObject(text);
                                JSONArray jsonPackagesGames = obj.getJSONArray("games");
                                int countOfGamesInPackages = jsonPackagesGames.length();
                                for(int j=0;j<countOfGamesInPackages; j++){
                                    JSONObject explrObject2 = jsonPackagesGames.getJSONObject(j);
                                    gamesModel.add(j,explrObject2.getString("name"));
                                }
                                gamesCombo.setModel(gamesModel);
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (JSONException ex) {
                                Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }                    
                    }
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        walletLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        gamesCombo = new javax.swing.JList();
        exitButton = new javax.swing.JButton();
        flashSDCard = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        packageList = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        toFlashList = new javax.swing.JList();
        labelPrice = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        buttonClear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tabi Games SD Flasher");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informacion de su cuenta", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        usernameLabel.setText("jLabel1");

        walletLabel.setText("jLabel2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(usernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                    .addComponent(walletLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(usernameLabel)
                .addGap(2, 2, 2)
                .addComponent(walletLabel)
                .addGap(2, 2, 2))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Juegos incluidos", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        gamesCombo.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {  };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        gamesCombo.setEnabled(false);
        jScrollPane1.setViewportView(gamesCombo);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        exitButton.setText("<<SALIR>>");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        flashSDCard.setText("<<FLASH SD CARD>>");
        flashSDCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flashSDCardActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Paquetes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 24))); // NOI18N

        jScrollPane2.setViewportView(packageList);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Venta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 24))); // NOI18N

        toFlashList.setEnabled(false);
        jScrollPane3.setViewportView(toFlashList);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addContainerGap())
        );

        labelPrice.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        labelPrice.setText("Precio:");

        jButton1.setText("Agregar a compra>>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        buttonClear.setText("<<Empezar nueva venta>>");
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(flashSDCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(flashSDCard)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitButton)
                    .addComponent(buttonClear))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
//      If the provider press the button we close the program.
        exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void flashSDCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flashSDCardActionPerformed
        // TODO add your handling code here:
        if(controlPackages==0){
            JOptionPane.showMessageDialog(null, "Debe elejir al menos un paquete para realizar una venta.", "ERROR: Ningun paquete agregado", 1);
        }
        else{
            for(int i=0;i<salesPackages.length;i++){
                
                String pathOfPackage = System.getProperty("user.dir") + File.separator + "Files" + File.separator;
                String pathOfDestination = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator;
                
                File fileToCopy= new File(pathOfDestination);
                if(!fileToCopy.exists()) new File(pathOfDestination).mkdir();
                
//              Here we copy the packages that the provider is selling
                for(int j=0; j<packages.size(); j++){
                    pathOfPackage = System.getProperty("user.dir") + File.separator + "Files" + File.separator + File.separator + "pk" + packages.get(j).getId();
                    pathOfDestination = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator + "pk" + packages.get(j).getId();
                    fileToCopy= new File(pathOfDestination);
                    File from = new File(pathOfPackage);
                    
                    try {
                        FileUtils.copyDirectory(from, fileToCopy);
                    } catch (IOException ex) {
                        Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(packages.get(j).getId()==salesPackages[i]){
                        HttpClient httpClient = HttpClientBuilder.create().build();
                        String postUrl = "http://"+server+"/api/v1/packages/"+ packages.get(0).getId()+"/buy";
                        HttpPost post = new HttpPost(postUrl);
                        StringEntity  postingString;            
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("api_token", supplier.getApiToken());
                            Random rand = new Random();
                            int value  = rand.nextInt((9999999 - 999) + 1) + 123;
                            String key = Integer.toString(value);
                            obj.put("key", key);
                            postingString = new StringEntity(obj.toString());
                            post.setEntity(postingString);
                            post.setHeader("Content-type", "application/json");
                            HttpResponse  response = httpClient.execute(post);
                            
                            File file = new File(pathOfDestination + "/key.txt");
 
                            if (!file.exists()) {
                                    file.createNewFile();
                                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                    BufferedWriter bw = new BufferedWriter(fw);
                                    bw.write(key);
                                    bw.close();
                            }
                            
                            else{
                                file = new File(pathOfDestination + "/key"+ i +".txt");
                                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                BufferedWriter bw = new BufferedWriter(fw);
                                bw.write(key);
                                bw.close();
                            }
                         
                            
                            } catch (UnsupportedEncodingException ex) {
                                Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (JSONException ex) {
                                Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        
                        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(pathOfDestination + "/open.txt"), "utf-8"))) {
                                writer.write("1");
                            } catch (UnsupportedEncodingException ex) {    
                            Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                        }    
                        
                    }
                    
                    InputStream in;
                    try {
                        in = new URL("http://"+server+"//api//v1//packages//" + packages.get(j).getId() + "//?api_token=" + supplier.getApiToken()).openStream();
                        String text = IOUtils.toString( in ) ;
                        IOUtils.closeQuietly(in);
                        JSONObject obj = new JSONObject(text);
                        JSONArray jsonPackagesGames = obj.getJSONArray("games");
                        int countOfGamesInPackages = jsonPackagesGames.length();
                        for(int k=0;k<countOfGamesInPackages; k++){
                            JSONObject explrObject2 = jsonPackagesGames.getJSONObject(k);
                            String pathOfGames = System.getProperty("user.dir") + File.separator + "Files" + File.separator + "game" + explrObject2.getInt("id");
                            pathOfDestination = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator + "pk" + packages.get(j).getId() + File.separator +"game" + explrObject2.getInt("id");
                            fileToCopy= new File(pathOfDestination);
                            from = new File(pathOfGames);
                            FileUtils.copyDirectory(from, fileToCopy);
                            if(packages.get(j).getId()!=salesPackages[i]){
                                pathOfDestination = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator + "pk" + packages.get(j).getId() + File.separator +"game" + explrObject2.getInt("id") + File.separator + "game.zip";
                                File fileToDelete= new File(pathOfDestination);
                                fileToDelete.delete();
                            }
                        }
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JSONException ex) {
                        Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                
//              Here we copy the rest of the packages
//                for(int j2=0; j2<packages.size(); j2++){
//                    
//                    pathOfPackage = System.getProperty("user.dir") + File.separator + "Files" + File.separator + File.separator + "pk" + packages.get(j2).getId();
//                    pathOfDestination = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator + "pk" + packages.get(j2).getId();
//                    fileToCopy= new File(pathOfDestination);
//                    File from = new File(pathOfPackage);
//
//                    if(!fileToCopy.exists()){
//                        new File(pathOfDestination).mkdir();
//                        try {
//                            FileUtils.copyDirectory(from, fileToCopy);
//                            InputStream in;
//                            in = new URL("http://"+server+"//api//v1//packages//" + packages.get(j2).getId() + "//?api_token=" + supplier.getApiToken()).openStream();
//                            String text = IOUtils.toString( in ) ;
//                            IOUtils.closeQuietly(in);
//                            JSONObject obj = new JSONObject(text);
//                            JSONArray jsonPackagesGames = obj.getJSONArray("games");
//                            int countOfGamesInPackages = jsonPackagesGames.length();
//                            System.out.println("esto si lo hizo");
//                            for(int k=0;k<countOfGamesInPackages; k++){
//                                JSONObject explrObject2 = jsonPackagesGames.getJSONObject(k);
//                                String pathOfGames = System.getProperty("user.dir") + File.separator + "Files" + File.separator + "game" + explrObject2.getInt("id");
//                                pathOfDestination = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator + "pk" + packages.get(j2).getId() + File.separator +"game" + explrObject2.getInt("id");
//                                
//                                fileToCopy= new File(pathOfDestination);
//                                from = new File(pathOfGames);
//                                FileUtils.copyDirectory(from, fileToCopy);
//                                
//                                pathOfDestination = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator + "pk" + packages.get(j2).getId() + File.separator +"game" + explrObject2.getInt("id")+ File.separator +"/game.zip";
//                                System.out.println("esto lo hace");
//                                System.out.println(pathOfDestination);
//                                File fileToDelete= new File(pathOfDestination);
//                                fileToDelete.delete();
//                                System.out.println(fileToDelete.getAbsolutePath());
////                                FileUtils.deleteDirectory(fileToDelete);
//                            }
//                        }
//                         catch (MalformedURLException ex) {
//                            Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (IOException ex) {
//                            Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (JSONException ex) {
//                            Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
                
                this.setVisible(false);
                FlashMemory fm = new FlashMemory(supplier,packages);
                fm.show();
                this.dispose();
            }
        }
    }//GEN-LAST:event_flashSDCardActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        sale = sale + packages.get(gameToSale).getFullPrice();
        int cents = sale % 100;
        int decimals = sale/100;
        labelPrice.setText("Precio: " + Integer.toString(decimals) + "." + Integer.toString(cents));   
        if(packages.get(gameToSale).getFullPrice()>supplier.getWallet()){
            JOptionPane.showMessageDialog(null, "No posee los creditos suficientes para adquirir este paquete. Contactar a Eurocase", "Error",JOptionPane.ERROR_MESSAGE);
        }
        else{
            
            int[] newSeries = new int[salesPackages.length + 1];
            for (int i = 0; i < salesPackages.length; i++){
                newSeries[i] = salesPackages[i];
            }
            newSeries[newSeries.length - 1] = packages.get(gameToSale).getId();
            salesPackages = newSeries; 
            
            packagesToSale.add(controlPackages, packages.get(gameToSale).toString());
            controlPackages ++;
            toFlashList.setModel(packagesToSale);
            
//            int itemToDelete = packageList.getSelectedIndex();
//            
//            DefaultListModel model2 = new DefaultListModel();
//            
//            for (int i = 0; i < packages.size(); i++) {
//                if (itemToDelete!=i) {
//                    model2.add(i,packages.get(i).toString() + " " + packages.get(i).getPrice());
//                }
//            } 
//            packageList.setModel(model2);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearActionPerformed
//      When the user press the button we clear all the information.       
        this.setVisible(false);
        Flasher flash;
        try {                    
            flash = new Flasher(supplier, packages);
            flash.show();
        } catch (JSONException ex) {
            Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
    }//GEN-LAST:event_buttonClearActionPerformed

    /**
     * @param args the command line arguments
     */
    public  void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Flasher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Flasher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Flasher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Flasher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Flasher().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClear;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton flashSDCard;
    private javax.swing.JList gamesCombo;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelPrice;
    private javax.swing.JList packageList;
    private javax.swing.JList toFlashList;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JLabel walletLabel;
    // End of variables declaration//GEN-END:variables

    private void setIcon() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo.png")));
    }
    
}
