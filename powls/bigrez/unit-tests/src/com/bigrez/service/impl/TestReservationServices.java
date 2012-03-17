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

package com.bigrez.service.impl;

import static com.bigrez.testutilties.AssertUtilties.asSet;
import static com.bigrez.testutilties.AssertUtilties.assertEqualDates;
import static com.bigrez.testutilties.AssertUtilties.assertNotEquals;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.nCopies;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static com.bigrez.service.ReservationServices.UNCONTROLLED;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import com.bigrez.domain.CardDetails;
import com.bigrez.domain.GuestProfile;
import com.bigrez.domain.Inventory;
import com.bigrez.domain.Money;
import com.bigrez.domain.Property;
import com.bigrez.domain.Rate;
import com.bigrez.domain.Reservation;
import com.bigrez.domain.RoomType;
import com.bigrez.service.EntityNotFoundException;
import com.bigrez.service.EntityNotTransientException;
import com.bigrez.service.NotFoundException;
import com.bigrez.service.ReservationServices;
import com.bigrez.service.RoomTypeUnavailableException;
import com.bigrez.service.ReservationServices.AvailabilityAndRates;
import com.bigrez.service.ReservationServices.AvailabilitySummary;
import com.bigrez.service.ReservationServices.RateDetails;
import com.bigrez.service.ReservationServices.AvailabilitySummary.Availability;
import com.bigrez.service.impl.ReservationServicesImpl.AvailabilityAndRatesImpl;
import com.bigrez.service.impl.ReservationServicesImpl.AvailabilityImpl;
import com.bigrez.service.impl.ReservationServicesImpl.RateDetailsImpl;
import com.bigrez.testutilties.AbstractEntityManagerTests;


/**
 * Unit tests for {@link ReservationServices}.
 *
 * @author Philip Aston
 */
public class TestReservationServices extends AbstractEntityManagerTests {

  private ReservationServicesImpl reservationServices;
  private final EmailServices emailServices = mock(EmailServices.class);

  @Before public void setUp() throws Exception {
    reservationServices = new ReservationServicesImpl();
    reservationServices.setEntityManager(getEntityManager());
    reservationServices.setEmailServices(emailServices);

    // Delete existing entities. Remember, we rollback this transaction after
    // the test so this only affects the view seen by test transaction.
    getEntityManager().createQuery("delete from Offer").executeUpdate();
    getEntityManager().createQuery("delete from Rate").executeUpdate();
    getEntityManager().createQuery("delete from Inventory").executeUpdate();
    getEntityManager().createQuery("delete from ReservationRate").executeUpdate();
    getEntityManager().flush();
    getEntityManager().createQuery("delete from Reservation").executeUpdate();
    getEntityManager().flush();
    getEntityManager().createQuery("delete from RoomType").executeUpdate();
    getEntityManager().createQuery("delete from Property").executeUpdate();
  }

  @Test public void testCreateReservationNoRoomType() throws Exception {
    final Property property = createProperty();
    final RoomType roomType = createTransientRoomType(property);

    final Reservation reservation = new Reservation();
    reservation.setRoomType(roomType);
    reservation.setArrivalDate(createDate(2010, 3, 12));
    reservation.setDepartureDate(createDate(2010, 3, 15));
    reservation.setCard(new CardDetails("t", "n", "e"));

    final List<RateDetails> noRates = emptyList();

    try {
      reservationServices.createReservation(reservation, noRates);
      fail("Expected NotFoundException");
    }
    catch (EntityNotFoundException e) {
      assertEquals(roomType, e.getEntity());
    }
  }

  @Test public void testCreateReservationNoGuestProfile() throws Exception {
    final Property property = createProperty();
    final RoomType roomType = createRoomType(property);

    final Reservation reservation = new Reservation();
    reservation.setRoomType(roomType);
    reservation.setArrivalDate(createDate(2010, 3, 12));
    reservation.setDepartureDate(createDate(2010, 3, 15));
    reservation.setCard(new CardDetails("t", "n", "e"));

    final GuestProfile guestProfile = new GuestProfile();
    guestProfile.setLogon("logon");
    guestProfile.setPassword("password");
    reservation.setGuestProfile(guestProfile);

    final List<RateDetails> noRates = emptyList();

    try {
      reservationServices.createReservation(reservation, noRates);
      fail("Expected NotFoundException");
    }
    catch (EntityNotFoundException e) {
      assertEquals(guestProfile, e.getEntity());
    }
  }

