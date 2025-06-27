package travs.utils;


import travs.common.SQLQueryParam;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Map;

public class EntityManagerUtils {
    public static Query buildQuery(EntityManager em, SQLQueryParam queryParam) {
        return buildQuery(em, queryParam.getSql(), queryParam.getParams());
    }

    public static Query buildQuery(EntityManager entityManager, String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql);
        params.forEach(query::setParameter);
        return query;
    }
}
