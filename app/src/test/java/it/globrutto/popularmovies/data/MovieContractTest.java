package it.globrutto.popularmovies.data;

import android.provider.BaseColumns;

import junit.framework.Assert;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by giuseppelobrutto on 13/03/17.
 */

public class MovieContractTest {

    @Test
    public void innerClassExists() throws Exception {
        Class[] innerClass = MovieContract.class.getDeclaredClasses();
        Assert.assertEquals("There should be one inner class inside the contract class", 1, innerClass.length);
    }

    @Test
    public void innerClassTypeCorrect() {
        Class[] innerClass = MovieContract.class.getDeclaredClasses();
        Assert.assertEquals("Connot find inner class", 1, innerClass.length);
        Class entryClass = innerClass[0];
        Assert.assertTrue("Should implement the BaseColumns interface", BaseColumns.class.isAssignableFrom(entryClass));
        Assert.assertTrue("Inner class should be final", Modifier.isFinal(entryClass.getModifiers()));
        Assert.assertTrue("Inner class should be static", Modifier.isStatic(entryClass.getModifiers()));
    }

    @Test
    public void innerClassMembersCorrect() throws Exception {
        Class[] innerClass = MovieContract.class.getDeclaredClasses();
        Assert.assertEquals("Cannot find unit test", 1, innerClass.length);
        Class entryClass = innerClass[0];
        Field[] fields = entryClass.getDeclaredFields();
        Assert.assertEquals("There should be 9 String members", 9, fields.length);
        for (Field field : fields) {
            Assert.assertTrue("All members should be String", field.getType() == String.class);
            Assert.assertTrue("All members should be final", Modifier.isFinal(field.getModifiers()));
            Assert.assertTrue("All members should be static", Modifier.isStatic(field.getModifiers()));
        }
    }
}
