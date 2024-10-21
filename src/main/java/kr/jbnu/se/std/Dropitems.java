package kr.jbnu.se.std;

public class Dropitems {
    private int dckslow;
    private int attplus;
    private int spdplus;
    private int timer;
    public Dropitems(int dckslow, int attplus, int spdplus, int timer){
        this.dckslow = dckslow;
        this.attplus = attplus;
        this.spdplus = spdplus;
        this.timer = timer;
    }
    public static class Chicken extends Dropitems {
        public Chicken(int dckslow, int attplus, int spdplus, int timer) {
            super(1, 1, 100_000_000, 2_000_000_000);
        }
    }
    public static class Coke extends Dropitems {
        public Coke(int dckslow, int attplus, int spdplus, int timer) {
            super(0, 2, 200_000_000, 1_500_000_000);
        }
    }
    public static class Pizza extends Dropitems {
        public Pizza(int dckslow, int attplus, int spdplus, int timer) {
            super(2, 1, 200_000_000, 2_000_000_000);
        }
    }
}
