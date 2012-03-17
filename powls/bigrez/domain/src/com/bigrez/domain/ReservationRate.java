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

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bigrez.jpa.DomainEntity;


/**
 * Entity representing a rate offered when a reservation was made.
 *
 * @author Philip Aston
 */
@Entity
public class ReservationRate extends DomainEntity {

  private static final long serialVersionUID = 1L;

  @ManyToOne(cascade=CascadeType.PERSIST)
  private Reservation reservation;

  @Temporal(TemporalType.DATE)
  private Date startDate;

  @Column(name="NUMNIGHTS")
  private int numberOfNights;

  @Embedded
  @AttributeOverride(name="amount", column=@Column(name="RATE"))
  private Money price;

  public ReservationRate() {
  }

  public ReservationRate(Reservation reservation,
                         Date startDate,
                         int numberOfNights,
                         Money price) {
    setReservation(reservation);
    setStartDate(startDate);
    setNumberOfNights(numberOfNights);
    setPrice(price);
  }

  public Reservation getReservation() {
    return reservation;
  }

  public void setReservation(Reservation reservation) {
    this.reservation = reservation;
    reservation.addReservationRate(this);
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public int getNumberOfNights() {
    return numberOfNights;
  }

  public void setNumberOfNights(int numberOfNights) {
    this.numberOfNights = numberOfNights;
  }

  public Money getPrice() {
    return price;
  }

  public void setPrice(Money price) {
    this.price = price;
  }

  @Override protected String toStringDescription() {
    final StringBuilder result = new StringBuilder();

    final Reservation roomType = getReservation();

    if (roomType == null) {
      result.append("<no reservation>");
    }
    else {
      result.append(roomType.getConfirmationNumber());
    }

    result.append(" - ");

    result.append(getPrice());

    return result.toString();
  }
}
