package com.tw.joi.delivery.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.exception.ResourceNotFoundException;
import com.tw.joi.delivery.service.InventoryService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @Test
    void shouldReturnTheHealthOfTheStore() throws Exception {
        String getUrl = "/inventory/health?storeId={storeId}";
        //add required mocking.
        String storeId="store101";

        GroceryStore store = GroceryStore.builder()
            .name("Fresh Picks")
            .outletId("store101")
            .build();

        InventoryHealthResponse response = new InventoryHealthResponse(
            store,
            "HEALTHY",
            3,
            0,
            0
        );

        when(inventoryService.fetchStoreInventoryHealth("store101"))
            .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get(getUrl, storeId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            //put meaningful assertions
            .andExpect(MockMvcResultMatchers.jsonPath("$.store.outletId", Is.is("store101")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.store.name", Is.is("Fresh Picks")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.inventoryStatus", Is.is("HEALTHY")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalProducts", Is.is(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lowStockProductCount", Is.is(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.outOfStockProductCount", Is.is(0)));

    }

    @Test
    void shouldReturnNotFoundWhenStoreDoesNotExist() throws Exception {
        String storeId = "invalidStore";

        when(inventoryService.fetchStoreInventoryHealth(storeId))
            .thenThrow(new ResourceNotFoundException(
                "Store not found for storeId: " + storeId
            ));

        mockMvc.perform(MockMvcRequestBuilders.get("/inventory/health")
                .param("storeId", storeId)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status", Is.is(404)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Is.is("Not Found")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                Is.is("Store not found for storeId: invalidStore")));
    }

    @Test
    void shouldReturnBadRequestWhenStoreIdIsMissing() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/inventory/health")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status", Is.is(400)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Is.is("Bad Request")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                Is.is("Missing required request parameter: storeId")));
    }
}