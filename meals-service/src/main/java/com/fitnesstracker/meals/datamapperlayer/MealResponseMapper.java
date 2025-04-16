package com.fitnesstracker.meals.datamapperlayer;

import com.fitnesstracker.meals.dataaccesslayer.Meal;
import com.fitnesstracker.meals.presentationlayer.MealController;
import com.fitnesstracker.meals.presentationlayer.MealResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface MealResponseMapper {
    @Mapping(source = "mealIdentifier.mealId", target = "mealId")
    //@Mapping(source = "userIdentifier.userId", target = "userId")
    @Mapping(source = "mealName", target = "mealName")
    @Mapping(source = "calories", target = "calories")
    @Mapping(source = "mealDate", target = "mealDate")
    @Mapping(source = "mealType", target = "mealType")
    MealResponseModel entityToResponseModel(Meal meal);

    List<MealResponseModel> entityToResponseModelList(List<Meal> meals);

    @AfterMapping()
    default void addLinks(@MappingTarget MealResponseModel mealResponseModel, Meal meal) {
        Link selfLink = linkTo(methodOn(MealController.class)
                .getMeal(mealResponseModel.getMealId()))
                .withSelfRel();
        mealResponseModel.add(selfLink);

        Link allMealsLink = linkTo(methodOn(MealController.class)
                .getMeals())
                .withRel("allMealsForUser");
        mealResponseModel.add(allMealsLink);
    }
}
