package net.turanar.stellaris.domain;

import net.turanar.stellaris.antlr.StellarisParser.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.turanar.stellaris.Global.*;

public enum ModifierType {
    host_has_dlc("Has DLC %s"),
    has_grand_archive_dlc(DefaultParser.SCRIPTED),
    has_astral_planes_dlc(DefaultParser.SCRIPTED),
    has_first_contact_dlc(DefaultParser.SCRIPTED),
    has_paragon_dlc(DefaultParser.SCRIPTED),
    has_nemesis(DefaultParser.SCRIPTED),
    has_machine_age_dlc(DefaultParser.SCRIPTED),
    has_overlord_dlc(DefaultParser.SCRIPTED),
    has_cosmic_storms_dlc(DefaultParser.SCRIPTED),
    has_biogenesis_dlc(DefaultParser.SCRIPTED),
    has_shroud_dlc(DefaultParser.SCRIPTED),
    has_infernals(DefaultParser.SCRIPTED),
    has_utopia(DefaultParser.SCRIPTED),
    has_ancrel(DefaultParser.SCRIPTED),

    num_ascension_perk_slots("Number of open ascension perk slots is %s %s", DefaultParser.SIMPLE_OPERATION),
    num_ascension_perk("Number of filled ascension perk slots is %s %s", DefaultParser.SIMPLE_OPERATION),
    has_ascension_perk("Has %s Ascension Perk"),
    has_authority("Has %s Authority"),
    has_blocker("Has £blocker£ Tile Blocker: %s"),
    has_technology("Has Technology: %s"),
    has_valid_civic("Has Government Civic: %s"),
    has_civic("Has Government Civic: %s"),
    has_modifier("Has the %s modifier"),
    has_ethic("Has %s Ethic"),
    has_tradition("Has %s Tradition"),
    has_country_flag((p) -> {
        switch (gs(p)) {
            case "has_encountered_psionic_auras":
                return "Has encountered Psionic Auras";
            case "payback_researching_gene_clinics":
                return i18n("origin_payback") + " origin and researching" +
                        i18n("building_medical_2") + " during " +
                        i18n("payback_out_of_warranty_chain_title") + " event";
            case "has_market_access":
                return "Has access to the Galactic Market";
            case "finish_shroud_forged_liberation_flag":
                return "Finished the " + i18n("situation_shroud_forged") + " situation by supporting the Shroud";
            case "covenant_end_of_the_cycle":
                return i18n("covenant_end_of_the_cycle");
            case "advanced_identity_creation":
                return "Finished the " + i18n("situation_digitization") + " situation";
            case "colossus_project":
                return has_ascension_perk.parse(p); // technically, only after special project is completed, but whatever
        }
        return f("Has the %s country flag", i18n(gs(p)));
    }),
    has_global_flag("Has the %s global flag"),
    has_deposit("Has deposit %s"),
    is_country_type("Is of country type: %s"),
    is_planet_class("Is %s"),
    has_communications("Has communication with our Empire"),
    pop_has_trait("Pop has trait %s"),
    has_policy_flag((p) -> f("Has policy %s", i18n(gs(p) + "_name"))),
    owns_any_bypass((p) -> f("Controls a system with a %s", i18n("bypass_" + gs(p).toLowerCase()))),
    has_seen_any_bypass((p) -> f("Has encountered a %s", i18n("bypass_" + gs(p).toLowerCase()))),

    is_xenophile(DefaultParser.SCRIPTED),
    is_pacifist(DefaultParser.SCRIPTED),
    is_materialist(DefaultParser.SCRIPTED),
    is_egalitarian(DefaultParser.SCRIPTED),
    is_authoritarian(DefaultParser.SCRIPTED),
    is_militarist(DefaultParser.SCRIPTED),
    is_xenophobe(DefaultParser.SCRIPTED),
    is_spiritualist(DefaultParser.SCRIPTED),
    is_fanatic_xenophile(DefaultParser.SCRIPTED),
    is_fanatic_pacifist(DefaultParser.SCRIPTED),
    is_fanatic_egalitarian(DefaultParser.SCRIPTED),
    is_fanatic_authoritarian(DefaultParser.SCRIPTED),
    is_fanatic_militarist(DefaultParser.SCRIPTED),
    is_fanatic_xenophobe(DefaultParser.SCRIPTED),
    is_fanatic_spiritualist(DefaultParser.SCRIPTED),
    is_gestalt(DefaultParser.SCRIPTED),
    is_mechanical_empire(DefaultParser.SCRIPTED),
    is_regular_empire(DefaultParser.SCRIPTED),
    is_machine_empire(DefaultParser.SCRIPTED),
    is_hive_empire(DefaultParser.SCRIPTED),
    is_megacorp(DefaultParser.SCRIPTED),
    allows_slavery(DefaultParser.SCRIPTED),
    has_psionic_ascension(DefaultParser.SCRIPTED),
    is_cloning_authority(DefaultParser.SCRIPTED),

