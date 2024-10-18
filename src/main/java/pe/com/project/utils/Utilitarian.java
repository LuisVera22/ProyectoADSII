package pe.com.project.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

public class Utilitarian {
    
    public static String saveImage(MultipartFile image){
        try {
            Path pathDire = Paths.get("src/main/resources/static/img/user_photo/");
            if (!Files.exists(pathDire)) {
                Files.createDirectories(pathDire);
            }

            byte[] bytes = image.getBytes();
            Path path = Paths.get("src/main/resources/static/img/user_photo/"+image.getOriginalFilename());

            Files.write(path, bytes);
            
            return image.getOriginalFilename();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String extractHash(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String passwordInput, String hashPassword){
        return BCrypt.checkpw(passwordInput, hashPassword);
    }
}
