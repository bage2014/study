package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Location;
import com.familytree.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Location>> getLocationsByMemberId(@PathVariable Long memberId) {
        List<Location> locations = locationService.getLocationsByMemberId(memberId);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/member/{memberId}/shared")
    public ResponseEntity<List<Location>> getSharedLocationsByMemberId(@PathVariable Long memberId) {
        List<Location> locations = locationService.getSharedLocationsByMemberId(memberId);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/member/{memberId}/primary")
    public ResponseEntity<Location> getPrimaryLocationByMemberId(@PathVariable Long memberId) {
        Location location = locationService.getPrimaryLocationByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("Primary location not found"));
        return ResponseEntity.ok(location);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        Location location = locationService.getLocationById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        return ResponseEntity.ok(location);
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        Location createdLocation = locationService.createLocation(location);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location locationDetails) {
        Location updatedLocation = locationService.updateLocation(id, locationDetails);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Location deleted successfully"));
    }
}
