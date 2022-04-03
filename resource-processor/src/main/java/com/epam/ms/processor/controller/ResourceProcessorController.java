package com.epam.ms.processor.controller;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/resource-processor")
public class ResourceProcessorController {

  // initial version, will be enhanced in the next task

  @PostMapping
  public ResponseEntity<List<String>> fetchMetadata(@RequestParam MultipartFile multipartFile)
      throws IOException, TikaException, SAXException {

    File file = convertMultipartToFile(multipartFile);
    FileInputStream inputStream = new FileInputStream(file);

    Metadata metadata = new Metadata();
    new Mp3Parser().parse(inputStream, new BodyContentHandler(), metadata, new ParseContext());

    inputStream.close();

    List<String> metadataList =
        Arrays.asList(
            metadata.get(TikaCoreProperties.TITLE),
            metadata.get(TikaCoreProperties.CREATOR),
            metadata.get(XMPDM.ALBUM),
            metadata.get(XMPDM.DURATION),
            metadata.get(XMPDM.RELEASE_DATE));

    return ResponseEntity.ok(metadataList);
  }

  private File convertMultipartToFile(MultipartFile multipartFile) {
    try {
      File file = new File(multipartFile.getOriginalFilename());
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(multipartFile.getBytes());
      fos.close();
      return file;
    } catch (Exception ex) {
      return null;
    }
  }
}
