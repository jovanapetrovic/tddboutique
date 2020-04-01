package com.jovana.service.impl;

import com.jovana.entity.shippingaddress.ShippingAddress;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.entity.shippingaddress.exception.InvalidPhoneNumberException;
import com.jovana.entity.user.User;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.exception.ValueMustNotBeNullOrEmptyException;
import com.jovana.repositories.shippingaddress.ShippingAddressRepository;
import com.jovana.service.util.RequestTestDataProvider;
import com.jovana.service.util.TestDataProvider;
import com.jovana.service.impl.shippingaddress.ShippingAddressService;
import com.jovana.service.impl.shippingaddress.ShippingAddressServiceImpl;
import com.jovana.service.impl.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Created by jovana on 31.03.2020
 */
@ExtendWith(MockitoExtension.class)
public class ShippingAddressServiceImplTest {

    @InjectMocks
    private ShippingAddressService shippingAddressService = new ShippingAddressServiceImpl();
    @Mock
    private ShippingAddressRepository shippingAddressRepository;
    @Mock
    private UserService userService;

    @DisplayName("When we want to find a ShippingAddress by id")
    @Nested
    class GetUserTest {

        private final Long SHIPPING_ADDRESS_ID_EXISTS = 10L;
        private final Long SHIPPING_ADDRESS_ID_NOT_EXISTS = 9999L;

        @DisplayName("Then ShippingAddress is fetched from database when id is valid")
        @Test
        public void testGetShippingAddressById() {
            // prepare
            when(shippingAddressRepository.findById(any(Long.class))).thenReturn(Optional.of(mock(ShippingAddress.class)));
            // exercise
            ShippingAddress shippingAddress = shippingAddressService.getUserShippingAddressById(SHIPPING_ADDRESS_ID_EXISTS);
            // verify
            Assertions.assertNotNull(shippingAddress, "ShippingAddress is null");
        }

        @DisplayName("Then error is thrown when ShippingAddress with passed id doesn't exist")
        @Test
        public void testGetShippingAddressByIdFailsWhenUserWithPassedIdDoesntExist() {
            // prepare
            when(shippingAddressRepository.findById(any(Long.class))).thenReturn(Optional.empty());
            // verify
            assertThrows(EntityNotFoundException.class,
                    () -> shippingAddressService.getUserShippingAddressById(SHIPPING_ADDRESS_ID_NOT_EXISTS), "ShippingAddress with id=" + SHIPPING_ADDRESS_ID_NOT_EXISTS + " doesn't exist");
        }
    }

    @DisplayName("When we want to add shipping address for newly registered user")
    @Nested
    class RegisterUserAddShippingAddressTest {

        private final String TEST_FIRSTNAME = "firstname";
        private final String TEST_LASTNAME = "lastname";
        private final String TEST_INVALID_PHONE_NUMBER = "+38118123456";
        private final String TEST_UNPARSABLE_PHONE_NUMBER = "unparsable";

        private ShippingAddressRequest shippingAddressRequest;
        private ShippingAddressRequest shippingAddressRequestWithoutNames;
        private User johnUser;
        private User janeUser;
        private ShippingAddress johnShippingAddress;
        private ShippingAddress janeShippingAddress;

        @BeforeEach
        void setUp() {
            // set requests
            shippingAddressRequest = RequestTestDataProvider.getShippingAddressRequests().get("john");
            shippingAddressRequestWithoutNames = RequestTestDataProvider.getShippingAddressRequests().get("noNames");

            // set users
            johnUser = TestDataProvider.getUsers().get("john");
            janeUser = TestDataProvider.getUsers().get("jane");

            // set shipping addresses
            johnShippingAddress = TestDataProvider.getShippingAddresses().get("john");
            janeShippingAddress = TestDataProvider.getShippingAddresses().get("jane");
        }

        @DisplayName("Then shipping address is created for user when valid ShippingAddressRequest is passed")
        @Test
        public void testAddUserShippingAddressSuccess() {
            // prepare
            when(shippingAddressRepository.save(any(ShippingAddress.class))).thenReturn(johnShippingAddress);
            when(shippingAddressRepository.findById(any(Long.class))).thenReturn(Optional.of(johnShippingAddress));

            // exercise
            Long shippingAddressId = shippingAddressService.addUserShippingAddress(johnUser.getId(), shippingAddressRequest);

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
            verify(shippingAddressRepository, times(1)).save(any(ShippingAddress.class));
        }

