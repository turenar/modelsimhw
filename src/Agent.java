import java.util.Random;

public class Agent {
    // 守守、自分は守ったが相手は破った、その逆、どちらも破った
    public static final int[] CRASH_COST = {-1_000, -200_000, -5_000_000, -2_000_000};
    public static final int RULE_BREAKING_BENEFIT = 2;
    public static final int POLICE_COST = -100;
    private static final double IMITATE_RATE = 0.01;
    private static final double MUTATION_RATE = 0.01;

    private static int nextAgentId = 0;
    private final int agentId;
    private int benefit = 0;
    private boolean isRuleBreaking = false;
    private Simulator sim;
    private Random random;
    private double ruleBreakingRate;

    public Agent(Simulator sim) {
        agentId = nextAgentId++;
        this.sim = sim;
        random = sim.getRandom();
        ruleBreakingRate = random.nextDouble();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(agentId);
    }

    public void preceedTime() {
        if (random.nextDouble() < IMITATE_RATE) {
            ruleBreakingRate = sim.getMaxBenefitAgent().getRuleBreakingRate();
        } else if (random.nextDouble() < MUTATION_RATE) {
            ruleBreakingRate = random.nextDouble();
        }

        isRuleBreaking = random.nextDouble() < ruleBreakingRate;
    }

    public int getBenefit() {
        return benefit;
    }

    public double getRuleBreakingRate() {
        return ruleBreakingRate;
    }

    public boolean isRuleBreaking() {
        return isRuleBreaking;
    }

    public void crashTo(Agent crashed) {
        int myCostIndex = (isRuleBreaking ? 2 : 0) + (crashed.isRuleBreaking ? 1 : 0);
        int yourCoustIndex = (isRuleBreaking ? 1 : 0) + (crashed.isRuleBreaking ? 2 : 0);

        benefit += CRASH_COST[myCostIndex];
        crashed.benefit += CRASH_COST[yourCoustIndex];
    }

    public void caughtByPolice() {
        benefit += POLICE_COST;
    }

    public void enjoyBreakingRule() {
        benefit += RULE_BREAKING_BENEFIT;
    }
}
