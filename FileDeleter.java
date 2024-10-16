package digital.ctm;

import java.io.File;

public class FileDeleter {
    public static void deleteFiles(String localDirectory) {
        File localDir = new File(localDirectory);

        if (!localDir.exists()) {
            System.out.println("Directory does not exist: " + localDirectory);
            return;
        }

        File[] listFiles = localDir.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    deleteFiles(file.getAbsolutePath());
                } else {
                    if (file.delete()) {
                        System.out.println("Successfully deleted file: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }



    public static void deleteDirectories(String dir){
       File folder = new File(dir);
       if (!folder.exists()) {
          System.out.println("the directory :" + folder.getName() + " dont exists");
       }
       String[] listFolders = folder.list();
        if(listFolders == null){
            System.err.println("The folder is null");
        }
       for (String directory : listFolders) {
            File currenFile = new File(folder.getPath(), directory);
            currenFile.delete();
              System.out.println("the directory: " + folder.getName() + "was eliminate");
                
           
       }

    }

    public static void createNewFolder(String dir){
                File directory = new File(dir);
                directory.mkdir();
                
    }

    public static void main(String[] args) {
        String dir = "C:\\Users\\Windows11\\Desktop\\Respaldos";
        deleteFiles(dir);
        deleteDirectories(dir);
    }
}
