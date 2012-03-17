// Copyright (C) 2009 Philip Aston
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

package com.bigrez.web;

import static com.bigrez.testutilties.BeanTester.testConstructorAndGetters;
import static org.junit.Assert.assertEquals;
import static java.util.Arrays.asList;

import java.util.List;

import org.junit.Test;


/**
 * Unit tests for {@link LabelValue}.
 *
 * @author Philip Aston
 */
public class TestLabelValue {

  @Test public void testGetters() throws Exception {
    testConstructorAndGetters(LabelValue.class, "label", "value");

    final LabelValue labelValue2 =
      testConstructorAndGetters(LabelValue.class, "label");
    assertEquals(labelValue2.getLabel(), labelValue2.getValue());
  }

  @Test public void testToString() throws Exception {
    final LabelValue lv1 = new LabelValue("test");
    assertEquals("test", lv1.toString());

    final LabelValue lv2 = new LabelValue("l", "v");
    assertEquals("l:v", lv2.toString());
  }

  @Test public void testMakeLabelValues() throws Exception {
    final List<LabelValue> lvs= LabelValue.makeLabelValues(
      asList(new String[] {"a", "b", "c"}));

    assertEquals("[a, b, c]", lvs.toString());

    final List<LabelValue> lvs2= LabelValue.makeLabelValues(
      asList(new String[] {"a", "b", "c"}),
      asList(new String[] {"1", "2", "3"}));

    assertEquals("[a:1, b:2, c:3]", lvs2.toString());
  }
}
