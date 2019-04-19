package Statistiky;

import OSPABA.Simulation;
import OSPStat.WStat;

import java.text.DecimalFormat;

public class WStatNamed extends WStat {

    private String name_;

    public WStatNamed(Simulation sim, String name) {
        super(sim);
        this.name_ = name;
    }

    public WStatNamed(WStatNamed original) {
        super(original);
        this.name_ = original.name_;
    }

    public String getName() {
        return name_;
    }

    public StatistikaInfo getStatistikaInfo() {
        DecimalFormat decimalFormat = new DecimalFormat(".0000");
        if (this.sampleSize() < 2.0) {
            return new StatistikaInfo(this.name_, decimalFormat.format(this.mean()));
        } else {
            double[] confidenceInteval = this.confidenceInterval_90();
            double mean = this.mean();
            double difference = confidenceInteval[1] - mean;
            return new StatistikaInfo(this.name_,
                    decimalFormat.format(confidenceInteval[0]) + "; " +
                            decimalFormat.format(mean) + " ±" +
                            decimalFormat.format(difference) + " ; " +
                            decimalFormat.format(confidenceInteval[1])
            );
        }
    }

}
