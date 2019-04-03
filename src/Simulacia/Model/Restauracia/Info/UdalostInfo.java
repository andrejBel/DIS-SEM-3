package Simulacia.Model.Restauracia.Info;

import java.util.Comparator;

public class UdalostInfo {

    private final long idUdalosti_;
    public final double casUdalosti_;
    public final String nazovUdalosti_;
    public final String info_;

    public UdalostInfo(long idUdalosti,double casUdalosti, String nazovUdalosti, String info) {
        this.idUdalosti_ = idUdalosti;
        this.casUdalosti_ = casUdalosti;
        this.nazovUdalosti_ = nazovUdalosti;
        this.info_ = info;
    }

    public static Comparator<UdalostInfo> GetComparator() {
        return (u1, u2) -> {
            double rozdiel = u1.casUdalosti_ - u2.casUdalosti_;
            if (rozdiel != 0.0) {
                return rozdiel > 0 ? 1 : -1;
            }
            long rozdielId = u1.idUdalosti_ - u2.idUdalosti_;
            return rozdielId > 0 ? 1 : -1;
        };
    }

}
