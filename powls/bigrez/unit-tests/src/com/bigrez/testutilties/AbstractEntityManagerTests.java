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

import java.sql.Date;
import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.bigrez.domain.Address;
import com.bigrez.domain.Property;
import com.bigrez.domain.RoomType;
import com.bigrez.jpa.DomainEntity;


/**
 * Base class for out of container tests that use an EntityManager.
 *
 * @author Philip Aston
 */
public abstract class AbstractEntityManagerTests {

  private final Calendar calendar = Calendar.getInstance();

  private static EntityManagerFactory emf;
  private EntityManager entityManager;

  @BeforeClass
  public static void setupEMF() {
    emf = Persistence.createEntityManagerFactory("TestPU");
  }

  @Before public void setupEntityManager() {
    try {
      entityManager = emf.createEntityManager();
      entityManager.getTransaction().begin();
    }
    catch (Throwable e) {
      // Because ignoring the exception is so sensible; thanks JUnit.
      e.printStackTrace();
    }
  }

  @After public void tearDownEntityManager() {
    try {
      if (entityManager.isOpen()) {
        if (entityManager.getTransaction().isActive()) {
          entityManager.getTransaction().rollback();
        }

        entityManager.close();
      }
    }
    catch (Throwable e) {
      // Because overriding the test exception with this one is so sensible;
      // thanks again JUnit.
      e.printStackTrace();
    }
  }

  public final EntityManagerFactory getEntityManagerFactory() {
    return emf;
  }

  public final EntityManager getEntityManager() {
    return entityManager;
  }

  public final void persistAndFlush(DomainEntity... entities) {
    for (DomainEntity entity : entities) {
      getEntityManager().persist(entity);
    }

    getEntityManager().flush();
  }


  public Property createTransientProperty() {
    final Property property = new Property();
    property.setDescription("My property");
    property.setFeatures("Some features");
    property.setAddress(new Address("a", "b", "c", "d", "e"));
    property.setPhone("1234121");

    return property;
  }

  public Property createProperty() {
    final Property property = createTransientProperty();
    persistAndFlush(property);
    return property.findEntity(getEntityManager());
  }

  public RoomType createTransientRoomType(Property property) {
    final RoomType roomType = new RoomType();
    roomType.setProperty(property);
    roomType.setDescription("My room type");
    roomType.setFeatures("Some features");

    return roomType;
  }

  public RoomType createRoomType(Property property) {
    final RoomType roomType = createTransientRoomType(property);
    persistAndFlush(roomType);
    return roomType.findEntity(getEntityManager());
  }

  public Date createDate(int year, int month, int day) {
    calendar.clear();
    calendar.set(year, month, day);
    return new Date(calendar.getTimeInMillis());
  }
}