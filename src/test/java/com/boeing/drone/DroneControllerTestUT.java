package com.boeing.drone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(DroneController.class)
public class DroneControllerTestUT {

    @Autowired
    MockMvc mvc;

    @MockBean
    private DroneRepository droneRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldGetDrones() throws Exception{
        // Setup
        Drone expected = new Drone();
        expected.setTitle("Get Title");
        List<Drone> drones = new ArrayList<>();
        drones.add(expected);
        when(this.droneRepository.findAll()).thenReturn(drones);

        // Exercise
        mvc.perform(get("/drone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Get Title")));

    }

    @Test
    public void shouldPostDrone() throws Exception {
        //Setup
        Drone expected = new Drone();
        expected.setTitle("Post Title");

        when(this.droneRepository.save(Mockito.any(Drone.class))).thenReturn(expected);

        //Exercise
         mvc.perform(post("/drone")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Post Title\",\"firstFlight\":\"2017-07-30\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Post Title")));
    }

    @Test
    public void shouldDeleteDrone() throws Exception{
        // Setup

        // Exercise
        mvc.perform(delete("/drone/21"))
                .andExpect(status().isOk());

        // Assert
        verify(droneRepository, times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    public void ShouldUpdateDrone() throws Exception {
        // Setup
        Drone initial = new Drone();
        initial.setTitle("Get Title");
        initial.setId(1L);
        List<Drone> drones = new ArrayList<>();
        drones.add(initial);

        Drone update = new Drone();
        update.setTitle("Update Title");


        when(this.droneRepository.save(Mockito.any(Drone.class))).thenReturn(update);

        // Exercise

        mvc.perform(put("/drone/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Update Title\",\"firstFlight\":\"2017-07-30\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Update Title")));
    }

}
