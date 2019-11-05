package com.orhanobut.hawk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static com.orhanobut.hawk.Hawk.db;

@RunWith(RobolectricTestRunner.class)
public class HawkDBIntegrationTest {

    @Before
    public void setUp() {
        Hawk.init(RuntimeEnvironment.application).build();
    }

    @After
    public void tearDown() {
        if (db().isBuilt()) {
            db().deleteAll();
        }
        db().destroy();
    }

    @Test
    public void testSingleItem() {
        db().put("boolean", true);
        assertThat(db().get("boolean")).isEqualTo(true);

        db().put("string", "string");
        assertThat(db().get("string")).isEqualTo("string");

        db().put("float", 1.5f);
        assertThat(db().get("float")).isEqualTo(1.5f);

        db().put("integer", 10);
        assertThat(db().get("integer")).isEqualTo(10);

        db().put("char", 'A');
        assertThat(db().get("char")).isEqualTo('A');

        db().put("object", new FooBar());
        FooBar fooBar = db().get("object");

        assertThat(fooBar).isNotNull();
        assertThat(fooBar.getName()).isEqualTo("hawk");

        assertThat(db().put("innerClass", new FooBar.InnerFoo())).isTrue();
        FooBar.InnerFoo innerFoo = db().get("innerClass");
        assertThat(innerFoo).isNotNull();
        assertThat(innerFoo.getName()).isEqualTo("hawk");
    }

    @Test
    public void testSingleItemDefault() {
        boolean result = db().get("tag", true);
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void testList() {
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");

        db().put("tag", list);

        List<String> list1 = db().get("tag");

        assertThat(list1).isNotNull();
        assertThat(list1.get(0)).isEqualTo("foo");
        assertThat(list1.get(1)).isEqualTo("bar");
    }

    @Test
    public void testEmptyList() {
        List<FooBar> list = new ArrayList<>();
        db().put("tag", list);

        List<FooBar> list1 = db().get("tag");

        assertThat(list1).isNotNull();
    }

    @Test
    public void testMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        db().put("map", map);

        Map<String, String> map1 = db().get("map");

        assertThat(map1).isNotNull();
        assertThat(map1.get("key")).isEqualTo("value");
    }

    @Test
    public void testEmptyMap() {
        Map<String, FooBar> map = new HashMap<>();
        db().put("tag", map);

        Map<String, FooBar> map1 = db().get("tag");

        assertThat(map1).isNotNull();
    }

    @Test
    public void testSet() {
        Set<String> set = new HashSet<>();
        set.add("foo");
        db().put("set", set);

        Set<String> set1 = db().get("set");

        assertThat(set1).isNotNull();
        assertThat(set1.contains("foo")).isTrue();
    }

    @Test
    public void testEmptySet() {
        Set<FooBar> set = new HashSet<>();
        db().put("tag", set);

        Set<FooBar> set1 = db().get("tag");

        assertThat(set1).isNotNull();
    }

    @Test
    public void testCount() {
        db().deleteAll();
        String value = "test";
        db().put("tag", value);
        db().put("tag1", value);
        db().put("tag2", value);
        db().put("tag3", value);
        db().put("tag4", value);

        assertThat(db().count()).isEqualTo(5);
    }

    @Test
    public void testDeleteAll() {
        String value = "test";
        db().put("tag", value);
        db().put("tag1", value);
        db().put("tag2", value);

        db().deleteAll();

        assertThat(db().count()).isEqualTo(0);
    }

    @Test
    public void testDelete() {
        db().deleteAll();
        String value = "test";
        db().put("tag", value);
        db().put("tag1", value);
        db().put("tag2", value);

        db().delete("tag");

        String result = db().get("tag");

        assertThat(result).isNull();
        assertThat(db().count()).isEqualTo(2);
    }

    @Test
    public void testContains() {
        db().put("key", "value");

        assertThat(db().contains("key")).isTrue();
    }


    @Test
    public void testHugeData() {
        for (int i = 0; i < 100; i++) {
            db().put("" + i, "" + i);
        }
        assertThat(true).isTrue();
    }

}
