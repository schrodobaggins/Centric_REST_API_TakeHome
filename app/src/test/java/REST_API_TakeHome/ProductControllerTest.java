package REST_API_TakeHome;

import REST_API_TakeHome.entity.Product;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes=ProductServer.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void PostAndGet() throws Exception {
        Product product;

        UUID uuid = UUID.randomUUID();
        List<String> tags = Arrays.asList("red", "shirt", "slim fit");
        String createdAt = Utilities.getCurrentDate();
        product = new Product(
                uuid,
                "Red shirt",
                "Red hugo boss shirt",
                "Hugo Boss",
                tags,
                "apparel",
                createdAt
        );

        mvc.perform(
                        post("/v1/products")
                                .content(Utilities.getJsonString(product))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        // timeout to ensure that POST request goes through
        TimeUnit.SECONDS.sleep(1);

        mvc.perform(
                        get("/v1/products")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].name").value("Red shirt"))
                .andExpect(jsonPath("$.data[*].description").value("Red hugo boss shirt"))
                .andExpect(jsonPath("$.data[*].brand").value("Hugo Boss"))
                .andExpect(jsonPath("$.data[*].tags").exists())
                .andExpect(jsonPath("$.data[*].category").value("apparel"));
    }



    @Test
    @Order(2)
    public void insertMany() throws Exception {
        int numToInsert = 5;
        for (int i = 1; i <= numToInsert; i++) {
            Product p;

            UUID uuid = UUID.randomUUID();
            List<String> tags = Arrays.asList("sports", "cap");
            String createdAt = Utilities.getCurrentDate();
            p = new Product(
                    uuid,
                    "Hat #" + i,
                    "Just a normal hat",
                    "Thrasher",
                    tags,
                    "hats",
                    createdAt
            );

            mvc.perform(
                            post("/v1/products")
                                    .content(Utilities.getJsonString(p))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        // timeout to ensure that POST request goes through
        TimeUnit.SECONDS.sleep(1);

        mvc.perform(
                        get("/v1/products")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(numToInsert + 1));
    }
    @Test
    @Order(3)
    public void testNullInput() throws Exception {
        Product product;

        // Dummy Product for Tests
        UUID uuid = UUID.randomUUID();
        List<String> tags = Arrays.asList("red", "shirt", "slim fit");
        String createdAt = Utilities.getCurrentDate();
        product = new Product(
                uuid,
                "Red Shirt",
                "Red hugo boss shirt",
                "Hugo Boss",
                tags,
                "apparel",
                createdAt
        );
        mvc.perform(post("/v1/products/input", 42L)
                        .contentType("application/json")
                        .param("name", "Red Shirt")
                        .param("brand", "Hugo Boss")
                        .param("description", "Red Hugo Boss Shirt")
                        .param("tags", "red", "shirt")
                        .param("category", "apparel")
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound());
    }
    @Test
    @Order(4)
    void testViewAll() throws Exception{
        mvc.perform(get("/v1/products")
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}