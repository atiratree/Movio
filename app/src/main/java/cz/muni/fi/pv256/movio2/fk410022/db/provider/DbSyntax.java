package cz.muni.fi.pv256.movio2.fk410022.db.provider;

public interface DbSyntax {
    String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";
    String CREATE_TABLE = "CREATE TABLE ";
    String COMMA = ",";

    interface Type {
        String TEXT = " TEXT";
        String INTEGER = " INTEGER";
        String REAL = " REAL";
        String PRIMARY_KEY = " PRIMARY KEY";
    }
}
