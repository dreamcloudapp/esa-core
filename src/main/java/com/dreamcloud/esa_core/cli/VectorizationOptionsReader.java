package com.dreamcloud.esa_core.cli;

import com.dreamcloud.esa_core.vectorizer.VectorizationOptions;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * A CLI utility class for reading the relevant command arguments for vectorization options.
 */
public class VectorizationOptionsReader {
    protected static String WINDOW_SIZE = "window-size";
    protected static String WINDOW_DROP = "window-drop";
    protected static String VECTOR_LIMIT = "vector-limit";
    protected static String CONCEPT_MULTIPLIER = "concept-multiplier";

    public void addOptions(Options options) {
        //WikiOptions
        Option option = new Option(null, WINDOW_SIZE, true, "");
        option.setRequired(false);
        options.addOption(option);

        option = new Option(null, WINDOW_DROP, true, "");
        option.setRequired(false);
        options.addOption(option);

        option = new Option(null, VECTOR_LIMIT, true, "");
        option.setRequired(false);
        options.addOption(option);

        option = new Option(null, CONCEPT_MULTIPLIER, true, "");
        option.setRequired(false);
        options.addOption(option);
    }

    public VectorizationOptions getOptions(CommandLine cli) {
        VectorizationOptions options = new VectorizationOptions();

        if (cli.hasOption(WINDOW_SIZE)) {
            options.setWindowSize(Integer.parseInt(cli.getOptionValue(WINDOW_SIZE)));
        }

        if (cli.hasOption(WINDOW_DROP)) {
            options.setWindowDrop(Float.parseFloat(cli.getOptionValue(WINDOW_DROP)));
        }

        if (cli.hasOption(VECTOR_LIMIT)) {
            options.setVectorLimit(Integer.parseInt(cli.getOptionValue(VECTOR_LIMIT)));
        }

        if (cli.hasOption(CONCEPT_MULTIPLIER)) {
            options.setCommonConceptMultiplier(Float.parseFloat(cli.getOptionValue(CONCEPT_MULTIPLIER)));
        }

        return options;
    }
}
