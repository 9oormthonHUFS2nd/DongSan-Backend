package com.dongsan.common.support;

import com.dongsan.common.config.TestQueryDSLConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Set;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@Import(TestQueryDSLConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@EnableJpaAuditing
public abstract class RepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void setUp(){
        resetIds();
    }

    private void resetIds(){
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // H2 데이터 베이스 사용
        for (var entity : entities) {
            // 엔티티 클래스 이름을 소문자로 변환하고 언더스코어로 연결
            String tableName = convertToTableName(entity.getJavaType().getSimpleName());

            // 1. DELETE 명령으로 모든 데이터 삭제
            jdbcTemplate.execute("DELETE FROM PUBLIC." + tableName);

            // 2. 기본 키를 1로 초기화
            jdbcTemplate.execute("ALTER TABLE PUBLIC." + tableName + " ALTER COLUMN id RESTART WITH 1");
        }
    }

    private String convertToTableName(String className) {
        // 클래스 이름을 소문자로 변환하고 언더스코어로 변환 (CamelCase 처리)
        StringBuilder tableName = new StringBuilder();
        for (char c : className.toCharArray()) {
            if (Character.isUpperCase(c) && tableName.length() > 0) {
                tableName.append('_');
            }
            tableName.append(Character.toLowerCase(c));
        }
        return tableName.toString();
    }
}
