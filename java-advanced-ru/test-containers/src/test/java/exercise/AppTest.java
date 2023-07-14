package exercise;

import exercise.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc

// BEGIN
@Testcontainers
@Transactional
// END
public class AppTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    // BEGIN
    @Container
    private static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("dbname")
            .withUsername("user")
            .withPassword("1235")
            .withInitScript("init.sql");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }
    // END


    @Test
    void testCreatePerson() throws Exception {
        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        post("/people")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"firstName\": \"Jackson\", \"lastName\": \"Bind\"}")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        MockHttpServletResponse response = mockMvc
                .perform(get("/people"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("Jackson", "Bind");
    }

    @Test
    void testGetPeople() throws Exception {
        MockHttpServletResponse responseGet = mockMvc
                .perform(
                        get("/people")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(responseGet.getStatus()).isEqualTo(200);
        assertThat(responseGet.getContentAsString()).isNotEmpty().isNotBlank();
    }

    @Test
    void testGetPerson() throws Exception {
        MockHttpServletResponse responseGet = mockMvc
                .perform(
                        get("/people")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();
        String body = responseGet.getContentAsString();
        List<Person> persons = mapper.readValue(body, new TypeReference<List<Person>>() {
        });
        persons.forEach(
                p -> {
                    try {
                        MockHttpServletResponse response = mockMvc
                                .perform(get("/people/{id}", p.getId()))
                                .andReturn().getResponse();
                        assertThat(p).isEqualTo(mapper.readValue(response.getContentAsString(), Person.class));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Test
    void testDeletePerson() throws Exception {
        MockHttpServletResponse responseGet = mockMvc
                .perform(
                        get("/people")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();
        String body = responseGet.getContentAsString();
        List<Person> persons = mapper.readValue(body, new TypeReference<List<Person>>() {
        });
        Person firstPerson = persons.stream().findFirst().get();
        MockHttpServletResponse responseDelete = mockMvc
                .perform(delete("/people/{id}", firstPerson.getId()))
                .andReturn()
                .getResponse();

        MockHttpServletResponse responseGetAllAfterRemove = mockMvc
                .perform(
                        get("/people")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        List<Person> personsAfterRemove = mapper.readValue(
                responseGetAllAfterRemove.getContentAsString(),
                new TypeReference<List<Person>>() {
                }
        );
        assertThat(personsAfterRemove.contains(firstPerson)).isFalse();
    }
}
