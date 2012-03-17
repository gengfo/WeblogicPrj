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

package com.bigrez.domain;

import static com.bigrez.jpa.DomainEntityScopeTunnel.getId;
import static com.bigrez.testutilties.AssertUtilties.assertContains;
import static com.bigrez.testutilties.AssertUtilties.assertContainsPattern;
import static com.bigrez.testutilties.AssertUtilties.assertNotEquals;
import static com.bigrez.testutilties.BeanTester.testAccessors;
import static com.bigrez.testutilties.BeanTester.testConstructorAndGetters;
import static com.bigrez.testutilties.Serializer.serialize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bigrez.jpa.DomainEntity;


/**
 * Unit tests for the entities.
 *
 * Currently expects the standard example data to be pre-loaded.
 *
 * @author Philip Aston
 */
public class TestWithStandardData {

  private static EntityManagerFactory emf;
  private EntityManager entityManager;

  @BeforeClass public static void setupEMF() {
    emf = Persistence.createEntityManagerFactory("TestPU");
  }

  @Before public void setupEntityManager() {
    try {
      entityManager = emf.createEntityManager();
    }
    catch (Throwable e) {
      // Because ignoring the exception is so sensible; thanks JUnit.
      e.printStackTrace();
    }
  }

  @After public void tearDownEntityManager() {
    entityManager.close();
  }

  @Test public void testProperty() throws Exception {
    final Query query = entityManager.createQuery("select p from Property p");

    final List<Property> allProperties = getResultList(query);

    assertTrue(allProperties.size() >= 3);

    final Property p1 = find(allProperties, 51);
    assertEquals("BigRez Inn", p1.getDescription());
    assertEquals("1000 37th Ave", p1.getAddress().getAddress1());
    assertTrue(p1.getFeatures().length() > 20);

    assertContains(p1.toString(), p1.getDescription());

    final Property p2 = find(allProperties, 52);
    assertEquals(p1, p1);
    assertNotEquals(p1, p2);
    assertNotEquals(p2, p1);
    assertNotEquals(p1, null);
    assertEquals(p1.hashCode(), p1.hashCode());

    final Property p3 = new Property();

    testAccessors(p3,
      "address", "phone", "features", "description", "imageFile");

    assertContains(p3.toString(), "Property[");

    final Property p4 = serialize(p1);
    assertEquals(p1, p4);
  }

  @Test public void testRoomType() throws Exception {
    final Query query =
      entityManager.createQuery("select t from RoomType t");

    final List<RoomType> entities = getResultList(query);

    assertTrue(entities.size() >= 3);

    final RoomType rt1 = find(entities, 11);
    assertEquals("Standard Room", rt1.getDescription());

    final RoomType rt2 = find(entities, 52);
    assertEquals(rt1, rt1);
    assertNotEquals(rt1, rt2);
    assertNotEquals(rt2, rt1);
    assertNotEquals(rt1, null);

    final RoomType rt3 = new RoomType();

    assertContains(rt3.toString(), "RoomType[");
    assertContains(rt3.toString(), "<no property> - null");

    testAccessors(rt3,
      "description", "features", "maximumAdults", "smokingFlag",
      "property", "numberOfRooms");

    assertContains(rt3.toString(), "RoomType[");
    assertContains(rt3.toString(), "null - ");

    final Property p = rt1.getProperty();
    assertNotNull(p);

    final RoomType rt4 = serialize(rt1);
    assertEquals(rt1, rt4);
  }

  @Test public void testGuestProfile() throws Exception {
    final Query query =
      entityManager.createQuery("select gp from GuestProfile gp");

    final List<GuestProfile> entities = getResultList(query);

    assertTrue(entities.size() >= 1);

    final GuestProfile gp1 = find(entities, 1);
    assertEquals("Greg", gp1.getFirstName());
    assertEquals(0, gp1.getReservations().size());

    final GuestProfile gp2 = new GuestProfile();
    assertEquals(gp1, gp1);
    assertNotEquals(gp1, gp2);
    assertNotEquals(gp2, gp1);
    assertNotEquals(gp1, null);
    assertEquals(gp1.hashCode(), gp1.hashCode());

    final GuestProfile gp3 = new GuestProfile();

    testAccessors(gp3,
      "logon", "password", "firstName", "lastName", "phone", "email", "card");

    assertContains(gp3.toString(), "GuestProfile[");

    final GuestProfile gp4 = serialize(gp1);
    assertEquals(gp1, gp4);
  }

  @Test public void testInventory() throws Exception {
    final Query query = entityManager.createQuery("select i from Inventory i");

    final List<Inventory> entities = getResultList(query);

    assertTrue(entities.size() >= 2);

    final Inventory i1 = find(entities, 1);
    assertEquals(0, i1.getRoomsAvailable());

    final Inventory i2 = find (entities, 2);
    assertEquals(10, i2.getRoomsAvailable());
    assertEquals(i1, i1);
    assertNotEquals(i1, i2);
    assertNotEquals(i2, i1);
    assertNotEquals(i1, null);
    assertEquals(i1.hashCode(), i1.hashCode());

    final Inventory i3 = new Inventory();

    assertContains(i3.toString(), "Inventory[");
    assertContains(i3.toString(), "<no roomtype> - 0 room(s)");

    testAccessors(i3, "roomType", "day", "roomsAvailable");

    assertContains(i3.toString(), "Inventory[");
    assertContains(i3.toString(),
                   "null - " + i3.getRoomsAvailable() + " room(s)");

    final Inventory i4 = serialize(i1);
    assertEquals(i1, i4);

    testConstructorAndGetters(
      Inventory.class, "roomType", "day", "roomsAvailable");
  }

