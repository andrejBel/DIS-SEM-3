package Simulacia.Statistiky;

import OSPStat.Stat;

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

}
