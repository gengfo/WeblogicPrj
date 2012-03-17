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

import javax.persistence.Embeddable;

import com.bigrez.jpa.ValueObject;


/**
 * Value object representing addresses.
 *
 * @author Philip Aston
 */
@Embeddable
public class Address extends ValueObject {

  private static final long serialVersionUID = 1L;

  private String address1;

  private String address2;

  private String city;

  private String stateCode;

  private String postalCode;

  protected Address() {
  }

  public Address(String address1,
                 String address2,
                 String city,
                 String stateCode,
                 String postalCode) {
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.stateCode = stateCode;
    this.postalCode = postalCode;
  }

  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStateCode() {
    return stateCode;
  }

  public void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * Hash code. See Effective Java, 2nd Edition, Item 9.
   *
   * @return The hash code.
   */
  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + nullSafeHashCode(getAddress1());
    result = 31 * result + nullSafeHashCode(getAddress2());
    result = 31 * result + nullSafeHashCode(getCity());
    result = 31 * result + nullSafeHashCode(getStateCode());
    result = 31 * result + nullSafeHashCode(getPostalCode());
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Address)) {
      return false;
    }

    final Address other = (Address) o;

    return
      nullSafeEquals(getAddress1(), other.getAddress1()) &&
      nullSafeEquals(getAddress2(), other.getAddress2()) &&
      nullSafeEquals(getCity(), other.getCity()) &&
      nullSafeEquals(getStateCode(), other.getStateCode()) &&
      nullSafeEquals(getPostalCode(), other.getPostalCode());
  }

  @Override
  protected String toStringDescription() {
    return new Formatter().format("%s/%s/%s/%s/%s",
      getAddress1(),
      getAddress2(),
      getCity(),
      getStateCode(),
      getPostalCode()).toString();
  }
}