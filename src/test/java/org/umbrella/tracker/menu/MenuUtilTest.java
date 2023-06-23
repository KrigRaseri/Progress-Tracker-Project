package org.umbrella.tracker.menu;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuUtilTest {

    /**
     * Method under test: {@link MenuUtil#firstNameCheck(String)}
     */
    @ParameterizedTest
    @ValueSource(strings = {"Jane", "John", "jean-claude", "O'Neill"})
    void first_name_validation_test_should_be_valid(String name) {
        // when
        boolean result = MenuUtil.firstNameCheck(name);
        // then
        assertThat(result).isTrue();
    }

    /**
     * Method under test: {@link MenuUtil#firstNameCheck(String)}
     */
    @ParameterizedTest
    @ValueSource(strings = {"Jane1", "John2", "J", "J.", "Stanisław", "Oğuz", "o niel", "o_niel", "o''niel"})
    void first_name_validation_test_should_be_invalid(String name) {
        // when
        boolean result = MenuUtil.firstNameCheck(name);
        // then
        assertThat(result).isFalse();
    }

    /**
     * Method under test: {@link MenuUtil#lastNameCheck(String)}
     */
    @ParameterizedTest
    @ValueSource(strings = {"Doe boo joo", "Smith", "jean-claude", "O'Neill"})
    void last_name_validation_test_should_be_valid(String name) {
        // when
        boolean result = MenuUtil.lastNameCheck(name);
        // then
        assertThat(result).isTrue();
    }

    /**
     * Method under test: {@link MenuUtil#lastNameCheck(String)}
     */
    @ParameterizedTest
    @ValueSource(strings = {"Doe1", "Smith2", "S", "S.", "Stanisław", "Oğuz", "o niel", "o_niel", "o''niel", "o'niel von bon"})
    void last_name_validation_test_should_be_invalid(String name) {
        // when
        boolean result = MenuUtil.lastNameCheck(name);
        // then
        assertThat(result).isFalse();
    }

    /**
     * Method under test: {@link MenuUtil#emailCheck(String)}
     */
    @ParameterizedTest
    @ValueSource(strings = {"jane.doe@example.com", "name@domain.net", "name@domm.co.uk"})
    void email_validation_test_should_be_valid(String email) {
        // when
        boolean result = MenuUtil.emailCheck(email);
        // then
        assertThat(result).isTrue();
    }

    /**
     * Method under test: {@link MenuUtil#emailCheck(String)}
     */
    @ParameterizedTest
    @ValueSource(strings = {"jane.doeexample", "name@domain", "name@domm."})
    void email_verification_test_should_be_invalid(String email) {
        // when
        boolean result = MenuUtil.emailCheck(email);
        // then
        assertThat(result).isFalse();
    }
}

