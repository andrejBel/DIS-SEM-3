package Simulacia.Statistiky;

import OSPStat.Stat;

import java.text.DecimalFormat;

public class StatNamed extends Stat {

    private String name_;

    public StatNamed(String name) {
        this.name_ = name;
    }

    public StatNamed(StatNamed original) {
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
