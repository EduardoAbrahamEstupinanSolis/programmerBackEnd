package digital.ctm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.jcraft.jsch.Channel; 
import com.jcraft.jsch.ChannelSftp; 
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.awt.*;

public class TransferSftp 
{

    public static void showMenssage(){
        JFrame frame = new JFrame("Simple GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel txtLabel = new JLabel("No apague el equipo, las carpetas de respaldo se están transfiriendo", SwingConstants.CENTER);
        txtLabel.setPreferredSize(new Dimension(500, 200));
        frame.getContentPane().add(txtLabel, BorderLayout.CENTER);

        //Display the window.
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public static void showMenssageWorkFinished(){
        JFrame frame = new JFrame("Simple GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel txtLabel = new JLabel("Transferencia terminada. Ya puede apagar el equipo", SwingConstants.CENTER);
        txtLabel.setPreferredSize(new Dimension(500, 200));
        frame.getContentPane().add(txtLabel, BorderLayout.CENTER);

        //Display the window.
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public static void showMenssageShutdownpc(){
        JFrame frame = new JFrame("Simple GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel txtLabel = new JLabel("El equipo se apagará", SwingConstants.CENTER);
        txtLabel.setPreferredSize(new Dimension(500, 200));
        frame.getContentPane().add(txtLabel, BorderLayout.CENTER);

        //Display the window.
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
/*
    public static void shutdownPc() {
        Runtime runtime = Runtime.getRuntime();
        try {

            runtime.exec("shutdown -s -t 0");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    public static void uploadDirectory(ChannelSftp channelSftp, String localDir, String remoteDirectory) throws SftpException, IOException, FileNotFoundException{
       File locaFile = new File(localDir);
      if (locaFile.isDirectory()) {
            try {
                channelSftp.mkdir(remoteDirectory);
            } catch (SftpException a) {
                //EXCEPTIONS
                System.out.println(a.id);
                System.out.println(a.getMessage());
                assert(a.id == ChannelSftp.SSH_FX_FAILURE);
                assert(a.id == 4);
            }           
          }
        File[] files = locaFile.listFiles();
        if (files != null) {
            for(File file : files){
                if (file.isDirectory()) {
                    String newDestination = remoteDirectory + "/" + file.getName();
                    uploadDirectory(channelSftp, file.getAbsolutePath(), newDestination);
                    System.out.println("Upload directory:" + file.getName());
                }else{

                    try {
                        channelSftp.put(file.getAbsolutePath(), remoteDirectory + "/" + file.getName());                    
                    } catch (SftpException e) {
                        System.out.println("Error in the file " + file.getName());
                        System.out.println(e.getMessage());
                    }
                }
            }
        }else{
            throw new IllegalArgumentException("The especified path is not a directory: " + localDir);
        }

      }

    public static void delete(String localDirectory){
        File localDir = new File(localDirectory);

        if (!localDir.exists()) {
            System.out.println("Directory does not exist: " + localDirectory);
            return;
        }
             
           File[] listFiles = localDir.listFiles();
           if(listFiles != null){
           for (File file : listFiles) {
                if (file.isDirectory()) {
                    delete(file.getAbsolutePath());
                }else{
                    file.delete();
                    System.out.println("Files delete sussesfull");
                }
           }
        }
    }

    public static void writeToFile(String file){
        try{
            FileWriter myWriter  = new FileWriter(file);
            myWriter.write("The last modified " + );
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) throws IOException
    {
        int port = 22;
        String ip = "192.168.100.44";
        String UserName = "DATA CENTER";
        String password = "Afiliados";
        String localDir = "C:\\Users\\Windows11\\Desktop\\Respaldos";
        String localDir2 = "C:\\Users\\Windows11\\Desktop";

        String remoteDir = "remote";
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = jsch.getSession(UserName, ip, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
             channelSftp = (ChannelSftp) channel;
             showMenssage();
            uploadDirectory(channelSftp, localDir, remoteDir);
               System.out.println("Transfer complet");
            delete(localDir);
            
             showMenssageWorkFinished();
             showMenssageShutdownpc();
           //  shutdownPc();
            channel.disconnect();
        } catch (JSchException | SftpException | FileNotFoundException e) {
           e.printStackTrace();                                   
        }finally{
          if(channelSftp != null){
            channelSftp.disconnect();
          }
          if (session != null) {
            session.disconnect();
          }
        }
    }
}