    is_ai("Is [|NOT ]AI", DefaultParser.SIMPLE_BOOLEAN),

    is_enslaved("Pop is [|NOT ]enslaved", DefaultParser.SIMPLE_BOOLEAN),
    is_sapient("Pop is [|NOT ]Sapient", DefaultParser.SIMPLE_BOOLEAN),
    has_any_megastructure_in_empire("[Has|Does NOT have] any Megastructure", DefaultParser.SIMPLE_BOOLEAN),
    always("[Always|Never]", DefaultParser.SIMPLE_BOOLEAN),

    years_passed("Number of years since game start is %s %s", DefaultParser.SIMPLE_OPERATION),
    num_owned_planets("Number of owned planets is %s %s", DefaultParser.SIMPLE_OPERATION),
    num_communications("Number of communications is %s %s", DefaultParser.SIMPLE_OPERATION),
    has_level("Skill level is %s %s", DefaultParser.SIMPLE_OPERATION),

    any_neighbor_country("Any Neighbor Country", DefaultParser.CONDITIONAL),
    any_country("Any Country", DefaultParser.CONDITIONAL),
    any_owned_planet("Any Owned Planet", DefaultParser.CONDITIONAL),
    any_planet_within_border("Any Planet within borders", DefaultParser.CONDITIONAL),
    any_planet("Any Planet", DefaultParser.CONDITIONAL),
    any_owned_pop("Any Owned Pop", DefaultParser.CONDITIONAL),
    any_system_within_border("Any System within borders", DefaultParser.CONDITIONAL),
    any_system_planet("Any Planet", DefaultParser.CONDITIONAL),
    any_relation("Any Country Relation",DefaultParser.CONDITIONAL),
    any_pop("Any Pop", DefaultParser.CONDITIONAL),
    owner_species("Founder Species :", DefaultParser.CONDITIONAL),
    no_scope("", DefaultParser.CONDITIONAL),

    is_astral_scar("Is [|NOT ]astral scar", DefaultParser.SIMPLE_BOOLEAN),

    NOR("All must be false", DefaultParser.CONDITIONAL),
    OR("One must be true", DefaultParser.CONDITIONAL),
    NAND("One or more must be false", DefaultParser.CONDITIONAL),
    AND("All must be true", DefaultParser.CONDITIONAL),

    // TODO(Tim Aschhoff): Make sure this does what I think it does
    pop_amount("Pop count is %s %s", DefaultParser.SIMPLE_OPERATION),

    has_ai_personality((p) -> f("AI Personality is %s", i18n("personality_" + gs(p).toLowerCase()))),

    has_completed_precursor_research("Has [|NOT ]completed Precursor technology", DefaultParser.SIMPLE_BOOLEAN),
    has_crisis_level("Has Crisis level: %s"),

    has_void_dweller_origin(DefaultParser.SCRIPTED),
    is_void_dweller_empire(DefaultParser.SCRIPTED),
    is_individual_machine(DefaultParser.SCRIPTED),
    is_lithoid_empire(DefaultParser.SCRIPTED),
    is_natural_design_empire(DefaultParser.SCRIPTED),
    is_wilderness_empire(DefaultParser.SCRIPTED),
    is_beastmasters_empire(DefaultParser.SCRIPTED),
    is_anglers_empire(DefaultParser.SCRIPTED),
    is_catalytic_empire(DefaultParser.SCRIPTED),
    is_memorialist_empire(DefaultParser.SCRIPTED),
    is_galactic_curators_empire(DefaultParser.SCRIPTED),
    is_astrometeorologist_empire(DefaultParser.SCRIPTED),
    is_storm_callers_empire(DefaultParser.SCRIPTED),
    is_eager_explorer_empire(DefaultParser.SCRIPTED),
    is_dimensional_worship_empire(DefaultParser.SCRIPTED),
    is_guided_sapience_empire(DefaultParser.SCRIPTED),
    is_world_forger_empire(DefaultParser.SCRIPTED),
    is_entropy_drinkers_empire(DefaultParser.SCRIPTED),
    is_infernal_empire(DefaultParser.SCRIPTED),
    is_chosen_empire(DefaultParser.SCRIPTED),

