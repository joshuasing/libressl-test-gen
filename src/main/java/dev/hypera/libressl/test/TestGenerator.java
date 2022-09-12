/*
 * Copyright (c) 2022 Joshua Sing <joshua@hypera.dev>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package dev.hypera.libressl.test;

import dev.hypera.libressl.test.rc2.RC2TestVectorGenerator;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jetbrains.annotations.NotNull;

/**
 * Test vector generator for LibreSSL.
 */
public final class TestGenerator {

    /**
     * To add another generator to this, replace {@code Collections.singletonList} with {@code Arrays.asList} and remove this comment
     */
    private static final @NotNull List<TestVectorGenerator> GENERATORS = Collections.singletonList(new RC2TestVectorGenerator());

    /**
     * Main method.
     * <p>Uses Apache commons-cli for argument parsing, which is horrible but gets the job done.</p>
     *
     * @param args Command arguments.
     */
    public static void main(String[] args) {
        Options options = new Options();

        /* Print help */
        Option help = new Option("h", "help", false, "prints help");
        options.addOption(help);

        /* Used to filter the algorithms for which test vectors should be generated */
        Option algorithms = new Option("a", "algorithms", true, "only generate test vectors for given algorithms");
        options.addOption(algorithms);

        /* Used to provide a directory to write the outputs to */
        Option output = new Option("o", "output", true, "output file (prints to stdout by default)");
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine command = parser.parse(options, args);
            if (command.hasOption("help")) {
                /* Handle help command */
                formatter.printHelp("java -jar libressl-test-gen.jar", options, true);
                return;
            }

            /* Generate */
            String outputFolder = command.hasOption("output") ? command.getOptionValue("output") : null;
            try (PrintStream writer = null == outputFolder ? System.out : resolvePath(outputFolder)) {
                List<TestVectorGenerator> generators = command.hasOption("algorithms") ? filterGenerators(command.getOptionValues("algorithms")) : GENERATORS;

                for (TestVectorGenerator generator : generators) {
                    writer.println("\n\n\t/* " + generator.getNames().get(0).toUpperCase() + " */");
                    for (TestVector vector : generator.generate()) {
                        writer.println(vector.stringify());
                    }
                }
            }
        } catch (ParseException ex) {
            System.out.println(ex.getLocalizedMessage());
            formatter.printHelp("java -jar libressl-test-gen.jar", options, true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Resolve the given file path.
     *
     * @param name File name/path.
     *
     * @return new PrintStream for writing to the file.
     * @throws IOException if something goes wrong.
     */
    private static @NotNull PrintStream resolvePath(@NotNull String name) throws IOException {
        Path path = Paths.get(name);
        if (!Files.exists(path)) {
            Files.createFile(path);
        } else if (Files.isDirectory(path)) {
            throw new IllegalArgumentException(name + " is a directory");
        }

        return new PrintStream(Files.newOutputStream(path, StandardOpenOption.TRUNCATE_EXISTING));
    }

    /**
     * Filter test vector generators.
     *
     * @param algorithms Algorithm strings to filter by.
     */
    private static @NotNull List<TestVectorGenerator> filterGenerators(@NotNull String[] algorithms) {
        List<String> algorithmNames = Arrays.asList(algorithms);
        return GENERATORS.stream().filter(g -> algorithmNames.stream().anyMatch(a -> g.getNames().stream().anyMatch(n -> n.equalsIgnoreCase(a))))
            .collect(Collectors.toList());
    }

}
