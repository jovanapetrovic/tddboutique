package com.jovana.service.integration;

import com.jovana.entity.shippingaddress.ShippingAddress;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.repositories.shippingaddress.ShippingAddressRepository;
import com.jovana.service.impl.shippingaddress.ShippingAddressService;
import com.jovana.service.integration.auth.WithMockCustomUser;
import com.jovana.service.util.RequestTestDataProvider;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

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

        @WithMockCustomUser
        @DisplayName("Then ShippingAddress is fetched from database when id is valid")
        @Test
        public void testGetShippingAddressById() {
            // prepare
            Long SHIPPING_ADDRESS_ID_EXISTS = 10L;
            // exercise
            ShippingAddress shippingAddress = shippingAddressService.getUserShippingAddressById(SHIPPING_ADDRESS_ID_EXISTS);
            // verify
            assertNotNull(shippingAddress, "ShippingAddress is null");
        }

    }

    @WithMockCustomUser
    @DisplayName("When we want to add shipping address for newly registered user")
    @Nested
    class AddUserShippingAddressTest {

        @WithMockCustomUser
        @DisplayName("Then shipping address is created for user when valid ShippingAddressRequest is passed")
        @Test
        public void testAddUserShippingAddressSuccess() {
            // prepare
            Long TEST_USER_ID = 10L;
            ShippingAddressRequest shippingAddressRequest = RequestTestDataProvider.getShippingAddressRequests().get("john");

            // exercise
            Long shippingAddressId = shippingAddressService.addUserShippingAddress(TEST_USER_ID, shippingAddressRequest);

            // verify
            ShippingAddress newShippingAddress = shippingAddressService.getUserShippingAddressById(shippingAddressId);

            assertAll("Verify new shipping address",
                    () -> assertNotNull(newShippingAddress, "ShippingAddress is null"),
                    () -> assertNotNull(newShippingAddress.getUser(), "User is null"),
                    () -> assertNotNull(newShippingAddress.getFirstName(), "First name is null"),
                    () -> assertEquals(shippingAddressRequest.getFirstName(), newShippingAddress.getFirstName(), "First name doesn't match"),
                    () -> assertNotNull(newShippingAddress.getLastName(), "Last name is null"),
                    () -> assertEquals(shippingAddressRequest.getLastName(), newShippingAddress.getLastName(), "Last name doesn't match"),
                    () -> assertNotNull(newShippingAddress.getAddress(), "Address is null"),
                    () -> assertEquals(shippingAddressRequest.getAddress(), newShippingAddress.getAddress(), "Address doesn't match"),
                    () -> assertNotNull(newShippingAddress.getZipCode(), "Zip code is null"),
                    () -> assertEquals(shippingAddressRequest.getZipCode(), newShippingAddress.getZipCode(), "Zip code doesn't match"),
                    () -> assertNotNull(newShippingAddress.getCity(), "City is null"),
                    () -> assertEquals(shippingAddressRequest.getCity(), newShippingAddress.getCity(), "City doesn't match"),
                    () -> assertNotNull(newShippingAddress.getCountry(), "Country is null"),
                    () -> assertEquals(shippingAddressRequest.getCountry(), newShippingAddress.getCountry(), "Country doesn't match"),
                    () -> assertNotNull(newShippingAddress.getPhone().getPhoneNumber(), "Phone number is null"),
                    () -> assertEquals(shippingAddressRequest.getPhoneNumber(), newShippingAddress.getPhone().getPhoneNumber(), "Phone number doesn't match")
            );
        }
    }

    @DisplayName("When we want to update shipping address for registered user")
    @Nested
    class UpdateUserShippingAddressTest {

        @WithMockCustomUser
        @DisplayName("Then shipping address is updated when valid ShippingAddressRequest is passed")
        @Test
        public void testUpdateUserShippingAddressSuccess() {
            // prepare
            Long TEST_SHIPPING_ADDRESS_ID = 11L;
            ShippingAddressRequest shippingAddressRequest = RequestTestDataProvider.getShippingAddressRequests().get("updateRequest");

            // exercise
            shippingAddressService.updateUserShippingAddress(TEST_SHIPPING_ADDRESS_ID, shippingAddressRequest);

            // verify
            ShippingAddress updatedShippingAddress = shippingAddressService.getUserShippingAddressById(TEST_SHIPPING_ADDRESS_ID);

            assertAll("Verify updated shipping address",
                    () -> assertEquals(shippingAddressRequest.getFirstName(), updatedShippingAddress.getFirstName(), "First name doesn't match"),
                    () -> assertEquals(shippingAddressRequest.getLastName(), updatedShippingAddress.getLastName(), "Last name doesn't match"),
                    () -> assertEquals(shippingAddressRequest.getAddress(), updatedShippingAddress.getAddress(), "Address doesn't match"),
                    () -> assertEquals(shippingAddressRequest.getZipCode(), updatedShippingAddress.getZipCode(), "Zip code doesn't match"),
                    () -> assertEquals(shippingAddressRequest.getCity(), updatedShippingAddress.getCity(), "City doesn't match"),
                    () -> assertEquals(shippingAddressRequest.getCountry(), updatedShippingAddress.getCountry(), "Country doesn't match"),
                    () -> assertEquals(shippingAddressRequest.getPhoneNumber(), updatedShippingAddress.getPhone().getPhoneNumber(), "Phone number doesn't match")
            );
        }
    }

}
