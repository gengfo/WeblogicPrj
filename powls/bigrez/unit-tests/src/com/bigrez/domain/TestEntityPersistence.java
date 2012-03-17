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

package com.bigrez.domain;

import static com.bigrez.testutilties.AssertUtilties.asSet;
import static com.bigrez.testutilties.AssertUtilties.assertContainsAll;
import static com.bigrez.jpa.DomainEntityScopeTunnel.getId;
import static com.bigrez.jpa.DomainEntityScopeTunnel.setId;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Date;

import org.junit.Test;

import com.bigrez.testutilties.AbstractEntityManagerTests;


/**
 * Unit tests for {@code DomainEntitys} that exercise more complex
 * behaviour such as relationships.
 *
 * @author Philip Aston
 */
public class TestEntityPersistence extends AbstractEntityManagerTests {

  @Test public void testProperty() throws Exception {

    final Property p = new Property();
    p.setDescription("Description");
    p.setFeatures("Some features");
    p.setPhone("0123 456 789");
    p.setAddress(
      new Address("address1", "address2", "city", "sc", "postalCode"));

    persistAndFlush(p);
    getEntityManager().clear();

    final Property p2 = p.findEntity(getEntityManager());
    assertEquals(p2, p);
  }

  @Test public void testGuestProfileToReservations() throws Exception {
    final GuestProfile guestProfile = new GuestProfile();
    guestProfile.setLogon("gp1");
    guestProfile.setPassword("password");
    final GuestProfile guestProfile2 = new GuestProfile();
    guestProfile2.setLogon("gp2");
    guestProfile2.setPassword("password");

    final Property property = new Property();
    property.setDescription("description");
    property.setFeatures("features");
    property.setPhone("phone");
    property.setAddress(new Address("1", "2", "3", "4", "5"));

    // The intermediate flushes are required so as not to violate foreign
    // key constraints.
    persistAndFlush(guestProfile, property);

    final RoomType roomType = new RoomType();
    roomType.setProperty(property);
    roomType.setDescription("description");
    roomType.setFeatures("features");

    persistAndFlush(roomType);

    final Date date1 = new Date(0);
    final Date date2 = new Date(1);
    final CardDetails card = new CardDetails("type", "number", "expiry");

    final Reservation reservation1 = new Reservation();
    reservation1.setGuestProfile(guestProfile);
    reservation1.setRoomType(roomType);
    reservation1.setArrivalDate(date1);
    reservation1.setDepartureDate(date2);
    reservation1.setCard(card);

    final Reservation reservation2 = new Reservation();
    reservation2.setGuestProfile(guestProfile2);
    reservation2.setRoomType(roomType);
    reservation2.setArrivalDate(date1);
    reservation2.setDepartureDate(date2);
    reservation2.setCard(card);

    final Reservation reservation3 = new Reservation();
    reservation3.setGuestProfile(guestProfile);
    reservation3.setRoomType(roomType);
    reservation3.setArrivalDate(date1);
    reservation3.setDepartureDate(date2);
    reservation3.setCard(card);

    // No-op
    reservation1.setGuestProfile(guestProfile);

    final GuestProfile gpToo = new GuestProfile();
    setId(gpToo, getId(guestProfile));

    // Not a no-op.
    reservation1.setGuestProfile(gpToo);
    assertEquals(1, gpToo.getReservations().size());
    assertEquals(1, guestProfile.getReservations().size());

    reservation1.setGuestProfile(guestProfile);
    assertEquals(0, gpToo.getReservations().size());
    assertEquals(2, guestProfile.getReservations().size());

    // Flush, clear and re-read to make sure everything is persisted.
    persistAndFlush(reservation1, reservation2, reservation3);
    getEntityManager().clear();

    final GuestProfile foundGP = guestProfile.findEntity(getEntityManager());
    final GuestProfile foundGP2 = guestProfile2.findEntity(getEntityManager());

    // Disconnect GPs to check eager loading.
    getEntityManager().clear();

    assertEquals(asSet(reservation1, reservation3),
                 asSet(foundGP.getReservations()));

    assertEquals(asSet(reservation2), asSet(foundGP2.getReservations()));
  }

