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
package dev.hypera.libressl.test.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Hexadecimal-related utilities.
 */
public final class Hex {

    private Hex() {
        throw new IllegalStateException("Hex is a utility class and cannot be constructed");
    }

    /**
     * Format the given byte array into a string.
     *
     * @param data    Byte array.
     * @param padding Line padding.
     *
     * @return byte array as a string.
     */
    public static @NotNull String formatArray(byte[] data, @NotNull String padding) {
        StringBuilder builder = new StringBuilder(padding);

        for (int i = 0; i < data.length; i++) {
            builder.append(String.format("0x%02x,", data[i]));

            if (i % 8 != 7) {
                builder.append(" ");
            } else if (i + i < data.length) {
                builder.append("\n").append(padding);
            }
        }

        return builder.toString();
    }

}
