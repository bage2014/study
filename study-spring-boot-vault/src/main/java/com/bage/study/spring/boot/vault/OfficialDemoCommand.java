package com.bage.study.spring.boot.vault;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.Versioned;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class OfficialDemoCommand implements CommandLineRunner {

  @Override
  public void run(String... strings) throws Exception {

    VaultEndpoint vaultEndpoint = new VaultEndpoint();

    vaultEndpoint.setHost("127.0.0.1");
    vaultEndpoint.setPort(8200);
    vaultEndpoint.setScheme("http");

    // Authenticate
    VaultTemplate vaultTemplate = new VaultTemplate(
            vaultEndpoint,
            new TokenAuthentication("dev-only-token")
    );
    // Write a secret
    Map<String, String> data = new HashMap<>();
    data.put("password", "Hashi123");

    Versioned.Metadata createResponse = vaultTemplate
            .opsForVersionedKeyValue("secret")
            .put("my-secret-password", data);

    System.out.println("Secret written successfully.");

    // Read a secret
    Versioned<Map<String, Object>> readResponse = vaultTemplate
            .opsForVersionedKeyValue("secret")
            .get("my-secret-password");

    String password = "";
    if (readResponse != null && readResponse.hasData()) {
      password = (String) readResponse.getData().get("password");
    }

    if (!password.equals("Hashi123")) {
      throw new Exception("Unexpected password");
    }

    System.out.println("Access granted!");

  }
}