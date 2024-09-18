package com.example.demo.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AzureBlobStorageService {
    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    private final BlobServiceClient blobServiceClient;

    @Autowired
    public AzureBlobStorageService(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName)
                .getBlobClient(UUID.randomUUID() + "-" + file.getOriginalFilename());

        try (InputStream inputStream = file.getInputStream()) {
            blobClient.upload(inputStream, file.getSize(), true);
        }

        return blobClient.getBlobUrl();
    }

    public List<String> uploadFiles(MultipartFile[] files) throws IOException {
        return Stream.of(files)
                .filter(file -> !file.isEmpty() && file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty())
                .map(file -> {
                    try {
                        return uploadFile(file); // Upload file and return its URL
                    } catch (IOException e) {
                        throw new RuntimeException("File upload failed", e);
                    }
                })
                .collect(Collectors.toList());
    }

    public void deleteBlob(String blobUrl) {

        String blobName = blobUrl.substring(blobUrl.lastIndexOf('/') + 1);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        if (blobClient.exists()) {
            blobClient.delete();
            System.out.println("Blob " + blobName + " has been deleted.");
        } else {
            System.out.println("Blob " + blobName + " does not exist and cannot be deleted.");
        }
    }
}
