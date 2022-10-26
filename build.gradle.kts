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
plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "dev.hypera"
version = "1.1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

dependencies {
    /* This is not required but allow code to be documented further */
    compileOnly("org.jetbrains:annotations:23.0.0")

    /* Command argument parsing */
    implementation("info.picocli:picocli:4.6.3")
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    /* "Shadow" allows us to create an "uber" or "fat" jar containing dependencies */
    shadowJar {
        archiveFileName.set("libressl-test-gen.jar")
        mergeServiceFiles()
        minimize()

        manifest {
            attributes(mapOf(
                "Main-Class" to "dev.hypera.libressl.test.TestGenerator",
            ))
        }
    }
}
