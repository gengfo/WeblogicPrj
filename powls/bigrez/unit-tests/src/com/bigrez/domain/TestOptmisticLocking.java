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

package com.bigrez.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static com.bigrez.jpa.JPAUtilties.isOptimisticLockingException;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.bigrez.testutilties.AbstractEntityManagerTests;


/**
 * Unit tests for optimistic locking.
 *
 * @author Philip Aston
 */
public class TestOptmisticLocking extends AbstractEntityManagerTests {

  @Test public void testSimpleOptimisticLockViolation() throws Exception {
    final Property p = this.createProperty();
    getEntityManager().getTransaction().commit();

    getEntityManager().close(); // Force p to be detached (Extended PC).

    final EntityManager em2 = getEntityManagerFactory().createEntityManager();
    em2.getTransaction().begin();

    final Property p2 = p.findEntity(em2);

    p2.setDescription("Something else");

    final EntityManager em3 = getEntityManagerFactory().createEntityManager();
    em3.getTransaction().begin();

    final Property p3 = p.findEntity(em3);

    p3.setDescription("foo foo");
    em2.getTransaction().commit();

    try {
      em3.getTransaction().commit();
      fail("Expected OptimisticLockException");
    }
    catch (Exception e) {
      assertTrue(isOptimisticLockingException(e));
    }

    final EntityManager em4 = getEntityManagerFactory().createEntityManager();
    em4.getTransaction().begin();
    final Property p4 = p.findEntity(em4);
    em4.remove(p4);
    em4.getTransaction().commit();
  }
}