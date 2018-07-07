package kz.greetgo.sandbox.db.util.sql;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

abstract class AbstractSQL<T> {

  private static final String AND = ") \nAND (";
  private static final String OR = ") \nOR (";

  public abstract T getSelf();

  protected abstract T createNew();

  private final Map<String, T> withMap = new LinkedHashMap<>();
  private Map<String, List<Integer>> indexMap = new HashMap<>();
  private Map<String, Object> valueMap = new HashMap<>();


  public T with(String view) {
    if (withMap.containsKey(view)) {
      throw new IllegalArgumentException("Already exists " + view);
    }

    {
      T t = createNew();

      withMap.put(view, t);

      return t;
    }
  }

  public T update(String table) {
    sql().statementType = SQLStatement.StatementType.UPDATE;
    sql().tables.add(table);
    return getSelf();
  }

  public T set(String sets) {
    sql().sets.add(sets);
    return getSelf();
  }

  public T insert_into(String tableName) {
    sql().statementType = SQLStatement.StatementType.INSERT;
    sql().tables.add(tableName);
    return getSelf();
  }

  public T values(String columns, String values) {
    sql().columns.add(columns);
    sql().values.add(values);
    return getSelf();
  }

  public T select(String columns) {
    sql().statementType = SQLStatement.StatementType.SELECT;
    sql().select.add(columns);
    return getSelf();
  }

  public T select_distinct(String columns) {
    sql().distinct = true;
    select(columns);
    return getSelf();
  }

  public T delete_from(String table) {
    sql().statementType = SQLStatement.StatementType.DELETE;
    sql().tables.add(table);
    return getSelf();
  }

  public T from(String table) {
    sql().tables.add(table);
    return getSelf();
  }

  public T join(String join) {
    sql().join.add(join);
    return getSelf();
  }

  public T innerjoin(String join) {
    sql().innerJoin.add(join);
    return getSelf();
  }

  public T leftjoin(String join) {
    sql().leftOuterJoin.add(join);
    return getSelf();
  }

  public T rightjoin(String join) {
    sql().rightOuterJoin.add(join);
    return getSelf();
  }

  public T outerjoin(String join) {
    sql().outerJoin.add(join);
    return getSelf();
  }

  public T where(String conditions) {
    sql().where.add(conditions);
    sql().lastList = sql().where;
    return getSelf();
  }

  public T or() {
    sql().lastList.add(OR);
    return getSelf();
  }

  public T and() {
    sql().lastList.add(AND);
    return getSelf();
  }

  public T group_by(String columns) {
    sql().groupBy.add(columns);
    return getSelf();
  }

  public T having(String conditions) {
    sql().having.add(conditions);
    sql().lastList = sql().having;
    return getSelf();
  }

  public T order_by(String columns) {
    sql().orderBy.add(columns);
    return getSelf();
  }

  public T limit(String conditions) {
    sql().limit = conditions;
    return getSelf();
  }

  public T offset(String conditions) {
    sql().offset = conditions;
    return getSelf();
  }

  private SQLStatement sql = new SQLStatement();

  private SQLStatement sql() {
    return sql;
  }

  public <A extends Appendable> A usingAppender(A a) {
    sql().sql(a);
    return a;
  }

  public String compile() {
    StringBuilder sb = new StringBuilder();

    if (withMap.size() > 0) {
      sb.append("with ");

      boolean needComma = false;

      for (Entry<String, T> e : withMap.entrySet()) {
        String name = e.getKey();
        String sql = e.getValue().toString();

        if (needComma) sb.append("\n, ");
        needComma = true;

        sb.append(name).append(" as (");
        sb.append(sql).append(") ");
      }

      sb.append('\n');
      sb.append('\n');
    }

    sql().sql(sb);
    return parse(sb.toString());
  }

  private static class SafeAppendable {
    private final Appendable a;
    private boolean empty = true;

    public SafeAppendable(Appendable a) {
      super();
      this.a = a;
    }

    public SafeAppendable append(CharSequence s) {
      try {
        if (empty && s.length() > 0) empty = false;
        a.append(s);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return this;
    }

    public boolean isEmpty() {
      return empty;
    }

  }

  private static class SQLStatement {

    public enum StatementType {
      DELETE, INSERT, SELECT, UPDATE
    }

