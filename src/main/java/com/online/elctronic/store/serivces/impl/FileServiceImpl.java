package com.online.elctronic.store.serivces.impl;

import com.online.elctronic.store.exceptions.BadApiRequest;
import com.online.elctronic.store.serivces.FileService;
import com.online.elctronic.store.validate.ImageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {


    private Logger logger = LoggerFactory.getLogger(ImageValidator.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {


        String originalFileName = file.getOriginalFilename();
        logger.info("FileName : {}", originalFileName);
        String fileName = UUID.randomUUID().toString();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileNameWithExtension = fileName+extension;
        String fullPathWithFileName = path + fileNameWithExtension;

        if (extension.equalsIgnoreCase(".png")
                || extension.equalsIgnoreCase(".jpg")
                || extension.equalsIgnoreCase(".jpeg")){

            File folder = new File(path);

            logger.info("File path is here", path);
            
            if (!folder.exists()){
                logger.info("File path is constructed", folder);
                folder.mkdirs();
            }

            //upload file at path
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;

        }else {
            throw new BadApiRequest("FIle with this "+ extension +" not allowed");
        }

    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + name;
        InputStream inputStream = new FileInputStream(fullPath);

        return inputStream;
    }
}
