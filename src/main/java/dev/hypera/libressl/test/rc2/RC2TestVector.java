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
package dev.hypera.libressl.test.rc2;

import dev.hypera.libressl.test.TestVector;
import dev.hypera.libressl.test.utils.Hex;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a RC2 test vector.
 */
final class RC2TestVector extends TestVector {

    private final @NotNull String modeNid;
    private final byte[] key;
    private final int keyBits;
    private final byte[] iv;
    private final byte[] in;
    private final byte[] out;
    private final boolean padding;

    RC2TestVector(@NotNull String modeNid, byte[] key, int keyBits, byte[] iv, byte[] in, byte[] out, boolean padding) {
        this.modeNid = modeNid;
        this.key = key;
        this.keyBits = keyBits;
        this.iv = iv;
        this.in = in;
        this.out = out;
        this.padding = padding;
    }

    /**
     * Format this test vector into a table entry for use in LibreSSL <a href="https://cvsweb.openbsd.org/cgi-bin/cvsweb/src/regress/lib/libcrypto/rc2/rc2_test.c">{@code rc2_test.c}</a>.
     *
     * @return table entry.
     */
    @Override
    public @NotNull String stringify() {
        return String.format(
            /* Table format - TODO: clean this up in the future */
            "\t{\n" +
                "\t\t.mode = NID_rc2_%s,\n" +
                "\t\t.key = {\n%s\n\t\t},\n" +
                "\t\t.key_len = %d,\n" +
                "\t\t.key_bits = %d,\n" +
                "\t\t.iv = {\n%s\n\t\t},\n" +
                "\t\t.iv_len = %d,\n" +
                "\t\t.in = {\n%s\n\t\t},\n" +
                "\t\t.in_len = %d,\n" +
                "\t\t.out = {\n%s\n\t\t},\n" +
                "\t\t.out_len = %d,%s\n" +
            "\t},",
            modeNid,
            Hex.formatArray(key, "\t\t\t"),
            key.length,
            keyBits,
            Hex.formatArray(iv, "\t\t\t"),
            iv.length,
            Hex.formatArray(in, "\t\t\t"),
            in.length,
            Hex.formatArray(out, "\t\t\t"),
            out.length,
            padding ? "\n\t\t.padding = 1" : ""
        );
    }

}
