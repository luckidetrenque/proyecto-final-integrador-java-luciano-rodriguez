package com.escueladeequitacion.hrs.security;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file, String fileName);
}
