package mage.abilities.effects.common;

import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.constants.Outcome;
import mage.game.Game;
import mage.players.Player;

import java.util.UUID;

/**
 * "Counter target spell. Its controller mills N cards."
 * <p>
 * The instructions are carried out in printed order (CR 608.2c): the countered
 * spell goes to its owner's graveyard first (CR 701.6a), then the milled cards
 * go on top of it. The target's controller is read before the counter because
 * the stack object is gone once the spell has been countered.
 */
public class CounterTargetThenMillEffect extends OneShotEffect {

    private final int millAmount;

    public CounterTargetThenMillEffect(int millAmount, String text) {
        super(Outcome.Benefit);
        this.millAmount = millAmount;
        this.staticText = text;
    }

    protected CounterTargetThenMillEffect(final CounterTargetThenMillEffect effect) {
        super(effect);
        this.millAmount = effect.millAmount;
    }

    @Override
    public CounterTargetThenMillEffect copy() {
        return new CounterTargetThenMillEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        UUID targetId = source.getFirstTarget();
        Player controller = targetId == null ? null : game.getPlayer(game.getControllerId(targetId));
        if (controller == null) {
            return false;
        }
        boolean countered = new CounterTargetEffect().apply(game, source);
        controller.millCards(millAmount, source, game);
        return countered;
    }
}
