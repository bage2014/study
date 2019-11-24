import com.bage.study.spring.boot.oauth2.server.v3.AuthorizationServerApplication;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AuthorizationServerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationClaimsIntegrationTest {


    @Autowired
    private JwtTokenStore tokenStore;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void exchange() throws Exception {
        String url = "/oauth2/token";
        HttpHeaders header = new HttpHeaders();
        String userAndPass = "bage:bage";
        header.add("Authorization", "Basic "+ Base64.encodeBase64String(userAndPass.getBytes()));

        System.out.println("Authorization:" + header.get("Authorization"));


        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", "bage");
        map.add("password", "bage");

        HttpEntity<Object> entity = new HttpEntity<Object>(map,header);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String sttr = response.getBody();

        System.out.println("retunr:" + sttr);
    }
    @Test
    public void whenTokenDoesNotContainIssuer_thenSuccess() throws Exception {
        // curl client:secret@localhost:8080/oauth/token -d grant_type=password -d username=bage -d password=bage
        String tokenValue = obtainAccessToken("client", "bage", "bage");
        OAuth2Authentication auth = tokenStore.readAuthentication(tokenValue);
//        Map<String, Object> details = (Map<String, Object>) auth.getDetails();

//        assertTrue(details.containsKey("organization"));
    }

    private String obtainAccessToken(
            String clientId, String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", "password");
        params.add("client_id", clientId);
        params.add("client_secret", "secret");
        params.add("username", username);
        params.add("password", password);

        this.mockMvc.perform(post("http://localhost:8080/oauth/token").params(params)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("true"));
        return null;
    }
}