package com.epam.ms.processor.metadata;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.metadata.XMPDM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SongMetadataParserTest {

  @SpyBean public SongMetadataParser songMetadataParser;

  @Test
  public void parseSongMetadata() throws Exception {
    // given
    String resourceId = "1234";
    Resource resource = new ByteArrayResource(new byte[] {});

    Metadata metadata = new Metadata();
    metadata.set(TikaCoreProperties.TITLE, "song-name");
    metadata.set(TikaCoreProperties.CREATOR, "song-artist");
    metadata.set(XMPDM.DURATION, "song-length");
    metadata.set(XMPDM.RELEASE_DATE, "song_year");
    doReturn(metadata).when(songMetadataParser).getMetadata(resource);

    // when
    Map<String, String> metadataMap = songMetadataParser.parseSongMetadata(resourceId, resource);

    // then
    assertEquals(
        metadataMap,
        Map.of(
            "name",
            "song-name",
            "artist",
            "song-artist",
            "length",
            "song-length",
            "year",
            "song_year",
            "resourceId",
            "1234"));
  }
}
