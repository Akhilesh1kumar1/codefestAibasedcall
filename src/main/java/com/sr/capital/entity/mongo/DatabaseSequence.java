package com.sr.capital.entity.mongo;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "database_sequences")
@Data
@Builder
@Getter
public class DatabaseSequence {

    @Id
    private String id;
    private long seq;
}