    founder_species("Founder Species:", DefaultParser.CONDITIONAL),
    is_archetype("Is archetype %s"),
    is_lithoid("Is [|NOT ]Lithoid", DefaultParser.SIMPLE_BOOLEAN),

    has_federation("Is [|NOT ]part of a Federation", DefaultParser.SIMPLE_BOOLEAN),
    federation("Federation:", DefaultParser.CONDITIONAL),
    has_federation_law("Has Federation Law %s"),
    has_federation_perk("Has Federation Perk %s"),
    any_member("Any Member:", DefaultParser.CONDITIONAL),

    is_diplomatic(DefaultParser.SCRIPTED),

    has_menace_perk("Has Crisis perk %s"),

    is_lithoid_devouring_swarm(DefaultParser.SCRIPTED),

    has_encountered_any_fauna(DefaultParser.SCRIPTED),
    has_encountered_tiyanki("Has [|NOT ]encountered Tiyanki", DefaultParser.SIMPLE_BOOLEAN),
    has_encountered_space_amoeba("Has [|NOT ]encountered Space Amoeba", DefaultParser.SIMPLE_BOOLEAN),
    has_encountered_crystalline_entity("Has [|NOT ]encountered Crystalline Entities", DefaultParser.SIMPLE_BOOLEAN),
    has_encountered_voidworm("Has [|NOT ]encountered Voidworms", DefaultParser.SIMPLE_BOOLEAN),
    has_encountered_cutholoid("Has [|NOT ]encountered Cuthuloids", DefaultParser.SIMPLE_BOOLEAN),


    acquired_specimen_count("Number of acquired specimens is %s %s", DefaultParser.SIMPLE_OPERATION),
    num_cosmic_storms_encountered("Number of Cosmic Storms encountered is %s %s", DefaultParser.SIMPLE_OPERATION),

    country_uses_bio_ships("Country [uses|does NOT use] biological ships", DefaultParser.SIMPLE_BOOLEAN),

    has_origin("Has Origin %s"),

    has_megastructure("Has Megastructure %s"),
    has_relic("Has Relic %s"),

    country_uses_consumer_goods("Country [uses|does NOT use] Consumer Goods", DefaultParser.SIMPLE_BOOLEAN),
    country_uses_food("Country [uses|does NOT use] Food", DefaultParser.SIMPLE_BOOLEAN),

    is_active_resolution("Currently active resolution is %s"),

    can_research_technology("Can research technology: %s"),

    is_galactic_community_member("Is [|NOT ]a member of the galactic community", DefaultParser.SIMPLE_BOOLEAN),
    can_form_federation_with_empire("[Can|CANNOT] form a federation with another empire", DefaultParser.SIMPLE_BOOLEAN),

    // TODO(Tim Aschhoff): This is not very clear what this means
    exists("%s exists"),

    num_buildings(p -> {
        List<String> conditions = new ArrayList<>();
        String op = null, rhs = null;

        for(PairContext prop : mapPairs(p.value())) {
            if (prop.BAREWORD().getText().equals("value")) {
                op = op(prop);
                rhs = gs(prop);
            } else {
                Modifier m = visitCondition(prop);
                conditions.add(m.toString());
            }

        }

        String retval = String.format("Number of buildings %s %s", op, rhs);
        for(int i = 0; i < conditions.size(); i++) {
            retval = retval + "\n" + LS + conditions.get(i).replaceAll(LS, "\t" + LS);
        }
        return retval;
    }),
    disabled("Is [|NOT ]disabled", DefaultParser.SIMPLE_BOOLEAN),
    in_construction("Is [|NOT ]in construction", DefaultParser.SIMPLE_BOOLEAN),
    type("Type is %s"),

    count_archaeological_site(p -> {
        String op = null, rhs = null;
        String limits = "";

        for (PairContext prop : mapPairs(p.value())) {
            if (prop.BAREWORD().getText().equals("count")) {
                op = op(prop);
                rhs = gs(prop);
            } else if(prop.BAREWORD().getText().equals("limit")) {
                for (PairContext l : mapPairs(prop.value())) {
                    Modifier m = visitCondition(l);
                    limits += "\n" + LS + m.toString();
                }
            }

        }

        String retval = String.format("Has a Number of archaeological sites %s %s", op, rhs);
        return retval + limits;
    }),
    is_site_completed("Site is [|NOT ]completed", DefaultParser.SIMPLE_BOOLEAN),

