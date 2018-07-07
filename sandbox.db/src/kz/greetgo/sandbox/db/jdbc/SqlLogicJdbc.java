package kz.greetgo.sandbox.db.jdbc;

import kz.greetgo.db.ConnectionCallback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SqlLogicJdbc<ReturnType, ItemClassType> implements ConnectionCallback<ReturnType> {

  public SqlLogicJdbc() {
  }

  @Override
  public abstract ReturnType doInConnection(Connection con) throws Exception;

  final protected void prepareSql() {
    select();
    from();
    innerJoin();
    leftJoin();
    where();
    groupBy();
    orderBy();
    limit();
    offset();
  }

  protected abstract void select();

  protected abstract void innerJoin();

  protected abstract void leftJoin();

  protected abstract void where();

  protected abstract void from();

  protected abstract void orderBy();

  protected abstract void groupBy();

  protected abstract void limit();

  protected abstract void offset();

  protected abstract ItemClassType fromRs(ResultSet rs) throws SQLException;
}
