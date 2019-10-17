import com.bage.study.spring.boot.oauth2.server.v3.AuthorizationServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

        AuthorizationEndpoint ss;
        params.add("grant_type", "password");
        params.add("client_id", clientId);
        params.add("client_secret", "secret");
        params.add("username", username);
        params.add("password", password);

        this.mockMvc.perform(post("/oauth/token").params(params)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("true"));
        return null;
    }
}