    // TODO(Tim Aschhoff):
    has_disconnected_drone_citizenship_type("Is [|NOT ]TODO", DefaultParser.SIMPLE_BOOLEAN),

    any_owned_pop_group("Any owned Population Group:", DefaultParser.CONDITIONAL),
    // is_sapient("Is [|NOT ]Sapient", DefaultParser.SIMPLE_BOOLEAN),
    // is_enslaved("Is [|NOT ]enslaved", DefaultParser.SIMPLE_BOOLEAN),
    is_livestock("Is [|NOT ]livestock", DefaultParser.SIMPLE_BOOLEAN),
    pop_group_has_trait("Has trait %s"),

    any_owned_leader("Any owned Leader:", DefaultParser.CONDITIONAL),
    is_ruler("Is [|NOT ]Ruler", DefaultParser.SIMPLE_BOOLEAN),
    is_councilor("Is [|NOT ]Councilor", DefaultParser.SIMPLE_BOOLEAN),
    // TODO(Tim Aschhoff) Confirm this is correct!
    has_base_skill("Skill level is %s %s", DefaultParser.SIMPLE_OPERATION),
    has_councilor(p -> f("Has Councilor %s", gs(p.value().map().pair().stream()
            .filter(sp -> sp.BAREWORD().getText().equals("COUNCILOR")).findFirst().get()))
    ),

    mid_game_years_passed("Number of midgame years passed %s %s", DefaultParser.SIMPLE_OPERATION),

    any_owned_species("Any owned Species:", DefaultParser.CONDITIONAL),
    is_organic_species("Is [|NOT ]Organic", DefaultParser.SIMPLE_BOOLEAN),

    // FXIME(Tim Aschhoff) Explain this
    has_storm_attraction_civic(DefaultParser.SCRIPTED),

    is_inside_nebula("Is [|NOT ]in nebula", DefaultParser.SIMPLE_BOOLEAN),
    has_any_capped_planet_farming_district("[Has|Does NOT have] limited amount of farming districts", DefaultParser.SIMPLE_BOOLEAN),
    has_any_agriculture_zone("[Has|Does NOT have] an agriculture zone", DefaultParser.SIMPLE_BOOLEAN),
    has_any_mining_zone("[Has|Does NOT have] a mining zone", DefaultParser.SIMPLE_BOOLEAN),
    has_any_generator_zone("[Has|Does NOT have] a generator zone", DefaultParser.SIMPLE_BOOLEAN),

    perc_communications_with_playable("Percentage of playable empires met is %s %s%%", DefaultParser.SIMPLE_OPERATION),

    // TODO(Tim Aschhoff): Make sure the definition for this does not change (i.e. if the federation perk changes)
    has_make_spiritualist_perk("Is [|NOT ]a Member of a spiritualist Federation with perk 'A Union of Faith'", DefaultParser.SIMPLE_BOOLEAN),

    is_homicidal(DefaultParser.SCRIPTED),
    can_get_planet_smelter(DefaultParser.SCRIPTED),
    has_encountered_psionic_auras(DefaultParser.SCRIPTED),
    is_psionic_species(DefaultParser.SCRIPTED),
    is_latent_psionic_species(DefaultParser.SCRIPTED),
    has_psionic_species_trait(DefaultParser.SCRIPTED),

    has_any_dna(DefaultParser.SCRIPTED),
    has_dna((p) -> {
        String dna_source = null;
        for(PairContext prop : mapPairs(p.value())) {
            if (prop.BAREWORD().getText().equals("ship_category")) {
                dna_source = i18n(gs(prop));
            }
        }
        return "Has " + dna_source + " dna";
    }),

    has_trait((p) -> {
        String expertise = i18n(gs(p));
        if(expertise.contains("Expertise: ")) expertise = expertise.replaceAll("Expertise: ","") + " Expert";
        return "Has trait " + expertise;
    }),
    area((p) -> StringUtils.capitalize(gs(p))),
    research_leader((p) -> {
        String area = "";
        List<String> conditions = new ArrayList<>();
        for(PairContext prop : mapPairs(p.value())) {
            Modifier m = visitCondition(prop);
            if(m.type.equals(ModifierType.area)) area = m.toString();
            else conditions.add(m.toString());
        }
        String retval = "Research Leader (" + area + "): ";
        for(int i = 0; i < conditions.size(); i++) {
            retval = retval + "\n" + LS + conditions.get(i);
        }
        return retval;
    }),

