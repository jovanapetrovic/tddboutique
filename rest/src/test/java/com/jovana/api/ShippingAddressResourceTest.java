package com.jovana.api;

import com.jovana.entity.PathConstants;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.service.impl.shippingaddress.ShippingAddressService;
import com.jovana.service.impl.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Created by jovana on 01.04.2020
 */
public class ShippingAddressResourceTest extends AbstractTest {

    private static final String TEST_USER_ID = "10";

    private static final String SHIPPING_ADDRESS_ADD_PATH = PathConstants.API
            + PathConstants.SHIPPING_ADDRESS_ADD.replace(PathConstants.PH_USER_ID, TEST_USER_ID);

    private static final String SHIPPING_ADDRESS_ADD_RESPONSE_WHEN_PARAMS_ARE_NULL = "shippingAddressResponseWhenParamsAreNull.json";
    private static final String SHIPPING_ADDRESS_ADD_RESPONSE_WHEN_PARAMS_SIZE_IS_WRONG = "shippingAddressResponseWhenParamsSizeIsWrong.json";

    @DisplayName("Add shipping address successfully when request is valid")
    @Test
    public void testAddShippingAddressSuccess() throws Exception {
        ShippingAddressRequest shippingAddressRequest = new ShippingAddressRequest();
        shippingAddressRequest.setUseFirstAndLastNameFromUser(false);
        shippingAddressRequest.setFirstName("John");
        shippingAddressRequest.setLastName("Doe");
        shippingAddressRequest.setAddress("Pobedina 1");
        shippingAddressRequest.setZipCode(18000L);
        shippingAddressRequest.setCity("Nis");
        shippingAddressRequest.setCountry("Serbia");
        shippingAddressRequest.setPhoneNumber("+38164123456");

        performSimplePost(
                SHIPPING_ADDRESS_ADD_PATH,
                shippingAddressRequest,
                MockMvcResultMatchers.status().isCreated());
    }

    @DisplayName("Add shipping address fails when request params are null")
    @Test
    public void testAddShippingAddressFailsWhenParamsAreNull() throws Exception {
        ShippingAddressRequest shippingAddressRequest = new ShippingAddressRequest();

        performPostAndExpectResponse(
                SHIPPING_ADDRESS_ADD_PATH,
                shippingAddressRequest,
                SHIPPING_ADDRESS_ADD_RESPONSE_WHEN_PARAMS_ARE_NULL,
                MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Add shipping address fails when request params size is wrong")
    @Test
    public void testAddShippingAddressFailsWhenParamsSizeIsWrong() throws Exception {
        ShippingAddressRequest shippingAddressRequest = new ShippingAddressRequest();
        shippingAddressRequest.setUseFirstAndLastNameFromUser(false);
        shippingAddressRequest.setFirstName("John");
        shippingAddressRequest.setLastName("Doe");
        shippingAddressRequest.setAddress("P 1");
        shippingAddressRequest.setZipCode(0L);
        shippingAddressRequest.setCity("N");
        shippingAddressRequest.setCountry("S");
        shippingAddressRequest.setPhoneNumber("+38164123456");

        performPostAndExpectResponse(
                SHIPPING_ADDRESS_ADD_PATH,
                shippingAddressRequest,
                SHIPPING_ADDRESS_ADD_RESPONSE_WHEN_PARAMS_SIZE_IS_WRONG,
                MockMvcResultMatchers.status().isBadRequest());
    }

}
