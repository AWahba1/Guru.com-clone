package com.guru.mediaservice.controllers;


import com.guru.mediaservice.exceptions.CannotUploadFileToFirebaseException;
import com.guru.mediaservice.models.Attachment;
import com.guru.mediaservice.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Autowired
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    public Attachment addAttachment(@RequestParam("file") MultipartFile file) throws IOException {
        try{
            return fileUploadService.upload(file);
        }
        catch(Exception e){
            throw new CannotUploadFileToFirebaseException();
        }


    }
}