    StatementType statementType;
    List<String> sets = new ArrayList<>();
    List<String> select = new ArrayList<>();
    List<String> tables = new ArrayList<>();
    List<String> join = new ArrayList<>();
    List<String> innerJoin = new ArrayList<>();
    List<String> outerJoin = new ArrayList<>();
    List<String> leftOuterJoin = new ArrayList<>();
    List<String> rightOuterJoin = new ArrayList<>();
    List<String> where = new ArrayList<>();
    List<String> having = new ArrayList<>();
    List<String> groupBy = new ArrayList<>();
    List<String> orderBy = new ArrayList<>();
    List<String> lastList = new ArrayList<>();
    List<String> columns = new ArrayList<>();
    List<String> values = new ArrayList<>();
    String limit = null;
    String offset = null;
    boolean distinct;

    private void sqlClause(SafeAppendable builder, String keyword, List<String> parts, String open,
                           String close, String conjunction) {
      if (!parts.isEmpty()) {
        if (!builder.isEmpty()) builder.append("\n");
        builder.append(keyword);
        builder.append(" ");
        builder.append(open);
        String last = "________";
        for (int i = 0, n = parts.size(); i < n; i++) {
          String part = parts.get(i);
          if (i > 0 && !part.equals(AND) && !part.equals(OR) && !last.equals(AND)
            && !last.equals(OR)) {
            builder.append(conjunction);
          }
          builder.append(part);
          last = part;
        }
        builder.append(close);
      }
    }

    private void sqlClause(SafeAppendable builder, String keyword, String part, String open,
                           String close, String conjunction) {
      if (part != null) {
        if (!builder.isEmpty()) builder.append("\n");
        builder.append(keyword);
        builder.append(" ");
        builder.append(open);
        builder.append(part);
        builder.append(close);
      }
    }

    private String selectSQL(SafeAppendable builder) {
      if (distinct) {
        sqlClause(builder, "SELECT DISTINCT", select, "", "", ", ");
      } else {
        sqlClause(builder, "SELECT", select, "", "", ", ");
      }

      sqlClause(builder, "FROM", tables, "", "", ", ");
      sqlClause(builder, "JOIN", join, "", "", "\nJOIN ");
      sqlClause(builder, "INNER JOIN", innerJoin, "", "", "\nINNER JOIN ");
      sqlClause(builder, "OUTER JOIN", outerJoin, "", "", "\nOUTER JOIN ");
      sqlClause(builder, "LEFT OUTER JOIN", leftOuterJoin, "", "", "\nLEFT OUTER JOIN ");
      sqlClause(builder, "RIGHT OUTER JOIN", rightOuterJoin, "", "", "\nRIGHT OUTER JOIN ");
      sqlClause(builder, "WHERE", where, "( ", " )", " AND ");
      sqlClause(builder, "GROUP BY", groupBy, "", "", ", ");
      sqlClause(builder, "HAVING", having, "(", ")", " AND ");
      sqlClause(builder, "ORDER BY", orderBy, "", "", ", ");
      sqlClause(builder, "LIMIT", limit, "", "", "");
      sqlClause(builder, "OFFSET", offset, "", "", "");
      return builder.toString();
    }

    private String insertSQL(SafeAppendable builder) {
      sqlClause(builder, "INSERT INTO", tables, "", "", "");
      sqlClause(builder, "", columns, "(", ")", ", ");
      sqlClause(builder, "VALUES", values, "(", ")", ", ");
      return builder.toString();
    }

    private String deleteSQL(SafeAppendable builder) {
      sqlClause(builder, "DELETE FROM", tables, "", "", "");
      sqlClause(builder, "WHERE", where, "( ", " )", " AND ");
      return builder.toString();
    }

    private String updateSQL(SafeAppendable builder) {

      sqlClause(builder, "UPDATE", tables, "", "", "");
      sqlClause(builder, "SET", sets, "", "", ", ");
      sqlClause(builder, "WHERE", where, "( ", " )", " AND ");
      return builder.toString();
    }

    public String sql(Appendable a) {
      SafeAppendable builder = new SafeAppendable(a);

      if (statementType == null) {
        return null;
      }

      String answer;

      switch (statementType) {
        case DELETE:
          answer = deleteSQL(builder);
          break;

        case INSERT:
          answer = insertSQL(builder);
          break;

        case SELECT:
          answer = selectSQL(builder);
          break;

        case UPDATE:
          answer = updateSQL(builder);
          break;

        default:
          answer = null;
      }

      return answer;
    }
  }

