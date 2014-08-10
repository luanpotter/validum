package xyz.luan.sabv.customs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import xyz.luan.sabv.Validation;
import xyz.luan.sabv.entities.Address;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.TYPE_USE })
@Validation(Address.class)
public @interface ValidAddress {

    public static class Validator implements xyz.luan.sabv.Validator<Address, ValidAddress> {

        @Override
        public List<String> validate(Address address, ValidAddress annotation) {
            boolean invalid = address != null && (address.getNumber() == 404 || "Bad Street".equals(address.getStreet()));
            return invalid ? Arrays.asList("xyz.luan.sabv.ValidAddress.invalidAddress") : Collections.emptyList();
        }
        
    }
}
