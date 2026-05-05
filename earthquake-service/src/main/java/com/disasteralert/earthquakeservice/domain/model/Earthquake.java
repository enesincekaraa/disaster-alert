package com.disasteralert.earthquakeservice.domain.model;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Earthquake {

    private String id;
    private LocalDateTime time;
    private Double latitude;
    private Double longitude;
    private Double depth;
    private Double magnitude;
    private String location;
    private String source;

    public boolean isSignificant(double minMagnitude) {
        return this.magnitude >= minMagnitude;
    }

    public boolean isShallow() {
        return this.depth <= 35.0;
    }
}