  @Test public void testPropertyToRoomTypes() throws Exception {
    final Property property1 = new Property();
    property1.setDescription("description");
    property1.setFeatures("features");
    property1.setPhone("phone");
    property1.setAddress(new Address("1", "2", "3", "4", "5"));

    final Property property2 = new Property();
    property2.setDescription("description");
    property2.setFeatures("features");
    property2.setPhone("phone");
    property2.setAddress(new Address("1", "2", "3", "4", "5"));

    persistAndFlush(property1, property2);

    final RoomType roomType1 = new RoomType();
    roomType1.setProperty(property1);
    roomType1.setDescription("description1");
    roomType1.setFeatures("features1");

    // No-op.
    roomType1.setProperty(property1);

    final Property property1Too = new Property();
    setId(property1Too, getId(property1));

    // Not a no-op.
    roomType1.setProperty(property1Too);
    assertEquals(1, property1Too.getRoomTypes().size());
    assertEquals(0, property1.getRoomTypes().size());

    roomType1.setProperty(property1);
    assertEquals(0, property1Too.getRoomTypes().size());
    assertEquals(1, property1.getRoomTypes().size());

    final RoomType roomType2 = new RoomType();
    roomType2.setProperty(property1);
    roomType2.setDescription("description2");
    roomType2.setFeatures("features2");

    final RoomType roomType3 = new RoomType();
    roomType3.setProperty(property2);
    roomType3.setDescription("description3");
    roomType3.setFeatures("features3");

    // Check everything is right in memory.
    assertContainsAll(asSet(roomType1, roomType2), property1.getRoomTypes());
    assertEquals(asList(roomType3), property2.getRoomTypes());

    // Flush, clear and re-read to check everything was persisted.
    persistAndFlush(roomType1, roomType2, roomType3);
    getEntityManager().clear();

    final Property foundProperty1 = property1.findEntity(getEntityManager());
    final Property foundProperty2 = property2.findEntity(getEntityManager());

    assertContainsAll(asSet(roomType1, roomType2), foundProperty1.getRoomTypes());
    assertEquals(asList(roomType3), foundProperty2.getRoomTypes());

    roomType2.setProperty(null);
    assertEquals(asSet(roomType1), asSet(property1.getRoomTypes()));

    getEntityManager().flush(); // Check we don't violate foreign keys.
  }

  @Test public void testPropertyToOffers() throws Exception {
    final Property property1 = new Property();
    property1.setDescription("description");
    property1.setFeatures("features");
    property1.setPhone("phone");
    property1.setAddress(new Address("1", "2", "3", "4", "5"));

    final Property property2 = new Property();
    property2.setDescription("description");
    property2.setFeatures("features");
    property2.setPhone("phone");
    property2.setAddress(new Address("a", "b", "c", "d", "e"));

    persistAndFlush(property1, property2);

    final Offer offer1 = new Offer(property1, "pic", "caption", "description");
    final Offer offer2 = new Offer(property2, "anotherpic", "blah", "blah");
    final Offer offer3 = new Offer(property1, "pic", "blurgh", "blurgh");

    // No-op.
    offer3.setProperty(property1);

    final Property property1Too = new Property();
    setId(property1Too, getId(property1));

    // Not a no-op.
    offer3.setProperty(property1Too);
    assertEquals(1, property1Too.getOffers().size());
    assertEquals(1, property1.getOffers().size());

    offer3.setProperty(property1);
    assertEquals(0, property1Too.getOffers().size());
    assertEquals(2, property1.getOffers().size());

    // Check everything is right in memory.
    assertEquals(asSet(offer1, offer3), asSet(property1.getOffers()));
    assertEquals(asSet(offer2), asSet(property2.getOffers()));

    // Flush, clear and re-read to check everything is persisted.
    persistAndFlush(offer1, offer2, offer3);
    getEntityManager().clear();

    final Property foundProperty1 = property1.findEntity(getEntityManager());
    final Property foundProperty2 = property2.findEntity(getEntityManager());

    assertEquals(asSet(offer1, offer3), asSet(foundProperty1.getOffers()));
    assertEquals(asSet(offer2), asSet(foundProperty2.getOffers()));

    offer3.setProperty(null);
    assertEquals(asSet(offer1), asSet(property1.getOffers()));

    getEntityManager().flush(); // Check we don't violate foreign keys.
  }

