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

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Formatter;

import javax.persistence.Embeddable;

import com.bigrez.jpa.ValueObject;


/**
 * Value object representing money.
 *
 * <p>Very simple implementation that only supports US Dollars.</p>
 *
 * <p>For financial applications, a Money class should provide rounding
 * aware arithmetic. See Fowler, Patterns of Enterprise Architecture,
 * Addison-Wesley 2003.</p>
 *
 * @author Philip Aston
 */
@Embeddable
public class Money extends ValueObject {

  private static final long serialVersionUID = 1L;

  private static final Currency DOLLARS = Currency.getInstance("USD");

  private BigDecimal amount;

  protected Money() {
  }

  public Money(BigDecimal amount) {
    this.amount = amount;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Currency getCurrency() {
    return DOLLARS;
  }

  /**
   * Hash code. See Effective Java, 2nd Edition, Item 9.
   *
   * @return The hash code.
   */
  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + nullSafeHashCode(getAmount());
    result = 31 * result + nullSafeHashCode(getCurrency());
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Money)) {
      return false;
    }

    final Money other = (Money) o;

    return
      nullSafeEquals(getAmount(), other.getAmount()) &&
      nullSafeEquals(getCurrency(), other.getCurrency());
  }

  @Override
  protected String toStringDescription() {
    return new Formatter().format(
      "%s %f", getCurrency().getSymbol(), getAmount()).toString();
  }
}
