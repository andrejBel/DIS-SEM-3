package Statistiky;

import OSPABA.Simulation;
import OSPStat.WStat;
import Utils.Helper;

import java.text.DecimalFormat;

public class WStatNamed extends WeightStat {

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

    public String getCsvFormat() {
        DecimalFormat decimalFormat = new DecimalFormat(".0000");
        if (this.sampleSize() < 2.0) {
            return name_ + Helper.DEFAULT_SEPARATOR +
                    decimalFormat.format(this.mean()) + Helper.DEFAULT_SEPARATOR +
                    "0" + Helper.DEFAULT_SEPARATOR +
                    "0" + Helper.DEFAULT_SEPARATOR;
        } else {
            double[] confidenceInteval = this.confidenceInterval_90();
            double mean = this.mean();
            double difference = confidenceInteval[1] - mean;
            return name_ + Helper.DEFAULT_SEPARATOR +
                    decimalFormat.format(mean) + Helper.DEFAULT_SEPARATOR +
                    decimalFormat.format(confidenceInteval[0]) + Helper.DEFAULT_SEPARATOR +
                    decimalFormat.format(confidenceInteval[1]) + Helper.DEFAULT_SEPARATOR;
        }
    }

}
