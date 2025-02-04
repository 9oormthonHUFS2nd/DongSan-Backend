package com.dongsan.rdb.domains.walkway.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagJpaRepository extends JpaRepository<HashtagEntity, Long> {

    List<HashtagEntity> findByNameIn(List<String> names);

    Optional<HashtagEntity> findByName(String name);
}
