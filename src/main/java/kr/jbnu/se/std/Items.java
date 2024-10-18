package kr.jbnu.se.std;

public class Items {
    private int dckslow;
    private int attplus;
    private int spdplus;
    public Items(int dckslow, int attplus, int spdplus){
        this.dckslow = dckslow;
        this.attplus = attplus;
        this.spdplus = spdplus;
    }
    public static class Chicken extends Items{
        public Chicken(int dckslow, int attplus, int spdplus) {
            super(dckslow, attplus, spdplus);
        }
    }
}