    has_resource((p) -> {
        String type = "";
        String count = "";
        for(PairContext prop : mapPairs(p.value())) {
            if(prop.BAREWORD().getText().equals("type")) {
                type = gs(prop);
            } else if (prop.BAREWORD().getText().equals("amount")) {
                count = op(prop) + " " + gs(prop);
            }
        }
        return "Has £" + type + "£ " + i18n(type) + " " +  count;
    }),

    count_starbase_sizes((p) -> {
        String retval = "Number of %s is %s %s";
        String size = null, operator = null, count = null;
        for(PairContext prop : mapPairs(p.value())) {
            if(prop.BAREWORD().getText().equals("starbase_size")) {
                size = i18n(gs(prop));
            } else if (prop.BAREWORD().getText().equals("count")) {
                operator = op(prop);
                count = gs(prop);
            }
        }
        return String.format(retval, size, operator, count);
    }),

    has_trait_in_council((p) -> {
        String retval = "Any Leader in council has trait %s %s";
        String trait = null, level = null;
        for(PairContext prop : mapPairs(p.value())) {
            if(prop.BAREWORD().getText().equals("TRAIT")) {

                String traitPreTranslation = gs(prop);

                if (Character.isDigit(traitPreTranslation.charAt(traitPreTranslation.length()-1))) {
                    int index = traitPreTranslation.lastIndexOf("_");
                    level = traitPreTranslation.substring(index + 1);
                    traitPreTranslation = traitPreTranslation.substring(0, index);
                } else {
                    level = "1";
                }
                trait = i18n(traitPreTranslation);

            } else {
                System.err.println("Unexpected field: " + prop.BAREWORD().getText());
            }
        }
        return String.format(retval, trait, level);
    }),

    has_tier1or2or3_in_council((p) -> {
        String retval = "Any Leader in council has trait %s at level 1, 2 or 3";
        String trait = null;
        for(PairContext prop : mapPairs(p.value())) {
            if(prop.BAREWORD().getText().equals("TRAIT")) {

                String traitPreTranslation = gs(prop);

                if (Character.isDigit(traitPreTranslation.charAt(traitPreTranslation.length()-1))) {
                    int index = traitPreTranslation.lastIndexOf("_");
                    traitPreTranslation = traitPreTranslation.substring(0, index);
                }

                trait = i18n(traitPreTranslation);

            } else {
                System.err.println("Unexpected field: " + prop.BAREWORD().getText());
            }
        }
        return String.format(retval, trait);
    }),

    num_districts((p)->{
        String type = "";
        String count = "";
        for(PairContext prop : mapPairs(p.value())) {
            if(prop.BAREWORD().getText().equals("type")) {
                type = i18n(gs(prop));
            } else if (prop.BAREWORD().getText().equals("value")) {
                count = op(prop) + " " + gs(prop);
            }
        }
        return "Number of " + type + " is " + count;
    }),

    is_specialist_subject_type((p)->{
        String type = "";
        for(PairContext prop : mapPairs(p.value())) {
            if(prop.BAREWORD().getText().equals("TYPE")) {
                type = i18n(gs(prop));
            }
        }
        return "Is a " + type + " (specialised subject)";
    }),

    count_owned_pops((p) -> {
        String limits = "";
        String count = "";
        for(PairContext prop : mapPairs(p.value())) {
            if(prop.BAREWORD().getText().equals("limit")) {
                for(PairContext l : mapPairs(prop.value())) {
                    Modifier m = visitCondition(l);
                    limits += "\n" + LS + m.toString();
                }
            } else if(prop.BAREWORD().getText().equals("count")) {
                count = op(prop) + " " + gs(prop);
            }
        }
        return "Has a number of pop " + count + limits;
    }),

    count_owned_pop_amount((p) -> {return count_owned_pops.parser.apply(p);}),

    days_passed(p -> {
        if (gs(p).equals("0") && p.SPECIFIER().getText().equals("=")) {
            return "At the start of the game";
        } else {
            return DefaultParser.SIMPLE_OPERATION.parser.apply("Number of days passed since the start of the game is %s %s", p);
        }
    }),

