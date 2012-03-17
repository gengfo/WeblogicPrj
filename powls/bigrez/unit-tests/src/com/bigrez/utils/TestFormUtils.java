// Copyright (C) 2009 Philip Aston
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

package com.bigrez.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.springframework.validation.Errors;


public class TestFormUtils {

  private final Errors errors = mock(Errors.class);

  @Test public void testIsEmpty() {
    assertTrue(FormUtils.isEmpty(""));
    assertTrue(FormUtils.isEmpty(null));
    assertFalse(FormUtils.isEmpty("abc"));
  }

  @Test public void testAssertNonEmpty() {
    assertTrue(FormUtils.assertNonEmpty(errors, "a", "myerror"));
    verifyNoMoreInteractions(errors);

    assertFalse(FormUtils.assertNonEmpty(errors, "", "myerror"));
    verify(errors).reject("myerror");
    verifyNoMoreInteractions(errors);

    assertFalse(FormUtils.assertNonEmpty(errors, null, "myothererror"));
    verify(errors).reject("myothererror");
    verifyNoMoreInteractions(errors);
  }

  @Test public void testAssertNonZero() {
    assertTrue(FormUtils.assertNonZero(errors, -1, "myerror"));
    verifyNoMoreInteractions(errors);

    assertFalse(FormUtils.assertNonZero(errors, (Number)null, "myerror"));
    verify(errors).reject("myerror");
    verifyNoMoreInteractions(errors);

    assertFalse(FormUtils.assertNonZero(errors, 0, "myothererror"));
    verify(errors).reject("myothererror");
    verifyNoMoreInteractions(errors);
  }

  @Test public void testAssertInteger() {
    assertTrue(FormUtils.assertInteger(errors, "-99", "myerror"));
    verifyNoMoreInteractions(errors);

    assertFalse(FormUtils.assertInteger(errors, "hello", "myerror"));
    verify(errors).reject("myerror");
    verifyNoMoreInteractions(errors);
  }

  @Test public void testAssertValidEmail() {
    final String[] validEmails = {
        "bob@blah.com",
        "bob.foo@blah.lah.com",
    };

    for (String e : validEmails) {
      assertTrue(FormUtils.assertValidEmail(errors, e, "myerror"));
    }

    verifyNoMoreInteractions(errors);

    final String[] invalidEmails = {
        "bob@blah",
        "bob",
        null,
        "bob@blah.com@lah.com",
        "bob.blah@com",
    };

    for (String e : invalidEmails) {
      final Errors errors = mock(Errors.class);
      assertFalse(FormUtils.assertValidEmail(errors, e, "myerror"));
      verify(errors).reject("myerror");
      verifyNoMoreInteractions(errors);
    }
  }

  @Test public void testAssertValidDate() {
    final String[] validDates = {
        "12/1/09",
        "1/12/09",
        "1/31/09",
    };

    for (String d : validDates) {
      assertTrue(FormUtils.assertValidDate(errors, d, "myerror"));
    }

    verifyNoMoreInteractions(errors);

    final String[] invalidDates = {
        "bob@blah.com",
        null,
        "99x/1/09",
        "13-1-09",
    };

    for (String d : invalidDates) {
      final Errors errors = mock(Errors.class);
      assertFalse(FormUtils.assertValidDate(errors, d, "myerror"));
      verify(errors).reject("myerror");
      verifyNoMoreInteractions(errors);
    }
  }
}
