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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bigrez.jpa.DomainEntity;


/**
 * Entity representing a rate.
 *
 * @author Philip Aston
 */
@Entity
@NamedQueries ({
  @NamedQuery(name = Rate.QUERY_BY_ROOMTYPE,
    query = "select r from Rate r where r.roomType = ?1 order by r.startDate"),
  @NamedQuery(name = Rate.QUERY_BY_ROOMTYPE_AND_DATES,
              query = "select r from Rate r where r.roomType = ?1" +
                      " and r.endDate > ?2 and r.startDate < ?3" +
                      " order by r.startDate")
 })
public class Rate extends DomainEntity {

  private static final long serialVersionUID = 1L;

  public static final String QUERY_BY_ROOMTYPE =
    "queryRateByRoomType";

  public static final String QUERY_BY_ROOMTYPE_AND_DATES =
    "queryRateByRoomTypeAndDates";

  @ManyToOne(cascade = CascadeType.PERSIST)
  private RoomType roomType;

  @Temporal(TemporalType.DATE)
  private Date startDate;

  @Temporal(TemporalType.DATE)
  private Date endDate;

  @Embedded
  @AttributeOverride(name="amount", column = @Column(name="RATE"))
  private Money price;

  public Rate() {
  }

  /**
   * Constructor for Rate.
   *
   * @param roomType Room type.
   * @param startDate Start date, inclusive.
   * @param endDate End date, exclusive.
   * @param price The rate.
   */
  public Rate(RoomType roomType, Date startDate, Date endDate, Money price) {
    setRoomType(roomType);
    setStartDate(startDate);
    setEndDate(endDate);
    setPrice(price);
  }

  public RoomType getRoomType() {
    return roomType;
  }

  public void setRoomType(RoomType roomType) {
    this.roomType = roomType;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Money getPrice() {
    return price;
  }

  public void setPrice(Money price) {
    this.price = price;
  }

  @Override protected String toStringDescription() {
    final StringBuilder result = new StringBuilder();

    final RoomType roomType = getRoomType();

    if (roomType == null) {
      result.append("<no roomtype>");
    }
    else {
      result.append(roomType.getDescription());
    }

    result.append(" - ");

    result.append(getPrice());

    return result.toString();
  }
}
