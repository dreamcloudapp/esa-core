package com.dreamcloud.esa_core.cli;

import com.dreamcloud.esa_core.analyzer.AnalyzerOptions;
import com.dreamcloud.esa_core.analyzer.FilterWordRepository;
import com.dreamcloud.esa_core.documentPreprocessor.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalyzerOptionsReader {
    protected static String PORTER_STEMMER_DEPTH = "stemmer-depth";
    protected static String MIN_WORD_LENGTH = "min-word-length";
    protected static String MAX_WORD_LENGTH = "max-word-length";
    protected static String FILTERS = "filter";
    protected static String PREPROCESSORS = "preprocessor";
    protected static String POS_TAGS = "pos";
    protected static String STOP_WORDS = "stop-words";
    protected static String GO_WORDS = "go-words";

    public void addOptions(Options options) {
        Option option = new Option(null, PORTER_STEMMER_DEPTH, true, "");
        option.setRequired(false);
        options.addOption(option);

        option = new Option(null, MIN_WORD_LENGTH, true, "");
        option.setRequired(false);
        options.addOption(option);

        option = new Option(null, MAX_WORD_LENGTH, true, "");
        option.setRequired(false);
        options.addOption(option);

        option = new Option(null, FILTERS, true, "");
        option.setRequired(false);
        option.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(option);

        option = new Option(null, PREPROCESSORS, true, "");
        option.setRequired(false);
        option.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(option);

        option = new Option(null, POS_TAGS, true, "");
        option.setRequired(false);
        option.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(option);

        option = new Option(null, STOP_WORDS, true, "");
        option.setRequired(false);
        option.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(option);

        option = new Option(null, GO_WORDS, true, "");
        option.setRequired(false);
        option.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(option);
    }

    public AnalyzerOptions getOptions(CommandLine cli) {
        AnalyzerOptions options = new AnalyzerOptions();

        String[] filters = cli.getOptionValues(FILTERS);
        if (filters != null) {
            for(String filter: filters) {
                switch (filter) {
                    case "stemmer" -> options.setUsingPorterStemmer(true);
                    case "classic" -> options.setUsingClassic(true);
                    case "lower" -> options.setUsingLowerCase(true);
                    case "singular" -> options.setUsingSingularCase(true);
                    case "ascii" -> options.setUsingAsciiFolding(true);
                }
            }
        }

        boolean hasPosTags = cli.hasOption(POS_TAGS);

        boolean hasStanfordPreprocessor = false;

        ArrayList<DocumentPreprocessor> preprocessors = new ArrayList<>();
        if (cli.hasOption(PREPROCESSORS)) {
            for (String preprocessor: cli.getOptionValues(PREPROCESSORS)) {
                switch (preprocessor) {
                    case "stanford-lemma" -> {
                        hasStanfordPreprocessor = true;
                        if (hasPosTags) {
                            List<String> posTags = Arrays.asList(cli.getOptionValues(POS_TAGS));
                            preprocessors.add(new StanfordLemmaPreprocessor(posTags));
                        } else {
                            preprocessors.add(new StanfordLemmaPreprocessor());
                        }
                    }
                    case "wiki" -> preprocessors.add(new WikiPreprocessor());
                    case "standard" -> preprocessors.add(new StandardPreprocessor());
                    default -> throw new IllegalArgumentException("Invalid document preprocessor '" + preprocessor + "'");
                }
            }
        }
        if (hasPosTags && !hasStanfordPreprocessor) {
            throw new IllegalArgumentException("The --pos option requires the --preprocessor stanford-lemma option to be set.");
        }
        options.setPreprocessor(new ChainedPreprocessor(preprocessors));

        if (cli.hasOption(PORTER_STEMMER_DEPTH)) {
            options.setUsingPorterStemmer(true);
            options.setPorterStemmerDepth(Integer.parseInt(cli.getOptionValue(PORTER_STEMMER_DEPTH)));
        }

        if (cli.hasOption(MIN_WORD_LENGTH)) {
            options.setMinimumWordLength(Integer.parseInt(cli.getOptionValue(MIN_WORD_LENGTH)));
        }

        if (cli.hasOption(MAX_WORD_LENGTH)) {
            options.setMaximumWordLength(Integer.parseInt(cli.getOptionValue(MAX_WORD_LENGTH)));
        }

        if (cli.hasOption(STOP_WORDS)) {
            FilterWordRepository repository = new FilterWordRepository(options.isUsingLowerCase());
            for (String sourceFileName: cli.getOptionValues(STOP_WORDS)) {
                repository.addSource(sourceFileName);
            }
            options.setStopWordsRepository(repository);
        }

        if (cli.hasOption(GO_WORDS)) {
            FilterWordRepository repository = new FilterWordRepository(options.isUsingLowerCase());
            for (String dictionaryFileName: cli.getOptionValues(GO_WORDS)) {
                repository.addSource(dictionaryFileName);
            }
            options.setFilterWordRepository(repository);
        }

        return options;
    }
}
