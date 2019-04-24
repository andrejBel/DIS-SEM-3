package Statistiky;

import OSPABA.Simulation;
import OSPStat.Stat;
import OSPStat.WStat;

public class WeightStat extends WStat {

    private Simulation _sim;
    private double _prevValue;
    private double _prevTime;

    public WeightStat(Simulation sim) {
        super(sim);
        this._sim = sim;
        this._prevTime = sim.currentTime();
        this._prevValue = 0.0D;
    }

    public WeightStat(WeightStat original) {
        super(original);
        this._sim = original._sim;
        this._prevValue = original._prevValue;
        this._prevTime = original._prevTime;
    }

    public void addSample(double sample) {
        double weight = this._sim.currentTime() - this._prevTime;
        this._sum += this._prevValue * weight;
        this._sumSquare += this._prevValue * this._prevValue * weight;
        this._weightSum += weight;
        this._prevTime = this._sim.currentTime();
        this._prevValue = sample;
        this.updateMinMax(sample);
    }

    @Override
    public double mean() {

        return Math.abs(this._weightSum) > 0.0D ? this._sum / this._weightSum : 0.0D;
    }

    public void clear() {
        super.clear();
        this._prevTime = this._sim != null ? this._sim.currentTime() : 0.0D;
        this._prevValue = 0.0D;
    }

    public void setPrevTimeToCurrent() {
        this._prevTime = _sim.currentTime();
    }

}
