package com.jovana.service.integration;

import com.jovana.entity.shippingaddress.ShippingAddress;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.repositories.shippingaddress.ShippingAddressRepository;
import com.jovana.service.impl.shippingaddress.ShippingAddressService;
import com.jovana.service.util.RequestTestDataProvider;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Created by jovana on 01.04.2020
 */
@FlywayTest(locationsForMigrate = {"db.additional.shippingaddress"})
public class ShippingAddressServiceImplIT extends AbstractTest {

    @Autowired
    private ShippingAddressService shippingAddressService;
    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    @DisplayName("When we want to find a ShippingAddress by id")
    @Nested
    class GetShippingAddressTest {

        private final Long SHIPPING_ADDRESS_ID_EXISTS = 10L;

        @DisplayName("Then ShippingAddress is fetched from database when id is valid")
        @Test
        public void testGetShippingAddressById() {
            // exercise
            ShippingAddress shippingAddress = shippingAddressService.getUserShippingAddressById(SHIPPING_ADDRESS_ID_EXISTS);
            // verify
            Assertions.assertNotNull(shippingAddress, "ShippingAddress is null");
        }

    }

    @DisplayName("When we want to add shipping address for newly registered user")
    @Nested
    class RegisterUserAddShippingAddressTest {

        private final Long TEST_USER_ID = 10L;

        private ShippingAddressRequest shippingAddressRequest;

        @BeforeEach
        void setUp() {
            // set requests
            shippingAddressRequest = RequestTestDataProvider.getShippingAddressRequests().get("john");
        }

        @DisplayName("Then shipping address is created for user when valid ShippingAddressRequest is passed")
        @Test
        public void testAddUserShippingAddressSuccess() {
            // exercise
            Long shippingAddressId = shippingAddressService.addUserShippingAddress(TEST_USER_ID, shippingAddressRequest);

            // verify
            ShippingAddress newShippingAddress = shippingAddressService.getUserShippingAddressById(shippingAddressId);

            assertAll("Verify John's shipping address",
                    () -> Assertions.assertNotNull(newShippingAddress, "ShippingAddress is null"),
                    () -> Assertions.assertNotNull(newShippingAddress.getFirstName(), "First name is null"),
                    () -> Assertions.assertEquals(shippingAddressRequest.getFirstName(), newShippingAddress.getFirstName(), "First name doesn't match"),
                    () -> Assertions.assertNotNull(newShippingAddress.getLastName(), "Last name is null"),
                    () -> Assertions.assertEquals(shippingAddressRequest.getLastName(), newShippingAddress.getLastName(), "Last name doesn't match"),
                    () -> Assertions.assertNotNull(newShippingAddress.getAddress(), "Address is null"),
                    () -> Assertions.assertEquals(shippingAddressRequest.getAddress(), newShippingAddress.getAddress(), "Address doesn't match"),
                    () -> Assertions.assertNotNull(newShippingAddress.getZipCode(), "Zip code is null"),
                    () -> Assertions.assertEquals(shippingAddressRequest.getZipCode(), newShippingAddress.getZipCode(), "Zip code doesn't match"),
                    () -> Assertions.assertNotNull(newShippingAddress.getCity(), "City is null"),
                    () -> Assertions.assertEquals(shippingAddressRequest.getCity(), newShippingAddress.getCity(), "City doesn't match"),
                    () -> Assertions.assertNotNull(newShippingAddress.getCountry(), "Country is null"),
                    () -> Assertions.assertEquals(shippingAddressRequest.getCountry(), newShippingAddress.getCountry(), "Country doesn't match"),
                    () -> Assertions.assertNotNull(newShippingAddress.getPhone().getPhoneNumber(), "Phone number is null"),
                    () -> Assertions.assertEquals(shippingAddressRequest.getPhoneNumber(), newShippingAddress.getPhone().getPhoneNumber(), "Phone number doesn't match")
            );
        }
    }

}
