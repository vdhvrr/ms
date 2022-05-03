package com.epam.ms.resource.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ResourceControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ResourceController resourceController;

  @Test
  void createResource_resourceCreated() throws Exception {
    // given
    MockMultipartFile mockMultipartFile =
        new MockMultipartFile(
            "file",
            RandomStringUtils.randomAlphabetic(4) + "tmp",
            "multipart/form-data",
            RandomStringUtils.randomAlphabetic(4).getBytes());
    given(resourceController.createResource(mockMultipartFile))
        .willReturn(ResponseEntity.ok(Collections.singletonMap("id", 123L)));

    // when
    // then
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/resources")
                .file(mockMultipartFile)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void delete_returnOnlyDeleted() throws Exception {
    // given
    given(resourceController.deleteResource(Arrays.asList(1L, 2L)))
        .willReturn(ResponseEntity.ok(Collections.singletonMap("ids", List.of(1L))));

    // when
    // then
    this.mockMvc
        .perform(delete("/resources").param("id", "1,2").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(
                    objectMapper.writeValueAsString(Collections.singletonMap("ids", List.of(1L)))));
  }
}
