package com.familytree.service;

import com.familytree.model.Location;
import com.familytree.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getLocationsByMemberId(Long memberId) {
        return locationRepository.findByMemberId(memberId);
    }

    public List<Location> getSharedLocationsByMemberId(Long memberId) {
        return locationRepository.findByMemberIdAndIsSharedTrue(memberId);
    }

    public Optional<Location> getPrimaryLocationByMemberId(Long memberId) {
        List<Location> primaryLocations = locationRepository.findByMemberIdAndIsPrimaryTrue(memberId);
        return primaryLocations.isEmpty() ? Optional.empty() : Optional.of(primaryLocations.get(0));
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public Location createLocation(Location location) {
        // 如果设置为主要位置，先将该成员的其他位置设置为非主要
        if (location.isPrimary()) {
            List<Location> memberLocations = locationRepository.findByMemberId(location.getMember().getId());
            for (Location loc : memberLocations) {
                if (loc.isPrimary()) {
                    loc.setPrimary(false);
                    locationRepository.save(loc);
                }
            }
        }
        return locationRepository.save(location);
    }

    public Location updateLocation(Long id, Location locationDetails) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        // 如果设置为主要位置，先将该成员的其他位置设置为非主要
        if (locationDetails.isPrimary()) {
            List<Location> memberLocations = locationRepository.findByMemberId(location.getMember().getId());
            for (Location loc : memberLocations) {
                if (loc.isPrimary() && !loc.getId().equals(id)) {
                    loc.setPrimary(false);
                    locationRepository.save(loc);
                }
            }
        }

        location.setLatitude(locationDetails.getLatitude());
        location.setLongitude(locationDetails.getLongitude());
        location.setAddress(locationDetails.getAddress());
        location.setShared(locationDetails.isShared());
        location.setPrimary(locationDetails.isPrimary());
        location.setLocationType(locationDetails.getLocationType());
        location.setName(locationDetails.getName());

        return locationRepository.save(location);
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}
