package net.turanar.stellaris.visitor;

import net.turanar.stellaris.domain.Modifier;
import net.turanar.stellaris.domain.ModifierType;
import net.turanar.stellaris.domain.Technology;
import net.turanar.stellaris.domain.WeightModifier;
import net.turanar.stellaris.antlr.StellarisParser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static net.turanar.stellaris.Global.gs;

@Component
public class ModifierVisitor {

    public ArrayList<Modifier> visitPotential(StellarisParser.PairContext ctx) {
        ArrayList<Modifier> retval = new ArrayList<>();

        ctx.value().map().pair().forEach(p -> {
            try {
                Modifier m = new Modifier();
                m.type = ModifierType.valueOf(p.BAREWORD().getText());
                m.pair = p;
                retval.add(m);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        });
        return retval;
    }

    public List<WeightModifier> visitPair(Technology tech, StellarisParser.PairContext ctx) {
        List<WeightModifier> retval = new ArrayList<WeightModifier>();
        ctx.value().map().pair().forEach(p -> {
            switch(p.BAREWORD().getText()) {
                case "factor": tech.base_factor = Float.valueOf(gs(p)); break;
                case "modifier":
                    WeightModifier m = visitModifier(p);
                    if (m == null) {
                        System.err.println("Failed to read modifier!!!!");
                        break;
                    }

                    if(m.pair == null && m.factor != null) {
                        assert(tech.base_factor == 1.0f);
                        tech.base_factor = m.factor;
                    }
                    else retval.add(m);
                    break;
            }
        });
        return retval;
    }

    public WeightModifier visitModifier(StellarisParser.PairContext ctx) {
        WeightModifier retval = new WeightModifier();
        for (StellarisParser.PairContext p : ctx.value().map().pair()) {
            try {
                switch (p.BAREWORD().getText()) {
                    case "factor":
                        if (gs(p).startsWith("value:") || gs(p).startsWith("trigger:")) {
                            // FIXME(Tim Aschhoff): This value is hardcoded for now
                            if (gs(p).equals("value:tech_weight_likelihood")) {
                                retval.factor = 1.25f;
                            } else if (gs(p).equals("trigger:acquired_specimen_count")) {
                                retval = new CustomWeightModifier("<b style='color:lime'>Number of acquired specimens</b>");
                            } else {
                                System.err.println("Please handle defines! No value for define " + gs(p));
                            }
                        } else {
                            retval.factor = Float.valueOf(gs(p));
                        }
                        break;
                    case "add":
                        retval.add = Integer.valueOf(gs(p));
                        System.err.println("retval.add:" + retval.add);
                        break;
                    default:
                        retval.type = ModifierType.valueOf(p.BAREWORD().getText());
                        retval.pair = p;
                }
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

        // FIXME(Tim Aschhoff): Janky hack
        if (retval.factor == null && retval.add == null && !(retval instanceof CustomWeightModifier)) {
            return null;
        }

        return retval;
    }
}
