package net.turanar.stellaris.visitor;

import net.turanar.stellaris.domain.WeightModifier;

public class CustomWeightModifier extends WeightModifier {
    private final String modText;

    public CustomWeightModifier(String modText) {
        this.modText = modText;
    }

    @Override
    public String toString() {
        String format = "(×%s)";
        if (type != null) format += " %s";
        return String.format(format, modText, type != null ? type.parse(pair).replaceAll("\\n", "<br/>") : "");
    }
}
