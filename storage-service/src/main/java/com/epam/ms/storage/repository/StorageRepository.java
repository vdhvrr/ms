package com.epam.ms.storage.repository;

import com.epam.ms.storage.model.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storage, Long> {}
