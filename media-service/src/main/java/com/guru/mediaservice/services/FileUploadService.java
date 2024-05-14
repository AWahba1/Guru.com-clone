package com.guru.mediaservice.services;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.guru.mediaservice.exceptions.CannotUploadFileToFirebaseException;
import com.guru.mediaservice.models.Attachment;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadService {

    @Value("${firebase.bucket.name}")
    private String firebaseBucketName;



    private String uploadFile(File file, String fileName) throws IOException {
        log.info("Bucket Name: "+ firebaseBucketName);

        BlobId blobId = BlobId.of(firebaseBucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        InputStream inputStream = FileUploadService.class.getClassLoader().getResourceAsStream("guruclone-bd66a-firebase-adminsdk-od8sq-da6d252435.json"); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/"+firebaseBucketName+"/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public Attachment upload(MultipartFile multipartFile) throws IOException {

        String originalFileName = multipartFile.getOriginalFilename();
        String identifier = UUID.randomUUID().toString();
        String firebaseFileName = identifier.concat(this.getExtension(originalFileName));

        File file = this.convertToFile(multipartFile, firebaseFileName);
        String URL = this.uploadFile(file, firebaseFileName);
        file.delete();
        log.info("URL: "+ URL);

        Attachment attachment = Attachment.builder()
                                .originalFileName(originalFileName)
                                .firebaseFileName(identifier)
                                .url(URL)
                                .build();

        return attachment;

    }
}

