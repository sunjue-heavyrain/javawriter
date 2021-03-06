/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.javawriter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public final class EnumWriterTest {
  @Test public void onlyTopLevelClassNames() {
    ClassName name = ClassName.bestGuessFromString("test.Foo.Bar");
    try {
      EnumWriter.forClassName(name);
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("test.Foo.Bar must be top-level type.");
    }
  }

  @Test public void constantsAreRequired() {
    EnumWriter enumWriter = EnumWriter.forClassName(ClassName.create("", "Test"));
    try {
      Writables.writeToString(enumWriter);
      fail();
    } catch (IllegalStateException e) {
      assertThat(e.getMessage()).isEqualTo("Cannot write an enum with no constants.");
    }
  }

  @Test public void constantsAreIndented() {
    EnumWriter enumWriter = EnumWriter.forClassName(ClassName.create("", "Test"));
    enumWriter.addConstant("HELLO");
    enumWriter.addConstant("WORLD");

    String expected = ""
        + "enum Test {\n"
        + "  HELLO,\n"
        + "  WORLD;\n"
        + "}\n";
    assertThat(Writables.writeToString(enumWriter)).isEqualTo(expected);
  }
}
