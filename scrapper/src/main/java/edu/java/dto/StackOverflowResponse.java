package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class StackOverflowResponse {

    @JsonProperty("title")
    private String title;

    @JsonProperty("last_activity_date")
    private OffsetDateTime lastActivityDate;

}
