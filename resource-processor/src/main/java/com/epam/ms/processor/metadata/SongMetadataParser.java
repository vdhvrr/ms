package com.epam.ms.processor.metadata;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SongMetadataParser {

  public Map<String, String> parseSongMetadata(String resourceId, Resource songBytesArray)
      throws Exception {

    Metadata metadata = new Metadata();
    new Mp3Parser()
        .parse(
            songBytesArray.getInputStream(),
            new BodyContentHandler(),
            metadata,
            new ParseContext());

    return Map.of(
        "name",
        metadata.get(TikaCoreProperties.TITLE),
        "artist",
        metadata.get(TikaCoreProperties.CREATOR),
        "length",
        metadata.get(XMPDM.DURATION),
        "year",
        metadata.get(XMPDM.RELEASE_DATE),
        "resourceId",
        resourceId);
  }
}