    calc_true_if((p) -> {
        String limits = "";
        String count = "";
        for(PairContext prop : mapPairs(p.value())) {
            if(prop.BAREWORD().getText().equals("amount")) {
                count = op(prop) + " " + gs(prop);
            } else {
                Modifier m = visitCondition(prop);
                limits += "\n" + LS + m.toString();
            }
        }
        return "Number of conditions true " + count + limits;
    }),

    NOT((p) -> {
        if(mapPairs(p.value()).size() > 1) return NOR.parser.apply(p);
        Modifier m = visitCondition(mapPairs(p.value()).get(0));
        if(m.type.equals(OR)) return NOR.parser.apply(mapPairs(p.value()).get(0));

        String retval = m.toString();
        if (retval.startsWith("Has encountered")) {
            return retval.replaceFirst("Has", "Has NOT");
        } else if (retval.startsWith("Has")) {
            return "Does NOT " + retval.replaceFirst("Has", "have");
        } else if (retval.startsWith("Is")) {
            return "Is NOT " + retval.replaceFirst("Is", "");
        } else if (retval.startsWith("Any")) {
            return retval.replaceFirst("Any", "No");
        } else {
            return "NOT " + retval;
        }
    }),

    DEFAULT((p) -> {
        String retval = p.getText();
        System.out.println(retval);
        return retval;
    })
    ;

    private static final Pattern SIMPLE_BOOLEAN_PATTERN = Pattern.compile("\\[([^]]*)\\|([^]]*)]");

    private static enum DefaultParser {
        SIMPLE((format,p) -> String.format(format,i18n(gs(p.value())))),
        SIMPLE_OPERATION((format,p) -> String.format(format, op(p), gs(p))),
        SIMPLE_BOOLEAN((format,p) -> {
            Matcher matcher = SIMPLE_BOOLEAN_PATTERN.matcher(format);
            boolean found = matcher.find();
            assert found;
            String yes = matcher.group(1);
            String no = matcher.group(2);
            matcher.reset();
            if (gs(p).equals("yes")) {
                return matcher.replaceFirst(yes);
            } else {
                return matcher.replaceFirst(no);
            }
        }),
        CONDITIONAL((format, p) -> {
            List<String> conditions = new ArrayList<>();

            for(PairContext prop : mapPairs(p.value())) {
                Modifier m = visitCondition(prop);
                conditions.add(m.toString());
            }

            String retval = format;
            for(int i = 0; i < conditions.size(); i++) {
                retval = retval + "\n" + LS + conditions.get(i).replaceAll(LS, "\t" + LS);
            }
            return retval;
        }),
        SCRIPTED((format, p) -> {
            PairContext q = GLOBAL_TRIGGERS.get(p.BAREWORD().getText());
            boolean value = gs(p).equals("yes");
            List<String> conditions = new ArrayList<>();

            if(!value) {
                return ModifierType.NOT.parse(q);
            }

            for(PairContext prop : mapPairs(q.value())) {
                Modifier m = visitCondition(prop);
                conditions.add(m.toString());
            }
            String retval = format;

            if(conditions.size() < 2) {
                retval = conditions.get(0);
                return retval;
            } else if (retval == null) {
                retval = "All must be true";
            }

            for(int i = 0; i < conditions.size(); i++) {
                retval = retval + "\n" + LS + conditions.get(i).replaceAll(LS, "\t" + LS);
            }
            return retval;
        });

        private BiFunction<String, PairContext, String> parser;

        private DefaultParser(BiFunction<String, PairContext,String> parser) {
            this.parser = parser;
        }

        public String apply(String format, PairContext pair) {
            return this.parser.apply(format, pair);
        }
    }

    private Function<PairContext,String> parser;

    ModifierType(String format, DefaultParser parser) {
        this.parser = (p) -> parser.apply(format, p);
    }

    ModifierType(DefaultParser parser) {
        this.parser = (p) -> parser.apply(null, p);
    }

    ModifierType(Function<PairContext,String> parser) {
        this.parser = parser;
    }

    ModifierType(String format) {
        this(format, DefaultParser.SIMPLE);
    }

    public String parse(PairContext pair) {
        return parser.apply(pair);
    }

    public static ModifierType value(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return DEFAULT;
        }
    }

    public static Modifier visitCondition(PairContext pair) {
        Modifier retval = new Modifier();
        retval.type = ModifierType.value(pair.BAREWORD().getText());
        retval.pair = pair;
        return retval;
    }
}
