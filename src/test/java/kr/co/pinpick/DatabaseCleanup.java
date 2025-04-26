package kr.co.pinpick;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import kr.co.pinpick.common.extension.StringExtension;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Profile("test")
@Service
@ExtensionMethod(StringExtension.class)
@Slf4j
public class DatabaseCleanup implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;

    private Map<String, String> tableIdColumns;

    @Override
    public void afterPropertiesSet() {
        tableIdColumns = entityManager.getMetamodel().getEntities().stream()
                .collect(Collectors.toMap(
                        entity -> {
                            Table tableAnnotation = entity.getJavaType().getAnnotation(Table.class);
                            return tableAnnotation != null ? tableAnnotation.name() : entity.getName();
                        },
                        this::getIdColumnName
                ));
    }

    private String getIdColumnName(EntityType<?> entity) {
        return "id";
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        for (Map.Entry<String, String> entry : tableIdColumns.entrySet()) {
            String tableName = entry.getKey();

            if (!isTableExists(tableName)) {
                continue;
            }
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1").executeUpdate();
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    private boolean isTableExists(String tableName) {
        String checkTableQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = :tableName";
        Query query = entityManager.createNativeQuery(checkTableQuery);
        query.setParameter("tableName", tableName);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }
}