import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class Simulator {
    private static final double POLICE_RATE = 0.1;
    private static final double CRASH_OBEYING_RATE = 0.002 / 365.;
    private static final double CRASH_BREAKING_RATE = 0.2 / 365.;
    private ArrayList<Agent> agents = new ArrayList<>();
    private Random random = new Random();

    public Simulator(int agentCount) {
        IntStream.range(0, agentCount).forEach(i -> agents.add(new Agent(this)));
    }

    public void preceedTime() {
        agents.stream().forEach(Agent::preceedTime);

        agents.stream()
                .filter(Agent::isRuleBreaking)
                .filter(agent -> (random.nextDouble() < CRASH_BREAKING_RATE)) // 事故をおこすものを抜き出し
                .forEach(agent -> agent.crashTo(getRandomAgent(agent)));
        agents.stream()
                .filter(agent -> !agent.isRuleBreaking())
                .filter(agent -> (random.nextDouble() < CRASH_OBEYING_RATE)) // 事故をおこすものを抜き出し
                .forEach(agent -> agent.crashTo(getRandomAgent(agent)));

        agents.stream()
                .filter(Agent::isRuleBreaking)
                .forEach(agent -> {
                    if (random.nextDouble() < POLICE_RATE) {
                        agent.caughtByPolice();
                    } else {
                        agent.enjoyBreakingRule();
                    }
                });
    }

    private Agent getRandomAgent(Agent crashedBy) {
        while (true) {
            Agent crashedTo = agents.get(random.nextInt(agents.size()));
            if (crashedBy != crashedTo) {
                return crashedTo;
            }
        }
    }

    public Agent getMaxBenefitAgent() {
        return agents.stream().max((a, b) -> a.getBenefit() - b.getBenefit()).get();
    }

    public Random getRandom() {
        return random;
    }

    public double getAverageBenefit() {
        return agents.stream().mapToInt(Agent::getBenefit).average().getAsDouble();
    }

    public double getAverageBreakingRule() {
        return agents.stream().mapToInt(agent -> agent.isRuleBreaking() ? 1 : 0).average().getAsDouble();
    }
}
