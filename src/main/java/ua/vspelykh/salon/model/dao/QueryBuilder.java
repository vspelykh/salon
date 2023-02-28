package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.util.MasterSort;

public class QueryBuilder {

    protected static final String SELECT = "SELECT * FROM ";
    protected static final String INSERT = "INSERT INTO ";
    protected static final String VALUES = " VALUES ";
    protected static final String UPDATE = "UPDATE ";
    @SuppressWarnings("SqlWithoutWhere")
    protected static final String DELETE = "DELETE FROM ";
    protected static final String WHERE = " WHERE ";
    protected static final String EQUAL = "=?";
    protected static final String NOT_EQUAL = "!=?";
    protected static final String INNER_JOIN = " INNER JOIN ";
    protected static final String LIMIT = " LIMIT ";
    protected static final String OFFSET = " OFFSET ";
    protected static final String ORDER_BY = " ORDER BY ";
    protected static final String NAME = "name";
    protected static final String DESC = " DESC";
    protected static final String LEVEL_EXP = "level";
    protected static final String LEVEL_YOUNG = "level DESC";
    protected static final String ILIKE = " ILIKE ";
    protected static final String OR = " OR ";
    protected static final String AND = " AND ";
    protected static final String ON_CONFLICT = "  ON CONFLICT ";
    protected static final String DO_UPDATE = "  DO UPDATE ";
    protected static final String SET = "SET ";
    protected static final String HAVING = " HAVING ";
    protected static final String SEARCH_PATTERN = "%%%s%%";
    protected static final String GROUP_BY = " GROUP BY";

    protected final StringBuilder query;

    public QueryBuilder() {
        query = new StringBuilder();
    }

    public QueryBuilder select(String table) {
        query.append(SELECT).append(table);
        return this;
    }

    public QueryBuilder selectFields(String tableName, String... fields) {
        query.append("SELECT ");
        if (fields.length == 1) {
            query.append(fields[0]);
        } else {
            for (int i = 0; i < fields.length - 1; i++) {
                query.append(fields[i]).append(", ");
            }
            query.append(fields[fields.length - 1]);
        }
        query.append(" FROM ").append(tableName);
        return this;
    }

    public QueryBuilder update(String table) {
        query.append(UPDATE).append(table).append(" ");
        return this;
    }

    public QueryBuilder delete(String table) {
        query.append(DELETE).append(table);
        return this;
    }

    public QueryBuilder insert(String table, String... fields) {
        query.append(INSERT).append(table).append(" ");
        appendFieldsForInsert(fields);
        query.append(VALUES);
        appendQuestionMark(fields.length);
        return this;
    }

    public QueryBuilder insertRepeat(String table, int count, String... fields) {
        query.append(INSERT).append(table).append(" ");
        appendFieldsForInsert(fields);
        query.append(VALUES);
        for (int i = 0; i < count; i++) {
            appendQuestionMark(fields.length);
            append(", ");
        }
        query.replace(query.length() - 2, query.length(), "");
        return this;
    }

    public QueryBuilder count(String tableName) {
        query.append("SELECT COUNT(1) FROM ").append(tableName);
        return this;
    }

    private void appendQuestionMark(int length) {
        query.append("(");
        if (length > 1) {
            query.append("?,".repeat(Math.max(0, length - 1)));
        }
        query.append("?)");
    }

    private void appendFieldsForInsert(String[] fields) {
        query.append("(");
        if (fields.length == 1) {
            query.append(fields[0]);
        } else {
            for (int i = 0; i < fields.length - 1; i++) {
                query.append(fields[i]).append(", ");
            }
            query.append(fields[fields.length - 1]);
        }
        query.append(")");
    }

    public QueryBuilder innerJoin(String table, String onCondition) {
        query.append(INNER_JOIN);
        query.append(table);
        alias(table);
        query.append(" ON ");
        query.append(onCondition);
        return this;
    }

    public QueryBuilder innerJoinCondition(String condition) {
        append(INNER_JOIN).append("(").append(condition).append(")");
        return this;
    }


    public QueryBuilder groupBy(String tableName, String field, String as) {
        append(GROUP_BY).alias(tableName).append(".").append(field).append(") AS ").append(as);
        return this;
    }

    public QueryBuilder where(String field) {
        query.append(WHERE).append(field).append(EQUAL);
        return this;
    }

    public QueryBuilder whereIn(String field, int length) {
        query.append(WHERE).append(field).append(" IN");
        appendQuestionMark(length);
        return this;
    }

