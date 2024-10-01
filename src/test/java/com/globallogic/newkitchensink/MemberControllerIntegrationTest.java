package com.globallogic.newkitchensink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.newkitchensink.controllers.MemberDTO;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { NewKitchenSinkApplication.class })
@WebAppConfiguration
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class MemberControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesMemberController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("memberController"));
    }

    @Test
    public void givenGetMembersByIdURI_whenMockMVC_thenVerifyOkResponse() throws Exception{
        MvcResult mvcResult = this.mockMvc.perform(get("/members/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @Test
    public void givenGetMembersURI_whenMockMVC_thenVerifyOkResponse() throws Exception{
        MvcResult mvcResult = this.mockMvc.perform(get("/members"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("application/json", mvcResult.getResponse().getContentType());
        assertTrue(content.contains("2125551212"));
    }

    @Test
    public void givenPostMembersURI_whenMockMVC_thenVerifyOkResponse() throws Exception {
        String json = getPostBody();
        this.mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andDo(print())
                .andExpect(status().isCreated()).andExpect(content()
                .contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Testing dude"));
    }

    @Test
    public void givenPostMembersURI_whenMockMVC_thenVerifyNameInvalidResponse() throws Exception {
        String json = getNameInvalidPostBody();
        MvcResult mvcResult = this.mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andDo(print())
                .andExpect(status().isBadRequest()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("application/json", mvcResult.getResponse().getContentType());
        assertTrue(content.contains("must not be empty"));
    }

    @Test
    public void givenPostMembersURI_whenMockMVC_thenVerifyEmailInvalidResponse() throws Exception {
        String json = getEmailInvalidPostBody();
        MvcResult mvcResult = this.mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andDo(print())
                .andExpect(status().isBadRequest()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("application/json", mvcResult.getResponse().getContentType());
        assertTrue(content.contains("Email is not valid"));
    }

    @Test
    public void givenPostMembersURI_whenMockMVC_thenVerifyPhoneInvalidResponse() throws Exception {
        String json = getPhoneNumberInvalidPostBody();
        MvcResult mvcResult = this.mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andDo(print())
                .andExpect(status().isBadRequest()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("application/json", mvcResult.getResponse().getContentType());
        assertTrue(content.contains("Length should be between"));
    }

    @Test
    public void givenPutMembersURI_whenMockMVC_thenVerifyOkResponse() throws Exception {
        String json = getPutBody();
        this.mockMvc.perform(put("/members/1").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Testing dude"));
    }

    private String getPostBody() throws JsonProcessingException {
        MemberDTO member = new MemberDTO();
        member.setEmail("test1@test.com");
        member.setName("Testing dude");
        member.setPhoneNumber("2343450122");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(member);
    }

    private String getNameInvalidPostBody() throws JsonProcessingException {
        MemberDTO member = new MemberDTO();
        member.setEmail("test1@test.com");
        member.setPhoneNumber("2343450122");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(member);
    }

    private String getEmailInvalidPostBody() throws JsonProcessingException {
        MemberDTO member = new MemberDTO();
        member.setName("Testing dude");
        member.setEmail("test1test.com");
        member.setPhoneNumber("2343450122");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(member);
    }

    private String getPhoneNumberInvalidPostBody() throws JsonProcessingException {
        MemberDTO member = new MemberDTO();
        member.setName("Testing dude");
        member.setEmail("test1@test.com");
        member.setPhoneNumber("2343450");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(member);
    }

    private String getPutBody() throws JsonProcessingException {
        MemberDTO member = new MemberDTO();
        member.setId(1L);
        member.setEmail("test1@test.com");
        member.setName("Testing dude");
        member.setPhoneNumber("2343450122");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(member);
    }

}
