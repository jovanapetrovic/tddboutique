package com.jovana.service.impl;

import com.jovana.entity.shippingaddress.ShippingAddress;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.entity.shippingaddress.exception.InvalidPhoneNumberException;
import com.jovana.entity.user.User;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.exception.ValueMustNotBeNullOrEmptyException;
import com.jovana.repositories.shippingaddress.ShippingAddressRepository;
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

        private ShippingAddressRequest shippingAddressRequest;
        private ShippingAddressRequest shippingAddressRequestWhenUserDataIsCopied;
        private ShippingAddressRequest shippingAddressRequestWithoutFirstName;
        private ShippingAddressRequest shippingAddressRequestWithoutLastName;
        private ShippingAddressRequest shippingAddressRequestWithInvalidPhoneNumber;
        private ShippingAddressRequest shippingAddressRequestWithUnparsablePhoneNumber;
        private ShippingAddress johnShippingAddress;
        private ShippingAddress janeShippingAddress;
        private User johnUser;
        private User janeUser;

        @BeforeEach
        void setUp() {
            // set requests
            shippingAddressRequest = ShippingAddressRequest.createUserShippingAddressRequest(
                    false,
                    "John",
                    "Doe",
                    "Pobedina 1",
                    18000L,
                    "Nis",
                    "Serbia",
                    "+38164123456");

            shippingAddressRequestWhenUserDataIsCopied = ShippingAddressRequest.createUserShippingAddressRequest(
                    true,
                    null,
                    null,
                    "Pobedina 1",
                    18000L,
                    "Nis",
                    "Serbia",
                    "+38164123456");

            shippingAddressRequestWithoutFirstName = ShippingAddressRequest.createUserShippingAddressRequest(
                    false,
                    null,
                    "Doe",
                    "Pobedina 1",
                    18000L,
                    "Nis",
                    "Serbia",
                    "+38164123456");

            shippingAddressRequestWithoutLastName = ShippingAddressRequest.createUserShippingAddressRequest(
                    false,
                    "Jane",
                    null,
                    "Pobedina 1",
                    18000L,
                    "Nis",
                    "Serbia",
                    "+38164123456");

            shippingAddressRequestWithInvalidPhoneNumber = ShippingAddressRequest.createUserShippingAddressRequest(
                    false,
                    "Jane",
                    "Doe",
                    "Pobedina 1",
                    18000L,
                    "Nis",
                    "Serbia",
                    "+38118123456");

            shippingAddressRequestWithUnparsablePhoneNumber = ShippingAddressRequest.createUserShippingAddressRequest(
                    false,
                    "Jane",
                    "Doe",
                    "Pobedina 1",
                    18000L,
                    "Nis",
                    "Serbia",
                    "unparsable");

            // set users
            johnUser = TestDataProvider.getUsers().get("john");
            janeUser = TestDataProvider.getUsers().get("jane");

            // set shipping addresses
            johnShippingAddress = TestDataProvider.getShippingAddresses().get("johnShippingAddress");
            janeShippingAddress = TestDataProvider.getShippingAddresses().get("janeShippingAddress");
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
        }

        @DisplayName("Then shipping address is created for user and first and last name are copied from user")
        @Test
        public void testAddUserShippingAddressSuccessWhenFirstAndLastNameAreCopiedFromUser() {
            // prepare
            when(userService.getUserById(any(Long.class))).thenReturn(janeUser);
            when(shippingAddressRepository.save(any(ShippingAddress.class))).thenReturn(janeShippingAddress);
            when(shippingAddressRepository.findById(any(Long.class))).thenReturn(Optional.of(janeShippingAddress));

            // exercise
            Long shippingAddressId = shippingAddressService.addUserShippingAddress(janeUser.getId(), shippingAddressRequestWhenUserDataIsCopied);

            // verify
            ShippingAddress newShippingAddress = shippingAddressService.getUserShippingAddressById(shippingAddressId);

            assertAll("Verify Jane's shipping address",
                    () -> Assertions.assertNotNull(newShippingAddress, "ShippingAddress is null"),
                    () -> Assertions.assertNotNull(newShippingAddress.getFirstName(), "First name is null"),
                    () -> Assertions.assertEquals(shippingAddressRequestWhenUserDataIsCopied.getFirstName(), newShippingAddress.getFirstName(), "First name doesn't match"),
                    () -> Assertions.assertNotNull(newShippingAddress.getLastName(), "Last name is null"),
                    () -> Assertions.assertEquals(shippingAddressRequestWhenUserDataIsCopied.getLastName(), newShippingAddress.getLastName(), "Last name doesn't match"),
                    () -> Assertions.assertEquals(shippingAddressRequestWhenUserDataIsCopied.getAddress(), newShippingAddress.getAddress(), "Address doesn't match"),
                    () -> Assertions.assertEquals(shippingAddressRequestWhenUserDataIsCopied.getZipCode(), newShippingAddress.getZipCode(), "Zip code doesn't match"),
                    () -> Assertions.assertEquals(shippingAddressRequestWhenUserDataIsCopied.getCity(), newShippingAddress.getCity(), "City doesn't match"),
                    () -> Assertions.assertEquals(shippingAddressRequestWhenUserDataIsCopied.getCountry(), newShippingAddress.getCountry(), "Country doesn't match"),
                    () -> Assertions.assertEquals(shippingAddressRequestWhenUserDataIsCopied.getPhoneNumber(), newShippingAddress.getPhone().getPhoneNumber(), "Phone number doesn't match")
            );
        }

        @DisplayName("Then creating shipping address fails when first name is not provided")
        @Test
        public void testAddUserShippingAddressFailsWhenFirstNameIsNotProvided() {
            // verify
            assertThrows(ValueMustNotBeNullOrEmptyException.class,
                    () -> shippingAddressService.addUserShippingAddress(janeUser.getId(), shippingAddressRequestWithoutFirstName), "First name must be provided");
        }

        @DisplayName("Then creating shipping address fails when last name is not provided")
        @Test
        public void testAddUserShippingAddressFailsWhenLastNameIsNotProvided() {
            // verify
            assertThrows(ValueMustNotBeNullOrEmptyException.class,
                    () -> shippingAddressService.addUserShippingAddress(janeUser.getId(), shippingAddressRequestWithoutLastName), "Last name must be provided");
        }

        @DisplayName("Then creating shipping address fails when phone number is invalid")
        @Test
        public void testAddUserShippingAddressFailsWhenPhoneNumberIsInvalid() {
            // verify
            assertThrows(InvalidPhoneNumberException.class,
                    () -> shippingAddressService.addUserShippingAddress(janeUser.getId(), shippingAddressRequestWithInvalidPhoneNumber), "Phone number is not valid.");
        }

        @DisplayName("Then creating shipping address fails when phone number cannot be parsed")
        @Test
        public void testAddUserShippingAddressFailsWhenPhoneNumberCannotBeParsed() {
            // verify
            assertThrows(InvalidPhoneNumberException.class,
                    () -> shippingAddressService.addUserShippingAddress(janeUser.getId(), shippingAddressRequestWithUnparsablePhoneNumber), "Phone number is not valid.");
        }

    }

}