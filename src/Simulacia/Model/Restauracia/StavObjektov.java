package Simulacia.Model.Restauracia;

import Simulacia.Model.Restauracia.Info.*;

import java.util.ArrayList;

public class StavObjektov {

    private ArrayList<UdalostInfo> kalendarUdalosti_ = new ArrayList<>();
    private BehReplikacieInfo statistikyReplikacie_ = null;

    private ArrayList<CasnikInfo> casniciInfo_ = new ArrayList<>();
    private ArrayList<KucharInfo> kucharInfo_ = new ArrayList<>();
    private ArrayList<StolInfo> stolyInfo_ = new ArrayList<>();

    private FrontInfo frontStolyCakajuceNaObsluhu_ = new FrontInfo();
    private FrontInfo frontZakazniciCakajuciNaObsluhu_ = new FrontInfo();
    private FrontInfo frontStolyCakajuceNaDonesenieJedla_ = new FrontInfo();
    private FrontInfo frontStolyCakajuceNaZaplatenie_ = new FrontInfo();


    public ArrayList<UdalostInfo> getKalendarUdalosti() {
        return kalendarUdalosti_;
    }

    public BehReplikacieInfo getStatistikyReplikacie() {
        return statistikyReplikacie_;
    }

    public void setStatistikyReplikacie(BehReplikacieInfo statistikyReplikacie) {
        this.statistikyReplikacie_ = statistikyReplikacie;
    }

    public ArrayList<CasnikInfo> getCasniciInfo() {
        return casniciInfo_;
    }

    public ArrayList<KucharInfo> getKuchariInfo() {
        return kucharInfo_;
    }

    public ArrayList<StolInfo> getStolyInfo() {
        return stolyInfo_;
    }

    public FrontInfo getFrontStolyCakajuceNaObsluhu() {
        return frontStolyCakajuceNaObsluhu_;
    }

    public FrontInfo getFrontZakazniciCakajuciNaObsluhu() {
        return frontZakazniciCakajuciNaObsluhu_;
    }

    public FrontInfo getFrontStolyCakajuceNaDonesenieJedla() {
        return frontStolyCakajuceNaDonesenieJedla_;
    }

    public FrontInfo getFrontStolyCakajuceNaZaplatenie() {
        return frontStolyCakajuceNaZaplatenie_;
    }
}
