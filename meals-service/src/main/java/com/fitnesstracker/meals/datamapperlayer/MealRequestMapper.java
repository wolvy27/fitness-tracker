package com.fitnesstracker.meals.datamapperlayer;


import com.fitnesstracker.meals.dataaccesslayer.Meal;
import com.fitnesstracker.meals.presentationlayer.MealRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MealRequestMapper {
    //@Mapping(source = "userId", target = "userIdentifier.userId")
    @Mapping(source = "mealName", target = "mealName")
    @Mapping(source = "calories", target = "calories")
    @Mapping(source = "mealDate", target = "mealDate")
    @Mapping(source = "mealType", target = "mealType")
    Meal requestModelToEntity(MealRequestModel mealRequestModel);
}
