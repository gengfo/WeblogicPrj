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

package com.bigrez.service.impl;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.interceptor.InvocationContext;

import org.junit.Before;
import org.junit.Test;


/**
 * Unit tests for {@link LoggingInterceptor}.
 *
 * @author Philip Aston
 */
public class TestLoggingInterceptor {

  private final Logger logger = mock(Logger.class);
  private final InvocationContext ic = mock(InvocationContext.class);

  @Before public void setup() throws Exception {
    final Method method = TestLoggingInterceptor.class.getMethod("setup");
    when(ic.getMethod()).thenReturn(method);
  }

  @Test public void testLoggingInterceptorGood() throws Exception {
    final LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
    loggingInterceptor.setLogger(logger);

    loggingInterceptor.audit(ic);

    verify(logger).log(eq(Level.INFO), startsWith("entering"));
    verify(logger).log(eq(Level.INFO), startsWith("exiting"));
    verifyNoMoreInteractions(logger);
  }

  @Test public void testLoggingInterceptorBad() throws Exception {
    final LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
    loggingInterceptor.setLogger(logger);

    final RuntimeException myException = new RuntimeException();

    when(ic.proceed()).thenThrow(myException);

    try {
      loggingInterceptor.audit(ic);
      fail("Expected exception");
    }
    catch (Exception e) {
      assertSame(myException, e);
    }

    verify(logger).log(eq(Level.INFO), startsWith("entering"));
    verify(logger).log(eq(Level.INFO),
                          startsWith("exception"),
                          eq(myException));
    verifyNoMoreInteractions(logger);
  }
}
