package com.bage.my.app.end.point.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Data
public class AppVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String version;
    private LocalDate releaseDate;
    private String releaseNotes;
    private String downloadUrl;
    private boolean forceUpdate;
    private String fileId;

    public AppVersion() {}


    public AppVersion(String version, LocalDate releaseDate, String releaseNotes, String downloadUrl, boolean forceUpdate, String fileId) {
        this.version = version;
        this.releaseDate = releaseDate;
        this.releaseNotes = releaseNotes;
        this.downloadUrl = downloadUrl;
        this.forceUpdate = forceUpdate;
        this.fileId = fileId;
    }
}