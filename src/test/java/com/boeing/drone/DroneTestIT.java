package com.boeing.drone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DroneTestIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DroneRepository droneRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup(){
        droneRepository.deleteAll();
    }

    @After
    public void after(){
        droneRepository.deleteAll();
    }

    @Test
    public void testGetDrone() throws Exception{
        // Setup
        Drone expected = new Drone();
        expected.setTitle("Test Drone");
        droneRepository.save(expected);

        // Exercise
        String response = mvc.perform(get("/drone"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<Drone> result = mapper.readValue(response, new TypeReference<List<Drone>>() {});

        // Assert
        Assert.assertEquals("GET should return a size of 1", 1, result.size());
        Drone actual = result.get(0);
        Assert.assertEquals("GET response should match what was saved to the database", expected.getTitle(), actual.getTitle());
    }

    @Test
    public void testPostDrone() throws Exception{
        // Setup
        Drone expected = new Drone();
        expected.setTitle("class test");
//        String da = "2017-07-30 00:00";
//        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
//        //df.setTimeZone(TimeZone.getDefault());
//        Date date=df.parse(da);
//        expected.setFirstFlight(date);
//        //mapper.setDateFormat(df);*/
/*        LocalDateTime date = LocalDateTime.now();
        expected.setFirstFlight(date);*/

        // Exercise
        String responses = mvc.perform(post("/drone")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"class test\",\"firstFlight\":\"2017-07-30\"}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Drone result = mapper.readValue(responses, new TypeReference<Drone>() {});

        // Assert


        Assert.assertEquals("Post should match what was saved to the database", expected.getTitle(), result.getTitle());
        //Assert.assertEquals("Post should match what was saved to the database", expected.getFirstFlight(), result.getFirstFlight());
    }

    @Test
    public void testDeleteDrone() throws Exception {
        // Setup
        Drone droneToBeDeleted = new Drone();
        droneToBeDeleted.setTitle("Delete me");
        //droneToBeDeleted.setId(1L);
        droneRepository.save(droneToBeDeleted);
        // Exercise

        String response = mvc.perform(get("/drone"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Drone> result = mapper.readValue(response, new TypeReference<List<Drone>>() {});

        Drone actual = result.get(0);
        Long id = actual.getId();

        this.mvc.perform(delete("/drone/{id}", id))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();

        List<Drone> length = droneRepository.findAll();

        // Assert
        Assert.assertEquals("Validating delete",0,length.size());
    }

    @Test
    public void testUpdateDrone() throws Exception {
        //Setup
        Drone droneToBeUpdated = new Drone();
        droneToBeUpdated.setTitle("Initial Value");
        droneRepository.save(droneToBeUpdated);

        String response = mvc.perform(get("/drone"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Drone> result = mapper.readValue(response, new TypeReference<List<Drone>>() {});

        Drone actual = result.get(0);
        Long id = actual.getId();

        String updateResponse = mvc.perform(put("/drone/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Title\",\"firstFlight\":\"2017-07-30\"}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Drone updateResult = droneRepository.findById(id).orElse(null);

        Assert.assertEquals("Validate After Update", "Updated Title", updateResult.getTitle());

    }
}
