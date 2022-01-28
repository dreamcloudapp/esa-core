package com.dreamcloud.esa_core.cli;

import com.dreamcloud.esa_core.analyzer.AnalyzerOptions;
import com.dreamcloud.esa_core.vectorizer.VectorizationOptions;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class AnalyzerOptionsReader {
    protected static String WINDOW_SIZE = "window-size";

    public Options addOptions(Options options) {
        //WikiOptions
        Option option = new Option(null, WINDOW_SIZE, true, "");
        option.setRequired(false);
        options.addOption(option);

        return options;
    }

    public VectorizationOptions getAnalyzerOptions(CommandLine cli) {
        AnalyzerOptions options = new AnalyzerOptions();

        if (cli.hasOption(WINDOW_SIZE)) {
            options.setWindowSize(Integer.parseInt(cli.getOptionValue(WINDOW_SIZE)));
        }

        return options;
    }
}
