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

package com.bigrez.testutilties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BeanTester {

  private static RandomObjectFactory randomObjectFactory =
    new RandomObjectFactory();

  /**
   * For the given bean, test the set and get method pair for each property
   * name in {@code propertyNames}.
   *
   *
   * @param bean Bean to test.
   * @param propertyNames Properties.
   * @throws Exception
   */
  public static void testAccessors(Object bean, String... propertyNames)
    throws Exception {

    final Map<String, PropertyDescriptor> pdMap =
      propertyDescriptorMap(bean.getClass());

    final List<Script> scripts = new ArrayList<Script>();

    for (String propertyName : propertyNames) {
      final PropertyDescriptor propertyDescriptor = pdMap.get(propertyName);

      scripts.add(
        new ValueScript(bean,
                        new PropertyAccessor(propertyDescriptor),
                        randomObjectFactory.generateParameter(
                          propertyDescriptor.getPropertyType())));
    }

    new CompositeScript(scripts).testSetAndGet();
  }

  /**
   * For the given bean, and a list of {@code propertyNames}, invoke a
   * constructor that takes the given property names (in the given order), then
   * test the getters.
   *
   * @param beanClass The bean class.
   * @param propertyNames The properties. Order must be as a corresponding
   * constructor.
   * @throws NoSuchMethodException if the constructor could not be found.
   */
  public static <T> T testConstructorAndGetters(Class<T> beanClass,
                                                String... propertyNames)
    throws Exception {

    final Map<String, PropertyDescriptor> pdMap =
      propertyDescriptorMap(beanClass);

    final List<Class<?>> constructorParameterTypes =
      new ArrayList<Class<?>>(propertyNames.length);
    final List<Object> constructorParameters =
      new ArrayList<Object>(propertyNames.length);
    final List<PropertyAccessor> propertyAccessors =
      new ArrayList<PropertyAccessor>(propertyNames.length);

    for (String propertyName : propertyNames) {
      final PropertyDescriptor propertyDescriptor = pdMap.get(propertyName);
      propertyAccessors.add(new PropertyAccessor(propertyDescriptor));
      final Class<?> propertyType = propertyDescriptor.getPropertyType();
      constructorParameterTypes.add(propertyType);
      constructorParameters.add(
        randomObjectFactory.generateParameter(propertyType));
    }

    final Constructor<T> constructor =
      beanClass.getConstructor(constructorParameterTypes.toArray(new Class[0]));

    final T bean =
      constructor.newInstance(constructorParameters.toArray(new Object[0]));

    final Iterator<PropertyAccessor> propertyAccessorIterator =
      propertyAccessors.iterator();

    for (Object parameter : constructorParameters) {
      assertEquals(parameter, propertyAccessorIterator.next().get(bean));
    }

    return bean;
  }

  private static final Map<String, PropertyDescriptor>
    propertyDescriptorMap(final Class<?> beanClass)
    throws IntrospectionException {

    final BeanInfo bi = Introspector.getBeanInfo(beanClass);

    final Map<String, PropertyDescriptor> pdMap =
      new HashMap<String, PropertyDescriptor>();

    for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
      pdMap.put(pd.getName(), pd);
    }

    // Wrap the result so we can assert for properties that don't exist.
    return new AbstractMap<String, PropertyDescriptor>() {

        @Override
        public PropertyDescriptor get(Object propertyName) {
          final PropertyDescriptor result = pdMap.get(propertyName);

          assertNotNull(
            "Property '" + propertyName + "' exists in " + beanClass, result);

          return result;
        }

        @Override
        public Set<Entry<String, PropertyDescriptor>> entrySet() {
          return pdMap.entrySet();
        }
      };
  }

  /**
   * Convenience wrapper around a PropertyDescriptor.
   */
  private static final class PropertyAccessor {
    private final PropertyDescriptor propertyDescriptor;
    private final Method writeMethod;
    private final Method readMethod;

    PropertyAccessor(PropertyDescriptor propertyDescriptor) {
      this.propertyDescriptor = propertyDescriptor;
      this.readMethod = propertyDescriptor.getReadMethod();
      this.writeMethod = propertyDescriptor.getWriteMethod();
    }

    public Object get(Object bean) throws Exception {
      assertNotNull(
        propertyDescriptor.getName() + " has a read method", readMethod);

      return readMethod.invoke(bean);
    }

    public void set(Object bean, Object value) throws Exception {
      assertNotNull(
        propertyDescriptor.getName() + " has a write method", writeMethod);

      writeMethod.invoke(bean, value);
    }
  }

  /**
   * Template methods - subclasses should assert things are OK when they are
   * called in order.
   * @see #testBasicAccessors
   */
  private static abstract class Script {

    void testSetAndGet() throws Exception {
      setData();
      getData();
    }

    abstract void setData() throws Exception;
    abstract void getData() throws Exception;
  }

  public static class ValueScript extends Script {
    private final Object bean;
    private final PropertyAccessor propertyAccessor;
    private final Object value;

    ValueScript(Object bean,
                     PropertyAccessor propertyAccessor,
                     Object value) {
      this.bean = bean;
      this.propertyAccessor = propertyAccessor;
      this.value = value;
    }

    @Override public void setData() throws Exception {
      propertyAccessor.set(bean, value);
    }

    @Override public void getData() throws Exception {
      final Object result = propertyAccessor.get(bean);
      assertEquals(value, result);
    }
  }

  public static class CompositeScript extends Script {
    private final List<Script> scripts;

    CompositeScript(List<Script> scripts) {
      this.scripts = scripts;
    }

    @Override
    public void getData() throws Exception {
      for (Script s : scripts) { s.getData(); }
    }

    @Override
    public void setData() throws Exception {
      for (Script s : scripts) { s.setData(); }
    }
  }
}
