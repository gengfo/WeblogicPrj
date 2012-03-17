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

package com.bigrez.jpa;

import javax.persistence.OptimisticLockException;


/**
 * Various utility methods.
 *
 * @author Philip Aston
 */
public final class JPAUtilties {

  /**
   * Unwrap {@code t} and return {@code true} if it represents an optimistic
   * locking failure.
   *
   * @param t Throwable to check.
   * @return {@code true} if and only if {@code t} represents an optimistic
   *   locking failure.
   */
  public static final boolean isOptimisticLockingException(Throwable t) {
    while (t != null && t.getCause() != t) {
      if (t instanceof OptimisticLockException) {
        return true;
      }

      t = t.getCause();
    }

    return false;
  }

  /**
   * Traverse up the class hierarchy, ignoring any classes outside our
   * packages. JPA enhancement can subclass our classes, this lets us
   * step past any subclasses.
   *
   * @param c The class to start with.
   * @return The first super class in a BigRez package.
   * @throws IllegalArgumentException If c does not have a super class in
   * a BigRez package.
   */
  @SuppressWarnings("unchecked")
  static <T> Class<? extends T> implementationClass(Class<? extends T> c) {

    Class<?> result = c;

    while (!result.getPackage().getName().startsWith("com.bigrez.")) {
      result =  result.getSuperclass();

      if (result == null) {
        throw new IllegalArgumentException("Not a BigRez instance");
      }
    }

    // This is safe if and only if T is in a BigRez package.
    return (Class<? extends T>) result;
  }
}
