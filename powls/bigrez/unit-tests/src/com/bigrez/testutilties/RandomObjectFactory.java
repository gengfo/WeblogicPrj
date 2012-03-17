// Copyright (C) 2008, 2009 Philip Aston
// All rights reserved.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package com.bigrez.testutilties;

import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 *  Factory that generates various types of test object.
 *
 * @author Philip Aston
 */
public final class RandomObjectFactory {

  private final Random random = new Random();

  @SuppressWarnings("unchecked")
  public <T> T generateParameter(Class<T> type) {

    if (Boolean.TYPE == type || Boolean.class == type) {
      return (T) Boolean.valueOf(random.nextBoolean());
    }

    if (Character.TYPE == type || Character.class == type) {
      return (T) Character.valueOf((char) random.nextInt());
    }

    if (Byte.TYPE == type || Byte.class == type) {
      return (T) Byte.valueOf((byte) random.nextInt(0x100));
    }

    if (Short.TYPE == type || Short.class == type) {
      return (T) Short.valueOf((short) random.nextInt(0x10000));
    }

    if (Integer.TYPE == type || Integer.class == type) {
      return (T) Integer.valueOf(random.nextInt());
    }

    if (Long.TYPE == type || Long.class == type) {
      return (T) Long.valueOf(random.nextLong());
    }

    if (Float.TYPE == type || Float.class == type) {
      return (T) new Float(random.nextFloat());
    }

    if (Double.TYPE == type || Double.class == type) {
      return (T) new Double(random.nextDouble());
    }

    if (Void.TYPE == type) {
      return null;
    }

    if (String.class == type) {
      final int n = random.nextInt(50);
      final StringBuilder characters = new StringBuilder(n);

      for (int i = 0; i < n; ++i) {
        characters.append((char)(0x20 + random.nextInt(0x60)));
      }

      return (T) characters.toString();
    }

    if (BigDecimal.class == type) {
      return (T) new BigDecimal(random.nextLong());
    }

    if (Date.class == type) {
      return (T) new Date(random.nextInt());
    }

    if (List.class.isAssignableFrom(type)) {
      return (T) Collections.emptyList();
    }

    for (Constructor<?> c : type.getConstructors() ){
      try {
        return (T) c.newInstance(generateParameters(c.getParameterTypes()));
      }
      catch (Exception e) {
      }
    }

    fail("Don't know what to do with a " + type);
    return null;
  }

  private Object[] generateParameters(Class<?>[] parameterTypes) {

    if (parameterTypes.length == 0) {
      return null;
    }
    else {
      final Object[] parameters = new Object[parameterTypes.length];

      for (int j = 0; j < parameters.length; ++j) {
        parameters[j] = generateParameter(parameterTypes[j]);
      }

      return parameters;
    }
  }
}
