package com.dongsan.domains.hashtag.repository;

import com.dongsan.domains.hashtag.entity.Hashtag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    List<Hashtag> findByNameIn(List<String> names);

    Optional<Hashtag> findByName(String name);
}
