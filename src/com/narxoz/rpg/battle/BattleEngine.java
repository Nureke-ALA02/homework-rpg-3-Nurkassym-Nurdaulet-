package com.narxoz.rpg.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BattleEngine {
    private static BattleEngine instance;
    private Random random = new Random(1L);
    private BattleEngine() {
    }
    public static BattleEngine getInstance() {
        if (instance == null) {
            instance = new BattleEngine();
        }
        return instance;
    }
    public BattleEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }
    public void reset() {
    }
    public EncounterResult runEncounter(List<Combatant> teamA, List<Combatant> teamB) {
        if (teamA == null || teamB == null || teamA.isEmpty() || teamB.isEmpty()) {
            throw new IllegalArgumentException("Both teams must have combatants.");
        }
        EncounterResult result = new EncounterResult();
        List<Combatant> a = new ArrayList<>(teamA);
        List<Combatant> b = new ArrayList<>(teamB);
        int rounds = 0;
        while (isTeamAlive(a) && isTeamAlive(b)) {
            rounds++;
            result.addLog("---- Round " + rounds + " ----");

            performRound(a, b, result);
            performRound(b, a, result);
        }
        String winner = isTeamAlive(a) ? "Heroes" : "Enemies";
        result.setWinner(winner);
        result.setRounds(rounds);
        result.addLog("Battle finished. Winner: " + winner);
        return result;
    }
    private void performRound(List<Combatant> attackers,
                              List<Combatant> defenders,
                              EncounterResult result) {
        for (Combatant attacker : attackers) {
            if (!attacker.isAlive()) continue;
            Combatant target = getRandomAlive(defenders);
            if (target == null) return;
            int damage = attacker.getAttackPower();
            target.takeDamage(damage);
            result.addLog(attacker.getName()
                    + " attacks "
                    + target.getName()
                    + " for "
                    + damage + " damage");

            if (!target.isAlive()) {
                result.addLog(target.getName() + " was defeated!");
            }
        }
    }
    private Combatant getRandomAlive(List<Combatant> team) {
        List<Combatant> alive = new ArrayList<>();
        for (Combatant c : team) {
            if (c.isAlive()) {
                alive.add(c);
            }
        }
        if (alive.isEmpty()) {
            return null;
        }
        return alive.get(random.nextInt(alive.size()));
    }
    private boolean isTeamAlive(List<Combatant> team) {
        for (Combatant c : team) {
            if (c.isAlive()) {
                return true;
            }
        }
        return false;
    }
}