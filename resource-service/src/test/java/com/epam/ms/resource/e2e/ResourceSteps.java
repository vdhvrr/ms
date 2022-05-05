package com.epam.ms.resource.e2e;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResourceSteps {

  @Autowired private APIClient apiClient;

  private Resource mp3File;
  private HttpEntity<Map> response;

  @Given("mp3 file")
  public void generateNewAudioFile() {
    mp3File = new ClassPathResource("s1.mp3");
  }

  @When("user uploads mp3 file")
  public void uploadAudioFile() {
    response = apiClient.postResource(mp3File);
  }

  @Then("id is returned")
  public void returnResourceId() {
    assertNotNull(response.getBody().get("id"));
  }

  @Then("user can fetch metadata by resource id")
  public void getAudioMetadataByResourceId() {
    await()
        .untilAsserted(
            () -> {
              Object id = response.getBody().get("id");
              ResponseEntity<Map> response = apiClient.getMetadata(id);
              assertNotNull(response.getBody());
              assertEquals(HttpStatus.OK, response.getStatusCode());
            });
  }
}