  @Test public void testCreateReservation() throws Exception {
    final Property property = createProperty();
    final RoomType roomType = createRoomType(property);

    final GuestProfile guestProfile = new GuestProfile();
    guestProfile.setLogon("logon");
    guestProfile.setPassword("password");
    persistAndFlush(guestProfile);

    final Date inventoryStartDate = createDate(2010, 3, 10);
    reservationServices.updateInventory(
      roomType, inventoryStartDate, asList(1, 2, 3, 0, 0, 6, 7));

    final Reservation reservation1 = new Reservation();
    reservation1.setArrivalDate(createDate(2010, 3, 12));
    reservation1.setDepartureDate(createDate(2010, 3, 15));
    reservation1.setCard(new CardDetails("t", "n", "e"));
    reservation1.setRoomType(roomType);
    reservation1.setGuestProfile(guestProfile);

    final List<RateDetails> noRates = emptyList();

    try {
      reservationServices.createReservation(reservation1, noRates);
      fail("Expected RoomTypeNotAvailableException");
    }
    catch (RoomTypeUnavailableException e) {
      assertEquals(roomType, e.getRoomType());
      assertEquals(asList(createDate(2010, 3, 13),
                          createDate(2010, 3, 14)),
                   e.getUnavailableDates());
    }

    verifyNoMoreInteractions(emailServices);

    reservationServices.updateInventory(
      roomType, inventoryStartDate, asList(1, 2, 3, 4, 5, 6, 7));

    // reservation must be transient - easiest to create another.
    final Reservation reservation2 = new Reservation();
    reservation2.setRoomType(roomType);
    reservation2.setArrivalDate(createDate(2010, 3, 12));
    reservation2.setDepartureDate(createDate(2010, 3, 15));
    reservation2.setCard(new CardDetails("t", "n", "e"));
    reservation2.setGuestProfile(guestProfile);

    reservationServices.createReservation(reservation2, noRates);

    assertEquals(
      asList(1, 2, 2, 3, 4, 6, 7),
      reservationServices.calculateAvailability(
        roomType, inventoryStartDate, createDate(2010, 3, 17)));

    verifyNoMoreInteractions(emailServices);

    guestProfile.setEmail("foo@bah.com");
    getEntityManager().merge(guestProfile);

    final Reservation reservation3 = new Reservation();
    reservation3.setRoomType(roomType);
    reservation3.setGuestProfile(guestProfile);
    reservation3.setArrivalDate(createDate(2010, 3, 12));
    reservation3.setDepartureDate(createDate(2010, 3, 15));
    reservation3.setCard(new CardDetails("t", "n", "e"));

    reservationServices.createReservation(reservation3, noRates);

    assertEquals(
      asList(1, 2, 1, 2, 3, 6, 7),
      reservationServices.calculateAvailability(
        roomType, inventoryStartDate, createDate(2010, 3, 17)));

    verify(emailServices).sendReservationConfirmedEmail(
      guestProfile.getEmail(), reservation3);

    final List<RateDetails> rates = new ArrayList<RateDetails>();

    rates.add(new RateDetailsImpl(createDate(2010, 3, 12),
                                  createDate(2010, 3, 14),
                                  new Money(new BigDecimal(292))));

    rates.add(new RateDetailsImpl(createDate(2010, 3, 10),
                                  createDate(2010, 3, 12),
                                  new Money(new BigDecimal(240))));

    final Reservation reservation4 = new Reservation();
    reservation4.setRoomType(roomType);
    reservation4.setGuestProfile(guestProfile);
    reservation4.setArrivalDate(createDate(2010, 3, 12));
    reservation4.setDepartureDate(createDate(2010, 3, 15));
    reservation4.setCard(new CardDetails("t", "n", "e"));

    final Reservation returnedReservation =
      reservationServices.createReservation(reservation4, rates);

    // Barfs if transient, but createReservation has flushed.
    returnedReservation.getExternalIdentity();

    verify(emailServices).sendReservationConfirmedEmail(
      guestProfile.getEmail(), reservation4);

    assertEquals(
      asList(1, 2, 0, 1, 2, 6, 7),
      reservationServices.calculateAvailability(
        roomType, inventoryStartDate, createDate(2010, 3, 17)));

    // Disconnect returnedReservation to confirm the GP is populated.
    getEntityManager().clear();

    assertEquals(
      asSet(reservation1, reservation2, reservation3, reservation4),
      asSet(returnedReservation.getGuestProfile().getReservations()));

    returnedReservation.getExternalIdentity(); // Barfs if transient.

    final Reservation reservation5 =
      reservation4.findEntity(getEntityManager());

    try {
      reservationServices.createReservation(reservation5, rates);
      fail("Expected NotTransientException");
    }
    catch (EntityNotTransientException e) {
    }
  }

