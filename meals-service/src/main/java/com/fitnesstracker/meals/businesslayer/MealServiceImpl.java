package com.fitnesstracker.meals.businesslayer;


import com.fitnesstracker.meals.dataaccesslayer.Meal;
import com.fitnesstracker.meals.dataaccesslayer.MealIdentifier;
import com.fitnesstracker.meals.dataaccesslayer.MealRepository;
import com.fitnesstracker.meals.dataaccesslayer.MealType;
import com.fitnesstracker.meals.datamapperlayer.MealRequestMapper;
import com.fitnesstracker.meals.datamapperlayer.MealResponseMapper;
import com.fitnesstracker.meals.presentationlayer.MealRequestModel;
import com.fitnesstracker.meals.presentationlayer.MealResponseModel;
import com.fitnesstracker.meals.utils.exceptions.InvalidCaloriesException;
import com.fitnesstracker.meals.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MealServiceImpl implements MealService {
    private MealRepository mealRepository;
    private MealResponseMapper mealResponseMapper;
    private MealRequestMapper mealRequestMapper;
    //private UserRepository userRepository;
    //private DailyLogRepository dailyLogRepository;

    public MealServiceImpl(MealRepository mealRepository, MealResponseMapper mealResponseMapper, MealRequestMapper mealRequestMapper) {
        this.mealRepository = mealRepository;
        this.mealResponseMapper = mealResponseMapper;
        this.mealRequestMapper = mealRequestMapper;
        //this.userRepository = userRepository;
        //this.dailyLogRepository = dailyLogRepository;
    }

    @Override
    public List<MealResponseModel> getMeals() {
        /*
        User foundUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }

         */
        List<Meal> meals = mealRepository.findAll();
        List<Meal> filteredMeals = new ArrayList<>();

        for (Meal meal : meals) {
            if (meal.getMealType() != MealType.NONE) {
                filteredMeals.add(meal);
            }
        }
        return mealResponseMapper.entityToResponseModelList(filteredMeals);
    }

    @Override
    public MealResponseModel getMealByMealId(String mealId) {
        //Meal foundMeal = mealRepository.findMealByMealIdentifier_MealIdAndUserIdentifier_UserId(mealId, userId);
        Meal foundMeal = mealRepository.findMealByMealIdentifier_MealId(mealId);
        if (foundMeal == null || foundMeal.getMealType() == MealType.NONE) {
            throw new NotFoundException("MealId not found: " + mealId);
        }
        /*
        User foundUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }

         */

        return mealResponseMapper.entityToResponseModel(foundMeal);
    }

    @Override
    public MealResponseModel addMeal(MealRequestModel newMealData) {
        /*
        User foundUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }

         */
        if (newMealData.getCalories() < 0) {
            throw new InvalidCaloriesException("Invalid Calories: " + newMealData.getCalories());
        }
        Meal meal = mealRequestMapper.requestModelToEntity(newMealData);
        meal.setMealIdentifier(new MealIdentifier());
        //meal.setUserIdentifier(foundUser.getUserIdentifier());
        Meal savedMeal = mealRepository.save(meal);
        /*
        DailyLog dailyLog = dailyLogRepository.findDailyLogByLogDateAndUserIdentifier_UserId(savedMeal.getMealDate(), userId);
        if (dailyLog != null) {
            dailyLog.getMealIdentifiers().add(savedMeal.getMealIdentifier());
            dailyLogRepository.save(dailyLog);
        }

         */

        return mealResponseMapper.entityToResponseModel(savedMeal);
    }

    @Override
    public MealResponseModel updateMeal(MealRequestModel newMealData, String mealId) {
        //Meal foundMeal = mealRepository.findMealByMealIdentifier_MealIdAndUserIdentifier_UserId(mealId, userId);
        Meal foundMeal = mealRepository.findMealByMealIdentifier_MealId(mealId);
        if (foundMeal == null || foundMeal.getMealType() == MealType.NONE) {
            throw new NotFoundException("MealId not found: " + mealId);
        }
        if (newMealData.getCalories() < 0) {
            throw new InvalidCaloriesException("Invalid Calories: " + newMealData.getCalories());
        }
        /*
        User foundUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }

         */
        Meal updatedMeal = mealRequestMapper.requestModelToEntity(newMealData);
        updatedMeal.setMealIdentifier(foundMeal.getMealIdentifier());
        //updatedMeal.setUserIdentifier(foundMeal.getUserIdentifier());
        updatedMeal.setId(foundMeal.getId());
        /*
        DailyLog dailyLog = dailyLogRepository.findDailyLogByLogDateAndUserIdentifier_UserId(updatedMeal.getMealDate(), userId);
        if (dailyLog != null) {
            dailyLogRepository.save(dailyLog);
        }

         */

        Meal savedMeal = mealRepository.save(updatedMeal);
        return mealResponseMapper.entityToResponseModel(savedMeal);
    }

    @Override
    public void deleteMeal(String mealId) {
        //Meal foundMeal = mealRepository.findMealByMealIdentifier_MealIdAndUserIdentifier_UserId(mealId, userId);
        Meal foundMeal = mealRepository.findMealByMealIdentifier_MealId(mealId);
        if (foundMeal == null || foundMeal.getMealType() == MealType.NONE) {
            throw new NotFoundException("MealId not found: " + mealId);
        }
        /*
        User foundUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }

         */
        /*
        DailyLog dailyLog = dailyLogRepository.findDailyLogByLogDateAndUserIdentifier_UserId(foundMeal.getMealDate(), userId);
        if (dailyLog != null) {
            List<MealIdentifier> mealIdentifiers = dailyLog.getMealIdentifiers();
            for (int i = 0; i < mealIdentifiers.size(); i++) {
                if (mealIdentifiers.get(i).getMealId().equals(foundMeal.getMealIdentifier().getMealId())) {
                    mealIdentifiers.remove(i);
                    break;
                }
            }
            dailyLogRepository.save(dailyLog);
        }

         */
        Meal savedMeal = mealRepository.save(foundMeal);
    }

    @Override
    public String getMealNameByMealId(String mealId) {
        Meal meal = mealRepository.findMealByMealIdentifier_MealId(mealId);

        if (meal == null) {
            throw new NotFoundException("MealId not found: " + mealId);
        }

        return meal.getMealName();
    }

    @Override
    public Integer getCaloriesByMealId(String mealId) {
        Meal meal = mealRepository.findMealByMealIdentifier_MealId(mealId);

        if (meal == null) {
            throw new NotFoundException("MealId not found: " + mealId);
        }

        return meal.getCalories();
    }



    @Override
    public MealType getMealTypeByMealId(String mealId) {
        Meal meal = mealRepository.findMealByMealIdentifier_MealId(mealId);

        if (meal == null) {
            throw new NotFoundException("MealId not found: " + mealId);
        }

        return meal.getMealType();
    }




}
