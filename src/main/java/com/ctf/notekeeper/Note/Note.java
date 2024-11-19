package com.ctf.notekeeper.Note;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notes")
public class Note {
    @Id
    private Integer id;
    private String description;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer userId;
}