  @Test public void testFindReservationByExternalIdentity() throws Exception {
    final Property property = createProperty();
    final RoomType roomType = createRoomType(property);

    final GuestProfile guestProfile = new GuestProfile();
    guestProfile.setLogon("logon");
    guestProfile.setPassword("password");
    persistAndFlush(guestProfile);

    final Reservation reservation1 = new Reservation();
    reservation1.setRoomType(roomType);
    reservation1.setGuestProfile(guestProfile);
    reservation1.setArrivalDate(createDate(2010, 3, 12));
    reservation1.setDepartureDate(createDate(2010, 3, 15));
    reservation1.setCard(new CardDetails("t", "n", "e"));
    getEntityManager().persist(reservation1);

    assertSame(reservation1,
               reservationServices.findReservationByExternalIdentity(
                 reservation1.getExternalIdentity()));

    getEntityManager().flush();
    getEntityManager().clear();

    final Reservation found1 =
      reservationServices.findReservationByExternalIdentity(
        reservation1.getExternalIdentity());
    assertNotSame(reservation1, found1);
    assertEquals(reservation1, found1);

    getEntityManager().remove(found1);

    try {
      reservationServices.findReservationByExternalIdentity(
        found1.getExternalIdentity());
      fail("Expected NotFoundException");
    }
    catch (NotFoundException e) {
    }
  }

  @Test public void testDeleteReservation() throws Exception {
    final Property property = createProperty();
    final RoomType roomType = createRoomType(property);

    final GuestProfile guestProfile = new GuestProfile();
    guestProfile.setLogon("logon");
    guestProfile.setPassword("password");
    persistAndFlush(guestProfile);

    final Reservation reservation = new Reservation();
    reservation.setRoomType(roomType);
    reservation.setGuestProfile(guestProfile);
    reservation.setArrivalDate(createDate(2010, 3, 12));
    reservation.setDepartureDate(createDate(2010, 3, 15));
    reservation.setCard(new CardDetails("t", "n", "e"));

    try {
      reservationServices.deleteReservation(reservation);
      fail("Expected NotFoundException");
    }
    catch (EntityNotFoundException e) {
      assertEquals(reservation, e.getEntity());
    }

    verifyNoMoreInteractions(emailServices);

    final Date inventoryStartDate = createDate(2010, 3, 10);
    reservationServices.updateInventory(
      roomType, inventoryStartDate, asList(1, 2, 3, 4, 5, 6, 7));

    persistAndFlush(reservation);

    reservationServices.deleteReservation(reservation);

    assertEquals(
      asList(1, 2, 4, 5, 6, 6, 7),
      reservationServices.calculateAvailability(
        roomType, inventoryStartDate, createDate(2010, 3, 17)));

    verifyNoMoreInteractions(emailServices);

    guestProfile.setEmail("fred@bloggs.com");
    getEntityManager().merge(guestProfile);
    getEntityManager().flush();
    getEntityManager().clear();

    try {
      reservationServices.deleteReservation(reservation);
      fail("Expected NotFoundException");
    }
    catch (EntityNotFoundException e) {
      assertEquals(reservation, e.getEntity());
    }

    assertEquals(
      asList(1, 2, 4, 5, 6, 6, 7),
      reservationServices.calculateAvailability(
        roomType, inventoryStartDate, createDate(2010, 3, 17)));

    verifyNoMoreInteractions(emailServices);

    final Reservation reservation2 = new Reservation();

    // roomTypeis detached, so don't use it as Inventory cascade's persist.
    final RoomType managedRoomType = getEntityManager().merge(roomType);
    reservation2.setRoomType(managedRoomType);

    final GuestProfile managedGP = getEntityManager().merge(guestProfile);
    reservation2.setGuestProfile(managedGP);

    reservation2.setArrivalDate(createDate(2010, 3, 12));
    reservation2.setDepartureDate(createDate(2010, 3, 15));
    reservation2.setCard(new CardDetails("t", "n", "e"));

    persistAndFlush(reservation2);

    reservationServices.deleteReservation(reservation2);

    assertEquals(
      asList(1, 2, 5, 6, 7, 6, 7),
      reservationServices.calculateAvailability(
        roomType, inventoryStartDate, createDate(2010, 3, 17)));

    verify(emailServices).sendReservationCancelledEmail(
      guestProfile.getEmail(), reservation2);
  }

