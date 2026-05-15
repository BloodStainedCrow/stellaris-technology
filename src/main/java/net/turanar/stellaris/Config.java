package net.turanar.stellaris;

import net.turanar.stellaris.domain.Technology;
import net.turanar.stellaris.util.StellarisYamlReader;
import net.turanar.stellaris.antlr.StellarisParser;
import net.turanar.stellaris.antlr.StellarisParserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static net.turanar.stellaris.Global.*;

@Configuration
public class Config {
    @Autowired
    StellarisParserFactory factory;

    @Bean("GLOBAL_VARIABLES")
    public Map<String,String> variables() throws IOException {
        HashMap<String,String> retval = new HashMap<>();

        parse("files/common/scripted_variables", "txt", p -> {
            System.err.println(p);
            factory.getParser(p).file().var().forEach(v -> retval.put(v.VARIABLE().getText(), v.NUMBER().getText()));
        });

        parse("files/common/technology", "txt", p -> {
            System.err.println(p);
            factory.getParser(p).file().var().forEach(v -> retval.put(v.VARIABLE().getText(), v.NUMBER().getText()));
        });

        return retval;
    }

    @Bean("GLOBAL_STRINGS")
    public Map<String,String> localisation() throws IOException {
        Map<String,String> retval = new HashMap<>();

        parseWithSubdirectories("files/localisation/english", "yml", path -> {
            System.err.println(path);
            Yaml yaml = new Yaml();
            Iterable<Object> data = yaml.loadAll(new StellarisYamlReader(path));
            Map<String,Map<Object,Object>> map = (Map<String,Map<Object,Object>>)data.iterator().next();
            Map<Object,Object> values = map.get("l_english");
            if (values == null) {
                return;
            }
            values.forEach((k, v) -> {
                retval.put(Objects.toString(k).toLowerCase(), v.toString());
            });
        });

        return retval;
    }

    @Bean("SCRIPTED_TRIGGERS")
    public Map<String, StellarisParser.PairContext> scriptedTriggers() throws IOException {
        Map<String, StellarisParser.PairContext> retval = new HashMap<>();

        parse("files/common/scripted_triggers", "txt", path -> {
            System.err.println(path);
            factory.getParser(path).file().pair().forEach(pair -> retval.put(pair.BAREWORD().getText(), pair));
        });

        return retval;
    }

    @Bean("GLOBAL_SCRIPTED_LOC")
    public Map<String, String> scriptedLocalization() throws IOException {
        Map<String, String> retval = new HashMap<>();

        parse("files/common/scripted_loc", "txt", path -> {
            if (path.getFileName().toString().equals("scripted_loc_ruloc.txt")) return; // broken file
            factory.getParser(path).file().pair().forEach(pair -> {
                if (!pair.BAREWORD().getText().equals("defined_text")) return;
                String name = null;
                String value = null;
                for (StellarisParser.PairContext p : pair.value().map().pair()) {
                    if (p.BAREWORD().getText().equals("name")) name = p.value().getText();
                    if (p.BAREWORD().getText().equals("default")) value = p.value().getText();
                }
                if (name == null || value == null) return;
                retval.put(name, value);
            });
        });

        return retval;
    }

    @Bean("technologies")
    public Map<String, Technology> technologies() {
        return new HashMap<>();
    }

}
