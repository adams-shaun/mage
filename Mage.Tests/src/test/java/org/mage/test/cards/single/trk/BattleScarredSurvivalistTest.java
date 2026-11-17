package org.mage.test.cards.single.trk;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 * "Whenever another creature dies, put a +1/+1 counter on this creature.
 * This ability triggers only once each turn."
 */
public class BattleScarredSurvivalistTest extends CardTestPlayerBase {

    @Test
    public void testOnceEachTurnSurvivesItsOwnDeath() {
        addCard(Zone.BATTLEFIELD, playerA, "Battle-Scarred Survivalist");
        addCard(Zone.BATTLEFIELD, playerA, "Memnite"); // dies first, uses the once-per-turn trigger
        addCard(Zone.BATTLEFIELD, playerB, "Elite Vanguard"); // dies with the Survivalist
        addCard(Zone.HAND, playerA, "Doom Blade");
        addCard(Zone.HAND, playerA, "Wrath of God");
        addCard(Zone.BATTLEFIELD, playerA, "Swamp", 2);
        addCard(Zone.BATTLEFIELD, playerA, "Plains", 4);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Doom Blade", "Memnite", true); // uses the trigger
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Wrath of God");
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA, true); // resolve only the sweeper
        checkStackSize("no second Survivalist trigger", 1, PhaseStep.PRECOMBAT_MAIN, playerA, 0);
        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();
    }
}
