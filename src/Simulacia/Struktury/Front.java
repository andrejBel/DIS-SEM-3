package Simulacia.Struktury;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Statistiky.StatistikaVazenyPriemer;

import java.util.LinkedList;
import java.util.Queue;

public class Front<T> {


    private Queue<T> front_ = new LinkedList<>();
    private StatistikaVazenyPriemer statistikaVazenyPriemer_;

    public Front(SimulacneJadro simulacneJadro) {
        front_ = new LinkedList<>();
        statistikaVazenyPriemer_ = new StatistikaVazenyPriemer(simulacneJadro);
    }

    public void add(T element) {
        this.front_.add(element);
        statistikaVazenyPriemer_.pridaj(this.front_.size());
    }

    public T poll() {
        T element = this.front_.poll();
        statistikaVazenyPriemer_.pridaj(this.front_.size());
        return element;
    }

    public Queue<T> getFront() {
        return front_;
    }

    public StatistikaVazenyPriemer getStatistikaVazenyPriemer() {
        return statistikaVazenyPriemer_;
    }

    public int size() {
        return front_.size();
    }

    public void clear() {
        front_.clear();

    }


}
