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

import dev.hypera.libressl.test.blowfish.BlowfishTestVectorGenerator;
import dev.hypera.libressl.test.rc2.RC2TestVectorGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.NotNull;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Test vector generator for LibreSSL.
 */
@Command()
public final class TestGenerator implements Callable<Integer> {

    private static final @NotNull List<TestVectorGenerator> GENERATORS = Arrays.asList(
        new BlowfishTestVectorGenerator(), new RC2TestVectorGenerator()
    );

    @Parameters(index = "0", description = "The algorithm to generate test vectors for.")
    private String algorithm;

    /**
     * Execute test generator command
     *
     * @return exit code.
     * @throws Exception if something goes wrong during execution.
     */
    @Override
    public Integer call() throws Exception {
        Optional<TestVectorGenerator> generator = GENERATORS.stream()
            .filter(g -> g.getNames().stream().anyMatch(n -> n.equalsIgnoreCase(algorithm)))
            .findFirst();

        if (generator.isPresent()) {
            generator.get().generate().forEach(vector -> {
                System.out.println(vector.stringify());
            });
            return 0;
        } else {
            System.err.printf("Cannot find algorithm: %s\n", algorithm);
        }

        return 1;
    }

    /**
     * Main method.
     * <p>Uses Apache commons-cli for argument parsing, which is horrible but gets the job
     * done.</p>
     *
     * @param args Command arguments.
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new TestGenerator()).execute(args);
        System.exit(exitCode);
    }

}
