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

import static com.bigrez.jpa.JPAUtilties.implementationClass;
import static com.bigrez.jpa.JPAUtilties.isOptimisticLockingException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.persistence.OptimisticLockException;

import org.junit.Test;

import com.foreign.ForeignDomainEntity;


/**
 * Unit tests for {@link DomainEntity}.
 *
 * @author Philip Aston
 */
public class TestJPAUtilties {
  @Test public void testImplementationClass() throws Exception {
    assertSame(MyDomainEntity.class,
               implementationClass(MyDomainEntity.class));
    assertSame(MyDomainEntity.class,
               implementationClass(ForeignDomainEntity.class));

    try {
      implementationClass(String.class);
      fail("Expected IllegalArgumentException");
    }
    catch (IllegalArgumentException e) {
    }
  }

  @Test public void testIsOptimisticLockingException() throws Exception {
    // Also tested with real optimistic locking exceptions by other tests.

    assertFalse(isOptimisticLockingException(new RuntimeException()));

    final OptimisticLockException ole = new OptimisticLockException();

    assertTrue(isOptimisticLockingException(ole));
    assertTrue(isOptimisticLockingException(new RuntimeException(ole)));
  }
}