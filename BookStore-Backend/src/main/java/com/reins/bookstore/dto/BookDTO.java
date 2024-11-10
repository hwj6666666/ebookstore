package com.reins.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String author;
    private String description;
    private Integer price;
    private String cover;
    private Integer sales;
    private List<String> tagNames;  // 仅存储标签名

    @JsonCreator
    public BookDTO(@JsonProperty("id") Long id,
                   @JsonProperty("title") String title,
                   @JsonProperty("author") String author,
                   @JsonProperty("description") String description,
                   @JsonProperty("price") Integer price,
                   @JsonProperty("cover") String cover) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.price = price;
        this.cover = cover;
    }

}