  @Test public void testOffer() throws Exception {
    final Query query = entityManager.createQuery("select i from Offer i");

    final List<Offer> entities = getResultList(query);

    assertTrue(entities.size() >= 3);

    final Offer o1 = find(entities, 1);
    final Offer o2 = find(entities, 2);
    assertContains(o2.getDescription(), "boat trip");
    assertEquals(o1, o1);
    assertNotEquals(o1, o2);
    assertNotEquals(o2, o1);
    assertNotEquals(o1, null);
    assertEquals(o1.hashCode(), o1.hashCode());

    final Offer o3 = new Offer();

    assertContains(o3.toString(), "Offer[");

    testAccessors(o3, "property", "imageFile", "caption", "description");

    assertContains(o3.toString(), "Offer[");
    assertContains(o3.toString(), o3.getDescription());

    final Offer o4 = serialize(o1);
    assertEquals(o1, o4);

    testConstructorAndGetters(
      Offer.class, "property", "imageFile", "caption", "description");
  }

  @Test public void testRate() throws Exception {
    final Query query = entityManager.createQuery("select r from Rate r");

    final List<Rate> entities = getResultList(query);

    assertTrue(entities.size() >= 9);

    final Rate r1 = find(entities, 21);
    assertEquals(95, r1.getPrice().getAmount().intValue());

    final Rate r2 = find(entities, 22);
    assertEquals(105, r2.getPrice().getAmount().intValue());
    assertEquals(r1, r1);
    assertNotEquals(r1, r2);
    assertNotEquals(r2, r1);
    assertNotEquals(r1, null);
    assertEquals(r1.hashCode(), r1.hashCode());

    final Rate r3 = new Rate();

    assertContains(r3.toString(), "Rate[");

    testAccessors(r3, "startDate", "endDate", "price", "roomType");

    assertContains(r3.toString(), "Rate[");
    assertContains(r3.toString(), "null - ");
    assertContainsPattern(r3.toString(), "Money\\[.+\\]");

    final Rate r4 = serialize(r1);
    assertEquals(r1, r4);

    testConstructorAndGetters(
      Rate.class, "roomType", "startDate", "endDate", "price");
  }

  @Test public void testReservationRate() throws Exception {
    final Query query =
      entityManager.createQuery("select r from ReservationRate r");

    query.getResultList();

    // No ReservationRates in standard data.

    final ReservationRate r1 = new ReservationRate();

    final ReservationRate r2 = new ReservationRate();
    assertEquals(r1, r1);
    assertNotEquals(r1, r2);
    assertNotEquals(r2, r1);
    assertNotEquals(r1, null);
    assertEquals(r1.hashCode(), r1.hashCode());

    final ReservationRate r3 = new ReservationRate();

    assertContains(r3.toString(), "ReservationRate[");

    testAccessors(r3, "reservation", "startDate", "numberOfNights", "price");

    assertContains(r3.toString(), "ReservationRate[");
    assertContains(r3.toString(), "null -");
    assertContainsPattern(r3.toString(), "Money\\[.+\\]");

    final ReservationRate r4 = serialize(r1);
    // Two transient instances with no identity are not equal.
    assertNotEquals(r1, r4);

    testConstructorAndGetters(
      ReservationRate.class,
      "reservation", "startDate", "numberOfNights", "price");
  }

  @Test public void testReservation() throws Exception {
    final Query query =
      entityManager.createQuery("select r from Reservation r");

    query.getResultList();

    // No Reservations in standard data.

    final Reservation r1 = new Reservation();

    final Reservation r2 = new Reservation();
    assertEquals(r1, r1);
    assertNotEquals(r1, r2);
    assertNotEquals(r2, r1);
    assertNotEquals(r1, null);
    assertEquals(r1.hashCode(), r1.hashCode());

    final Reservation r3 = new Reservation();

    assertContains(r3.toString(), "Reservation[");

    testAccessors(r3,
      "guestProfile", "departureDate", "card", "roomType", "arrivalDate");

    assertContains(r3.toString(), "Reservation[");
    assertNull(r3.getConfirmationNumber());

    final Reservation r4 = serialize(r1);
    // Two transient instances with no identity are not equal.
    assertNotEquals(r1, r4);
  }

  /**
   * Helper method to minimise scope of SuppressWarnings.
   */
  @SuppressWarnings("unchecked")
  private static <T> List<T> getResultList(Query query) {
    return query.getResultList();
  }

  private static <T extends DomainEntity> T find(List<T> entities, long id) {

    for (T e : entities) {
      if (getId(e) == id) {
        return e;
      }
    }

    return null;
  }
}
