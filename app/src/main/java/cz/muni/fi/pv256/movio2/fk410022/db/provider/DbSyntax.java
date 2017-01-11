package cz.muni.fi.pv256.movio2.fk410022.db.provider;

public class DbSyntax {

    public static String equalsTo(String field) {
        return String.format("%s = ?", field);
    }

    public static String columnEquality(String tableOne, String columnOne, String tableTwo, String columnTwo) {
        return String.format("%s.%s = %s.%s", tableOne, columnOne, tableTwo, columnTwo);
    }

    public static String makeColumn(String table, String column) {
        return String.format("%s.%s", table, column);
    }

    public static String desc(String column) {
        return String.format("%s DESC", column);
    }
}