  @Test public void testReservationConfirmationNumber() throws Exception {

    final GuestProfile guestProfile = new GuestProfile();
    guestProfile.setLogon("gp1");
    guestProfile.setPassword("password");
    persistAndFlush(guestProfile);

    final Property property = createProperty();

    final RoomType roomType = createRoomType(property);

    final Date date1 = new Date(0);
    final Date date2 = new Date(1);
    final CardDetails card = new CardDetails("type", "number", "expiry");

    final Reservation reservation = new Reservation();
    reservation.setGuestProfile(guestProfile);
    reservation.setRoomType(roomType);
    reservation.setArrivalDate(date1);
    reservation.setDepartureDate(date2);
    reservation.setCard(card);

    assertNull(reservation.getConfirmationNumber());

    persistAndFlush(reservation);

    assertNotNull(reservation.getConfirmationNumber());
  }

  @Test public void testReservationToReservationRates() throws Exception {

    final GuestProfile guestProfile = new GuestProfile();
    guestProfile.setLogon("gp1");
    guestProfile.setPassword("password");
    persistAndFlush(guestProfile);

    final Property property = createProperty();

    final RoomType roomType = createRoomType(property);

    final Date date1 = new Date(0);
    final Date date2 = new Date(1);
    final CardDetails card = new CardDetails("type", "number", "expiry");

    Reservation reservation = new Reservation();
    reservation.setGuestProfile(guestProfile);
    reservation.setRoomType(roomType);
    reservation.setArrivalDate(date1);
    reservation.setDepartureDate(date2);
    reservation.setCard(card);
    persistAndFlush(reservation);
    getEntityManager().clear();

    assertEquals(emptyList(), reservation.getReservationRates());

    try {
      reservation.addReservationRate(
        new ReservationRate(new Reservation(),
                            createDate(2011, 3, 1),
                            10,
                            new Money(new BigDecimal(1))));
      fail("Expected IllegalArgumentException");
    }
    catch (IllegalArgumentException e) {
    }

    reservation = getEntityManager().merge(reservation);

    final ReservationRate rate1 =
      new ReservationRate(reservation,
                          createDate(2011, 3, 1),
                          10,
                          new Money(new BigDecimal(120)));

    final ReservationRate rate2 =
      new ReservationRate(reservation,
                          createDate(2010, 11, 11),
                          1,
                          new Money(new BigDecimal(123)));

    final ReservationRate rate3 =
      new ReservationRate(reservation,
                          createDate(2011, 5, 1),
                          3,
                          new Money(new BigDecimal(119)));

    final ReservationRate rate4 =
      new ReservationRate(reservation,
                          createDate(1999, 5, 1),
                          1,
                          new Money(new BigDecimal(110)));

    // Result not sorted.
    assertEquals(asList(rate1, rate2, rate3, rate4),
                 reservation.getReservationRates());

    persistAndFlush(rate1, rate2, rate3, rate4);

    getEntityManager().clear();

    final Reservation reservation2 = reservation.findEntity(getEntityManager());

    // Result sorted.
    assertEquals(asList(rate4, rate2, rate1, rate3),
                 reservation2.getReservationRates());

    reservation2.addReservationRate(rate4);

    // Result not changed.
    assertEquals(asList(rate4, rate2, rate1, rate3),
                 reservation2.getReservationRates());

  }
}
