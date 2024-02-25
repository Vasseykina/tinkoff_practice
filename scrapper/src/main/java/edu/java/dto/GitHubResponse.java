package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class GitHubResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
}
