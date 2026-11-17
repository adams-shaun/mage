package org.mage.test.cards.single.trk;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 * Shared regression for the per-turn trigger limit: the counter must survive
 * the source leaving the battlefield for a leaves-the-battlefield trigger.
 *
 * Morbid Opportunist: "Whenever one or more other creatures die, draw a card.
 * This ability triggers only once each turn."
 */
public class MorbidOpportunistTest extends CardTestPlayerBase {

    @Test
    public void testOnceEachTurnSurvivesItsOwnDeath() {
        addCard(Zone.BATTLEFIELD, playerA, "Morbid Opportunist");
        addCard(Zone.BATTLEFIELD, playerA, "Memnite"); // dies first, uses the once-per-turn trigger
        addCard(Zone.BATTLEFIELD, playerB, "Elite Vanguard"); // dies with the Opportunist
        addCard(Zone.HAND, playerA, "Doom Blade");
        addCard(Zone.HAND, playerA, "Wrath of God");
        addCard(Zone.BATTLEFIELD, playerA, "Swamp", 2);
        addCard(Zone.BATTLEFIELD, playerA, "Plains", 4);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Doom Blade", "Memnite", true); // draws one card
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Wrath of God"); // must not draw a second card
        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();

        assertHandCount(playerA, 1);
    }
}
