package com.reins.bookstore.repository;

import com.reins.bookstore.entity.BookTag;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface BookTagRepository extends Neo4jRepository<BookTag, String> {


  @Query(
      "MATCH (t:Tag {name: $name})-[:SUBCATEGORY_OF*0..2]->(relatedTag:Tag) "
          + "RETURN DISTINCT relatedTag.name")
  List<String> findRelatedTagsByTagName(String name);

  @Query(
      "MATCH (b:Book)-[:HAS_TAG]->(tag:Tag) "
          + "WHERE tag.name IN $relatedTags "
          + "RETURN DISTINCT b.bookId")
  List<Integer> findBooksByTags(List<String> relatedTags);
}