  @Test public void testCalculateAvailabilitySummmaryNoProperty()
    throws Exception {

    final Property property = createTransientProperty();

    try {
      reservationServices.calculateAvailabilitySummary(property,
                                                       createDate(2010, 3, 12),
                                                       10);
      fail("Expected NotFoundException");
    }
    catch (EntityNotFoundException e) {
    }
  }

  @Test public void testCalculateAvailabilitySummmary() throws Exception {

    final Date date1 = createDate(2010, 3, 12);
    final Date date2 = createDate(2010, 3, 13);
    final Date date3 = createDate(2010, 5, 12);
    final Date date1FirstDayOfMonth = createDate(2010, 3, 1);

    final Property property = createProperty();

    final RoomType roomType1 = new RoomType();
    roomType1.setDescription("My room type");
    roomType1.setFeatures("Some features");
    roomType1.setProperty(property);

    final RoomType roomType2 = new RoomType();
    roomType2.setDescription("My other room type");
    roomType2.setFeatures("Some features");
    roomType2.setProperty(property);

    persistAndFlush(roomType1, roomType2);

    // Clear persistent context since Property doesn't provide a way to
    // fix up its list of room types.
    getEntityManager().clear();

    final List<AvailabilitySummary> result1 =
      reservationServices.calculateAvailabilitySummary(property, date1, 10);

    assertEquals(2, result1.size());

    final AvailabilityImpl noAvailability = new AvailabilityImpl();

    for (AvailabilitySummary as : result1) {
      assertEqualDates(date1FirstDayOfMonth, as.getStartDate());

      final List<Availability> availabilities = as.getAvailabilityByMonth();

      assertEquals(nCopies(10, noAvailability), availabilities);
    }

    // roomType1 is detached, so don't use it as Inventory cascades persist.
    final RoomType managedRoomType = getEntityManager().merge(roomType1);

    final Inventory inventory1 = new Inventory(managedRoomType, date1, 10);
    final Inventory inventory2 = new Inventory(managedRoomType, date2, 3);
    final Inventory inventory3 = new Inventory(managedRoomType, date3, 0);

    persistAndFlush(inventory1, inventory2, inventory3);

    final List<AvailabilitySummary> result2 =
      reservationServices.calculateAvailabilitySummary(property, date1, 10);

    assertEquals(2, result2.size());

    for (AvailabilitySummary as : result2) {
      assertEqualDates(date1FirstDayOfMonth, as.getStartDate());

      final List<Availability> expected =
        new ArrayList<Availability>(nCopies(10, noAvailability));

      if (as.getRoomType().equals(roomType1)) {
        expected.set(0, new AvailabilityImpl(2, 0));
        expected.set(2, new AvailabilityImpl(1, 1));
      }
      else {
        assertEquals(roomType2, as.getRoomType());
      }

      assertEquals(expected, as.getAvailabilityByMonth());
    }

    final List<AvailabilitySummary> result3 =
      reservationServices.calculateAvailabilitySummary(property, date1, 2);

    for (AvailabilitySummary as : result3) {
      assertEqualDates(date1FirstDayOfMonth, as.getStartDate());

      final List<Availability> expected =
        new ArrayList<Availability>(nCopies(2, noAvailability));

      if (as.getRoomType().equals(roomType1)) {
        expected.set(0, new AvailabilityImpl(2, 0));
      }
      else {
        assertEquals(roomType2, as.getRoomType());
      }

      assertEquals(expected, as.getAvailabilityByMonth());
    }
  }

