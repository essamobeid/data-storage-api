package com.github.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DataController.class)
public class DataControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void putObjects() throws Exception {
        MvcResult testResult = this.mockMvc
                .perform(put("/data/myRepo")
                .content(new String("something".getBytes(), StandardCharsets.UTF_8)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON.toString(), testResult.getResponse().getContentType());
        MvcResult otherResult = this.mockMvc
                .perform(put("/data/myRepo")
                .content(new String("other".getBytes(), StandardCharsets.UTF_8)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON.toString(), otherResult.getResponse().getContentType());
        JsonNode testResponse = getResponseJson(testResult);
        JsonNode otherResponse = getResponseJson(otherResult);

        assertNotEquals(testResponse.get("oid").asText(), otherResponse.get("oid").asText());
        assertEquals(testResponse.get("size").asInt(), 9);
        assertEquals(otherResponse.get("size").asInt(), 5);
    }

    @Test
    public void getObject() throws Exception {
        MvcResult testResult = this.mockMvc
                .perform(put("/data/myRepo")
                .content(new String("something".getBytes(), StandardCharsets.UTF_8)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode testResponse = getResponseJson(testResult);
        MvcResult testGetResult = this.mockMvc
                .perform(get("/data/myRepo/" + testResponse.get("oid").asText()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("something", getResponseText(testGetResult));
    }

    @Test
    public void getMissingObject() throws Exception {
        MvcResult testGetResult = this.mockMvc
                .perform(get("/data/myRepo/missing"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        assertTrue(getResponseText(testGetResult).isEmpty());
    }

    @Test
    public void deleteObjects() throws Exception {
        MvcResult testResult = this.mockMvc
                .perform(put("/data/myRepo").content(new String("something".getBytes(), StandardCharsets.UTF_8)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        MvcResult otherResult = this.mockMvc
                .perform(put("/data/otherRepo").content(new String("something".getBytes(), StandardCharsets.UTF_8)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode testResponse = getResponseJson(testResult);
        JsonNode otherResponse = getResponseJson(otherResult);

        MvcResult testDeleteResult = this.mockMvc
                .perform(delete("/data/myRepo/" + testResponse.get("oid").asText()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(getResponseText(testDeleteResult).isEmpty());

        this.mockMvc.perform(delete("/data/myRepo/" + testResponse.get("oid").asText()))
                .andDo(print())
                .andExpect(status().isNotFound());

        MvcResult testGetResult = this.mockMvc
                .perform(get("/data/otherRepo/" + otherResponse.get("oid").asText()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("something", getResponseText(testGetResult));
    }

    @Test
    public void deleteMissingObject() throws Exception {
        MvcResult testGetResult = this.mockMvc
                .perform(delete("/data/myRepo/missing"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        assertTrue(getResponseText(testGetResult).isEmpty());
    }

    private String getResponseText(MvcResult result) throws Exception {
        return result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    private JsonNode getResponseJson(MvcResult result) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }
}
