package net.turanar.stellaris.domain;

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

        return String.format(format, mod, type != null ? type.parse(pair).replaceAll("\\n","<br/>") : "");
    }
}
