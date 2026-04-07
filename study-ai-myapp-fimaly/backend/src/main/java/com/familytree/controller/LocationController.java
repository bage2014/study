package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Location;
import com.familytree.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping("/member/{memberId}")
    public ApiResponse<List<Location>> getLocationsByMemberId(@PathVariable Long memberId) {
        try {
            List<Location> locations = locationService.getLocationsByMemberId(memberId);
            return ApiResponse.success(locations);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/member/{memberId}/shared")
    public ApiResponse<List<Location>> getSharedLocationsByMemberId(@PathVariable Long memberId) {
        try {
            List<Location> locations = locationService.getSharedLocationsByMemberId(memberId);
            return ApiResponse.success(locations);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/member/{memberId}/primary")
    public ApiResponse<Location> getPrimaryLocationByMemberId(@PathVariable Long memberId) {
        try {
            Location location = locationService.getPrimaryLocationByMemberId(memberId)
                    .orElseThrow(() -> new RuntimeException("Primary location not found"));
            return ApiResponse.success(location);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Location> getLocationById(@PathVariable Long id) {
        try {
            Location location = locationService.getLocationById(id)
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            return ApiResponse.success(location);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<Location> createLocation(@RequestBody Location location) {
        try {
            Location createdLocation = locationService.createLocation(location);
            return ApiResponse.success(createdLocation);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Location> updateLocation(@PathVariable Long id, @RequestBody Location locationDetails) {
        try {
            Location updatedLocation = locationService.updateLocation(id, locationDetails);
            return ApiResponse.success(updatedLocation);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteLocation(@PathVariable Long id) {
        try {
            locationService.deleteLocation(id);
            return ApiResponse.success(null, "Location deleted successfully");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
