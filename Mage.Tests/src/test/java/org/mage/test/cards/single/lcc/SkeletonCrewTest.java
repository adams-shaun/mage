package org.mage.test.cards.single.lcc;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 * {@link mage.cards.s.SkeletonCrew Skeleton Crew} {3}{B} 3/3
 * Whenever one or more creature cards leave your graveyard, create a 2/2 black Skeleton Pirate creature token.
 * {5}{B}: Return Skeleton Crew from your graveyard to the battlefield tapped.
 * <p>
 * CR 603.10a: leaves-the-graveyard abilities look back to the state before the event, so Skeleton Crew
 * returning itself from the graveyard doesn't trigger its own ability.
 */
public class SkeletonCrewTest extends CardTestPlayerBase {

    private static final String crew = "Skeleton Crew";
    private static final String pirate = "Skeleton Pirate Token";

    @Test
    public void test_SelfReturnDoesNotTrigger() {
        setStrictChooseMode(true);

        addCard(Zone.GRAVEYARD, playerA, crew);
        addCard(Zone.BATTLEFIELD, playerA, "Swamp", 6);

        activateAbility(1, PhaseStep.PRECOMBAT_MAIN, playerA, "{5}{B}: Return");
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN);
        checkStackSize("no Skeleton Crew trigger", 1, PhaseStep.PRECOMBAT_MAIN, playerA, 0);

        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();

        assertPermanentCount(playerA, crew, 1);
        assertPermanentCount(playerA, "Skeleton Pirate Token", 0);
        assertPowerToughness(playerA, crew, 3, 3);
    }

    @Test
    public void test_OtherCreatureLeavingStillTriggers() {
        setStrictChooseMode(true);

        addCard(Zone.BATTLEFIELD, playerA, crew);
        addCard(Zone.GRAVEYARD, playerA, "Grizzly Bears");
        addCard(Zone.HAND, playerA, "It Doesn't Add Up"); // {3}{B}{B} instant, return target creature card from your graveyard to the battlefield
        addCard(Zone.BATTLEFIELD, playerA, "Swamp", 5);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "It Doesn't Add Up", "Grizzly Bears");
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN);

        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();

        assertPermanentCount(playerA, "Grizzly Bears", 1);
        assertPermanentCount(playerA, "Skeleton Pirate Token", 1);
    }
}
