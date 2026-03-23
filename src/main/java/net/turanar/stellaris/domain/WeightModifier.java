package net.turanar.stellaris.domain;

import static net.turanar.stellaris.Global.LS;

public class WeightModifier extends Modifier {
    @Override
    public String toString() {
        String format;
        String mod;

        if (add != null ) {
            assert(factor == null);
            if (add > 0) {
                format = "(+%s)";
                mod = "<b style='color:lime'>" + add + "</b>";
            } else {
                format = "(%s)";
                mod = "<b style='color:red'>" + add + "</b>";
            }
        } else if (factor != null) {
            format = "(×%s)";

            if (factor >= 1.0f) {
                mod = "<b style='color:lime'>" + factor + "</b>";
            } else {
                mod = "<b style='color:red'>" + factor + "</b>";
            }
        } else {
            System.err.println(this.pair.getText());
            System.exit(1);
            return "";
        }

         if(type != null) format += " %s";

        String typeStr = "";
        if (type != null) {
            if (type == ModifierType.AND && !children.isEmpty()) {
                // Render AND clause with children
                StringBuilder sb = new StringBuilder("All must be true");
                for (Modifier child : children) {
                    sb.append("<br/>")
                            .append(LS.replace("  ", "&nbsp;"))
                            .append(child.toString().replace(LS, "&nbsp;&nbsp;" + LS.replace("  ", "&nbsp;"))
                                    .replace("\n", "<br/>"));
                }
                typeStr = sb.toString();
            } else {
                typeStr = type.parse(pair).replace("\n", "<br/>");
            }
        }
        return String.format(format, mod, typeStr);
    }
}
