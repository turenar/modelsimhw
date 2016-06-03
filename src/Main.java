public class Main {
    public static void main(String[] args) {
        Simulator sim = new Simulator(10000);
        for (int i = 0; i < 10000; i++) {
            System.out.printf("\rbenefit: %f, breakingRate: %f\n", sim.getAverageBenefit(), sim.getAverageBreakingRule());
            sim.preceedTime();
        }
    }
}
