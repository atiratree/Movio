package cz.muni.fi.pv256.movio2.fk410022.utils;

import java.util.ArrayList;
import java.util.Collection;

public class ParamUtils {

    public static Iterable<Boolean[]> getTruthTable(int n) {
        Collection<Boolean[]> parameters = new ArrayList<>(n);

        int rows = (int) Math.pow(2, n);

        for (int i = 0; i < rows; i++) {
            Boolean[] params = new Boolean[n];
            parameters.add(params);
            for (int j = n - 1; j >= 0; j--) {
                params[j] = (i / (int) Math.pow(2, j)) % 2 == 0;
            }
        }

        return parameters;
    }
}
