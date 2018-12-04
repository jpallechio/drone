package com.boeing.drone;

import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/drone")
public class DroneController {

    private final DroneRepository droneRepository;

    public DroneController(DroneRepository droneRepository){
        this.droneRepository = droneRepository;
    }

    @GetMapping("")
    public Iterable<Drone> all(){
        return this.droneRepository.findAll();
    }

    @PostMapping("")
    public Drone create(@RequestBody Drone drone){
        return this.droneRepository.save(drone);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        this.droneRepository.deleteById(id);
    }

    @PutMapping("{id}")
    public Drone update(@PathVariable Long id, @RequestBody Drone drone) {
        Drone droneToUpdate = this.droneRepository.findById(id).orElse(new Drone());
        droneToUpdate.setFirstFlight(drone.getFirstFlight());
        droneToUpdate.setTitle(drone.getTitle());
        return this.droneRepository.save(droneToUpdate);
    }
}
