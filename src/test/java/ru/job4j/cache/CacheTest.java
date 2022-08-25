package ru.job4j.cache;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CacheTest {

    @Test
    public void whenAddBaseTheSame() {
        Cache cache = new Cache();
        Base base = new Base(0, 0);
        assertThat(cache.add(base)).isTrue();
        assertThat(cache.add(base)).isFalse();
    }

    @Test
    public void whenUpdateBase() {
        Cache cache = new Cache();
        Base base1 = new Base(0, 0);
        base1.setName("base");
        Base base2 = new Base(1, 0);
        assertThat(cache.add(base1)).isTrue();
        assertThat(cache.get(0).getName()).isEqualTo("base");
        assertThat(cache.add(base2)).isTrue();
        base1.setName("baseSet");
        assertThat(cache.update(base1)).isTrue();
        assertThat(cache.get(0).getName()).isEqualTo("baseSet");
        assertThat(cache.get(0).getVersion()).isEqualTo(1);
    }

    @Test
    public void whenDeleteBase() {
        Cache cache = new Cache();
        Base base1 = new Base(0, 0);
        base1.setName("base");
        Base base2 = new Base(1, 0);
        assertThat(cache.add(base1)).isTrue();
        assertThat(cache.add(base2)).isTrue();
        cache.delete(base1);
        assertThat(cache.get(0)).isNull();
    }

    @Test
    public void whenThrownException() {
        Cache cache = new Cache();
        Base base1 = new Base(0, 0);
        Base base2 = new Base(0, 1);
        cache.add(base1);
        assertThatThrownBy(() -> cache.update(base2))
                .hasMessage("Versions are not equal");
    }
}