  @Test public void testAvailabilityImpl() throws Exception {
    final Availability availability1 = new AvailabilityImpl(0, 0);
    assertEquals(availability1, availability1);
    assertNotEquals(availability1, 0L);
    assertEquals(0, availability1.getControls());
    assertEquals(0, availability1.getCloseOuts());

    final Availability availability2 = new AvailabilityImpl(0, 3);
    assertNotEquals(availability1, availability2);
    assertNotEquals(availability2, availability1);
    assertEquals(0, availability2.getControls());
    assertEquals(3, availability2.getCloseOuts());

    final AvailabilityImpl availability3 = new AvailabilityImpl(10, 1);
    final Availability availability4 = new AvailabilityImpl(10, 1);
    assertEquals(availability3, availability4);
    assertEquals(availability3.hashCode(), availability4.hashCode());
    assertEquals("(10, 1)", availability3.toString());
    assertEquals(10, availability3.getControls());
    assertEquals(1, availability3.getCloseOuts());

    final Inventory inventory = new Inventory();
    availability3.addControl(inventory);
    assertEquals("(11, 2)", availability3.toString());

    inventory.setRoomsAvailable(1);
    availability3.addControl(inventory);
    assertEquals("(12, 2)", availability3.toString());
  }

  @Test public void testCalculateAvailability() throws Exception {

    final Date date1 = createDate(2010, 3, 12);
    final Date date2 = createDate(2010, 3, 13);

    final RoomType roomType1 = new RoomType();
    roomType1.setDescription("My room type");
    roomType1.setFeatures("Some features");

    try {
      reservationServices.calculateAvailability(roomType1, date1, date2);
      fail("Expected NotFoundException");
    }
    catch (EntityNotFoundException e) {
    }

    final Property property = createProperty();

    roomType1.setProperty(property);

    final RoomType roomType2 = new RoomType();
    roomType2.setDescription("My other room type");
    roomType2.setFeatures("Some features");
    roomType2.setProperty(property);

    persistAndFlush(roomType1, roomType2);

    assertEquals("Empty result if dates the same",
      emptyList(),
      reservationServices.calculateAvailability(roomType1, date1, date1));

    assertEquals("Empty result if start > end",
      emptyList(),
      reservationServices.calculateAvailability(roomType1, date2, date1));

    assertEquals("Uncontrolled if no inventory",
      asList(UNCONTROLLED),
      reservationServices.calculateAvailability(roomType1, date1, date2));

    final Inventory inventory1 = new Inventory(roomType1, date1, 10);

    persistAndFlush(inventory1);

    assertEquals(
      asList(10),
      reservationServices.calculateAvailability(roomType1, date1, date2));

    assertEquals(
      "Time component of start date is irrelevant",
      asList(10),
      reservationServices.calculateAvailability(
        roomType1, new Date(date1.getTime() + 1000000), date2));

    final Date date3 = createDate(2010, 3, 15);
    final Date date4 = createDate(2010, 3, 14);

    // Different room type.
    final Inventory inventory2 = new Inventory(roomType2, date2, 20);

    final Inventory inventory3 = new Inventory(roomType1, date4, 0);

    persistAndFlush(inventory2, inventory3);

    assertEquals(
      asList(10, UNCONTROLLED, 0),
      reservationServices.calculateAvailability(roomType1, date1, date3));
  }

