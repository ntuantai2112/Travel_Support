package travs.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FileStore {
    public static String UPLOAD_FOLDER = "./post-image";

    public static List<String> getFilePaths(List<MultipartFile> multipartFiles, String prefix) {
        List<String> images = new ArrayList<String>();
        if (multipartFiles != null) {
            for (int i = 0; i < multipartFiles.size(); i++) {
                MultipartFile imageFile = multipartFiles.get(i);
                if (imageFile != null && !imageFile.isEmpty()) {
                    try {
                        int index = Objects.requireNonNull(imageFile.getOriginalFilename()).lastIndexOf(".");
                        String ext = Objects.requireNonNull(imageFile.getOriginalFilename()).substring(index);
                        String image = prefix + System.currentTimeMillis() + "-" + i + ext;

                        Path pathAvatar = Paths.get(UPLOAD_FOLDER + File.separator + image);
                        Files.write(pathAvatar, imageFile.getBytes());

                        images.add(image);
                    } catch (IOException e) {
                        throw new RuntimeException();

                    }
                }
            }

        }
        return images;
    }

    public static String getDefaultAvatar() {
        try {
            String fileName = "avatar-" + RandomNumber.getRandomNumberString();
            BufferedImage image = ImageIO.read(new File(UPLOAD_FOLDER + "/avatarDefault.png"));
            ImageIO.write(image, "png", new File(UPLOAD_FOLDER + fileName + ".png"));

            return fileName + ".png";
        } catch (Exception e) {
            log.warn("Get Default Avatar Fail:{}", e.getMessage());
            return null;
        }
    }


    public static String getFilePath(MultipartFile multipartFile, String prefix) {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            try {
                int index = Objects.requireNonNull(multipartFile.getOriginalFilename()).lastIndexOf(".");
                String ext = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(index);
                String image = prefix + System.currentTimeMillis() + ext;

                Path pathImage = Paths.get(UPLOAD_FOLDER + File.separator + image);
                Files.write(pathImage, multipartFile.getBytes());

                return image;
            } catch (IOException e) {

            }
        }
        return null;
    }


    public static void deleteFile(String filePath) {
        if (filePath != null) {
            try {
                File avatarFile = new File(UPLOAD_FOLDER + File.separator + filePath);
                boolean b2 = avatarFile.delete();
                if (b2) {
                    avatarFile.delete();
                }
            } catch (Exception ignored) {
            }
        }
    }

}