  private String parse(String query) {
    int length = query.length();
    StringBuilder parsedQuery = new StringBuilder(length);
    boolean inSingleQuote = false;
    boolean inDoubleQuote = false;
    int index = 1;

    for (int i = 0; i < length; i++) {
      char c = query.charAt(i);
      if (inSingleQuote) {
        if (c == '\'') {
          inSingleQuote = false;
        }
      } else if (inDoubleQuote) {
        if (c == '"') {
          inDoubleQuote = false;
        }
      } else {
        if (c == '\'') {
          inSingleQuote = true;
        } else if (c == '"') {
          inDoubleQuote = true;
        } else if (c == ':' && i + 1 < length &&
          Character.isJavaIdentifierStart(query.charAt(i + 1))) {
          int j = i + 2;
          while (j < length && Character.isJavaIdentifierPart(query.charAt(j))) {
            j++;
          }
          String name = query.substring(i + 1, j);
          c = '?';
          i += name.length();

          List<Integer> indexList = indexMap.computeIfAbsent(name, k -> new LinkedList<>());
          indexList.add(index);

          index++;
        }
      }
      parsedQuery.append(c);
    }
    for (Entry<String, List<Integer>> entry : indexMap.entrySet()) {
      List list = entry.getValue();
      int[] indexes = new int[list.size()];
      int i = 0;
      for (Object aList : list) {
        Integer x = (Integer) aList;
        indexes[i++] = x;
      }
      entry.setValue(Arrays.stream(indexes).boxed().collect(Collectors.toList()));
    }

    return parsedQuery.toString();
  }

  private List<Integer> getIndexes(String name) {
    List<Integer> indexes = indexMap.get(name);
    if (indexes == null) {
      throw new IllegalArgumentException("Parameter not found: " + name);
    }
    return indexes;
  }

  private void setObject(String name, Object value, PreparedStatement ps) throws SQLException {
    List<Integer> indexes = getIndexes(name);
    for (Integer index : indexes) {
      ps.setObject(index, value);
    }
  }


  private void setString(String name, String value, PreparedStatement ps) throws SQLException {
    List<Integer> indexes = getIndexes(name);
    for (Integer index : indexes) {
      ps.setString(index, value);
    }
  }


  private void setInt(String name, int value, PreparedStatement ps) throws SQLException {
    List<Integer> indexes = getIndexes(name);
    for (Integer index : indexes) {
      ps.setInt(index, value);
    }
  }


  private void setLong(String name, long value, PreparedStatement ps) throws SQLException {
    List<Integer> indexes = getIndexes(name);
    for (Integer index : indexes) {
      ps.setLong(index, value);
    }
  }

  private void setTimestamp(String name, Timestamp value, PreparedStatement ps) throws SQLException {
    List<Integer> indexes = getIndexes(name);
    for (Integer index : indexes) {
      ps.setTimestamp(index, value);
    }
  }

  private void setDate(String name, Date value, PreparedStatement ps) throws SQLException {
    List<Integer> indexes = getIndexes(name);
    for (Integer index : indexes) {
      if (value instanceof java.sql.Date)
        ps.setDate(index, (java.sql.Date) value);
      else
        ps.setTimestamp(index, new Timestamp(value.getTime()));
    }
  }

  public void setValue(String name, Object value) {
    valueMap.put(name, value);
  }

  public PreparedStatement applyParameter(PreparedStatement ps) throws SQLException {
    for (Entry<String, Object> entry : valueMap.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      if (value instanceof Integer) {
        setInt(key, (Integer) value, ps);
      } else if (value instanceof String) {
        setString(key, (String) value, ps);
      } else if (value instanceof Timestamp) {
        setTimestamp(key, (Timestamp) value, ps);
      } else if (value instanceof Long) {
        setLong(key, (Long) value, ps);
      } else if (value instanceof java.sql.Date) {
        setDate(key, (java.sql.Date) value, ps);
      } else if (value instanceof Date) {
        setDate(key, (Date) value, ps);
      } else
        setObject(key, value, ps);
    }
    return ps;
  }

}