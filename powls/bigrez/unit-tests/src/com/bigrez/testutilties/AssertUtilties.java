// Copyright (C) 2008, 2009 Philip Aston
// All rights reserved.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package com.bigrez.testutilties;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;


/**
 * Additional assertions to complement those provided by JUnit.
 */
public class AssertUtilties {

  public static void assertNotEquals(
    String message, Object expected, Object actual) {

    if (expected == null && actual != null) {
      return;
    }

    if (expected != null && !areEqual(expected, actual)) {
      return;
    }

    fail("Objects were equal");
  }

  public static void assertNotEquals(Object expected, Object actual) {
    assertNotEquals(null, expected, actual);
  }

  /**
   * Check the date parts of the supplied dates are identical.
   */
  public static void assertEqualDates(Date expected, Date actual) {
    final long day = 24 * 60 * 60 * 1000;

    final long delta = Math.abs(actual.getTime() - expected.getTime());

    Assert.assertTrue(
      new Formatter().format("%tc equals %tc", actual, expected).toString(),
      delta < day);
  }

  private static boolean areEqual(Object expected, Object actual) {
    if (expected instanceof Number && actual instanceof Number) {
      return ((Number) expected).longValue() == ((Number) actual).longValue();
    }
    return expected.equals(actual);
  }

  public static void assertContains(String text, String value) {
    assertTrue("'" + text + "' contains '" + value + "'",
               text != null && text.indexOf(value) >= 0);
  }

  public static void assertContainsPattern(String text, String pattern) {
    final Matcher matcher =
      Pattern.compile(pattern, Pattern.DOTALL).matcher(text);

    assertTrue("'" + text + "' contains pattern '" + pattern + "'",
               matcher.find());
  }

  public static final <T> void assertContainsAll(List<T> c1, List<T> c2) {
    assertContainsAll(asSet(c1), asSet(c2));
  }

  public static final <T> void assertContainsAll(Set<T> c1, List<T> c2) {
    assertContainsAll(c1, asSet(c2));
  }

  public static final <T> void assertContainsAll(List<T> c1, Set<T> c2) {
    assertContainsAll(asSet(c1), c2);
  }

  public static final <T> void assertContainsAll(Set<T> c1, Set<T> c2) {
    assertTrue(c1.containsAll(c2));
  }

  public static final <T> Set<T> asSet(T... a) {
    return asSet(Arrays.asList(a));
  }

  public static final <T> Set<T> asSet(List<T> list) {
    return new HashSet<T>(list);
  }
}
