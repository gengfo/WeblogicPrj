// Copyright (C) 2008 Philip Aston
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

import static com.bigrez.testutilties.AssertUtilties.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.foreign.ForeignDomainEntity;


/**
 * Unit tests for {@link DomainEntity}.
 *
 * @author Philip Aston
 */
public class TestDomainEntity {
  @Test public void testDomainEntity() throws Exception {
    final MyDomainEntity entity1 = new MyDomainEntity();
    final MyDomainEntity entity2 = new MyDomainEntity();
    assertNotEquals(entity1, entity2);
    assertNotEquals(entity1.hashCode(), entity2.hashCode());

    entity1.setId(12);
    assertEquals(entity1, entity1);

    entity2.setId(12);
    assertEquals(entity1, entity2);
    assertEquals(entity1.hashCode(), entity2.hashCode());

    final MyOtherDomainEntity entity3 = new MyOtherDomainEntity();
    entity3.setId(12);
    assertNotEquals(entity1, entity3);
    assertNotEquals(entity1.hashCode(), entity3.hashCode());
  }

  @Test public void testToExternalIdentity() throws Exception {
    final MyDomainEntity entity1 = new MyDomainEntity();
    entity1.setId(1);
    final String id1 = entity1.getExternalIdentity();
    assertEquals(id1, entity1.getExternalIdentity());

    final MyDomainEntity entity2 = new MyDomainEntity();

    try {
      entity2.getExternalIdentity();
      fail("Expected IllegalStateException");
    }
    catch (IllegalStateException e) {
    }

    entity2.setId(2);
    assertNotEquals(id1, entity2.getExternalIdentity());

    entity2.setId(1);
    assertEquals(id1, entity2.getExternalIdentity());
  }

  @Test public void testfindByExternalIdentity() throws Exception {
    final MyDomainEntity entity1 = new MyDomainEntity();
    entity1.setId(1);
    final String id1 = entity1.getExternalIdentity();

    final EntityManager em = mock(EntityManager.class);

    DomainEntity.findByExternalIdentity(em, MyDomainEntity.class, id1);

    verify(em).find(MyDomainEntity.class, 1L);

    try {
      DomainEntity.findByExternalIdentity(em, MyOtherDomainEntity.class, id1);
      fail("Expected IllegalArgumentException");
    }
    catch (IllegalArgumentException e) {
    }

    final String[] badIDs = {
        "blah",
        "MyDomainEntity-",
        "MyDomainEntity-1232X",
        "XMyDomainEntity-1232",
        "",
        "MyDomainEntity1232",
        "1232",
        "MyDomainEntity-137893721897321897321897318987312837219193213211829731",
    };

    for (String badID : badIDs) {
      try {
        DomainEntity.findByExternalIdentity(em, MyDomainEntity.class, badID);
        fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e) {
      }
    }
  }

  @Test public void testForeignSubclassesDontAffectEquality() throws Exception {
    final MyDomainEntity entity1 = new MyDomainEntity();
    final MyDomainEntity entity2 = new ForeignDomainEntity();

    assertNotEquals(entity1, entity2);
    assertNotEquals(entity2, entity1);

    entity1.setId(10);
    assertNotEquals(entity1, entity2);
    assertNotEquals(entity2, entity1);

    entity2.setId(10);
    assertEquals(entity1, entity2);
    assertEquals(entity2, entity1);
    assertEquals(entity1.hashCode(), entity2.hashCode());

    entity2.setId(11);
    assertNotEquals(entity1, entity2);
    assertNotEquals(entity2, entity1);
  }
}
