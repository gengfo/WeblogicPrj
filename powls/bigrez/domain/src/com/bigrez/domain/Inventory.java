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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bigrez.jpa.DomainEntity;


/**
 * Inventory entity.
 *
 * <p>An inventory entry is a restriction on the number of rooms of a
 * particular room type that can be sold for a particular date. If no
 * {@code Inventory} exists for that day, there is no restriction.</p>
 *
 * @author Philip Aston
 */
@Entity
@NamedQueries ({
  @NamedQuery(name = Inventory.QUERY_BY_ROOMTYPE_AND_DATES,
              query = "select i from Inventory i where i.roomType = ?1" +
                      " and i.day >= ?2 and i.day < ?3"),
  @NamedQuery(name = Inventory.QUERY_BY_PROPERTY_AND_DATES,
              query = "select i from Inventory i " +
                      "where i.roomType.property = ?1 " +
                      "and i.day >= ?2 and i.day < ?3")
 })
public class Inventory extends DomainEntity {

  private static final long serialVersionUID = 1L;

  public static final String QUERY_BY_ROOMTYPE_AND_DATES =
    "queryInventoryByRoomTypeAndDates";

  public static final String QUERY_BY_PROPERTY_AND_DATES =
    "queryInventoryByPropertyAndDates";

  @ManyToOne(cascade = CascadeType.PERSIST)
  private RoomType roomType;

  @Temporal(TemporalType.DATE)
  private Date day;

  @Column(name="ROOMSAVAIL")
  private int roomsAvailable;

  public Inventory() {
    super();
  }

  public Inventory(RoomType roomType, Date day, int roomsAvailable) {
    setRoomType(roomType);
    setDay(day);
    setRoomsAvailable(roomsAvailable);
  }

  public RoomType getRoomType() {
    return roomType;
  }

  public void setRoomType(RoomType roomType) {
    this.roomType = roomType;
  }

  public Date getDay() {
    return day;
  }

  public void setDay(Date day) {
    this.day = day;
  }

  public int getRoomsAvailable() {
    return roomsAvailable;
  }

  public void setRoomsAvailable(int roomsAvailable) {
    this.roomsAvailable = roomsAvailable;
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
    result.append(getRoomsAvailable());
    result.append(" room(s) on ");
    result.append(getDay());

    return result.toString();
  }
}