  @Test public void testUpdateInventory() throws Exception {

    final Date date1 = createDate(2010, 3, 12);

    final Property property = createProperty();

    final RoomType roomType1 = new RoomType();
    roomType1.setDescription("My room type");
    roomType1.setFeatures("Some features");
    roomType1.setProperty(property);

    final List<Integer> noIntegers = emptyList();

    try {
      reservationServices.updateInventory(roomType1, date1, noIntegers);
      fail("Expected NotFoundException");
    }
    catch (EntityNotFoundException e) {
    }

    persistAndFlush(roomType1);

    final Inventory inventory1 = new Inventory(roomType1, date1, 1);
    persistAndFlush(inventory1);

    reservationServices.updateInventory(roomType1, date1, noIntegers);

    assertEquals(
      inventory1,
      inventory1.findEntity(getEntityManager()));

    reservationServices.updateInventory(roomType1, date1, asList(10, 0, 1));

    assertEquals(
      asList(10, 0, 1),
      reservationServices.calculateAvailability(roomType1,
                                                date1,
                                                createDate(2010, 3, 15)));

    reservationServices.updateInventory(
      roomType1, date1, asList(UNCONTROLLED, 1, UNCONTROLLED));

    assertEquals(
      asList(UNCONTROLLED, 1, UNCONTROLLED),
      reservationServices.calculateAvailability(roomType1,
                                                date1,
                                                createDate(2010, 3, 15)));
  }

  @Test public void testCalculateRates() throws Exception {
    final Date date1 = createDate(2008, 4, 1);
    final Date date2 = createDate(2008, 5, 3);
    final Date date3 = createDate(2008, 5, 4);

    final Property property = createProperty();

    final RoomType roomType1 = new RoomType();
    roomType1.setDescription("My room type");
    roomType1.setFeatures("Some features");
    roomType1.setProperty(property);

    final RoomType roomType2 = new RoomType();
    roomType2.setDescription("My other room type");
    roomType2.setFeatures("Some different features");
    roomType2.setProperty(property);

    try {
      reservationServices.calculateRates(roomType1, date1, date2);
      fail("Expected NotFoundException");
    }
    catch (EntityNotFoundException e) {
    }

    persistAndFlush(roomType1, roomType2);

    assertEquals(
      emptyList(),
      reservationServices.calculateRates(roomType1, date1, date2));

    final Rate rate1 =
      new Rate(roomType1, date1, date2, new Money(new BigDecimal(1.23)));
    final Rate rate2 =
      new Rate(roomType1, date2, date3, new Money(new BigDecimal(1.46)));
    final Rate rate3 =
      new Rate(roomType2, date1, date2, new Money(new BigDecimal(1.23)));

    persistAndFlush(rate1, rate2, rate3);

    assertEquals(
      asList(new RateDetailsImpl(rate1)),
      reservationServices.calculateRates(roomType1, date1, date2));
    assertEquals(
      asList(new RateDetailsImpl(rate2)),
      reservationServices.calculateRates(roomType1, date2, date3));
    assertEquals(
      asList(new RateDetailsImpl(rate1), new RateDetailsImpl(rate2)),
      reservationServices.calculateRates(roomType1, date1, date3));
  }

  @Test public void testCalculateRatesOverlappingPeriod() throws Exception {
    // Greg's example.
    final Property property = createProperty();

    final RoomType roomType = new RoomType();
    roomType.setDescription("My room type");
    roomType.setFeatures("Some features");
    roomType.setProperty(property);

    persistAndFlush(roomType);

    final Money usd100 = new Money(new BigDecimal(100.0));
    final Money usd120 = new Money(new BigDecimal(120.0));

    final Rate rate1 = new Rate(roomType,
                                createDate(2009, 3, 1),
                                createDate(2009, 3, 16),
                                usd100);

    final Rate rate2 = new Rate(roomType,
                                createDate(2009, 3, 16),
                                createDate(2009, 4, 15),
                                usd120);

    persistAndFlush(rate1, rate2);

   assertEquals(
     asList(new RateDetailsImpl(createDate(2009, 3, 14),
                                createDate(2009, 3, 16),
                                usd100),
            new RateDetailsImpl(createDate(2009, 3, 16),
                                createDate(2009, 3, 18),
                                usd120)),
     reservationServices.calculateRates(roomType,
                                        createDate(2009, 3, 14),
                                        createDate(2009, 3, 18)));

  }

