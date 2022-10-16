package itmo.soa3.validation;


import itmo.soa3.dto.LocationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationDtoValidator implements ConstraintValidator<LocationAnnotation, LocationDto> {
    public void initialize(LocationAnnotation constraintAnnotation) {
    }

    /**
     * Validate zipcode and city depending on the country
     */
    public boolean isValid(LocationDto object, ConstraintValidatorContext context) {
        if (object == null) {
            throw new IllegalArgumentException("@LocationAnnotation only applies to LocationDto objects");
        }
        LocationDto locationDto = (LocationDto) object;
        if (locationDto.getId() != null){
            return true;
        }else{
            return locationDto.getY() != null && locationDto.getZ() != null;
        }
    }
}