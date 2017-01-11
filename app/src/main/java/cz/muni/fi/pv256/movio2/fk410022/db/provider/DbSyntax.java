package cz.muni.fi.pv256.movio2.fk410022.db.provider;

public class DbSyntax {

    public static String equalsTo(String field) {
        return String.format("%s = ?", field);
    }
}