  @Test public void testRateDetailsImpl() throws Exception {
    final Date date1 = createDate(2008, 4, 1);
    final Date date2 = createDate(2008, 5, 3);

    final RateDetails rateDetails1 =
      new RateDetailsImpl(date1, date2, new Money(new BigDecimal(1.23)));
    assertEquals(rateDetails1, rateDetails1);
    assertNotEquals(rateDetails1, 0L);

    final RateDetails rateDetails2 =
      new RateDetailsImpl(date1, date2, new Money(new BigDecimal(1.43)));
    assertNotEquals(rateDetails1, rateDetails2);
    assertNotEquals(rateDetails2, rateDetails1);

    final RateDetails rateDetails3 =
      new RateDetailsImpl(date1, date2, rateDetails1.getPrice());
    assertEquals(rateDetails3, rateDetails1);
    assertEquals(rateDetails3.hashCode(), rateDetails1.hashCode());
    assertEquals("RateDetails(1.23, 2008-05-01, 33)", rateDetails3.toString());
    assertEquals(new Money(new BigDecimal(1.23)), rateDetails3.getPrice());
    assertEquals(date1, rateDetails3.getStartDate());
    assertEquals(33, rateDetails3.getNumberOfNights());

    class Expected {
      private final Date date1;
      private final Date date2;
      private final int numberOfNights;

      public Expected(Date date1, Date date2, int numberOfNights) {
        this.date1 = date1;
        this.date2 = date2;
        this.numberOfNights = numberOfNights;
      }

      public void assertExpectation() {
        final RateDetails rd = new RateDetailsImpl(
           date1, date2, new Money(new BigDecimal(1)));

        assertEquals(numberOfNights, rd.getNumberOfNights());
      }
    }

    for (Expected e : asList(
        new Expected(createDate(1922, 8, 1), createDate(1922, 8, 2), 1),
        new Expected(createDate(1999, 11, 31), createDate(2000, 0, 1), 1),
        new Expected(createDate(2000, 0, 1), createDate(2000, 0, 1), 0),
        new Expected(createDate(2001, 0, 1), createDate(2000, 0, 1), -365),
        new Expected(createDate(2012, 0, 1), createDate(2012, 1, 1), 31)
      )) {

      e.assertExpectation();
    }
  }

