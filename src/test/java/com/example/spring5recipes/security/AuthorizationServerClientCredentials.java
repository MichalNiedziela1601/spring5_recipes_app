package com.example.spring5recipes.security;

import com.example.spring5recipes.Spring5RecipesApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Spring5RecipesApplication.class)
@ContextConfiguration
//@DataJpaTest
public class AuthorizationServerClientCredentials {

    private final String CLIENT_ID = "client";
    private final String SECRET = "secret";

    @Autowired
    WebApplicationContext wac;

//    @Autowired
//    private FilterChainProxy springSecurityFilterChain;


    MockMvc mvc;

    @Before
    public void setup() throws Exception {
//        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void givenNotAuthWhenSecureUrlThen401() throws Exception {

        mvc.perform(get("/uoms")).andExpect(status().isUnauthorized());
    }

//    @Test
    public void givenAuthorizationWhenSecureUrlThenReturnResult() throws Exception {

        String accessToken = obtainAccessToken("user","password");

        mvc.perform(get("/uoms").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, SECRET))
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Response: " + resultString);

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
