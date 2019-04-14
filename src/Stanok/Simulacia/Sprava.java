package Stanok.Simulacia;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import Stanok.SimulaciaStanok;

public class Sprava extends MessageForm {

    private double casVstupu_ = 0.0;
    private double casKoncaCakania_ = 0.0;

    public Sprava(SimulaciaStanok mySim) {
        super(mySim);
    }

    public Sprava(Sprava original) {
        super(original);
        this.casVstupu_ = original.casVstupu_;
        this.casKoncaCakania_ = original.casKoncaCakania_;
    }

    @Override
    public MessageForm createCopy() {
        return new Sprava(this);
    }

    @Override
    public SimulaciaStanok mySim() {
        return (SimulaciaStanok) super.mySim();
    }

    public double getCasVstupu() {
        return casVstupu_;
    }

    public void setCasVstupu(double casVstupu) {
        this.casVstupu_ = casVstupu;
    }

    public double getCasKoncaCakania() {
        return casKoncaCakania_;
    }

    public void setCasKoncaCakania(double casKoncaCakania) {
        this.casKoncaCakania_ = casKoncaCakania;
    }

    public double getCasCakania() {
        return this.casKoncaCakania_ - this.casVstupu_;
    }

}