  @Test public void testCalculateRatesAndAvailabilty() throws Exception {
    final Date date1 = createDate(2010, 3, 12);
    final Date date2 = createDate(2010, 3, 13);
    final Date date3 = createDate(2010, 3, 14);

    final Property p1 = createTransientProperty();
    final Property p2 = createTransientProperty();

    final RoomType roomType1 = new RoomType();
    roomType1.setDescription("My room type");
    roomType1.setFeatures("Some features");
    roomType1.setProperty(p1);

    final RoomType roomType2 = new RoomType();
    roomType2.setDescription("My other room type");
    roomType2.setFeatures("Some more features");
    roomType2.setProperty(p1);

    final RoomType roomType3 = new RoomType();
    roomType3.setDescription("Another room type");
    roomType3.setFeatures("Yet more features");
    roomType3.setProperty(p2);

    try {
      reservationServices.calculateRatesAndAvailabilty(p1, date1, date2);
      fail("Expected NotFoundException");
    }
    catch (EntityNotFoundException e) {
      assertEquals(p1, e.getEntity());
    }

    persistAndFlush(p1, p2, roomType1, roomType2, roomType3);

    // Clear persistent context since Property doesn't provide a way to
    // fix up its list of room types.
    getEntityManager().clear();

    // roomType1 is detached, so don't use it as Inventory cascade's persist.
    final RoomType managedRoomType1 = getEntityManager().merge(roomType1);
    final RoomType managedRoomType2 = getEntityManager().merge(roomType2);

    final Inventory inventory1 = new Inventory(managedRoomType1, date1, 10);
    final Inventory inventory2 = new Inventory(managedRoomType1, date2, 3);
    final Inventory inventory3= new Inventory(managedRoomType2, date1, 0);

    persistAndFlush(inventory1, inventory2, inventory3);

    final List<AvailabilityAndRates> ratesAndAvailability =
      reservationServices.calculateRatesAndAvailabilty(p1, date1, date2);

    assertEquals(2, ratesAndAvailability.size());

    final Set<RoomType> roomTypes = new HashSet<RoomType>();

    for (AvailabilityAndRates ar : ratesAndAvailability) {
      final RoomType roomType = ar.getRoomType();

      roomTypes.add(roomType);

      if (roomType.equals(roomType1)) {
        assertEquals(emptyList(), ar.getRates());
        assertEquals(emptyList(), ar.getBlockingDates());
      }
      else {
        assertEquals(roomType2, roomType);
        assertEquals(emptyList(), ar.getRates());
        assertEquals(asList(date1), ar.getBlockingDates());
      }
    }

    assertEquals(asSet(roomType1, roomType2), roomTypes);

    final Rate rate1 =
      new Rate(managedRoomType1, date1, date2, new Money(new BigDecimal(123)));
    final Rate rate2 =
      new Rate(managedRoomType1, date2, date3, new Money(new BigDecimal(146)));
    final Rate rate3 =
      new Rate(managedRoomType2, date1, date2, new Money(new BigDecimal(127)));

    persistAndFlush(rate1, rate2, rate3);

    final List<AvailabilityAndRates> ratesAndAvailability2 =
      reservationServices.calculateRatesAndAvailabilty(p1, date1, date2);
    assertEquals(2, ratesAndAvailability.size());

    final List<RateDetails> roomType1Rates;
    final List<RateDetails> roomType2Rates;

    if (ratesAndAvailability2.get(0).getRoomType().equals(roomType1)) {
      roomType1Rates = ratesAndAvailability2.get(0).getRates();
      roomType2Rates = ratesAndAvailability2.get(1).getRates();
    }
    else {
      roomType1Rates = ratesAndAvailability2.get(1).getRates();
      roomType2Rates = ratesAndAvailability2.get(0).getRates();
    }

    assertEquals(asList(new RateDetailsImpl(rate1)), roomType1Rates);
    assertEquals(asList(new RateDetailsImpl(rate3)), roomType2Rates);

    final EntityManager em = getEntityManager();
    em.remove(rate3);
    em.remove(inventory3);
    em.flush();
    em.remove(managedRoomType2);
    persistAndFlush();

    // Clear persistent context since Property doesn't provide a way to
    // fix up its list of room types.
    getEntityManager().clear();

    final List<AvailabilityAndRates> ratesAndAvailability3 =
      reservationServices.calculateRatesAndAvailabilty(
        p1, createDate(2010, 3, 10), createDate(2010, 3, 15));

    assertEquals(1, ratesAndAvailability3.size());
    assertEquals(asList(new RateDetailsImpl(rate1),
                        new RateDetailsImpl(rate2)),
                 ratesAndAvailability3.get(0).getRates());

    assertEquals(emptyList(),
                 ratesAndAvailability3.get(0).getBlockingDates());
  }

  @Test public void testAvailabilityAndRatesImpl() throws Exception {
    final RoomType roomType = new RoomType();
    final Date date1 = createDate(2008, 4, 1);
    final Date date2 = createDate(2008, 5, 3);

    final RateDetails rateDetails1 =
      new RateDetailsImpl(date1, date2, new Money(new BigDecimal(1.23)));
    final RateDetails rateDetails2 =
      new RateDetailsImpl(date1, date2, new Money(new BigDecimal(1.43)));

    final AvailabilityAndRates ar1 = new AvailabilityAndRatesImpl(
      roomType, asList(rateDetails1), asList(date1));
    assertEquals(ar1, ar1);
    assertNotEquals(ar1, 0);
    assertEquals(roomType, ar1.getRoomType());
    assertEquals(asList(rateDetails1), ar1.getRates());
    assertEquals(asList(date1), ar1.getBlockingDates());

    final AvailabilityAndRates ar2 = new AvailabilityAndRatesImpl(
      roomType, asList(rateDetails1), asList(date1));
    assertEquals(ar1, ar2);
    assertEquals(ar1.hashCode(), ar2.hashCode());

    final AvailabilityAndRates ar3 = new AvailabilityAndRatesImpl(
      roomType, asList(rateDetails1, rateDetails2), asList(date1));
    assertNotEquals(ar1, ar3);

  }
}
