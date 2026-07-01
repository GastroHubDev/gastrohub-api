package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.UserTypeJpaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserTypeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserTypeJpaRepository userTypeJpaRepository;

    @BeforeEach
    void setUp() {
        userTypeJpaRepository.deleteAll();
    }

    @Test
    void shouldCreateUserType() throws Exception {
        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Client",
                                  "baseCategory": "CLIENT"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Client"))
                .andExpect(jsonPath("$.baseCategory").value("CLIENT"));
    }

    @Test
    void shouldListUserTypes() throws Exception {
        createUserType("Client", "CLIENT");
        createUserType("Owner", "OWNER");

        mockMvc.perform(get("/user-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldFindUserTypeById() throws Exception {
        Long id = createUserType("Owner", "OWNER");

        mockMvc.perform(get("/user-types/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Owner"))
                .andExpect(jsonPath("$.baseCategory").value("OWNER"));
    }

    @Test
    void shouldUpdateUserType() throws Exception {
        Long id = createUserType("Client", "CLIENT");

        mockMvc.perform(put("/user-types/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Owner",
                                  "baseCategory": "OWNER"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Owner"))
                .andExpect(jsonPath("$.baseCategory").value("OWNER"));
    }

    @Test
    void shouldDeleteUserType() throws Exception {
        Long id = createUserType("Client", "CLIENT");

        mockMvc.perform(delete("/user-types/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/user-types/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnConflictWhenNameAlreadyExists() throws Exception {
        createUserType("Client", "CLIENT");

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Client",
                                  "baseCategory": "CLIENT"
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "   ",
                                  "baseCategory": "CLIENT"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    private Long createUserType(String name, String baseCategory) throws Exception {
        MvcResult result = mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "%s",
                                  "baseCategory": "%s"
                                }
                                """.formatted(name, baseCategory)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("id").asLong();
    }
}