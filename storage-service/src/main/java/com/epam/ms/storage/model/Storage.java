package com.epam.ms.storage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Storage {

  @Id
  @GeneratedValue(generator = "storage_id_seq")
  private Long id;

  @NotBlank @Column private String storageType;

  @NotBlank @Column private String bucket;

  @NotBlank @Column private String path;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStorageType() {
    return storageType;
  }

  public void setStorageType(String type) {
    this.storageType = type;
  }

  public String getBucket() {
    return bucket;
  }

  public void setBucket(String bucket) {
    this.bucket = bucket;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
