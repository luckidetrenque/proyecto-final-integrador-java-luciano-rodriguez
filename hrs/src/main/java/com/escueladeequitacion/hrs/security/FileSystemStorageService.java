package com.escueladeequitacion.hrs.security;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation = Paths.get("uploads");

    public FileSystemStorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar la carpeta de subidas", e);
        }
    }

    @Override
    public String store(MultipartFile file, String fileName) {
        try {
            if (file.isEmpty())
                throw new RuntimeException("Archivo vacío");

            Path destinationFile = this.rootLocation.resolve(Paths.get(fileName))
                    .normalize().toAbsolutePath();

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + fileName; // Retorna la ruta relativa
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar archivo", e);
        }
    }
}
