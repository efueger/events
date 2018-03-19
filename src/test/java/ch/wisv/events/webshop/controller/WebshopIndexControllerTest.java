package ch.wisv.events.webshop.controller;

import ch.wisv.events.EventsApplicationTest;
import ch.wisv.events.core.model.event.Event;
import ch.wisv.events.core.model.event.EventStatus;
import ch.wisv.events.core.model.product.Product;
import ch.wisv.events.core.repository.EventRepository;
import ch.wisv.events.core.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EventsApplicationTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class WebshopIndexControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ProductRepository productRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testIndex() throws Exception {
        Event event = new Event(
                "title event",
                "description",
                "location",
                10,
                10,
                "",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "short description"
        );
        Product product = new Product();
        product.setSellStart(LocalDateTime.now().minusDays(2));
        product.setSellEnd(LocalDateTime.now().plusHours(1));
        productRepository.saveAndFlush(product);

        event.setProducts(Collections.singletonList(product));
        event.setPublished(EventStatus.PUBLISHED);
        eventRepository.saveAndFlush(event);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("webshop/index"))
                .andExpect(model().attribute("orderProduct", hasProperty("products", is(new HashMap<String, Long>()))))
                .andExpect(model().attribute("events", hasSize(1)))
                .andExpect(model().attribute("events", hasItem(hasProperty("key", is(event.getKey())))))
                .andExpect(content().string(containsString("action=\"/checkout\"")));
    }

    @Test
    public void testIndexNoEvents() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("webshop/index"))
                .andExpect(model().attribute("orderProduct", hasProperty("products", is(new HashMap<String, Long>()))))
                .andExpect(model().attribute("events", hasSize(0)))
                .andExpect(content().string(containsString("action=\"/checkout\"")));
    }
}