package org.example.bookstoreapp.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.bookstoreapp.BookstoreAppApplication;
import org.example.bookstoreapp.config.TestSecurityConfig;
import org.example.bookstoreapp.domain.auth.dto.request.SigninRequest;
import org.example.bookstoreapp.domain.auth.dto.request.SignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = {
                BookstoreAppApplication.class,
                TestSecurityConfig.class  // 운영 SecurityConfig 대신 이걸 강제로 사용
        }
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final String adminEmail = "admin@example.com";
    private final String adminRawPassword = "Admin1234!!";

    @Test
    @DisplayName("관리자 인증 후 admin Api 접근 성공")
    public void admin_access_success() throws Exception {
        //1.로그인
        SigninRequest request = new SigninRequest(adminEmail, adminRawPassword);

        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String bearerToken = jsonNode.get("data").asText();
        assertThat(bearerToken).isNotNull();

        // 관리자 엔드포인트 호출
        mockMvc.perform(get("/admin/test")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("관리자 인증 후 admin Api 접근 실패")
    public void access_admin_fail() throws Exception {
        String userEmail = "user123@user.com";
        String password= "userPassword1!";
        String name= "김유저";
        String nickname = "김별명";

        //1.회원가입
        SignupRequest request = new SignupRequest(userEmail, password, name, nickname);
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andReturn();

        //2.로그인
        SigninRequest signinRequest = new SigninRequest(userEmail,password);
        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signinRequest))
                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String bearerToken = jsonNode.get("data").asText();

        //3.엔드포인트 호출
        mockMvc.perform(get("/admin/test")
                .header("Authorization",bearerToken))
                .andExpect(status().isForbidden());
    }
}
