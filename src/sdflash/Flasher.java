package sdflash;

import java.awt.Font;
import java.awt.Point;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
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
    String server = "159.203.120.113:3000";
    static Supplier supplier;    
    static List<Game> games = new ArrayList<Game>();
    static List<Package> packages = new ArrayList<Package>();
    int gamesSelects = 0;
    DefaultListModel modelOfSale = new DefaultListModel();
    int gameToSale = 0;
    int controlPackages = 0;
    int walletActual=0;
    boolean checkForDelete = false;
    
    public Flasher() {
        initComponents();   
    }

    public Flasher(final Supplier supplier, final List<Package> packagesListSent, final List<Game> gamesListSent) throws JSONException {
//      First we initialize the values of the variables and another for the JFrame.  
        initComponents();
        setIcon();
        
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        jLabel1.setVisible(false);
        paths = File.listRoots();
        
        for(File path:paths)
        {
//            jComboBox1.addItem(("Drive Name: "+path+"       Description: "+fsv.getSystemTypeDescription(path)));
            jComboBox2.addItem(path);
        }
        
        this.setLocationRelativeTo(null);
        this.supplier = supplier;      
        walletActual = supplier.getWallet();
        this.packages = packagesListSent;
        this.games = gamesListSent;
        int wallet = this.supplier.getWallet();
        float priceShow = (float) wallet / 100;
        String text = "";
        String name = "";
        int id = 0;
//      Here we set the text of the labels.  
        usernameLabel.setText("Usuario: " + supplier.getName());
        walletLabel.setText("Credito: " + priceShow);
        
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
                    DefaultListModel gamesModel = new DefaultListModel();
                    for (int i = 0; i < packages.size(); i++) {
                        if(packages.get(i).toString().equals(ar.get(gameSelected))){
                            gameToSale = i;

                            int idOfGames[] = new int[packages.get(i).getSizeOfGames()];
                            idOfGames = packages.get(i).getGamesInPackage();
                            int controlUbication = 0;
                            for(int j=0;j<games.size();j++)
                            {
                                for(int k=0;k<idOfGames.length;k++){
                                    if(idOfGames[k]==games.get(j).getIDpackage()){
                                        gamesModel.add(controlUbication,games.get(j).getName());
                                        controlUbication++;
                                    }
                                }
//                                System.out.println(games.get(j).getIDpackage());
//                                if (games.get(j).getIDpackage()==packages.get(i).getId())
//                                {
//                                    gamesModel.add(j,games.get(j).getName());
//                                }
                            }
//                            try {
//                                DefaultListModel gamesModel = new DefaultListModel();
//                                InputStream in = new URL("http://"+server+"//api//v1//packages//"+Integer.toString(id)+"//?api_token=" + supplier.getApiToken()).openStream();
//                                String text = IOUtils.toString( in ) ;
//                                IOUtils.closeQuietly(in);
//                                    
//                                JSONObject obj = new JSONObject(text);
//                                JSONArray jsonPackagesGames = obj.getJSONArray("games");
//                                int countOfGamesInPackages = jsonPackagesGames.length();
//                                for(int j=0;j<countOfGamesInPackages; j++){
//                                    JSONObject explrObject2 = jsonPackagesGames.getJSONObject(j);
//                                    gamesModel.add(j,explrObject2.getString("name"));
//                                }
//                                gamesCombo.setModel(gamesModel);
//                            } catch (MalformedURLException ex) {
//                                Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
//                            } catch (IOException ex) {
//                                Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
//                            } catch (JSONException ex) {
//                                Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
//                            }
                        }                    
                    }
                    gamesCombo.setModel(gamesModel);
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
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        toFlashList = new javax.swing.JList();
        labelPrice = new javax.swing.JLabel();
        buttonClear = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tabi SD Burner");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informacion de su cuenta", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        usernameLabel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        usernameLabel.setText("jLabel1");

        walletLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        walletLabel.setForeground(new java.awt.Color(255, 0, 0));
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicaciones", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 24))); // NOI18N

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
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

        jButton1.setText("AGREGAR A COMPRA>>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1))
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

        labelPrice.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        labelPrice.setForeground(new java.awt.Color(0, 51, 153));
        labelPrice.setText("Precio:");

        buttonClear.setText("<<EMPEZAR NUEVA VENTA>>");
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione SD Card a flashear" }));

        jButton2.setText("<<RECARGAR");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Creando la SD...Espere");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 13, Short.MAX_VALUE))
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(flashSDCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonClear, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2))
                            .addComponent(exitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(flashSDCard)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitButton)
                    .addComponent(buttonClear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1))
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
            if(jComboBox2.getSelectedItem().toString().equals("Seleccione SD Card a flashear")){
                JOptionPane.showMessageDialog(null, "Debe elejir una micro SD para flashear.", "ERROR: Ninguna tarjeta seleccionada", 1);
            }
            else{
                jPanel1.setVisible(false);
                jPanel2.setVisible(false);
                jPanel3.setVisible(false);
                jPanel4.setVisible(false);
                labelPrice.setVisible(false);
                buttonClear.setVisible(false);
                exitButton.setVisible(false);
                flashSDCard.setVisible(false);
                jComboBox2.setVisible(false);
                jButton2.setVisible(false);
                jLabel1.setVisible(true);
                jLabel1.setFont(new Font("Serif", Font.PLAIN, 30));
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

                        InputStream in;
                        try {
                            in = new URL("http://"+server+"//api//v1//packages//" + packages.get(j).getId() + "//?api_token=" + supplier.getApiToken()).openStream();
                            String text = IOUtils.toString( in ) ;
                            checkForDelete = true;
                            IOUtils.closeQuietly(in);
                            JSONObject obj = new JSONObject(text);
                            System.out.println(text);
                            JSONArray jsonPackagesGames = obj.getJSONArray("games");
                            int countOfGamesInPackages = jsonPackagesGames.length();
                            
                            for(int k=0;k<countOfGamesInPackages; k++){
                                JSONObject explrObject2 = jsonPackagesGames.getJSONObject(k);
                                String pathOfGames = System.getProperty("user.dir") + File.separator + "Files" + File.separator + "game" + explrObject2.getInt("id");
                                pathOfDestination = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator + "pk" + packages.get(j).getId() + File.separator +"game" + explrObject2.getInt("id");
                                fileToCopy= new File(pathOfDestination);
                                from = new File(pathOfGames);
                                FileUtils.copyDirectory(from, fileToCopy);
                                
//                                if(Arrays.binarySearch(salesPackages, packages.get(j).getId())<0){
                                for (int l = 0; l < salesPackages.length; l++) {
                                   if((packages.get(j).getId()==salesPackages[l])){
                                        checkForDelete = false;
                                    } 
                                }
                                if (checkForDelete){
                                    String pathOfPackageToCopy = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator + "pk" + packages.get(j).getId() + File.separator + "key.txt";
                                    File filePackageToCopy= new File(pathOfPackageToCopy);
                                    if(!filePackageToCopy.exists()){
                                        pathOfDestination = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator + "pk" + packages.get(j).getId() + File.separator +"game" + explrObject2.getInt("id") + File.separator + "game.zip";
                                        File fileToDelete= new File(pathOfDestination);
                                        fileToDelete.delete();
                                    }
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
                }
                
                String microSD = jComboBox2.getSelectedItem().toString();
                File aDrive = new File(microSD);
//                long freeSpace = aDrive.length();
                
                String folderToCopy = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator;
                File toCopy = new File(folderToCopy);
                
                long freeSpace = aDrive.getFreeSpace();
                long freeSpace2 = FileUtils.sizeOfDirectory(toCopy);
                
                if(freeSpace>freeSpace2){
                
                       for(int i=0;i<salesPackages.length;i++){
                        String pathOfPackage = System.getProperty("user.dir") + File.separator + "Files" + File.separator;
                        String pathOfDestination = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator;
                        File fileToCopy= new File(pathOfDestination);
                        if(!fileToCopy.exists()) new File(pathOfDestination).mkdir();
    //                  Here we copy the packages that the provider is selling
    
                        for(int j=0; j<packages.size(); j++){
                            if(packages.get(j).getId()==salesPackages[i]){
                                HttpClient httpClient = HttpClientBuilder.create().build();
                                String postUrl = "http://"+server+"/api/v1/packages/"+ packages.get(j).getId()+"/buy";
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
                                    Boolean found;
                                    do{
                                        HttpResponse  response = httpClient.execute(post);
                                        String word = "200 OK";
                                        String textoTest = response.toString();
                                        found = !textoTest.contains(word);
                                    }while(found);
                                    pathOfDestination = System.getProperty("user.dir") + File.separator + "FolderToCopy" + File.separator + "pk" + salesPackages[i];
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
                                try {
                                    String fileName = pathOfDestination + "/package.txt";
                                    FileWriter fw = new FileWriter(fileName,true);
                                    fw.append("\n1");
//                                    fw.append("1");
                                    fw.close();
//                                    Writer output = new BufferedWriter(new FileWriter(, true));
//                                    output.append("1");
                                } catch (IOException ex) {
                                    Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                                }
//                                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
//                                    new FileOutputStream(pathOfDestination + "/package.txt"), "utf-8"))) {
//                                        writer.append("1");
//                                    } catch (UnsupportedEncodingException ex) {    
//                                    Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
//                                } catch (FileNotFoundException ex) {
//                                    Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
//                                } catch (IOException ex) {
//                                    Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
//                                }    
                            }
                        }
                    }
//                    esto es para copiar y eliminar todo
                    String pathOfCopy = System.getProperty("user.dir") + File.separator + "FolderToCopy";
                    String sn = jComboBox2.getSelectedItem().toString();
                    String pathOfDestination = sn + "games";
                    File fileToCopy= new File(pathOfCopy);

                    File to = new File(pathOfDestination);
                    if (!to.exists()){
                        new File(pathOfDestination).mkdir();
                    }
                    try {
                        FileUtils.copyDirectory(fileToCopy, to);
                        FileUtils.deleteDirectory(fileToCopy);
                    } catch (IOException ex) {
                        Logger.getLogger(FlashMemory.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                    
    //                asdsadsaddsadsadadsa
                    Flasher flash;
                    try {                    
                        supplier.setWallet(supplier.getWallet()-sale);
                        flash = new Flasher(supplier, packages,games);
                        flash.show();
                    } catch (JSONException ex) {
                        Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.dispose();
                }
                else{
                    long sizeNeeded = freeSpace2 /1024 / 1024;
                    JOptionPane.showMessageDialog(null, "Debe elejir una micro SD con espacio suficiente\n El mismo debe ser de:." + sizeNeeded + " MB", "ERROR: Ninguna tarjeta seleccionada", 1);
                }
            }
        }
    }//GEN-LAST:event_flashSDCardActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (packageList.isSelectionEmpty()){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un paquete.", "Error",JOptionPane.ERROR_MESSAGE);
        }
        else{
            if(packages.get(gameToSale).getFullPrice()>walletActual){
                JOptionPane.showMessageDialog(null, "No posee los creditos suficientes para adquirir este paquete. Contactar a Eurocase", "Error",JOptionPane.ERROR_MESSAGE);
            }
            else{
                walletActual = walletActual - packages.get(gameToSale).getFullPrice();
                
                sale = sale + packages.get(gameToSale).getFullPrice();
                float priceShow = (float) sale / 100;
                labelPrice.setText("Precio: "  + priceShow);
                float creditUpDate = (float) (walletActual)/ 100;
                
                walletLabel.setText("Credito:" + creditUpDate);
                int[] newSeries = new int[salesPackages.length + 1];
                for (int i = 0; i < salesPackages.length; i++){
                    newSeries[i] = salesPackages[i];
                }
                newSeries[newSeries.length - 1] = packages.get(gameToSale).getId();
                salesPackages = newSeries; 

                packagesToSale.add(controlPackages, packages.get(gameToSale).toString());
                controlPackages ++;
                toFlashList.setModel(packagesToSale);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearActionPerformed
//      When the user press the button we clear all the information.       
        this.setVisible(false);
        Flasher flash;
        try {                    
            flash = new Flasher(supplier, packages, games);
            flash.show();
        } catch (JSONException ex) {
            Logger.getLogger(Flasher.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
    }//GEN-LAST:event_buttonClearActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jComboBox2.removeAllItems();
        jComboBox2.addItem("Seleccione SD Card a flashear");
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        
        paths = File.listRoots();
        
        for(File path:paths)
        {
//            jComboBox1.addItem(("Drive Name: "+path+"       Description: "+fsv.getSystemTypeDescription(path)));
            jComboBox2.addItem(path);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelPrice;
    public javax.swing.JList packageList;
    private javax.swing.JList toFlashList;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JLabel walletLabel;
    // End of variables declaration//GEN-END:variables

    private void setIcon() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo.ico")));
    }
    
}
