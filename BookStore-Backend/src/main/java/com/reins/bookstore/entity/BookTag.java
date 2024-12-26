package com.reins.bookstore.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Tag")
@Data
public class BookTag {
  @Id private String name;
}
