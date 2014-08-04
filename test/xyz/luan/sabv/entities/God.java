package xyz.luan.sabv.entities;

public class God {
    
    private String name;

    private Power[] powers;
    private Weakness[] weaknesses;
    
    public God(String name, Power[] powers, Weakness[] weaknesses) {
        this.name = name;
        this.powers = powers;
        this.weaknesses = weaknesses;
    }

    public String getName() {
        return name;
    }

    public Power[] getPowers() {
        return powers;
    }

    public Weakness[] getWeaknesses() {
        return weaknesses;
    }
}
