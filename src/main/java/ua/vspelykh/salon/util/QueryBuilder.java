package ua.vspelykh.salon.util;

public class QueryBuilder {

    protected static final String SELECT = "SELECT * FROM ";
    protected static final String INSERT = "INSERT INTO ";
    protected static final String VALUES = " VALUES ";
    @SuppressWarnings("SqlWithoutWhere")
    protected static final String DELETE = "DELETE FROM ";
    protected static final String WHERE = " WHERE ";
    protected static final String EQUAL = "=?";
    protected static final String NOT_EQUAL = "!=?";
    protected static final String INNER_JOIN = " INNER JOIN ";
    protected static final String LIMIT = " LIMIT ";
    protected static final String OFFSET = " OFFSET ";
    protected static final String ORDER_BY = " ORDER BY ";
    protected static final String NAME_ASC = "name asc";
    protected static final String NAME_DESC = "name desc";
    protected static final String LEVEL_EXP = "level asc";
    protected static final String LEVEL_YOUNG = "level desc";
    protected static final String ILIKE = " ILIKE ";
    protected static final String OR = " OR ";
    protected static final String AND = " AND ";
    protected static final String ON_CONFLICT = "  ON CONFLICT ";
    protected static final String DO_UPDATE = "  DO UPDATE ";
    protected static final String SET = "SET ";
    protected static final String HAVING = " HAVING ";
    protected static final String SEARCH_PATTERN = "'%%%s%%'";


    private StringBuilder query;

    public QueryBuilder() {
        query = new StringBuilder();
    }

    public QueryBuilder select() {
        query.append(SELECT);
        return this;
    }

    public QueryBuilder from(String table) {
        query.append(" FROM ");
        query.append(table);
        return this;
    }

    public QueryBuilder innerJoin(String table, String onCondition) {
        query.append(INNER_JOIN);
        query.append(table);
        query.append(" ON ");
        query.append(onCondition);
        return this;
    }

    public QueryBuilder where(String condition) {
        query.append(WHERE);
        query.append(condition);
        return this;
    }

    public QueryBuilder and(String condition) {
        query.append(AND);
        query.append(condition);
        return this;
    }

    public QueryBuilder orderBy(String column, boolean ascending) {
        query.append(ORDER_BY);
        query.append(column);
        if (!ascending) {
            query.append(" DESC");
        }
        return this;
    }

    public QueryBuilder offset(int offset) {
        query.append(OFFSET);
        query.append(offset);
        return this;
    }

    public QueryBuilder limit(int limit) {
        query.append(LIMIT);
        query.append(limit);
        return this;
    }

    public String build() {
        return query.toString();
    }
}