        @DisplayName("Then shipping address is created for user and first and last name are copied from user")
        @Test
        public void testAddUserShippingAddressSuccessWhenFirstAndLastNameAreCopiedFromUser() {
            // prepare
            when(userService.getUserById(any(Long.class))).thenReturn(janeUser);
            when(shippingAddressRepository.save(any(ShippingAddress.class))).thenReturn(janeShippingAddress);
            when(shippingAddressRepository.findById(any(Long.class))).thenReturn(Optional.of(janeShippingAddress));

            // exercise
            Long shippingAddressId = shippingAddressService.addUserShippingAddress(janeUser.getId(), shippingAddressRequestWithoutNames);

            // verify
            ShippingAddress newShippingAddress = shippingAddressService.getUserShippingAddressById(shippingAddressId);

            assertAll("Verify Jane's shipping address",
                    () -> Assertions.assertNotNull(newShippingAddress, "ShippingAddress is null"),
                    () -> Assertions.assertNotNull(newShippingAddress.getFirstName(), "First name is null"),
                    () -> Assertions.assertEquals(shippingAddressRequestWithoutNames.getFirstName(), newShippingAddress.getFirstName(), "First name doesn't match"),
                    () -> Assertions.assertNotNull(newShippingAddress.getLastName(), "Last name is null"),
                    () -> Assertions.assertEquals(shippingAddressRequestWithoutNames.getLastName(), newShippingAddress.getLastName(), "Last name doesn't match"),
                    () -> Assertions.assertEquals(shippingAddressRequestWithoutNames.getAddress(), newShippingAddress.getAddress(), "Address doesn't match"),
                    () -> Assertions.assertEquals(shippingAddressRequestWithoutNames.getZipCode(), newShippingAddress.getZipCode(), "Zip code doesn't match"),
                    () -> Assertions.assertEquals(shippingAddressRequestWithoutNames.getCity(), newShippingAddress.getCity(), "City doesn't match"),
                    () -> Assertions.assertEquals(shippingAddressRequestWithoutNames.getCountry(), newShippingAddress.getCountry(), "Country doesn't match"),
                    () -> Assertions.assertEquals(shippingAddressRequestWithoutNames.getPhoneNumber(), newShippingAddress.getPhone().getPhoneNumber(), "Phone number doesn't match")
            );
            verify(shippingAddressRepository, times(1)).save(any(ShippingAddress.class));
        }

        @DisplayName("Then creating shipping address fails when first name is not provided")
        @Test
        public void testAddUserShippingAddressFailsWhenFirstNameIsNotProvided() {
            // prepare
            ShippingAddressRequest shippingAddressRequest = mock(ShippingAddressRequest.class);
            when(shippingAddressRequest.getUseFirstAndLastNameFromUser()).thenReturn(false);

            // verify
            assertThrows(ValueMustNotBeNullOrEmptyException.class,
                    () -> shippingAddressService.addUserShippingAddress(janeUser.getId(), shippingAddressRequest), "First name must be provided");
            verify(shippingAddressRepository, times(0)).save(any(ShippingAddress.class));
        }

        @DisplayName("Then creating shipping address fails when last name is not provided")
        @Test
        public void testAddUserShippingAddressFailsWhenLastNameIsNotProvided() {
            // prepare
            ShippingAddressRequest shippingAddressRequest = mock(ShippingAddressRequest.class);
            when(shippingAddressRequest.getUseFirstAndLastNameFromUser()).thenReturn(false);
            when(shippingAddressRequest.getFirstName()).thenReturn(TEST_FIRSTNAME);

            // verify
            assertThrows(ValueMustNotBeNullOrEmptyException.class,
                    () -> shippingAddressService.addUserShippingAddress(janeUser.getId(), shippingAddressRequest), "Last name must be provided");
            verify(shippingAddressRepository, times(0)).save(any(ShippingAddress.class));
        }

        @DisplayName("Then creating shipping address fails when phone number is invalid")
        @Test
        public void testAddUserShippingAddressFailsWhenPhoneNumberIsInvalid() {
            // prepare
            ShippingAddressRequest shippingAddressRequest = mock(ShippingAddressRequest.class);
            when(shippingAddressRequest.getFirstName()).thenReturn(TEST_FIRSTNAME);
            when(shippingAddressRequest.getLastName()).thenReturn(TEST_LASTNAME);
            when(shippingAddressRequest.getPhoneNumber()).thenReturn(TEST_INVALID_PHONE_NUMBER);

            // verify
            assertThrows(InvalidPhoneNumberException.class,
                    () -> shippingAddressService.addUserShippingAddress(janeUser.getId(), shippingAddressRequest), "Phone number is not valid.");
            verify(shippingAddressRepository, times(0)).save(any(ShippingAddress.class));
        }

        @DisplayName("Then creating shipping address fails when phone number cannot be parsed")
        @Test
        public void testAddUserShippingAddressFailsWhenPhoneNumberCannotBeParsed() {
            // prepare
            ShippingAddressRequest shippingAddressRequest = mock(ShippingAddressRequest.class);
            when(shippingAddressRequest.getFirstName()).thenReturn(TEST_FIRSTNAME);
            when(shippingAddressRequest.getLastName()).thenReturn(TEST_LASTNAME);
            when(shippingAddressRequest.getPhoneNumber()).thenReturn(TEST_UNPARSABLE_PHONE_NUMBER);

            // verify
            assertThrows(InvalidPhoneNumberException.class,
                    () -> shippingAddressService.addUserShippingAddress(janeUser.getId(), shippingAddressRequest), "Phone number is not valid.");
            verify(shippingAddressRepository, times(0)).save(any(ShippingAddress.class));
        }

    }

}