package ro.poc.kafkaconsumerpostgres.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DocumentModel {
    private String title;
    private String description;
    private String author;
    private String content;
    private Integer pages;
    private LocalDateTime sentDate;
}
