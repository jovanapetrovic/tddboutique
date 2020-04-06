package com.jovana.service.impl.shippingaddress;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.jovana.entity.shippingaddress.ShippingAddress;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.entity.shippingaddress.exception.InvalidPhoneNumberException;
import com.jovana.entity.user.User;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.exception.ValueMustNotBeNullOrEmptyException;
import com.jovana.repositories.shippingaddress.ShippingAddressRepository;
import com.jovana.service.impl.user.UserService;
import com.jovana.service.security.IsAdmin;
import com.jovana.service.security.IsAdminOrUser;
import com.jovana.service.security.IsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by jovana on 31.03.2020
 */
@Service
@Transactional
public class ShippingAddressServiceImpl implements ShippingAddressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingAddressServiceImpl.class);

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;
    @Autowired
    private UserService userService;

    @IsAdminOrUser
    @Override
    public ShippingAddress getUserShippingAddressById(Long shippingAddressId) {
        Optional<ShippingAddress> shippingAddress = shippingAddressRepository.findById(shippingAddressId);
        if (shippingAddress.isEmpty()) {
            LOGGER.info("ShippingAddress with id = {} was not found in the db.", shippingAddressId);
            throw new EntityNotFoundException("No ShippingAddress found with id = " + shippingAddressId);
        }
        return shippingAddress.get();
    }

    @IsUser
    @Override
    public Long addUserShippingAddress(Long userId, ShippingAddressRequest shippingAddressRequest) {
        validateFirstAndLastName(userId, shippingAddressRequest);
        validatePhoneNumber(shippingAddressRequest.getPhoneNumber());

        User user = userService.getUserById(userId);
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setUser(user);
        shippingAddress.setFirstName(shippingAddressRequest.getFirstName());
        shippingAddress.setLastName(shippingAddressRequest.getLastName());
        shippingAddress.setAddress(shippingAddressRequest.getAddress());
        shippingAddress.setZipCode(shippingAddressRequest.getZipCode());
        shippingAddress.setCity(shippingAddressRequest.getCity());
        shippingAddress.setCountry(shippingAddressRequest.getCountry());
        shippingAddress.setPhone(shippingAddressRequest.getPhoneNumber());

        ShippingAddress newShippingAddress = shippingAddressRepository.save(shippingAddress);
        return newShippingAddress.getId();
    }

    @IsUser
    @Override
    public Long updateUserShippingAddress(Long shippingAddressId, ShippingAddressRequest shippingAddressRequest) {
        validatePhoneNumber(shippingAddressRequest.getPhoneNumber());

        ShippingAddress shippingAddress = getUserShippingAddressById(shippingAddressId);
        if (shippingAddressRequest.getFirstName() != null && !shippingAddressRequest.getFirstName().isEmpty()) {
            shippingAddress.setFirstName(shippingAddressRequest.getFirstName());
        }
        if (shippingAddressRequest.getLastName() != null && !shippingAddressRequest.getLastName().isEmpty()) {
            shippingAddress.setLastName(shippingAddressRequest.getLastName());
        }
        shippingAddress.setAddress(shippingAddressRequest.getAddress());
        shippingAddress.setZipCode(shippingAddressRequest.getZipCode());
        shippingAddress.setCity(shippingAddressRequest.getCity());
        shippingAddress.setCountry(shippingAddressRequest.getCountry());
        shippingAddress.setPhone(shippingAddressRequest.getPhoneNumber());

        ShippingAddress updatedShippingAddress = shippingAddressRepository.save(shippingAddress);
        return updatedShippingAddress.getId();
    }

    private ShippingAddressRequest validateFirstAndLastName(Long userId, ShippingAddressRequest shippingAddressRequest) {
        if (shippingAddressRequest.getUseFirstAndLastNameFromUser()) {
            User user = userService.getUserById(userId);
            shippingAddressRequest.setFirstName(user.getFirstName());
            shippingAddressRequest.setLastName(user.getLastName());
        } else if (shippingAddressRequest.getFirstName() == null || shippingAddressRequest.getFirstName().isEmpty()) {
            throw new ValueMustNotBeNullOrEmptyException("firstName", "You must provide first name for shipping address.");
        } else if (shippingAddressRequest.getLastName() == null || shippingAddressRequest.getLastName().isEmpty()) {
            throw new ValueMustNotBeNullOrEmptyException("lastName", "You must provide last name for shipping address.");
        }
        return shippingAddressRequest;
    }

    private void validatePhoneNumber(String number) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, null);
            if (!phoneNumberUtil.isValidNumber(phoneNumber)) {
                LOGGER.error("this phoneNumber {} is invalid", phoneNumber);
                throw new InvalidPhoneNumberException(number, "INVALID", "The phone number is not valid.");
            }
        } catch (NumberParseException e) {
            LOGGER.error("Exception", e);
            throw new InvalidPhoneNumberException(number, e.getErrorType().name(), "Could not parse phone number.");
        }
    }

}