    public QueryBuilder whereAliasIn(String table, String field, int length) {
        append(WHERE).alias(table).append(".").append(field).append(" IN");
        appendQuestionMark(length);
        return this;
    }

    public QueryBuilder whereInCondition(String field, String sql) {
        query.append(WHERE).append(field).append(" IN(").append(sql).append(")");
        return this;
    }

    public QueryBuilder andInCondition(String tableName, String field, int length) {
        append(" AND");
        alias(tableName).append(".");
        append(field).append(" IN");
        appendQuestionMark(length);
        return this;
    }

    public QueryBuilder and(String field) {
        query.append(AND).append(field).append(EQUAL);
        return this;
    }

    public QueryBuilder andWithAlias(String tableName, String field) {
        query.append(" AND");
        alias(tableName);
        query.append(".").append(field).append(EQUAL);
        return this;
    }

    public QueryBuilder aliasIn(String tableName, String field, String condition) {
        alias(tableName);
        query.append(".").append(field).append(" IN(").append(condition).append(")");
        return this;
    }

    public QueryBuilder andWithAliasIn(String tableName, String field, String condition) {
        query.append(" AND");
        alias(tableName);
        query.append(".").append(field).append(" IN(").append(condition).append(")");
        return this;
    }

    public QueryBuilder or(String field) {
        query.append(OR).append(field).append(EQUAL);
        return this;
    }

    public QueryBuilder whereILike(String field) {
        append(WHERE).append(field).append(ILIKE).append("?");
        return this;
    }

    public QueryBuilder havingILike(String field, String pattern, String part) {
        append(HAVING).append(field).append(ILIKE).append(String.format(pattern, part));
        return this;
    }

    public QueryBuilder orILike(String field) {
        append(OR).append(field).append(ILIKE).append("?");
        return this;
    }

    public QueryBuilder iLike(String field) {
        query.append(field).append(ILIKE).append("?");
        return this;
    }

    public QueryBuilder set(String... fields) {
        query.append(SET);
        appendFields(fields);
        return this;
    }

    public QueryBuilder having(String field, String pattern, String part) {
        query.append(HAVING).append(field).append(" ").append(String.format(pattern, part));
        return this;
    }

    public QueryBuilder onConflict(String... fields) {
        query.append(ON_CONFLICT);
        appendFieldsForInsert(fields);
        return this;
    }

    public QueryBuilder alias(String forTableName) {
        String[] split = forTableName.split("_");
        StringBuilder alias = new StringBuilder();
        for (String s : split) {
            alias.append(s.charAt(0));
        }
        query.append(" ").append(alias);
        return this;
    }

    private void appendFields(String[] fields) {
        if (fields.length == 1) {
            query.append(fields[0]).append(EQUAL);
        } else {
            for (int i = 0; i < fields.length - 1; i++) {
                query.append(fields[i]).append(EQUAL).append(", ");
            }
        }
        query.append(fields[fields.length - 1]).append(EQUAL);
    }

    public QueryBuilder doUpdate(String... fields) {
        query.append(DO_UPDATE).append(SET);
        appendFields(fields);
        return this;
    }

    public QueryBuilder andNotEqual(String field) {
        query.append(AND).append(field).append(NOT_EQUAL);
        return this;
    }

    public QueryBuilder orderBy(String column) {
        query.append(ORDER_BY);
        query.append(column);
        return this;
    }

    public QueryBuilder orderBy(MasterSort sort) {
        append(ORDER_BY);
        if (sort == MasterSort.NAME_ASC) {
            append(NAME);
        } else if (sort == MasterSort.NAME_DESC) {
            append(NAME);
            desc();
        } else if (sort == MasterSort.FIRST_PRO) {
            append(LEVEL_EXP);
        } else if (sort == MasterSort.FIRST_YOUNG) {
            append(LEVEL_YOUNG);
        }
        return this;
    }

    public QueryBuilder desc() {
        query.append(DESC);
        return this;
    }

    public QueryBuilder pagination(int page, int size) {
        int offset;
        if (page == 1) {
            offset = 0;
        } else {
            offset = (page - 1) * size;
        }
        query.append(LIMIT).append(size);
        query.append(OFFSET).append(offset);
        return this;
    }

    public QueryBuilder exists(String condition) {
        query.append("SELECT EXISTS (").append(condition).append(")");
        return this;
    }

    public String build() {
        return query.toString();
    }

    public QueryBuilder append(String sql) {
        query.append(sql);
        return this;
    }
}