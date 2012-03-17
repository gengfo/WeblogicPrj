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

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
//import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bigrez.jpa.DomainEntity;


/**
 * Entity representing a reservation.
 *
 * @author Philip Aston
 */
@Entity
public class Reservation extends DomainEntity {

  private static final long serialVersionUID = 1L;

  /**
   * The confirmation number. Automatically assigned when the
   * {@code Reservation} is made persistent. We map the confirmation
   * number to a physical column so it can be accessed by other
   * applications and reporting tools.
   *
   * <p>Using GeneratedValue on a non-identity field is a Kodo extension. If
   * you wish to use this with other JPA providers, including TopLink, comment
   * out the GeneratedValue annotation, and add in the
   */
  @Column(name="CONFIRMNUM")
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CommonSequence")
  private String confirmationNumber;

  @ManyToOne(cascade = CascadeType.PERSIST)
  private GuestProfile guestProfile;

  @ManyToOne(cascade = CascadeType.PERSIST)
  private RoomType roomType;

  @Column(name="ARRIVE")
  @Temporal(TemporalType.DATE)
  private Date arrivalDate;

  @Column(name="DEPART")
  @Temporal(TemporalType.DATE)
  private Date departureDate;

  @Embedded
  private CardDetails card;

  @OneToMany(mappedBy="reservation", cascade = CascadeType.ALL)
  @OrderBy("startDate")
  private List<ReservationRate> reservationRates =
    new ArrayList<ReservationRate>();

  public Reservation() {
  }

  // Alternative way to set the confirmation number for JPA implementations
  // such as TopLink that do not allow the GeneratedValue annotations to be
  // used for non-identity fields. See documentation for the confirmationNumber
  // field.
  /*
  @PrePersist
  public void calculateConfirmationNumber() {
    if (confirmationNumber == null) {
      confirmationNumber = String.valueOf(Math.abs((int)System.nanoTime()));
    }
  }
  */

  public String getConfirmationNumber() {
    return confirmationNumber;
  }

  public GuestProfile getGuestProfile() {
    return guestProfile;
  }

  public void setGuestProfile(GuestProfile newGuestProfile) {
    if (guestProfile != null) {
      if (guestProfile == newGuestProfile) {
        return;
      }
      else {
        guestProfile.getMutableReservations().remove(this);
      }
    }

    // newGuestProfile can be null if the Reservation is being removed.
    // If so, we're careful not to set guestProfile to null, since some JPA
    // implementations (including TopLink) will then attempt an UPDATE to this
    // Reservation which violates the foreign key constraint.

    if (newGuestProfile != null) {
      guestProfile = newGuestProfile;

      assert !guestProfile.getMutableReservations().contains(this);
      guestProfile.getMutableReservations().add(this);
    }
  }

  public RoomType getRoomType() {
    return roomType;
  }

  public void setRoomType(RoomType roomType) {
    this.roomType = roomType;
  }

  public Date getArrivalDate() {
    return arrivalDate;
  }

  public void setArrivalDate(Date arrivalDate) {
    this.arrivalDate = arrivalDate;
  }

  public Date getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(Date departureDate) {
    this.departureDate = departureDate;
  }

  public CardDetails getCard() {
    return card;
  }

  public void setCard(CardDetails card) {
    this.card = card;
  }

  /**
   * List of rates that were offered when this reservation was made.
   *
   * <p>If the {@code Reservation} has been obtained from the database and
   * {@link #addReservationRate} has not been called subsequently, the list
   * will be sorted by start date, otherwise it may not be sorted.</p>
   *
   * @return The list of rates.
   */
  public List<ReservationRate> getReservationRates() {
    return Collections.unmodifiableList(reservationRates);
  }

  /**
   * Add a ReservationRate.
   *
   * <p>We allow clients to maintain the {@code reservationRates} relationship
   * in memory. However, if this method is called we no longer guarantee that
   * the list of rates returned by calling {@link #getReservationRates()} on
   * the instance is sorted.
   *
   * @param reservationRate The rate.
   * @throws IllegalArgumentException If the {@code reservationRate} is not for
   *  this {@code Reservation}.
   */
  public void addReservationRate(ReservationRate reservationRate) {
    if (!(equals(reservationRate.getReservation()))) {
      throw new IllegalArgumentException(
        "ReservationRate is not for this Reservation");
    }

    if (!reservationRates.contains(reservationRate)) {
      reservationRates.add(reservationRate);
    }
  }

  @Override protected String toStringDescription() {
    return getConfirmationNumber();
  }
}
