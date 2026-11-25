package mage.cards.t;

import mage.abilities.effects.common.CounterTargetThenMillEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.target.TargetSpell;

import java.util.UUID;

/**
 * @author TheElk801
 */
public final class ThoughtCollapse extends CardImpl {

    public ThoughtCollapse(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.INSTANT}, "{1}{U}{U}");

        // Counter target spell. Its controller puts the top three cards of their library into their graveyard.
        this.getSpellAbility().addEffect(new CounterTargetThenMillEffect(
                3, "Counter target spell. Its controller mills three cards"));
        this.getSpellAbility().addTarget(new TargetSpell());
    }

    private ThoughtCollapse(final ThoughtCollapse card) {
        super(card);
    }

    @Override
    public ThoughtCollapse copy() {
        return new ThoughtCollapse(this);
    }
}
