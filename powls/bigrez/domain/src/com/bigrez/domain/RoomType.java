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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.bigrez.jpa.DomainEntity;


/**
 * Room type entity.
 *
 * @author Philip Aston
 */
@Entity
public class RoomType extends DomainEntity {

  private static final long serialVersionUID = 1L;

  @ManyToOne(cascade=CascadeType.PERSIST)
  private Property property;

  private String description;

  private String features;

  @Column(name="MAXADULTS")
  private int maximumAdults;

  private boolean smokingFlag;

  @Column(name="NUMROOMS")
  private int numberOfRooms;

  public RoomType() {
    super();
  }

  public Property getProperty() {
    return property;
  }

  public void setProperty(Property newProperty) {

    if (property != null) {
      if (property == newProperty) {
        return;
      }
      else {
        property.getMutableRoomTypes().remove(this);
      }
    }

    // newProperty can be null if the RoomType is being removed.
    // If so, we're careful not to set guestProfile to null, since some JPA
    // implementations (including TopLink) may then attempt an UPDATE to this
    // RoomType which violates the foreign key constraint.

    if (newProperty != null) {
      property = newProperty;

      assert !property.getMutableRoomTypes().contains(this);
      property.getMutableRoomTypes().add(this);
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFeatures() {
    return features;
  }

  public void setFeatures(String features) {
    this.features = features;
  }

  public int getMaximumAdults() {
    return maximumAdults;
  }

  public void setMaximumAdults(int maximumAdults) {
    this.maximumAdults = maximumAdults;
  }

  public boolean getSmokingFlag() {
    return smokingFlag;
  }

  public void setSmokingFlag(boolean smokingFlag) {
    this.smokingFlag = smokingFlag;
  }

  public int getNumberOfRooms() {
    return numberOfRooms;
  }

  public void setNumberOfRooms(int numberOfRooms) {
    this.numberOfRooms = numberOfRooms;
  }

  @Override protected String toStringDescription() {
    final StringBuilder result = new StringBuilder();

    final Property property = getProperty();

    if (property == null) {
      result.append("<no property>");
    }
    else {
      result.append(property.getDescription());
    }

    result.append(" - ");
    result.append(getDescription());

    return result.toString();
  }
}
