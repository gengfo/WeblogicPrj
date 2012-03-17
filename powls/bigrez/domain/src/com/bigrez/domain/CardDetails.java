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

import java.util.Formatter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.bigrez.jpa.ValueObject;


/**
 * Card details value object.
 *
 * @author Philip Aston
 */
@Embeddable
public class CardDetails extends ValueObject {

  private static final long serialVersionUID = 1L;

  @Column(name = "CARDTYPE")
  private String type;

  @Column(name = "CARDNUM")
  private String number;

  @Column(name = "CARDEXP")
  private String expiry;

  protected CardDetails() {
  }

  public CardDetails(String type, String number, String expiry) {
    this.type = type;
    this.number = number;
    this.expiry = expiry;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getExpiry() {
    return expiry;
  }

  public void setExpiry(String expiry) {
    this.expiry = expiry;
  }

  /**
   * Hash code. See Effective Java, 2nd Edition, Item 9.
   *
   * @return The hash code.
   */
  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + nullSafeHashCode(getType());
    result = 31 * result + nullSafeHashCode(getNumber());
    result = 31 * result + nullSafeHashCode(getExpiry());
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof CardDetails)) {
      return false;
    }

    final CardDetails other = (CardDetails) o;

    return
      nullSafeEquals(getNumber(), other.getNumber()) &&
      nullSafeEquals(getExpiry(), other.getExpiry()) &&
      nullSafeEquals(getType(), other.getType());
  }

  @Override
  protected String toStringDescription() {
    return new Formatter().format("%s", getNumber()).toString();
  }
}