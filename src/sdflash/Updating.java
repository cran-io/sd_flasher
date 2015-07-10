package sdflash;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static sdflash.Flasher.games;

public final class Updating extends javax.swing.JFrame {
    
    String server = "104.236.88.136:3000";
    Supplier supp;
    static List<Game> games = new ArrayList<Game>();
    static List<Package> packages = new ArrayList<Package>();
    static List<String> gamesInPackages = new ArrayList<String>();
    
    /**
     * Creates new form Updating
     * @param supplier
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    
    public Updating(Supplier supp){
        initComponents();
        this.setLocationRelativeTo(null);
        jLabel1.setText("Verificando versiones...");
        this.supp = supp;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("jLabel1");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sdflash/loading.gif"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables

    public void start() throws IOException, InterruptedException, JSONException{
        
        Timer tClosingConnextion = new Timer(8000, new ActionListener ()
        { 
            @Override
                public void actionPerformed(ActionEvent e) 
                {                 
                    jLabel1.setText("Cerrando conexion...");
                }   
        }); 
        tClosingConnextion.setRepeats(false);
        
        Timer tUpdatingGames = new Timer(6000, new ActionListener ()
        { 
            @Override
                public void actionPerformed(ActionEvent e) 
                {                 
                    jLabel1.setText("Actualizando juegos y paquetes...");
                }   
        }); 
        tClosingConnextion.setRepeats(false);
        
        Timer tFirstDownload = new Timer(6000, new ActionListener ()
        { 
            @Override
                public void actionPerformed(ActionEvent e) 
                {                 
                    jLabel1.setText("Descargando todos los paquetes y juegos por prrimera vez...");
                }   
        }); 
        tFirstDownload.setRepeats(false);
        
        Timer tLogin = new Timer(8000, new ActionListener ()
        { 
            @Override
                public void actionPerformed(ActionEvent e) 
                {    
                    jLabel1.setText("Ingresando al sistema...");
                    Flasher flash;
                    try {
                        flash = new Flasher(supp, packages);
                        flash.show();
                    } catch (JSONException ex) {
                        Logger.getLogger(Updating.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                    Updating.this.setVisible(false);
                }   
        }); 
        tLogin.setRepeats(false);
        
        Timer tNothingToUpdate = new Timer(6000, new ActionListener ()
        { 
            @Override
                public void actionPerformed(ActionEvent e) 
                {                 
                    jLabel1.setText("No hay nuevas actulizaciones...");
                    
                }   
        }); 
        tNothingToUpdate.setRepeats(false);        
        
        boolean differences = false;
        boolean firstDownload = false;
        String text = "";
        int gameNumber = 0;
        
        int packageNumber = 0;
        
        InputStream in = null;
        in = new URL("http://"+server+"//api//v1//games//?api_token=" + supp.getApiToken()).openStream();
        
        text = IOUtils.toString( in ) ;
        IOUtils.closeQuietly(in);
        
        JSONArray jsonArray = new JSONArray(text); 
        int countOfGames = jsonArray.length();
        
        in = new URL("http://"+server+"//api//v1//packages//?api_token=" + supp.getApiToken()).openStream();
        
        text = IOUtils.toString( in ) ;
        IOUtils.closeQuietly(in);
        
        JSONArray jsonPackages = new JSONArray(text); 
        int countOfPackages = jsonPackages.length(); 
        
        for (int i = 0; i < jsonArray.length(); i++) 
        {
            JSONObject explrObject = jsonArray.getJSONObject(i);
            games.add(new Game(explrObject.getString("name"),explrObject.getString("description"),explrObject.getInt("id"), explrObject.getInt("version")));
        }
        
        int id = 0;
        
        for (int i = 0; i < jsonPackages.length(); i++) 
        {
            JSONObject explrObject = jsonPackages.getJSONObject(i);
            packages.add(new Package(explrObject.getString("name"), explrObject.getInt("id"),explrObject.getInt("price")));
        }
        
        String pathOfFiles = System.getProperty("user.dir") + File.separator + "Files" + File.separator;
        File folderOfFiles = new File(pathOfFiles);
        
//      Here we make the ckeck of the existence of the folder, if it 's dosen't exist the program download all the files  
        if(!folderOfFiles.exists()){
            firstDownload = true;
            new File(pathOfFiles).mkdir();
//          Here we start the downloading of all the games and packages  
            do{
                JSONObject explrObject = jsonArray.getJSONObject(gameNumber);
                String game = "game" + explrObject.getInt("id") + "//";
                pathOfFiles = System.getProperty("user.dir") + File.separator + "Files" + File.separator + game;
                folderOfFiles = new File(pathOfFiles);
//              First we check if the main file exist, if it does not we download all the files because its the first time that the provider use the program  
                if(!folderOfFiles.exists()){
                    new File(pathOfFiles).mkdir();

//                  Here we copy the 4 images of the preview of the game 
                    for (int i=1; i<5; i++){
                        File fileToCopy = new File(folderOfFiles,"image" + Integer.toString(i) + ".png");
                        URL dir = new URL("http://fdda2013.web44.net//" + game + "//image" + Integer.toString(i) + ".png");
                        FileUtils.copyURLToFile(dir, fileToCopy);
                    }

//                  Here we copy te game that is encrypted so it does not matter that the provider has acces to it
                    File fileToCopy = new File(folderOfFiles,"game.zip");
                    URL dir = new URL("http://fdda2013.web44.net//" + game + "//game.zip");
                    FileUtils.copyURLToFile(dir, fileToCopy);
//                  Finally we get the version & info text an d the logo of the game
                    fileToCopy = new File(folderOfFiles,"version.txt");
                    dir = new URL("http://fdda2013.web44.net//" + game + "version.dat");
                    FileUtils.copyURLToFile(dir, fileToCopy);
                    fileToCopy = new File(folderOfFiles,"info.txt");
                    dir = new URL("http://fdda2013.web44.net//" + game + "info.dat");
                    FileUtils.copyURLToFile(dir, fileToCopy);
                    fileToCopy = new File(folderOfFiles,"logo.png");
                    dir = new URL("http://fdda2013.web44.net//" + game + "logo.png");
                    FileUtils.copyURLToFile(dir, fileToCopy);
                }
                gameNumber++;
            }while(gameNumber<countOfGames);
            
            do{
                JSONObject explrObject = jsonPackages.getJSONObject(packageNumber);
                String packageString = "pk" + explrObject.getInt("id") + "//";
                pathOfFiles = System.getProperty("user.dir") + File.separator + "Files" + File.separator + packageString;
                folderOfFiles = new File(pathOfFiles);
//              First we check if the main file exist, if it does not we download all the files because its the first time that the provider use the program  
                if(!folderOfFiles.exists()){
                    new File(pathOfFiles).mkdir();
                    File fileToCopy = new File(folderOfFiles,"package.txt");
                    URL dir = new URL("http://fdda2013.web44.net//" + packageString + "package.dat");
                    FileUtils.copyURLToFile(dir, fileToCopy);
                    fileToCopy = new File(folderOfFiles,"logo.png");
                    dir = new URL("http://fdda2013.web44.net//" + packageString + "logo.png");
                    FileUtils.copyURLToFile(dir, fileToCopy);
                }
                packageNumber++;
            }while(packageNumber<countOfPackages);
            
        }
        
//      The provider already has some files downloaded so we only update the games that has changed  
        else{
            gameNumber = 0;
            do{
                JSONObject explrObject = jsonArray.getJSONObject(gameNumber);
                String game = "game" + explrObject.getInt("id");
                pathOfFiles = System.getProperty("user.dir") + File.separator + "Files" + File.separator + game + File.separator ;
                folderOfFiles = new File(pathOfFiles);
                File fileToRead = new File(folderOfFiles,"version.txt");
                BufferedReader br = new BufferedReader(new FileReader(fileToRead));
                try {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();
                    while (line != null) {
                        sb.append(line);
                        line = br.readLine();
                    }
                    String version = sb.toString();
                    int ver = Integer.parseInt(version);
                    for(int i=0; i<countOfGames; i++){
                        if(games.get(i).getName().equals(game))
//                          If the version of the game that the provider has in his computer is older than the one that is on the server we download all the new files  
                            if (games.get(i).getVersion()>ver){
                                differences = true;
                                File fileToCopy = new File(folderOfFiles,"info.txt");
                                URL dir = new URL("http://fdda2013.web44.net//" + game + "//info.txt");
                                FileUtils.copyURLToFile(dir, fileToCopy);
                                
                                fileToCopy = new File(folderOfFiles,"game.zip");
                                dir = new URL("http://fdda2013.web44.net//" + game + "//game.zip");
                                FileUtils.copyURLToFile(dir, fileToCopy);
                                
                                fileToCopy = new File(folderOfFiles,"logo.png");
                                dir = new URL("http://fdda2013.web44.net//" + game + "//logo.png");
                                FileUtils.copyURLToFile(dir, fileToCopy);
                                
                                for (int j=1; j<5; j++){
                                    fileToCopy = new File(folderOfFiles,"image" + Integer.toString(j) + ".png");
                                    dir = new URL("http://fdda2013.web44.net//" + game + "//image" + Integer.toString(j) + ".png");
                                    FileUtils.copyURLToFile(dir, fileToCopy);
                                }
                                
                                fileToCopy = new File(folderOfFiles,"version.txt");
                                dir = new URL("http://fdda2013.web44.net//" + game + "//version.dat");
                                FileUtils.copyURLToFile(dir, fileToCopy);
                            };
                    }
                } 
                finally {
                    br.close();
                }
                gameNumber++;
            }while(gameNumber<countOfGames);
            
        }
        
        if (differences)
        {
            if (firstDownload) tFirstDownload.start();
            else tUpdatingGames.start();
        }
        else tNothingToUpdate.start();
        
        tClosingConnextion.start();
        
        tLogin.start();
        
    }
    
}
